package com.example.cisnewsapp.Models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class SportsPost extends Post {
    ArrayList<Integer> yearGroups;
    private String sport;
    private String level;

    public SportsPost(String postName, String postCategory, String postCreator, String info, Date postDate, Date lastsUntil, ArrayList<Integer> yearGroups, String sport, String level, String id, String approvalStatus, String picURL) {
        super(postName, postCategory, postCreator, info, postDate, lastsUntil, id, approvalStatus, picURL);
        this.yearGroups = yearGroups;
        this.sport = sport;
        this.level = level;
    }

    public SportsPost(ArrayList<Integer> yearGroups, String sport, String level) {
        this.yearGroups = yearGroups;
        this.sport = sport;
        this.level = level;
    }

    public ArrayList<Integer> getYearGroups() {
        return yearGroups;
    }

    public void setYearGroups(ArrayList<Integer> yearGroups) {
        this.yearGroups = yearGroups;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
