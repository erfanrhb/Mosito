package rahbari.erfan.mosito.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import rahbari.erfan.mosito.models.Music;

public class MusicsConverter {
    @TypeConverter
    public static List<Music> storedStringToMusics(String value) {
        return new Gson().fromJson(value, new TypeToken<List<Music>>() {
        }.getType());
    }

    @TypeConverter
    public static String MusicsToStoredString(List<Music> cl) {
        return new Gson().toJson(cl);
    }
}
