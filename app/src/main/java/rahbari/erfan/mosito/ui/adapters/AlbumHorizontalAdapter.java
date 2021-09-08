package rahbari.erfan.mosito.ui.adapters;

import android.app.Activity;

import rahbari.erfan.mosito.R;
import rahbari.erfan.mosito.databinding.AdapterAlbumHorizontalBinding;
import rahbari.erfan.mosito.interfaces.Response;
import rahbari.erfan.mosito.models.Album;
import rahbari.erfan.mosito.models.AlbumLibrary;
import rahbari.erfan.mosito.utils.AdapterUtil;

public class AlbumHorizontalAdapter extends AdapterUtil<AlbumLibrary, AdapterAlbumHorizontalBinding> {
    private Response<Album> clicked;

    public AlbumHorizontalAdapter(Activity activity) {
        super(activity, R.layout.adapter_album_horizontal);
    }

    public void setClicked(Response<Album> clicked) {
        this.clicked = clicked;
    }

    @Override
    public void bindViewHolder(AdapterAlbumHorizontalBinding binding, AlbumLibrary item, int position) {
        binding.setAlbum(item.album);
        binding.getRoot().setOnClickListener(v -> {
            if (clicked != null) {
                clicked.response(item.album);
            }
        });
    }
}
