package rahbari.erfan.mosito.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SearchResponse implements Serializable {
    @SerializedName("musics")
    private List<Music> musics;
    @SerializedName("artists")
    private List<Artist> artists;
    @SerializedName("albums")
    private List<Album> albums;
    @SerializedName("playlists")
    private List<Playlist> playlists;
    @SerializedName("musics_count")
    private int musics_count;
    @SerializedName("albums_count")
    private int albums_count;
    @SerializedName("playlists_count")
    private int playlists_count;

    public List<Music> getMusics() {
        return musics;
    }

    public void setMusics(List<Music> musics) {
        this.musics = musics;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }

    public List<Playlist> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(List<Playlist> playlists) {
        this.playlists = playlists;
    }

    public int getMusics_count() {
        return musics_count;
    }

    public void setMusics_count(int musics_count) {
        this.musics_count = musics_count;
    }

    public int getAlbums_count() {
        return albums_count;
    }

    public void setAlbums_count(int albums_count) {
        this.albums_count = albums_count;
    }

    public int getPlaylists_count() {
        return playlists_count;
    }

    public void setPlaylists_count(int playlists_count) {
        this.playlists_count = playlists_count;
    }
}
