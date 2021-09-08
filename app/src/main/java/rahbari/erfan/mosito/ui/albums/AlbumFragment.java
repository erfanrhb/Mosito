package rahbari.erfan.mosito.ui.albums;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import rahbari.erfan.mosito.R;
import rahbari.erfan.mosito.databinding.FragmentAlbumBinding;
import rahbari.erfan.mosito.models.Album;
import rahbari.erfan.mosito.resources.Repositories.AlbumRep;
import rahbari.erfan.mosito.ui.adapters.MusicVerticalAdapter;
import rahbari.erfan.mosito.ui.artists.ArtistFragment;
import rahbari.erfan.mosito.ui.artists.ArtistsBottomSheetFragment;
import rahbari.erfan.mosito.utils.FragmentUtil;
import rahbari.erfan.mosito.utils.MositoUtils;
import rahbari.erfan.mosito.utils.Utils;

public class AlbumFragment extends FragmentUtil<FragmentAlbumBinding> {
    private static final String ALBUM = "album";

    private Album album;
    private AlbumRep rep;

    public static AlbumFragment newInstance(Album album) {
        AlbumFragment fragment = new AlbumFragment();
        Bundle args = new Bundle();
        args.putSerializable(ALBUM, album);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rep = new AlbumRep();

        if (getArguments() != null) {
            album = (Album) getArguments().getSerializable(ALBUM);
        }
    }

    @Override
    public void onViewCreate() {
        MusicVerticalAdapter adapter = new MusicVerticalAdapter();
        LinearLayoutManager manager = new LinearLayoutManager(activity);
        binding.recyclerMusics.setItemAnimator(null);
        binding.recyclerMusics.setLayoutManager(manager);
        binding.recyclerMusics.setAdapter(adapter);

        rep.albumMusic(album.getId()).observe(this, albumMusics -> binding.setAlbumLibrary(albumMusics)); // local album observer
        rep.musics(album.getId()).observe(this, adapter::submitList); // local musics observer
        binding.swipeRefresh.setOnRefreshListener(this::load);

        binding.btnAdd.setOnClickListener(v -> MositoUtils.addLibraryAlbum(album));
        binding.btnSub.setOnClickListener(v -> MositoUtils.subLibraryAlbum(album));

        binding.setTime("");
        binding.setItems("");
        binding.setArtistText("");

        rep.getAlbumDao().durationAlbum(album.getId()).observe(this, aLong -> {
            if (aLong == null) return;
            activity.runOnUiThread(() -> binding.setTime(Utils.minute(aLong)));
        });
        rep.getAlbumDao().musicsAlbum(album.getId()).observe(this, integer -> {
            if (integer == null) return;
            activity.runOnUiThread(() -> binding.setItems(integer.toString()));
        });

        rep.getAlbumDao().artistInAlbum(album.getId()).observe(this, artists -> {
            if (artists == null || artists.size() == 0) return;

            if (artists.size() == 1) {
                binding.setArtistText(artists.get(0).getName());
                binding.tvArtist.setOnClickListener(view -> navigation.addFragment(this, ArtistFragment.newInstance(artists.get(0))));
            } else {
                binding.tvArtist.setOnClickListener(view -> {
                    ArtistsBottomSheetFragment fragment = ArtistsBottomSheetFragment.newInstance(getString(R.string.artists_in_this_album), "album", album.getId());
                    fragment.show(getChildFragmentManager(), "");
                    fragment.setOnClick(response -> {
                        fragment.dismiss();
                        navigation.addFragment(this, ArtistFragment.newInstance(response));
                    });
                });

                if (artists.size() == 2) {
                    binding.setArtistText(artists.get(0).getName() + " and " + artists.get(1).getName());
                } else {
                    binding.setArtistText(artists.get(0).getName() + " and (" + (artists.size() - 1) + " more)");
                }
            }
        });

        load();
    }

    private void load() {
        binding.swipeRefresh.setRefreshing(true);
        rep.show(album.getId(), response -> activity.runOnUiThread(() -> binding.swipeRefresh.setRefreshing(false)));
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_album;
    }
}
