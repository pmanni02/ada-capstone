package com.example.phoebemanning.capstone.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phoebemanning.capstone.DownloadImage;
import com.example.phoebemanning.capstone.ImageApi;
import com.example.phoebemanning.capstone.Models.Image_Models.ImageData;
import com.example.phoebemanning.capstone.Models.Image_Models.Items;
import com.example.phoebemanning.capstone.Models.Nutrient_Models.Food;
import com.example.phoebemanning.capstone.Models.Nutrient_Models.Foods;
import com.example.phoebemanning.capstone.Models.Nutrient_Models.NutrientData;
import com.example.phoebemanning.capstone.Models.Nutrient_Models.Nutrients;
import com.example.phoebemanning.capstone.R;
import com.example.phoebemanning.capstone.UsdaApi;

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

    TextView productName;
    String intentStringNdbno;
    String intentStringUpc;
    ListView listView;
    ArrayList<String> myArray;
    ImageView imageView;

    public String baseUrl = null;

    public String firstImg;

    public String getFirstImg() {
        return firstImg;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        productName = findViewById(R.id.productName);
        listView = findViewById(R.id.listView);
        myArray = new ArrayList<String>();
        imageView = findViewById(R.id.imageView);

//      Get data from main activity
        Intent intent = getIntent();
        String intentStringName = intent.getStringExtra("name");
        productName.setText(intentStringName);
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
                Log.i("URL", call.request().url().toString());

                if(response.isSuccessful()){
//                    Log.i("onResponse", "Call is successful");
                    if(response.body() != null){
//                        Log.i("onResponse", "Body NOT NULL");

                        Items[] items = response.body().getItems();
                        String [] images = items[0].getImages();
                        String testImg = images[0];
                        baseUrl = testImg;


                        Log.i("onResponse", images[0]);

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
//                    String error = response.body().getError().toString();
                    Log.i("onResponse", "Image API call failed");
                    imageView.setImageResource(R.drawable.default_img);
//                    Toast.makeText(ProductActivity.this, "U", Toast.LENGTH_SHORT).show();
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
//                    Log.i("Response", "Nutrient Api Call Success");
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
            if(id == 208 || id == 269 || id == 307 || id == 606){
                myArray.add(name);
            }
        }

        //Array Adapter
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ProductActivity.this, android.R.layout.simple_expandable_list_item_1, myArray);
        listView.setAdapter(arrayAdapter);
    }

}

