package rahbari.erfan.mosito.ui.artists;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import rahbari.erfan.mosito.R;
import rahbari.erfan.mosito.databinding.FragmentSwipeRecyclerBinding;
import rahbari.erfan.mosito.models.Artist;
import rahbari.erfan.mosito.resources.Repositories.ArtistRep;
import rahbari.erfan.mosito.ui.adapters.AlbumVerticalAdapter;
import rahbari.erfan.mosito.ui.albums.AlbumFragment;
import rahbari.erfan.mosito.utils.FragmentUtil;
import rahbari.erfan.mosito.utils.PaginateUtil;

public class ArtistAlbumFragment extends FragmentUtil<FragmentSwipeRecyclerBinding> {
    private static final String ARTIST = "artist";

    private Artist artist;
    private ArtistRep rep;
    private final int perPage = 20;
    private AlbumVerticalAdapter adapter;

    public static ArtistAlbumFragment newInstance(Artist artist) {
        ArtistAlbumFragment fragment = new ArtistAlbumFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARTIST, artist);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rep = new ArtistRep();
        adapter = new AlbumVerticalAdapter();

        if (getArguments() != null) {
            artist = (Artist) getArguments().getSerializable(ARTIST);
        }
    }

    @Override
    public void onViewCreate() {

        LinearLayoutManager manager = new LinearLayoutManager(activity);

        binding.recyclerView.setItemAnimator(null);
        binding.recyclerView.setLayoutManager(manager);
        binding.recyclerView.setAdapter(adapter);

        adapter.setClicked(response -> navigation.addFragment(this, AlbumFragment.newInstance(response)));

        PaginateUtil pagination = new PaginateUtil(binding.recyclerView, perPage, page -> rep.albums(artist.getId(), page, perPage));


        binding.swipeRefresh.setOnRefreshListener(() -> {
            binding.swipeRefresh.setRefreshing(false);
            pagination.refresh();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        rep.albums(artist.getId(), perPage).observe(this, adapter::submitList);
    }

    @Override
    public void onPause() {
        super.onPause();
        rep.albums(artist.getId(), perPage).removeObservers(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_swipe_recycler;
    }
}
