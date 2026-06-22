package com.myhindlab.abkat.models;

public class CampTypeModel {

    private String campTypeName;
    private int campTypeId;

    public CampTypeModel(String campTypeName, int campTypeId) {
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
