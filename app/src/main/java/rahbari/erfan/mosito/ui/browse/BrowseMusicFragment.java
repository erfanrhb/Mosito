package rahbari.erfan.mosito.ui.browse;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import rahbari.erfan.mosito.R;
import rahbari.erfan.mosito.databinding.FragmentSwipeRecyclerBinding;
import rahbari.erfan.mosito.resources.Repositories.MusicRep;
import rahbari.erfan.mosito.ui.adapters.MusicVerticalAdapter;
import rahbari.erfan.mosito.utils.FragmentUtil;
import rahbari.erfan.mosito.utils.PaginateUtil;

public class BrowseMusicFragment extends FragmentUtil<FragmentSwipeRecyclerBinding> {
    private MusicRep rep;
    private PaginateUtil paginate;
    private MusicVerticalAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rep = new MusicRep();
    }

    @Override
    public void onViewCreate() {
        int perPage = 25;

        LinearLayoutManager manager = new LinearLayoutManager(activity);
        adapter = new MusicVerticalAdapter();
        binding.recyclerView.setItemAnimator(null);
        binding.recyclerView.setLayoutManager(manager);
        binding.recyclerView.setAdapter(adapter);

        paginate = new PaginateUtil(binding.recyclerView, perPage, page -> rep.latestApi(page, perPage));

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
        rep.latestDao().observe(this, adapter::submitList);
    }

    @Override
    public void onPause() {
        super.onPause();
        rep.latestDao().removeObservers(this);
    }
}
