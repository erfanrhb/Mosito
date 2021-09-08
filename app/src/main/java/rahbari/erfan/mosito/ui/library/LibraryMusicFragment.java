package rahbari.erfan.mosito.ui.library;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import rahbari.erfan.mosito.R;
import rahbari.erfan.mosito.databinding.FragmentSwipeRecyclerBinding;
import rahbari.erfan.mosito.resources.Repositories.MusicRep;
import rahbari.erfan.mosito.ui.adapters.MusicVerticalAdapter;
import rahbari.erfan.mosito.utils.FragmentUtil;

public class LibraryMusicFragment extends FragmentUtil<FragmentSwipeRecyclerBinding> {
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

        binding.swipeRefresh.setOnRefreshListener(() -> {
            adapter.notifyDataSetChanged();
            binding.swipeRefresh.setRefreshing(false);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        musicRep.libraryPaginate().observe(this, adapter::submitList);
        musicRep.getMusicDao().libraryCount().observe(this, integer -> {
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
        musicRep.libraryPaginate().removeObservers(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_swipe_recycler;
    }
}
