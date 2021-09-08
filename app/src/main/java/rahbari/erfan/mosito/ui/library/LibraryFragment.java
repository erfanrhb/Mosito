package rahbari.erfan.mosito.ui.library;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import rahbari.erfan.mosito.R;
import rahbari.erfan.mosito.databinding.FragmentLibraryBinding;
import rahbari.erfan.mosito.resources.Repositories.MusicRep;
import rahbari.erfan.mosito.ui.adapters.MusicVerticalAdapter;
import rahbari.erfan.mosito.utils.FragmentUtil;

public class LibraryFragment extends FragmentUtil<FragmentLibraryBinding> {
    private MusicRep musicRep;
    private MusicVerticalAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        musicRep = new MusicRep();
        adapter = new MusicVerticalAdapter();
    }

    @Override
    public void onViewCreate() {

        LinearLayoutManager manager = new LinearLayoutManager(activity);
        binding.recyclerView.setItemAnimator(null);
        binding.recyclerView.setLayoutManager(manager);
        binding.recyclerView.setAdapter(adapter);

        binding.btnArtists.setOnClickListener(v -> navigation.addFragment(this, new LibraryArtistFragment()));
        binding.btnMusics.setOnClickListener(v -> navigation.addFragment(this, new LibraryMusicFragment()));
        binding.btnAlbums.setOnClickListener(v -> navigation.addFragment(this, new LibraryAlbumFragment()));
        binding.btnPlaylists.setOnClickListener(v -> navigation.addFragment(this, new LibraryPlaylistsFragment()));
        binding.swipeRefresh.setOnRefreshListener(() -> binding.swipeRefresh.setRefreshing(false));

        binding.swipeRefresh.setOnRefreshListener(() -> {
            activity.sendBroadcast(new Intent("SYNC_SERVICE"));
            binding.swipeRefresh.setRefreshing(false);
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_library;
    }

    @Override
    public void onResume() {
        super.onResume();
        musicRep.recentlyAdded().observe(this, adapter::submitList);
    }

    @Override
    public void onPause() {
        super.onPause();
        musicRep.recentlyAdded().removeObservers(this);
    }
}

