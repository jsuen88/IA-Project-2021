package com.example.cisnewsapp.Models;

import java.util.ArrayList;

public class Student extends User{
    private String yearGroup;

    public Student(String name, String uid, String userType, String email, int numPosts, ArrayList<String> createdPosts, String yearGroup) {
        super(name, uid, userType, email, numPosts, createdPosts);
        this.yearGroup = yearGroup;
    }

    public Student(String yearGroup) {
        this.yearGroup = yearGroup;
    }

    public String getYearGroup() {
        return yearGroup;
    }

    public void setYearGroup(String yearGroup) {
        this.yearGroup = yearGroup;
    }
}
