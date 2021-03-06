package com.example.cisnewsapp.Models;

import java.util.ArrayList;
import java.util.Date;

public class Teacher extends User{
    private String subject;

    public Teacher(String name, String uid, String userType, String email, int numPosts, ArrayList<String> createdPosts, ArrayList<String> seenPosts, String currentlyViewing, ArrayList<String> starredPosts, Date lastVisit, int currentStreak, int longestStreak, Date accountCreated, String subject) {
        super(name, uid, userType, email, numPosts, createdPosts, seenPosts, currentlyViewing, starredPosts, lastVisit, currentStreak, longestStreak, accountCreated);
        this.subject = subject;
    }

    public Teacher(String subject) {
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
