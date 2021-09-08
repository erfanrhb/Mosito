package rahbari.erfan.mosito.resources.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SyncRequest {
    @SerializedName("libraries")
    private List<SyncList> libraries;
    @SerializedName("created_at")
    private String created_at;

    public SyncRequest(List<SyncList> libraries, String updated_at) {
        this.libraries = libraries;
        this.created_at = updated_at;
    }

    public List<SyncList> getLibraries() {
        return libraries;
    }

    public void setLibraries(List<SyncList> libraries) {
        this.libraries = libraries;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getCreated_at() {
        return created_at;
    }
}
