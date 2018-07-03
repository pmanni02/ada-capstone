package com.example.phoebemanning.capstone;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class List {

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
