package com.example.cisnewsapp.Models;

import java.time.LocalDate;
import java.util.Date;

public class ServicePost extends Post {
    private boolean cantoneseRequired;
    private String targetDemographic;
    private String day;

    public ServicePost(String postName, String postCategory, String postCreator, String info, Date postDate, Date lastsUntil, boolean cantoneseRequired, String targetDemographic, String id, String approvalStatus, String picURL, String day, String contactEmail) {
        super(postName, postCategory, postCreator, info, postDate, lastsUntil, id, approvalStatus, picURL, contactEmail);
        this.cantoneseRequired = cantoneseRequired;
        this.targetDemographic = targetDemographic;
        this.day = day;
    }

    public ServicePost() {
    }

    public ServicePost(boolean cantoneseRequired, String targetDemographic) {
        this.cantoneseRequired = cantoneseRequired;
        this.targetDemographic = targetDemographic;
    }

    public boolean isCantoneseRequired() {
        return cantoneseRequired;
    }

    public void setCantoneseRequired(boolean cantoneseRequired) {
        this.cantoneseRequired = cantoneseRequired;
    }

    public String getTargetDemographic() {
        return targetDemographic;
    }

    public void setTargetDemographic(String targetDemographic) {
        this.targetDemographic = targetDemographic;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
