package com.example.phoebemanning.capstone.Models;

import com.example.phoebemanning.capstone.Models.Search_Models.Item;

import java.util.List;

public class User {
    private String email;
    private String firstName;
    private String lastName;
    private Integer age;
    private Integer weight;
//    private String [] searches;

    public User(String email, String firstName, String lastName, Integer age, Integer weight) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.weight = weight;
//        this.searches = searches;

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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }
//
//    public String[] getSearches() {
//        return searches;
//    }
//
//    public void setSearches(String[] searches) {
//        this.searches = searches;
//    }
}
