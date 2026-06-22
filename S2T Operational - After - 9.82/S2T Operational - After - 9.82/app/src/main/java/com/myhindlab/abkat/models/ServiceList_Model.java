package com.myhindlab.abkat.models;

public class ServiceList_Model {

    private int icon;
    private String title;
    private String description;
    private String activityname;

    public ServiceList_Model(int icon, String title, String description, String activityname) {
        this.icon = icon;
        this.title = title;
        this.description = description;
        this.activityname = activityname;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getActivityname() {
        return activityname;
    }

    public void setActivityname(String activityname) {
        this.activityname = activityname;
    }
}
