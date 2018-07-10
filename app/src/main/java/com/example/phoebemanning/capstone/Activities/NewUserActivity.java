package com.example.phoebemanning.capstone.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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
    EditText age;
    EditText weight;
    EditText email;
    EditText password;

    public void createAccount(View view){
        final String emailString = email.getText().toString();
        final String passwordString = password.getText().toString();
        final String firstNameString = firstName.getText().toString();
        final String lastNameString = lastName.getText().toString();
        final Integer ageInt = Integer.parseInt(age.getText().toString());
        final Integer weightInt = Integer.parseInt(weight.getText().toString());

        if(!emailString.equals("") && !passwordString.equals("")){
            mAuth.createUserWithEmailAndPassword(emailString, passwordString).addOnCompleteListener(NewUserActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()){
                        Toast.makeText(NewUserActivity.this, "Failed to create account", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(NewUserActivity.this, "Account Created", Toast.LENGTH_SHORT).show();

                        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                        String uid = current_user.getUid();
                        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
//                      add new user info to database
                        User user = new User(emailString, firstNameString, lastNameString, ageInt, weightInt);
                        databaseReference.setValue(user);

                        Intent intent = new Intent(NewUserActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
            });
        } else {
            Toast.makeText(this, "Username and password cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        mAuth = FirebaseAuth.getInstance();

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        age = findViewById(R.id.age);
        weight = findViewById(R.id.weight);
        email = findViewById(R.id.newEmailId);
        password = findViewById(R.id.newPasswordId);
    }
}
