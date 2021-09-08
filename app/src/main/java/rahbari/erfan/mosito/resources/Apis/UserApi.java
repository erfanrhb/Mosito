package rahbari.erfan.mosito.resources.Apis;

import rahbari.erfan.mosito.models.User;
import rahbari.erfan.mosito.resources.BaseRetrofit;
import io.reactivex.Observable;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public class UserApi extends BaseRetrofit implements UserInterface {

    @Override
    public Observable<User> index() {
        return create(UserInterface.class).index();
    }

    @Override
    public Observable<User> login(int register, String name, String email, String password) {
        return create(UserInterface.class).login(register, name, email, password);
    }

    @Override
    public Observable<String> logout() {
        return create(UserInterface.class).logout();
    }
}

interface UserInterface {
    @GET("auth")
    Observable<User> index();

    @POST("auth")
    @FormUrlEncoded
    Observable<User> login(@Field("register") int register, @Field("name") String name, @Field("email") String email, @Field("password") String password);

    @DELETE("auth/me")
    Observable<String> logout();
}
