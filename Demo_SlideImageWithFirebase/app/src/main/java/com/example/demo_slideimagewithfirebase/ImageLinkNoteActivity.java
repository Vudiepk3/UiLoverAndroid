package com.example.demo_slideimagewithfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.demo_slideimagewithfirebase.adapter.ImageAdapter;
import com.example.demo_slideimagewithfirebase.databinding.ActivityImageLinkNoteBinding;
import com.example.demo_slideimagewithfirebase.model.ImageModel;
import com.google.firebase.database.*;
import java.util.ArrayList;
import java.util.Locale;

// Lớp ImageLinkNoteActivity kế thừa AppCompatActivity
public class ImageLinkNoteActivity extends AppCompatActivity {
    private DatabaseReference databaseReference; // Tham chiếu đến Firebase Database
    private ValueEventListener eventListener; // Lắng nghe sự kiện thay đổi dữ liệu
    private ArrayList<ImageModel> dataList; // Danh sách dữ liệu hình ảnh
    private ImageAdapter adapter; // Adapter cho RecyclerView
    private ActivityImageLinkNoteBinding binding; // Đối tượng binding cho hoạt động

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Khởi tạo binding từ layout
        binding = ActivityImageLinkNoteBinding.inflate(getLayoutInflater());
        // Đặt nội dung của activity là binding root
        setContentView(binding.getRoot());

        // Thiết lập GridLayoutManager cho RecyclerView
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        binding.recyclerView.setLayoutManager(gridLayoutManager);
        // Xóa focus từ thanh tìm kiếm
        binding.search.clearFocus();

        // Tạo và hiển thị dialog đang tải dữ liệu
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        // Khởi tạo danh sách dữ liệu và adapter
        dataList = new ArrayList<>();
        adapter = new ImageAdapter(this, dataList);
        binding.recyclerView.setAdapter(adapter);
        databaseReference = FirebaseDatabase.getInstance().getReference("SlideImage");
        dialog.show();

        // Lắng nghe sự kiện thay đổi dữ liệu từ Firebase
        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                dataList.clear(); // Xóa dữ liệu cũ
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    ImageModel ImageModel = itemSnapshot.getValue(ImageModel.class);
                    if (ImageModel != null) {
                        dataList.add(ImageModel); // Thêm dữ liệu mới
                    }
                }
                adapter.notifyDataSetChanged(); // Thông báo dữ liệu thay đổi
                dialog.dismiss(); // Ẩn dialog
            }

            @Override
            public void onCancelled(DatabaseError error) {
                dialog.dismiss(); // Ẩn dialog nếu có lỗi
            }
        });

        // Lấy số lượng ảnh hiện có trong cơ sở dữ liệu
        DatabaseReference getCountImage = FirebaseDatabase.getInstance().getReference("SlideImage");
        getCountImage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                int count = (int) snapshot.getChildrenCount();
                binding.txtNumberDocument.setText("Số Ảnh Hiện Có : " + count);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Không làm gì khi có lỗi
            }
        });

        // Xử lý sự kiện khi nhấn nút FAB (nút thêm ảnh mới)
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ImageLinkNoteActivity.this, UploadActivity.class);
                startActivity(intent);
            }
        });

        // Xử lý sự kiện tìm kiếm
        binding.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText); // Gọi phương thức tìm kiếm
                return true;
            }
        });
    }

    // Phương thức tìm kiếm trong danh sách dữ liệu
    private void searchList(String text) {
        ArrayList<ImageModel> searchList = new ArrayList<>();
        for (ImageModel ImageModel : dataList) {
            if (ImageModel.getNameImgae() != null && ImageModel.getNameImgae().toLowerCase(Locale.getDefault()).contains(text.toLowerCase(Locale.getDefault()))) {
                searchList.add(ImageModel); // Thêm các mục tìm kiếm phù hợp vào danh sách
            }
        }
        adapter.searchDataList(searchList); // Cập nhật danh sách hiển thị trong adapter
    }
}
