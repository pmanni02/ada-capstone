package com.example.phoebemanning.capstone.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phoebemanning.capstone.Adapters.RecyclerAdapterScans;
import com.example.phoebemanning.capstone.Models.Scan;
import com.example.phoebemanning.capstone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserScansActivity extends AppCompatActivity {

    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    Button scanNew;
    RecyclerView recyclerViewScans;
    RecyclerAdapterScans adapter;
    LinearLayoutManager layoutManager;
    ProgressBar loadScansProgress;
    ArrayList<Scan> list = new ArrayList<>();
    TextView noScans;

    public void scanNewBtnClick(View view){
        startActivity(new Intent(UserScansActivity.this, MainActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setTitle("");
        setContentView(R.layout.activity_user_scans);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        scanNew = findViewById(R.id.scanNewBtn);
        recyclerViewScans = findViewById(R.id.recyclerViewScans);
        noScans = findViewById(R.id.noScansText);

        recyclerViewScans.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerViewScans.setLayoutManager(layoutManager);

        loadScansProgress = findViewById(R.id.loadScansProgress);
        loadScansProgress.setVisibility(View.VISIBLE);

        getScanList();
    }

    private void getScanList() {
        if(mUser != null){
            String uid = mUser.getUid();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Scans").child(uid);

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for(DataSnapshot s : dataSnapshot.getChildren()){
                        Scan scan = s.getValue(Scan.class);
                        list.add(scan);
                    }
                    loadScansProgress.setVisibility(View.INVISIBLE);
                    setScanList(list);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.i("Database error: ", databaseError.getMessage());
//                    Toast.makeText(UserScansActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void setScanList(ArrayList<Scan> list) {
        if(list.get(0).getProductName().equals("Null")){
            noScans.setVisibility(View.VISIBLE);
        } else {
            noScans.setVisibility(View.INVISIBLE);
            adapter = new RecyclerAdapterScans(list, UserScansActivity.this);
            recyclerViewScans.setAdapter(adapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.saved_scans_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_logout:
                if(mUser !=null && mAuth != null){
                    mAuth.signOut();
                    startActivity(new Intent(UserScansActivity.this, LoginActivity.class));
                    finish();
                }
                return true;

            case R.id.action_settings:
                startActivity(new Intent(UserScansActivity.this, UserSettingsActivity.class));
                finish();
                return true;

            case R.id.action_user_profile:
                startActivity(new Intent(UserScansActivity.this, ProfileActivity.class));
                finish();
                return true;

            case R.id.action_information:
                startActivity(new Intent(UserScansActivity.this, InfoActivity.class));
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
