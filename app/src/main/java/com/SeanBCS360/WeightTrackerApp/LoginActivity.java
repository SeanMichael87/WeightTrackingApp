package com.SeanBCS360.WeightTrackerApp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class LoginActivity extends AppCompatActivity {

    private DBHandler db;
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
        username =findViewById(R.id.username);
        password = findViewById(R.id.password);

        loginButton.setOnClickListener(view -> {
            db.getInstance(LoginActivity.this)
            boolean valid = db.loginValidation(username, password);
            if(valid) {
                enterMainActivity()
            } else {
                CharSequence text = "Login Failed!";
                int duration = Toast.LENGTH_SHORT;

                Toast.makeText(this, text, duration).show();
            }
                     
        });
        signUp.setOnClickListener(view -> showSignUpDialog());
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
