package com.SeanBCS360.WeightTrackerApp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class ProfileFrag extends Fragment {

private static final int REQUEST_SMS_PERMISSION = 123;
    EditText changeUsername;
    EditText changePassword;
    EditText changePhoneNum;
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
        changePhoneNum = v.findViewById(R.id.profile_phone);
        changeGoalWeight = v.findViewById(R.id.profile_goal_weight);
        smsSwitch = v.findViewById(R.id.sms_switch);
        delete = v.findViewById(R.id.delete_profile);
        update = v.findViewById(R.id.update_profile);

        UserSessionManager manager = new UserSessionManager(requireActivity());
        int userId = manager.getUserId();


        smsSwitch.setChecked(db.getSMSState(userId));

        if (userId != -1) {
            smsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    // Request SMS permission
                    if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.SEND_SMS}, REQUEST_SMS_PERMISSION);
                    } else {
                        // Permission already granted, you can proceed with SMS-related operations
                        db.updateSMSState(userId, "true");
                    }
                } else {
                    db.updateSMSState(userId, "false");
                }
            });
            update.setOnClickListener(view -> {
                String newUsername = changeUsername.getText().toString();
                String newPassword = changePassword.getText().toString();
                String newPhoneNum = changePhoneNum.getText().toString();

                if(!changeGoalWeight.getText().toString().isEmpty()) {
                    newGoalWeight = Float.parseFloat(changeGoalWeight.getText().toString());
                } else {
                    newGoalWeight = 0f;
                }

                if (db.updateProfile(userId, newUsername, newPassword, newPhoneNum, newGoalWeight)) {
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
