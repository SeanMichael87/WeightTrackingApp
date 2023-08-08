package com.SeanBCS360.WeightTrackerApp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

public class PermissionsDialog extends DialogFragment {
    private static final int REQUEST_SMS_PERMISSION = 123;

    UserSessionManager manager;
    DBHandler db;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.dialog_sms, null))
               .setPositiveButton(R.string.accept, (dialog, id) -> {
                   db = new DBHandler(requireActivity());
                   manager = new UserSessionManager(requireActivity());
                   int userId = manager.getUserId();
                   manager.setIsFirstLogin(userId, false);
                   if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                       ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.SEND_SMS}, REQUEST_SMS_PERMISSION);
                   } else {
                       // Permission already granted, you can proceed with SMS-related operations
                       db.updateSMSState(userId, "true");
                   }


               })
               .setNegativeButton(R.string.deny, (dialog, id) -> {
                   // User cancelled the dialog
               });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
