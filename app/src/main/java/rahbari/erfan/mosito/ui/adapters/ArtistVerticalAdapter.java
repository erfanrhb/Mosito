package rahbari.erfan.mosito.ui.adapters;

import android.app.Activity;

import rahbari.erfan.mosito.R;
import rahbari.erfan.mosito.databinding.AdapterArtistsVerticalBinding;
import rahbari.erfan.mosito.interfaces.Response;
import rahbari.erfan.mosito.models.Artist;
import rahbari.erfan.mosito.utils.AdapterUtil;

public class ArtistVerticalAdapter extends AdapterUtil<Artist, AdapterArtistsVerticalBinding> {
    private Response<Artist> onClick;

    public ArtistVerticalAdapter(Activity activity) {
        super(activity, R.layout.adapter_artists_vertical);
    }

    @Override
    public void bindViewHolder(AdapterArtistsVerticalBinding binding, Artist item, int position) {
        binding.setArtist(item);
        binding.getRoot().setOnClickListener(v -> {
            if (onClick != null) onClick.response(item);
        });
    }

    public void setOnClick(Response<Artist> onClick) {
        this.onClick = onClick;
    }
}
