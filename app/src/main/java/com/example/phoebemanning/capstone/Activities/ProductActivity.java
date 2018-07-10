package com.example.phoebemanning.capstone.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phoebemanning.capstone.Apis.ImageApi;
import com.example.phoebemanning.capstone.Models.Image_Models.ImageData;
import com.example.phoebemanning.capstone.Models.Image_Models.Items;
import com.example.phoebemanning.capstone.Models.Nutrient_Models.Food;
import com.example.phoebemanning.capstone.Models.Nutrient_Models.Foods;
import com.example.phoebemanning.capstone.Models.Nutrient_Models.NutrientData;
import com.example.phoebemanning.capstone.Models.Nutrient_Models.Nutrients;
import com.example.phoebemanning.capstone.R;
import com.example.phoebemanning.capstone.Adapters.RecyclerAdapter;
import com.example.phoebemanning.capstone.Apis.UsdaApi;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductActivity extends AppCompatActivity {

    private FirebaseUser mUser;
    private FirebaseAuth mAuth;

    TextView productName;
    String intentStringNdbno;
    String intentStringUpc;
    ImageView imageView;
    ArrayList<Nutrients> nutrientArray;

    public String baseUrl = null;

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    RecyclerAdapter adapter;
    ProgressBar loadProductProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        loadProductProgress = findViewById(R.id.loadProductProgress);
        loadProductProgress.setVisibility(View.VISIBLE);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        productName = findViewById(R.id.productName);
        nutrientArray = new ArrayList<Nutrients>();
        imageView = findViewById(R.id.imageView);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

//      Get data from main activity
        Intent intent = getIntent();
        String intentStringName = intent.getStringExtra("name");
        String[] splitProductName = intentStringName.split(",");
        productName.setText(splitProductName[0]);

        intentStringNdbno = intent.getStringExtra("ndbno");
        intentStringUpc = intent.getStringExtra("upc");

//      Make GET request for product nutrients
        getNutrients();

//      Make GET request for product image
        getImage();
    }

    public void getImage(){

        Call<ImageData> call = ImageApi.getClient().getImage(intentStringUpc);

        call.enqueue(new Callback<ImageData>() {
            @Override
            public void onResponse(Call<ImageData> call, Response<ImageData> response) {

                if(response.isSuccessful()){
                    loadProductProgress.setVisibility(View.INVISIBLE);
                    if(response.body() != null){

                        Items[] items = response.body().getItems();
                        String [] images = items[0].getImages();
                        String testImg = images[0];
                        baseUrl = testImg;

                        ImageDownloader imgTask = new ImageDownloader();
                        Bitmap myImage;

                        try {
                            myImage = imgTask.execute(images).get();
                            imageView.setImageBitmap(myImage);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        Log.i("onResponse", "Body NULL");
                    }
                } else {
                    Log.i("onResponse", "Image API call failed");
                    imageView.setImageResource(R.drawable.default_img);
                    loadProductProgress.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ImageData> call, Throwable t) {
                Toast.makeText(ProductActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });

    }

    public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream inputStream = connection.getInputStream();
//              Convert to Bitmap
                Bitmap myBitmap = BitmapFactory.decodeStream(inputStream);

                return myBitmap;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    public void getNutrients() {

        Call<NutrientData> call = UsdaApi.getClient().getNutrients(intentStringNdbno, getResources().getString(R.string.usda_api_key));

        call.enqueue(new Callback<NutrientData>() {
            @Override
            public void onResponse(Call<NutrientData> call, Response<NutrientData> response) {
//                Log.i("URL", call.request().url().toString());

                if(response.isSuccessful()) {
                    if (response.body() != null) {
                        Foods foods = response.body().getFoods().get(0);
                        Food food = foods.getFood();
                        Nutrients [] nutrients = food.getNutrients();

                        setList(nutrients);
                    } else {
                        Log.i("ERROR", "ERROR");
                        Toast.makeText(ProductActivity.this, "Zero search results", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.i("Response", "Usda API Call FAILED");
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
            if(id == 208|| id == 204 ||id == 606|| id == 269 || id == 307 ){
                nutrientArray.add(nutrients[i]);
            }
        }

//      RecyclerView custom adapter
        adapter = new RecyclerAdapter(nutrientArray, ProductActivity.this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_logout){
            if(mUser !=null && mAuth != null){
                mAuth.signOut();
                Toast.makeText(this, "Signed Out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ProductActivity.this, LoginActivity.class));
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

}

