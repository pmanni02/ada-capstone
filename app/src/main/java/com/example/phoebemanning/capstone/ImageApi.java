package com.example.phoebemanning.capstone;

import android.content.Context;
import android.provider.SyncStateContract;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.provider.Settings.Global.getString;

public class ImageApi {

    private static Retrofit retrofit = null;

    public static ImageApiInterface getClient() {

        // change your base URL
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.upcitemdb.com/prod/trial/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        //Creating object for our interface
        ImageApiInterface api = retrofit.create(ImageApiInterface.class);
        return api; // return the APIInterface object
    }
}
