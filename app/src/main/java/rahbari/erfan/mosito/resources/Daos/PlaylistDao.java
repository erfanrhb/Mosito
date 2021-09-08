package rahbari.erfan.mosito.resources.Daos;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;
import androidx.lifecycle.LiveData;

import java.util.List;

import rahbari.erfan.mosito.models.MusicLibrary;
import rahbari.erfan.mosito.models.Playlist;
import rahbari.erfan.mosito.models.PlaylistLibrary;
import rahbari.erfan.mosito.models.PlaylistMusicRole;
import rahbari.erfan.mosito.models.UserPlaylist;

@Dao
public interface PlaylistDao {
    @Insert
    void create(Playlist... playlists);

    @Update
    void update(Playlist... playlists);

    @Query("SELECT COUNT(*) FROM playlists WHERE id = :Id")
    int count(long Id);

    @Transaction
    @Query("SELECT * FROM playlists WHERE deleted_at IS NULL ORDER BY created_at DESC LIMIT 25")
    LiveData<List<PlaylistLibrary>> latest();

    @Transaction
    @Query("SELECT * FROM playlists WHERE deleted_at IS NULL AND id = :playlistId LIMIT 1")
    LiveData<PlaylistLibrary> playlist(long playlistId);

    @Transaction
    @Query("SELECT playlists.* FROM playlists JOIN user_playlists ON user_playlists.playlist_id = playlists.id WHERE playlists.deleted_at IS NULL AND user_playlists.deleted_at IS NULL ORDER BY user_playlists.created_at DESC")
    DataSource.Factory<Integer, PlaylistLibrary> libraryPaginate();

    @Query("SELECT COUNT(*) FROM user_playlists WHERE user_playlists.deleted_at IS NULL")
    LiveData<Integer> libraryCount();

    @Transaction
    @Query("SELECT musics.* FROM musics " +
            "JOIN playlists_musics_role ON playlists_musics_role.music_id = musics.id " +
            "WHERE musics.deleted_at IS NULL AND playlists_musics_role.playlist_id = :playlistId " +
            "GROUP BY musics.id ORDER BY musics.created_at LIMIT 20")
    DataSource.Factory<Integer, MusicLibrary> musics(long playlistId);

    @Insert
    void addToPlaylist(PlaylistMusicRole... musicRole);

    @Query("SELECT COUNT(*) FROM playlists_musics_role WHERE playlist_id = :playlistId AND music_id = :musicId")
    int existMusic(long playlistId, long musicId);

    @Insert
    void addLibrary(UserPlaylist... library);

    @Query("SELECT COUNT(*) FROM user_playlists WHERE user_playlists.playlist_id = :playlistId")
    int existLibrary(long playlistId);

    @Query("UPDATE user_playlists SET deleted_at = :deleted, created_at = :deleted WHERE user_playlists.playlist_id = :playlistId")
    void subLibrary(String deleted, long playlistId);

    @Query("UPDATE user_playlists SET deleted_at = NULL, created_at = :created WHERE user_playlists.playlist_id = :playlistId")
    void updateLibrary(String created, long playlistId);

    @Query("UPDATE user_playlists SET deleted_at = :deleted, created_at = :created WHERE user_playlists.playlist_id = :playlistId")
    void updateLibrary(String created, String deleted, long playlistId);

    @Transaction
    @Query("SELECT * FROM playlists WHERE title LIKE :search OR description LIKE :search LIMIT 6")
    DataSource.Factory<Integer, PlaylistLibrary> searchPaginate(String search);

    @Query("SELECT COUNT(*) FROM playlists WHERE title LIKE :search OR description LIKE :search")
    LiveData<Integer> searchCount(String search);

    @Transaction
    @Query("SELECT * FROM playlists WHERE id in (:arrayId)")
    DataSource.Factory<Integer, PlaylistLibrary> whereInIds(long[] arrayId);

    @Query("SELECT * FROM user_playlists WHERE created_at >= :synced_at")
    List<UserPlaylist> libraryRoot(String synced_at);

    @Query("SELECT * FROM user_playlists")
    List<UserPlaylist> libraryRoot();

    @Query("SELECT playlist_id FROM user_playlists WHERE playlist_id NOT IN (SELECT id FROM playlists) LIMIT 50")
    List<Long> withoutModel();
}
