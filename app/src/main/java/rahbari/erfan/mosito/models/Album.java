package rahbari.erfan.mosito.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import rahbari.erfan.mosito.resources.BaseModel;

@Entity(tableName = "albums")
public class Album extends BaseModel implements Serializable {
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
    @SerializedName("performer")
    private String performer;
    @ColumnInfo
    @SerializedName("cover")
    private String cover;
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
    @SerializedName("performerFa")
    private String performerFa;

    /** exclude from database **/

    @SerializedName("impressions")
    private long impressions;

    /** maybe unavailable **/

    @SerializedName("musics")
    private List<Music> musics;
    @SerializedName("artists")
    private List<Artist> artists;

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

    public String getPerformer() {
        return performer;
    }

    public void setPerformer(String performer) {
        this.performer = performer;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
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

    public String getPerformerFa() {
        return performerFa;
    }

    public void setPerformerFa(String performerFa) {
        this.performerFa = performerFa;
    }

    public long getImpressions() {
        return impressions;
    }

    public void setImpressions(long impressions) {
        this.impressions = impressions;
    }

    public List<Music> getMusics() {
        return musics;
    }

    public void setMusics(List<Music> musics) {
        this.musics = musics;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }
}
