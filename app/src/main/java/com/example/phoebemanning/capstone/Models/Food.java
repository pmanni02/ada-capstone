package com.example.phoebemanning.capstone.Models;

public class Food {
    private Nutrients[] nutrients;

    public Nutrients[] getNutrients () {
        return nutrients;
    }

    public void setNutrients (Nutrients[] nutrients) {
        this.nutrients = nutrients;
    }

    @Override
    public String toString() {
        return "ClassPojo [nutrients = "+nutrients+"]";
    }
}
