package com.SeanBCS360.WeightTrackerApp;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    NavigationBarView navigationBarView;
    DBHandler db;
    UserSessionManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationBarView = findViewById(R.id.bottomNavigationView);
        navigationBarView.setOnItemSelectedListener(this);
        navigationBarView.setSelectedItemId(R.id.dashboard);
    }
    @Override
    protected void onStart() {
        super.onStart();
        db = new DBHandler(this);
        manager = new UserSessionManager(this);
        int userid = manager.getUserId();

        if(!Boolean.parseBoolean(db.getSMSState(userid))) {
            showSmsDialog();
        }

    }

    DashFrag newFrag = new DashFrag();
    HistoryFrag hisFrag = new HistoryFrag();
    ProfileFrag profileFrag = new ProfileFrag();
    void showSmsDialog() {
        FragmentManager manager = getSupportFragmentManager();
        PermissionsDialog dialog = new PermissionsDialog();
        dialog.show(manager, "sms permission");
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        FragmentManager manager = getSupportFragmentManager();

        if(id == R.id.dashboard) {
            manager.beginTransaction().replace(R.id.flFragment, newFrag).commit();
            return true;
        } else if (id == R.id.history) {
            manager.beginTransaction().replace(R.id.flFragment, hisFrag).commit();
            return true;
        } else if (id == R.id.profile) {
            manager.beginTransaction().replace(R.id.flFragment, profileFrag).commit();
            return true;
        }

        return false;
    }
}