package com.example.autoimageslider;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo ImageSlider
        ImageSlider imageSlider = findViewById(R.id.ImageSlide);

        // Tạo danh sách các SlideModel để thêm vào ImageSlider
        final List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/fir-slideimagewithfirebase.appspot.com/o/banner_image%2Fbanner_image_one?alt=media&token=7746e1f0-433d-419e-a254-efa2b72557ee", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/fir-slideimagewithfirebase.appspot.com/o/banner_image%2Fbanner_image_two?alt=media&token=98d0632d-ad3d-4161-b011-2251fa4662f8", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/fir-slideimagewithfirebase.appspot.com/o/banner_image%2Fbanner_image_three?alt=media&token=6fccfa77-6878-4646-8a58-a15fb029634b", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/fir-slideimagewithfirebase.appspot.com/o/banner_image%2Fbanner_image_four?alt=media&token=c088bbf3-347c-46a8-8b1e-0f25094d35fa", ScaleTypes.FIT));

        // Đặt danh sách các SlideModel cho ImageSlider và thiết lập kiểu hiển thị
        imageSlider.setImageList(slideModels, ScaleTypes.FIT);

        // Thiết lập sự kiện khi người dùng nhấp vào từng ảnh
        imageSlider.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemSelected(int i) {
                // Nếu nhấp vào ảnh đầu tiên, mở trang web
                if (i == 0) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://sv.haui.edu.vn/register/"));
                    startActivity(intent);
                } else {
                    // Nếu nhấp vào các ảnh khác, hiển thị Toast "Thành công"
                    Toast.makeText(MainActivity.this, "Thành công", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
