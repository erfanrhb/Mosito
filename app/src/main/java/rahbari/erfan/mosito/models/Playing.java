package rahbari.erfan.mosito.models;

import java.util.List;

import rahbari.erfan.mosito.enums.Command;
import rahbari.erfan.mosito.enums.Status;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class Playing {
    private Music music;
    private Status status;
    private Integer position;
    private Integer percent;
    private Integer buffer;
    private List<Long> musicList;
    private Command command;
    private Integer seekTo;

    private final PublishSubject<Music> musicIdPublish = PublishSubject.create();
    private final PublishSubject<Integer> seekToPublish = PublishSubject.create();
    private final PublishSubject<Status> statusPublishSubject = PublishSubject.create();
    private final PublishSubject<Integer> bufferPublishSubject = PublishSubject.create();
    private final PublishSubject<Integer> percentPublishSubject = PublishSubject.create();
    private final PublishSubject<Command> commandPublishSubject = PublishSubject.create();
    private final PublishSubject<Integer> positionPublishSubject = PublishSubject.create();
    private final PublishSubject<List<Long>> musicListPublishSubject = PublishSubject.create();

    public Observable<Integer> seekToObservable() {
        return seekToPublish;
    }

    public Observable<Command> commandObservable() {
        return commandPublishSubject;
    }

    public Observable<List<Long>> musicListObservable() {
        return musicListPublishSubject;
    }

    public Observable<Integer> bufferObservable() {
        return bufferPublishSubject;
    }

    public Observable<Integer> percentObservable() {
        return percentPublishSubject;
    }

    public Observable<Integer> positionObservable() {
        return positionPublishSubject;
    }

    public Observable<Status> statusObservable() {
        return statusPublishSubject;
    }

    public Observable<Music> musicIdObservable() {
        return musicIdPublish;
    }

    public Music getMusic() {
        return music;
    }

    public Command getCommand() {
        return command;
    }

    public void setSeekTo(Integer seekTo) {
        this.seekTo = seekTo;
        this.seekToPublish.onNext(seekTo);
    }

    public Integer getSeekTo() {
        return seekTo != null ? seekTo : 0;
    }

    public void setCommand(Command command) {
        this.command = command;
        commandPublishSubject.onNext(command);
    }

    public void setMusic(Music music) {
        this.music = music;
        musicIdPublish.onNext(music);
    }

    public void setMusicList(List<Long> musicList) {
        this.musicList = musicList;
        musicListPublishSubject.onNext(musicList);
    }

    public List<Long> getMusicList() {
        return musicList;
    }

    public Status getStatus() {
        return status != null ? status : Status.PAUSE;
    }

    public void setStatus(Status status) {
        this.status = status;
        statusPublishSubject.onNext(status);
    }

    public int getPosition() {
        return position != null ? position : 0;
    }

    public void setPosition(int position) {
        this.position = position;
        positionPublishSubject.onNext(position);
    }

    public int getPercent() {
        return percent != null ? buffer : 0;
    }

    public void setPercent(int percent) {
        this.percent = percent;
        percentPublishSubject.onNext(percent);
    }

    public int getBuffer() {
        return buffer != null ? buffer : 0;
    }

    public void setBuffer(int buffer) {
        this.buffer = buffer;
        bufferPublishSubject.onNext(buffer);
    }
}
