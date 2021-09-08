package rahbari.erfan.mosito.ui.artists;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.google.android.material.tabs.TabLayout;

import rahbari.erfan.mosito.R;
import rahbari.erfan.mosito.databinding.FragmentArtistBinding;
import rahbari.erfan.mosito.models.Artist;
import rahbari.erfan.mosito.utils.FragmentUtil;
import rahbari.erfan.mosito.utils.TabAdapterUtil;

public class ArtistFragment extends FragmentUtil<FragmentArtistBinding> {
    private static final String ARTIST = "artist";

    private Artist artist;

    public static ArtistFragment newInstance(Artist artist) {
        ArtistFragment fragment = new ArtistFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARTIST, artist);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            artist = (Artist) getArguments().getSerializable(ARTIST);
        }
    }

    @Override
    public void onViewCreate() {
        binding.setArtist(artist);

        Glide.with(activity)
                .asBitmap()
                .placeholder(R.drawable.ic_mic_black)
                .load(artist.getPicture())
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Bitmap> transition) {
                        binding.imgPicture.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        binding.imgPicture.setImageBitmap(resource);
                        Palette.from(resource).generate(palette -> {
                            if (palette != null) {
                                int accent = palette.getDarkVibrantColor(ContextCompat.getColor(activity, R.color.DarkColorAccent));
                                int primary = palette.getDarkMutedColor(ContextCompat.getColor(activity, R.color.DarkColorPrimary));

                                GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BL_TR, new int[]{primary, accent});
                                gradientDrawable.setCornerRadius(0f);

                                binding.collapsingToolbar.setContentScrim(gradientDrawable);
                            }
                        });
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        binding.imgPicture.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                        binding.imgPicture.setImageDrawable(placeholder);
                    }
                });

        TabAdapterUtil adapterUtil = new TabAdapterUtil(getChildFragmentManager(), 3, ArtistTopHitFragment.newInstance(artist),
                ArtistMusicFragment.newInstance(artist), ArtistAlbumFragment.newInstance(artist));

        binding.viewPager.setAdapter(adapterUtil);
        binding.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout));
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_artist;
    }
}
