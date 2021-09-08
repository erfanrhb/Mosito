package rahbari.erfan.mosito.ui.adapters.diffUtil;

import androidx.recyclerview.widget.DiffUtil;

import rahbari.erfan.mosito.models.Artist;

public class ArtistLibraryDiffUtil extends DiffUtil.ItemCallback<Artist> {

    @Override
    public boolean areItemsTheSame(Artist oldItem, Artist newItem) {
        return oldItem.getId() == newItem.getId();
    }

    @Override
    public boolean areContentsTheSame(Artist oldItem, Artist newItem) {
        return oldItem.getId() == newItem.getId();
    }
}
