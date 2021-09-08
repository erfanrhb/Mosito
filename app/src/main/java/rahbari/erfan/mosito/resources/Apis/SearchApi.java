package rahbari.erfan.mosito.resources.Apis;

import rahbari.erfan.mosito.models.SearchResponse;
import rahbari.erfan.mosito.resources.BaseRetrofit;
import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class SearchApi extends BaseRetrofit implements SearchInterface {

    @Override
    public Observable<SearchResponse> search(String search) {
        return create(SearchInterface.class).search(search);
    }
}

interface SearchInterface {
    @POST("search")
    @FormUrlEncoded
    Observable<SearchResponse> search(@Field("search") String search);
}
