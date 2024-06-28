package com.example.demo_slideimagewithfirebase;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.demo_slideimagewithfirebase.databinding.ActivityDetailBinding;

// Lớp DetailActivity kế thừa AppCompatActivity
public class DetailActivity extends AppCompatActivity {
    private String imageUrl = ""; // Biến lưu trữ URL của hình ảnh
    private ActivityDetailBinding binding; // Đối tượng binding cho hoạt động

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Khởi tạo binding từ layout
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        // Đặt nội dung của activity là binding root
        setContentView(binding.getRoot());

        // Lấy dữ liệu từ Intent
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            // Gán dữ liệu tên hình ảnh vào TextView
            binding.detailNameImage.setText(bundle.getString("NameImage"));
            // Gán dữ liệu ghi chú hình ảnh vào TextView
            binding.detailNoteImage.setText(bundle.getString("NoteImage"));
            // Lấy URL của hình ảnh từ bundle
            imageUrl = bundle.getString("UrlImage");
            // Sử dụng Glide để tải hình ảnh từ URL vào ImageView
            Glide.with(this).load(imageUrl).into(binding.detailImage);
        }
    }
}
