package com.SeanBCS360.WeightTrackerApp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {
    private static final String DB_NAME = "user_prof.db";
    private static final int DB_VERSION = 5;

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    private static final class ProfileTable {
        private static final String PROFILE_TABLE = "userprofile";
        private static final String WEIGHT_TABLE = "weight_data";
        private static final String ID_COL = "id";
        private static final String USERID_COL = "user_id";
        private static final String USERNAME_COL = "username";
        private static final String PASS_COL = "password";
        private static final String PHONE_COL = "phone_numb";
        private static final String CURRENT_COL = "current_weight";
        private static final String CURR_DATE_COL = "curr_date";
        private static final String GOAL_COL = "goal_weight";
        private static final String GOAL_DATE_COL = "goal_date";
        private static final String SMS_STATE_COL = "sms_state";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the User Table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + ProfileTable.PROFILE_TABLE + " ("
                + ProfileTable.ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ProfileTable.USERNAME_COL + " TEXT UNIQUE NOT NULL,"
                + ProfileTable.PASS_COL + " TEXT NOT NULL,"
                + ProfileTable.PHONE_COL + " TEXT NOT NULL,"
                + ProfileTable.GOAL_COL + " REAL NOT NULL,"
                + ProfileTable.GOAL_DATE_COL + " TEXT NOT NULL,"
                + ProfileTable.SMS_STATE_COL + " TEXT NOT NULL)");

        //Create the user Weight History Table with key reference to primary table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + ProfileTable.WEIGHT_TABLE + " ("
                + ProfileTable.ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ProfileTable.USERID_COL + " INTEGER NOT NULL,"
                + ProfileTable.CURRENT_COL + " REAL NOT NULL,"
                + ProfileTable.CURR_DATE_COL + " TEXT NOT NULL,"
                + "FOREIGN KEY (user_id) REFERENCES " + ProfileTable.PROFILE_TABLE + "(id)" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists " + ProfileTable.PROFILE_TABLE);
        db.execSQL("drop table if exists " + ProfileTable.WEIGHT_TABLE);
        onCreate(db);
    }

    //USER AUTHENTICATION
    public int authenticateUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"id"};
        String selection = "username = ? AND password = ?";
        String[] selectionArgs = {username, password};
        Cursor cursor = db.query(ProfileTable.PROFILE_TABLE, columns, selection, selectionArgs, null, null, null);

        int userId = -1; // Default value if user not found
        if (cursor != null && cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            cursor.close();
        }
        db.close();
        return userId;
    }

    //CREATE functions
    public int insertUserData(String username, String password, String phoneNumber, double goalWeight, String goalDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ProfileTable.USERNAME_COL, username);
        values.put(ProfileTable.PASS_COL, password);
        values.put(ProfileTable.PHONE_COL, password);
        values.put(ProfileTable.GOAL_COL, goalWeight);
        values.put(ProfileTable.GOAL_DATE_COL, goalDate);
        values.put(ProfileTable.SMS_STATE_COL, "false");
        long userId = db.insert(ProfileTable.PROFILE_TABLE, null, values);
        db.close();
        return (int) userId; //Will be used for authentication and validation of data
    }

    public void insertWeightData(int userId, double weight, String weightDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ProfileTable.USERID_COL, userId);
        values.put(ProfileTable.CURRENT_COL, weight);
        values.put(ProfileTable.CURR_DATE_COL, weightDate);
        db.insert(ProfileTable.WEIGHT_TABLE, null, values);
        db.close();
    }

    //READ functions
    public boolean getSMSState(int userid) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {ProfileTable.SMS_STATE_COL};
        String selection = "id = ?";
        String[] selectionArgs = {String.valueOf(userid)};
        Cursor cursor = db.query(ProfileTable.PROFILE_TABLE, columns, selection, selectionArgs, null, null, null);

        String choice = "false";
        if (cursor != null && cursor.moveToFirst()) {
            choice = cursor.getString(cursor.getColumnIndexOrThrow(ProfileTable.SMS_STATE_COL));
            cursor.close();
        }
        db.close();
        return Boolean.parseBoolean(choice);
    }

    public double getGoalWeight(int userid) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {ProfileTable.GOAL_COL};
        String selection = "id = ?";
        String[] selectionArgs = {String.valueOf(userid)};
        Cursor cursor = db.query(ProfileTable.PROFILE_TABLE, columns, selection, selectionArgs, null, null, null);

        double weight = -1;
        if (cursor != null && cursor.moveToFirst()) {
            weight = cursor.getDouble(cursor.getColumnIndexOrThrow(ProfileTable.GOAL_COL));
            cursor.close();
        }
        db.close();
        return weight;
    }

    public List<Double> getUserWeights(int userId) {
        List<Double> weights = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                "weight_data",
                new String[]{ProfileTable.CURRENT_COL},
                "user_id = ?",
                new String[]{String.valueOf(userId)},
                null,
                null,
                null
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                double weight = cursor.getDouble(cursor.getColumnIndexOrThrow(ProfileTable.CURRENT_COL));
                weights.add(weight);
            }
            cursor.close();
        }
        db.close();
        return weights;
    }

    public String getGoalDate(int userid) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {ProfileTable.GOAL_DATE_COL};
        String selection = "id = ?";
        String[] selectionArgs = {String.valueOf(userid)};
        Cursor cursor = db.query(ProfileTable.PROFILE_TABLE, columns, selection, selectionArgs, null, null, null);

        String date = "";
        if (cursor != null && cursor.moveToFirst()) {
            date = cursor.getString(cursor.getColumnIndexOrThrow(ProfileTable.GOAL_DATE_COL));
            cursor.close();
        }
        db.close();
        return date;
    }

    // Method to fetch all data from the weight database
    public List<DataModel> getAllData(int userID) {
        List<DataModel> dataList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = "user_id = ?";
        String[] selectionArgs = {String.valueOf(userID)};
        Cursor cursor = db.query(ProfileTable.WEIGHT_TABLE, null, selection, selectionArgs, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(ProfileTable.ID_COL));
                int userid = cursor.getInt(cursor.getColumnIndexOrThrow(ProfileTable.USERID_COL));
                String weight = cursor.getString(cursor.getColumnIndexOrThrow(ProfileTable.CURRENT_COL));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(ProfileTable.CURR_DATE_COL));
                DataModel data = new DataModel(id, userid, weight, date);
                dataList.add(data);
            }
            cursor.close();
        }
        return dataList;
    }

    //UPDATE functions
    public boolean updateProfile(long userid, String username, String password, float goalWeight) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        if (!username.isEmpty()) {
            values.put(ProfileTable.USERNAME_COL, username);
        }
        if (!password.isEmpty()) {
            values.put(ProfileTable.PASS_COL, password);
        }
        if (goalWeight != 0f) {
            values.put(ProfileTable.GOAL_COL, goalWeight);
        }

        int rowsUpdated = db.update(ProfileTable.PROFILE_TABLE, values, "id = ?",
                new String[]{Float.toString(userid)});
        return rowsUpdated > 0;
    }

    public void updateWeight(int id, String newWeight) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ProfileTable.CURRENT_COL, newWeight);

        db.update(ProfileTable.WEIGHT_TABLE, values, "id = ?", new String[]{String.valueOf(id)});

        db.close();
    }

    public void updateSMSState(long userid, String state) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ProfileTable.SMS_STATE_COL, state);
        db.update(ProfileTable.PROFILE_TABLE, values, "id = ?",
                new String[]{Float.toString(userid)});
    }

    public boolean deleteProfile(long userid) {
        SQLiteDatabase db = getWritableDatabase();
        int rowsDeleted = db.delete(ProfileTable.PROFILE_TABLE, ProfileTable.ID_COL + " = ?",
                new String[]{Long.toString(userid)});

        db.delete(ProfileTable.WEIGHT_TABLE, ProfileTable.USERID_COL + " = ?",
                new String[]{Long.toString(userid)});

        return rowsDeleted > 0;
    }

    public boolean deleteRowWeight(int id) {
        SQLiteDatabase db = getWritableDatabase();
        int rowsDeleted = db.delete(ProfileTable.WEIGHT_TABLE, ProfileTable.ID_COL + " = ?",
                new String[]{Long.toString(id)});

        return rowsDeleted > 0;
    }

}
