package rahbari.erfan.mosito.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Download implements Serializable {
    @SerializedName("message")
    private String message;
    @SerializedName("url")
    private String url;
    @SerializedName("source")
    private String source;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
