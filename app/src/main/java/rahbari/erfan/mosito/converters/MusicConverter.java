package rahbari.erfan.mosito.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;

import rahbari.erfan.mosito.models.Music;

public class MusicConverter {
    @TypeConverter
    public static Music storedStringToMusic(String value) {
        return new Gson().fromJson(value, Music.class);
    }

    @TypeConverter
    public static String MusicToStoredString(Music cl) {
        return new Gson().toJson(cl);
    }
}
