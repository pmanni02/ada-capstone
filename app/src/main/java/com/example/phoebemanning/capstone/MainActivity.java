package com.example.phoebemanning.capstone;

import android.content.Context;
import android.content.Intent;
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

    public void submitOnClick(View view) {

//        Log.i("UPC TEXT: ", upcEditText.getText().toString());

//      Hide the Keyboard after submit
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(upcEditText.getWindowToken(), 0);

        if (upcEditText.length() != 12){
            Toast.makeText(this, "Invalid UPC", Toast.LENGTH_LONG).show();
        } else {
            getProductCode();
        }
    }

    public void getProductCode(){
        Log.i("UPC TEXT: ", upcEditText.getText().toString());
        Call<ResponseData> call = UsdaApi.getClient().getResponse(upcEditText.getText().toString(), getResources().getString(R.string.usda_api_key));

        call.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                Log.i("Response", "Success");

                if (response.isSuccessful()){
                    if (response.body() != null){

                        List list = response.body().getList();

                        if (list != null){
                            java.util.List<Item> item = list.getItem();
                            String name = item.get(0).getName();
                            String ndbno = item.get(0).getNdbno();

                            Intent intent = new Intent(getApplicationContext(), ProductActivity.class);
                            intent.putExtra("name",name);
                            intent.putExtra("ndbno", ndbno);
                            intent.putExtra("upc", upcEditText.getText().toString());
                            startActivity(intent);

                            Log.i("NAME", item.get(0).getName());
                            Log.i("NDBNO", item.get(0).getNdbno());
                        } else {
                            Toast.makeText(MainActivity.this, "Your search resulted in zero results. Change your parameters and try again", Toast.LENGTH_SHORT).show();
                        }

                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        upcEditText = findViewById(R.id.upcEditText);
    }
}
