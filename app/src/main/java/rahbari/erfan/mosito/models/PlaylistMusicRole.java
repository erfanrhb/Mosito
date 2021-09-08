package rahbari.erfan.mosito.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "playlists_musics_role")
public class PlaylistMusicRole {
    @ColumnInfo
    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo
    private long playlist_id;
    @ColumnInfo
    private long music_id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPlaylist_id() {
        return playlist_id;
    }

    public void setPlaylist_id(long playlist_id) {
        this.playlist_id = playlist_id;
    }

    public long getMusic_id() {
        return music_id;
    }

    public void setMusic_id(long music_id) {
        this.music_id = music_id;
    }
}
