package rahbari.erfan.mosito.resources;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import rahbari.erfan.mosito.Application;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class BaseRetrofit {
    //    public static final String BASE_DOWN_URL = "https://mosito.ir";
//    public static final String BASE_DOWN_URL = "http://192.168.1.110";
    public static final String BASE_DOWN_URL = "https://" + Application.mosito_domain;
    public static final String BASE_URL = "https://" + Application.mosito_domain + "/api/";

    protected <T> T create(Class<T> clazz) {
        return retrofit().create(clazz);
    }

    protected <T> T download(Class<T> clazz) {
        return retrofitDownload().create(clazz);
    }

    private Retrofit retrofitDownload() {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .build();

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS);

        httpClient.addInterceptor(chain -> {
            Request original = chain.request();

            Request request = original.newBuilder()
                    .header("Authorization", "Bearer " + Application.api_token)
                    .method(original.method(), original.body())
                    .build();
            return chain.proceed(request);
        });

        return new Retrofit.Builder()
                .baseUrl(BASE_URL).client(okHttpClient)
                .client(httpClient.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build();
    }

    private Retrofit retrofit() {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .build();

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson custom = gsonBuilder.setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();

        int cacheSize = 512 * 1024 * 1024; // 512 MB
        Cache cache = new Cache(Application.getInstance().getCacheDir(), cacheSize);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .cache(cache)
                .readTimeout(15, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS);

        httpClient.addInterceptor(chain -> {
            Request original = chain.request();

            Request request = original.newBuilder()
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .header("Authorization", "Bearer " + Application.api_token)
                    .method(original.method(), original.body())
                    .build();

            return chain.proceed(request);
        });

        RxJava2CallAdapterFactory rxAdapter = RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io());

        return new Retrofit.Builder()
                .baseUrl(BASE_URL).client(okHttpClient)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create(custom))
                .addCallAdapterFactory(rxAdapter)
                .build();
    }
}
