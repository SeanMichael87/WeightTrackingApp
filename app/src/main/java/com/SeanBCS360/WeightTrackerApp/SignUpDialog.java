package com.SeanBCS360.WeightTrackerApp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link } factory method to
 * create an instance of this fragment.
 */
public class SignUpDialog extends DialogFragment {

    ImageView calenderIcon;
    private DBHandler db;
    TextView calText;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_signup, null);

        calenderIcon = v.findViewById(R.id.calendar);
        EditText username = v.findViewById(R.id.username);
        EditText password = v.findViewById(R.id.password);
        EditText currentWeight = v.findViewById(R.id.curr_weight);
        EditText goalWeight = v.findViewById(R.id.goal_weight);
        calText = v.findViewById(R.id.calendarText);

        db = DBHandler.getInstance(getActivity());

        calenderIcon.setOnClickListener(view -> {
//            showTimePickerDialog();
            Calendar mCalendar = Calendar.getInstance();
            int year = mCalendar.get(Calendar.YEAR);
            int month = mCalendar.get(Calendar.MONTH);
            int day = mCalendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    getActivity(), (datePicker, year1, monthOfYear, dayOfMonth) -> {
                        mCalendar.set(Calendar.YEAR, year1);
                        mCalendar.set(Calendar.MONTH, monthOfYear);
                        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        String goalDate = DateFormat.getDateInstance(DateFormat.FULL).format(mCalendar.getTime());
                        calText.setText(goalDate);
                    },
               year, month, day);

            datePickerDialog.show();
        });

        builder.setView(v)
                .setPositiveButton("Sign Up", (dialog, id) -> {
                    String userName = username.getText().toString();
                    String passWord = password.getText().toString();

                    SimpleDateFormat sdf = new SimpleDateFormat("dd-mm-yyyy");
                    String todayDate = sdf.format(new Date());

                    float currWeight = Float.parseFloat(currentWeight.getText().toString());
                    float gWeight = Float.parseFloat(goalWeight.getText().toString());

                    String goalDate = calText.getText().toString();

                    db.addUser(userName, passWord, currWeight, todayDate, gWeight, goalDate);
                })
                .setNegativeButton("Quit", (dialog, id) -> {
                    // User cancelled the dialog
                });

        // Create the AlertDialog object and return it
        return builder.create();
    }
    public void showTimePickerDialog() {
        FragmentManager manager = getChildFragmentManager();
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(manager, "DatePicker");
    }

}
