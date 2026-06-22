package com.myhindlab.abkat.models;

public class CampCalenderDatesModel {

    private String day;
    private String month;
    private String year;
    private String date;
    private int campCount;


//    public CampCalenderDatesModel(String day, String month, String year, String date, String campCount) {
//        this.day = day;
//        this.month = month;
//        this.year = year;
//        this.date = date;
//        this.campCount = campCount;
//    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCampCount() {
        return campCount;
    }

    public void setCampCount(int campCount) {
        this.campCount = campCount;
    }
}
