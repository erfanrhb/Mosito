package rahbari.erfan.mosito.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import rahbari.erfan.mosito.models.Artist;

public class ArtistsConverter {
    @TypeConverter
    public static List<Artist> storedStringToArtists(String value) {
        return new Gson().fromJson(value, new TypeToken<List<Artist>>() {
        }.getType());
    }

    @TypeConverter
    public static String ArtistsToStoredString(List<Artist> cl) {
        return new Gson().toJson(cl);
    }
}
