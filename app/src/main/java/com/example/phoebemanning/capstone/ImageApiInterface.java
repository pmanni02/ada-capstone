package com.example.phoebemanning.capstone;

import android.graphics.Bitmap;

import com.example.phoebemanning.capstone.Models.Image_Models.ImageData;
import com.example.phoebemanning.capstone.Models.Search_Models.ResponseData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ImageApiInterface {

    @GET("lookup")
    Call<ImageData> getImage(@Query("upc") String upc);

}
