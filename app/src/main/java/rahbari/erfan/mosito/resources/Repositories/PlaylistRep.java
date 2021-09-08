package rahbari.erfan.mosito.resources.Repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rahbari.erfan.mosito.Application;
import rahbari.erfan.mosito.interfaces.Response;
import rahbari.erfan.mosito.models.ErrorHandler;
import rahbari.erfan.mosito.models.Music;
import rahbari.erfan.mosito.models.MusicLibrary;
import rahbari.erfan.mosito.models.Paginate;
import rahbari.erfan.mosito.models.Playlist;
import rahbari.erfan.mosito.models.PlaylistLibrary;
import rahbari.erfan.mosito.models.PlaylistMusicRole;
import rahbari.erfan.mosito.models.UserPlaylist;
import rahbari.erfan.mosito.resources.Apis.PlaylistApi;
import rahbari.erfan.mosito.resources.BaseRoom;
import rahbari.erfan.mosito.resources.Daos.PlaylistDao;
import rahbari.erfan.mosito.resources.models.UploadRequest;
import rahbari.erfan.mosito.utils.CallWrapper;
import rahbari.erfan.mosito.utils.Utils;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;

public class PlaylistRep {
    private final PlaylistDao playlistDao;
    private final PlaylistApi playlistApi;

    public PlaylistRep() {
        playlistDao = BaseRoom.getDatabase(Application.getInstance()).playlistDao();
        playlistApi = new PlaylistApi();
    }

    public void UserPlaylistUpdateOrCreate(UserPlaylist x) {
        if (playlistDao.existLibrary(x.getPlaylist_id()) > 0)
            playlistDao.updateLibrary(x.getCreated_at(), x.getDeleted_at(), x.getPlaylist_id());
        else
            playlistDao.addLibrary(x);
    }

    public PlaylistDao getPlaylistDao() {
        return playlistDao;
    }

    public LiveData<PlaylistLibrary> playlistMusic(long playlistId) {
        return playlistDao.playlist(playlistId);
    }

    public LiveData<PagedList<MusicLibrary>> musics(long playlistId) {
        return new LivePagedListBuilder<>(playlistDao.musics(playlistId), 20).build();
    }

    public LiveData<PagedList<PlaylistLibrary>> libraryPaginate() {
        return new LivePagedListBuilder<>(playlistDao.libraryPaginate(), 20).build();
    }

    public void addLibrary(Playlist playlist) {
        if (playlistDao.existLibrary(playlist.getId()) > 0) {
            playlistDao.updateLibrary(Utils.today(), playlist.getId());
        } else {
            playlistDao.addLibrary(new UserPlaylist(playlist.getId(), Utils.today()));
        }
    }

    public void subLibrary(Playlist playlist) {
        if (playlistDao.existLibrary(playlist.getId()) > 0) {
            playlistDao.subLibrary(Utils.today(), playlist.getId());
        }
    }


    private void addToPlaylist(long playlistId, List<Music> items) {
        List<Music> musics = new ArrayList<>();
        for (Music x : items) {
            if (playlistDao.existMusic(playlistId, x.getId()) == 0) {
                musics.add(x);
            }
        }

        PlaylistMusicRole[] toCreate = new PlaylistMusicRole[musics.size()];

        for (int i = 0; i < musics.size(); i++) {
            PlaylistMusicRole role = new PlaylistMusicRole();
            role.setPlaylist_id(playlistId);
            role.setMusic_id(musics.get(i).getId());
            toCreate[i] = role;
        }

        playlistDao.addToPlaylist(toCreate);
    }

    public LiveData<PagedList<PlaylistLibrary>> whereId(long[] ids) {
        return new LivePagedListBuilder<>(playlistDao.whereInIds(ids), 20).build();
    }

    public void updateOrCreate(List<Playlist> playlists) {
        List<Playlist> create = new ArrayList<>();
        List<Playlist> update = new ArrayList<>();

        for (Playlist x : playlists) {
            if (playlistDao.count(x.getId()) > 0) {
                update.add(x);
            } else {
                create.add(x);
            }
        }

        Playlist[] toCreate = new Playlist[create.size()];
        Playlist[] toUpdate = new Playlist[update.size()];

        create.toArray(toCreate);
        update.toArray(toUpdate);

        playlistDao.create(toCreate);
        playlistDao.update(toUpdate);

        for (Playlist x : playlists) {
            if (x.getMusics() != null && x.getMusics().size() > 0) {
                addToPlaylist(x.getId(), x.getMusics());
            }
        }
    }

    public void updateOrCreate(Playlist playlist) {
        if (playlistDao.count(playlist.getId()) > 0) {
            playlistDao.update(playlist);
        } else {
            playlistDao.create(playlist);
        }
    }

    public LiveData<List<PlaylistLibrary>> latest() {
        return playlistDao.latest();
    }

    public void musics(long playlistId, int pagination, int page) {
        playlistApi.musics(playlistId, pagination, page).subscribeOn(Schedulers.io()).subscribe(new CallWrapper<Paginate<Music>>() {
            @Override
            protected void onSuccess(Paginate<Music> musicPaginate) {
                addToPlaylist(playlistId, musicPaginate.data);
            }

            @Override
            protected void onError(int status, ErrorHandler response) {
                Log.e("PlaylistRep", response.getMessage());
            }
        });
    }

    public void create(String title, String type, String description, File picture, UploadRequest.UploadCallbacks uploadCallbacks, Response<Playlist> playlistResponse, Response<ErrorHandler> handlerResponse) {
        playlistApi.store(
                MultipartBody.Part.createFormData("title", title != null ? title : ""),
                MultipartBody.Part.createFormData("type", type != null ? type : ""),
                MultipartBody.Part.createFormData("description", description != null ? description : ""),
                picture != null ? MultipartBody.Part.createFormData("picture", picture.getName(), new UploadRequest(picture, "image/*", uploadCallbacks)) :
                        MultipartBody.Part.createFormData("picture", "")
        ).subscribeOn(Schedulers.io()).subscribe(new CallWrapper<Playlist>() {
            @Override
            protected void onSuccess(Playlist playlist) {
                updateOrCreate(playlist);
                playlistResponse.response(playlist);
            }

            @Override
            protected void onError(int status, ErrorHandler response) {
                handlerResponse.response(response);
            }
        });
    }

    public void index(int paginate, int page) {
        playlistApi.playlists(paginate, page).subscribeOn(Schedulers.io()).subscribe(new CallWrapper<Paginate<Playlist>>() {
            @Override
            protected void onSuccess(Paginate<Playlist> playlistPaginate) {
                updateOrCreate(playlistPaginate.data);
            }

            @Override
            protected void onError(int status, ErrorHandler response) {
                Log.e("PlaylistRep", response.getMessage());
            }
        });
    }
}
