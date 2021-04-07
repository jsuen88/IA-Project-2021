package com.example.cisnewsapp.Models;

public class Post {
    private String postName;
    private String postCategory;
    private String postCreator;
    private String info;
    private String postDate;
    private String lastsUntil;

    public Post(String postName, String postCategory, String postCreator, String info, String postDate, String lastsUntil) {
        this.postName = postName;
        this.postCategory = postCategory;
        this.postCreator = postCreator;
        this.info = info;
        this.postDate = postDate;
        this.lastsUntil = lastsUntil;
    }

    public Post()
    {
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public String getPostCategory() {
        return postCategory;
    }

    public void setPostCategory(String postCategory) {
        this.postCategory = postCategory;
    }

    public String getPostCreator() {
        return postCreator;
    }

    public void setPostCreator(String postCreator) {
        this.postCreator = postCreator;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getLastsUntil() {
        return lastsUntil;
    }

    public void setLastsUntil(String lastsUntil) {
        this.lastsUntil = lastsUntil;
    }
}
