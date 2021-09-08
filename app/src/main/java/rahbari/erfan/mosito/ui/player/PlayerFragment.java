package rahbari.erfan.mosito.ui.player;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import eightbitlab.com.blurview.RenderScriptBlur;
import rahbari.erfan.mosito.Application;
import rahbari.erfan.mosito.R;
import rahbari.erfan.mosito.databinding.FragmentPlayerBinding;
import rahbari.erfan.mosito.enums.Command;
import rahbari.erfan.mosito.enums.Status;
import rahbari.erfan.mosito.models.Music;
import rahbari.erfan.mosito.utils.FragmentUtil;
import rahbari.erfan.mosito.utils.SubjectWrapper;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PlayerFragment extends FragmentUtil<FragmentPlayerBinding> {
    private int progressWidth = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreate() {
        float radius = 15f;
        View decorView = activity.getWindow().getDecorView();
        ViewGroup rootView = decorView.findViewById(android.R.id.content);
        Drawable windowBackground = decorView.getBackground();
        binding.blurView.setupWith(rootView)
                .setFrameClearDrawable(windowBackground)
                .setBlurAlgorithm(new RenderScriptBlur(activity))
                .setBlurRadius(radius)
                .setHasFixedTransformationMatrix(true);

        ViewTreeObserver vto = binding.relativeProgress.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                binding.relativeProgress.getViewTreeObserver().removeOnPreDrawListener(this);
                progressWidth = binding.relativeProgress.getMeasuredWidth();
                return true;
            }
        });

        binding.btnPlayPause.setOnClickListener(v -> {
            if (Application.playing.getStatus() == Status.PLAY)
                Application.playing.setCommand(Command.Pause);
            else if (Application.playing.getMusic() != null)
                Application.playing.setCommand(Command.Play);
        });

        binding.btnNext.setOnClickListener(v -> Application.playing.setCommand(Command.Next));
        binding.blurView.setOnClickListener(v -> new PlayerListFragment().show(getChildFragmentManager(), ""));
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_player;
    }

    private void bufferChange(Integer percent) {
        ViewGroup.LayoutParams params = binding.viewBuffer.getLayoutParams();
        if (percent == 100) {
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        } else {
            params.width = (progressWidth / 100) * percent;
        }
        binding.viewBuffer.setLayoutParams(params);
    }

    private void percentChange(Integer percent) {
        ViewGroup.LayoutParams params = binding.viewProgress.getLayoutParams();
        if (percent >= 99) {
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        } else {
            params.width = (progressWidth / 100) * percent;
        }
        binding.viewProgress.setLayoutParams(params);
    }

    private void statusChange(Status val) {
        switch (val) {
            case PLAY:
                binding.progressBar.setVisibility(View.GONE);
                binding.btnPlayPause.setVisibility(View.VISIBLE);
                binding.btnPlayPause.setIcon(ContextCompat.getDrawable(activity, R.drawable.ic_pause));
                break;
            case PAUSE:
            case COMPLETED:
                binding.progressBar.setVisibility(View.GONE);
                binding.btnPlayPause.setVisibility(View.VISIBLE);
                binding.btnPlayPause.setIcon(ContextCompat.getDrawable(activity, R.drawable.ic_play));
                break;
            case PREPARING:
                binding.btnPlayPause.setVisibility(View.GONE);
                binding.btnPlayPause.setIcon(ContextCompat.getDrawable(activity, R.drawable.ic_pause));
                binding.progressBar.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        bufferChange(Application.playing.getBuffer());
        percentChange(Application.playing.getPercent());
        statusChange(Application.playing.getStatus());

        if (Application.playing.getMusic() != null) {
            binding.setMusic(Application.playing.getMusic());
        }

        Application.playing.bufferObservable()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SubjectWrapper<Integer>() {
                    @Override
                    public void onChange(Integer percent) {
                        bufferChange(percent);
                    }
                });

        Application.playing.percentObservable()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SubjectWrapper<Integer>() {
                    @Override
                    public void onChange(Integer percent) {
                        percentChange(percent);
                    }
                });

        Application.playing.statusObservable()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SubjectWrapper<Status>() {
                    @Override
                    public void onChange(Status val) {
                        statusChange(val);
                    }
                });

        Application.playing.musicIdObservable()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SubjectWrapper<Music>() {
                    @Override
                    public void onChange(Music val) {
                        binding.setMusic(val);
                    }
                });

    }
}
