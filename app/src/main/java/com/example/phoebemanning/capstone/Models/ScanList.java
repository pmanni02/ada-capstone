package com.example.phoebemanning.capstone.Models;

//import com.example.phoebemanning.capstone.Models.Search_Models.Item;

import java.util.List;

public class ScanList {

    public ScanList(List<Scan> scans) {
        this.scans = scans;
    }

    java.util.List<Scan> scans = null;

    public List<Scan> getScans() {
        return scans;
    }

    public void setScans(List<Scan> scans) {
        this.scans = scans;
    }

    public ScanList() {

    }
}
