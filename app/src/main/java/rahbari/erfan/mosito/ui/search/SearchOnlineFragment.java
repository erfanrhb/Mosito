package rahbari.erfan.mosito.ui.search;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import rahbari.erfan.mosito.R;
import rahbari.erfan.mosito.databinding.FragmentSearchResultBinding;
import rahbari.erfan.mosito.resources.Repositories.SearchRep;
import rahbari.erfan.mosito.ui.adapters.AlbumHorizontalPaginateAdapter;
import rahbari.erfan.mosito.ui.adapters.ArtistAdapter;
import rahbari.erfan.mosito.ui.adapters.MusicVerticalAdapter;
import rahbari.erfan.mosito.ui.adapters.PlaylistAdapter;
import rahbari.erfan.mosito.ui.albums.AlbumFragment;
import rahbari.erfan.mosito.ui.artists.ArtistFragment;
import rahbari.erfan.mosito.utils.FragmentUtil;

public class SearchOnlineFragment extends FragmentUtil<FragmentSearchResultBinding> {
    private SearchRep rep;
    private String searchQuery;

    private AlbumHorizontalPaginateAdapter albumAdapter;
    private MusicVerticalAdapter musicAdapter;
    private PlaylistAdapter playlistAdapter;
    private ArtistAdapter artistAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rep = new SearchRep();
    }

    public void setSearchQuery(String searchQuery) {
        search(searchQuery, this.searchQuery);
        this.searchQuery = searchQuery;
    }

    @Override
    public void onViewCreate() {
        LinearLayoutManager musicsAdapter = new LinearLayoutManager(activity);
        musicAdapter = new MusicVerticalAdapter();
        binding.recyclerMusics.setItemAnimator(null);
        binding.recyclerMusics.setLayoutManager(musicsAdapter);
        binding.recyclerMusics.setAdapter(musicAdapter);

        GridLayoutManager albumsManager = new GridLayoutManager(activity, 2);
        albumAdapter = new AlbumHorizontalPaginateAdapter();
        binding.recyclerAlbums.setItemAnimator(null);
        binding.recyclerAlbums.setLayoutManager(albumsManager);
        binding.recyclerAlbums.setAdapter(albumAdapter);
        albumAdapter.setClicked(album -> navigation.addFragment(this, AlbumFragment.newInstance(album)));

        LinearLayoutManager artistsManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        artistAdapter = new ArtistAdapter(activity);
        binding.recyclerArtists.setItemAnimator(null);
        binding.recyclerArtists.setLayoutManager(artistsManager);
        binding.recyclerArtists.setAdapter(artistAdapter);
        artistAdapter.setOnClick(response -> navigation.addFragment(this, ArtistFragment.newInstance(response)));

        GridLayoutManager playlistManager = new GridLayoutManager(activity, 2);
        playlistAdapter = new PlaylistAdapter();
        binding.recyclerPlaylists.setItemAnimator(null);
        binding.recyclerPlaylists.setLayoutManager(playlistManager);
        binding.recyclerPlaylists.setAdapter(playlistAdapter);
    }

    private void search(String val, String oldVal) {
        if (rep == null || musicAdapter == null) return;

        rep.searchOnline(val, response -> activity.runOnUiThread(() -> {

            rep.arrayMusicIds(response.getMusics()).observe(this, musicAdapter::submitList);
            rep.arrayAlbumIds(response.getAlbums()).observe(this, albumAdapter::submitList);
            rep.arrayPlaylistIds(response.getPlaylists()).observe(this, playlistAdapter::submitList);
            rep.arrayArtistIds(response.getArtists()).observe(this, artistAdapter::setArray);

            binding.setAlbumsCount(response.getAlbums_count());
            binding.setMusicsCount(response.getMusics_count());
            binding.setArtistsCount(response.getArtists().size());
            binding.setPlaylistsCount(response.getPlaylists_count());

        }), response -> {

        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_search_result;
    }
}
