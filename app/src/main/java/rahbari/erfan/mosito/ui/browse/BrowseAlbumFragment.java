package rahbari.erfan.mosito.ui.browse;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import rahbari.erfan.mosito.R;
import rahbari.erfan.mosito.databinding.FragmentSwipeRecyclerBinding;
import rahbari.erfan.mosito.resources.Repositories.AlbumRep;
import rahbari.erfan.mosito.ui.adapters.AlbumVerticalAdapter;
import rahbari.erfan.mosito.ui.albums.AlbumFragment;
import rahbari.erfan.mosito.utils.FragmentUtil;
import rahbari.erfan.mosito.utils.PaginateUtil;

public class BrowseAlbumFragment extends FragmentUtil<FragmentSwipeRecyclerBinding> {
    private AlbumRep rep;
    private PaginateUtil paginate;
    private AlbumVerticalAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rep = new AlbumRep();
    }

    @Override
    public void onViewCreate() {
        int perPage = 25;

        LinearLayoutManager manager = new LinearLayoutManager(activity);
        adapter = new AlbumVerticalAdapter();
        binding.recyclerView.setItemAnimator(null);
        binding.recyclerView.setLayoutManager(manager);
        binding.recyclerView.setAdapter(adapter);

        adapter.setClicked(response -> navigation.addFragment(this, AlbumFragment.newInstance(response)));

        paginate = new PaginateUtil(binding.recyclerView, perPage, page -> rep.latest(page, perPage));

        binding.swipeRefresh.setOnRefreshListener(() -> {
            activity.runOnUiThread(() -> binding.swipeRefresh.setRefreshing(false));
            paginate.refresh();
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_swipe_recycler;
    }

    @Override
    public void onResume() {
        super.onResume();
        rep.latest().observe(this, adapter::submitList);
    }

    @Override
    public void onPause() {
        super.onPause();
        rep.latest().removeObservers(this);
    }
}
