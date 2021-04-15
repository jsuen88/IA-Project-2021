package com.example.cisnewsapp.Models;

import java.util.ArrayList;

public class User {
    private String name;
    private String uid;
    private String userType;
    private String email;
    private int numPosts;
    ArrayList<String> createdPosts;
    ArrayList<String> seenPosts;

    public User(String name, String uid, String userType, String email, int numPosts, ArrayList<String> createdPosts, ArrayList<String> seenPosts) {
        this.name = name;
        this.uid = uid;
        this.userType = userType;
        this.email = email;
        this.numPosts = numPosts;
        this.createdPosts = createdPosts;
        this.seenPosts = seenPosts;
    }

    public User()
    {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getNumPosts() {
        return numPosts;
    }

    public void setNumPosts(int numPosts) {
        this.numPosts = numPosts;
    }

    public ArrayList<String> getCreatedPosts() {
        return createdPosts;
    }

    public void setCreatedPosts(ArrayList<String> createdPosts) {
        this.createdPosts = createdPosts;
    }

    public ArrayList<String> getSeenPosts() {
        return seenPosts;
    }

    public void setSeenPosts(ArrayList<String> seenPosts) {
        this.seenPosts = seenPosts;
    }
}
