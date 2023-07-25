package com.SeanBCS360.WeightTrackerApp;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        loginButton = findViewById(R.id.login);

        loginButton.setOnClickListener(view -> showCustomDialog());
    }

    void showCustomDialog() {
        Button accept;
        Button deny;
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.sms_permissions);

        accept = dialog.findViewById(R.id.button_accept);
        deny = dialog.findViewById(R.id.button_deny);

        accept.setOnClickListener(view -> dialog.dismiss());
        deny.setOnClickListener(view -> dialog.dismiss());

        dialog.show();

    }
}