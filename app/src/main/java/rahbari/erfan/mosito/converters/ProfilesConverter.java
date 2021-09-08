package rahbari.erfan.mosito.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import rahbari.erfan.mosito.models.Profile;

public class ProfilesConverter {
    @TypeConverter
    public static List<Profile> storedStringToProfile(String value) {
        return new Gson().fromJson(value, new TypeToken<List<Profile>>() {
        }.getType());
    }

    @TypeConverter
    public static String ProfileToStoredString(List<Profile> cl) {
        return new Gson().toJson(cl);
    }
}
