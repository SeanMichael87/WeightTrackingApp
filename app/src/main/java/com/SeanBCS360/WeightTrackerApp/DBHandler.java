package com.SeanBCS360.WeightTrackerApp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandler extends SQLiteOpenHelper {
    private static final String DB_NAME = "profile.db";
    private static final int DB_VERSION = 1;
    public static DBHandler instance;

    private DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static DBHandler getInstance(Context context) {
        if(instance == null) {
            instance = new DBHandler(context);
        }
        return instance;
    }

    private static final class ProfileTable {
        private static final String TABLE_NAME = "userprofile";
        private static final String ID_COL = "id";
        private static final String USERNAME_COL = "username";
        private static final String PASS_COL = "password";
        private static final String CURRENT_COL = "current_weight";
        private static final String CURR_DATE_COL = "curr_date";
        private static final String GOAL_COL = "goal_weight";
        private static final String GOAL_DATE_COL = "goal_date";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + ProfileTable.TABLE_NAME + " ("
                    + ProfileTable.ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + ProfileTable.USERNAME_COL + " TEXT,"
                    + ProfileTable.PASS_COL + " TEXT,"
                    + ProfileTable.CURRENT_COL + " FLOAT,"
                    + ProfileTable.CURR_DATE_COL + " TEXT,"
                    + ProfileTable.GOAL_COL+ " FLOAT,"
                    + ProfileTable.GOAL_DATE_COL + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public long addUser(String username, String password, float currentWeight, String currentDate,
                        float goalWeight, String goalDate) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ProfileTable.USERNAME_COL, username);
        values.put(ProfileTable.PASS_COL, password);
        values.put(ProfileTable.CURRENT_COL, currentWeight);
        values.put(ProfileTable.CURR_DATE_COL, currentDate);
        values.put(ProfileTable.GOAL_COL, goalWeight);
        values.put(ProfileTable.GOAL_DATE_COL, goalDate);

        return db.insert(ProfileTable.TABLE_NAME, null, values);
    }
    
    public boolean loginValidation(String username, String password){
     boolean validation = false;

    SQLiteDatabase db = this.getReadableDatabase();
    String sql = "select * from " + ProfileTable.TABLE_NAME;
    Cursor cursor = db.rawQuery(sql,null);
    cursor.moveToFirst();
    while(cursor.isAfterLast()== false){
         long id = cursor.getLong(0);
         String username = cursor.getString(1);
         String password = cursor.getString(2);
         float currentWeight = cursor.getFloat(3);
         String currentDate = cursor.getString(4);
          float goalWeight = cursor.getFloat(5);
          String goalDate = cursor.getString(6);
         Log.d(TAG, "Username/Pass = " + username + ", " + password + ", " + validation);
        if(email.equals(emailDb) && password.equals(passwordDb)){
            validation=true;
            break;
        }
        else{
            cursor.moveToNext();
        }

    }
    return  validation;
}

    
public void getGoalWeight(String username) {
   SQLiteDatabase db = getReadableDatabase();

   String sql = "select GOAL_COL from " + ProfileTable.TABLE_NAME + " where username = '" + username + "'";
   Cursor cursor = db.rawQuery(sql, null);
   if (cursor.moveToFirst()) {
      do {
         long id = cursor.getLong(0);
         String username = cursor.getString(1);
         String password = cursor.getString(2);
         float currentWeight = cursor.getFloat(3);
          String currentDate = cursor.getString(4);
          float goalWeight = cursor.getFloat(5);
          String goalDate = cursor.getString(6);
         Log.d(TAG, "Goal Weight = " + username + ", " + goalDate + ", " + goalWeight);
      } while (cursor.moveToNext());
   }
   cursor.close();
}
}
