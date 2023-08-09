package com.SeanBCS360.WeightTrackerApp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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
public class ForgotPassDialog extends DialogFragment {

    public DBHandler db;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_get_pass, null);

        EditText username = v.findViewById(R.id.username);
        EditText phoneNumber = v.findViewById(R.id.phone_number);

        db = new DBHandler(getActivity());
        
        String enteredUserName = username.getText().toString();
        String enteredPhoneNum = phoneNumber.getText().toString();

        String queryPhoneNum;


        builder.setView(v)
                .setPositiveButton("Sign Up", (dialog, id) -> {
                    //if enterephone# correlates to entered useName -send message
                    sendPasswordSMS(userId); 
                })
                .setNegativeButton("Quit", (dialog, id) -> {
                    CharSequence text = "Come Back Soon!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast.makeText(getActivity(), text, duration).show();
                });

        // Create the AlertDialog object and return it
        return builder.create();
    }

    private void sendPasswordSMS(int userId) {
        String phoneNumber = db.getPhoneNumber(userId);
        String message = "Your password is: ";

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage("1"+ phoneNumber, null, message, null, null);
    }


}
