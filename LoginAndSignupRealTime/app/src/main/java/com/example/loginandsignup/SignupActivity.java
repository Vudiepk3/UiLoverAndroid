package com.example.loginandsignup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    EditText signupName, signupEmail, signupUsername, signupPassword;
    TextView loginRedirectText;
    Button signupButton;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signupName = findViewById(R.id.signup_name);
        signupEmail = findViewById(R.id.signup_email);
        signupUsername = findViewById(R.id.signup_username);
        signupPassword = findViewById(R.id.signup_password);
        signupButton = findViewById(R.id.signup_button);
        loginRedirectText = findViewById(R.id.loginRedirectText);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateSignUpName() | !validateSignUpEmail() | !validateSignUpUserName() | !validateSignUpPassword()) {
                        Toast.makeText(SignupActivity.this, "Check your data", Toast.LENGTH_SHORT).show();
                }else if(!isInPictureInPictureMode()){
                    Toast.makeText(SignupActivity.this, "Check your internet", Toast.LENGTH_SHORT).show();
                }else{
                    database = FirebaseDatabase.getInstance();
                    reference = database.getReference("users");
                    String name = signupName.getText().toString();
                    String email = signupEmail.getText().toString();
                    String username = signupUsername.getText().toString();
                    String password = signupPassword.getText().toString();
                    HelperClass helperClass = new HelperClass(name, email, username, password);
                    reference.child(username).setValue(helperClass);
                    Toast.makeText(SignupActivity.this, "You have signup successfully!", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    }, 1000); // 1000 milliseconds = 1 seconds

                }

            }
        });

        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }
    private boolean validateInput(EditText editText, String fieldName) {
        String val = editText.getText().toString().trim();
        if (val.isEmpty()) {
            editText.setError(fieldName + " cannot be empty");
            return false;
        } else {
            editText.setError(null);
            return true;
        }
    }

    private boolean validateSignUpName() {
        return validateInput(signupName, "Name");
    }

    private boolean validateSignUpEmail() {
        return validateInput(signupEmail, "Email");
    }

    private boolean validateSignUpUserName() {
        return validateInput(signupUsername, "Username");
    }

    private boolean validateSignUpPassword() {
        return validateInput(signupPassword, "Password");
    }
    public boolean isInternetConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return
                activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}