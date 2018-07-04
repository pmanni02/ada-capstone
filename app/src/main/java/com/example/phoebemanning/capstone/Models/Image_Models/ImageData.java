package com.example.phoebemanning.capstone.Models.Image_Models;

import java.util.Arrays;

public class ImageData {

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
