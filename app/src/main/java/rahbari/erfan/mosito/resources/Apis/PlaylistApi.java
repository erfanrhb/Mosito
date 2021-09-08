package rahbari.erfan.mosito.resources.Apis;

import rahbari.erfan.mosito.models.Music;
import rahbari.erfan.mosito.models.Paginate;
import rahbari.erfan.mosito.models.Playlist;
import rahbari.erfan.mosito.resources.BaseRetrofit;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class PlaylistApi extends BaseRetrofit implements PlaylistInterface {
    @Override
    public Observable<Paginate<Playlist>> playlists(int pagination, int page) {
        return create(PlaylistInterface.class).playlists(pagination, page);
    }

    @Override
    public Observable<Paginate<Music>> musics(long playlist_id, int pagination, int page) {
        return create(PlaylistInterface.class).musics(playlist_id, pagination, page);
    }

    @Override
    public Observable<Playlist> store(MultipartBody.Part title, MultipartBody.Part type, MultipartBody.Part description, MultipartBody.Part picture) {
        return create(PlaylistInterface.class).store(title, type, description, picture);
    }
}


interface PlaylistInterface {
    @GET("playlists")
    Observable<Paginate<Playlist>> playlists(@Query("pagination") int pagination, @Query("page") int page);

    @GET("playlists/{playlistId}")
    Observable<Paginate<Music>> musics(@Path("playlistId") long playlist_id, @Query("pagination") int pagination, @Query("page") int page);

    @Multipart
    @POST("playlists")
    Observable<Playlist> store(
            @Part MultipartBody.Part title,
            @Part MultipartBody.Part type,
            @Part MultipartBody.Part description,
            @Part MultipartBody.Part picture
    );
}
