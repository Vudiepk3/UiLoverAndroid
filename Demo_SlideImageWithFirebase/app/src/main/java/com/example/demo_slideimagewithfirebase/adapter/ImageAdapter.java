package com.example.demo_slideimagewithfirebase.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.demo_slideimagewithfirebase.DetailActivity;
import com.example.demo_slideimagewithfirebase.R;
import com.example.demo_slideimagewithfirebase.model.ImageModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ImageAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private Context context;
    private List<ImageModel> dataList;

    public ImageAdapter(Context context, List<ImageModel> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context).load(dataList.get(position).getUrlImage()).into(holder.recImage);
        holder.recNameImage.setText(dataList.get(position).getNameImage());
        holder.recLinkWeb.setText(dataList.get(position).getLinkWeb());
        holder.recNoteImage.setText(dataList.get(position).getNoteImage());


        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("UrlImage", dataList.get(holder.getAdapterPosition()).getUrlImage());
                intent.putExtra("NameImage", dataList.get(holder.getAdapterPosition()).getNameImage());
                intent.putExtra("LinkWeb", dataList.get(holder.getAdapterPosition()).getLinkWeb());
                intent.putExtra("NoteImage", dataList.get(holder.getAdapterPosition()).getNoteImage());
                intent.putExtra("Key", dataList.get(holder.getAdapterPosition()).getKey());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void searchDataList(ArrayList<ImageModel> searchList){
        dataList = searchList;
        notifyDataSetChanged();
    }
}

class MyViewHolder extends RecyclerView.ViewHolder{

    ImageView recImage;
    TextView recNameImage, recLinkWeb, recNoteImage;
    CardView recCard;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        recImage = itemView.findViewById(R.id.recImage);
        recCard = itemView.findViewById(R.id.recCard);
        recNoteImage = itemView.findViewById(R.id.recNoteImage);
        recNameImage = itemView.findViewById(R.id.recNameImage);
        recLinkWeb = itemView.findViewById(R.id.recLinkWeb);
    }
}