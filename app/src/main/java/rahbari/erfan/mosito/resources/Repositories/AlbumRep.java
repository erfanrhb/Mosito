package rahbari.erfan.mosito.resources.Repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import java.util.ArrayList;
import java.util.List;

import rahbari.erfan.mosito.Application;
import rahbari.erfan.mosito.interfaces.Response;
import rahbari.erfan.mosito.models.Album;
import rahbari.erfan.mosito.models.AlbumLibrary;
import rahbari.erfan.mosito.models.AlbumMusicRole;
import rahbari.erfan.mosito.models.Artist;
import rahbari.erfan.mosito.models.ErrorHandler;
import rahbari.erfan.mosito.models.Music;
import rahbari.erfan.mosito.models.MusicLibrary;
import rahbari.erfan.mosito.models.Paginate;
import rahbari.erfan.mosito.models.UserAlbum;

import rahbari.erfan.mosito.resources.Apis.AlbumApi;
import rahbari.erfan.mosito.resources.BaseRoom;
import rahbari.erfan.mosito.resources.Daos.AlbumDao;
import rahbari.erfan.mosito.utils.CallWrapper;
import rahbari.erfan.mosito.utils.Utils;
import io.reactivex.schedulers.Schedulers;

public class AlbumRep {
    private final AlbumDao albumDao;
    private final AlbumApi albumApi;

    public AlbumRep() {
        albumDao = BaseRoom.getDatabase(Application.getInstance()).albumDao();
        albumApi = new AlbumApi();
    }

    public void UserAlbumUpdateOrCreate(UserAlbum x) {
        if (albumDao.existLibrary(x.getAlbum_id()) > 0)
            albumDao.updateLibrary(x.getCreated_at(), x.getDeleted_at(), x.getAlbum_id());
        else
            albumDao.addLibrary(x);
    }

    public AlbumDao getAlbumDao() {
        return albumDao;
    }

    public LiveData<AlbumLibrary> albumMusic(long albumId) {
        return albumDao.album(albumId);
    }

    public LiveData<PagedList<MusicLibrary>> musics(long albumId) {
        return new LivePagedListBuilder<>(albumDao.musics(albumId), 20).build();
    }

    public LiveData<PagedList<AlbumLibrary>> libraryPaginate() {
        return new LivePagedListBuilder<>(albumDao.libraryPaginate(), 20).build();
    }

    public void addLibrary(Album album) {
        if (albumDao.existLibrary(album.getId()) > 0) {
            albumDao.updateLibrary(Utils.today(), album.getId());
        } else {
            albumDao.addLibrary(new UserAlbum(album.getId(), Utils.today()));
        }
    }

    public void subLibrary(Album album) {
        if (albumDao.existLibrary(album.getId()) > 0) {
            albumDao.subLibrary(Utils.today(), album.getId());
        }
    }

    public LiveData<PagedList<AlbumLibrary>> whereId(long[] ids) {
        return new LivePagedListBuilder<>(albumDao.whereInIds(ids), 20).build();
    }

    public void show(long albumId, Response<Boolean> response) {
        albumApi.album(albumId).subscribeOn(Schedulers.io()).subscribe(new CallWrapper<Album>() {
            @Override
            protected void onSuccess(Album album) {
                updateOrCreate(album);

                // add musics with relations
                new MusicRep().updateOrCreate(album.getMusics());
                addToAlbum(albumId, album.getMusics());

                ArtistRep artistRep = new ArtistRep();
                // add artists with relations
                artistRep.updateOrCreate(album.getArtists());
                for (Artist x : album.getArtists()) {
                    artistRep.addAlbumsToArtist(x.getId(), album);
                }

                response.response(true);
            }

            @Override
            protected void onError(int status, ErrorHandler failed) {
                Log.e("AlbumRep", failed.getMessage());
                response.response(false);
            }
        });
    }

    private void addToAlbum(long albumId, List<Music> items) {
        List<Music> musics = new ArrayList<>();
        for (Music x : items) {
            if (albumDao.existInAlbum(x.getId(), albumId) == 0) {
                musics.add(x);
            }
        }

        AlbumMusicRole[] toCreate = new AlbumMusicRole[musics.size()];
        for (int i = 0; i < musics.size(); i++) {
            AlbumMusicRole role = new AlbumMusicRole();
            role.setAlbum_id(albumId);
            role.setMusic_id(musics.get(i).getId());
            toCreate[i] = role;
        }
        albumDao.addToAlbum(toCreate);
    }

    public void updateOrCreate(List<Album> albums) {
        List<Album> create = new ArrayList<>();
        List<Album> update = new ArrayList<>();

        ArtistRep artistRep = new ArtistRep();

        for (Album x : albums) {
            if (albumDao.count(x.getId()) > 0) {
                update.add(x);
            } else {
                create.add(x);
            }

            if (x.getArtists() != null) {
                artistRep.updateOrCreate(x.getArtists());
                artistRep.addAlbumsToArtist(x.getArtists(), x);
            }
        }

        Album[] toCreate = new Album[create.size()];
        Album[] toUpdate = new Album[update.size()];

        create.toArray(toCreate);
        update.toArray(toUpdate);

        albumDao.create(toCreate);
        albumDao.update(toUpdate);

        for (Album x : albums) {
            if (x.getMusics() != null && x.getMusics().size() > 0) {
                addToAlbum(x.getId(), x.getMusics());
            }
        }
    }

    public void updateOrCreate(Album album) {
        if (albumDao.count(album.getId()) > 0) {
            albumDao.update(album);
        } else {
            albumDao.create(album);
        }
    }

    public void latest(int page, int pagination) {
        albumApi.albums(pagination, page).subscribeOn(Schedulers.io()).subscribe(new CallWrapper<Paginate<Album>>() {
            @Override
            protected void onSuccess(Paginate<Album> albumPaginate) {
                updateOrCreate(albumPaginate.data);
            }

            @Override
            protected void onError(int status, ErrorHandler response) {
                Log.e("AlbumRep", response.getMessage());
            }
        });
    }
    //

    public LiveData<PagedList<AlbumLibrary>> latest(int limit) {
        return new LivePagedListBuilder<>(albumDao.latest(limit), 25).build();
    }

    public LiveData<PagedList<AlbumLibrary>> latest() {
        return new LivePagedListBuilder<>(albumDao.latest(), 25).build();
    }
}
