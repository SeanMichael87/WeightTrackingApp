package com.SeanBCS360.WeightTrackerApp;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class HistoryFrag extends Fragment implements DataAdapter.OnDeleteButtonClickListener,
DataAdapter.OnEditButtonClickListener{

    private RecyclerView recyclerView;
    private DataAdapter dataAdapter;
    private List<DataModel> dataList;
    DBHandler db;

    public HistoryFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_history, container, false);

        recyclerView = v.findViewById(R.id.recyclerview);

        UserSessionManager manager = new UserSessionManager(requireActivity());
        int userId = manager.getUserId();
        db = new DBHandler(getActivity());

        dataList = db.getAllData(userId);
        dataAdapter = new DataAdapter(dataList, (DataAdapter.OnEditButtonClickListener) this,
                this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(dataAdapter);

        // Update the adapter with the new data
        dataAdapter.updateDataList(dataList);
        // Inflate the layout for this fragment
        return v;
    }
    @Override
    public void onDeleteButtonClick(int position) {
        DataModel dataToDelete = dataList.get(position);
        int idToDelete = dataToDelete.getId();

        // Delete data from the database
        boolean deletedRows = db.deleteRowWeight(idToDelete);

        if (deletedRows) {
            dataList.remove(position);
            dataAdapter.updateDataList(dataList); // Update the adapter
        }
    }
    @Override
    public void onEditButtonClick(int position) {
        showUpdateWeightDialog(position);
    }

    private void showUpdateWeightDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.edit_weight_dialog, null);
        builder.setView(dialogView);

        EditText editTextWeight = dialogView.findViewById(R.id.edit_weight);
        Button buttonUpdate = dialogView.findViewById(R.id.button_update);

        AlertDialog dialog = builder.create();

        buttonUpdate.setOnClickListener(view -> {
            String newWeight = editTextWeight.getText().toString().trim();
            DataModel id = dataList.get(position);
            int idToEdit = id.getId();

            db = new DBHandler(requireContext());

            if (!newWeight.isEmpty()) {
                db.updateWeight(idToEdit, newWeight);
                id.setWeight(newWeight);
                dataAdapter.notifyItemChanged(position);
                dialog.dismiss();
            } else {
                editTextWeight.setError("Please enter a valid weight");
            }
        });

        dialog.show();
    }

}
