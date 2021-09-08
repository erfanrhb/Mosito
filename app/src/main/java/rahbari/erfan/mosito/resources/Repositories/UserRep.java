package rahbari.erfan.mosito.resources.Repositories;

import android.content.Intent;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;

import rahbari.erfan.mosito.Application;
import rahbari.erfan.mosito.interfaces.Response;
import rahbari.erfan.mosito.models.ErrorHandler;
import rahbari.erfan.mosito.models.User;
import rahbari.erfan.mosito.resources.Apis.UserApi;
import rahbari.erfan.mosito.resources.BaseRoom;
import rahbari.erfan.mosito.resources.Daos.UserDao;
import rahbari.erfan.mosito.utils.CallWrapper;
import rahbari.erfan.mosito.utils.Utils;
import io.reactivex.schedulers.Schedulers;

public class UserRep {
    private UserApi api;
    private UserDao dao;

    public UserRep() {
        api = new UserApi();
        dao = BaseRoom.getDatabase(Application.getInstance()).userDao();
    }

    public LiveData<User> userLiveData() {
        return dao.live();
    }

    public User user() {
        return dao.user();
    }

    private void updateOrCreate(User user) {
        if (dao.count(user.getId()) > 0)
            dao.update(user);
        else
            dao.insert(user);
    }

    private void clear() {
        List<User> users = dao.users();
        for (User x : users) {
            dao.delete(x.getId());
        }
    }

    public void store(String name, String email, String password, int register, Response<User> userResponse, Response<ErrorHandler> handlerResponse, Response<Boolean> doneResponse) {
        api.login(register, name, email, password).subscribeOn(Schedulers.io()).subscribe(new CallWrapper<User>() {
            @Override
            protected void onSuccess(User user) {
                updateOrCreate(user);
                if (user.getToken() != null) {
                    Application.api_token = user.getToken();
                    Utils.setShare("token", user.getToken());
                }
                userResponse.response(user);
                doneResponse.response(true);
                Application.getInstance().sendBroadcast(new Intent("SYNC_SERVICE"));
            }

            @Override
            protected void onError(int status, ErrorHandler response) {
                handlerResponse.response(response);
                doneResponse.response(false);
            }
        });
    }

    public void logout() {
        api.logout().subscribeOn(Schedulers.io()).subscribe(new CallWrapper<String>() {
            @Override
            protected void onSuccess(String string) {
                clear();
                Application.api_token = null;
                Utils.setShare("token", null);
            }

            @Override
            protected void onError(int status, ErrorHandler response) {
                Log.e("logout", response.getMessage());
                clear();
                Application.api_token = null;
                Utils.setShare("token", null);
                Utils.setShare("synced_at", null);
            }
        });
    }

    public void index(Response<User> userResponse, Response<ErrorHandler> handlerResponse) {
        api.index().subscribeOn(Schedulers.io()).subscribe(new CallWrapper<User>() {
            @Override
            protected void onSuccess(User user) {
                updateOrCreate(user);
                userResponse.response(user);
            }

            @Override
            protected void onError(int status, ErrorHandler response) {
                handlerResponse.response(response);
            }
        });
    }
}
