package rahbari.erfan.mosito.ui.player;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import java.util.List;

import rahbari.erfan.mosito.Application;
import rahbari.erfan.mosito.R;
import rahbari.erfan.mosito.databinding.FragmentListPlayerBinding;
import rahbari.erfan.mosito.enums.Command;
import rahbari.erfan.mosito.enums.Status;
import rahbari.erfan.mosito.models.Music;
import rahbari.erfan.mosito.resources.Repositories.MusicRep;
import rahbari.erfan.mosito.ui.adapters.MusicVerticalAdapter;
import rahbari.erfan.mosito.utils.BottomSheetFragmentUtil;
import rahbari.erfan.mosito.utils.SubjectWrapper;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PlayerListFragment extends BottomSheetFragmentUtil<FragmentListPlayerBinding> {
    private int progressWidth = 0;
    private MusicRep musicRep;
    private MusicVerticalAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        musicRep = new MusicRep();
        adapter = new MusicVerticalAdapter();
    }

    @Override
    @SuppressLint("ClickableViewAccessibility")
    public void onViewCreate() {
        LinearLayoutManager manager = new LinearLayoutManager(activity);
        binding.recyclerMusics.setItemAnimator(null);
        binding.recyclerMusics.setLayoutManager(manager);
        binding.recyclerMusics.setAdapter(adapter);
        binding.btnClose.setOnClickListener(v -> dismiss());

        binding.indicator.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {

            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
                Application.playing.setSeekTo( (seekBar.getProgress() * Application.playing.getMusic().getDuration()) / 100);
            }
        });

//        ViewTreeObserver vto = binding.relativeProgress.getViewTreeObserver();
//        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//            public boolean onPreDraw() {
//                binding.relativeProgress.getViewTreeObserver().removeOnPreDrawListener(this);
//                progressWidth = binding.relativeProgress.getMeasuredWidth();
//                return true;
//            }
//        });

        binding.btnPlayPause.setOnClickListener(v -> {
            if (Application.playing.getStatus() == Status.PLAY)
                Application.playing.setCommand(Command.Pause);
            else if (Application.playing.getMusic() != null)
                Application.playing.setCommand(Command.Play);
        });

//        binding.btnNext.setOnClickListener(v -> Application.playing.setCommand(Command.Next));
//        binding.btnBack.setOnClickListener(v -> Application.playing.setCommand(Command.Back));
//        binding.rlSeeker.setOnTouchListener((v, event) -> {
//            if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                Application.playing.setSeekTo(((int) event.getX()) * Application.playing.getMusic().getDuration() / progressWidth);
//            }
//            return true;
//        });

//        IndicatorSeekBar seekBar = IndicatorSeekBar
//                .with(getContext())
//                .max(110)
//                .min(10)
//                .progress(53)
//                .tickCount(7)
//                .showTickMarksType(TickMarkType.OVAL)
//                .tickMarksColor(getResources().getColor(R.color.color_blue, null))
//                .tickMarksSize(13)//dp
//                .showTickTexts(true)
//                .tickTextsColor(getResources().getColor(R.color.color_pink))
//                .tickTextsSize(13)//sp
//                .tickTextsTypeFace(Typeface.MONOSPACE)
//                .showIndicatorType(IndicatorType.ROUNDED_RECTANGLE)
//                .indicatorColor(getResources().getColor(R.color.color_blue))
//                .indicatorTextColor(Color.parseColor("#ffffff"))
//                .indicatorTextSize(13)//sp
//                .thumbColor(getResources().getColor(R.color.colorAccent, null))
//                .thumbSize(14)
//                .trackProgressColor(getResources().getColor(R.color.colorAccent,null))
//                .trackProgressSize(4)
//                .trackBackgroundColor(getResources().getColor(R.color.color_gray))
//                .trackBackgroundSize(2)
//                .onlyThumbDraggable(false)
//                .build();

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_list_player;
    }

    private void musicListChange(List<Long> input) {
        if (input != null && input.size() > 0) {
            musicRep.musicsIds(input).observe(this, adapter::submitList);
        }
    }

    private void bufferChange(Integer percent) {
//        ViewGroup.LayoutParams params = binding.viewBuffer.getLayoutParams();
//        if (percent == 100) {
//            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
//        } else {
//            params.width = (progressWidth / 100) * percent;
//        }
//        binding.viewBuffer.setLayoutParams(params);
    }

    private void percentChange(Integer percent) {
        binding.indicator.setProgress(percent);
//        ViewGroup.LayoutParams params = binding.viewProgress.getLayoutParams();
//        if (percent >= 99) {
//            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
//        } else {
//            params.width = (progressWidth / 100) * percent;
//        }
//        binding.viewProgress.setLayoutParams(params);
    }

    private void statusChange(Status val) {
        if (val == null) return;

        switch (val) {
            case PLAY:
                binding.btnNext.setVisibility(View.VISIBLE);
                binding.progressBar.setVisibility(View.GONE);
                binding.btnPlayPause.setVisibility(View.VISIBLE);
                binding.btnPlayPause.setIcon(ContextCompat.getDrawable(activity, R.drawable.ic_pause));
                break;
            case PAUSE:
            case COMPLETED:
                binding.btnNext.setVisibility(View.VISIBLE);
                binding.progressBar.setVisibility(View.GONE);
                binding.btnPlayPause.setVisibility(View.VISIBLE);
                binding.btnPlayPause.setIcon(ContextCompat.getDrawable(activity, R.drawable.ic_play));
                break;
            case PREPARING:
                binding.btnNext.setVisibility(View.GONE);
                binding.btnPlayPause.setVisibility(View.GONE);
                binding.btnPlayPause.setIcon(ContextCompat.getDrawable(activity, R.drawable.ic_pause));
                binding.progressBar.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        musicListChange(Application.playing.getMusicList());
        bufferChange(Application.playing.getBuffer());
        percentChange(Application.playing.getPercent());
        statusChange(Application.playing.getStatus());
        binding.setMusic(Application.playing.getMusic());

        Application.playing.musicListObservable().subscribeOn(Schedulers.io())
                .subscribe(new SubjectWrapper<List<Long>>() {
                    @Override
                    public void onChange(List<Long> val) {
                        musicListChange(val);
                    }
                });

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
