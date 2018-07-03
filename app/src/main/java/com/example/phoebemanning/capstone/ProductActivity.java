package com.example.phoebemanning.capstone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ProductActivity extends AppCompatActivity {

    TextView productName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        productName = findViewById(R.id.productName);

        Intent intent = getIntent();
        String intentStringName = intent.getStringExtra("name");
        productName.setText(intentStringName);
        String intentStringUpc = intent.getStringExtra("upc");
        String intentStringNdbno = intent.getStringExtra("ndbno");
    }
}
