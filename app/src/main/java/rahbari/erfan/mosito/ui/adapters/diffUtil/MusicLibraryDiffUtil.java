package rahbari.erfan.mosito.ui.adapters.diffUtil;

import androidx.recyclerview.widget.DiffUtil;

import java.util.Objects;

import rahbari.erfan.mosito.models.MusicLibrary;

public class MusicLibraryDiffUtil extends DiffUtil.ItemCallback<MusicLibrary> {

    @Override
    public boolean areItemsTheSame(MusicLibrary oldItem, MusicLibrary newItem) {
        return oldItem.music.getId() == newItem.music.getId();
    }

    @Override
    public boolean areContentsTheSame(MusicLibrary oldItem, MusicLibrary newItem) {

        if (oldItem.music.getId() != newItem.music.getId())
            return false;

        if ((newItem.library == null) && (oldItem.library == null))
            return true;

        if ((newItem.library == null) || (oldItem.library == null))
            return false;

        if (Objects.equals(oldItem.library.getDeleted_at(), newItem.library.getDeleted_at()))
            return false;

        return !Objects.equals(oldItem.library.isDownload(), newItem.library.isDownload());
    }

}
