package com.example.pruebaappredsocial;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AlertAdapter extends RecyclerView.Adapter<AlertAdapter.AlertViewHolder> {
    private List<Alert> alertList;

    public AlertAdapter(List<Alert> alertList) {
        this.alertList = alertList;
    }

    @NonNull
    @Override
    public AlertViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alert, parent, false);
        return new AlertViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlertViewHolder holder, int position) {
        Alert alert = alertList.get(position);
        holder.tvAlertTitle.setText(alert.getTitle());
        holder.tvAlertReason.setText(alert.getReason());
        holder.tvAlertDate.setText(alert.getDate());

        // Aquí puedes agregar diferentes iconos para distintos tipos de alertas
        switch (alert.getType()) {
            case "MINIMA":
                holder.ivAlertTypeIcon.setImageResource(R.drawable.ic_alert_blue);
                break;
            case "MEDIA":
                holder.ivAlertTypeIcon.setImageResource(R.drawable.ic_alert_yellow  );
                break;
            case "GRAVE":
                holder.ivAlertTypeIcon.setImageResource(R.drawable.ic_alert_red);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return alertList.size();
    }

    public static class AlertViewHolder extends RecyclerView.ViewHolder {
        TextView tvAlertTitle, tvAlertReason, tvAlertDate;
        ImageView ivAlertTypeIcon;

        public AlertViewHolder(View itemView) {
            super(itemView);
            tvAlertTitle = itemView.findViewById(R.id.tvAlertTitle);
            tvAlertReason = itemView.findViewById(R.id.tvAlertReason);
            tvAlertDate = itemView.findViewById(R.id.tvAlertDate);
            ivAlertTypeIcon = itemView.findViewById(R.id.ivAlertTypeIcon);
        }
    }
}

