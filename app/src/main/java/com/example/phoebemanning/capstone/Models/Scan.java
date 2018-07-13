package com.example.phoebemanning.capstone.Models;

public class Scan {

    private String productName;
    private String upcCode;
    private String ndbno;

    public Scan(String productName, String upcCode, String ndbno) {
        this.productName = productName;
        this.upcCode = upcCode;
        this.ndbno = ndbno;
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

    public String getNdbno() {
        return ndbno;
    }

    public void setNdbno(String ndbno) {
        this.ndbno = ndbno;
    }
}
