package com.myhindlab.abkat.utilities;

import android.location.Address;

import com.google.android.gms.maps.model.LatLng;

public class ConstantData {

    private ConstantData() {
    }

    public static ConstantData _instance;

    public static ConstantData getInstance() {
        if (_instance == null) {
            _instance = new ConstantData();
        }
        return _instance;
    }

    // --- Application Constant Data

    private String setSitetypeid;
    private String setSitetypeName;

    private LatLng latLng;
    private Address address;
    private Class<?> className;


    public Class<?> getClassName() {
        return className;
    }

    public void setClassName(Class<?> className) {
        this.className = className;
    }

    public String getSetSitetypeid() {
        return setSitetypeid;
    }

    public void setSetSitetypeid(String setSitetypeid) {
        this.setSitetypeid = setSitetypeid;
    }

    public String getSetSitetypeName() {
        return setSitetypeName;
    }

    public void setSetSitetypeName(String setSitetypeName) {
        this.setSitetypeName = setSitetypeName;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

//  -------------------------------------------------------------------------------------------------------------------------

    private String calendarCampId;
    private int campType;

    public String getCalendarCampId() {
        return calendarCampId;
    }

    public void setCalendarCampId(String calendarCampId) {
        this.calendarCampId = calendarCampId;
    }

    public int getCampType() {
        return campType;
    }

    public void setCampType(int campType) {
        this.campType = campType;
    }
}