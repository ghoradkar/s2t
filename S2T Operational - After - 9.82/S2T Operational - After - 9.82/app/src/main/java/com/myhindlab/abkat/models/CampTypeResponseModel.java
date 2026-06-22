package com.myhindlab.abkat.models;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CampTypeResponseModel {

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

        @SerializedName("CAMPTYPE")
        @Expose
        private Integer camptype;
        @SerializedName("CampTypeDescription")
        @Expose
        private String campTypeDescription;
        @SerializedName("CatagoryID")
        @Expose
        private Integer catagoryID;
        @SerializedName("CatagoryType")
        @Expose
        private String catagoryType;

        public Output(Integer camptype, String campTypeDescription, Integer catagoryID, String catagoryType) {
            this.camptype = camptype;
            this.campTypeDescription = campTypeDescription;
            this.catagoryID = catagoryID;
            this.catagoryType = catagoryType;
        }

        public Integer getCamptype() {
            return camptype;
        }

        public void setCamptype(Integer camptype) {
            this.camptype = camptype;
        }

        public String getCampTypeDescription() {
            return campTypeDescription;
        }

        public void setCampTypeDescription(String campTypeDescription) {
            this.campTypeDescription = campTypeDescription;
        }

        public Integer getCatagoryID() {
            return catagoryID;
        }

        public void setCatagoryID(Integer catagoryID) {
            this.catagoryID = catagoryID;
        }

        public String getCatagoryType() {
            return catagoryType;
        }

        public void setCatagoryType(String catagoryType) {
            this.catagoryType = catagoryType;
        }

    }
}

