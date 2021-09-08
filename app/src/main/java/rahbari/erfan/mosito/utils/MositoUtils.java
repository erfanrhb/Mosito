package rahbari.erfan.mosito.utils;

import android.content.Intent;

import androidx.paging.PagedList;

import java.util.ArrayList;
import java.util.List;

import rahbari.erfan.mosito.Application;
import rahbari.erfan.mosito.R;
import rahbari.erfan.mosito.interfaces.Response;
import rahbari.erfan.mosito.models.Album;
import rahbari.erfan.mosito.models.Music;
import rahbari.erfan.mosito.models.MusicLibrary;
import rahbari.erfan.mosito.resources.Repositories.AlbumRep;
import rahbari.erfan.mosito.resources.Repositories.MusicRep;

public class MositoUtils {
    public static void addToDownloadQueue(Music music) {
        new Thread(() -> new MusicRep().download(music)).start();
    }

    public static void addLibraryAlbum(Album album) {
        new Thread(() -> {
            new AlbumRep().addLibrary(album);
            Intent i = new Intent("TOAST_DIALOG");
            i.putExtra("message", Application.getInstance().getString(R.string.added_to_library));
            Application.getInstance().sendBroadcast(i);
            Application.getInstance().sendBroadcast(new Intent("SYNC_SERVICE"));
        }).start();
    }

    public static void subLibraryAlbum(Album album) {
        new Thread(() -> {
            new AlbumRep().subLibrary(album);
            Intent i = new Intent("TOAST_DIALOG");
            i.putExtra("message", Application.getInstance().getString(R.string.removed_from_library));
            Application.getInstance().sendBroadcast(i);
            Application.getInstance().sendBroadcast(new Intent("SYNC_SERVICE"));
        }).start();
    }

    public static void addLibraryMusic(Music music) {
        new Thread(() -> {
            new MusicRep().addLibrary(music);
            Intent i = new Intent("TOAST_DIALOG");
            i.putExtra("message", Application.getInstance().getString(R.string.added_to_library));
            Application.getInstance().sendBroadcast(i);
            Application.getInstance().sendBroadcast(new Intent("SYNC_SERVICE"));
        }).start();
    }

    public static void play(Music music) {
        Application.playing.setMusic(music);
    }

    public static void play(PagedList<MusicLibrary> musicLibraries, Music music) {
        Application.playing.setMusic(music);

        List<Long> musics = new ArrayList<>();
        for (MusicLibrary musicLibrary : musicLibraries) {
            if (musicLibrary != null && musicLibrary.music != null)
                musics.add(musicLibrary.music.getId());
        }
        Application.playing.setMusicList(musics);
    }

    public static void subLibraryMusic(Music music, Response<Boolean> done) {
        new Thread(() -> {
            new MusicRep().subLibrary(music);
            Intent i = new Intent("TOAST_DIALOG");
            i.putExtra("message", Application.getInstance().getString(R.string.removed_from_library));
            Application.getInstance().sendBroadcast(i);
            Application.getInstance().sendBroadcast(new Intent("SYNC_SERVICE"));
            done.response(true);
        }).start();
    }
}
