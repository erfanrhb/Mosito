package rahbari.erfan.mosito.resources.Apis;

import rahbari.erfan.mosito.models.Music;
import rahbari.erfan.mosito.models.Paginate;
import rahbari.erfan.mosito.resources.BaseRetrofit;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class MusicApi extends BaseRetrofit implements MusicInterface {

    public Observable<Paginate<Music>> musicsLatest(int pagination, int page, String order_by) {
        return create(MusicInterface.class).musicsLatest(pagination, page, order_by);
    }

    public Observable<Paginate<Music>> musicsMostPlayed(int pagination, String order_by, int most_played) {
        return create(MusicInterface.class).musicsMostPlayed(pagination, order_by, most_played);
    }

    @Override
    public Observable<Paginate<Music>> musicsArtist(int pagination, int page, String order_by, long artist_id) {
        return create(MusicInterface.class).musicsArtist(pagination, page, order_by, artist_id);
    }

    @Override
    public Observable<Music> music(long musicId) {
        return create(MusicInterface.class).music(musicId);
    }
}

interface MusicInterface {
    @GET("musics")
    Observable<Paginate<Music>> musicsLatest(@Query("pagination") int pagination,
                                             @Query("page") int page,
                                             @Query("order_by") String order_by);

    @GET("musics")
    Observable<Paginate<Music>> musicsMostPlayed(@Query("pagination") int pagination,
                                                 @Query("order_by") String order_by,
                                                 @Query("most_played") int most_played);

    @GET("musics")
    Observable<Paginate<Music>> musicsArtist(@Query("pagination") int pagination,
                                             @Query("page") int page,
                                             @Query("order_by") String order_by,
                                             @Query("artist_id") long artist_id);


    @GET("musics/{musicId}")
    Observable<Music> music(@Path("musicId") long musicId);
}
