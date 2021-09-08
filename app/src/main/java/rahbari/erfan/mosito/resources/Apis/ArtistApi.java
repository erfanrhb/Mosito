package rahbari.erfan.mosito.resources.Apis;

import rahbari.erfan.mosito.models.Artist;
import rahbari.erfan.mosito.resources.BaseRetrofit;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class ArtistApi extends BaseRetrofit implements ArtistInterface {

    @Override
    public Observable<Artist> artist(String artistId) {
        return create(ArtistInterface.class).artist(artistId);
    }
}

interface ArtistInterface {
    @GET("artists/{artistId}")
    Observable<Artist> artist(@Path("artistId") String artistId);
}

