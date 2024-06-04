package com.example.demo_searchviewrecyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder> {

    private List<LanguageData> mList;

    public LanguageAdapter(List<LanguageData> mList) {
        this.mList = mList;
    }

    public static class LanguageViewHolder extends RecyclerView.ViewHolder {
        ImageView logo;
        TextView titleTv;

        public LanguageViewHolder(@NonNull View itemView) {
            super(itemView);
            logo = itemView.findViewById(R.id.logoIv);
            titleTv = itemView.findViewById(R.id.titleTv);
        }
    }

    public void setFilteredList(List<LanguageData> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LanguageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.each_item, parent, false);
        return new LanguageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LanguageViewHolder holder, int position) {
        holder.logo.setImageResource(mList.get(position).getLogo());
        holder.titleTv.setText(mList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
