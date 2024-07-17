package com.example.demo_customer_notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class NotificationReceiver extends BroadcastReceiver {
    // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && "BTN_ACTION".equals(intent.getAction())) {
            // Get the message from the intent extras
            String msg = intent.getStringExtra("msg");
            // Show a toast message with the received message
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }
    }
}
