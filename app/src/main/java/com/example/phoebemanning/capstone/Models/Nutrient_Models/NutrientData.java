package com.example.phoebemanning.capstone.Models.Nutrient_Models;

import com.example.phoebemanning.capstone.Models.Nutrient_Models.Foods;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NutrientData {

    @SerializedName("foods")
    @Expose
    java.util.List<Foods> foods = null;


    public java.util.List<Foods> getFoods() {
        return foods;
    }

    public void setFoods(java.util.List<Foods> foods) {
        this.foods = foods;
    }

}

