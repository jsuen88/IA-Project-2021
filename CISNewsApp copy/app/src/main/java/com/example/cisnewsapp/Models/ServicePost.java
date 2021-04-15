package com.example.cisnewsapp.Models;

public class ServicePost extends Post {
    private boolean cantoneseRequired;
    private String targetDemographic;

    public ServicePost(String postName, String postCategory, String postCreator, String info, String postDate, String lastsUntil, boolean cantoneseRequired, String targetDemographic, String id) {
        super(postName, postCategory, postCreator, info, postDate, lastsUntil, id);
        this.cantoneseRequired = cantoneseRequired;
        this.targetDemographic = targetDemographic;
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
}
