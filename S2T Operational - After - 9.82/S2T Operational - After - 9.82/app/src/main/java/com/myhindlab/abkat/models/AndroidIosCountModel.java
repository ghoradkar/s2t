package com.myhindlab.abkat.models;



import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AndroidIosCountModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("path")
    @Expose
    private String path;
    @SerializedName("dateTime")
    @Expose
    private String dateTime;
    @SerializedName("details")
    @Expose
    private Details details;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public Details getDetails() {
        return details;
    }

    public void setDetails(Details details) {
        this.details = details;
    }


    public class Count {

        @SerializedName("iOS")
        @Expose
        private Integer iOS;
        @SerializedName("Android")
        @Expose
        private Integer android;

        public Integer getiOS() {
            return iOS;
        }

        public void setiOS(Integer iOS) {
            this.iOS = iOS;
        }

        public Integer getAndroid() {
            return android;
        }

        public void setAndroid(Integer android) {
            this.android = android;
        }

    }

    public class Details {

        @SerializedName("count")
        @Expose
        private List<Count> count;

        public List<Count> getCount() {
            return count;
        }

        public void setCount(List<Count> count) {
            this.count = count;
        }

    }

}