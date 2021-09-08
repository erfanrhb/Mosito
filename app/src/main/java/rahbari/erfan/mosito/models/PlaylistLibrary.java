package rahbari.erfan.mosito.models;

import androidx.room.Embedded;
import androidx.room.Relation;

import rahbari.erfan.mosito.resources.BaseModel;

public class PlaylistLibrary extends BaseModel {
    @Embedded
    public Playlist playlist;
    @Relation(parentColumn = "id", entityColumn = "playlist_id")
    public UserPlaylist library;

    @Override
    public long getId() {
        return playlist.getId();
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    public UserPlaylist getLibrary() {
        return library;
    }

    public void setLibrary(UserPlaylist library) {
        this.library = library;
    }
}
