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
import rahbari.erfan.mosito.models.Artist;
import rahbari.erfan.mosito.models.ArtistAlbumRole;
import rahbari.erfan.mosito.models.ArtistAlbums;
import rahbari.erfan.mosito.models.ArtistMusicRole;
import rahbari.erfan.mosito.models.ArtistMusics;
import rahbari.erfan.mosito.models.ErrorHandler;
import rahbari.erfan.mosito.models.Music;
import rahbari.erfan.mosito.models.MusicLibrary;
import rahbari.erfan.mosito.models.Paginate;
import rahbari.erfan.mosito.resources.Apis.AlbumApi;
import rahbari.erfan.mosito.resources.Apis.ArtistApi;
import rahbari.erfan.mosito.resources.Apis.MusicApi;
import rahbari.erfan.mosito.resources.BaseRoom;
import rahbari.erfan.mosito.resources.Daos.ArtistDao;
import rahbari.erfan.mosito.utils.CallWrapper;
import io.reactivex.schedulers.Schedulers;

public class ArtistRep {
    private final ArtistApi api;
    private final MusicApi musicApi;
    private final AlbumApi albumApi;
    private final ArtistDao artistDao;

    public ArtistRep() {
        api = new ArtistApi();
        musicApi = new MusicApi();
        albumApi = new AlbumApi();
        artistDao = BaseRoom.getDatabase(Application.getInstance()).artistDao();
    }

    private void updateOrCreate(Artist artist) {
        if (artistDao.count(artist.getId()) > 0) {
            artistDao.update(artist);
        } else {
            artistDao.create(artist);
        }
    }

    void updateOrCreate(List<Artist> artists) {
        List<Artist> create = new ArrayList<>();
        List<Artist> update = new ArrayList<>();

        for (Artist x : artists) {
            if (artistDao.count(x.getId()) > 0) {
                update.add(x);
            } else {
                create.add(x);
            }
        }

        Artist[] toCreate = new Artist[create.size()];
        Artist[] toUpdate = new Artist[update.size()];

        create.toArray(toCreate);
        update.toArray(toUpdate);

        artistDao.create(toCreate);
        artistDao.update(toUpdate);
    }

    public LiveData<PagedList<Artist>> library() {
        return new LivePagedListBuilder<>(artistDao.library(), 20).build();
    }

    public LiveData<ArtistMusics> artistMusics(long artistId) {
        return artistDao.artistWithMusics(artistId);
    }

    public LiveData<ArtistAlbums> artistAlbums(long artistId) {
        return artistDao.artistWithAlbums(artistId);
    }

    public LiveData<PagedList<MusicLibrary>> latest(long artistId,int perPage) {
        return new LivePagedListBuilder<>(artistDao.latest(artistId), perPage).build();
    }

    public LiveData<PagedList<MusicLibrary>> tops(long artistId, int perPage) {
        return new LivePagedListBuilder<>(artistDao.tops(artistId), perPage).build();
    }

    public LiveData<PagedList<AlbumLibrary>> albums(long artistId, int perPage) {
        return new LivePagedListBuilder<>(artistDao.albums(artistId), perPage).build();
    }

    public ArtistDao getArtistDao() {
        return artistDao;
    }

    public void artist(String artistId, Response<Artist> artistResponse, Response<ErrorHandler> handlerResponse) {
        api.artist(artistId).subscribeOn(Schedulers.io()).subscribe(new CallWrapper<Artist>() {
            @Override
            protected void onSuccess(Artist artist) {
                artistResponse.response(artist);
                updateOrCreate(artist);
            }

            @Override
            protected void onError(int status, ErrorHandler response) {
                Log.e("ArtistRep", response.getMessage());
                handlerResponse.response(response);
            }
        });
    }

    public LiveData<List<Artist>> whereId(long[] ids) {
        return artistDao.whereInIds(ids);
    }

    public void addMusicsToArtist(long artistId, List<Music> items) {
        List<Music> musics = new ArrayList<>();
        for (Music x : items) {
            if (artistDao.existMusicInArtist(artistId, x.getId()) == 0) {
                musics.add(x);
            }
        }

        ArtistMusicRole[] toCreate = new ArtistMusicRole[musics.size()];

        for (int i = 0; i < musics.size(); i++) {
            ArtistMusicRole role = new ArtistMusicRole();
            role.setArtist_id(artistId);
            role.setMusic_id(musics.get(i).getId());
            toCreate[i] = role;
        }

        artistDao.addToArtist(toCreate);
    }

