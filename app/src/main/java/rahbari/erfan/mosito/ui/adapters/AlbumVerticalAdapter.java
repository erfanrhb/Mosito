package rahbari.erfan.mosito.ui.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedListAdapter;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;

import java.util.Objects;

import rahbari.erfan.mosito.R;
import rahbari.erfan.mosito.databinding.AdapterVerticalAlbumBinding;
import rahbari.erfan.mosito.interfaces.Response;
import rahbari.erfan.mosito.models.Album;
import rahbari.erfan.mosito.models.AlbumLibrary;
import rahbari.erfan.mosito.ui.adapters.diffUtil.AlbumLibraryDiffUtil;
import rahbari.erfan.mosito.utils.PaginateViewHolder;
import rahbari.erfan.mosito.utils.Utils;

public class AlbumVerticalAdapter extends PagedListAdapter<AlbumLibrary, PaginateViewHolder<AdapterVerticalAlbumBinding>> {
    private Response<Album> clicked;

    public AlbumVerticalAdapter() {
        super(new AlbumLibraryDiffUtil());
        setHasStableIds(true);
    }

    public void setClicked(Response<Album> clicked) {
        this.clicked = clicked;
    }

    @NonNull
    @Override
    public PaginateViewHolder<AdapterVerticalAlbumBinding> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PaginateViewHolder<>(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.adapter_vertical_album, parent, false));
    }

    @Override
    public long getItemId(int position) {
        return Objects.requireNonNull(getItem(position)).getId();
    }

    @Override
    public void onBindViewHolder(@NonNull PaginateViewHolder<AdapterVerticalAlbumBinding> holder, int position) {
        AlbumLibrary item = getItem(position);
        if (item != null) {
            holder.binding.getRoot().setOnClickListener(view -> clicked.response(item.album));
            holder.binding.setAlbum(item.album);

            Context context = holder.itemView.getContext();

            Glide.with(context)
                    .asBitmap()
                    .placeholder(R.drawable.ic_playlist_play)
                    .load(Utils.pictureUrl(item.album.getCover()))
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Bitmap> transition) {
                            holder.binding.imgPicture.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            holder.binding.imgPicture.setImageBitmap(resource);

                            Palette.from(resource).generate(palette -> {
                                if (palette != null) {
                                    int accent = palette.getDarkVibrantColor(ContextCompat.getColor(context, R.color.DarkColorAccent));
                                    int primary = palette.getDarkMutedColor(ContextCompat.getColor(context, R.color.DarkColorPrimary));

                                    GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BL_TR, new int[]{primary, accent});
                                    gradientDrawable.setCornerRadius(0f);

                                    holder.binding.rootView.setBackground(gradientDrawable);
                                }
                            });
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                            holder.binding.imgPicture.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            holder.binding.imgPicture.setImageDrawable(placeholder);
                        }
                    });
        }
    }
}
