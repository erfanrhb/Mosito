package rahbari.erfan.mosito.ui.dialogs;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;

import eightbitlab.com.blurview.RenderScriptBlur;
import rahbari.erfan.mosito.R;
import rahbari.erfan.mosito.databinding.AdapterMusicDownloadBinding;
import rahbari.erfan.mosito.databinding.FragmentDownloadBinding;
import rahbari.erfan.mosito.models.MusicDownloadEm;
import rahbari.erfan.mosito.resources.Repositories.MusicRep;
import rahbari.erfan.mosito.utils.AdapterUtil;
import rahbari.erfan.mosito.utils.FragmentDialogUtil;

public class DownloadFragment extends FragmentDialogUtil<FragmentDownloadBinding> {

    private MusicRep musicRep;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        musicRep = new MusicRep();
    }

    @Override
    public void onViewCreate() {

        LinearLayoutManager manager = new LinearLayoutManager(activity);
        AdapterUtil<MusicDownloadEm, AdapterMusicDownloadBinding> adapterUtil =
                new AdapterUtil<MusicDownloadEm, AdapterMusicDownloadBinding>(activity, R.layout.adapter_music_download) {
                    @Override
                    public void bindViewHolder(AdapterMusicDownloadBinding binding, MusicDownloadEm item, int position) {
                        binding.setMusicDownload(item);
                    }
                };
        binding.recycler.setLayoutManager(manager);
        binding.recycler.setItemAnimator(null);
        binding.recycler.setAdapter(adapterUtil);

        musicRep.getMusicDao().selectDownloads().observe(this, musicDownloadEms -> {
            adapterUtil.setArray(musicDownloadEms);
            Log.e("Download", new Gson().toJson(musicDownloadEms));
            binding.tvEmpty.setVisibility(musicDownloadEms.size() == 0 ? View.VISIBLE : View.GONE);
            binding.tvTitle.setVisibility(musicDownloadEms.size() == 0 ? View.GONE : View.VISIBLE);
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View decorView = activity.getWindow().getDecorView();
        ViewGroup rootView = decorView.findViewById(android.R.id.content);
        Drawable windowBackground = decorView.getBackground();
        binding.blurView.setupWith(rootView)
                .setFrameClearDrawable(windowBackground)
                .setBlurAlgorithm(new RenderScriptBlur(activity))
                .setBlurRadius(10f)
                .setHasFixedTransformationMatrix(true);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_download;
    }
}
