package rahbari.erfan.mosito.ui.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedListAdapter;

import rahbari.erfan.mosito.R;
import rahbari.erfan.mosito.databinding.AdapterMusicCartBinding;
import rahbari.erfan.mosito.models.Music;
import rahbari.erfan.mosito.ui.adapters.diffUtil.MusicDiffUtil;
import rahbari.erfan.mosito.utils.PaginateViewHolder;

public class MusicCartAdapter extends PagedListAdapter<Music, PaginateViewHolder<AdapterMusicCartBinding>> {

    public MusicCartAdapter() {
        super(new MusicDiffUtil());
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public PaginateViewHolder<AdapterMusicCartBinding> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PaginateViewHolder<>(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.adapter_music_cart, parent, false));
    }

    @Override
    public long getItemId(int position) {
        Music x = getItem(position);
        return x != null ? x.getId() : 0;
    }

    @Override
    public void onBindViewHolder(@NonNull PaginateViewHolder<AdapterMusicCartBinding> holder, int position) {
        holder.binding.setMusic(getItem(position));
    }
}
