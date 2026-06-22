package com.myhindlab.abkat.activities.regularcampcreation.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class CampCreationModel implements Serializable {
    @SerializedName("selectedSites")
    @Expose
    private List<SiteListModel.Output> siteList;

    @SerializedName("campDetails")
    @Expose
    private CampDetails campDetails;

    @SerializedName("selectedDevices")
    @Expose
    private List<SubDeviceListModel.Output> selectedDevice;

    @SerializedName("consumable")
    @Expose
    private List<ConsumableListModel.Output> consumables;

    @SerializedName("resourceList")
    @Expose
    private List<ResourceListForMappingModel.Output> resources;

    public List<SiteListModel.Output> getSiteList() {
        return siteList;
    }

    public void setSiteList(List<SiteListModel.Output> siteList) {
        this.siteList = siteList;
    }

    public CampDetails getCampDetails() {
        return campDetails;
    }

    public void setCampDetails(CampDetails campDetails) {
        this.campDetails = campDetails;
    }

    public List<SubDeviceListModel.Output> getSelectedDevice() {
        return selectedDevice;
    }

    public void setSelectedDevice(List<SubDeviceListModel.Output> selectedDevice) {
        this.selectedDevice = selectedDevice;
    }

    public List<ConsumableListModel.Output> getConsumables() {
        return consumables;
    }

    public void setConsumables(List<ConsumableListModel.Output> consumables) {
        this.consumables = consumables;
    }

    public List<ResourceListForMappingModel.Output> getResources() {
        return resources;
    }

    public void setResources(List<ResourceListForMappingModel.Output> resources) {
        this.resources = resources;
    }

    @SerializedName("ISD2DCamp")
    @Expose
    private int ISD2DCamp;

    public int getISD2DCamp() {
        return ISD2DCamp;
    }

    public void setISD2DCamp(int ISD2DCamp) {
        this.ISD2DCamp = ISD2DCamp;
    }

    public class CampDetails implements Serializable {

        @SerializedName("distLgdCode")
        @Expose
        private Integer distLgdCode;
        @SerializedName("campName")
        @Expose
        private String campName;
        @SerializedName("campAddress")
        @Expose
        private String campAddress;
        @SerializedName("expectedBeneficiary")
        @Expose
        private Integer expectedBeneficiary;
        @SerializedName("campDate")
        @Expose
        private String campDate;

        @SerializedName("campId")
        @Expose
        private String campId;

        @SerializedName("labCode")
        @Expose
        private String labCode;

        @SerializedName("EmpCode")
        @Expose
        private String EmpCode;

        @SerializedName("skipFlag")
        @Expose
        private String skipFlag;

        public String getSkipFlag() {
            return skipFlag;
        }

        public void setSkipFlag(String skipFlag) {
            this.skipFlag = skipFlag;
        }

        public String getEmpCode() {
            return EmpCode;
        }

        public void setEmpCode(String empCode) {
            EmpCode = empCode;
        }

        public String getLabCode() {
            return labCode;
        }

        public void setLabCode(String labCode) {
            this.labCode = labCode;
        }

        public String getCampId() {
            return campId;
        }

        public void setCampId(String campId) {
            this.campId = campId;
        }

        @SerializedName("postCampDate")
        @Expose
        private String postCampDate;
        @SerializedName("nearestHospitalId")
        @Expose
        private Integer nearestHospitalId;
        @SerializedName("IsInternalApproval")
        @Expose
        private Integer isInternalApproval;
        @SerializedName("IsGovermentApproval")
        @Expose
        private Integer isGovermentApproval;
        @SerializedName("CampOrganisedBy")
        @Expose
        private Integer campOrganisedBy;
        @SerializedName("PartnerId")
        @Expose
        private Integer selectedPartnerId;


        public Integer getSelectedPartnerId() {
            return selectedPartnerId;
        }

        public void setSelectedPartnerId(Integer selectedPartnerId) {
            this.selectedPartnerId = selectedPartnerId;
        }

        public Integer getDistLgdCode() {
            return distLgdCode;
        }

        public void setDistLgdCode(Integer distLgdCode) {
            this.distLgdCode = distLgdCode;
        }

        public String getCampName() {
            return campName;
        }

        public void setCampName(String campName) {
            this.campName = campName;
        }

        public String getCampAddress() {
            return campAddress;
        }

        public void setCampAddress(String campAddress) {
            this.campAddress = campAddress;
        }

        public Integer getExpectedBeneficiary() {
            return expectedBeneficiary;
        }

        public void setExpectedBeneficiary(Integer expectedBeneficiary) {
            this.expectedBeneficiary = expectedBeneficiary;
        }

        public String getCampDate() {
            return campDate;
        }

        public void setCampDate(String campDate) {
            this.campDate = campDate;
        }

        public String getPostCampDate() {
            return postCampDate;
        }

        public void setPostCampDate(String postCampDate) {
            this.postCampDate = postCampDate;
        }

        public Integer getNearestHospitalId() {
            return nearestHospitalId;
        }

        public void setNearestHospitalId(Integer nearestHospitalId) {
            this.nearestHospitalId = nearestHospitalId;
        }

        public Integer getIsInternalApproval() {
            return isInternalApproval == null ? 0 : isInternalApproval;
        }

        public void setIsInternalApproval(Integer isInternalApproval) {
            this.isInternalApproval = isInternalApproval;
        }

        public Integer getIsGovermentApproval() {
            return isGovermentApproval == null ? 0 : isGovermentApproval;
        }

        public void setIsGovermentApproval(Integer isGovermentApproval) {
            this.isGovermentApproval = isGovermentApproval;
        }

        public Integer getCampOrganisedBy() {
            return campOrganisedBy;
        }

        public void setCampOrganisedBy(Integer campOrganisedBy) {
            this.campOrganisedBy = campOrganisedBy;
        }

    }

}