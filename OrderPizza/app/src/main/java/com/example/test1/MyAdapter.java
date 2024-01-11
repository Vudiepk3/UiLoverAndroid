package com.example.test1;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends ArrayAdapter {

    private Activity context;
    private int layoutID;
    private ArrayList<Pizza> list=null;

    public MyAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
        this.context= (Activity) context;
        this.layoutID=resource;
        this.list= (ArrayList<Pizza>) objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        convertView = inflater.inflate(layoutID,null);
        if(position>=0 && list.size()>0){
            TextView txtSTT = convertView.findViewById(R.id.txtTitle);
            TextView txtND = convertView.findViewById(R.id.txtND);
            txtSTT.setText("Pizza "+position);
            txtND.setText(list.get(position).toString()+"");
        }
        return convertView;
    }
}
