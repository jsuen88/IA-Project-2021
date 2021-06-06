package com.example.cisnewsapp.Models;

import java.util.ArrayList;
import java.util.Date;

public class Admin extends User {
    private int approvedPosts;
    private int deniedPosts;

    public Admin(String name, String uid, String userType, String email, int numPosts, ArrayList<String> createdPosts, int approvedPosts, int deniedPosts, ArrayList<String> seenPosts, String currentlyViewing, ArrayList<String> starredPosts, Date lastVisit, int currentStreak, int longestStreak) {
        super(name, uid, userType, email, numPosts, createdPosts, seenPosts, currentlyViewing, starredPosts, lastVisit, currentStreak, longestStreak);
        this.approvedPosts = approvedPosts;
        this.deniedPosts = deniedPosts;
    }

    public Admin(int approvedPosts, int deniedPosts) {
        this.approvedPosts = approvedPosts;
        this.deniedPosts = deniedPosts;
    }

    public int getApprovedPosts() {
        return approvedPosts;
    }

    public void setApprovedPosts(int approvedPosts) {
        this.approvedPosts = approvedPosts;
    }

    public int getDeniedPosts() {
        return deniedPosts;
    }

    public void setDeniedPosts(int deniedPosts) {
        this.deniedPosts = deniedPosts;
    }
}
