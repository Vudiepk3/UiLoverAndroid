package com.example.demo_slideimagewithfirebase.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.demo_slideimagewithfirebase.DetailActivity;
import com.example.demo_slideimagewithfirebase.R;
import com.example.demo_slideimagewithfirebase.model.ImageModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

// Adapter cho RecyclerView hiển thị danh sách hình ảnh
public class ImageAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private Context context; // Biến lưu trữ ngữ cảnh
    private List<ImageModel> dataList; // Danh sách dữ liệu hình ảnh
    private ImageModel model; // Model hình ảnh
    private List<String> keys = new ArrayList<>(); // Danh sách các khóa node trên Firebase

    // Constructor nhận vào ngữ cảnh và danh sách dữ liệu
    public ImageAdapter(Context context, List<ImageModel> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    // Tạo ViewHolder và gán layout cho các item trong RecyclerView
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new MyViewHolder(view);
    }

    // Gán dữ liệu cho các view trong ViewHolder
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // Sử dụng Glide để tải hình ảnh từ URL
        Glide.with(context).load(dataList.get(position).getUrlImage())
                .into(holder.recImage);
        // Gán tên và ghi chú cho hình ảnh
        holder.recNameImage.setText(dataList.get(position).getNameImgae());
        holder.recNoteImage.setText(dataList.get(position).getNoteImage());
        holder.recLinkWeb.setText(dataList.get(position).getLinkWebsite());
        // Xử lý sự kiện click vào thẻ
        holder.recCard.setOnClickListener(v -> {
            // Tạo Intent để mở DetailActivity
            Intent intent = new Intent(context, DetailActivity.class);
            // Truyền dữ liệu qua Intent
            intent.putExtra("UrlImage", dataList.get(holder.getAdapterPosition()).getUrlImage());
            intent.putExtra("NameImage", dataList.get(holder.getAdapterPosition()).getNameImgae());
            intent.putExtra("NoteImage", dataList.get(holder.getAdapterPosition()).getNoteImage());

            // Bắt đầu Activity mới
            context.startActivity(intent);
        });


    }


    // Phương thức lấy reference của node Firebase tại vị trí chỉ định


    // Trả về số lượng item trong danh sách dữ liệu
    @Override
    public int getItemCount() {
        return dataList.size();
    }

    // Phương thức cập nhật danh sách dữ liệu tìm kiếm
    public void searchDataList(List<ImageModel> searchList) {
        dataList = searchList;
        notifyDataSetChanged();
    }
}

// Lớp ViewHolder chứa các view của item
class MyViewHolder extends RecyclerView.ViewHolder {

    ImageView recImage; // Hình ảnh
    TextView recNameImage, recNoteImage,recLinkWeb; // Tên và ghi chú hình ảnh
    CardView recCard; // Thẻ chứa các view

    // Constructor gán các view cho ViewHolder
    public MyViewHolder(View itemView) {
        super(itemView);
        recImage = itemView.findViewById(R.id.recImage);
        recNameImage = itemView.findViewById(R.id.recNameImage);
        recNoteImage = itemView.findViewById(R.id.recNoteImage);
        recLinkWeb = itemView.findViewById(R.id.recLinkWeb);
        recCard = itemView.findViewById(R.id.recCard);
    }
}
