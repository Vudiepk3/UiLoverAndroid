package com.example.demo_slideimagewithfirebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DetailActivity extends AppCompatActivity {

    // Khai báo các thành phần giao diện
    TextView detailNameImage, detailLinkWeb, detailNoteImage;
    ImageView detailImage;
    FloatingActionButton deleteButton, editButton;

    // Khởi tạo biến
    String key = "";
    String imageUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Ánh xạ các thành phần giao diện với biến
        detailNameImage = findViewById(R.id.detailNameImage);
        detailLinkWeb = findViewById(R.id.detailLinkWeb);
        detailNoteImage = findViewById(R.id.detailNoteImage);
        detailImage = findViewById(R.id.detailUrlImage);

        deleteButton = findViewById(R.id.deleteButton);
        editButton = findViewById(R.id.editButton);

        // Lấy dữ liệu từ Intent bundle
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            detailNameImage.setText(bundle.getString("NameImage"));
            detailLinkWeb.setText(bundle.getString("LinkWeb"));
            detailNoteImage.setText(bundle.getString("NoteImage"));
            key = bundle.getString("Key");
            imageUrl = bundle.getString("UrlImage");

            // Hiển thị hình ảnh vào ImageView sử dụng Glide
            Glide.with(this).load(bundle.getString("UrlImage")).into(detailImage);
        }

        // Thiết lập sự kiện click cho nút xóa
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Xóa hình ảnh từ Firebase Storage và dữ liệu tương ứng từ Realtime Database
                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SlideImage");
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageReference = storage.getReferenceFromUrl(imageUrl);
                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        reference.child(key).removeValue(); // Xóa dữ liệu từ Firebase Realtime Database
                        Toast.makeText(DetailActivity.this, "Đã xóa", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish(); // Kết thúc activity hiện tại
                    }
                });
            }
        });

        // Thiết lập sự kiện click cho nút chỉnh sửa
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Chuyển sang UpdateActivity với dữ liệu để chỉnh sửa
                Intent intent = new Intent(DetailActivity.this, UpdateActivity.class)
                        .putExtra("NameImage", detailNameImage.getText().toString())
                        .putExtra("LinkWeb", detailLinkWeb.getText().toString())
                        .putExtra("NoteImage", detailNoteImage.getText().toString())
                        .putExtra("UrlImage", imageUrl)
                        .putExtra("Key", key);
                startActivity(intent);
            }
        });
    }
}
