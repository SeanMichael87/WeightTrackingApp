package com.SeanBCS360.WeightTrackerApp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link } factory method to
 * create an instance of this fragment.
 */
public class SignUpDialog extends DialogFragment {

    private DBHandler db;
    private EditText username;
    private EditText password;
    private EditText phoneNumber;
    private EditText currentWeight;
    private EditText goalWeight;
    private TextView calDate;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_signup, null);

        username = v.findViewById(R.id.username);
        password = v.findViewById(R.id.password);
        phoneNumber = v.findViewById(R.id.phone_number);
        currentWeight = v.findViewById(R.id.curr_weight);
        goalWeight = v.findViewById(R.id.goal_weight);
        calDate = v.findViewById(R.id.cal_date);

        db = new DBHandler(getActivity());

        calDate.setOnClickListener(view -> {
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
                        calDate.setText(goalDate);
                    },
               year, month, day);

            datePickerDialog.show();
        });

        builder.setView(v)
                .setPositiveButton("Sign Up", null) // Set click listener to null initially
                .setNegativeButton("Quit", (dialog, id) -> {
                    CharSequence text = "Come Back Soon!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast.makeText(getActivity(), text, duration).show();

                    // Dismiss the dialog
                    dialog.dismiss();
                });

        // Create the AlertDialog object and return it
        AlertDialog alertDialog = builder.create();

        // Had issue with original button not validating because it exits the lambda
        // Set the positive button's click listener after creating the dialog
        alertDialog.setOnShowListener(dialogInterface -> {
            Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(view -> {
                String userName = username.getText().toString();
                String passWord = password.getText().toString();
                String phoneNum = phoneNumber.getText().toString();

                String todayDate = getDate();

                float currWeight = Float.parseFloat(currentWeight.getText().toString());
                float gWeight = Float.parseFloat(goalWeight.getText().toString());

                String goalDate = calDate.getText().toString();

                // Input Validation
                boolean isValid = true;

                if (userName.length() < 4) {
                    username.setError("Username must be at least 4 characters.");
                    isValid = false;
                }

                if (passWord.length() < 5) {
                    password.setError("Password must be at least 5 characters.");
                    isValid = false;
                }

                if (phoneNum.length() != 11) {
                    phoneNumber.setError("Phone number must be 11 digits.");
                    isValid = false;
                }

                if (currWeight <= 80) {
                    phoneNumber.setError("Enter a realistic weight");
                    isValid = false;
                }

                if (gWeight <= 80) {
                    phoneNumber.setError("Enter a healthy weight goal");
                    isValid = false;
                }

                if (isValid) {
                    // Validation succeeded, proceed with data insertion
                    int userId = db.insertUserData(userName, passWord, phoneNum, gWeight, goalDate);
                    db.insertWeightData(userId, currWeight, todayDate);

                    // Dismiss the dialog after successful data insertion
                    alertDialog.dismiss();
                }
            });
        });

// Show the dialog
        alertDialog.show();

// Return the AlertDialog object
        return alertDialog;
    }

    private String getDate() {
        Date currentDate = new Date();
        // Define the desired date format
        String dateFormatPattern = "EEEE, MMMM d, yyyy";
        // Create a SimpleDateFormat object with the specified pattern and locale
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatPattern, Locale.US);

        return dateFormat.format(currentDate);
    }

}
