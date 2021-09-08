package rahbari.erfan.mosito.models;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class ArtistAlbums {
    @Embedded
    public Artist artist;
    @Relation(
            parentColumn = "id",
            entityColumn = "artist_id",
            entity = ArtistAlbumRole.class,
            projection = "album_id"
    )
    public List<AlbumLibrary> albums;
}
