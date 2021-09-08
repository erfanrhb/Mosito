package rahbari.erfan.mosito.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import rahbari.erfan.mosito.resources.BaseModel;

@Entity(tableName = "artists")
public class Artist extends BaseModel implements Serializable {
    @PrimaryKey
    @ColumnInfo
    @SerializedName("id")
    private long id;
    @ColumnInfo
    @SerializedName("name")
    private String name;
    @ColumnInfo
    @SerializedName("nameFa")
    private String nameFa;
    @ColumnInfo
    @SerializedName("picture")
    private String picture;
    @ColumnInfo
    @SerializedName("profiles")
    private List<Profile> profiles;
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
    @SerializedName("background")
    private String background;

    /** exclude from database **/

    @SerializedName("musics_count")
    private long musics_count;
    @SerializedName("albums_count")
    private long albums_count;
    @SerializedName("impressions")
    private long impressions;

    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameFa() {
        return nameFa;
    }

    public void setNameFa(String nameFa) {
        this.nameFa = nameFa;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public List<Profile> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<Profile> profiles) {
        this.profiles = profiles;
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

    public String getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public long getMusics_count() {
        return musics_count;
    }

    public void setMusics_count(long musics_count) {
        this.musics_count = musics_count;
    }

    public long getAlbums_count() {
        return albums_count;
    }

    public void setAlbums_count(long albums_count) {
        this.albums_count = albums_count;
    }

    public long getImpressions() {
        return impressions;
    }

    public void setImpressions(long impressions) {
        this.impressions = impressions;
    }
}
