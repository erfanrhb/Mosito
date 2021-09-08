package rahbari.erfan.mosito.resources.Repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import java.util.List;

import rahbari.erfan.mosito.interfaces.Response;
import rahbari.erfan.mosito.models.Album;
import rahbari.erfan.mosito.models.AlbumLibrary;
import rahbari.erfan.mosito.models.Artist;
import rahbari.erfan.mosito.models.ErrorHandler;
import rahbari.erfan.mosito.models.Music;
import rahbari.erfan.mosito.models.MusicLibrary;
import rahbari.erfan.mosito.models.Playlist;
import rahbari.erfan.mosito.models.PlaylistLibrary;
import rahbari.erfan.mosito.models.SearchResponse;
import rahbari.erfan.mosito.resources.Apis.SearchApi;
import rahbari.erfan.mosito.utils.CallWrapper;
import io.reactivex.schedulers.Schedulers;

public class SearchRep {
    private final MusicRep musicRep;
    private final AlbumRep albumRep;
    private final SearchApi searchApi;
    private final ArtistRep artistRep;
    private final PlaylistRep playlistRep;

    public SearchRep() {
        searchApi = new SearchApi();
        musicRep = new MusicRep();
        albumRep = new AlbumRep();
        artistRep = new ArtistRep();
        playlistRep = new PlaylistRep();
    }

    public LiveData<PagedList<MusicLibrary>> searchMusic(String search) {
        return new LivePagedListBuilder<>(musicRep.getMusicDao().searchPaginate(search), 20).build();
    }

    public LiveData<Integer> searchMusicCount(String search) {
        return musicRep.getMusicDao().searchCount(search);
    }

    public LiveData<PagedList<AlbumLibrary>> searchAlbum(String search) {
        return new LivePagedListBuilder<>(albumRep.getAlbumDao().searchPaginate(search), 20).build();
    }

    public LiveData<Integer> searchAlbumCount(String search) {
        return albumRep.getAlbumDao().searchCount(search);
    }

    public LiveData<PagedList<Artist>> searchArtist(String search) {
        return new LivePagedListBuilder<>(artistRep.getArtistDao().searchPaginate(search), 20).build();
    }

    public LiveData<Integer> searchArtistCount(String search) {
        return artistRep.getArtistDao().searchCount(search);
    }

    public LiveData<PagedList<PlaylistLibrary>> searchPlaylists(String search) {
        return new LivePagedListBuilder<>(playlistRep.getPlaylistDao().searchPaginate(search), 20).build();
    }

    public LiveData<Integer> searchPlaylistsCount(String search) {
        return playlistRep.getPlaylistDao().searchCount(search);
    }

    public LiveData<PagedList<MusicLibrary>> arrayMusicIds(List<Music> musics) {
        long[] array = new long[musics.size()];
        for (int i = 0; i < musics.size(); i++)
            array[i] = musics.get(i).getId();
        return musicRep.whereId(array);
    }

    public LiveData<List<Artist>> arrayArtistIds(List<Artist> artists) {
        long[] array = new long[artists.size()];
        for (int i = 0; i < artists.size(); i++)
            array[i] = artists.get(i).getId();
        return artistRep.whereId(array);
    }

    public LiveData<PagedList<AlbumLibrary>> arrayAlbumIds(List<Album> albums) {
        long[] array = new long[albums.size()];
        for (int i = 0; i < albums.size(); i++)
            array[i] = albums.get(i).getId();
        return albumRep.whereId(array);
    }

    public LiveData<PagedList<PlaylistLibrary>> arrayPlaylistIds(List<Playlist> playlists) {
        long[] array = new long[playlists.size()];
        for (int i = 0; i < playlists.size(); i++)
            array[i] = playlists.get(i).getId();
        return playlistRep.whereId(array);
    }

    public void searchOnline(String text, Response<SearchResponse> response, Response<Boolean> done) {
        searchApi.search(text).subscribeOn(Schedulers.io()).subscribe(new CallWrapper<SearchResponse>() {
            @Override
            protected void onSuccess(SearchResponse searchResponse) {
                musicRep.updateOrCreate(searchResponse.getMusics());
                albumRep.updateOrCreate(searchResponse.getAlbums());
                playlistRep.updateOrCreate(searchResponse.getPlaylists());
                artistRep.updateOrCreate(searchResponse.getArtists());

                response.response(searchResponse);
                done.response(true);
            }

            @Override
            protected void onError(int status, ErrorHandler response) {
                Log.e("Search", response.getMessage());
                done.response(false);
            }
        });
    }
}

