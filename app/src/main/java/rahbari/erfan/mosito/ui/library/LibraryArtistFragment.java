package rahbari.erfan.mosito.ui.library;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import rahbari.erfan.mosito.R;
import rahbari.erfan.mosito.databinding.FragmentSwipeRecyclerBinding;
import rahbari.erfan.mosito.resources.Repositories.ArtistRep;
import rahbari.erfan.mosito.ui.adapters.ArtistPagedAdapter;
import rahbari.erfan.mosito.ui.artists.ArtistFragment;
import rahbari.erfan.mosito.utils.FragmentUtil;

public class LibraryArtistFragment extends FragmentUtil<FragmentSwipeRecyclerBinding> {
    private ArtistRep artistRep;
    private ArtistPagedAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        artistRep = new ArtistRep();
        adapter = new ArtistPagedAdapter();
    }

    @Override
    public void onViewCreate() {
        GridLayoutManager manager = new GridLayoutManager(activity, 3);

        binding.recyclerView.setItemAnimator(null);
        binding.recyclerView.setLayoutManager(manager);
        binding.recyclerView.setAdapter(adapter);

        adapter.setClicked(response -> navigation.addFragment(this, ArtistFragment.newInstance(response)));

        binding.swipeRefresh.setOnRefreshListener(() -> {
            adapter.notifyDataSetChanged();
            binding.swipeRefresh.setRefreshing(false);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        artistRep.library().observe(this, adapter::submitList);
        artistRep.getArtistDao().libraryCount().observe(this, integer -> {
            if (integer == null) return;
            if (integer == 0) {
                binding.setEmptyMessage(getString(R.string.library_empty));
                binding.setTitle(getString(R.string.library_musics));
            } else {
                binding.setEmptyMessage(null);
                binding.setTitle(null);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        artistRep.library().removeObservers(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_swipe_recycler;
    }
}
