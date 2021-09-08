package rahbari.erfan.mosito.resources.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SyncResponse {
    @SerializedName("updated_at")
    private String updated_at;
    @SerializedName("libraries")
    private List<SyncList> libraries;

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public List<SyncList> getLibraries() {
        return libraries;
    }

    public void setLibraries(List<SyncList> libraries) {
        this.libraries = libraries;
    }
}
