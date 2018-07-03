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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class MainActivity extends AppCompatActivity {

    EditText upcEditText;
//    UsdaApi api;

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

//            Call<ResponseData> call = UsdaApi.getClient().getResponse();
            Call<ResponseData> call = UsdaApi.getClient().getResponse(upcEditText.getText().toString(),"q6qvjjZIr3O49ztYwWN61onoR0t0XxdZziUMOkzn");

            call.enqueue(new Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    Log.i("Response", "Success");

                    if (response.isSuccessful()){
                        if (response.body() != null){
                            List list = response.body().getList();
                            java.util.List<Item> item = list.getItem();

                            Log.i("NAME", item.get(0).getName());
                            Log.i("NDBNO", item.get(0).getNdbno());
                        }
                    }

                }

                @Override
                public void onFailure(Call<ResponseData> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        upcEditText = findViewById(R.id.upcEditText);
//        api = UsdaApi.getClient().create(UsdaApi.class);
    }
}
