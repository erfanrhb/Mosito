package rahbari.erfan.mosito.ui.adapters.diffUtil;

import androidx.recyclerview.widget.DiffUtil;

import rahbari.erfan.mosito.models.AlbumLibrary;

public class AlbumLibraryDiffUtil extends DiffUtil.ItemCallback<AlbumLibrary> {

    @Override
    public boolean areItemsTheSame(AlbumLibrary oldItem, AlbumLibrary newItem) {
        return oldItem.album.getId() == newItem.album.getId();
    }

    @Override
    public boolean areContentsTheSame(AlbumLibrary oldItem, AlbumLibrary newItem) {
        return oldItem.album.getId() == newItem.album.getId()
                && (newItem.library != null) == (oldItem.library != null);
    }
}
