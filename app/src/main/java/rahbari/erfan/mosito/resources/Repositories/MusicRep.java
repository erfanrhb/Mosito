package rahbari.erfan.mosito.resources.Repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import java.util.ArrayList;
import java.util.List;

import rahbari.erfan.mosito.Application;
import rahbari.erfan.mosito.interfaces.Response;
import rahbari.erfan.mosito.models.Download;
import rahbari.erfan.mosito.models.ErrorHandler;
import rahbari.erfan.mosito.models.Music;
import rahbari.erfan.mosito.models.MusicLibrary;
import rahbari.erfan.mosito.models.Paginate;
import rahbari.erfan.mosito.models.UserMusic;
import rahbari.erfan.mosito.resources.Apis.DownloadApi;
import rahbari.erfan.mosito.resources.Apis.MusicApi;
import rahbari.erfan.mosito.resources.BaseRoom;
import rahbari.erfan.mosito.resources.Daos.MusicDao;
import rahbari.erfan.mosito.utils.CallWrapper;
import rahbari.erfan.mosito.utils.Utils;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class MusicRep {
    private final MusicDao musicDao;
    private final MusicApi musicApi;
    private final DownloadApi downloadApi;

    public MusicRep() {
        musicApi = new MusicApi();
        downloadApi = new DownloadApi();
        musicDao = BaseRoom.getDatabase(Application.getInstance()).musicDao();
    }

    public void UserMusicUpdateOrCreate(UserMusic x) {
        if (musicDao.existLibrary(x.getMusic_id()) > 0)
            musicDao.updateLibrary(x.getCreated_at(), x.getDeleted_at(), x.getMusic_id());
        else
            musicDao.addLibrary(x);
    }

    public MusicDao getMusicDao() {
        return musicDao;
    }

    public LiveData<List<MusicLibrary>> downloadQueue() {
        return musicDao.downloadQueue();
    }

    public LiveData<PagedList<MusicLibrary>> libraryPaginate() {
        return new LivePagedListBuilder<>(musicDao.libraryPaginate(), 20).build();
    }

    public LiveData<PagedList<MusicLibrary>> musicsIds(List<Long> ids) {
        return new LivePagedListBuilder<>(musicDao.musicsIds(ids), 20).build();
    }

    public LiveData<PagedList<MusicLibrary>> recentlyAdded() {
        return new LivePagedListBuilder<>(musicDao.recentlyAdded(), 20).build();
    }

    public MusicLibrary musicLibrary(long musicId) {
        return musicDao.musicLibrary(musicId);
    }

    public void download(Music music) {
        musicDao.download(music.getId());
    }

    public void addLibrary(Music music) {
        if (musicDao.existLibrary(music.getId()) > 0) {
            musicDao.updateLibrary(Utils.today(), music.getId());
        } else {
            musicDao.addLibrary(new UserMusic(music.getId(), Utils.today()));
        }
    }

    public void subLibrary(Music music) {
        if (musicDao.existLibrary(music.getId()) > 0) {
            musicDao.subLibrary(Utils.today(), music.getId());
        }
    }

    public void update(Music... music) {
        musicDao.update(music);
    }

    public void create(Music... music) {
        musicDao.insert(music);
    }

    public LiveData<PagedList<MusicLibrary>> whereId(long[] ids) {
        return new LivePagedListBuilder<>(musicDao.whereInIds(ids), 20).build();
    }

    public void updateOrCreate(List<Music> musics) {
        List<Music> create = new ArrayList<>();
        List<Music> update = new ArrayList<>();

        ArtistRep artistRep = new ArtistRep();

        for (Music x : musics) {
            if (musicDao.count(x.getId()) > 0) {
                update.add(x);
            } else {
                create.add(x);
            }

            if (x.getArtists() != null) {
                artistRep.updateOrCreate(x.getArtists());
                artistRep.addMusicsToArtist(x.getArtists(), x);
            }
        }

        Music[] toCreate = new Music[create.size()];
        Music[] toUpdate = new Music[update.size()];

        create.toArray(toCreate);
        update.toArray(toUpdate);

        create(toCreate);
        update(toUpdate);
    }

    /*** rewrite by new apis ***/

    public void requestFile(long musicId, Response<Download> response, Response<ErrorHandler> failed) {
        downloadApi.requestFile(musicId).subscribeOn(Schedulers.io()).subscribe(new CallWrapper<Download>() {
            @Override
            protected void onSuccess(Download download) {
                response.response(download);
            }

            @Override
            protected void onError(int status, ErrorHandler response) {
                failed.response(response);
            }
        });
    }

    public void requestFile(Music music, Response<ResponseBody> response, Response<ErrorHandler> failed) {
        downloadApi.requestFile(music.getId()).subscribeOn(Schedulers.io()).subscribe(new CallWrapper<Download>() {
            @Override
            protected void onSuccess(Download download) {
                download(download.getUrl(), response, failed);
            }

            @Override
            protected void onError(int status, ErrorHandler response) {
                failed.response(response);
            }
        });
    }

    private void download(String link, Response<ResponseBody> response, Response<ErrorHandler> failed) {
        downloadApi.download(link).observeOn(Schedulers.io()).subscribe(new CallWrapper<ResponseBody>() {
            @Override
            protected void onSuccess(ResponseBody responseBody) {
                response.response(responseBody);
            }

            @Override
            protected void onError(int status, ErrorHandler response) {
                failed.response(response);
            }
        });
    }

    public void latestApi(int page, int pagination) {
        Log.e("MusicRep.latestApi", "fetch page " + page + " / " + pagination);
        musicApi.musicsLatest(pagination, page, "musics.created_at").subscribe(new CallWrapper<Paginate<Music>>() {
            @Override
            protected void onSuccess(Paginate<Music> musicPaginate) {
                updateOrCreate(musicPaginate.data);
            }

            @Override
            protected void onError(int status, ErrorHandler response) {
                Log.e("MusicRep.latestApi", response.getMessage());
            }
        });
    }

    public void mostPlayed(int pagination) {
        Log.e("MusicRep.latestApi", "fetch page " + pagination);
        musicApi.musicsMostPlayed(pagination, "musics.created_at", pagination).subscribe(new CallWrapper<Paginate<Music>>() {
            @Override
            protected void onSuccess(Paginate<Music> musicPaginate) {
                updateOrCreate(musicPaginate.data);
            }

            @Override
            protected void onError(int status, ErrorHandler response) {
                Log.e("MusicRep.latestApi", response.getMessage());
            }
        });
    }


    public LiveData<PagedList<MusicLibrary>> latestDao() {
        return new LivePagedListBuilder<>(musicDao.latest(), 25).build();
    }

    public LiveData<PagedList<MusicLibrary>> latestLimitDao(int limit) {
        return new LivePagedListBuilder<>(musicDao.latestLimit(limit), 25).build();
    }

    public LiveData<PagedList<Music>> mostPlayedDao(int limit) {
        return new LivePagedListBuilder<>(musicDao.mostPlayed(Utils.getDay(-14), limit), 25).build();
    }
}
