package rahbari.erfan.mosito.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;

import rahbari.erfan.mosito.models.Album;

public class AlbumConverter {
    @TypeConverter
    public static Album storedStringToAlbum(String value) {
        return new Gson().fromJson(value, Album.class);
    }

    @TypeConverter
    public static String AlbumToStoredString(Album cl) {
        return new Gson().toJson(cl);
    }
}
