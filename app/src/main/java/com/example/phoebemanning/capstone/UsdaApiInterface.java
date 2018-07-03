package com.example.phoebemanning.capstone;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UsdaApiInterface {

    @GET("search/?format=json")
    Call<ResponseData> getResponse(@Query("q") String upc, @Query("api_key") String apiKey);

}
