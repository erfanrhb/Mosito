package rahbari.erfan.mosito.models;

import androidx.room.Embedded;
import androidx.room.Relation;

import rahbari.erfan.mosito.resources.BaseModel;

public class MusicDownloadEm extends BaseModel {
    @Embedded
    public Music music;
    @Relation(parentColumn = "id", entityColumn = "music_id")
    public MusicDownload download;

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

    public MusicDownload getDownload() {
        return download;
    }

    public void setDownload(MusicDownload download) {
        this.download = download;
    }
}
