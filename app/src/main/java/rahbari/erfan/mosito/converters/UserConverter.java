package rahbari.erfan.mosito.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;

import rahbari.erfan.mosito.models.User;

public class UserConverter {
    @TypeConverter
    public static User storedStringToUser(String value) {
        return new Gson().fromJson(value, User.class);
    }

    @TypeConverter
    public static String UserToStoredString(User cl) {
        return new Gson().toJson(cl);
    }
}
