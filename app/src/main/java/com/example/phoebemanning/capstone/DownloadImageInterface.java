package com.example.phoebemanning.capstone;

import com.example.phoebemanning.capstone.Models.Image_Models.ImageData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DownloadImageInterface {

    @GET("{input}")
    Call<ImageData> getImage(@Path("input") String input);

}
