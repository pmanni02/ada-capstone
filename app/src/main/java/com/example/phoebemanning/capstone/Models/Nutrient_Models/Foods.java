package com.example.phoebemanning.capstone.Models.Nutrient_Models;

import com.example.phoebemanning.capstone.Models.Nutrient_Models.Food;
import com.google.gson.annotations.SerializedName;

public class Foods {
    @SerializedName("food")
    Food food;

    public Food getFood () {
        return food;
    }

    public void setFood (Food food) {
        this.food = food;
    }

    @Override
    public String toString() {
        return "ClassPojo [food = "+food+"]";
    }
}
