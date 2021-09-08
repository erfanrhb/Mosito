package rahbari.erfan.mosito.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LifecycleService;

import com.bumptech.glide.Glide;

import rahbari.erfan.mosito.Application;
import rahbari.erfan.mosito.MainActivity;
import rahbari.erfan.mosito.R;
import rahbari.erfan.mosito.enums.Command;
import rahbari.erfan.mosito.enums.Status;
import rahbari.erfan.mosito.models.Music;
import rahbari.erfan.mosito.resources.BaseRetrofit;
import rahbari.erfan.mosito.utils.SubjectWrapper;
import io.reactivex.schedulers.Schedulers;

public class NotificationService extends LifecycleService {
    private static final String ON_PLAY_PAUSE_CLICK = "PLAY_PAUSE_CLICK";
    private static final String ON_NEXT_CLICK = "NEXT_CLICK";
    private static final String BROADCAST_CHANNEL = "BROADCAST_CHANNEL";
    private static final String CHANNEL_ID = "Player Notification";
    private static final int NOTIFY_ID = 4161;

    private NotificationManager notifyManager;
    private Notification playerNotification;
    private RemoteViews remoteView;

    private int lastStat = -1;

    @Override
    public void onCreate() {
        super.onCreate();

        notifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        createNotificationChannel();

        remoteView = new RemoteViews(getPackageName(), R.layout.notification_small);

        Intent resultIntent = new Intent(this, MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        remoteView.setOnClickPendingIntent(R.id.btnPlayNotify, getPendingSelfIntent(getApplicationContext(), ON_PLAY_PAUSE_CLICK));
        remoteView.setOnClickPendingIntent(R.id.btnNextNotify, getPendingSelfIntent(getApplicationContext(), ON_NEXT_CLICK));

        playerNotification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_play)
                .setOngoing(true)
                .setContentIntent(resultPendingIntent)
                .setAutoCancel(false)
                .setSilent(true)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(remoteView)
                .build();

        Application.playing.musicIdObservable().observeOn(Schedulers.io()).subscribe(new SubjectWrapper<Music>() {
            @Override
            public void onChange(Music val) {
                onMusicChange(val);
            }
        });

        Application.playing.statusObservable().observeOn(Schedulers.io()).subscribe(new SubjectWrapper<Status>() {
            @Override
            public void onChange(Status val) {
                onMusicStatusChange(val);
            }
        });
    }

    private void onMusicStatusChange(Status status) {
        switch (status) {
            case PLAY:
                playerNotification.icon = R.drawable.ic_play;
                break;
            case PAUSE:
                playerNotification.icon = R.drawable.ic_pause;
                break;
            default:
                playerNotification.icon = R.drawable.ic_time;
        }

        if (this.lastStat != playerNotification.icon) {
            notifyManager.notify(NOTIFY_ID, playerNotification);
            this.lastStat = playerNotification.icon;
        }
    }

    private void onMusicChange(Music music) {
        try {
            if (music == null) {
                notifyManager.cancel(NOTIFY_ID);
                return;
            }

            Bitmap bitmap = Glide.with(getApplicationContext())
                    .asBitmap()
                    .load(BaseRetrofit.BASE_DOWN_URL + music.getPicture())
                    .placeholder(R.drawable.ic_headset)
                    .submit(256, 256)
                    .get();

            remoteView.setImageViewBitmap(R.id.imgPicture, bitmap);
            remoteView.setTextViewText(R.id.tvTitle, music.getTitle());
            remoteView.setTextViewText(R.id.tvPerformer, music.getPerformer());

            notifyManager.notify(NOTIFY_ID, playerNotification);

        } catch (Exception exception) {
            Log.e("NotificationService", exception.getMessage(), exception);
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_ID, importance);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent i = new Intent(BROADCAST_CHANNEL);
        i.putExtra("action", action);
        return PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    @Override
    public void onStart(@Nullable @org.jetbrains.annotations.Nullable Intent intent, int startId) {
        super.onStart(intent, startId);
        registerReceiver(broadcastReceiver, new IntentFilter(BROADCAST_CHANNEL));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
        notifyManager.cancelAll();
    }


    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            switch (intent.getStringExtra("action")) {
                case ON_PLAY_PAUSE_CLICK:
                    if (Application.playing.getStatus() == Status.PLAY)
                        Application.playing.setCommand(Command.Pause);
                    else if (Application.playing.getMusic() != null)
                        Application.playing.setCommand(Command.Play);
                    break;
                case ON_NEXT_CLICK:
                    Application.playing.setCommand(Command.Next);
                    break;
                default:
                    Log.e("onReceive", intent.getStringExtra("action"));
            }

        }
    };
}
