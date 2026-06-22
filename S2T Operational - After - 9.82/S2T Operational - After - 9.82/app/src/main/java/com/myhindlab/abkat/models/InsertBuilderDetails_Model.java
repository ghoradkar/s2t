package com.myhindlab.abkat.models;

import java.util.ArrayList;

public class InsertBuilderDetails_Model {

    private String SiteDetailId;
    private String ReraId;
    private String BuilderName;
    private String BuilderContactNo;
    private String EmailId;
    private String SiteMngrName;
    private String SiteMngrContactNo;
    private String SiteMngrEmailId;
    private String CreatedBy;
    private ArrayList<ContractorOtherPersonDetails_Model> contractorDetails;
//    private ArrayList<OtherPersonDetails_Model> otherPersonDetails;

    private String OtherContName;

    public String getSiteDetailId() {
        return SiteDetailId;
    }

    public void setSiteDetailId(String siteDetailId) {
        SiteDetailId = siteDetailId;
    }

    public String getReraId() {
        return ReraId;
    }

    public void setReraId(String reraId) {
        ReraId = reraId;
    }

    public String getBuilderName() {
        return BuilderName;
    }

    public void setBuilderName(String builderName) {
        BuilderName = builderName;
    }

    public String getBuilderContactNo() {
        return BuilderContactNo;
    }

    public void setBuilderContactNo(String builderContactNo) {
        BuilderContactNo = builderContactNo;
    }

    public String getEmailId() {
        return EmailId;
    }

    public void setEmailId(String emailId) {
        EmailId = emailId;
    }

    public String getSiteMngrName() {
        return SiteMngrName;
    }

    public void setSiteMngrName(String siteMngrName) {
        SiteMngrName = siteMngrName;
    }

    public String getSiteMngrContactNo() {
        return SiteMngrContactNo;
    }

    public void setSiteMngrContactNo(String siteMngrContactNo) {
        SiteMngrContactNo = siteMngrContactNo;
    }

    public String getSiteMngrEmailId() {
        return SiteMngrEmailId;
    }

    public void setSiteMngrEmailId(String siteMngrEmailId) {
        SiteMngrEmailId = siteMngrEmailId;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    public void setContractorDetails(ArrayList<ContractorOtherPersonDetails_Model> contractorDetails) {
        this.contractorDetails = contractorDetails;
    }

    public ArrayList<ContractorOtherPersonDetails_Model> getContractorDetails() {
        return contractorDetails;
    }

//    public ArrayList<OtherPersonDetails_Model> getOtherPersonDetails() {
//        return otherPersonDetails;
//    }
//
//    public void setOtherPersonDetails(ArrayList<OtherPersonDetails_Model> otherPersonDetails) {
//        this.otherPersonDetails = otherPersonDetails;
//    }

    public String getOtherContName() {
        return OtherContName;
    }

    public void setOtherContName(String otherContName) {
        OtherContName = otherContName;
    }
}