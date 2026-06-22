package com.myhindlab.abkat.models.doortodoor.d2d_camp_activity;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetLocationDetailsByLabCodeResponseModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("output")
    @Expose
    private List<Output> output;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Output> getOutput() {
        return output;
    }

    public void setOutput(List<Output> output) {
        this.output = output;
    }


    public class Output {

        @SerializedName("Latitude")
        @Expose
        private Double latitude;
        @SerializedName("longitude")
        @Expose
        private Double longitude;
        @SerializedName("LocationID")
        @Expose
        private Integer locationID;
        @SerializedName("LabName")
        @Expose
        private String labName;
        @SerializedName("DistanceFrom")
        @Expose
        private float DistanceFrom;


        public float getDistanceFrom() {
            return DistanceFrom;
        }

        public void setDistanceFrom(float distanceFrom) {
            DistanceFrom = distanceFrom;
        }

        public Double getLatitude() {
            return latitude;
        }

        public void setLatitude(Double latitude) {
            this.latitude = latitude;
        }

        public Double getLongitude() {
            return longitude;
        }

        public void setLongitude(Double longitude) {
            this.longitude = longitude;
        }

        public Integer getLocationID() {
            return locationID;
        }

        public void setLocationID(Integer locationID) {
            this.locationID = locationID;
        }

        public String getLabName() {
            return labName;
        }

        public void setLabName(String labName) {
            this.labName = labName;
        }

    }
}
