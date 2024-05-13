package com.example.projetsession.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.projetsession.OnReportLongClickListener;
import com.example.projetsession.R;
import com.example.projetsession.models.Reports;
import java.util.ArrayList;
import java.util.List;

public class ReportsAdapter extends RecyclerView.Adapter<ReportsAdapter.ReportsViewHolder> {

    private List<Reports> reportsList;
    private OnReportLongClickListener longClickListener;

    // Single constructor that ensures reportsList is never null
    public ReportsAdapter(List<Reports> reportsList, OnReportLongClickListener longClickListener) {
        this.reportsList = reportsList != null ? reportsList : new ArrayList<>();
        this.longClickListener = longClickListener;
    }

    @NonNull
    @Override
    public ReportsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.reports_list, parent, false);
        return new ReportsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportsViewHolder holder, int position) {
        Reports report = reportsList.get(position);
        holder.nameTextView.setText(report.getName());
        holder.dateTextView.setText(report.getDate());
        holder.descriptionTextView.setText(report.getDescription());
        // Use Glide to load the image from a path
        if (report.getPhoto() != null && !report.getPhoto().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(report.getPhoto()) // Load from a file path, URL, or resource
                    .apply(new RequestOptions().placeholder(R.drawable.baseline_image_24).error(R.drawable.baseline_image_24)) // Optional: placeholders and error images
                    .into(holder.iconImageView);
        } else {
            // Set a default image or leave blank if no image path is available
            holder.iconImageView.setImageResource(R.drawable.baseline_image_24);
        }


        holder.itemView.setOnLongClickListener(v -> {
            if (longClickListener != null) {
                longClickListener.onReportLongClicked(report);
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return reportsList.size();
    }

    public void setReports(List<Reports> reportsList) {
        this.reportsList = reportsList != null ? reportsList : new ArrayList<>();
        notifyDataSetChanged();
    }

    static class ReportsViewHolder extends RecyclerView.ViewHolder {
        ImageView iconImageView;
        TextView nameTextView;
        TextView dateTextView;
        TextView descriptionTextView;

        ReportsViewHolder(View view) {
            super(view);
            iconImageView = view.findViewById(R.id.mtrl_list_item_icon);
            nameTextView = view.findViewById(R.id.mtrl_list_item_text);
            dateTextView = view.findViewById(R.id.mtrl_list_item_secondary_text);
            descriptionTextView = view.findViewById(R.id.mtrl_list_item_tertiary_text);
        }
    }
}
