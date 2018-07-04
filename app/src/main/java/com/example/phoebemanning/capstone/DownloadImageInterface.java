package com.example.phoebemanning.capstone;

import com.example.phoebemanning.capstone.Models.Image_Models.ImageData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface DownloadImageInterface {

//    @GET
//    Call<String> getImage(@Url String url);

    @GET("/")
    Call<String> getImage(@Url String url);

}
