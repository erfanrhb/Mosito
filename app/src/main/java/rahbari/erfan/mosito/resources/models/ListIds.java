package rahbari.erfan.mosito.resources.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListIds {
    @SerializedName("ids")
    private List<Long> ids;



    public ListIds(List<Long> ids) {
        this.ids = ids;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }
}
