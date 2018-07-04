package com.example.phoebemanning.capstone.Models.Search_Models;

import com.example.phoebemanning.capstone.Models.Search_Models.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseData {

    @SerializedName("list")
    @Expose
    List list;

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }
}
