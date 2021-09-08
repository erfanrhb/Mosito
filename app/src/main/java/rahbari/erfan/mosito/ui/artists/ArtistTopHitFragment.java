package rahbari.erfan.mosito.ui.artists;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import rahbari.erfan.mosito.R;
import rahbari.erfan.mosito.databinding.FragmentSwipeRecyclerBinding;
import rahbari.erfan.mosito.models.Artist;
import rahbari.erfan.mosito.resources.Repositories.ArtistRep;
import rahbari.erfan.mosito.ui.adapters.MusicVerticalAdapter;
import rahbari.erfan.mosito.utils.FragmentUtil;
import rahbari.erfan.mosito.utils.PaginateUtil;

public class ArtistTopHitFragment extends FragmentUtil<FragmentSwipeRecyclerBinding> {
    private static final String ARTIST = "artist";

    private Artist artist;
    private ArtistRep rep;
    private final int perPage = 25;
    private PaginateUtil pagination;
    private MusicVerticalAdapter musicAdapter;

    public static ArtistTopHitFragment newInstance(Artist artist) {
        ArtistTopHitFragment fragment = new ArtistTopHitFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARTIST, artist);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rep = new ArtistRep();
        musicAdapter = new MusicVerticalAdapter();

        if (getArguments() != null) {
            artist = (Artist) getArguments().getSerializable(ARTIST);
        }
    }

    @Override
    public void onViewCreate() {
        LinearLayoutManager musicManager = new LinearLayoutManager(activity);
        binding.recyclerView.setItemAnimator(null);
        binding.recyclerView.setLayoutManager(musicManager);
        binding.recyclerView.setAdapter(musicAdapter);

        pagination = new PaginateUtil(binding.recyclerView, perPage, page -> rep.tops(artist.getId(), page, perPage));

        binding.swipeRefresh.setOnRefreshListener(() -> {
            binding.swipeRefresh.setRefreshing(false);
            pagination.refresh();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        rep.tops(artist.getId(), perPage).observe(this, musicAdapter::submitList);
    }

    @Override
    public void onPause() {
        super.onPause();
        rep.tops(artist.getId(), perPage).removeObservers(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_swipe_recycler;
    }
}
