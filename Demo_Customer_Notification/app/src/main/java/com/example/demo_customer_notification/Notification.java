package com.example.demo_customer_notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

public class Notification {
    private final Context context;
    private final NotificationManager notificationManager;
    private int request = 0;

    // Constructor to initialize the Notification object with context
    public Notification(Context context) {
        this.context = context;
        // Get the system service for notification management
        this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    // Method to initialize and show the notification
    public void initNotification() {
        // Create a notification channel for Android O and above
        String notification_id = "1000";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String notification_channel = "Notification";
            NotificationChannel channel = new NotificationChannel(notification_id, notification_channel, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        // Inflate the custom notification layout
        RemoteViews customview = new RemoteViews(context.getPackageName(), R.layout.notilay);
        // Set click listeners for buttons in the custom layout
        customview.setOnClickPendingIntent(R.id.btn1, getBroadcast("btn1 call"));
        customview.setOnClickPendingIntent(R.id.btn2, getBroadcast("btn2 call"));
        customview.setOnClickPendingIntent(R.id.btn3, getBroadcast("btn3 call"));
        // Set the text for the TextViews in the custom layout
        customview.setTextViewText(R.id.text2, "body");
        customview.setTextViewText(R.id.text1, "title");

        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, notification_id)
                .setSmallIcon(R.drawable.image_notification)  // Set the small icon for the notification
                .setContent(customview);  // Set the custom layout as the notification content
        android.app.Notification notification = builder.build();
        // Show the notification
        notificationManager.notify(Integer.parseInt(notification_id), notification);
    }

    // Method to create a PendingIntent for the broadcast receiver
    private PendingIntent getBroadcast(String msg) {
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.setAction("BTN_ACTION");
        intent.putExtra("msg", msg);
        request++;
        // Create a PendingIntent for the broadcast receiver
        return PendingIntent.getBroadcast(context, request, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
