package com.example.cisnewsapp.Models;

import java.util.ArrayList;
import java.util.Date;

public class Student extends User{
    private String yearGroup;

    public Student(String name, String uid, String userType, String email, int numPosts, ArrayList<String> createdPosts, String yearGroup, ArrayList<String> seenPosts, String currentlyViewing, ArrayList<String> starredPosts, Date lastVisit, int currentStreak, int longestStreak) {
        super(name, uid, userType, email, numPosts, createdPosts, seenPosts, currentlyViewing, starredPosts, lastVisit, currentStreak, longestStreak);
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
