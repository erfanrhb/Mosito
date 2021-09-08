package rahbari.erfan.mosito.ui.browse;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import rahbari.erfan.mosito.R;
import rahbari.erfan.mosito.databinding.AdapterOffersBinding;
import rahbari.erfan.mosito.databinding.FragmentBrowseBinding;
import rahbari.erfan.mosito.models.Offer;
import rahbari.erfan.mosito.resources.Repositories.AlbumRep;
import rahbari.erfan.mosito.resources.Repositories.MusicRep;
import rahbari.erfan.mosito.resources.Repositories.OfferRep;
import rahbari.erfan.mosito.ui.adapters.AlbumHorizontalPaginateAdapter;
import rahbari.erfan.mosito.ui.adapters.MusicCartAdapter;
import rahbari.erfan.mosito.ui.adapters.MusicVerticalAdapter;
import rahbari.erfan.mosito.ui.albums.AlbumFragment;
import rahbari.erfan.mosito.utils.AdapterUtil;
import rahbari.erfan.mosito.utils.FragmentUtil;

public class BrowseFragment extends FragmentUtil<FragmentBrowseBinding> {
    private final int mostPlayPerPage = 9;
    private final int perPage = 24;
    private final int albumPerPage = 5;
    private MusicRep musicRep;
    private AlbumRep albumRep;
    private MusicVerticalAdapter adapter;
    private MusicCartAdapter mostPlayedAdapter;
    private AlbumHorizontalPaginateAdapter albumHorizontalAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        musicRep = new MusicRep();
        albumRep = new AlbumRep();
    }

    @Override
    public void onViewCreate() {
        /* register albums adapter **/
        LinearLayoutManager albumManager = new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false);
        albumHorizontalAdapter = new AlbumHorizontalPaginateAdapter();
        binding.recyclerAlbums.setItemAnimator(null);
        binding.recyclerAlbums.setLayoutManager(albumManager);
        binding.recyclerAlbums.setAdapter(albumHorizontalAdapter);
        albumRep.latest(1, albumPerPage);

        /* register most played adapter **/
        GridLayoutManager gridManager = new GridLayoutManager(activity, 3);
        mostPlayedAdapter = new MusicCartAdapter();
        binding.recyclerMostPlayed.setItemAnimator(null);
        binding.recyclerMostPlayed.setLayoutManager(gridManager);
        binding.recyclerMostPlayed.setAdapter(mostPlayedAdapter);
        musicRep.mostPlayed(mostPlayPerPage);

        /* register latest adapter **/
        GridLayoutManager manager = new GridLayoutManager(activity, 4, RecyclerView.HORIZONTAL, false);
        adapter = new MusicVerticalAdapter();
        binding.recyclerLatest.setItemAnimator(null);
        binding.recyclerLatest.setLayoutManager(manager);
        binding.recyclerLatest.setAdapter(adapter);

        binding.recyclerLatest.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {

                    int offset = recyclerView.computeHorizontalScrollOffset() % recyclerView.computeHorizontalScrollExtent();
                    int center = recyclerView.computeHorizontalScrollExtent() / 2;

                    if (offset >= center) {
                        binding.recyclerLatest.smoothScrollToPosition(manager.findLastVisibleItemPosition());
                    } else {
                        binding.recyclerLatest.smoothScrollToPosition(manager.findFirstVisibleItemPosition());
                    }
                }
            }
        });

        musicRep.latestApi(1, perPage);

        /* register offer adapter **/
        LinearLayoutManager offerManager = new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false);
        AdapterUtil<Offer, AdapterOffersBinding> adapterOffer = new AdapterUtil<Offer, AdapterOffersBinding>(activity, R.layout.adapter_offers) {
            @Override
            public void bindViewHolder(AdapterOffersBinding binding, Offer item, int position) {
                binding.setOffer(item);
            }
        };
        binding.recyclerOffers.setItemAnimator(null);
        binding.recyclerOffers.setLayoutManager(offerManager);
        binding.recyclerOffers.setAdapter(adapterOffer);
        new OfferRep().offers(adapterOffer::setArray);

        /* register item actions **/
        binding.btnMusicSeeAll.setOnClickListener(view -> navigation.addFragment(this, new BrowseMusicFragment()));
        binding.btnAlbumsSeeAll.setOnClickListener(view -> navigation.addFragment(this, new BrowseAlbumFragment()));
        binding.swipeRefresh.setOnRefreshListener(() -> activity.runOnUiThread(() -> binding.swipeRefresh.setRefreshing(false)));
        albumHorizontalAdapter.setClicked(response -> navigation.addFragment(this, AlbumFragment.newInstance(response)));
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_browse;
    }

    @Override
    public void onResume() {
        super.onResume();
        albumRep.latest(albumPerPage).observe(this, albumLibraries -> albumHorizontalAdapter.submitList(albumLibraries));
        musicRep.mostPlayedDao(mostPlayPerPage).observe(this, mostPlayedAdapter::submitList);
        musicRep.latestLimitDao(perPage).observe(this, adapter::submitList);
    }

    @Override
    public void onPause() {
        super.onPause();
        albumRep.latest(albumPerPage).removeObservers(this);
        musicRep.mostPlayedDao(mostPlayPerPage).removeObservers(this);
        musicRep.latestLimitDao(perPage).removeObservers(this);
    }
}
