package com.example.cisnewsapp.Models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class AcademicsPost extends Post{
    ArrayList<Integer> yearGroups;

    public AcademicsPost(String postName, String postCategory, String postCreator, String info, Date postDate, Date lastsUntil, ArrayList<Integer> yearGroups, String id, String approvalStatus) {
        super(postName, postCategory, postCreator, info, postDate, lastsUntil, id, approvalStatus);
        this.yearGroups = yearGroups;
    }

    public AcademicsPost(ArrayList<Integer> yearGroups) {
        this.yearGroups = yearGroups;
    }

    public ArrayList<Integer> getYearGroups() {
        return yearGroups;
    }

    public void setYearGroups(ArrayList<Integer> yearGroups) {
        this.yearGroups = yearGroups;
    }
}
