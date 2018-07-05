package com.example.phoebemanning.capstone.Models.Nutrient_Models;

public class Measures {

//    private String eqv;

    private String eunit;

    private String value;

//    private String qty;
//
//    private String label;

//    public String getEqv ()
//    {
//        return eqv;
//    }
//
//    public void setEqv (String eqv)
//    {
//        this.eqv = eqv;
//    }

    public String getEunit ()
    {
        return eunit;
    }

    public void setEunit (String eunit)
    {
        this.eunit = eunit;
    }

    public String getValue ()
    {
        return value;
    }

    public void setValue (String value)
    {
        this.value = value;
    }

//    public String getQty ()
//    {
//        return qty;
//    }
//
//    public void setQty (String qty)
//    {
//        this.qty = qty;
//    }
//
//    public String getLabel ()
//    {
//        return label;
//    }
//
//    public void setLabel (String label)
//    {
//        this.label = label;
//    }

    @Override
    public String toString()
    {
        return "ClassPojo [eunit = "+eunit+", value = "+value+"]";
    }

}
