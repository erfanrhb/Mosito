package rahbari.erfan.mosito.resources.Apis;

import rahbari.erfan.mosito.models.Album;
import rahbari.erfan.mosito.models.Music;
import rahbari.erfan.mosito.models.Paginate;
import rahbari.erfan.mosito.models.Playlist;
import rahbari.erfan.mosito.resources.BaseRetrofit;
import rahbari.erfan.mosito.resources.models.ListIds;
import rahbari.erfan.mosito.resources.models.SyncRequest;
import rahbari.erfan.mosito.resources.models.SyncResponse;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public class LibraryApi extends BaseRetrofit implements LibraryInterface {

    @Override
    public Observable<SyncResponse> index(SyncRequest request) {
        return create(LibraryInterface.class).index(request);
    }

    @Override
    public Observable<Paginate<Music>> musics(ListIds ids) {
        return create(LibraryInterface.class).musics(ids);
    }

    @Override
    public Observable<Paginate<Album>> albums(ListIds ids) {
        return create(LibraryInterface.class).albums(ids);
    }

    @Override
    public Observable<Paginate<Playlist>> playlists(ListIds ids) {
        return create(LibraryInterface.class).playlists(ids);
    }
}

interface LibraryInterface {
    @POST("libraries")
    Observable<SyncResponse> index(@Body SyncRequest request);

    @PUT("libraries/musics")
    Observable<Paginate<Music>> musics(@Body ListIds ids);

    @PUT("libraries/albums")
    Observable<Paginate<Album>> albums(@Body ListIds ids);

    @PUT("libraries/playlists")
    Observable<Paginate<Playlist>> playlists(@Body ListIds ids);
}
