package rahbari.erfan.mosito.resources.models;

import com.google.gson.annotations.SerializedName;

public class SyncList {
    @SerializedName("art_id")
    private long art_id;
    @SerializedName("art_type")
    private String art_type;
    @SerializedName("created_at")
    private String created_at;
    @SerializedName("deleted_at")
    private String deleted_at;

    public SyncList() {

    }

    public SyncList(long art_id, String art_type, String created_at, String deleted_at) {
        this.art_id = art_id;
        this.art_type = art_type;
        this.created_at = created_at;
        this.deleted_at = deleted_at;
    }

    public long getArt_id() {
        return art_id;
    }

    public void setArt_id(long art_id) {
        this.art_id = art_id;
    }

    public String getArt_type() {
        return art_type;
    }

    public void setArt_type(String art_type) {
        this.art_type = art_type;
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
