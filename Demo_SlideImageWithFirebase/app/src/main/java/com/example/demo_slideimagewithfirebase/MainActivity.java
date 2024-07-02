package com.example.demo_slideimagewithfirebase;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.Button;
import android.widget.Toast;

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
import java.util.List;

// Lớp MainActivity kế thừa AppCompatActivity và triển khai NavigationView.OnNavigationItemSelectedListener
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DatabaseReference databaseReference; // Tham chiếu cơ sở dữ liệu Firebase
    private ValueEventListener eventListener; // Listener để nhận dữ liệu từ Firebase

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Đặt layout cho activity
        loadImageSlideWithFireBase(); // Gọi phương thức để tải hình ảnh từ Firebase và hiển thị
        manageUpLoadImage(); // Gọi phương thức để thiết lập sự kiện click cho các nút
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


        ImageSlider imageSlider2 = findViewById(R.id.ImageSlide2);
        ArrayList<SlideModel> slideModels2 = new ArrayList<>();
        List<String> linkWebsites = new ArrayList<>(); // Danh sách lưu trữ các link website

        databaseReference = FirebaseDatabase.getInstance().getReference("SlideImage");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                slideModels2.clear(); // Xóa danh sách hình ảnh cũ
                linkWebsites.clear(); // Xóa danh sách link website cũ

                // Duyệt qua từng mục trong dữ liệu Firebase
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    ImageModel imageModel = itemSnapshot.getValue(ImageModel.class);
                    if (imageModel != null && imageModel.getUrlImage() != null) {
                        slideModels2.add(new SlideModel(imageModel.getUrlImage(), ScaleTypes.FIT)); // Thêm hình ảnh vào danh sách slide
                        linkWebsites.add(imageModel.getLinkWeb()); // Thêm link website vào danh sách
                    }
                }

                imageSlider2.setImageList(slideModels2, ScaleTypes.FIT); // Cập nhật danh sách hình ảnh

                imageSlider2.setItemClickListener(i -> {
                    if (i >= 0 && i < linkWebsites.size()) {
                        String linkWebsite = linkWebsites.get(i);
                        if (linkWebsite != null && !linkWebsite.isEmpty() && !linkWebsite.equals("No Link Website")) {
                            try {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkWebsite));startActivity(intent); // Mở trang web bằng Intent
                            } catch (ActivityNotFoundException e) {
                                Toast.makeText(MainActivity.this, "Không thể mở đường dẫn web.", Toast.LENGTH_SHORT).show(); // Thông báo nếu không thể mở đường dẫn
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Không có đường dẫn web.", Toast.LENGTH_SHORT).show(); // Thông báo nếu không có đường dẫn web
                        }
                    }
                });

               // Đóng ProgressDialog sau khi tải xong dữ liệu
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Failed to load data.", Toast.LENGTH_SHORT).show(); // Thông báo lỗi tải dữ liệu
            }
        });
    }

    // Phương thức thiết lập sự kiện click cho các nút
    private void manageUpLoadImage(){
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
