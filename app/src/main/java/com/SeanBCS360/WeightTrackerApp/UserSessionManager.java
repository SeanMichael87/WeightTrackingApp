package com.SeanBCS360.WeightTrackerApp;

import android.content.Context;
import android.content.SharedPreferences;

//Used to save and get the userID information for account
public class UserSessionManager {

    private static final String PREF_NAME = "UserSessionPref";
    private static final String KEY_USER_ID = "userId";

    private final SharedPreferences pref;
    private final SharedPreferences.Editor editor;
    private final Context context;

    public UserSessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void setUserId(int userId) {
        editor.putInt(KEY_USER_ID, userId);
        editor.apply();
    }

    public int getUserId() {
        return pref.getInt(KEY_USER_ID, -1); // -1 indicates default value if not found
    }

    public void setIsFirstLogin(int userId, boolean isFirstLogin){
        editor.putBoolean(Integer.toString(userId), isFirstLogin);
        editor.apply();

    }
    public boolean isFirstLogin(int userId) {
        return pref.getBoolean(Integer.toString(userId), true);
    }

}
