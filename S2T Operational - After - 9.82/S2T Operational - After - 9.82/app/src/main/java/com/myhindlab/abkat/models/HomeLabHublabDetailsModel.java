package com.myhindlab.abkat.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class HomeLabHublabDetailsModel {

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

        @SerializedName("Campid")
        @Expose
        private Integer campid;
        @SerializedName("CampDate")
        @Expose
        private String campDate;
        @SerializedName("HomeLab")
        @Expose
        private String homeLab;
        @SerializedName("HubLab")
        @Expose
        private String hubLab;
        @SerializedName("HomeLabI")
        @Expose
        private Integer homeLabI;
        @SerializedName("HubLabID")
        @Expose
        private Integer hubLabID;

        public Integer getCampid() {
            return campid;
        }

        public void setCampid(Integer campid) {
            this.campid = campid;
        }

        public String getCampDate() {
            return campDate;
        }

        public void setCampDate(String campDate) {
            this.campDate = campDate;
        }

        public String getHomeLab() {
            return homeLab;
        }

        public void setHomeLab(String homeLab) {
            this.homeLab = homeLab;
        }

        public String getHubLab() {
            return hubLab;
        }

        public void setHubLab(String hubLab) {
            this.hubLab = hubLab;
        }

        public Integer getHomeLabI() {
            return homeLabI;
        }

        public void setHomeLabI(Integer homeLabI) {
            this.homeLabI = homeLabI;
        }

        public Integer getHubLabID() {
            return hubLabID;
        }

        public void setHubLabID(Integer hubLabID) {
            this.hubLabID = hubLabID;
        }

    }
}