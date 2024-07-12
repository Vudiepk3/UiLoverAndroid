package com.example.demo_localnotificationsandroid;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.demo_localnotificationsandroid.databinding.ActivityMainBinding;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        createNotificationChannel();
        binding.submitButton.setOnClickListener(v -> scheduleNotification());
    }

    @SuppressLint("ScheduleExactAlarm")
    private void scheduleNotification() {
        Intent intent = new Intent(getApplicationContext(), Notification.class);
        String title = Objects.requireNonNull(binding.titleET.getText()).toString();
        String message = Objects.requireNonNull(binding.messageET.getText()).toString();
        intent.putExtra(Notification.titleExtra, title);
        intent.putExtra(Notification.messageExtra, message);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getApplicationContext(),
                Notification.notificationID,
                intent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        long time = getTime();
        alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                time,
                pendingIntent
        );
        showAlert(time, title, message);
    }

    private void showAlert(long time, String title, String message) {
        Date date = new Date(time);
        DateFormat dateFormat =
                android.text.format.DateFormat.getLongDateFormat(getApplicationContext());
        DateFormat timeFormat =
                android.text.format.DateFormat.getTimeFormat(getApplicationContext());

        new AlertDialog.Builder(this)
                .setTitle("Notification Scheduled")
                .setMessage("Title: " + title +
                        "\nMessage: " + message +
                        "\nAt: " + dateFormat.format(date) + " " + timeFormat.format(date))
                .setPositiveButton("Okay", (dialog, which) -> {})
                .show();
    }

    private long getTime() {
        int minute = binding.timePicker.getMinute();
        int hour = binding.timePicker.getHour();
        int day = binding.datePicker.getDayOfMonth();
        int month = binding.datePicker.getMonth();
        int year = binding.datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute);
        return calendar.getTimeInMillis();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name = "Notif Channel";
            String desc = "A Description of the Channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(Notification.channelID, name, importance);
            channel.setDescription(desc);
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
