package rahbari.erfan.mosito.ui.adapters.diffUtil;

import androidx.recyclerview.widget.DiffUtil;

import rahbari.erfan.mosito.models.PlaylistLibrary;

public class PlaylistLibraryDiffUtil extends DiffUtil.ItemCallback<PlaylistLibrary> {

    @Override
    public boolean areItemsTheSame(PlaylistLibrary oldItem, PlaylistLibrary newItem) {
        return oldItem.playlist.getId() == newItem.playlist.getId();
    }

    @Override
    public boolean areContentsTheSame(PlaylistLibrary oldItem, PlaylistLibrary newItem) {
        return oldItem.playlist.getId() == newItem.playlist.getId()
                && (newItem.library != null) == (oldItem.library != null);
    }
}
