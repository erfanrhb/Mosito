package rahbari.erfan.mosito.ui.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedListAdapter;

import rahbari.erfan.mosito.R;
import rahbari.erfan.mosito.models.MusicLibrary;
import rahbari.erfan.mosito.utils.MositoUtils;
import rahbari.erfan.mosito.utils.PaginateViewHolder;
import rahbari.erfan.mosito.databinding.AdapterMusicVerticalBinding;
import rahbari.erfan.mosito.ui.adapters.diffUtil.MusicLibraryDiffUtil;

public class MusicVerticalAdapter extends PagedListAdapter<MusicLibrary, PaginateViewHolder<AdapterMusicVerticalBinding>> {

    public MusicVerticalAdapter() {
        super(new MusicLibraryDiffUtil());
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public PaginateViewHolder<AdapterMusicVerticalBinding> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PaginateViewHolder<>(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.adapter_music_vertical, parent, false));
    }

    @Override
    public long getItemId(int position) {
        MusicLibrary x = getItem(position);
        return x != null ? x.getId() : 0;
    }

    @Override
    public void onBindViewHolder(@NonNull PaginateViewHolder<AdapterMusicVerticalBinding> holder, int position) {
        MusicLibrary item = getItem(position);
        holder.binding.setMusicLibrary(item);
        if (item != null && item.getMusic() != null) {
            holder.binding.rootLinear.setOnClickListener(v -> MositoUtils.play(getCurrentList(), item.getMusic()));
            holder.binding.btnAction.setOnClickListener(v -> {

                if (item.library == null || item.library.getDeleted_at() != null) {
                    MositoUtils.addLibraryMusic(item.getMusic());
                } else {
                    if (item.library.isDownload()) {
                        // noting
                    } else {
                        MositoUtils.addToDownloadQueue(item.getMusic());
                    }
                }

            });
        }
    }
}
