package rahbari.erfan.mosito.services;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.LifecycleService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import rahbari.erfan.mosito.Application;
import rahbari.erfan.mosito.enums.Command;
import rahbari.erfan.mosito.enums.Status;
import rahbari.erfan.mosito.models.Music;
import rahbari.erfan.mosito.models.MusicLibrary;
import rahbari.erfan.mosito.resources.Repositories.MusicRep;
import rahbari.erfan.mosito.utils.SubjectWrapper;
import io.reactivex.schedulers.Schedulers;

public class PlayService extends LifecycleService implements MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {

    private MusicRep musicRep;
    private MediaPlayer player;
    private List<Music> musicList;
    private Timer timer = new Timer();

    @Override
    public void onCreate() {
        super.onCreate();

        musicRep = new MusicRep();
        musicList = new ArrayList<>();

        Application.playing.seekToObservable().observeOn(Schedulers.io()).subscribe(new SubjectWrapper<Integer>() {
            @Override
            public void onChange(Integer val) {
                seekTo(val);
            }
        });

        Application.playing.musicIdObservable().observeOn(Schedulers.io()).subscribe(new SubjectWrapper<Music>() {
            @Override
            public void onChange(Music val) {
                prepare(val);
            }
        });

        Application.playing.commandObservable().subscribeOn(Schedulers.io()).subscribe(new SubjectWrapper<Command>() {
            @Override
            public void onChange(Command val) {
                switch (val) {
                    case Back:
                        goBack();
                        break;
                    case Next:
                        goNext();
                        break;
                    case Play:
                        goPlay();
                        break;
                    case Pause:
                        goPause();
                        break;
                    case Toggle:
                        goToggle();
                }
            }
        });
    }

    private void seekTo(int pos) {
        if (player == null) return;

        if (player.isPlaying()) {
            player.seekTo(pos * 1000);
        } else
            goPlay();
    }

    private void goToggle() {
        if (player != null && player.isPlaying())
            goPause();
        else
            goPlay();
    }

    private void goPause() {
        if (player != null && player.isPlaying()) {
            Application.playing.setStatus(Status.PAUSE);
            player.pause();
            clearTimer();
        }
    }

    private void goPlay() {
        if (Application.playing.getMusic() == null && Application.playing.getMusicList() == null) {
            playLibrary();
            return;
        }

        if (player != null && player.isPlaying()) {
            return;
        }

        if (player == null && Application.playing.getMusic() != null) {
            prepare(Application.playing.getMusic());
        } else if (player != null && !player.isPlaying()) {
            Application.playing.setStatus(Status.PLAY);
            player.seekTo(Application.playing.getPosition());
            player.start();
            startTimer();
        }
    }

    private void playLibrary() {

        new Thread(() -> {
            List<Long> ids = musicRep.getMusicDao().libraryIds();

            if (ids == null || ids.size() == 0) return;

            Music music = musicRep.getMusicDao().music(ids.get(0));

            Handler handler = new Handler(Looper.getMainLooper());
            handler.post((Runnable) () -> {
                Application.playing.setMusicList(ids);
                Application.playing.setMusic(music);
            });

        }).start();
    }

    private void goBack() {
        new Thread(() -> {

            List<Long> musics = Application.playing.getMusicList();

            if (musics != null && musics.size() > 0) {
                int index = musics.indexOf(Application.playing.getMusic().getId());
                if (index >= 0) {
                    Application.playing.setMusic(musicRep.getMusicDao().music(index == 0 ? musics.get(musics.size() - 1) : musics.get(index - 1)));
                } else {
                    Application.playing.setMusic(musicRep.getMusicDao().music(musics.get(0)));
                }
            } else if (Application.playing.getMusic() != null) {
                prepare(Application.playing.getMusic());
            } else {
                playLibrary();
            }

        }).start();
    }

    private void goNext() {
        new Thread(() -> {

            List<Long> musics = Application.playing.getMusicList();

            if (musics != null && musics.size() > 0) {
                int index = musics.indexOf(Application.playing.getMusic().getId());
                if (index >= 0) {
                    Application.playing.setMusic(musicRep.getMusicDao().music(musics.size() - 2 >= index ? musics.get(index + 1) : musics.get(0)));
                } else {
                    Application.playing.setMusic(musicRep.getMusicDao().music(musics.get(0)));
                }
            } else if (Application.playing.getMusic() != null) {
                prepare(Application.playing.getMusic());
            } else {
                playLibrary();
            }

        }).start();
    }

    private void prepare(Music music) {
        if (music == null) return;

        MusicLibrary musicLibrary = musicRep.musicLibrary(music.getId());

        if (player == null) {
            player = new MediaPlayer();
            player.setOnBufferingUpdateListener(this);
            player.setOnPreparedListener(this);
            player.setOnCompletionListener(this);
            player.setOnErrorListener(this);
        }

        if (player.isPlaying()) {
            player.stop();
            player.reset();
        }


        Application.playing.setPercent(0);

        try {
            if (musicLibrary.getLibrary() != null && musicLibrary.getLibrary().getUri() != null && new File(musicLibrary.getLibrary().getUri()).exists()) {
                File musicFile = new File(musicLibrary.getLibrary().getUri());

                Application.playing.setBuffer(100);
                Application.playing.setStatus(Status.PLAY);

                player.setDataSource(getApplicationContext(), Uri.fromFile(musicFile));
                player.prepare();
                player.start();

            } else {
                Application.playing.setBuffer(0);
                Application.playing.setStatus(Status.PREPARING);

                musicRep.requestFile(music.getId(), response -> {

                    try {
                        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        player.setDataSource(response.getUrl());
                        player.prepare();
                        player.start();

                    } catch (Exception e) {
                        Log.e("PlayerService", e.getMessage(), e);
                    }

                }, response -> Log.e("PlayerService", response.getMessage()));
            }
        } catch (Exception e) {
            Log.e("PlayerService", e.getMessage(), e);
        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        Application.playing.setBuffer(percent);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Application.playing.setStatus(Status.COMPLETED);

        if (player != null) {
            if (player.isPlaying()) {
                player.pause();
            }
            player = null;
        }

        clearTimer();
        goNext();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.e("PlayService", "onPrepared");
        Application.playing.setStatus(Status.PLAY);
        startTimer();
    }

    private void startTimer() {
        clearTimer();
        timer = new Timer();
        timer.schedule(new playerTimer(), 0, 250);
    }

    private void clearTimer() {
        if (timer == null) return;
        timer.cancel();
        timer.purge();
        timer = null;
    }

    class playerTimer extends TimerTask {
        @Override
        public void run() {
            if (player != null && player.isPlaying()) {
                Application.playing.setPosition(player.getCurrentPosition());
                Application.playing.setPercent((player.getCurrentPosition() * 100) / player.getDuration());
            }
        }
    }

}
