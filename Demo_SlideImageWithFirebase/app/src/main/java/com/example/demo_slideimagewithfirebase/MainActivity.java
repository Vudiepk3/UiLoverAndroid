package com.example.demo_slideimagewithfirebase;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.widget.Button;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import com.example.demo_slideimagewithfirebase.model.ImageModel;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

// Lớp MainActivity kế thừa AppCompatActivity và triển khai NavigationView.OnNavigationItemSelectedListener
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DatabaseReference databaseReference; // Tham chiếu cơ sở dữ liệu Firebase
    private ValueEventListener eventListener; // Listener để nhận dữ liệu từ Firebase

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Đặt layout cho activity
        loadImageSlideWithFireBase(); // Gọi phương thức để tải hình ảnh từ Firebase và hiển thị
        upLoadImage(); // Gọi phương thức để thiết lập sự kiện click cho các nút
    }

    // Phương thức tải hình ảnh từ Firebase và hiển thị trong ImageSlider
    private void loadImageSlideWithFireBase() {
        ImageSlider imageSlider1 = findViewById(R.id.ImageSlide1); // Lấy đối tượng ImageSlider từ layout
        ArrayList<SlideModel> slideModels = new ArrayList<>(); // Tạo danh sách để lưu trữ các SlideModel

        // Thêm các SlideModel vào danh sách (các hình ảnh từ Firebase)
        slideModels.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/fir-slideimagewithfirebase.appspot.com/o/banner_image%2Fbanner_image_one?alt=media&token=7746e1f0-433d-419e-a254-efa2b72557ee", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/fir-slideimagewithfirebase.appspot.com/o/banner_image%2Fbanner_image_two?alt=media&token=98d0632d-ad3d-4161-b011-2251fa4662f8", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/fir-slideimagewithfirebase.appspot.com/o/banner_image%2Fbanner_image_three?alt=media&token=6fccfa77-6878-4646-8a58-a15fb029634b", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/fir-slideimagewithfirebase.appspot.com/o/banner_image%2Fbanner_image_four?alt=media&token=c088bbf3-347c-46a8-8b1e-0f25094d35fa", ScaleTypes.FIT));

        // Đặt danh sách hình ảnh vào ImageSlider và thiết lập kiểu hiển thị
        imageSlider1.setImageList(slideModels, ScaleTypes.FIT);

        // Thiết lập sự kiện click cho từng hình ảnh trong ImageSlider
        imageSlider1.setItemClickListener(i -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://sv.haui.edu.vn/register/"));
            startActivity(intent); // Mở trang web khi người dùng nhấp vào hình ảnh
        });


        ImageSlider imageSlider2 = findViewById(R.id.ImageSlide2); // Lấy đối tượng ImageSlider thứ hai từ layout
        ArrayList<SlideModel> slideModels2 = new ArrayList<>(); // Tạo danh sách để lưu trữ các SlideModel thứ hai
        databaseReference = FirebaseDatabase.getInstance().getReference("SlideImage");
        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                slideModels2.clear(); // Xóa dữ liệu cũ
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    ImageModel imageModel = itemSnapshot.getValue(ImageModel.class);
                    if (imageModel != null && imageModel.getUrlImage() != null) {
                        slideModels2.add(new SlideModel(imageModel.getUrlImage(), ScaleTypes.FIT)); // Thêm dữ liệu mới
                    }
                }
                imageSlider2.setImageList(slideModels2, ScaleTypes.FIT); // Cập nhật danh sách hình ảnh trong ImageSlider
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Xử lý lỗi nếu cần thiết
            }
        });
    }

    // Phương thức thiết lập sự kiện click cho các nút
    private void upLoadImage(){
        Button imageSlide1 = findViewById(R.id.imageSlide1); // Lấy đối tượng Button từ layout
        imageSlide1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Khi nhấn vào nút imageSlide1, mở ImageActivity
                Intent intent = new Intent(MainActivity.this, ImageActivity.class);
                startActivity(intent);
            }
        });

        Button imageSlide2 = findViewById(R.id.imageSlide2); // Lấy đối tượng Button từ layout
        imageSlide2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Khi nhấn vào nút imageSlide2, mở ImageLinkNoteActivity
                Intent intent = new Intent(MainActivity.this, ImageLinkNoteActivity.class);
                startActivity(intent);
            }
        });
    }

    // Phương thức xử lý sự kiện khi một mục trong NavigationView được chọn
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
