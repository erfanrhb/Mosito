package rahbari.erfan.mosito.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "artist_musics_role")
public class ArtistMusicRole {
    @ColumnInfo
    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo
    private long artist_id;
    @ColumnInfo
    private long music_id;

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

    public long getMusic_id() {
        return music_id;
    }

    public void setMusic_id(long music_id) {
        this.music_id = music_id;
    }
}
