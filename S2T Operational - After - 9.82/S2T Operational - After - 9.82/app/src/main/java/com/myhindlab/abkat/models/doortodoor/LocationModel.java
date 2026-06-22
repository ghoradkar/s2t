package com.myhindlab.abkat.models.doortodoor;

public class LocationModel {
    public String displayName;
    public double lat;
    public double lon;

    public LocationModel(String displayName, double lat, double lon) {
        this.displayName = displayName;
        this.lat = lat;
        this.lon = lon;
    }
}
