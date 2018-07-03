package com.example.phoebemanning.capstone;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UsdaApiInterface {

//    String BASE_URL = "https://api.nal.usda.gov/ndb/";

    @GET("search/?format=json&q=000000000000&api_key=q6qvjjZIr3O49ztYwWN61onoR0t0XxdZziUMOkzn")
//    @GET("search/?format=json")
    Call<ResponseData> getResponse(@Query("upc") String upc, @Query("X-Authorization") String apiKey);
//    Call<ResponseData> getResponse();
}
