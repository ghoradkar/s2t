package com.myhindlab.abkat.models;

import java.io.Serializable;

public class GetApprovedCampListDetailsForAppList implements Serializable {

    private String CampId;
    private String CampNo;
    private String CampLocation;
    private String CampDate;
    private String Status;
    private String Description;
    private String DISTLGDCODE;
    private String DISTNAME;
    private String Remark;
    private String SiteDetailId;
    private String CampType;
    private String FLAG;
    private String LABCODE;
    private String IsRegdDone;

    public String getIsRegdDone() {
        return IsRegdDone;
    }

    public void setIsRegdDone(String isRegdDone) {
        IsRegdDone = isRegdDone;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    private String CreatedBy;

    public String getLABCODE() {
        return LABCODE;
    }

    public void setLABCODE(String LABCODE) {
        this.LABCODE = LABCODE;
    }

    private String ISCampType = "0";

    public String getISCampType() {
        return ISCampType;
    }

    public void setISCampType(String ISCampType) {
        this.ISCampType = ISCampType;
    }

    public String getIsCampClosed() {
        return IsCampClosed;
    }

    public void setIsCampClosed(String isCampClosed) {
        IsCampClosed = isCampClosed;
    }

    private String IsCampClosed;

    public String getCampTypeDescription() {
        return CampTypeDescription;
    }

    public void setCampTypeDescription(String campTypeDescription) {
        CampTypeDescription = campTypeDescription;
    }

    public String getInitiatedBy() {
        return InitiatedBy;
    }

    public void setInitiatedBy(String initiatedBy) {
        InitiatedBy = initiatedBy;
    }

    public String getInitiatedBy1() {
        return InitiatedBy1;
    }

    public void setInitiatedBy1(String initiatedBy1) {
        InitiatedBy1 = initiatedBy1;
    }

    private String CampTypeDescription;
    private String InitiatedBy;
    private String InitiatedBy1;

    public String getCampId() {
        return CampId;
    }

    public void setCampId(String campId) {
        CampId = campId;
    }

    public String getCampNo() {
        return CampNo;
    }

    public void setCampNo(String campNo) {
        CampNo = campNo;
    }

    public String getCampLocation() {
        return CampLocation;
    }

    public void setCampLocation(String campLocation) {
        CampLocation = campLocation;
    }

    public String getCampDate() {
        return CampDate;
    }

    public void setCampDate(String campDate) {
        CampDate = campDate;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getDISTLGDCODE() {
        return DISTLGDCODE;
    }

    public void setDISTLGDCODE(String DISTLGDCODE) {
        this.DISTLGDCODE = DISTLGDCODE;
    }

    public String getDISTNAME() {
        return DISTNAME;
    }

    public void setDISTNAME(String DISTNAME) {
        this.DISTNAME = DISTNAME;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getSiteDetailId() {
        return SiteDetailId;
    }

    public void setSiteDetailId(String siteDetailId) {
        SiteDetailId = siteDetailId;
    }

    public String getCampType() {
        if (CampType != null) {
            return CampType;
        } else {
            return "1";
        }
    }

    public void setCampType(String campType) {
        CampType = campType;
    }

    public String getFLAG() {
        return FLAG;
    }

    public void setFLAG(String FLAG) {
        this.FLAG = FLAG;
    }
}
