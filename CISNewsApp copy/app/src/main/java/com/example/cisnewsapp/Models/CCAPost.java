package com.example.cisnewsapp.Models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class CCAPost extends Post{
    ArrayList<Integer> yearGroups;
    private String day;

    public CCAPost(String postName, String postCategory, String postCreator, String info, Date postDate, Date lastsUntil, ArrayList<Integer> yearGroups, String day, String id, String approvalStatus, String picURL) {
        super(postName, postCategory, postCreator, info, postDate, lastsUntil, id, approvalStatus, picURL);
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
