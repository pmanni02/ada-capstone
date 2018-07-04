package com.example.phoebemanning.capstone;

import android.util.Log;

import com.example.phoebemanning.capstone.Models.NutrientData;
import com.example.phoebemanning.capstone.Models.ResponseData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UsdaApiInterface {

    @GET("search/?format=json")
    Call<ResponseData> getResponse(@Query("q") String upc, @Query("api_key") String apiKey);

//  V2/reports?ndbno=45309551&type=f&format=json&api_key=q6qvjjZIr3O49ztYwWN61onoR0t0XxdZziUMOkzn
    @GET("V2/reports?type=f&format=json")
    Call<NutrientData> getNutrients(@Query("ndbno") String ndbno, @Query("api_key") String apiKey);

}
