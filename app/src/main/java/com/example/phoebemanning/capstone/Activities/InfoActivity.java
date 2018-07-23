package com.example.phoebemanning.capstone.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.phoebemanning.capstone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class InfoActivity extends AppCompatActivity {

    private FirebaseUser mUser;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setTitle("");
        setContentView(R.layout.activity_info);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.info_page_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_logout:
                if(mUser !=null && mAuth != null){
                    mAuth.signOut();
                    startActivity(new Intent(InfoActivity.this, LoginActivity.class));
                    finish();
                }
                return true;

            case R.id.action_settings:
                startActivity(new Intent(InfoActivity.this, UserSettingsActivity.class));
                finish();
                return true;

            case R.id.action_user_profile:
                startActivity(new Intent(InfoActivity.this, ProfileActivity.class));
                finish();
                return true;

            case R.id.action_user_scans:
                startActivity(new Intent(InfoActivity.this, UserScansActivity.class));
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
