package com.example.phoebemanning.capstone.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phoebemanning.capstone.Models.User;
import com.example.phoebemanning.capstone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    TextView userName;
    TextView userBmr;
    TextView userEmail;
    Button defaultBtn;
    Button updateBtn;
    
    public void defaultBtnClick(View view){
        if(mUser != null){
            String uid = mUser.getUid();
            final DatabaseReference updateDbRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("dailyCalAmount");
            updateDbRef.setValue("2000");
            Toast.makeText(this, "Your new Daily Value is 2000", Toast.LENGTH_SHORT).show();
        }
    }
    
    public void updateBtnClick(View view){
        startActivity(new Intent(ProfileActivity.this, UserSettingsActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setTitle("");
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        userName = findViewById(R.id.userName);
        userBmr = findViewById(R.id.userBmr);
        userEmail = findViewById(R.id.userEmail);
        defaultBtn = findViewById(R.id.defaultBtn);
        updateBtn = findViewById(R.id.updateBtn);

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        String current_id = mUser.getUid();
        final DatabaseReference databaseReference = database.getReference().child("Users").child(current_id);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                String fullName = null;
                String email = null;
                String bmr = null;
                if (user != null) {
                    fullName = user.getFirstName().toUpperCase() + " " + user.getLastName().toUpperCase();
                    email = user.getEmail();
                    bmr = user.getDailyCalAmount();
                }
                userName.setText(fullName);
                userEmail.setText(email);
                if(bmr.equals("2000")){
                    userBmr.setText("Daily Calories (default): " + bmr);
                } else {
                    userBmr.setText("Daily Calories : " + bmr);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProfileActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_logout:
                if(mUser !=null && mAuth != null){
                    mAuth.signOut();
//                    Toast.makeText(this, "Signed Out", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                    finish();
                }
                return true;

            case R.id.action_settings:
                startActivity(new Intent(ProfileActivity.this, UserSettingsActivity.class));
                finish();
                return true;

            case R.id.action_user_scans:
                startActivity(new Intent(ProfileActivity.this, UserScansActivity.class));
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
