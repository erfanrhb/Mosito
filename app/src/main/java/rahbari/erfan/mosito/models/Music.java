package rahbari.erfan.mosito.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import rahbari.erfan.mosito.resources.BaseModel;

@Entity(tableName = "musics")
public class Music extends BaseModel implements Serializable {
    @PrimaryKey
    @ColumnInfo
    @SerializedName("id")
    private long id;
    @ColumnInfo
    @SerializedName("title")
    private String title;
    @ColumnInfo
    @SerializedName("performer")
    private String performer;
    @ColumnInfo
    @SerializedName("titleFa")
    private String titleFa;
    @ColumnInfo
    @SerializedName("performerFa")
    private String performerFa;
    @ColumnInfo
    @SerializedName("lyric")
    private String lyric;
    @ColumnInfo
    @SerializedName("picture")
    private String picture;
    @ColumnInfo
    @SerializedName("size")
    private String size;
    @ColumnInfo
    @SerializedName("bitrate")
    private String bitrate;
    @ColumnInfo
    @SerializedName("duration")
    private int duration;
    @ColumnInfo
    @SerializedName("created_at")
    private String created_at;
    @ColumnInfo
    @SerializedName("updated_at")
    private String updated_at;
    @ColumnInfo
    @SerializedName("deleted_at")
    private String deleted_at;

    /**
     * exclude from database
     **/

    @SerializedName("downloads")
    private long downloads;
    @SerializedName("impressions")
    private long impressions;

    /**
     * maybe unavailable
     **/

    @SerializedName("album")
    private Album album;
    @SerializedName("artists")
    private List<Artist> artists;


    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPerformer() {
        return performer;
    }

    public void setPerformer(String performer) {
        this.performer = performer;
    }

    public String getTitleFa() {
        return titleFa;
    }

    public void setTitleFa(String titleFa) {
        this.titleFa = titleFa;
    }

    public String getPerformerFa() {
        return performerFa;
    }

    public void setPerformerFa(String performerFa) {
        this.performerFa = performerFa;
    }

    public String getLyric() {
        return lyric;
    }

    public void setLyric(String lyric) {
        this.lyric = lyric;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getBitrate() {
        return bitrate;
    }

    public void setBitrate(String bitrate) {
        this.bitrate = bitrate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
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

    public long getDownloads() {
        return downloads;
    }

    public void setDownloads(long downloads) {
        this.downloads = downloads;
    }

    public long getImpressions() {
        return impressions;
    }

    public void setImpressions(long impressions) {
        this.impressions = impressions;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public String localFileName() {
        return id + ".mp3";
    }
}
