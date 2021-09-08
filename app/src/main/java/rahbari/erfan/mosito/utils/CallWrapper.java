package rahbari.erfan.mosito.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Objects;

import rahbari.erfan.mosito.models.ErrorHandler;
import io.reactivex.observers.DisposableObserver;
import retrofit2.HttpException;

public abstract class CallWrapper<T> extends DisposableObserver<T> {
    protected abstract void onSuccess(T t);

    protected abstract void onError(int status, ErrorHandler response);

    @Override
    public void onNext(T t) {
        try {
            onSuccess(t);
        } catch (Exception ex) {
            onError(ex);
        }
    }

    @Override
    public void onError(Throwable e) {
        try {
            if (e instanceof HttpException) {
                HttpException httpException = ((HttpException) e);
                ErrorHandler errorHandler = errorParse(Objects.requireNonNull(Objects.requireNonNull(httpException.response()).errorBody()).string());
                onError(httpException.code(), errorHandler);
            } else if (e instanceof JsonSyntaxException) {
                JsonSyntaxException httpException = ((JsonSyntaxException) e);
                onError(0, new ErrorHandler(httpException.getMessage()));
            } else if (e instanceof SocketTimeoutException) {
                onError(0, new ErrorHandler("Request timeout"));
            } else if (e instanceof IOException) {
                onError(0, new ErrorHandler("Network error"));
            } else {
                onError(0, new ErrorHandler(e.getMessage()));
            }
        } catch (Exception err) {
            onError(0, new ErrorHandler(err.getMessage()));
        }
    }

    @Override
    public void onComplete() {

    }

    private ErrorHandler errorParse(String json) {
        try {
            Gson gson = new Gson();
            ErrorHandler apiError = gson.fromJson(json, new TypeToken<ErrorHandler>() {
            }.getType());
            if (apiError != null) Log.e("CallWrapper", apiError.getMessage());
            return apiError;
        } catch (Exception e) {
            Log.e("CallWrapper", "errorParse", e);
        }
        return null;
    }
}
