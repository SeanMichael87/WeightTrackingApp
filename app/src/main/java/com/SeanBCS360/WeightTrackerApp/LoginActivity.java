package com.SeanBCS360.WeightTrackerApp;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class LoginActivity extends AppCompatActivity {

    Button loginButton;
  
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        loginButton = findViewById(R.id.login);
        loginButton.setOnClickListener(view -> showCustomDialog());
    }

    void showCustomDialog() {
        FragmentManager manager = getSupportFragmentManager();
        PermissionsDialog dialog = new PermissionsDialog();
        dialog.show(manager, "sms permission");
    }

}
