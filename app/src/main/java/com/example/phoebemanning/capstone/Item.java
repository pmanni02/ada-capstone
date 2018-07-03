package com.example.phoebemanning.capstone;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
//import com.google.gson.annotations.SerializedName;

public class Item {

    @SerializedName("name")
    @Expose
    String name;

    @SerializedName("ndbno")
    @Expose
    String ndbno;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNdbno() {
        return ndbno;
    }

    public void setNdbno(String ndbno) {
        this.ndbno = ndbno;
    }

    @Override
    public String toString() {
        return name;
    }

}
