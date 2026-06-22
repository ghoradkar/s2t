package com.myhindlab.abkat.activities.campApproval.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CampStatusListModel {

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

        @SerializedName("DISTNAME")
        @Expose
        private String distname;
        @SerializedName("DISTLGDCODE")
        @Expose
        private Integer distlgdcode;
        @SerializedName("CampId")
        @Expose
        private Integer campId;
        @SerializedName("CampLocation")
        @Expose
        private String campLocation;
        @SerializedName("CampDate")
        @Expose
        private String campDate;
        @SerializedName("CampName")
        @Expose
        private String campName;
        @SerializedName("Expectedbeneficiarycount")
        @Expose
        private Integer expectedbeneficiarycount;

        @SerializedName("Remark")
        @Expose
        private String remark;
        @SerializedName("ISCampType")
        @Expose
        private String iSCampType = "0";

        @SerializedName("InternalCampFlag")
        @Expose
        private String internalCampFlag = "";

        @SerializedName("PartnerIDForCampCreate")
        @Expose
        private Integer partnerIDForCampCreate = 0;


        public Integer getPartnerIDForCampCreate() {
            return partnerIDForCampCreate;
        }

        public void setPartnerIDForCampCreate(Integer partnerIDForCampCreate) {
            this.partnerIDForCampCreate = partnerIDForCampCreate;
        }

        public String getInternalCampFlag() {
            return internalCampFlag;
        }

        public void setInternalCampFlag(String internalCampFlag) {
            this.internalCampFlag = internalCampFlag;
        }

        public String getiSCampType() {
            return iSCampType;
        }

        public void setiSCampType(String iSCampType) {
            this.iSCampType = iSCampType;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getDistname() {
            return distname;
        }

        public void setDistname(String distname) {
            this.distname = distname;
        }

        public Integer getDistlgdcode() {
            return distlgdcode;
        }

        public void setDistlgdcode(Integer distlgdcode) {
            this.distlgdcode = distlgdcode;
        }

        public Integer getCampId() {
            return campId;
        }

        public void setCampId(Integer campId) {
            this.campId = campId;
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

        public String getCampName() {
            return campName;
        }

        public void setCampName(String campName) {
            this.campName = campName;
        }

        public Integer getExpectedbeneficiarycount() {
            return expectedbeneficiarycount;
        }

        public void setExpectedbeneficiarycount(Integer expectedbeneficiarycount) {
            this.expectedbeneficiarycount = expectedbeneficiarycount;
        }

    }
}
