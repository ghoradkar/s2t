package com.myhindlab.abkat.activities.campApproval.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class CampDetailsModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("output")
    @Expose
    private List<Output> output = null;

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

        @SerializedName("CampDate")
        @Expose
        private String campDate;
        @SerializedName("CampNo")
        @Expose
        private String campNo;
        @SerializedName("CampName")
        @Expose
        private String campName;
        @SerializedName("CampLocation")
        @Expose
        private String campLocation;
        @SerializedName("District")
        @Expose
        private String district;
        @SerializedName("Expectedbeneficiarycount")
        @Expose
        private Integer expectedbeneficiarycount;

        public String getCampDate() {
            return campDate;
        }

        public void setCampDate(String campDate) {
            this.campDate = campDate;
        }

        public String getCampNo() {
            return campNo;
        }

        public void setCampNo(String campNo) {
            this.campNo = campNo;
        }

        public String getCampName() {
            return campName;
        }

        public void setCampName(String campName) {
            this.campName = campName;
        }

        public String getCampLocation() {
            return campLocation;
        }

        public void setCampLocation(String campLocation) {
            this.campLocation = campLocation;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public Integer getExpectedbeneficiarycount() {
            return expectedbeneficiarycount;
        }

        public void setExpectedbeneficiarycount(Integer expectedbeneficiarycount) {
            this.expectedbeneficiarycount = expectedbeneficiarycount;
        }

    }

}

