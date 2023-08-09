package com.SeanBCS360.WeightTrackerApp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class LoginActivity extends AppCompatActivity {

    public DBHandler db;
    Button loginButton;
    Button signUp;
    Button forgotPass;
    EditText username;
    EditText password;
  
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.login);
        signUp = findViewById(R.id.signup);
        username =findViewById(R.id.username);
        password = findViewById(R.id.password);
        forgotPass = findViewById(R.id.reset_pass);

        loginButton.setOnClickListener(view -> {
           db = new DBHandler(LoginActivity.this);
            int userId= db.authenticateUser(username.getText().toString(), password.getText().toString());
            if(userId > -1) {
                UserSessionManager sessionManager = new UserSessionManager(this);
                sessionManager.setUserId(userId);
                enterMainActivity();
            } else {
                CharSequence text = "Login Failed!";
                int duration = Toast.LENGTH_SHORT;

                Toast.makeText(this, text, duration).show();
            }
                     
        });
        signUp.setOnClickListener(view -> showSignUpDialog());
        forgotPass.setOnClickListener(view -> showForgotPassDialog());
    }

    void showSignUpDialog() {
        FragmentManager manager = getSupportFragmentManager();
        SignUpDialog dialog = new SignUpDialog();
        dialog.show(manager, "Sign Up");
    }
    void showForgotPassDialog() {
        FragmentManager manager = getSupportFragmentManager();
        ForgotPassDialog dialog = new ForgotPassDialog();
        dialog.show(manager, "Forgot Password");
    }

    public void enterMainActivity() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }

}
