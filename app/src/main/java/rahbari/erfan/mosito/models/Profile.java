package rahbari.erfan.mosito.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Profile implements Serializable {
    @SerializedName("id")
    private String id;
    @SerializedName("url")
    private String url;
    @SerializedName("type")
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
