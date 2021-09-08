package rahbari.erfan.mosito.models;

import androidx.room.Embedded;
import androidx.room.Relation;

import rahbari.erfan.mosito.resources.BaseModel;

public class MusicLibrary extends BaseModel {
    @Embedded
    public Music music;
    @Relation(parentColumn = "id", entityColumn = "music_id")
    public UserMusic library;

    @Override
    public long getId() {
        return music.getId();
    }

    public Music getMusic() {
        return music;
    }

    public void setMusic(Music music) {
        this.music = music;
    }

    public UserMusic getLibrary() {
        return library;
    }

    public void setLibrary(UserMusic library) {
        this.library = library;
    }
}
