package com.example.cisnewsapp.Models;

import java.time.LocalDate;
import java.util.Date;

public class Post {
    private String postName;
    private String postCategory;
    private String postCreator;
    private String info;
    private Date postDate;
    private Date lastsUntil;
    private String id;
    private String approvalStatus;
    private String picURL;
    private String contactEmail;

    public Post(String postName, String postCategory, String postCreator, String info, Date postDate, Date lastsUntil, String id, String approvalStatus, String picURL, String contactEmail) {
        this.postName = postName;
        this.postCategory = postCategory;
        this.postCreator = postCreator;
        this.info = info;
        this.postDate = postDate;
        this.lastsUntil = lastsUntil;
        this.id = id;
        this.approvalStatus = approvalStatus;
        this.picURL = picURL;
        this.contactEmail = contactEmail;
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

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    public Date getLastsUntil() {
        return lastsUntil;
    }

    public void setLastsUntil(Date lastsUntil) {
        this.lastsUntil = lastsUntil;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getPicURL() {
        return picURL;
    }

    public void setPicURL(String picURL) {
        this.picURL = picURL;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }
}
