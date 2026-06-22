package com.myhindlab.abkat.models.doortodoor;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChannelPartnerModel {

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

        @SerializedName("CatagoryID")
        @Expose
        private Integer catagoryID;
        @SerializedName("CatagoryName")
        @Expose
        private String catagoryName;

        public Integer getCatagoryID() {
            return catagoryID;
        }

        public void setCatagoryID(Integer catagoryID) {
            this.catagoryID = catagoryID;
        }

        public String getCatagoryName() {
            return catagoryName;
        }

        public void setCatagoryName(String catagoryName) {
            this.catagoryName = catagoryName;
        }

    }
}