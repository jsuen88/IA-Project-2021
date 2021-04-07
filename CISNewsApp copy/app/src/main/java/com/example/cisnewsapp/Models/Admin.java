package com.example.cisnewsapp.Models;

import java.util.ArrayList;

public class Admin extends User {
    private int approvedPosts;
    private int deniedPosts;

    public Admin(String name, String uid, String userType, String email, int numPosts, ArrayList<String> createdPosts, int approvedPosts, int deniedPosts) {
        super(name, uid, userType, email, numPosts, createdPosts);
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
