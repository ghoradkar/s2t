package com.myhindlab.abkat.models;

import java.util.List;

public class CampNestedListModel {

    String district;
    String date;
    Long regWorker;
    List<NestedCampListModel.Output> campList;
    private boolean isExpanded;

    public CampNestedListModel(String district, String date, Long regWorker, List<NestedCampListModel.Output> campList, boolean isExpanded) {
        this.district = district;
        this.date = date;
        this.regWorker = regWorker;
        this.campList = campList;
        this.isExpanded = isExpanded;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expandable) {
        isExpanded = expandable;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getRegWorker() {
        return regWorker;
    }

    public void setRegWorker(Long regWorker) {
        this.regWorker = regWorker;
    }

    public List<NestedCampListModel.Output> getCampList() {
        return campList;
    }

    public void setCampList(List<NestedCampListModel.Output> campList) {
        this.campList = campList;
    }
}
