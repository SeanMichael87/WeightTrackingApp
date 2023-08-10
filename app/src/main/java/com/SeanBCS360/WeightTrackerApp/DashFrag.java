package com.SeanBCS360.WeightTrackerApp;

import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class DashFrag extends Fragment {

    private static final int PERMISSIONS_REQUEST_SMS = 1;
    TextView goalWeight;
    TextView weightOutput;
    TextView difference;
    TextView daysToGoal;
    EditText dailyWeight;
    Button  add;
    DBHandler db;
    String weightDiff;

    List<Double> weights;


    public DashFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dash, container, false);

        db = new DBHandler(getActivity());
        goalWeight = v.findViewById(R.id.dash_goal_weight);
        weightOutput = v.findViewById(R.id.current_weight);
        difference = v.findViewById(R.id.difference);
        dailyWeight = v.findViewById(R.id.daily_weight);
        daysToGoal = v.findViewById(R.id.days_to_goal);
        add = v.findViewById((R.id.weight_button));


        UserSessionManager manager = new UserSessionManager(requireActivity());
        int userID = manager.getUserId();

        if (userID != -1) {

            //Current Weight Values
            weights = db.getUserWeights(userID);
            double currWeight = weights.get(weights.size() -1);
            weightOutput.setText(formatWeights(currWeight));

            //Current Goal Values
            double getGoalWeight = db.getGoalWeight(userID);
            goalWeight.setText(formatWeights(getGoalWeight));

            //Current weight Difference
            weightDiff = weightDifference(getGoalWeight, currWeight);
            difference.setText(weightDiff);

            //Days left to Goal Date
            long daysDiff = dateDifference(db.getGoalDate(userID));
            daysToGoal.setText(String.valueOf(daysDiff));

            //Add new daily weight
            add.setOnClickListener(view -> {
                String weightInput = dailyWeight.getText().toString().trim();

                if (weightInput.isEmpty()) {
                    dailyWeight.setError("Please enter a weight value.");
                    return;
                }

                try {
                    float updateWeight = Float.parseFloat(weightInput);

                    if (updateWeight <= 10) {
                        dailyWeight.setError("Weight must be greater than 10.");
                        return;
                    }

                    // Continue with adding the weight and updating the UI
                    addWeightToDB(db, updateWeight, userID);
                    weightOutput.setText(formatWeights(updateWeight));

                    weightDiff = weightDifference(getGoalWeight, updateWeight);
                    difference.setText(weightDiff);
                    dailyWeight.setText("");

                    if (!manager.messageSent(userID)) {
                        if (Float.parseFloat(weightDiff) <= 0f && db.getSMSState(userID)) {
                            sendCongratulatorySMS(userID);
                            manager.setMessageSent(userID, true);
                        }
                    }
                } catch (NumberFormatException e) {
                    dailyWeight.setError("Invalid weight format. Please enter a valid number.");
                }
            });


        } else {
            Toast.makeText(getActivity(), "Id not found", Toast.LENGTH_LONG).show();
        }
        // Inflate the layout for this fragment
        return v;
    }

    public void addWeightToDB(DBHandler db, float currWeight, int userID) {
        Date currentDate = new Date();
        // Define the desired date format
        String dateFormatPattern = "EEEE, MMMM d, yyyy";
        // Create a SimpleDateFormat object with the specified pattern and locale
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatPattern, Locale.US);

        String todayDate = dateFormat.format(currentDate);

        db.insertWeightData(userID, currWeight, todayDate);
    }

    public String formatWeights(double weights) {
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(weights);
    }

    public String weightDifference(double goalWeight, double currWeight) {
        double difference = currWeight - goalWeight;
        return formatWeights(difference);
    }

    public long dateDifference(String goalDate) {
        Date currDate = new Date();
        // Define the desired date format
        String dateFormatPattern = "EEEE, MMMM d, yyyy";
        // Create a SimpleDateFormat object with the specified pattern and locale
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatPattern, Locale.US);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormatPattern);

        String todayDate = dateFormat.format(currDate);

        LocalDate startDate = LocalDate.parse(goalDate, formatter);
        LocalDate endDate = LocalDate.parse(todayDate, formatter);
        
        return Math.abs(ChronoUnit.DAYS.between(startDate, endDate));
    }
    private void sendCongratulatorySMS(int userId) {
        String phoneNumber = db.getPhoneNumber(userId);
        String message = "Congratulations on meeting your weight goals! Keep up the Great Work!";

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage("1"+ phoneNumber, null, message, null, null);
    }
}
