package rahbari.erfan.mosito.resources.Daos;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import rahbari.erfan.mosito.models.Artist;
import rahbari.erfan.mosito.models.Music;
import rahbari.erfan.mosito.models.MusicDownload;
import rahbari.erfan.mosito.models.MusicDownloadEm;
import rahbari.erfan.mosito.models.MusicLibrary;
import rahbari.erfan.mosito.models.UserMusic;

@Dao
public interface MusicDao {
    @Insert
    void insert(Music... music);

    @Update
    void update(Music... music);

    @Query("SELECT COUNT(*) FROM musics WHERE id = :id")
    int count(long id);

    @Transaction
    @Query("SELECT * FROM musics WHERE deleted_at IS NULL AND musics.id = :musicId LIMIT 1")
    MusicLibrary musicLibrary(long musicId);

    @Query("SELECT * FROM musics WHERE deleted_at IS NULL AND musics.id = :musicId LIMIT 1")
    Music music(long musicId);

    @Transaction
    @Query("SELECT * FROM musics WHERE deleted_at IS NULL ORDER BY created_at DESC LIMIT :limit")
    DataSource.Factory<Integer, MusicLibrary> latestLimit(int limit);

    @Transaction
    @Query("SELECT * FROM musics WHERE deleted_at IS NULL AND id IS NOT NULL ORDER BY created_at DESC")
    DataSource.Factory<Integer, MusicLibrary> latest();

    @Query("SELECT * FROM musics WHERE deleted_at IS NULL AND created_at >= :created ORDER BY downloads DESC LIMIT :limit")
    DataSource.Factory<Integer, Music> mostPlayed(String created, int limit);

    @Transaction
    @Query("SELECT musics.* FROM musics JOIN user_musics ON user_musics.music_id = musics.id WHERE musics.deleted_at IS NULL AND user_musics.deleted_at IS NULL ORDER BY user_musics.created_at DESC")
    DataSource.Factory<Integer, MusicLibrary> libraryPaginate();

    @Transaction
    @Query("SELECT musics.* FROM musics JOIN user_musics ON user_musics.music_id = musics.id WHERE musics.deleted_at IS NULL AND user_musics.deleted_at IS NULL ORDER BY user_musics.created_at DESC LIMIT 50")
    DataSource.Factory<Integer, MusicLibrary> recentlyAdded();

    @Query("SELECT musics.id FROM musics JOIN user_musics ON user_musics.music_id = musics.id WHERE musics.deleted_at IS NULL AND user_musics.deleted_at IS NULL ORDER BY user_musics.created_at DESC LIMIT 50")
    List<Long> libraryIds();

    @Query("SELECT COUNT(*) FROM user_musics WHERE user_musics.deleted_at IS NULL")
    LiveData<Integer> libraryCount();

    @Insert
    void addLibrary(UserMusic... library);

    @Query("SELECT COUNT(*) FROM user_musics WHERE user_musics.music_id = :musicId")
    int existLibrary(long musicId);

    @Query("UPDATE user_musics SET deleted_at = :deleted, created_at = :deleted WHERE user_musics.music_id = :musicId")
    void subLibrary(String deleted, long musicId);

    @Query("UPDATE user_musics SET deleted_at = :deleted, created_at = :created WHERE user_musics.music_id = :musicId")
    void updateLibrary(String created, String deleted, long musicId);

    @Query("UPDATE user_musics SET deleted_at = NULL, created_at = :created WHERE user_musics.music_id = :musicId")
    void updateLibrary(String created, long musicId);

    @Query("UPDATE user_musics SET download = 1 WHERE music_id = :musicId AND deleted_at IS NULL")
    void download(long musicId);

    @Transaction
    @Query("SELECT musics.* FROM musics JOIN user_musics ON user_musics.music_id = musics.id WHERE musics.deleted_at IS NULL AND user_musics.deleted_at IS NULL AND user_musics.uri IS NULL AND user_musics.download = 1")
    LiveData<List<MusicLibrary>> downloadQueue();

    @Query("UPDATE user_musics SET uri = :uri WHERE music_id = :musicId")
    void updateURI(String uri, long musicId);

    @Transaction
    @Query("SELECT * FROM musics WHERE title LIKE :search OR titleFa LIKE :search OR performer LIKE :search OR performerFa LIKE :search OR lyric LIKE :search LIMIT 12")
    DataSource.Factory<Integer, MusicLibrary> searchPaginate(String search);

    @Query("SELECT COUNT(*) FROM musics WHERE title LIKE :search OR titleFa LIKE :search OR performer LIKE :search OR performerFa LIKE :search OR lyric LIKE :search")
    LiveData<Integer> searchCount(String search);

    @Transaction
    @Query("SELECT * FROM musics WHERE id in (:arrayId)")
    DataSource.Factory<Integer, MusicLibrary> whereInIds(long[] arrayId);

    @Query("SELECT * FROM user_musics WHERE created_at >= :synced_at")
    List<UserMusic> libraryRoot(String synced_at);

    @Query("SELECT * FROM user_musics")
    List<UserMusic> libraryRoot();

    @Query("SELECT music_id FROM user_musics WHERE music_id NOT IN (SELECT id FROM musics) LIMIT 50")
    List<Long> withoutModel();

    @Insert
    void insertDownload(MusicDownload musicDownload);

    @Query("UPDATE downloads SET percent = :percent WHERE music_id = :musicId")
    void updateDownload(long musicId, long percent);

    @Query("DELETE FROM downloads WHERE music_id = :musicId")
    void deleteDownload(long musicId);

    @Transaction
    @Query("SELECT musics.* FROM musics JOIN user_musics ON user_musics.music_id = musics.id WHERE musics.deleted_at IS NULL AND user_musics.deleted_at IS NULL AND user_musics.uri IS NULL AND user_musics.download = 1 ORDER BY musics.id DESC")
    LiveData<List<MusicDownloadEm>> selectDownloads(); //todo :: need upgrade pagination

    @Query("SELECT * FROM artists WHERE id in (SELECT artist_id FROM artist_musics_role WHERE music_id = :musicId)")
    LiveData<List<Artist>> artistInMusic(long musicId);

    @Query("SELECT * FROM musics WHERE id IN (:ids)")
    DataSource.Factory<Integer, MusicLibrary> musicsIds(List<Long> ids);
}
