package rahbari.erfan.mosito.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import rahbari.erfan.mosito.resources.BaseModel;

@Entity(tableName = "playlists")
public class Playlist extends BaseModel implements Serializable {
    @PrimaryKey
    @ColumnInfo
    @SerializedName("id")
    private long id;
    @ColumnInfo
    @SerializedName("type")
    private String type;
    @ColumnInfo
    @SerializedName("title")
    private String title;
    @ColumnInfo
    @SerializedName("picture")
    private String picture;
    @ColumnInfo
    @SerializedName("description")
    private String description;
    @ColumnInfo
    @SerializedName("creator")
    private User creator;
    @ColumnInfo
    @SerializedName("created_at")
    private String created_at;
    @ColumnInfo
    @SerializedName("updated_at")
    private String updated_at;
    @ColumnInfo
    @SerializedName("deleted_at")
    private String deleted_at;
    @ColumnInfo
    @SerializedName("mine")
    private boolean mine;
    @ColumnInfo
    @SerializedName("musics_count")
    private int musics_count;
    @ColumnInfo
    @SerializedName("users_count")
    private int users_count;

    @Ignore
    @SerializedName("musics")
    private List<Music> musics;

    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public int getUsers_count() {
        return users_count;
    }

    public void setUsers_count(int users_count) {
        this.users_count = users_count;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public int getMusics_count() {
        return musics_count;
    }

    public void setMusics_count(int musics_count) {
        this.musics_count = musics_count;
    }

    public List<Music> getMusics() {
        return musics;
    }

    public void setMusics(List<Music> musics) {
        this.musics = musics;
    }

    public String getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
    }

    public boolean isMine() {
        return mine;
    }

    public void setMine(boolean mine) {
        this.mine = mine;
    }
}
