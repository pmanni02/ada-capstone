package com.example.phoebemanning.capstone.Apis;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
