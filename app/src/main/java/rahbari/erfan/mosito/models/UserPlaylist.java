package rahbari.erfan.mosito.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "user_playlists")
public class UserPlaylist {
    @ColumnInfo
    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo
    @SerializedName("playlist_id")
    private long playlist_id;
    @ColumnInfo
    @SerializedName("created_at")
    private String created_at;
    @ColumnInfo
    @SerializedName("deleted_at")
    private String deleted_at;

    public UserPlaylist(long playlist_id, String created_at) {
        this.playlist_id = playlist_id;
        this.created_at = created_at;
    }

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

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
    }
}
