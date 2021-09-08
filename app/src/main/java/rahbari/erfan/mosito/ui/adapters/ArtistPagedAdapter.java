package rahbari.erfan.mosito.ui.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedListAdapter;

import java.util.Objects;

import rahbari.erfan.mosito.R;
import rahbari.erfan.mosito.databinding.AdapterArtistBinding;
import rahbari.erfan.mosito.interfaces.Response;
import rahbari.erfan.mosito.models.Artist;
import rahbari.erfan.mosito.ui.adapters.diffUtil.ArtistLibraryDiffUtil;
import rahbari.erfan.mosito.utils.PaginateViewHolder;

public class ArtistPagedAdapter extends PagedListAdapter<Artist, PaginateViewHolder<AdapterArtistBinding>> {
        private Response<Artist> clicked;

        public ArtistPagedAdapter() {
            super(new ArtistLibraryDiffUtil());
            setHasStableIds(true);
        }

        public void setClicked(Response<Artist> clicked) {
            this.clicked = clicked;
        }

        @NonNull
        @Override
        public PaginateViewHolder<AdapterArtistBinding> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new PaginateViewHolder<>(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.adapter_artist, parent, false));
        }

        @Override
        public long getItemId(int position) {
            return Objects.requireNonNull(getItem(position)).getId();
        }

        @Override
        public void onBindViewHolder(@NonNull PaginateViewHolder<AdapterArtistBinding> holder, int position) {
            Artist a = getItem(position);
            holder.binding.setArtist(a);
            holder.binding.getRoot().setOnClickListener(view -> clicked.response(a));
        }
    }
