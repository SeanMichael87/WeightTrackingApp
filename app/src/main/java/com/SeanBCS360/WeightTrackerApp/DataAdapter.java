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
    private final OnDeleteButtonClickListener onDeleteButtonClickListener;
    private final OnEditButtonClickListener editButtonClickListener;

    public DataAdapter(List<DataModel> dataList, OnEditButtonClickListener editButtonClickListener,
                       OnDeleteButtonClickListener onDeleteButtonClickListener) {
        this.dataList = dataList;
        this.editButtonClickListener = editButtonClickListener;
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
    public interface OnEditButtonClickListener {
        void onEditButtonClick(int position);
    }
    public interface OnDeleteButtonClickListener {

        void onDeleteButtonClick(int position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView currentWeights;
        private final TextView associatedDates;
        private final ImageView editButton;
        private final ImageView delButton;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            associatedDates = itemView.findViewById(R.id.tvDate);
            currentWeights = itemView.findViewById(R.id.tvWeight);
            editButton = itemView.findViewById(R.id.editButton);
            delButton = itemView.findViewById(R.id.btnDelete);

            // Set click listeners for edit and delete actions (if needed)
            editButton.setOnClickListener(view-> {
                if (editButtonClickListener != null) {
                    editButtonClickListener.onEditButtonClick(getAdapterPosition());
                }
            });
            delButton.setOnClickListener(view -> {
                if (onDeleteButtonClickListener != null) {
                    onDeleteButtonClickListener.onDeleteButtonClick(getAdapterPosition());
                }
            });
        }

        public void bindData(DataModel data) {
            currentWeights.setText(data.getWeight());
            associatedDates.setText(data.getDate());
        }

    }
}

