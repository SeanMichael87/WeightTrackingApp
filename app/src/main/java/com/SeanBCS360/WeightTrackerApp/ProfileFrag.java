package com.SeanBCS360.WeightTrackerApp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class ProfileFrag extends Fragment {
    
    EditText changeUsername;
    EditText changePassword;
    EditText changeGoalWeight;
    Button  delete;
    Button  update;
    DBHandler db;

    public ProfileFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         View v = inflater.inflate(R.layout.fragment_dash, container, false);

        db = new DBHandler(getActivity());
        changeUsername = v.findViewById(R.id.profile_username);
        changePassword = v.findViewById(R.id.profile_password);
        changeGoalWeight = v.findViewById(R.id.profile_goal_weight);
        delete = v.findViewById(R.id.delete_profile);
        update = v.findViewById(R.id.update_profile);     

        UserSessionManager manager = new UserSessionManager(requireActivity());
        int userID = manager.getUserId();

        if (userID != -1) {
                

        } else {
            Toast.makeText(getActivity(), "Id not found", Toast.LENGTH_LONG).show();
        }

        return v;
    }
}
