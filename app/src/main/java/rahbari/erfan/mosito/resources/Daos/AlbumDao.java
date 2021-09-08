package rahbari.erfan.mosito.resources.Daos;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import rahbari.erfan.mosito.models.Album;
import rahbari.erfan.mosito.models.AlbumLibrary;
import rahbari.erfan.mosito.models.AlbumMusicRole;
import rahbari.erfan.mosito.models.Artist;
import rahbari.erfan.mosito.models.MusicLibrary;
import rahbari.erfan.mosito.models.UserAlbum;

@Dao
public interface AlbumDao {
    @Insert
    void create(Album... albums);

    @Update
    void update(Album... albums);

    @Query("SELECT COUNT(*) FROM albums WHERE id = :Id")
    int count(long Id);

    @Transaction
    @Query("SELECT * FROM albums WHERE deleted_at IS NULL ORDER BY created_at DESC")
    DataSource.Factory<Integer, AlbumLibrary> latest();

    @Transaction
    @Query("SELECT * FROM albums WHERE deleted_at IS NULL ORDER BY created_at DESC LIMIT :limit")
    DataSource.Factory<Integer, AlbumLibrary> latest(int limit);


    @Transaction
    @Query("SELECT * FROM albums WHERE deleted_at IS NULL AND id = :albumId LIMIT 1")
    LiveData<AlbumLibrary> album(long albumId);

    @Transaction
    @Query("SELECT albums.* FROM albums JOIN user_albums ON user_albums.album_id = albums.id WHERE albums.deleted_at IS NULL AND user_albums.deleted_at IS NULL ORDER BY user_albums.created_at DESC")
    DataSource.Factory<Integer, AlbumLibrary> libraryPaginate();

    @Query("SELECT COUNT(*) FROM user_albums WHERE user_albums.deleted_at IS NULL")
    LiveData<Integer> libraryCount();

    @Transaction
    @Query("SELECT musics.* FROM musics " +
            "JOIN albums_musics_role ON albums_musics_role.music_id = musics.id " +
            "WHERE musics.deleted_at IS NULL AND albums_musics_role.album_id = :albumId " +
            "GROUP BY musics.id ORDER BY musics.created_at LIMIT 20")
    DataSource.Factory<Integer, MusicLibrary> musics(long albumId);

    @Query("SELECT SUM(musics.duration) FROM musics WHERE musics.deleted_at IS NULL AND musics.id in (SELECT albums_musics_role.music_id FROM albums_musics_role WHERE albums_musics_role.album_id = :albumId)")
    LiveData<Long> durationAlbum(long albumId);

    @Query("SELECT COUNT(albums_musics_role.music_id) FROM albums_musics_role WHERE albums_musics_role.album_id = :albumId")
    LiveData<Integer> musicsAlbum(long albumId);

    @Insert
    void addToAlbum(AlbumMusicRole... musicRole);

    @Query("SELECT COUNT(*) FROM albums_musics_role WHERE music_id = :musicId and album_id = :albumId")
    int existInAlbum(long musicId, long albumId);

    @Insert
    void addLibrary(UserAlbum... library);

    @Query("SELECT COUNT(*) FROM user_albums WHERE user_albums.album_id = :albumId")
    int existLibrary(long albumId);

    @Query("UPDATE user_albums SET deleted_at = :deleted, created_at = :deleted WHERE user_albums.album_id = :albumId")
    void subLibrary(String deleted, long albumId);

    @Query("UPDATE user_albums SET deleted_at = NULL, created_at = :created WHERE user_albums.album_id = :albumId")
    void updateLibrary(String created, long albumId);

    @Query("UPDATE user_albums SET deleted_at = :deleted, created_at = :created WHERE user_albums.album_id = :albumId")
    void updateLibrary(String created, String deleted, long albumId);

    @Transaction
    @Query("SELECT * FROM albums WHERE name LIKE :search OR nameFa LIKE :search OR performer LIKE :search OR performerFa LIKE :search LIMIT 6")
    DataSource.Factory<Integer, AlbumLibrary> searchPaginate(String search);

    @Query("SELECT COUNT(*) FROM albums WHERE name LIKE :search OR nameFa LIKE :search OR performer LIKE :search OR performerFa LIKE :search")
    LiveData<Integer> searchCount(String search);

    @Transaction
    @Query("SELECT * FROM albums WHERE id in (:arrayId)")
    DataSource.Factory<Integer, AlbumLibrary> whereInIds(long[] arrayId);

    @Query("SELECT * FROM user_albums WHERE created_at >= :synced_at")
    List<UserAlbum> libraryRoot(String synced_at);

    @Query("SELECT * FROM user_albums")
    List<UserAlbum> libraryRoot();

    @Query("SELECT album_id FROM user_albums WHERE album_id NOT IN (SELECT id FROM albums) LIMIT 50")
    List<Long> withoutModel();

    @Query("SELECT * FROM artists WHERE id in (SELECT artist_id FROM artist_album_role WHERE album_id = :albumId)")
    LiveData<List<Artist>> artistInAlbum(long albumId);
}
