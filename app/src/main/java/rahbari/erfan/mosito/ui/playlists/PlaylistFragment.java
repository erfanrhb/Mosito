package rahbari.erfan.mosito.ui.playlists;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import rahbari.erfan.mosito.R;
import rahbari.erfan.mosito.databinding.FragmentPlaylistBinding;
import rahbari.erfan.mosito.models.Playlist;
import rahbari.erfan.mosito.resources.Repositories.PlaylistRep;
import rahbari.erfan.mosito.ui.adapters.MusicVerticalAdapter;
import rahbari.erfan.mosito.utils.FragmentUtil;
import rahbari.erfan.mosito.utils.PaginateUtil;

public class PlaylistFragment extends FragmentUtil<FragmentPlaylistBinding> {
    private static final String PLAYLIST = "playlist";

    private Playlist playlist;
    private PlaylistRep playlistRep;
    private MusicVerticalAdapter adapter;

    public static PlaylistFragment newInstance(Playlist playlist) {
        PlaylistFragment fragment = new PlaylistFragment();
        Bundle args = new Bundle();
        args.putSerializable(PLAYLIST, playlist);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        playlistRep = new PlaylistRep();
        adapter = new MusicVerticalAdapter();

        if (getArguments() != null) {
            playlist = (Playlist) getArguments().getSerializable(PLAYLIST);
        }
    }

    @Override
    public void onViewCreate() {
        int perPage = 25;

        LinearLayoutManager manager = new LinearLayoutManager(activity);
        binding.rvMusics.setItemAnimator(null);
        binding.rvMusics.setLayoutManager(manager);
        binding.rvMusics.setAdapter(adapter);

        new PaginateUtil(binding.rvMusics, perPage, page -> playlistRep.musics(playlist.getId(), perPage, page));
    }

    @Override
    public void onResume() {
        super.onResume();
        playlistRep.getPlaylistDao().playlist(playlist.getId()).observe(this, playlistLibrary -> binding.setPlaylistLibrary(playlistLibrary));
        playlistRep.musics(playlist.getId()).observe(this, adapter::submitList);
    }

    @Override
    public void onPause() {
        super.onPause();
        playlistRep.getPlaylistDao().playlist(playlist.getId()).removeObservers(this);
        playlistRep.musics(playlist.getId()).removeObservers(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_playlist;
    }
}
