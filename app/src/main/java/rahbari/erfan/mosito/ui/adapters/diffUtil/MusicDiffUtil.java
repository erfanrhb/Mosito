package rahbari.erfan.mosito.ui.adapters.diffUtil;

import androidx.recyclerview.widget.DiffUtil;

import rahbari.erfan.mosito.models.Music;

public class MusicDiffUtil extends DiffUtil.ItemCallback<Music> {

    @Override
    public boolean areItemsTheSame(Music oldItem, Music newItem) {
        return oldItem.getId() == newItem.getId();
    }

    @Override
    public boolean areContentsTheSame(Music oldItem, Music newItem) {
        return oldItem.getId() == newItem.getId();
    }

}
