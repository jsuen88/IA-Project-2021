package com.example.cisnewsapp.Models;

import java.util.ArrayList;

public class CCAPost extends Post{
    ArrayList<Integer> yearGroups;
    private String day;

    public CCAPost(String postName, String postCategory, String postCreator, String info, String postDate, String lastsUntil, ArrayList<Integer> yearGroups, String day) {
        super(postName, postCategory, postCreator, info, postDate, lastsUntil);
        this.yearGroups = yearGroups;
        this.day = day;
    }

    public CCAPost(ArrayList<Integer> yearGroups, String day) {
        this.yearGroups = yearGroups;
        this.day = day;
    }

    public ArrayList<Integer> getYearGroups() {
        return yearGroups;
    }

    public void setYearGroups(ArrayList<Integer> yearGroups) {
        this.yearGroups = yearGroups;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
