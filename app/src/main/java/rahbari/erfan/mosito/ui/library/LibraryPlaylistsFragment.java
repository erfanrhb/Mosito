package rahbari.erfan.mosito.ui.library;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import rahbari.erfan.mosito.R;
import rahbari.erfan.mosito.databinding.FragmentPlaylistsBinding;
import rahbari.erfan.mosito.resources.Repositories.PlaylistRep;
import rahbari.erfan.mosito.ui.adapters.PlaylistAdapter;
import rahbari.erfan.mosito.ui.playlists.PlaylistCreateFragment;
import rahbari.erfan.mosito.ui.playlists.PlaylistFragment;
import rahbari.erfan.mosito.utils.FragmentUtil;
import rahbari.erfan.mosito.utils.PaginateUtil;

public class LibraryPlaylistsFragment extends FragmentUtil<FragmentPlaylistsBinding> {
    private PlaylistRep playlistRep;
    private PaginateUtil paginate;
    private PlaylistAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        playlistRep = new PlaylistRep();
        adapter = new PlaylistAdapter();
    }

    @Override
    public void onViewCreate() {
        int perPage = 25;

        GridLayoutManager manager = new GridLayoutManager(activity, 2);

        binding.recyclerView.setItemAnimator(null);
        binding.recyclerView.setLayoutManager(manager);
        binding.recyclerView.setAdapter(adapter);

        adapter.setClicked(response -> navigation.addFragment(this, PlaylistFragment.newInstance(response)));

        paginate = new PaginateUtil(binding.recyclerView, perPage, page -> playlistRep.index(perPage, page));

        binding.setTitle(getString(R.string.library_playlists));
        binding.btnAdd.setOnClickListener(view -> navigation.addFragment(this, new PlaylistCreateFragment()));

        binding.swipeRefresh.setOnRefreshListener(() -> {
            adapter.notifyDataSetChanged();
            binding.swipeRefresh.setRefreshing(false);
            paginate.refresh();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        playlistRep.libraryPaginate().observe(this, adapter::submitList);
        playlistRep.getPlaylistDao().libraryCount().observe(this, integer -> {
            if (integer == null) return;
            if (integer == 0) binding.setEmptyMessage(getString(R.string.library_empty));
            else binding.setEmptyMessage(null);
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        playlistRep.libraryPaginate().removeObservers(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_playlists;
    }
}
