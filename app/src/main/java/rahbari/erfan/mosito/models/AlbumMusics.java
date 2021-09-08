package rahbari.erfan.mosito.models;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class AlbumMusics {
    @Embedded
    public Album album;
    @Relation(
            parentColumn = "id",
            entityColumn = "album_id",
            entity = AlbumMusicRole.class,
            projection = "music_id"
    )
    public List<MusicLibrary> musics;
}
