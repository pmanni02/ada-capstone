package com.example.phoebemanning.capstone.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
    RadioButton femaleBtn;
    RadioButton maleBtn;
    EditText weight;
    EditText heightFt;
    EditText heightIn;
    EditText age;
    Button submitBtn;
    
    public void submitSettings(View view){
        
        if((weight.getText().toString().equals("")) ||
                (heightFt.getText().toString().equals("")) ||
                (heightIn.getText().toString().equals("")) ||
                (age.getText().toString().equals(""))){
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        } else {
            Double weightVal = Double.parseDouble(weight.getText().toString());
            Double heightFtVal = Double.parseDouble(heightFt.getText().toString());
            Double heightInVal = Double.parseDouble(heightIn.getText().toString());
            Integer ageVal = Integer.parseInt(age.getText().toString());
            int gender = radioGroup.getCheckedRadioButtonId();

            Integer bmr = getBasalMetabolicRate(gender, weightVal, heightFtVal, heightInVal, ageVal);
            Toast.makeText(this, bmr.toString(), Toast.LENGTH_SHORT).show();
        }
    }

//    Men	BMR = 88.362 + (13.397 x weight in kg) + (4.799 x height in cm) - (5.677 x age in years)
//    Women BMR = 447.593 + (9.247 x weight in kg) + (3.098 x height in cm) - (4.330 x age in years)

    private int getBasalMetabolicRate(int gender, Double weightVal, Double heightFtVal, Double heightInVal, Integer ageVal) {
        Double weightKg = weightVal * 0.45359;
        Double totalFeet = heightFtVal + (heightInVal * 0.0833);
        Double heightCm = totalFeet * 30.48;
        Double BMR;

        if(gender == femaleBtn.getId()){
            BMR = 447.593 + (9.247 * weightKg) + (3.098 * heightCm) - (4.330 * ageVal);
        } else {
            BMR = 88.362 + (13.397 * weightKg) + (4.799 * heightCm) - (5.677 * ageVal);
        }
        return (int) Math.rint(BMR);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        radioGroup = findViewById(R.id.gender);
        femaleBtn = findViewById(R.id.femaleRadio);
        maleBtn = findViewById(R.id.maleRadio);
        weight = findViewById(R.id.weight);
        heightFt = findViewById(R.id.heightFt);
        heightIn = findViewById(R.id.heightIn);
        age = findViewById(R.id.age);
        submitBtn = findViewById(R.id.submit);
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
