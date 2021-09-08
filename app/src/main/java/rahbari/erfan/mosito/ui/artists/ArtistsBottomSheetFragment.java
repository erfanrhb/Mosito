package rahbari.erfan.mosito.ui.artists;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;

import rahbari.erfan.mosito.R;
import rahbari.erfan.mosito.databinding.FragmentTitleListBinding;
import rahbari.erfan.mosito.interfaces.Response;
import rahbari.erfan.mosito.models.Artist;
import rahbari.erfan.mosito.resources.Repositories.AlbumRep;
import rahbari.erfan.mosito.resources.Repositories.MusicRep;
import rahbari.erfan.mosito.ui.adapters.ArtistVerticalAdapter;
import rahbari.erfan.mosito.utils.BottomSheetFragmentUtil;

public class ArtistsBottomSheetFragment extends BottomSheetFragmentUtil<FragmentTitleListBinding> {
    private static final String TITLE = "title";
    private static final String MODEL = "model";
    private static final String OPTION = "option";

    private long option;
    private String title;
    private String model;
    private Response<Artist> onClick;
    private ArtistVerticalAdapter adapter;
    private LiveData<List<Artist>> liveData;

    public static ArtistsBottomSheetFragment newInstance(String title, String model, long option) {
        ArtistsBottomSheetFragment fragment = new ArtistsBottomSheetFragment();
        Bundle args = new Bundle();
        args.putString(TITLE, title);
        args.putString(MODEL, model);
        args.putLong(OPTION, option);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(TITLE);
            model = getArguments().getString(MODEL);
            option = getArguments().getLong(OPTION);
        }
    }

    @Override
    public void onViewCreate() {
        binding.setText(title);

        LinearLayoutManager manager = new LinearLayoutManager(activity);
        adapter = new ArtistVerticalAdapter(activity);

        binding.recycler.setLayoutManager(manager);
        binding.recycler.setAdapter(adapter);


        switch (model) {
            case "album":
                liveData = new AlbumRep().getAlbumDao().artistInAlbum(option);
                break;
            case "music":
                liveData = new MusicRep().getMusicDao().artistInMusic(option);
                break;
            default:
                throw new RuntimeException("model is undefined");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        liveData.observe(this, adapter::setArray);
        adapter.setOnClick(onClick);
    }

    @Override
    public void onPause() {
        super.onPause();
        liveData.removeObservers(this);
        adapter.setOnClick(null);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_title_list;
    }

    public void setOnClick(Response<Artist> onClick) {
        this.onClick = onClick;
    }
}