package com.example.cisnewsapp.Models;

import java.util.ArrayList;

public class SportsPost extends Post {
    ArrayList<Integer> yearGroups;
    private String sport;
    private String level;

    public SportsPost(String postName, String postCategory, String postCreator, String info, String postDate, String lastsUntil, ArrayList<Integer> yearGroups, String sport, String level) {
        super(postName, postCategory, postCreator, info, postDate, lastsUntil);
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
