package rahbari.erfan.mosito.resources.Daos;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import rahbari.erfan.mosito.models.AlbumLibrary;
import rahbari.erfan.mosito.models.Artist;
import rahbari.erfan.mosito.models.ArtistAlbumRole;
import rahbari.erfan.mosito.models.ArtistAlbums;
import rahbari.erfan.mosito.models.ArtistMusicRole;
import rahbari.erfan.mosito.models.ArtistMusics;
import rahbari.erfan.mosito.models.MusicLibrary;

@Dao
public interface ArtistDao {
    @Insert
    void create(Artist... artist);

    @Update
    void update(Artist... artist);

    @Query("SELECT COUNT(*) FROM artists WHERE id = :Id")
    int count(long Id);

    @Insert
    void addToArtist(ArtistMusicRole... musicRole);

    @Query("SELECT COUNT(*) FROM artist_album_role WHERE album_id = :albumId and artist_id = :artistId")
    int existAlbumInArtist(long artistId, long albumId);

    @Insert
    void addToArtist(ArtistAlbumRole... albumRole);

    @Query("SELECT COUNT(*) FROM artist_musics_role WHERE music_id = :musicId and artist_id = :artistId")
    int existMusicInArtist(long artistId, long musicId);

    @Query("SELECT * FROM artists " +
            "WHERE artists.id IN (SELECT artist_id FROM artist_musics_role WHERE music_id IN (SELECT music_id FROM user_musics WHERE user_musics.deleted_at IS NULL)) " +
            "OR artists.id IN (SELECT artist_id FROM artist_album_role WHERE album_id IN (SELECT album_id FROM user_albums WHERE user_albums.deleted_at IS NULL))")
    DataSource.Factory<Integer, Artist> library();

    @Query("SELECT COUNT(*) FROM artists " +
            "WHERE artists.id IN (SELECT artist_id FROM artist_musics_role WHERE music_id IN (SELECT music_id FROM user_musics WHERE user_musics.deleted_at IS NULL)) " +
            "OR artists.id IN (SELECT artist_id FROM artist_album_role WHERE album_id IN (SELECT album_id FROM user_albums WHERE user_albums.deleted_at IS NULL))")
    LiveData<Integer> libraryCount();

    @Transaction
    @Query("SELECT * FROM artists WHERE deleted_at IS NULL AND id = :artistId LIMIT 1")
    LiveData<ArtistMusics> artistWithMusics(long artistId);

    @Transaction
    @Query("SELECT * FROM artists WHERE deleted_at IS NULL AND id = :artistId LIMIT 1")
    LiveData<ArtistAlbums> artistWithAlbums(long artistId);

    @Transaction
    @Query("SELECT musics.* FROM musics " +
            "JOIN artist_musics_role ON artist_musics_role.music_id = musics.id " +
            "WHERE musics.deleted_at IS NULL AND artist_musics_role.artist_id = :artistId " +
            "GROUP BY musics.id ORDER BY musics.created_at")
    DataSource.Factory<Integer, MusicLibrary> latest(long artistId);

    @Transaction
    @Query("SELECT musics.* FROM musics " +
            "JOIN artist_musics_role ON artist_musics_role.music_id = musics.id " +
            "WHERE musics.deleted_at IS NULL AND artist_musics_role.artist_id = :artistId " +
            "GROUP BY musics.id ORDER BY musics.downloads DESC")
    DataSource.Factory<Integer, MusicLibrary> tops(long artistId);

    @Transaction
    @Query("SELECT albums.* FROM albums " +
            "JOIN artist_album_role ON artist_album_role.album_id = albums.id " +
            "WHERE albums.deleted_at IS NULL AND artist_album_role.artist_id = :artistId " +
            "GROUP BY albums.id ORDER BY albums.created_at")
    DataSource.Factory<Integer, AlbumLibrary> albums(long artistId);

    @Transaction
    @Query("SELECT musics.* FROM musics " +
            "JOIN artist_musics_role ON artist_musics_role.music_id = musics.id " +
            "WHERE musics.deleted_at IS NULL AND artist_musics_role.artist_id = :artistId " +
            "GROUP BY musics.id ORDER BY musics.created_at")
    DataSource.Factory<Integer, MusicLibrary> artistMusic(long artistId);

    @Transaction
    @Query("SELECT albums.* FROM albums " +
            "JOIN artist_album_role ON artist_album_role.album_id = albums.id " +
            "WHERE albums.deleted_at IS NULL AND artist_album_role.artist_id = :artistId " +
            "GROUP BY albums.id ORDER BY albums.created_at")
    DataSource.Factory<Integer, AlbumLibrary> artistAlbum(long artistId);

    @Query("SELECT * FROM artists WHERE name LIKE :search OR nameFa LIKE :search LIMIT 24")
    DataSource.Factory<Integer, Artist> searchPaginate(String search);

    @Query("SELECT * FROM artists WHERE id in (:arrayId)")
    LiveData<List<Artist>> whereInIds(long[] arrayId);

    @Query("SELECT COUNT(*) FROM artists WHERE name LIKE :search OR nameFa LIKE :search")
    LiveData<Integer> searchCount(String search);
}
