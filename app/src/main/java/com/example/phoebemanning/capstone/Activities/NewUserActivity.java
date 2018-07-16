package com.example.phoebemanning.capstone.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.phoebemanning.capstone.Models.Scan;
import com.example.phoebemanning.capstone.Models.User;
import com.example.phoebemanning.capstone.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewUserActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private String userID;

    EditText firstName;
    EditText lastName;
    EditText email;
    EditText password;
    EditText confirmPassword;
    ProgressBar newUserProgress;

    public void createAccount(View view){

        final String emailString = email.getText().toString();
        final String passwordString = password.getText().toString();
        final String confirmPasswordString = confirmPassword.getText().toString();
        final String firstNameString = firstName.getText().toString();
        final String lastNameString = lastName.getText().toString();

        if(!emailString.equals("") && !passwordString.equals("")){
            if(passwordString.equals(confirmPasswordString)){
                newUserProgress.setVisibility(View.VISIBLE);
                mAuth.createUserWithEmailAndPassword(emailString, passwordString).addOnCompleteListener(NewUserActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(NewUserActivity.this, "Failed to create account", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(NewUserActivity.this, "Account Created", Toast.LENGTH_SHORT).show();

                            FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                            String uid = current_user.getUid();

//                      add new user info to database
                            User user = new User(emailString, firstNameString, lastNameString, "2000");
                            databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
                            databaseReference.setValue(user);

//                      add empty scan to database
                            Scan emptyScan = new Scan("Null", "Null", "Null");
                            DatabaseReference databaseReferenceScan = FirebaseDatabase.getInstance().getReference().child("Scans").child(uid).push();
                            databaseReferenceScan.setValue(emptyScan);

                            Intent intent = new Intent(NewUserActivity.this, MainActivity.class);
                            Toast.makeText(NewUserActivity.this, "Fill out user settings for more accurate results", Toast.LENGTH_LONG).show();
                            startActivity(intent);
                        }
                        newUserProgress.setVisibility(View.INVISIBLE);
                    }
                });
            } else {
                Toast.makeText(this, "Passwords must match, try again.", Toast.LENGTH_SHORT).show();
            }
            
        } else {
            Toast.makeText(this, "Username and password cannot be empty", Toast.LENGTH_SHORT).show();
            newUserProgress.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        mAuth = FirebaseAuth.getInstance();

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        email = findViewById(R.id.newEmailId);
        password = findViewById(R.id.newPasswordId);
        confirmPassword = findViewById(R.id.confirmPassword);
        newUserProgress = findViewById(R.id.newUserProgress);
    }
}
