package com.example.demo_localnotificationsandroid;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;

public class Notification extends BroadcastReceiver {
    public static final int notificationID = 1;
    public static final String channelID = "channel1";
    public static final String titleExtra = "titleExtra";
    public static final String messageExtra = "messageExtra";

    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra(titleExtra);
        String message = intent.getStringExtra(messageExtra);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(context, channelID)
                .setSmallIcon(R.drawable.image_notificion)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(notificationID, notification.build());
    }
}
