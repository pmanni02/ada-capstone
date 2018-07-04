package com.example.phoebemanning.capstone.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phoebemanning.capstone.Models.Nutrient_Models.Food;
import com.example.phoebemanning.capstone.Models.Nutrient_Models.Foods;
import com.example.phoebemanning.capstone.Models.Nutrient_Models.NutrientData;
import com.example.phoebemanning.capstone.Models.Nutrient_Models.Nutrients;
import com.example.phoebemanning.capstone.R;
import com.example.phoebemanning.capstone.UsdaApi;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductActivity extends AppCompatActivity {

    TextView productName;
    String intentStringNdbno;
    ListView listView;
    ArrayList<String> myArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        productName = findViewById(R.id.productName);
        listView = findViewById(R.id.listView);
        myArray = new ArrayList<String>();

//      Get data from main activity
        Intent intent = getIntent();
        String intentStringName = intent.getStringExtra("name");
        productName.setText(intentStringName);
        intentStringNdbno = intent.getStringExtra("ndbno");

//      String intentStringUpc = intent.getStringExtra("upc");

//      Make GET request for product image
        getNutrients();
    }

    public void getNutrients() {

        Call<NutrientData> call = UsdaApi.getClient().getNutrients(intentStringNdbno, getResources().getString(R.string.usda_api_key));

        call.enqueue(new Callback<NutrientData>() {
            @Override
            public void onResponse(Call<NutrientData> call, Response<NutrientData> response) {
//                Log.i("URL", call.request().url().toString());

                if(response.isSuccessful()) {
                    Log.i("Response", "Nutrient Api Call Success");
                    if (response.body() != null) {
                        Foods foods = response.body().getFoods().get(0);
                        Food food = foods.getFood();
                        Nutrients [] nutrients = food.getNutrients();

                        setList(nutrients);
//                        Log.i("Nutrient List", nutrients[0].getName());
                    } else {
                        Log.i("ERROR", "ERROR");
                        Toast.makeText(ProductActivity.this, "Zero search results", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.i("Response", "Call FAILED");
                }
            }

            @Override
            public void onFailure(Call<NutrientData> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void setList(Nutrients [] nutrients){

        Integer id;
        String name;

        for(int i=0; i < nutrients.length; i++){
            id = Integer.parseInt(nutrients[i].getNutrient_id());
            name = nutrients[i].getName();
            if(id == 208 || id == 269 || id == 307 || id == 606){
                myArray.add(name);
            }
        }

        //Array Adapter
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ProductActivity.this, android.R.layout.simple_expandable_list_item_1, myArray);
        listView.setAdapter(arrayAdapter);
    }

}

