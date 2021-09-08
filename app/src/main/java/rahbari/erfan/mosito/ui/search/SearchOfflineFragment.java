package rahbari.erfan.mosito.ui.search;

import android.os.Bundle;
import android.util.Log;

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

public class SearchOfflineFragment extends FragmentUtil<FragmentSearchResultBinding> {
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
        this.searchQuery = searchQuery;
        search("%" + searchQuery + "%");
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

    private void search(String val) {
        if (rep == null || musicAdapter == null) return;

        Log.e("Search", "search offline for " + val);

        rep.searchMusic(val).observe(this, musicAdapter::submitList);
        rep.searchAlbum(val).observe(this, albumAdapter::submitList);
        rep.searchArtist(val).observe(this, artistAdapter::setArray);
        rep.searchPlaylists(val).observe(this, playlistAdapter::submitList);

        rep.searchMusicCount(val).observe(this, integer -> activity.runOnUiThread(() -> binding.setMusicsCount(integer)));
        rep.searchAlbumCount(val).observe(this, integer -> activity.runOnUiThread(() -> binding.setAlbumsCount(integer)));
        rep.searchPlaylistsCount(val).observe(this, integer -> activity.runOnUiThread(() -> binding.setPlaylistsCount(integer)));
        rep.searchArtistCount(val).observe(this, integer -> activity.runOnUiThread(() -> binding.setArtistsCount(integer)));
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_search_result;
    }
}
