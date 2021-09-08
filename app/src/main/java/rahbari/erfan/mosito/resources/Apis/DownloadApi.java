package rahbari.erfan.mosito.resources.Apis;

import rahbari.erfan.mosito.models.Download;
import rahbari.erfan.mosito.resources.BaseRetrofit;
import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public class DownloadApi extends BaseRetrofit implements DownloadInterface {
    @Override
    public Observable<ResponseBody> download(String link) {
        return download(DownloadInterface.class).download(link);
    }

    @Override
    public Observable<Download> requestFile(long musicId) {
        return create(DownloadInterface.class).requestFile(musicId);
    }
}

interface DownloadInterface {
    @GET
    @Streaming
    Observable<ResponseBody> download(@Url String link);

    @GET("download/{musicId}")
    Observable<Download> requestFile(@Path("musicId") long musicId);
}
