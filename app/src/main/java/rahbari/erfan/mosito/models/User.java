package rahbari.erfan.mosito.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import rahbari.erfan.mosito.resources.BaseModel;

@Entity(tableName = "users")
public class User extends BaseModel implements Serializable {
    @PrimaryKey
    @ColumnInfo
    @SerializedName("id")
    private long id;
    @ColumnInfo
    @SerializedName("name")
    private String name;
    @ColumnInfo
    @SerializedName("email")
    private String email;
    @ColumnInfo
    @SerializedName("country")
    private String country;
    @ColumnInfo
    @SerializedName("phone")
    private String phone;
    @ColumnInfo
    @SerializedName("picture")
    private String picture;
    @ColumnInfo
    @SerializedName("email_verified_at")
    private String email_verified_at;
    @ColumnInfo
    @SerializedName("phone_verified_at")
    private String phone_verified_at;
    @ColumnInfo
    @SerializedName("offers_updated_at")
    private String offers_updated_at;
    @ColumnInfo
    @SerializedName("created_at")
    private String created_at;
    @ColumnInfo
    @SerializedName("updated_at")
    private String updated_at;
    @ColumnInfo
    @SerializedName("token")
    private String token;

    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getEmail_verified_at() {
        return email_verified_at;
    }

    public void setEmail_verified_at(String email_verified_at) {
        this.email_verified_at = email_verified_at;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public String getPhone() {
        return phone;
    }

    public String getPhone_verified_at() {
        return phone_verified_at;
    }

    public void setPhone_verified_at(String phone_verified_at) {
        this.phone_verified_at = phone_verified_at;
    }

    public String getOffers_updated_at() {
        return offers_updated_at;
    }

    public void setOffers_updated_at(String offers_updated_at) {
        this.offers_updated_at = offers_updated_at;
    }
}
