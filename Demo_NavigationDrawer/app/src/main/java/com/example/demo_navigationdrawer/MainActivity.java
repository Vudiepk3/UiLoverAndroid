package com.example.demo_navigationdrawer;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.nav_logout) {
            confirmLogout();
        } else if (itemId == R.id.nav_evaluate) {
            openLink("https://play.google.com/store/apps/details?id=com.dhcn.MyHAUI&pcampaignid=web_share");
        } else if (itemId == R.id.nav_share) {
            shareApplication();
        } else if (itemId == R.id.nav_feedback) {
            sendFeedback();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }
    private void confirmLogout() {
        View alertCustomDialog = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_logout, null);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

        alertDialog.setView(alertCustomDialog);
        ImageButton cancelImage = alertCustomDialog.findViewById(R.id.imgCancel);
        Button btnLogout = alertCustomDialog.findViewById(R.id.btnLogout);
        final AlertDialog dialog = alertDialog.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        cancelImage.setOnClickListener(v -> dialog.cancel());

        btnLogout.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "Cảm ơn bạn đã sử dụng sản phẩm", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> finish(), 2000);
        });
    }

    private void openLink(String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareApplication() {
        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
            intent.putExtra(Intent.EXTRA_TEXT, "This is my text");
            startActivity(Intent.createChooser(intent, "Hãy chia sẻ đến với mọi người"));
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendFeedback() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:vulq2k3@gmail.com"));
            intent.putExtra(Intent.EXTRA_SUBJECT, "Xin Chào");
            intent.putExtra(Intent.EXTRA_TEXT, "Hi");
            startActivity(Intent.createChooser(intent, "Send mail..."));
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
