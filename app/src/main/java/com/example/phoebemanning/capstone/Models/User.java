package com.example.phoebemanning.capstone.Models;

import com.example.phoebemanning.capstone.Models.Search_Models.Item;

import java.util.List;

public class User {
    private String email;
    private String firstName;
    private String lastName;
    private String dailyCalAmount;
    private String gender;

    public User(String email, String firstName, String lastName, String dailyCalAmount, String gender) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.dailyCalAmount = dailyCalAmount;
    }

    public User() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDailyCalAmount() {
        return dailyCalAmount;
    }

    public void setDailyCalAmount(String dailyCalAmount) {
        this.dailyCalAmount = dailyCalAmount;
    }
}