    public void addMusicsToArtist(long artistId, Music music) {
        if (artistDao.existMusicInArtist(artistId, music.getId()) == 0) {
            ArtistMusicRole role = new ArtistMusicRole();
            role.setArtist_id(artistId);
            role.setMusic_id(music.getId());
            artistDao.addToArtist(role);
        }
    }

    public void addMusicsToArtist(List<Artist> artists, Music music) {
        List<Artist> items = new ArrayList<>();
        for (Artist x : artists) {
            if (artistDao.existMusicInArtist(x.getId(), music.getId()) == 0) {
                items.add(x);
            }
        }

        ArtistMusicRole[] toCreate = new ArtistMusicRole[items.size()];

        for (int i = 0; i < items.size(); i++) {
            ArtistMusicRole role = new ArtistMusicRole();
            role.setArtist_id(items.get(i).getId());
            role.setMusic_id(music.getId());
            toCreate[i] = role;
        }

        artistDao.addToArtist(toCreate);
    }

    public void addAlbumsToArtist(List<Artist> artists, Album album) {
        List<Artist> items = new ArrayList<>();
        for (Artist x : artists) {
            if (artistDao.existAlbumInArtist(x.getId(), album.getId()) == 0) {
                items.add(x);
            }
        }

        ArtistAlbumRole[] toCreate = new ArtistAlbumRole[items.size()];

        for (int i = 0; i < items.size(); i++) {
            ArtistAlbumRole role = new ArtistAlbumRole();
            role.setArtist_id(items.get(i).getId());
            role.setAlbum_id(album.getId());
            toCreate[i] = role;
        }

        artistDao.addToArtist(toCreate);
    }

    public void addAlbumsToArtist(long artistId, Album album) {
        if (artistDao.existAlbumInArtist(artistId, album.getId()) == 0) {
            ArtistAlbumRole role = new ArtistAlbumRole();
            role.setArtist_id(artistId);
            role.setAlbum_id(album.getId());
            artistDao.addToArtist(role);
        }
    }

    public void addAlbumsToArtist(long artistId, List<Album> items) {
        List<Album> albums = new ArrayList<>();
        for (Album x : items) {
            if (artistDao.existAlbumInArtist(artistId, x.getId()) == 0) {
                albums.add(x);
            }
        }

        ArtistAlbumRole[] toCreate = new ArtistAlbumRole[albums.size()];

        for (int i = 0; i < albums.size(); i++) {
            ArtistAlbumRole role = new ArtistAlbumRole();
            role.setArtist_id(artistId);
            role.setAlbum_id(albums.get(i).getId());
            toCreate[i] = role;
        }

        artistDao.addToArtist(toCreate);
    }

    public void tops(long artistId, int page, int pagination) {
        musicApi.musicsArtist(pagination, page, "musics.downloads", artistId).subscribeOn(Schedulers.io()).subscribe(new CallWrapper<Paginate<Music>>() {
            @Override
            protected void onSuccess(Paginate<Music> musicPaginate) {
                new MusicRep().updateOrCreate(musicPaginate.data);
                addMusicsToArtist(artistId, musicPaginate.data);
            }

            @Override
            protected void onError(int status, ErrorHandler response) {
                Log.e("ArtistRep", response.getMessage());
            }
        });
    }

    public void latest(long artistId, int page, int pagination) {
        musicApi.musicsArtist(pagination, page, "musics.created_at", artistId)
                .subscribeOn(Schedulers.io()).subscribe(new CallWrapper<Paginate<Music>>() {
            @Override
            protected void onSuccess(Paginate<Music> musicPaginate) {
                new MusicRep().updateOrCreate(musicPaginate.data);
                addMusicsToArtist(artistId, musicPaginate.data);
            }

            @Override
            protected void onError(int status, ErrorHandler response) {
                Log.e("ArtistRep", response.getMessage());
            }
        });
    }

    public void albums(long artistId, int page, int pagination) {
        albumApi.artistAlbums(pagination, page, artistId).subscribeOn(Schedulers.io()).subscribe(new CallWrapper<Paginate<Album>>() {
            @Override
            protected void onSuccess(Paginate<Album> albumPaginate) {
                new AlbumRep().updateOrCreate(albumPaginate.data);
                addAlbumsToArtist(artistId, albumPaginate.data);
            }

            @Override
            protected void onError(int status, ErrorHandler response) {
                Log.e("ArtistRep", response.getMessage());
            }
        });
    }
}
