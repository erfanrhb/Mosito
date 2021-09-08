package rahbari.erfan.mosito.ui.adapters;

import android.app.Activity;

import rahbari.erfan.mosito.R;
import rahbari.erfan.mosito.databinding.AdapterArtistBinding;
import rahbari.erfan.mosito.interfaces.Response;
import rahbari.erfan.mosito.models.Artist;
import rahbari.erfan.mosito.utils.AdapterUtil;

public class ArtistAdapter extends AdapterUtil<Artist, AdapterArtistBinding> {
    private Response<Artist> onClick;

    public ArtistAdapter(Activity activity) {
        super(activity, R.layout.adapter_artist);
    }

    @Override
    public void bindViewHolder(AdapterArtistBinding binding, Artist item, int position) {
        binding.setArtist(item);
        binding.getRoot().setOnClickListener(v -> {
            if (onClick != null) onClick.response(item);
        });
    }

    public void setOnClick(Response<Artist> onClick) {
        this.onClick = onClick;
    }
}
