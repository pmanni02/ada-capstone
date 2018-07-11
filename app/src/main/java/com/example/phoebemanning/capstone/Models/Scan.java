package com.example.phoebemanning.capstone.Models;

public class Scan {

    private String productName;
    private String upcCode;

    public Scan(String productName, String upcCode) {
        this.productName = productName;
        this.upcCode = upcCode;
    }

    public Scan() {
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getUpcCode() {
        return upcCode;
    }

    public void setUpcCode(String upcCode) {
        this.upcCode = upcCode;
    }
}
