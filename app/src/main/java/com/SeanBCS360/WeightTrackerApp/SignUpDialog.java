package com.SeanBCS360.WeightTrackerApp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link } factory method to
 * create an instance of this fragment.
 */
public class SignUpDialog extends DialogFragment {

    ImageView calenderIcon;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_signup, null);

        builder.setView(v)
                .setPositiveButton("Sign Up", (dialog, id) -> {
                    // Accept terms
                })
                .setNegativeButton("Quit", (dialog, id) -> {
                    // User cancelled the dialog
                });
        calenderIcon = v.findViewById(R.id.calendar);
        calenderIcon.setOnClickListener(view -> showTimePickerDialog());
        // Create the AlertDialog object and return it
        return builder.create();
    }
    public void showTimePickerDialog() {
        FragmentManager manager = getParentFragmentManager();
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(manager, "DatePicker");
    }
}