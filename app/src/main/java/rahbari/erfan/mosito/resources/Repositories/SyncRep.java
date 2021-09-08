package rahbari.erfan.mosito.resources.Repositories;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import rahbari.erfan.mosito.interfaces.Response;
import rahbari.erfan.mosito.models.Album;
import rahbari.erfan.mosito.models.ErrorHandler;
import rahbari.erfan.mosito.models.Music;
import rahbari.erfan.mosito.models.Paginate;
import rahbari.erfan.mosito.models.Playlist;
import rahbari.erfan.mosito.models.User;
import rahbari.erfan.mosito.models.UserAlbum;
import rahbari.erfan.mosito.models.UserMusic;
import rahbari.erfan.mosito.models.UserPlaylist;
import rahbari.erfan.mosito.resources.Apis.LibraryApi;
import rahbari.erfan.mosito.resources.models.ListIds;
import rahbari.erfan.mosito.resources.models.SyncRequest;
import rahbari.erfan.mosito.resources.models.SyncList;
import rahbari.erfan.mosito.resources.models.SyncResponse;
import rahbari.erfan.mosito.utils.CallWrapper;
import rahbari.erfan.mosito.utils.Utils;
import io.reactivex.schedulers.Schedulers;

public class SyncRep {
    private final LibraryApi api;

    public SyncRep() {
        api = new LibraryApi();
    }

    public void sync(Response<Boolean> onResponse) {
        UserRep userRep = new UserRep();
        MusicRep musicRep = new MusicRep();
        AlbumRep albumRep = new AlbumRep();
        PlaylistRep playlistRep = new PlaylistRep();

        User user = userRep.user();
        if (user == null) return;

        String updated_at = Utils.getShare("synced_at", null);

        Log.e("SyncRep", "sync started for date: " + updated_at);

        List<SyncList> libraries = new ArrayList<>();
        List<UserMusic> musics = updated_at != null ? musicRep.getMusicDao().libraryRoot(updated_at) : musicRep.getMusicDao().libraryRoot();
        List<UserAlbum> albums = updated_at != null ? albumRep.getAlbumDao().libraryRoot(updated_at) : albumRep.getAlbumDao().libraryRoot();
        List<UserPlaylist> playlists = updated_at != null ? playlistRep.getPlaylistDao().libraryRoot(updated_at) : playlistRep.getPlaylistDao().libraryRoot();

        for (UserMusic x : musics) {
            libraries.add(new SyncList(x.getMusic_id(), "App\\Music", x.getCreated_at(), x.getDeleted_at()));
        }

        for (UserAlbum x : albums) {
            libraries.add(new SyncList(x.getAlbum_id(), "App\\Album", x.getCreated_at(), x.getDeleted_at()));
        }

        for (UserPlaylist x : playlists) {
            libraries.add(new SyncList(x.getPlaylist_id(), "App\\Playlist", x.getCreated_at(), x.getDeleted_at()));
        }

        Log.e("SyncRep", "sending " + musics.size() + " musics, " + albums.size() + " albums, " + playlists.size() + " playlists");

        api.index(new SyncRequest(libraries, updated_at)).subscribeOn(Schedulers.io()).subscribe(new CallWrapper<SyncResponse>() {
            @Override
            protected void onSuccess(SyncResponse response) {
                for (SyncList x : response.getLibraries()) {
                    switch (x.getArt_type()) {
                        case "App\\Music":
                            UserMusic userMusic = new UserMusic(x.getArt_id(), x.getCreated_at());
                            userMusic.setDeleted_at(x.getDeleted_at());
                            musicRep.UserMusicUpdateOrCreate(userMusic);
                            break;
                        case "App\\Album":
                            UserAlbum userAlbum = new UserAlbum(x.getArt_id(), x.getCreated_at());
                            userAlbum.setDeleted_at(x.getDeleted_at());
                            albumRep.UserAlbumUpdateOrCreate(userAlbum);
                            break;
                        case "App\\Playlist":
                            UserPlaylist userPlaylist = new UserPlaylist(x.getArt_id(), x.getCreated_at());
                            userPlaylist.setDeleted_at(x.getDeleted_at());
                            playlistRep.UserPlaylistUpdateOrCreate(userPlaylist);
                    }
                }
                Utils.setShare("synced_at", response.getUpdated_at());
                Log.e("SyncRep", "synced " + response.getLibraries().size() + " record at " + response.getUpdated_at());
                onResponse.response(true);
            }

            @Override
            protected void onError(int status, ErrorHandler response) {
                Log.e("SyncRep", response.getMessage());
            }
        });
    }

    public void albumWithoutModel() {
        AlbumRep albumRep = new AlbumRep();

        List<Long> ids = albumRep.getAlbumDao().withoutModel();
        if (ids.size() <= 0) return;

        Log.e("SyncRep", "send to store " + ids.size() + " albums");
        api.albums(new ListIds(ids)).subscribeOn(Schedulers.io()).subscribe(new CallWrapper<Paginate<Album>>() {
            @Override
            protected void onSuccess(Paginate<Album> albumPaginate) {
                Log.e("SyncRep", "stored " + albumPaginate.getTotal() + " albums");
                albumRep.updateOrCreate(albumPaginate.data);
                if (albumPaginate.getTotal() > 0) {
                    albumWithoutModel();
                }
            }

            @Override
            protected void onError(int status, ErrorHandler response) {
                Log.e("SyncRep", response.getMessage());
            }
        });
    }

    public void musicWithoutModel() {
        MusicRep musicRep = new MusicRep();

        List<Long> ids = musicRep.getMusicDao().withoutModel();
        if (ids.size() <= 0) return;

        Log.e("SyncRep", "send to store " + ids.size() + " musics");
        api.musics(new ListIds(ids)).subscribeOn(Schedulers.io()).subscribe(new CallWrapper<Paginate<Music>>() {
            @Override
            protected void onSuccess(Paginate<Music> musicPaginate) {
                Log.e("SyncRep", "stored " + musicPaginate.getTotal() + " musics");
                musicRep.updateOrCreate(musicPaginate.data);
                if (musicPaginate.getTotal() > 0) {
                    musicWithoutModel();
                }
            }

            @Override
            protected void onError(int status, ErrorHandler response) {
                Log.e("SyncRep", response.getMessage());
            }
        });
    }

    public void playlistWithoutModel() {
        PlaylistRep playlistRep = new PlaylistRep();

        List<Long> ids = playlistRep.getPlaylistDao().withoutModel();
        if (ids.size() <= 0) return;

        Log.e("SyncRep", "send to store " + ids.size() + " playlists");
        api.playlists(new ListIds(ids)).subscribeOn(Schedulers.io()).subscribe(new CallWrapper<Paginate<Playlist>>() {
            @Override
            protected void onSuccess(Paginate<Playlist> playlistPaginate) {
                Log.e("SyncRep", "stored " + playlistPaginate.getTotal() + "  playlists");
                playlistRep.updateOrCreate(playlistPaginate.data);
                if (playlistPaginate.getTotal() > 0) {
                    playlistWithoutModel();
                }
            }

            @Override
            protected void onError(int status, ErrorHandler response) {
                Log.e("SyncRep", response.getMessage());
            }
        });
    }
}
