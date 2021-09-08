package rahbari.erfan.mosito.models;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class ArtistMusics {
    @Embedded
    public Artist artist;
    @Relation(
            parentColumn = "id",
            entityColumn = "artist_id",
            entity = ArtistMusicRole.class,
            projection = "music_id"
    )
    public List<MusicLibrary> musics;
}
