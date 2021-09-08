package rahbari.erfan.mosito.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "user_albums")
public class UserAlbum {
    @ColumnInfo
    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo
    @SerializedName("album_id")
    private long album_id;
    @ColumnInfo
    @SerializedName("created_at")
    private String created_at;
    @ColumnInfo
    @SerializedName("deleted_at")
    private String deleted_at;

    public UserAlbum(long album_id, String created_at) {
        this.album_id = album_id;
        this.created_at = created_at;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(long album_id) {
        this.album_id = album_id;
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
