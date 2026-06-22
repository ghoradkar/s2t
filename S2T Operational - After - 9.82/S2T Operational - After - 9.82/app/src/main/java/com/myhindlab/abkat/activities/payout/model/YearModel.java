package com.myhindlab.abkat.activities.payout.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class YearModel {

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

        @SerializedName("YearID")
        @Expose
        private Integer yearID;
        @SerializedName("Year_Name")
        @Expose
        private String yearName;

        public Integer getYearID() {
            return yearID;
        }

        public void setYearID(Integer yearID) {
            this.yearID = yearID;
        }

        public String getYearName() {
            return yearName;
        }

        public void setYearName(String yearName) {
            this.yearName = yearName;
        }

    }

}