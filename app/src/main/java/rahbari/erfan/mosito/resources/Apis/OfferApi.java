package rahbari.erfan.mosito.resources.Apis;

import java.util.List;

import rahbari.erfan.mosito.models.Offer;
import rahbari.erfan.mosito.resources.BaseRetrofit;
import io.reactivex.Observable;
import retrofit2.http.GET;

public class OfferApi extends BaseRetrofit implements OfferInterface {
    @Override
    public Observable<List<Offer>> offers() {
        return create(OfferInterface.class).offers();
    }
}

interface OfferInterface {
    @GET("offers")
    Observable<List<Offer>> offers();
}
