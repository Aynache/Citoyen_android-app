package com.example.projetsession;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReportListViewHolder extends RecyclerView.ViewHolder {


    public final ImageView icon;
    public final TextView text;

    public ReportListViewHolder(@NonNull View view) {
        super(view);
        this.icon = itemView.findViewById(R.id.mtrl_list_item_icon);
        this.text = itemView.findViewById(R.id.mtrl_list_item_text);
    }

    @NonNull
    public static ReportListViewHolder create(@NonNull ViewGroup parent) {
        return new ReportListViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.report_list, parent, false));
    }
}