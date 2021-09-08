package rahbari.erfan.mosito.resources.Repositories;

import android.util.Log;

import java.util.List;

import rahbari.erfan.mosito.interfaces.Response;
import rahbari.erfan.mosito.models.ErrorHandler;
import rahbari.erfan.mosito.models.Offer;
import rahbari.erfan.mosito.resources.Apis.OfferApi;
import rahbari.erfan.mosito.utils.CallWrapper;

public class OfferRep {
    private OfferApi api;

    public OfferRep() {
        api = new OfferApi();
    }

    public void offers(Response<List<Offer>> response) {
        api.offers().subscribe(new CallWrapper<List<Offer>>() {
            @Override
            protected void onSuccess(List<Offer> offers) {
                response.response(offers);
            }

            @Override
            protected void onError(int status, ErrorHandler response) {
                Log.e("OfferRep.offers", response.getMessage());
            }
        });
    }
}
