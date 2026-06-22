package com.myhindlab.abkat.activities.payout.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class MonthModel {

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

        @SerializedName("Month_id")
        @Expose
        private Integer Month_id;
        @SerializedName("Month_Name_Eng")
        @Expose
        private String Month_Name_Eng;

        public Integer getMonth_id() {
            return Month_id;
        }

        public void setMonth_id(Integer yearID) {
            this.Month_id = yearID;
        }

        public String getMonth_Name_Eng() {
            return Month_Name_Eng;
        }

        public void setMonth_Name_Eng(String yearName) {
            this.Month_Name_Eng = Month_Name_Eng;
        }

    }

}