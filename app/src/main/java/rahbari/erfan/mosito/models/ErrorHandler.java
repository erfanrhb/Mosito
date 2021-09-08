package rahbari.erfan.mosito.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ErrorHandler implements Serializable {
    @SerializedName("message")
    private String message;
    @Expose
    @SerializedName("errors")
    private Map<String, List<String>> errors;

    public ErrorHandler(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, List<String>> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, List<String>> errors) {
        this.errors = errors;
    }

}
