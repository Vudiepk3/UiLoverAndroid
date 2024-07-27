package com.example.demo_intent;

import android.content.ActivityNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.content.Intent;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final String link = "https://play.google.com/store/apps/details?id=";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        getID();
    }
    private void getID(){
        //start activity
        Button btnStartActivity = findViewById(R.id.btnStartActivity);
        btnStartActivity.setOnClickListener(v -> {
            Intent intent = new Intent(this, Activity1.class);
            startActivity(intent);
        });
        //share link app
        Button btnShare = findViewById(R.id.btnShare);
        btnShare.setOnClickListener(v -> {
            try{
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "This is an awesome app for your android mobile, Check it ..."+
                        link+ getPackageName());
                startActivity(Intent.createChooser(shareIntent, "Share Link"));
            }catch (Exception e){
                Log.e("ShareIntent", "Error while sharing content: " + e.getMessage());
            }
        });
        //rating app in CHPlay
        Button btnRateApp = findViewById(R.id.btnRateApp);
        btnRateApp.setOnClickListener(v -> {
            try {
                Intent rateIntent = new Intent(Intent.ACTION_VIEW);
                rateIntent.setData(Uri.parse(link + getPackageName()));
                rateIntent.setPackage("com.android.vending");
                startActivity(rateIntent);
            }catch (ActivityNotFoundException e){
                Intent rateIntent = new Intent(Intent.ACTION_VIEW);
                rateIntent.setData(Uri.parse("market://details?id=" + getPackageName()));
                startActivity(rateIntent);
            }
        });
    }

}