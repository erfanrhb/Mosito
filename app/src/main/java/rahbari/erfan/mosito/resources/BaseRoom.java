package rahbari.erfan.mosito.resources;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import rahbari.erfan.mosito.converters.AlbumConverter;
import rahbari.erfan.mosito.converters.ArtistsConverter;
import rahbari.erfan.mosito.converters.MusicConverter;
import rahbari.erfan.mosito.converters.MusicsConverter;
import rahbari.erfan.mosito.converters.ProfilesConverter;
import rahbari.erfan.mosito.converters.StatusConverter;
import rahbari.erfan.mosito.converters.UserConverter;
import rahbari.erfan.mosito.models.Album;
import rahbari.erfan.mosito.models.AlbumMusicRole;
import rahbari.erfan.mosito.models.Artist;
import rahbari.erfan.mosito.models.ArtistAlbumRole;
import rahbari.erfan.mosito.models.ArtistMusicRole;
import rahbari.erfan.mosito.models.Music;
import rahbari.erfan.mosito.models.MusicDownload;
import rahbari.erfan.mosito.models.Playlist;
import rahbari.erfan.mosito.models.PlaylistMusicRole;
import rahbari.erfan.mosito.models.User;
import rahbari.erfan.mosito.models.UserAlbum;
import rahbari.erfan.mosito.models.UserMusic;
import rahbari.erfan.mosito.models.UserPlaylist;
import rahbari.erfan.mosito.resources.Daos.AlbumDao;
import rahbari.erfan.mosito.resources.Daos.ArtistDao;
import rahbari.erfan.mosito.resources.Daos.MusicDao;
import rahbari.erfan.mosito.resources.Daos.PlaylistDao;
import rahbari.erfan.mosito.resources.Daos.UserDao;

@TypeConverters({
        ArtistsConverter.class,
        ProfilesConverter.class,
        MusicConverter.class,
        MusicsConverter.class,
        AlbumConverter.class,
        StatusConverter.class,
        UserConverter.class
})
@Database(entities = {
        Artist.class,
        User.class,
        Music.class,
        Album.class,
        UserMusic.class,
        UserAlbum.class,
        ArtistAlbumRole.class,
        ArtistMusicRole.class,
        AlbumMusicRole.class,
        Playlist.class,
        UserPlaylist.class,
        MusicDownload.class,
        PlaylistMusicRole.class}, version = 2, exportSchema = false)
public abstract class BaseRoom extends RoomDatabase {

    public abstract UserDao userDao();

    public abstract MusicDao musicDao();

    public abstract AlbumDao albumDao();

    public abstract ArtistDao artistDao();


    public abstract PlaylistDao playlistDao();

    private static volatile BaseRoom INSTANCE;

    public static BaseRoom getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (BaseRoom.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), BaseRoom.class, "mosito")
                            .fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;
    }
}
