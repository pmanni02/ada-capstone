package com.example.phoebemanning.capstone;

import com.example.phoebemanning.capstone.Models.Search_Models.ResponseData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ImageApiInterface {

    @GET("lookup")
    Call<ResponseData> getResponse(@Query("upc") String upc);

}
