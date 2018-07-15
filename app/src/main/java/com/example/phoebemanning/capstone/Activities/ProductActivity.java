package com.example.phoebemanning.capstone.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import com.example.phoebemanning.capstone.BuildConfig;
import com.example.phoebemanning.capstone.Models.Image_Models.ImageData;
import com.example.phoebemanning.capstone.Models.Image_Models.Items;
import com.example.phoebemanning.capstone.Models.Nutrient_Models.Food;
import com.example.phoebemanning.capstone.Models.Nutrient_Models.Foods;
import com.example.phoebemanning.capstone.Models.Nutrient_Models.NutrientData;
import com.example.phoebemanning.capstone.Models.Nutrient_Models.Nutrients;
import com.example.phoebemanning.capstone.Models.Scan;
import com.example.phoebemanning.capstone.Models.User;
import com.example.phoebemanning.capstone.R;
import com.example.phoebemanning.capstone.Adapters.RecyclerAdapterNutrients;
import com.example.phoebemanning.capstone.Apis.UsdaApi;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

//    private static final String TAG = "ProductActivity";
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
    RecyclerAdapterNutrients adapter;
    ProgressBar loadProductProgress;
    FloatingActionButton addNewScanButton;
    Menu main_menu;
//    Item favorite;

    @SuppressLint("ResourceType")
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
        addNewScanButton = findViewById(R.id.floatingActionButton);
        main_menu = findViewById(R.menu.main_menu);

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

        addNewScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProductActivity.this, MainActivity.class));
            }
        });

//      add on click action for fav icon
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

        Call<NutrientData> call = UsdaApi.getClient().getNutrients(intentStringNdbno, BuildConfig.ApiKey);

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

        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        String current_id = mUser.getUid();
        DatabaseReference databaseReference = database.getReference().child("Users").child(current_id);

        Integer id;
//        String name;

        for(int i=0; i < nutrients.length; i++){
            id = Integer.parseInt(nutrients[i].getNutrient_id());
//            name = nutrients[i].getName();
            if(id == 208|| id == 204 ||id == 606|| id == 269 || id == 307 ){
                nutrientArray.add(nutrients[i]);
            }
        }

        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                String dailyVal = user.getDailyCalAmount();

                adapter = new RecyclerAdapterNutrients(nutrientArray, dailyVal,ProductActivity.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProductActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_logout:
                if(mUser !=null && mAuth != null){
                    mAuth.signOut();
                    Toast.makeText(this, "Signed Out", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ProductActivity.this, LoginActivity.class));
                    finish();
                }
                return true;

            case R.id.action_user_scans:
                startActivity(new Intent(ProductActivity.this, UserScansActivity.class));
                finish();

            case R.id.action_favorite:
                saveUpc();
                return true;

//            case R.id.action_percent:
//                Toast.makeText(this, "Percent pressed", Toast.LENGTH_SHORT).show();
//                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void saveUpc() {
        final String name = productName.getText().toString();
        final String upcCode = intentStringUpc;
        final String ndbno = intentStringNdbno;

        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        if (current_user != null) {
            String uid = current_user.getUid();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Scans").child(uid);

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ArrayList<Scan> list = new ArrayList<Scan>();

                    for(DataSnapshot s : dataSnapshot.getChildren()){
                        Scan scan = s.getValue(Scan.class);
                        list.add(scan);
                    }

                    int size = list.size();
//                    Log.i("SIZE", String.valueOf(size));
                    if(list.size() == 1 && list.get(0).getProductName().equals("Null")){
                        updateScan(name, upcCode, ndbno);
                        Toast.makeText(ProductActivity.this, "Added to Favorites", Toast.LENGTH_SHORT).show();
                    } else {
                        //check if scan already exists before adding
                        Boolean found = false;
                        for(int i=0; i<size; i++){
                            if(list.get(i).getProductName().equals(name)){
                                found = true;
                                Toast.makeText(ProductActivity.this, "Product Already Saved", Toast.LENGTH_SHORT).show();
                            }
                        }
                        if(!found){
                            addNewScan(name, upcCode, ndbno);
                            Toast.makeText(ProductActivity.this, "Added to Favorites", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(ProductActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void updateScan(final String name, final String upcCode, final String ndbno) {
        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        if(current_user != null){
            String uid = current_user.getUid();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Scans").child(uid);
            databaseReference.removeValue(new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                    if(databaseError == null){
                        addNewScan(name, upcCode, ndbno);
                    } else {
                        Toast.makeText(ProductActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }

    private void addNewScan(String name, String upcCode, String ndbno) {
        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        if (current_user != null) {
            String uid = current_user.getUid();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Scans").child(uid).push();
            Scan newScan = new Scan(name, upcCode, ndbno);
            databaseReference.setValue(newScan);
        }
    }

}

