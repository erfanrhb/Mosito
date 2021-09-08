package rahbari.erfan.mosito.resources.Apis;

import rahbari.erfan.mosito.models.Album;
import rahbari.erfan.mosito.models.Paginate;
import rahbari.erfan.mosito.resources.BaseRetrofit;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class AlbumApi extends BaseRetrofit implements AlbumInterface {

    @Override
    public Observable<Paginate<Album>> artistAlbums(int pagination, int page, long artist_id) {
        return create(AlbumInterface.class).artistAlbums(pagination, page, artist_id);
    }

    @Override
    public Observable<Paginate<Album>> albums(int pagination, int page) {
        return create(AlbumInterface.class).albums(pagination, page);
    }


    @Override
    public Observable<Album> album(long albumId) {
        return create(AlbumInterface.class).album(albumId);
    }
}

interface AlbumInterface {
    @GET("albums")
    Observable<Paginate<Album>> artistAlbums(@Query("pagination") int pagination,
                                             @Query("page") int page,
                                             @Query("artist_id") long artist_id);

    @GET("albums")
    Observable<Paginate<Album>> albums(@Query("pagination") int pagination,
                                       @Query("page") int page);

    @GET("albums/{albumId}")
    Observable<Album> album(@Path("albumId") long albumId);
}
