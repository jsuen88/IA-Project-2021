package com.example.cisnewsapp.Models;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class User {
    private String name;
    private String uid;
    private String userType;
    private String email;
    private int numPosts;
    ArrayList<String> createdPosts;
    ArrayList<String> seenPosts;
    String currentlyViewing;
    ArrayList<String> starredPosts;

    public User(String name, String uid, String userType, String email, int numPosts, ArrayList<String> createdPosts, ArrayList<String> seenPosts, String currentlyViewing, ArrayList<String> starredPosts) {
        this.name = name;
        this.uid = uid;
        this.userType = userType;
        this.email = email;
        this.numPosts = numPosts;
        this.createdPosts = createdPosts;
        this.seenPosts = seenPosts;
        this.currentlyViewing = currentlyViewing;
        this.starredPosts = starredPosts;
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

    public String getCurrentlyViewing() {
        return currentlyViewing;
    }

    public void setCurrentlyViewing(String currentlyViewing) {
        this.currentlyViewing = currentlyViewing;
    }

    public ArrayList<String> getStarredPosts() {
        return starredPosts;
    }

    public void setStarredPosts(ArrayList<String> starredPosts) {
        this.starredPosts = starredPosts;
    }
}
