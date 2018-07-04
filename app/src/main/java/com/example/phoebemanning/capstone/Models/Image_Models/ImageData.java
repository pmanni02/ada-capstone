package com.example.phoebemanning.capstone.Models.Image_Models;

import java.util.Arrays;

public class ImageData {

    @Override
    public String toString() {
//        return "ImageData{" +
////                "error='" + error + '\'' +
////                ", items=" + Arrays.toString(items) +
////                '}';
        return "{error}";
    }

    String error;

    Items[] items;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Items[] getItems ()
    {
        return items;
    }

    public void setItems (Items[] items)
    {
        this.items = items;
    }
}
