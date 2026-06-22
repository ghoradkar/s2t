package com.myhindlab.abkat.models;

public class InsertHealthHistoryNewModel {
    private String Regdid;
    private String PhyExamTypeID;
    private String PhyExamStatus;
    private String Year;
    private String Month;
    private String Description;

    public String getRegdid() {
        return Regdid;
    }

    public void setRegdid(String regdid) {
        Regdid = regdid;
    }

    public String getPhyExamTypeID() {
        return PhyExamTypeID;
    }

    public void setPhyExamTypeID(String phyExamTypeID) {
        PhyExamTypeID = phyExamTypeID;
    }

    public String getPhyExamStatus() {
        return PhyExamStatus;
    }

    public void setPhyExamStatus(String phyExamStatus) {
        PhyExamStatus = phyExamStatus;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }

    public String getMonth() {
        return Month;
    }

    public void setMonth(String month) {
        Month = month;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
