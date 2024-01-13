package com.example.loginandsignup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditProfileActivity extends AppCompatActivity {

    EditText editName, editEmail, editUsername, editPassword;
    Button saveButton;
    String nameUser, emailUser, usernameUser, passwordUser;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        reference = FirebaseDatabase.getInstance().getReference("users");
        editName = findViewById(R.id.editName);
        editEmail = findViewById(R.id.editEmail);
        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        saveButton = findViewById(R.id.saveButton);
        showData();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNameChanged() || isEmailChanged() || isPasswordChanged()) {
                    Toast.makeText(EditProfileActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditProfileActivity.this, "No Changes Found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean updateUserInfo(String field, String value) {
        if (!value.equals(field)) {
            reference.child(usernameUser).child(field).setValue(value);
            return true;
        } else {
            return false;
        }
    }
    public boolean isNameChanged(){
        String newName = editName.getText().toString();
        boolean isChanged = updateUserInfo("name", newName);
        if (isChanged) {
            nameUser = newName;
        }
        return isChanged;
    }

    public boolean isEmailChanged(){
        String newEmail = editEmail.getText().toString();
        boolean isChanged = updateUserInfo("email", newEmail);
        if (isChanged) {
            emailUser = newEmail;
        }
        return isChanged;
    }

    public boolean isPasswordChanged(){
        String newPassword = editPassword.getText().toString();
        boolean isChanged = updateUserInfo("password", newPassword);
        if (isChanged) {
            passwordUser = newPassword;
        }
        return isChanged;
    }

    public void showData(){
        Intent intent = getIntent();

        nameUser = intent.getStringExtra("name");
        emailUser = intent.getStringExtra("email");
        usernameUser = intent.getStringExtra("username");
        passwordUser = intent.getStringExtra("password");

        editName.setText(nameUser);
        editEmail.setText(emailUser);
        editUsername.setText(usernameUser);
        editPassword.setText(passwordUser);
    }
}