package rahbari.erfan.mosito.ui.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedListAdapter;

import java.util.Objects;

import rahbari.erfan.mosito.R;
import rahbari.erfan.mosito.databinding.AdapterAlbumHorizontalBinding;
import rahbari.erfan.mosito.interfaces.Response;
import rahbari.erfan.mosito.models.Album;
import rahbari.erfan.mosito.models.AlbumLibrary;
import rahbari.erfan.mosito.ui.adapters.diffUtil.AlbumLibraryDiffUtil;
import rahbari.erfan.mosito.utils.PaginateViewHolder;

public class AlbumHorizontalPaginateAdapter extends PagedListAdapter<AlbumLibrary, PaginateViewHolder<AdapterAlbumHorizontalBinding>> {
    private Response<Album> clicked;

    public AlbumHorizontalPaginateAdapter() {
        super(new AlbumLibraryDiffUtil());
        setHasStableIds(true);
    }

    public void setClicked(Response<Album> clicked) {
        this.clicked = clicked;
    }

    @NonNull
    @Override
    public PaginateViewHolder<AdapterAlbumHorizontalBinding> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PaginateViewHolder<>(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.adapter_album_horizontal, parent, false));
    }

    @Override
    public long getItemId(int position) {
        return Objects.requireNonNull(getItem(position)).getId();
    }

    @Override
    public void onBindViewHolder(@NonNull PaginateViewHolder<AdapterAlbumHorizontalBinding> holder, int position) {
        AlbumLibrary item = getItem(position);
        if (item != null) {
            holder.binding.setAlbum(item.album);
            holder.binding.getRoot().setOnClickListener(view -> clicked.response(item.getAlbum()));
        }
    }
}
