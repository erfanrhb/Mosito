package rahbari.erfan.mosito.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Paginate<T> implements Serializable {
    @SerializedName("current_page")
    private int currentPage;
    @SerializedName("data")
    public List<T> data;
    @SerializedName("from")
    private String from;
    @SerializedName("last_page")
    private int lastPage;
    @SerializedName("next_page_url")
    private String nextPageUrl;
    @SerializedName("per_page")
    private int perPage;
    @SerializedName("prev_page_url")
    private String prevPageUrl;
    @SerializedName("to")
    private String to;
    @SerializedName("total")
    private int total;
    private final static long serialVersionUID = 5360585717066938647L;

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public String getNextPageUrl() {
        return nextPageUrl;
    }

    public void setNextPageUrl(String nextPageUrl) {
        this.nextPageUrl = nextPageUrl;
    }

    public int getPerPage() {
        return perPage;
    }

    public void setPerPage(int perPage) {
        this.perPage = perPage;
    }

    public String getPrevPageUrl() {
        return prevPageUrl;
    }

    public void setPrevPageUrl(String prevPageUrl) {
        this.prevPageUrl = prevPageUrl;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

}