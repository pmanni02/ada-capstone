package com.example.phoebemanning.capstone.Models.Nutrient_Models;

public class Nutrients {
//    private String unit;
//    private String derivation;

    private String name;
    private String nutrient_id;
    private String value;
    private Measures[] measures;

//    private String group;

//    public String getUnit ()
//    {
//        return unit;
//    }
//
//    public void setUnit (String unit)
//    {
//        this.unit = unit;
//    }
//
//    public String getDerivation ()
//    {
//        return derivation;
//    }
//
//    public void setDerivation (String derivation)
//    {
//        this.derivation = derivation;
//    }

    public String getName () {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

    public String getNutrient_id () {
        return nutrient_id;
    }

    public void setNutrient_id (String nutrient_id) {
        this.nutrient_id = nutrient_id;
    }

//    public String getValue ()
//    {
//        return value;
//    }
//
//    public void setValue (String value)
//    {
//        this.value = value;
//    }

    public Measures[] getMeasures () {
        return measures;
    }

    public void setMeasures (Measures[] measures) {
        this.measures = measures;
    }

//    public String getGroup ()
//    {
//        return group;
//    }
//
//    public void setGroup (String group)
//    {
//        this.group = group;
//    }

//    TODO: add measures class and add to recyclerView

    @Override
    public String toString() {
        return "ClassPojo [name = "+name+", nutrient_id = "+nutrient_id+"]";
    }
}
