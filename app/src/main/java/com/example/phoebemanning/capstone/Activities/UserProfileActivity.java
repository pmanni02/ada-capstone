package com.example.phoebemanning.capstone.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.phoebemanning.capstone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserProfileActivity extends AppCompatActivity {

    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    RadioGroup radioGroup;
    RadioButton female;
    RadioButton male;
    EditText weight;
    EditText heightFt;
    EditText heightIn;
    EditText age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        radioGroup = findViewById(R.id.gender);
        weight = findViewById(R.id.weight);
        heightFt = findViewById(R.id.heightFt);
        heightIn = findViewById(R.id.heightIn);
        age = findViewById(R.id.age);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.no_favorite_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_logout:
                if(mUser !=null && mAuth != null){
                    mAuth.signOut();
                    Toast.makeText(this, "Signed Out", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(UserProfileActivity.this, LoginActivity.class));
                    finish();
                }
                return true;

            case R.id.action_user_scans:
                startActivity(new Intent(UserProfileActivity.this, UserScansActivity.class));
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
