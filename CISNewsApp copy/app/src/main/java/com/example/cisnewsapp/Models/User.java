package com.example.cisnewsapp.Models;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

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
    Date lastVisit;
    int currentStreak;
    int longestStreak;

    public User(String name, String uid, String userType, String email, int numPosts, ArrayList<String> createdPosts, ArrayList<String> seenPosts, String currentlyViewing, ArrayList<String> starredPosts, Date lastVisit, int currentStreak, int longestStreak) {
        this.name = name;
        this.uid = uid;
        this.userType = userType;
        this.email = email;
        this.numPosts = numPosts;
        this.createdPosts = createdPosts;
        this.seenPosts = seenPosts;
        this.currentlyViewing = currentlyViewing;
        this.starredPosts = starredPosts;
        this.lastVisit = lastVisit;
        this.currentStreak = currentStreak;
        this.longestStreak = longestStreak;
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

    public Date getLastVisit() {
        return lastVisit;
    }

    public void setLastVisit(Date lastVisit) {
        this.lastVisit = lastVisit;
    }

    public int getCurrentStreak() {
        return currentStreak;
    }

    public void setCurrentStreak(int currentStreak) {
        this.currentStreak = currentStreak;
    }

    public int getLongestStreak() {
        return longestStreak;
    }

    public void setLongestStreak(int longestStreak) {
        this.longestStreak = longestStreak;
    }
}
