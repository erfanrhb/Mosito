package rahbari.erfan.mosito.ui.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedListAdapter;

import java.util.Objects;

import rahbari.erfan.mosito.R;
import rahbari.erfan.mosito.databinding.AdapterPlaylistBinding;
import rahbari.erfan.mosito.interfaces.Response;
import rahbari.erfan.mosito.models.Playlist;
import rahbari.erfan.mosito.models.PlaylistLibrary;
import rahbari.erfan.mosito.ui.adapters.diffUtil.PlaylistLibraryDiffUtil;
import rahbari.erfan.mosito.utils.PaginateViewHolder;

public class PlaylistAdapter extends PagedListAdapter<PlaylistLibrary, PaginateViewHolder<AdapterPlaylistBinding>> {
    private Response<Playlist> clicked;

    public PlaylistAdapter() {
        super(new PlaylistLibraryDiffUtil());
        setHasStableIds(true);
    }

    public void setClicked(Response<Playlist> clicked) {
        this.clicked = clicked;
    }

    @NonNull
    @Override
    public PaginateViewHolder<AdapterPlaylistBinding> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PaginateViewHolder<>(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.adapter_playlist, parent, false));
    }

    @Override
    public long getItemId(int position) {
        return Objects.requireNonNull(getItem(position)).getId();
    }

    @Override
    public void onBindViewHolder(@NonNull PaginateViewHolder<AdapterPlaylistBinding> holder, int position) {
        PlaylistLibrary item = getItem(position);
        if (item != null) holder.binding.setPlaylist(item);
        if (clicked != null)
            holder.binding.getRoot().setOnClickListener(view -> clicked.response(item.playlist));
    }
}
