package com.example.phoebemanning.capstone;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UsdaApi {

    private static Retrofit retrofit = null;
    public static UsdaApiInterface getClient() {

        // change your base URL
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.nal.usda.gov/ndb/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        //Creating object for our interface
        UsdaApiInterface api = retrofit.create(UsdaApiInterface.class);
        return api; // return the APIInterface object
    }
}
