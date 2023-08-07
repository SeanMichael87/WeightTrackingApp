package com.SeanBCS360.WeightTrackerApp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class HistoryFrag extends Fragment implements DataAdapter.OnDeleteButtonClickListener{

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
        dataAdapter = new DataAdapter(dataList, this);

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
}
