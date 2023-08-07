package com.SeanBCS360.WeightTrackerApp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private List<DataModel> dataList;
    private OnDeleteButtonClickListener onDeleteButtonClickListener;

    public DataAdapter(List<DataModel> dataList, OnDeleteButtonClickListener onDeleteButtonClickListener) {
        this.dataList = dataList;
        this.onDeleteButtonClickListener = onDeleteButtonClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_weight, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataModel data = dataList.get(position);
        holder.bindData(data);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void updateDataList(List<DataModel> newDataList) {
        dataList = newDataList;
        notifyDataSetChanged();
    }
    public interface OnDeleteButtonClickListener {
        void onDeleteButtonClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView currentWeights;
        private TextView associatedDates;
        private ImageView delButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            associatedDates = itemView.findViewById(R.id.tvDate);
            currentWeights = itemView.findViewById(R.id.tvWeight);
            delButton = itemView.findViewById(R.id.btnDelete);

            // Set click listeners for edit and delete actions (if needed)
            delButton.setOnClickListener(view -> {
                if (onDeleteButtonClickListener != null) {
                    onDeleteButtonClickListener.onDeleteButtonClick(getAdapterPosition());
                }
                    }
            );
            // itemView.findViewById(R.id.buttonEdit).setOnClickListener(...);
        }

        public void bindData(DataModel data) {
            currentWeights.setText(data.getWeight());
            associatedDates.setText(data.getDate());
        }

    }
}

