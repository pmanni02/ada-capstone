package com.example.phoebemanning.capstone.Models.Search_Models;

import com.example.phoebemanning.capstone.Models.Search_Models.Item;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class List<T> {

    @SerializedName("item")
    @Expose
    java.util.List<Item> item = null;

    public java.util.List<Item> getItem() {
        return item;
    }

    public void setItem(java.util.List<Item> item) {
        this.item = item;
    }

}
