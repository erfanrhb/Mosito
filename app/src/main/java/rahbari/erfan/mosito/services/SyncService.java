package rahbari.erfan.mosito.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.lifecycle.LifecycleService;

import rahbari.erfan.mosito.resources.Repositories.SyncRep;

public class SyncService extends LifecycleService {
    private SyncRep rep;
    private BroadcastReceiver receiver;


    @Override
    public void onCreate() {
        super.onCreate();

        rep = new SyncRep();

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                sync();
            }
        };

        registerReceiver(receiver, new IntentFilter("SYNC_SERVICE"));

        sync();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        unregisterReceiver(receiver);
    }

    private void sync() {
        new Thread(() -> rep.sync(response -> objectDownload())).start();
    }

    private void objectDownload() {
        new Thread(() -> rep.musicWithoutModel()).start();
        new Thread(() -> rep.albumWithoutModel()).start();
        new Thread(() -> rep.playlistWithoutModel()).start();
    }
}
