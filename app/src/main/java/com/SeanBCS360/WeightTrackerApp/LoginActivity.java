package com.SeanBCS360.WeightTrackerApp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class LoginActivity extends AppCompatActivity {

    Button loginButton;
    Button signUp;
    Button forgotPass;
    EditText username;
    EditText password;
  
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // not permanent code - used for testing navigation

        loginButton = findViewById(R.id.login);
        signUp = findViewById(R.id.signup);

        loginButton.setOnClickListener(view -> enterMainActivity());
        signUp.setOnClickListener(view -> showSignUpDialog());
    }

    void showCustomDialog() {
        FragmentManager manager = getSupportFragmentManager();
        PermissionsDialog dialog = new PermissionsDialog();
        dialog.show(manager, "sms permission");
    }
    void showSignUpDialog() {
        FragmentManager manager = getSupportFragmentManager();
        SignUpDialog dialog = new SignUpDialog();
        dialog.show(manager, "Sign Up");
    }


    public void enterMainActivity() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }

}
