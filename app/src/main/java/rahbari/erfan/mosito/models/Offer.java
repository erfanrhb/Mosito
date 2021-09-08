package rahbari.erfan.mosito.models;

import com.google.gson.annotations.SerializedName;

import rahbari.erfan.mosito.resources.BaseModel;

public class Offer extends BaseModel {
    @SerializedName("id")
    private long id;
    @SerializedName("title")
    private String title;
    @SerializedName("titleFa")
    private String titleFa;
    @SerializedName("subtitle")
    private String subtitle;
    @SerializedName("subtitleFa")
    private String subtitleFa;
    @SerializedName("picture")
    private String picture;
    @SerializedName("uri")
    private String uri;
    @SerializedName("tag")
    private String tag;
    @SerializedName("created_at")
    private String created_at;
    @SerializedName("updated_at")
    private String updated_at;

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

    public String getTitleFa() {
        return titleFa;
    }

    public void setTitleFa(String titleFa) {
        this.titleFa = titleFa;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getSubtitleFa() {
        return subtitleFa;
    }

    public void setSubtitleFa(String subtitleFa) {
        this.subtitleFa = subtitleFa;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
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
}
