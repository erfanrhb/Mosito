package rahbari.erfan.mosito.models;

import androidx.room.Embedded;
import androidx.room.Relation;

import rahbari.erfan.mosito.resources.BaseModel;

public class AlbumLibrary extends BaseModel {
    @Embedded
    public Album album;
    @Relation(parentColumn = "id", entityColumn = "album_id")
    public UserAlbum library;

    @Override
    public long getId() {
        return album.getId();
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public UserAlbum getLibrary() {
        return library;
    }

    public void setLibrary(UserAlbum library) {
        this.library = library;
    }
}
