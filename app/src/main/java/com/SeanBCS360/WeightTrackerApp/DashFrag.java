package com.SeanBCS360.WeightTrackerApp;

import android.graphics.Color;
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

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
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
    LineChart graph;
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
        graph = v.findViewById(R.id.graph);
        goalWeight = v.findViewById(R.id.dash_goal_weight);
        weightOutput = v.findViewById(R.id.current_weight);
        difference = v.findViewById(R.id.difference);
        dailyWeight = v.findViewById(R.id.daily_weight);
        daysToGoal = v.findViewById(R.id.days_to_goal);
        add = v.findViewById((R.id.weight_button));


        UserSessionManager manager = new UserSessionManager(requireActivity());
        int userId = manager.getUserId();

        if (userId != -1) {
            setupWeightGraph(userId);

            //Current Weight Values
            weights = db.getUserWeights(userId);
            double currWeight = weights.get(weights.size() -1);
            weightOutput.setText(formatWeights(currWeight));

            //Current Goal Values
            double getGoalWeight = db.getGoalWeight(userId);
            goalWeight.setText(formatWeights(getGoalWeight));

            //Current weight Difference
            weightDiff = weightDifference(getGoalWeight, currWeight);
            difference.setText(weightDiff);

            //Days left to Goal Date
            long daysDiff = dateDifference(db.getGoalDate(userId));
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
                    addWeightToDB(db, updateWeight, userId);
                    weightOutput.setText(formatWeights(updateWeight));

                    weightDiff = weightDifference(getGoalWeight, updateWeight);
                    difference.setText(weightDiff);
                    dailyWeight.setText("");

                    if (!manager.messageSent(userId)) {
                        if (Float.parseFloat(weightDiff) <= 0f && db.getSMSState(userId)) {
                            sendCongratulatorySMS(userId);
                            manager.setMessageSent(userId, true);
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

    //Resources:
    //https://stackoverflow.com/questions/42553017/android-calculate-days-between-two-dates
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

    //Create graph:
    //Resources:
    //https://weeklycoding.com/mpandroidchart/
    //https://stackoverflow.com/questions/32718820/failed-to-resolve-com-github-philjaympandroidchartv2-1-4
    //https://stackoverflow.com/questions/60682917/legend-setposition-is-not-a-valid-method
    private void setupWeightGraph(int userId) {
        // Retrieve weight history data from your database
        List<Double> weights = db.getUserWeights(userId);

        // Calculate the goal weight and Y-axis range
        double getGoalWeight = db.getGoalWeight(userId);
        double minWeight = Math.min(getGoalWeight, Collections.min(weights));
        double maxWeight = Math.max(getGoalWeight, Collections.max(weights));

        // Create a list of Entry objects from the weight history data
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < weights.size(); i++) {
            entries.add(new Entry(i, weights.get(i).floatValue()));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Weight History");
        graph.getDescription().setText("Daily Weights");

        graph.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        graph.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        graph.getLegend().setDrawInside(true);

        dataSet.setColor(Color.BLUE); // Customize line color

        LineData lineData = new LineData(dataSet);
        graph.setData(lineData);

        // Customize X-axis
        XAxis xAxis = graph.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int index = (int) value;
                if (index >= 0 && index < weights.size()) {
                    return formatDateForXAxis(); // Implement this method to format the date
                }
                return "";
            }
        });

        // Customize Y-axis
        YAxis yAxisLeft = graph.getAxisLeft();
        graph.getAxisRight().setEnabled(false);
        yAxisLeft.setAxisMinimum((float) minWeight); // Minimum Y-axis value
        yAxisLeft.setAxisMaximum((float) maxWeight); // Maximum Y-axis value

        // Refresh chart
        graph.invalidate();
    }
    private String formatDateForXAxis() {
        //CurrentDate placeholder
        LocalDate currentDate = LocalDate.now();

        String dateFormatPattern = "MMM dd,";

        // Create a DateTimeFormatter with the specified pattern
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormatPattern);

       return currentDate.format(formatter);

    }
}
