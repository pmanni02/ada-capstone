package com.example.phoebemanning.capstone;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText upcEditText;

    public void submitOnClick(View view) {

        Log.i("UPC TEXT: ", upcEditText.getText().toString());

//      Hide the Keyboard after submit
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(upcEditText.getWindowToken(), 0);

        if (upcEditText.length() != 12){
            Toast.makeText(this, "Invalid UPC", Toast.LENGTH_LONG).show();
        } else {
//            UpcSearch task = new UpcSearch();
//            task.execute("https://api.nal.usda.gov/ndb/search/?format=json&q=" + upcEditText.getText().toString() + "&sort=n&max=25&offset=0&api_key=q6qvjjZIr3O49ztYwWN61onoR0t0XxdZziUMOkzn");

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        upcEditText = findViewById(R.id.upcEditText);
    }
}
