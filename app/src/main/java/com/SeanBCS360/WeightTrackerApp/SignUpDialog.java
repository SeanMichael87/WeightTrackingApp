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

    ImageView calenderIcon;
    public DBHandler db;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_signup, null);

        EditText username = v.findViewById(R.id.username);
        EditText password = v.findViewById(R.id.password);
        EditText currentWeight = v.findViewById(R.id.curr_weight);
        EditText goalWeight = v.findViewById(R.id.goal_weight);
        TextView calDate = v.findViewById(R.id.cal_date);

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
                .setPositiveButton("Sign Up", (dialog, id) -> {
                    String userName = username.getText().toString();
                    String passWord = password.getText().toString();

                    String todayDate = getDate();

                    float currWeight = Float.parseFloat(currentWeight.getText().toString());
                    float gWeight = Float.parseFloat(goalWeight.getText().toString());

                    String goalDate = calDate.getText().toString();

                    int userId = db.insertUserData(userName, passWord, gWeight, goalDate);
                    db.insertWeightData(userId, currWeight, todayDate);
                })
                .setNegativeButton("Quit", (dialog, id) -> {

                    CharSequence text = "Come Back Soon!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast.makeText(getActivity(), text, duration).show();
                });

        // Create the AlertDialog object and return it
        return builder.create();
    }

    public String getDate() {
        Date currentDate = new Date();
        // Define the desired date format
        String dateFormatPattern = "EEEE, MMMM d, yyyy";
        // Create a SimpleDateFormat object with the specified pattern and locale
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatPattern, Locale.US);

        return dateFormat.format(currentDate);
    }

}
