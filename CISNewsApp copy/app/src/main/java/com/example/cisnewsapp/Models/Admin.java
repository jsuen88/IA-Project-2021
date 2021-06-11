package com.example.cisnewsapp.Models;

import java.util.ArrayList;
import java.util.Date;

public class Admin extends User {
    public int approvedPosts;
    public int deniedPosts;
    public int processedPosts;

    public Admin(String name, String uid, String userType, String email, int numPosts, ArrayList<String> createdPosts, ArrayList<String> seenPosts, String currentlyViewing, ArrayList<String> starredPosts, Date lastVisit, int currentStreak, int longestStreak, Date accountCreated, int approvedPosts, int deniedPosts, int processedPosts) {
        super(name, uid, userType, email, numPosts, createdPosts, seenPosts, currentlyViewing, starredPosts, lastVisit, currentStreak, longestStreak, accountCreated);
        this.approvedPosts = approvedPosts;
        this.deniedPosts = deniedPosts;
        this.processedPosts = processedPosts;
    }

    public Admin()
    {

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

    public int getProcessedPosts() {
        return processedPosts;
    }

    public void setProcessedPosts(int processedPosts) {
        this.processedPosts = processedPosts;
    }
}
