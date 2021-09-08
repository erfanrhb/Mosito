package rahbari.erfan.mosito.ui.library;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import rahbari.erfan.mosito.R;
import rahbari.erfan.mosito.databinding.FragmentSwipeRecyclerBinding;
import rahbari.erfan.mosito.resources.Repositories.AlbumRep;
import rahbari.erfan.mosito.ui.adapters.AlbumHorizontalPaginateAdapter;
import rahbari.erfan.mosito.ui.albums.AlbumFragment;
import rahbari.erfan.mosito.utils.FragmentUtil;

public class LibraryAlbumFragment extends FragmentUtil<FragmentSwipeRecyclerBinding> {
    private AlbumRep albumRep;
    private AlbumHorizontalPaginateAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        albumRep = new AlbumRep();
    }

    @Override
    public void onViewCreate() {
        GridLayoutManager manager = new GridLayoutManager(activity, 2);
        adapter = new AlbumHorizontalPaginateAdapter();
        binding.recyclerView.setItemAnimator(null);
        binding.recyclerView.setLayoutManager(manager);
        binding.recyclerView.setAdapter(adapter);

        adapter.setClicked(album -> navigation.addFragment(this, AlbumFragment.newInstance(album)));

        binding.swipeRefresh.setOnRefreshListener(() -> {
            adapter.notifyDataSetChanged();
            binding.swipeRefresh.setRefreshing(false);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        albumRep.libraryPaginate().observe(this, adapter::submitList);
        albumRep.getAlbumDao().libraryCount().observe(this, integer -> {
            if (integer == null) return;
            if (integer == 0) {
                binding.setEmptyMessage(getString(R.string.library_empty));
                binding.setTitle(getString(R.string.library_album));
            }
            else {
                binding.setEmptyMessage(null);
                binding.setTitle(null);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        albumRep.libraryPaginate().removeObservers(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_swipe_recycler;
    }
}
