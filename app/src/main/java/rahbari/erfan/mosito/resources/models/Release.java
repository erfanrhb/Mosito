package rahbari.erfan.mosito.resources.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import rahbari.erfan.mosito.models.Album;
import rahbari.erfan.mosito.models.Artist;
import rahbari.erfan.mosito.models.Music;

public class Release implements Serializable {
    @SerializedName("popular")
    private List<Music> popular;
    @SerializedName("artists")
    private List<Artist> artists;
    @SerializedName("musics")
    private List<Music> musics;
    @SerializedName("albums")
    private List<Album> albums;

    public List<Music> getPopular() {
        return popular;
    }

    public void setPopular(List<Music> popular) {
        this.popular = popular;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public List<Music> getMusics() {
        return musics;
    }

    public void setMusics(List<Music> musics) {
        this.musics = musics;
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }
}
