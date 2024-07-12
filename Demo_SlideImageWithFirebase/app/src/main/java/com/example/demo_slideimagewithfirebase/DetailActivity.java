package com.example.demo_slideimagewithfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.demo_slideimagewithfirebase.model.ImageModel;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DetailActivity extends AppCompatActivity {

    // Khai báo các thành phần giao diện
    TextView detailNameImage, detailLinkWeb, detailNoteImage;
    ImageView detailImage;
    FloatingActionButton deleteButton, editButton;
    DatabaseReference databaseReference;
    // Khởi tạo biến
    String key = "";
    String imageUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getID();

        // Hiển thị dialog tiến trình
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(DetailActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();

        // Lấy dữ liệu từ Intent bundle
        databaseReference = FirebaseDatabase.getInstance().getReference("SlideImage");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            key = bundle.getString("Key");
            if (key != null) {
                loadDataFromFirebase();
                dialog.dismiss();
            } else {
                // Xử lý trường hợp không có Key
                Toast.makeText(this, "Không tìm thấy dữ liệu", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        // Phương thức xóa
        deleteImage();
        // Phương thức chỉnh sửa
        editImage();

    }
    private void getID(){
        // Ánh xạ các thành phần giao diện với biến
        detailNameImage = findViewById(R.id.detailNameImage);
        detailLinkWeb = findViewById(R.id.detailLinkWeb);
        detailNoteImage = findViewById(R.id.detailNoteImage);
        detailImage = findViewById(R.id.detailUrlImage);
        deleteButton = findViewById(R.id.deleteButton);
        editButton = findViewById(R.id.editButton);
    }
    private void loadDataFromFirebase() {
        databaseReference.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ImageModel imageModel = snapshot.getValue(ImageModel.class);
                    if (imageModel != null) {
                        detailNameImage.setText(imageModel.getNameImage());
                        detailLinkWeb.setText(imageModel.getLinkWeb());
                        detailNoteImage.setText(imageModel.getNoteImage());
                        imageUrl = imageModel.getUrlImage();

                        // Load hình ảnh sử dụng Glide
                        Glide.with(DetailActivity.this).load(imageUrl).into(detailImage);
                    }
                } else {
                    // Xử lý trường hợp không tìm thấy dữ liệu với Key đã cho
                    Toast.makeText(DetailActivity.this, "Không tìm thấy dữ liệu", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi khi đọc dữ liệu từ Firebase
                Toast.makeText(DetailActivity.this, "Lỗi khi đọc dữ liệu", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
    private void deleteImage(){
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Hiển thị dialog xác nhận xóa
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
                builder.setTitle("Xác nhận xóa");
                builder.setMessage("Bạn thực sự muốn xóa ảnh?");
                builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Xử lý xóa ảnh và dữ liệu tương ứng trên Firebase
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference storageReference = storage.getReferenceFromUrl(imageUrl);

                        // Xóa ảnh trên Firebase Storage
                        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                // Nếu xóa thành công, xóa dữ liệu trong Firebase Realtime Database
                                databaseReference.child(key).removeValue();
                                Toast.makeText(DetailActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Nếu xảy ra lỗi trong quá trình xóa ảnh
                                Toast.makeText(DetailActivity.this, "Xóa không thành công: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Người dùng chọn hủybỏ xóa
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
    private void editImage(){
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Chuyển sang Activity UpdateImageActivity để chỉnh sửa thông tin ảnh
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

    // Tải lại dữ liệu từ Firebase khi Activity trở lại hoạtđộng
    protected void onResume() {
        super.onResume();
        if (key != null) {
            loadDataFromFirebase();
        }
    }

}
