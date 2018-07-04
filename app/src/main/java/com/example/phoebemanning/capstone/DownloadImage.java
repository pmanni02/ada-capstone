package com.example.phoebemanning.capstone;

import android.provider.SyncStateContract;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DownloadImage {

    private static Retrofit retrofit = null;
    public static DownloadImageInterface getClient() {

        // change your base URL
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        //Creating object for our interface
        DownloadImageInterface api = retrofit.create(DownloadImageInterface.class);
        return api; // return the APIInterface object
    }

}
