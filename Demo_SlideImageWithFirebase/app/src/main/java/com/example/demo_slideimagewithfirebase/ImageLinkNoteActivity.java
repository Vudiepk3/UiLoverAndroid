package com.example.demo_slideimagewithfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.demo_slideimagewithfirebase.adapter.ImageAdapter;
import com.example.demo_slideimagewithfirebase.model.ImageModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ImageLinkNoteActivity extends AppCompatActivity {
    FloatingActionButton fab;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    RecyclerView recyclerView;
    List<ImageModel> dataList;
    ImageAdapter adapter;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_link_note);

        // Ánh xạ các thành phần giao diện
        recyclerView = findViewById(R.id.recyclerView);
        fab = findViewById(R.id.fab);
        searchView = findViewById(R.id.search);
        searchView.clearFocus();

        // Thiết lập RecyclerView và GridLayoutManager
        GridLayoutManager gridLayoutManager = new GridLayoutManager(ImageLinkNoteActivity.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        // Hiển thị dialog tiến trình khi đang tải dữ liệu
        AlertDialog.Builder builder = new AlertDialog.Builder(ImageLinkNoteActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        dataList = new ArrayList<>();

        // Khởi tạo adapter và set vào RecyclerView
        adapter = new ImageAdapter(ImageLinkNoteActivity.this, dataList);
        recyclerView.setAdapter(adapter);

        // Hiển thị số lượng ảnh hiện có từ Firebase Realtime Database
        TextView txtNumberImage = findViewById(R.id.txtNumberImage);
        DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference().child("SlideImage");
        categoryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long count = snapshot.getChildrenCount();
                txtNumberImage.setText("Số Ảnh Hiện Có: " + count);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý khi lỗi
            }
        });

        // Lấy dữ liệu từ Firebase Realtime Database và cập nhật vào RecyclerView
        databaseReference = FirebaseDatabase.getInstance().getReference("SlideImage");
        dialog.show();
        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                for (DataSnapshot itemSnapshot: snapshot.getChildren()){
                    ImageModel dataClass = itemSnapshot.getValue(ImageModel.class);
                    dataClass.setKey(itemSnapshot.getKey());
                    dataList.add(dataClass);
                    dialog.dismiss();
                }
                adapter.notifyDataSetChanged();
                dialog.dismiss(); // Đóng dialog sau khi tải xong dữ liệu
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss(); // Đóng dialog khi có lỗi xảy ra
            }
        });

        // Thiết lập SearchView để tìm kiếm ảnh theo tên
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText); // Gọi phương thức để tìm kiếm
                return true;
            }
        });

        // Thiết lập sự kiện click cho nút thêm mới ảnh
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ImageLinkNoteActivity.this, UploadActivity.class);
                startActivity(intent);
            }
        });

    }

    // Phương thức tìm kiếm trong danh sách ảnh
    public void searchList(String text){
        ArrayList<ImageModel> searchList = new ArrayList<>();
        for (ImageModel dataClass: dataList){
            if (dataClass.getNameImage().toLowerCase().contains(text.toLowerCase())){
                searchList.add(dataClass);
            }
        }
        adapter.searchDataList(searchList); // Cập nhật danh sách ảnh sau khi tìm kiếm
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseReference.removeEventListener(eventListener); // Hủy đăng ký lắng nghe sự kiện khi hủy activity
    }

}
