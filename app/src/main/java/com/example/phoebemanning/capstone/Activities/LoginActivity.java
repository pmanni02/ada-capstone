package com.example.phoebemanning.capstone.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.phoebemanning.capstone.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    //    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
//    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mUser;

    private EditText email;
    private EditText password;
    private ProgressBar loginProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference databaseReference = database.getReference("Users");
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginProgress = findViewById(R.id.loginProgress);

        FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mUser = firebaseAuth.getCurrentUser();
                if(mUser !=null){
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }
            }
        };
    }

    public void loginOnClick(View view){

        final String emailString = email.getText().toString();
        final String passwordString = password.getText().toString();

        if(!emailString.equals("") && !passwordString.equals("")){
            loginProgress.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(emailString, passwordString)
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>(){
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                String exception = task.getException().getMessage();
                                Toast.makeText(LoginActivity.this, exception, Toast.LENGTH_SHORT).show();
                            } else {
                                if(!emailString.equals("") || !passwordString.equals("")){
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Username and password cannot be empty", Toast.LENGTH_SHORT).show();
                                }
                            }

                            loginProgress.setVisibility(View.INVISIBLE);
                        }
                    });
        }
    }

    public void createAccountClick(View view){
        Intent intent = new Intent(LoginActivity.this, NewUserActivity.class);
        startActivity(intent);
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        if(item.getItemId() == R.id.action_logout){
//            mAuth.signOut();
//            startActivity(new Intent(LoginActivity.this, MainActivity.class));
//            finish();
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        getMenuInflater().inflate(R.menu.main_menu, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
}
