package com.myhindlab.abkat.models;

public class SendCourierToLabNewModel {

    private String campTypeName;
    private String labName;
    private int campTypeId;
    private int labId;

    public String getLabName() {
        return labName;
    }

    public void setLabName(String labName) {
        this.labName = labName;
    }

    public int getLabId() {
        return labId;
    }

    public void setLabId(int labId) {
        this.labId = labId;
    }

    public SendCourierToLabNewModel(String campTypeName, int campTypeId) {
        this.campTypeName = campTypeName;
        this.campTypeId = campTypeId;
    }

    public String getCampTypeName() {
        return campTypeName;
    }

    public void setCampTypeName(String campTypeName) {
        this.campTypeName = campTypeName;
    }

    public int getCampTypeId() {
        return campTypeId;
    }

    public void setCampTypeId(int campTypeId) {
        this.campTypeId = campTypeId;
    }
}
