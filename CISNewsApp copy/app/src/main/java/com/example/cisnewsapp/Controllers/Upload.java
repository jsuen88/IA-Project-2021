package com.example.cisnewsapp.Controllers;

public class Upload {
    public String mImageName;
    public String mImageURL;

    public Upload() {

    }

    public Upload(String name, String imageURL) {
        if (name.trim().equals("")) {
            name = "No Name";
        }
        mImageName = name;
        mImageURL = imageURL;

    }

    public void setImageName(String name) {
        mImageName = name;
    }

    public String getName() {
        return mImageName;
    }

    public void setImageURL(String imageURL) {
        mImageURL = imageURL;
    }

    public String getImageURL() {
        return mImageURL;
    }
}

