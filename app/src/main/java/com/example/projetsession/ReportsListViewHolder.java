package com.example.projetsession;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class ReportsListViewHolder extends ReportListViewHolder{

    public final TextView secondary;

    public ReportsListViewHolder(@NonNull View view) {
        super(view);
        this.secondary = itemView.findViewById(R.id.mtrl_list_item_secondary_text);
    }

    @NonNull
    public static ReportsListViewHolder create(@NonNull ViewGroup parent) {
        return new ReportsListViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.reports_list, parent, false));
    }
}
