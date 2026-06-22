package com.myhindlab.abkat.models;

public class InsertHealthHistoryModel {
    private String PE_TestID;
    private String TestStatus;
    private String Since;
    private String SinceYear;
    private String SinceMonth;
    private String Description;

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getSinceYear() {
        return SinceYear;
    }

    public void setSinceYear(String sinceYear) {
        SinceYear = sinceYear;
    }

    public String getSinceMonth() {
        return SinceMonth;
    }

    public void setSinceMonth(String sinceMonth) {
        SinceMonth = sinceMonth;
    }

    public String getPE_TestID() {
        return PE_TestID;
    }

    public void setPE_TestID(String PE_TestID) {
        this.PE_TestID = PE_TestID;
    }

    public String getTestStatus() {
        return TestStatus;
    }

    public void setTestStatus(String testStatus) {
        TestStatus = testStatus;
    }

    public String getSince() {
        return Since;
    }

    public void setSince(String since) {
        Since = since;
    }
}
