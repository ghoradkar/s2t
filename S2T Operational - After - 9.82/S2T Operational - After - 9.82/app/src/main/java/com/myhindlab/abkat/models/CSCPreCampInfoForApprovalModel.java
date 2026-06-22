package com.myhindlab.abkat.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class CSCPreCampInfoForApprovalModel {

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


    public static class Output implements Serializable {

        @SerializedName("CSCPreAprrovedid")
        @Expose
        private Integer cSCPreAprrovedid;
        @SerializedName("Distlgdcode")
        @Expose
        private Integer distlgdcode;
        @SerializedName("Tallgdcode")
        @Expose
        private Integer tallgdcode;
        @SerializedName("GP_NAME")
        @Expose
        private String gpName;
        @SerializedName("GP_ID")
        @Expose
        private Integer gpId;
        @SerializedName("Address")
        @Expose
        private String address;
        @SerializedName("Camp_Name")
        @Expose
        private String campName;
        @SerializedName("EstimatedCampDate")
        @Expose
        private String estimatedCampDate;
        @SerializedName("ExpectedBeneficary")
        @Expose
        private Integer expectedBeneficary;
        @SerializedName("CSCUserName")
        @Expose
        private String cSCUserName;
        @SerializedName("CSCUserID")
        @Expose
        private Integer cSCUserID;
        @SerializedName("CSCUserDesignation")
        @Expose
        private String cSCUserDesignation;
        @SerializedName("CSCUserDesignationID")
        @Expose
        private Integer cSCUserDesignationID;
        @SerializedName("CSCUserMobile")
        @Expose
        private String cSCUserMobile;
        @SerializedName("CampStatus")
        @Expose
        private Integer campStatus;
        @SerializedName("LocationImagePath")
        @Expose
        private String locationImagePath;
        @SerializedName("CreatedOn")
        @Expose
        private String createdOn;
        @SerializedName("ApprovedBy")
        @Expose
        private Integer approvedBy;
        @SerializedName("ApprovedDate")
        @Expose
        private String approvedDate;

        @SerializedName("Reason")
        @Expose
        private String reason;

        @SerializedName("CampType")
        @Expose
        private Integer campType;


        @SerializedName("DivMngrApproval")
        @Expose
        private Integer DivMngrApproval;

        public Integer getCampType() {
            return campType;
        }

        public void setCampType(Integer campType) {
            this.campType = campType;
        }

        public String getCampTypeDescription() {
            return campTypeDescription;
        }

        public void setCampTypeDescription(String campTypeDescription) {
            this.campTypeDescription = campTypeDescription;
        }

        @SerializedName("CampTypeDescription")
        @Expose
        private String campTypeDescription;


        public Output() {
        }

        public Output(Integer cSCPreAprrovedid, Integer distlgdcode, Integer tallgdcode, String gpName, Integer gpId, String address, String campName, String estimatedCampDate, Integer expectedBeneficary, String cSCUserName, Integer cSCUserID, String cSCUserDesignation, Integer cSCUserDesignationID, String cSCUserMobile, Integer campStatus, String locationImagePath, String createdOn, Integer approvedBy, String approvedDate, String reason) {
            this.cSCPreAprrovedid = cSCPreAprrovedid;
            this.distlgdcode = distlgdcode;
            this.tallgdcode = tallgdcode;
            this.gpName = gpName;
            this.gpId = gpId;
            this.address = address;
            this.campName = campName;
            this.estimatedCampDate = estimatedCampDate;
            this.expectedBeneficary = expectedBeneficary;
            this.cSCUserName = cSCUserName;
            this.cSCUserID = cSCUserID;
            this.cSCUserDesignation = cSCUserDesignation;
            this.cSCUserDesignationID = cSCUserDesignationID;
            this.cSCUserMobile = cSCUserMobile;
            this.campStatus = campStatus;
            this.locationImagePath = locationImagePath;
            this.createdOn = createdOn;
            this.approvedBy = approvedBy;
            this.approvedDate = approvedDate;
            this.reason = reason;
        }

        public Output(Integer cscPreAprrovedid, Integer distlgdcode, Integer tallgdcode, String gpName, Integer gpId, String address, String estimatedCampDate, Integer expectedBeneficary, String cscUserName, Integer cscUserID, String cscUserDesignation, Integer cscUserDesignationID, String cscUserMobile, Integer campStatus, String locationImagePath, String createdOn, Integer approvedBy, String approvedDate, String reason) {
            this.cSCPreAprrovedid = cSCPreAprrovedid;
            this.distlgdcode = distlgdcode;
            this.tallgdcode = tallgdcode;
            this.gpName = gpName;
            this.gpId = gpId;
            this.address = address;
            this.campName = campName;
            this.estimatedCampDate = estimatedCampDate;
            this.expectedBeneficary = expectedBeneficary;
            this.cSCUserName = cSCUserName;
            this.cSCUserID = cSCUserID;
            this.cSCUserDesignation = cSCUserDesignation;
            this.cSCUserDesignationID = cSCUserDesignationID;
            this.cSCUserMobile = cSCUserMobile;
            this.campStatus = campStatus;
            this.locationImagePath = locationImagePath;
            this.createdOn = createdOn;
            this.approvedBy = approvedBy;
            this.approvedDate = approvedDate;
            this.reason = reason;
        }

        public Integer getCSCPreAprrovedid() {
            return cSCPreAprrovedid;
        }

        public void setCSCPreAprrovedid(Integer cSCPreAprrovedid) {
            this.cSCPreAprrovedid = cSCPreAprrovedid;
        }

        public Integer getDistlgdcode() {
            return distlgdcode;
        }

        public void setDistlgdcode(Integer distlgdcode) {
            this.distlgdcode = distlgdcode;
        }

        public Integer getTallgdcode() {
            return tallgdcode;
        }

        public void setTallgdcode(Integer tallgdcode) {
            this.tallgdcode = tallgdcode;
        }

        public String getGpName() {
            return gpName;
        }

        public void setGpName(String gpName) {
            this.gpName = gpName;
        }

        public Integer getGpId() {
            return gpId;
        }

        public void setGpId(Integer gpId) {
            this.gpId = gpId;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCampName() {
            return campName;
        }

        public void setCampName(String campName) {
            this.campName = campName;
        }

        public String getEstimatedCampDate() {
            return estimatedCampDate;
        }

        public void setEstimatedCampDate(String estimatedCampDate) {
            this.estimatedCampDate = estimatedCampDate;
        }

        public Integer getExpectedBeneficary() {
            return expectedBeneficary;
        }

        public void setExpectedBeneficary(Integer expectedBeneficary) {
            this.expectedBeneficary = expectedBeneficary;
        }

        public String getCSCUserName() {
            return cSCUserName;
        }

        public void setCSCUserName(String cSCUserName) {
            this.cSCUserName = cSCUserName;
        }

        public Integer getCSCUserID() {
            return cSCUserID;
        }

        public void setCSCUserID(Integer cSCUserID) {
            this.cSCUserID = cSCUserID;
        }

        public String getCSCUserDesignation() {
            return cSCUserDesignation;
        }

        public void setCSCUserDesignation(String cSCUserDesignation) {
            this.cSCUserDesignation = cSCUserDesignation;
        }

        public Integer getCSCUserDesignationID() {
            return cSCUserDesignationID;
        }

        public void setCSCUserDesignationID(Integer cSCUserDesignationID) {
            this.cSCUserDesignationID = cSCUserDesignationID;
        }

        public String getCSCUserMobile() {
            return cSCUserMobile;
        }

        public void setCSCUserMobile(String cSCUserMobile) {
            this.cSCUserMobile = cSCUserMobile;
        }

        public Integer getCampStatus() {
            return campStatus;
        }

        public void setCampStatus(Integer campStatus) {
            this.campStatus = campStatus;
        }

        public String getLocationImagePath() {
            return locationImagePath;
        }

        public void setLocationImagePath(String locationImagePath) {
            this.locationImagePath = locationImagePath;
        }

        public String getCreatedOn() {
            return createdOn;
        }

        public void setCreatedOn(String createdOn) {
            this.createdOn = createdOn;
        }

        public Integer getApprovedBy() {
            return approvedBy;
        }

        public void setApprovedBy(Integer approvedBy) {
            this.approvedBy = approvedBy;
        }

        public String getApprovedDate() {
            return approvedDate;
        }

        public void setApprovedDate(String approvedDate) {
            this.approvedDate = approvedDate;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public Integer getDivMngrApproval() {
            return DivMngrApproval == null ? 0 : DivMngrApproval;
        }

        public void setDivMngrApproval(Integer divMngrApproval) {
            DivMngrApproval = divMngrApproval;
        }
    }
}
