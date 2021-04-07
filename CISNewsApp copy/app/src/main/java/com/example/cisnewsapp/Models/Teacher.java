package com.example.cisnewsapp.Models;

import java.util.ArrayList;

public class Teacher extends User{
    private String subject;

    public Teacher(String name, String uid, String userType, String email, int numPosts, ArrayList<String> createdPosts, String subject) {
        super(name, uid, userType, email, numPosts, createdPosts);
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