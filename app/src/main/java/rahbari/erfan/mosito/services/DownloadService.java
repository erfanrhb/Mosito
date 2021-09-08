package rahbari.erfan.mosito.services;

import android.os.Environment;
import android.util.Log;

import androidx.lifecycle.LifecycleService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import rahbari.erfan.mosito.models.Download;
import rahbari.erfan.mosito.models.ErrorHandler;
import rahbari.erfan.mosito.models.Music;
import rahbari.erfan.mosito.models.MusicDownload;
import rahbari.erfan.mosito.resources.Apis.DownloadApi;
import rahbari.erfan.mosito.resources.BaseRetrofit;
import rahbari.erfan.mosito.resources.Repositories.MusicRep;
import rahbari.erfan.mosito.utils.CallWrapper;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class DownloadService extends LifecycleService {
    private final MusicRep rep;
    private final DownloadApi downloadApi;
    private long lastPercent = 0;
    private boolean downloading = false;

    public DownloadService() {
        this.rep = new MusicRep();
        this.downloadApi = new DownloadApi();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        rep.downloadQueue().observe(this, musicLibraries -> {
            if (!downloading && musicLibraries.size() > 0) {
                downloadLink(musicLibraries.get(0).getMusic());
            }
        });
    }

    private void downloadLink(Music music) {
        downloading = true;

        Log.e("DownloadService", "started " + music.getTitle() + " - " + music.getPerformer());

        Log.e("DownloadService", "current address   : " + BaseRetrofit.BASE_URL);

        downloadApi.requestFile(music.getId()).observeOn(Schedulers.io()).subscribe(new CallWrapper<Download>() {
            @Override
            protected void onSuccess(Download download) {

                Log.e("DownloadService", "download link     : " + download.getUrl());
                Log.e("DownloadService", "download source   : " + download.getSource());

                downloadApi.download(download.getUrl()).observeOn(Schedulers.io()).subscribe(new CallWrapper<ResponseBody>() {
                    @Override
                    protected void onSuccess(ResponseBody responseBody) {
                        writeStorage(music, responseBody);
                    }

                    @Override
                    protected void onError(int status, ErrorHandler response) {
                        Log.e("DownloadService", response.getMessage());
                    }
                });

            }

            @Override
            protected void onError(int status, ErrorHandler response) {
                Log.e("DownloadService", response.getMessage());
            }
        });


    }

    private void writeStorage(Music music, ResponseBody body) {
        try {
            new Thread(() -> rep.getMusicDao().insertDownload(new MusicDownload(music.getId()))).start();

            File storage = getApplication().getExternalFilesDir(Environment.DIRECTORY_MUSIC);
            File file = File.createTempFile(String.valueOf(music.getId()), ".mp3", storage);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(file);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    long currentPercent = (fileSizeDownloaded * 100) / fileSize;
                    if (Math.abs(currentPercent - lastPercent) > 5 || currentPercent == 100) {
                        lastPercent = currentPercent;
                        Log.e("DownloadService", "Download " + currentPercent + "%");
                        new Thread(() -> rep.getMusicDao().updateDownload(music.getId(), currentPercent)).start();
                    }
                }

                outputStream.flush();

                Log.e("DownloadService", "Download completed");

                rep.getMusicDao().updateURI(file.getPath(), music.getId());

                downloading = false;
            } catch (IOException e) {
                Log.e("DownloadService", e.getMessage(), e);
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }

            }
        } catch (IOException e) {
            Log.e("DownloadService", e.getMessage(), e);
        } finally {
            new Thread(() -> rep.getMusicDao().deleteDownload(music.getId())).start();
        }
    }
}