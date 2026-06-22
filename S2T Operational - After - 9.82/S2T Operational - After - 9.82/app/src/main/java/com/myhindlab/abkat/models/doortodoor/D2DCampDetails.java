package com.myhindlab.abkat.models.doortodoor;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class D2DCampDetails implements Serializable{

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("output")
    @Expose
    private ArrayList<Output> output = null;

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
        @SerializedName("CampName")
        @Expose
        private String campName;
        @SerializedName("CampLocation")
        @Expose
        private String campLocation;
        @SerializedName("CampId1")
        @Expose
        private Integer campId1;
        @SerializedName("CampNo")
        @Expose
        private String campNo;
        @SerializedName("CampLocation1")
        @Expose
        private String campLocation1;
        @SerializedName("CampDate")
        @Expose
        private String campDate;
        @SerializedName("IsActive")
        @Expose
        private Boolean isActive;
        @SerializedName("CreatedBy")
        @Expose
        private Integer createdBy;
        @SerializedName("CreatedDate")
        @Expose
        private String createdDate;
        @SerializedName("ModifiedBy")
        @Expose
        private Integer modifiedBy;
        @SerializedName("ModifiedDate")
        @Expose
        private String modifiedDate;
        @SerializedName("IsCampInitiated")
        @Expose
        private Object isCampInitiated;
        @SerializedName("DISTLGDCODE")
        @Expose
        private Integer distlgdcode;
        @SerializedName("CampStartDate")
        @Expose
        private String campStartDate;
        @SerializedName("CampEndDate")
        @Expose
        private Object campEndDate;
        @SerializedName("No_Dr_Required")
        @Expose
        private Object noDrRequired;
        @SerializedName("No_Nrs_Required")
        @Expose
        private Object noNrsRequired;
        @SerializedName("No_Phlebo_Required")
        @Expose
        private Object noPhleboRequired;
        @SerializedName("No_Admin_Required")
        @Expose
        private Object noAdminRequired;
        @SerializedName("Exp_Patients")
        @Expose
        private Object expPatients;
        @SerializedName("EstimatedCost")
        @Expose
        private Object estimatedCost;
        @SerializedName("No_Vendor_Required")
        @Expose
        private Object noVendorRequired;
        @SerializedName("FireStationNo")
        @Expose
        private Object fireStationNo;
        @SerializedName("PoliceStationNo")
        @Expose
        private Object policeStationNo;
        @SerializedName("LABCODE")
        @Expose
        private Integer labcode;
        @SerializedName("FACILITYCODE")
        @Expose
        private Integer facilitycode;
        @SerializedName("AffilatedHospitalId")
        @Expose
        private Integer affilatedHospitalId;
        @SerializedName("ArogyaMitraId")
        @Expose
        private Object arogyaMitraId;
        @SerializedName("ArogyaMitra")
        @Expose
        private String arogyaMitra;
        @SerializedName("ArogyaMitraMOB")
        @Expose
        private String arogyaMitraMOB;
        @SerializedName("PostCampDate")
        @Expose
        private String postCampDate;
        @SerializedName("CampName1")
        @Expose
        private String campName1;
        @SerializedName("CampType")
        @Expose
        private String campType;
        @SerializedName("actualpostcampdate")
        @Expose
        private Object actualpostcampdate;
        @SerializedName("RefferalLabCode")
        @Expose
        private Object refferalLabCode;
        @SerializedName("CampApprovalFilePath")
        @Expose
        private Object campApprovalFilePath;
        @SerializedName("ISCSCCamp")
        @Expose
        private Object iSCSCCamp;
        @SerializedName("SiteDetailId")
        @Expose
        private Integer SiteDetailId;
        @SerializedName("DISTNAME")
        @Expose
        private String DISTNAME;
        @SerializedName("IsCampClosed")
        @Expose
        private String isCampClosed;

        public String getIsCampClosed() {
            return isCampClosed;
        }

        public void setIsCampClosed(String isCampClosed) {
            this.isCampClosed = isCampClosed;
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

        public String getInitiatedBy1() {
            return initiatedBy1;
        }

        public void setInitiatedBy1(String initiatedBy1) {
            this.initiatedBy1 = initiatedBy1;
        }

        @SerializedName("InitiatedBy1")
        @Expose
        private String initiatedBy1;

        public String getCampCreatedBy() {
            return campCreatedBy;
        }

        public void setCampCreatedBy(String campCreatedBy) {
            this.campCreatedBy = campCreatedBy;
        }

        @SerializedName("CampCreatedBy")
        @Expose
        private String campCreatedBy;

        public Integer getCampId() {
            return campId;
        }

        public void setCampId(Integer campId) {
            this.campId = campId;
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

        public Integer getCampId1() {
            return campId1;
        }

        public void setCampId1(Integer campId1) {
            this.campId1 = campId1;
        }

        public String getCampNo() {
            return campNo;
        }

        public void setCampNo(String campNo) {
            this.campNo = campNo;
        }

        public String getCampLocation1() {
            return campLocation1;
        }

        public void setCampLocation1(String campLocation1) {
            this.campLocation1 = campLocation1;
        }

        public String getCampDate() {
            return campDate;
        }

        public void setCampDate(String campDate) {
            this.campDate = campDate;
        }

        public Boolean getIsActive() {
            return isActive;
        }

        public void setIsActive(Boolean isActive) {
            this.isActive = isActive;
        }

        public Integer getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(Integer createdBy) {
            this.createdBy = createdBy;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

        public Integer getModifiedBy() {
            return modifiedBy;
        }

        public void setModifiedBy(Integer modifiedBy) {
            this.modifiedBy = modifiedBy;
        }

        public String getModifiedDate() {
            return modifiedDate;
        }

        public void setModifiedDate(String modifiedDate) {
            this.modifiedDate = modifiedDate;
        }

        public Object getIsCampInitiated() {
            return isCampInitiated;
        }

        public void setIsCampInitiated(Object isCampInitiated) {
            this.isCampInitiated = isCampInitiated;
        }

        public Integer getDistlgdcode() {
            return distlgdcode;
        }

        public void setDistlgdcode(Integer distlgdcode) {
            this.distlgdcode = distlgdcode;
        }

        public String getCampStartDate() {
            return campStartDate;
        }

        public void setCampStartDate(String campStartDate) {
            this.campStartDate = campStartDate;
        }

        public Object getCampEndDate() {
            return campEndDate;
        }

        public void setCampEndDate(Object campEndDate) {
            this.campEndDate = campEndDate;
        }

        public Object getNoDrRequired() {
            return noDrRequired;
        }

        public void setNoDrRequired(Object noDrRequired) {
            this.noDrRequired = noDrRequired;
        }

        public Object getNoNrsRequired() {
            return noNrsRequired;
        }

        public void setNoNrsRequired(Object noNrsRequired) {
            this.noNrsRequired = noNrsRequired;
        }

        public Object getNoPhleboRequired() {
            return noPhleboRequired;
        }

        public void setNoPhleboRequired(Object noPhleboRequired) {
            this.noPhleboRequired = noPhleboRequired;
        }

        public Object getNoAdminRequired() {
            return noAdminRequired;
        }

        public void setNoAdminRequired(Object noAdminRequired) {
            this.noAdminRequired = noAdminRequired;
        }

        public Object getExpPatients() {
            return expPatients;
        }

        public void setExpPatients(Object expPatients) {
            this.expPatients = expPatients;
        }

        public Object getEstimatedCost() {
            return estimatedCost;
        }

        public void setEstimatedCost(Object estimatedCost) {
            this.estimatedCost = estimatedCost;
        }

        public Object getNoVendorRequired() {
            return noVendorRequired;
        }

        public void setNoVendorRequired(Object noVendorRequired) {
            this.noVendorRequired = noVendorRequired;
        }

        public Object getFireStationNo() {
            return fireStationNo;
        }

        public void setFireStationNo(Object fireStationNo) {
            this.fireStationNo = fireStationNo;
        }

        public Object getPoliceStationNo() {
            return policeStationNo;
        }

        public void setPoliceStationNo(Object policeStationNo) {
            this.policeStationNo = policeStationNo;
        }

        public Integer getLabcode() {
            return labcode;
        }

        public void setLabcode(Integer labcode) {
            this.labcode = labcode;
        }

        public Integer getFacilitycode() {
            return facilitycode;
        }

        public void setFacilitycode(Integer facilitycode) {
            this.facilitycode = facilitycode;
        }

        public Integer getAffilatedHospitalId() {
            return affilatedHospitalId;
        }

        public void setAffilatedHospitalId(Integer affilatedHospitalId) {
            this.affilatedHospitalId = affilatedHospitalId;
        }

        public Object getArogyaMitraId() {
            return arogyaMitraId;
        }

        public void setArogyaMitraId(Object arogyaMitraId) {
            this.arogyaMitraId = arogyaMitraId;
        }

        public String getArogyaMitra() {
            return arogyaMitra;
        }

        public void setArogyaMitra(String arogyaMitra) {
            this.arogyaMitra = arogyaMitra;
        }

        public String getArogyaMitraMOB() {
            return arogyaMitraMOB;
        }

        public void setArogyaMitraMOB(String arogyaMitraMOB) {
            this.arogyaMitraMOB = arogyaMitraMOB;
        }

        public String getPostCampDate() {
            return postCampDate;
        }

        public void setPostCampDate(String postCampDate) {
            this.postCampDate = postCampDate;
        }

        public String getCampName1() {
            return campName1;
        }

        public void setCampName1(String campName1) {
            this.campName1 = campName1;
        }

        public String getCampType() {
            return campType;
        }

        public void setCampType(String campType) {
            this.campType = campType;
        }

        public Object getActualpostcampdate() {
            return actualpostcampdate;
        }

        public void setActualpostcampdate(Object actualpostcampdate) {
            this.actualpostcampdate = actualpostcampdate;
        }

        public Object getRefferalLabCode() {
            return refferalLabCode;
        }

        public void setRefferalLabCode(Object refferalLabCode) {
            this.refferalLabCode = refferalLabCode;
        }

        public Object getCampApprovalFilePath() {
            return campApprovalFilePath;
        }

        public void setCampApprovalFilePath(Object campApprovalFilePath) {
            this.campApprovalFilePath = campApprovalFilePath;
        }

        public Object getISCSCCamp() {
            return iSCSCCamp;
        }

        public void setISCSCCamp(Object iSCSCCamp) {
            this.iSCSCCamp = iSCSCCamp;
        }

        public Integer getSiteDetailId() {
            return SiteDetailId;
        }

        public void setSiteDetailId(Integer siteDetailId) {
            SiteDetailId = siteDetailId;
        }

        public String getDISTNAME() {
            return DISTNAME;
        }

        public void setDISTNAME(String DISTNAME) {
            this.DISTNAME = DISTNAME;
        }
    }

}

