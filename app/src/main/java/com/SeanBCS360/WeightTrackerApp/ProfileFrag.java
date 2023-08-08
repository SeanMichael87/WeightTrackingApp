package com.SeanBCS360.WeightTrackerApp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.SwitchCompat;
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
    SwitchCompat smsSwitch;
    Button delete;
    Button update;
    float newGoalWeight;
    DBHandler db;

    public ProfileFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        db = new DBHandler(getActivity());
        changeUsername = v.findViewById(R.id.profile_username);
        changePassword = v.findViewById(R.id.profile_password);
        changeGoalWeight = v.findViewById(R.id.profile_goal_weight);
        smsSwitch = v.findViewById(R.id.sms_switch);
        delete = v.findViewById(R.id.delete_profile);
        update = v.findViewById(R.id.update_profile);

        UserSessionManager manager = new UserSessionManager(requireActivity());
        int userId = manager.getUserId();

        String newUsername = changeUsername.getText().toString();
        String newPassword = changePassword.getText().toString();

        if (!changeGoalWeight.getText().toString().isEmpty()) {
            newGoalWeight = Float.parseFloat(changeGoalWeight.getText().toString());
        } else {
            newGoalWeight = 0f;
        }

        smsSwitch.setChecked(db.getSMSState(userId));

        if (userId != -1) {
            smsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    db.updateSMSState(userId, "true");
                } else {
                    db.updateSMSState(userId, "false");
                }
            });
            update.setOnClickListener(view -> {

                if (db.updateProfile(userId, newUsername, newPassword, newGoalWeight)) {
                    Toast.makeText(getActivity(), "Profile Updated", Toast.LENGTH_LONG).show();
                }

            });

            delete.setOnClickListener(view -> {
                if (db.deleteProfile(userId)) {
                    Intent i = new Intent(requireContext(), LoginActivity.class);
                    startActivity(i);
                }

            });

        } else {
            Toast.makeText(getActivity(), "Id not found", Toast.LENGTH_LONG).show();
        }

        return v;
    }
}
