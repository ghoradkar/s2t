package com.myhindlab.abkat.models.couriermodule;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CamplistOnLandingLabModel implements Serializable {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("output")
    @Expose
    private ArrayList<Output> output;

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

    public ArrayList<Output> getOutput() {
        return output;
    }

    public void setOutput(ArrayList<Output> output) {
        this.output = output;
    }


    public class Output implements Serializable {

        @SerializedName("CampId")
        @Expose
        private Integer campId;
        @SerializedName("campid")
        @Expose
        private Integer campid;

        public Integer getExpenseAmount() {
            return expenseAmount;
        }

        public void setExpenseAmount(Integer expenseAmount) {
            this.expenseAmount = expenseAmount;
        }

        @SerializedName("ExpenseAmount")
        @Expose
        private Integer expenseAmount;

        public Integer getCampid() {
            return campid;
        }

        public void setCampid(Integer campid) {
            this.campid = campid;
        }

        @SerializedName("SiteDetailId")
        @Expose
        private Integer siteDetailId;
        @SerializedName("CampNo")
        @Expose
        private String campNo;
        @SerializedName("CampLocation")
        @Expose
        private String campLocation;
        @SerializedName("CampDate")
        @Expose
        private String campDate;
        @SerializedName("DISTLGDCODE")
        @Expose
        private Integer distlgdcode;
        @SerializedName("DISTNAME")
        @Expose
        private String distname;
        @SerializedName("CampType")
        @Expose
        private Integer campType;
        @SerializedName("LandingLab")
        @Expose
        private Integer landingLab;

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        @SerializedName("isChecked")
        @Expose
        private boolean isChecked;

        public Integer getCampId() {
            return campId;
        }

        public void setCampId(Integer campId) {
            this.campId = campId;
        }

        public Integer getSiteDetailId() {
            return siteDetailId;
        }

        public void setSiteDetailId(Integer siteDetailId) {
            this.siteDetailId = siteDetailId;
        }

        public String getCampNo() {
            return campNo;
        }

        public void setCampNo(String campNo) {
            this.campNo = campNo;
        }

        public String getCampLocation() {
            return campLocation;
        }

        public void setCampLocation(String campLocation) {
            this.campLocation = campLocation;
        }

        public String getCampDate() {
            return campDate;
        }

        public void setCampDate(String campDate) {
            this.campDate = campDate;
        }

        public Integer getDistlgdcode() {
            return distlgdcode;
        }

        public void setDistlgdcode(Integer distlgdcode) {
            this.distlgdcode = distlgdcode;
        }

        public String getDistname() {
            return distname;
        }

        public void setDistname(String distname) {
            this.distname = distname;
        }

        public Integer getCampType() {
            return campType;
        }

        public void setCampType(Integer campType) {
            this.campType = campType;
        }

        public Integer getLandingLab() {
            return landingLab;
        }

        public void setLandingLab(Integer landingLab) {
            this.landingLab = landingLab;
        }

    }
}