package com.example.phoebemanning.capstone.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.phoebemanning.capstone.Adapters.RecyclerAdapterNutrients;
import com.example.phoebemanning.capstone.Models.User;
import com.example.phoebemanning.capstone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserSettingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    EditText weight;
    EditText heightFt;
    EditText heightIn;
    EditText age;
    Button submitBtn;
    Double exerciseFactor;
    String gender;
    
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
            Integer bmr = getBasalMetabolicRate(gender, weightVal, heightFtVal, heightInVal, ageVal);
            saveValInDatabase(bmr);
        }
    }

//    Men	BMR = 88.362 + (13.397 x weight in kg) + (4.799 x height in cm) - (5.677 x age in years)
//    Women BMR = 447.593 + (9.247 x weight in kg) + (3.098 x height in cm) - (4.330 x age in years)

    private int getBasalMetabolicRate(String gender, Double weightVal, Double heightFtVal, Double heightInVal, Integer ageVal) {
        Double weightKg = weightVal * 0.45359;
        Double totalFeet = heightFtVal + (heightInVal * 0.0833);
        Double heightCm = totalFeet * 30.48;
        Double BMR;

        if(gender == "female"){
            BMR = (447.593 + (9.247 * weightKg) + (3.098 * heightCm) - (4.330 * ageVal)) * exerciseFactor;
        } else {
            BMR = (88.362 + (13.397 * weightKg) + (4.799 * heightCm) - (5.677 * ageVal)) * exerciseFactor;
        }
        return (int) Math.rint(BMR);
    }

    private void saveValInDatabase(Integer bmr) {
        final String BMR = Integer.toString(bmr);

        if(mUser != null){
            String uid = mUser.getUid();
            final DatabaseReference updateDbRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("dailyCalAmount");
            updateDbRef.setValue(BMR);
            Toast.makeText(this, "Your new Daily Value is " + BMR, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(UserSettingsActivity.this, ProfileActivity.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setTitle("");
        setContentView(R.layout.activity_user_settings);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        weight = findViewById(R.id.weight);
        heightFt = findViewById(R.id.heightFt);
        heightIn = findViewById(R.id.heightIn);
        age = findViewById(R.id.age);
        submitBtn = findViewById(R.id.submit);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        String current_id = mUser.getUid();
        DatabaseReference databaseReference = database.getReference().child("Users").child(current_id);

        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                gender = user.getGender();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("Database error: ", databaseError.getMessage());
//                Toast.makeText(ProductActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

//      Dropdown menu
        Spinner spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.exercise_amount,
                android.R.layout.simple_spinner_item);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
        if(pos == 0){
            exerciseFactor = 1.0;
        } else if(pos == 1){
            exerciseFactor = 1.2;
        } else if(pos == 2){
            exerciseFactor = 1.375;
        } else if(pos == 3){
            exerciseFactor = 1.55;
        } else if(pos == 4){
            exerciseFactor = 1.725;
        } else if(pos == 5){
            exerciseFactor = 1.9;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_settings_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_logout:
                if(mUser !=null && mAuth != null){
                    mAuth.signOut();
                    startActivity(new Intent(UserSettingsActivity.this, LoginActivity.class));
                    finish();
                }
                return true;

            case R.id.action_user_scans:
                startActivity(new Intent(UserSettingsActivity.this, UserScansActivity.class));
                finish();
                return true;

            case R.id.action_user_profile:
                startActivity(new Intent(UserSettingsActivity.this, ProfileActivity.class));
                finish();
                return true;

            case R.id.action_information:
                startActivity(new Intent(UserSettingsActivity.this, InfoActivity.class));
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
