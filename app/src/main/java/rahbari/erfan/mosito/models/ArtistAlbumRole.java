package rahbari.erfan.mosito.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "artist_album_role")
public class ArtistAlbumRole {
    @ColumnInfo
    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo
    private long artist_id;
    @ColumnInfo
    private long album_id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getArtist_id() {
        return artist_id;
    }

    public void setArtist_id(long artist_id) {
        this.artist_id = artist_id;
    }

    public long getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(long album_id) {
        this.album_id = album_id;
    }
}
