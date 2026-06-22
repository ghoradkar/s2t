package com.myhindlab.abkat.models;

import java.io.Serializable;

public class ConstructionSitesList_Model implements Serializable {

    private String sitetypeid;
    private String SiteDetailId;
    private String SiteName;
    private String SiteAddress;
    private String PinCode;
    private String City;
    private String Latitude;
    private String Longitude;
    private String DISTLGDCODE;
    private String DISTNAME;
    private String TALLGDCODE;
    private String TALNAME;
    private String ReraId;
    private String ReraNo;

    private String userID;
    private String builderID;
    private String currentLatitude;
    private String currentLongitude;
    private String Flag;

    public String getSitetypeid() {
        return sitetypeid;
    }

    public void setSitetypeid(String sitetypeid) {
        this.sitetypeid = sitetypeid;
    }

    public String getSiteDetailId() {
        return SiteDetailId;
    }

    public void setSiteDetailId(String siteDetailId) {
        SiteDetailId = siteDetailId;
    }

    public String getSiteName() {
        return SiteName;
    }

    public void setSiteName(String siteName) {
        SiteName = siteName;
    }

    public String getSiteAddress() {
        if (SiteAddress == null) {
            SiteAddress = "";
        }
        return SiteAddress;
    }

    public void setSiteAddress(String siteAddress) {
        SiteAddress = siteAddress;
    }

    public String getPinCode() {
        if (PinCode == null) {
            PinCode = "";
        }
        return PinCode;
    }

    public void setPinCode(String pinCode) {
        PinCode = pinCode;
    }

    public String getCity() {
        if (City == null) {
            City = "";
        }
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
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

    public String getTALLGDCODE() {
        return TALLGDCODE;
    }

    public void setTALLGDCODE(String TALLGDCODE) {
        this.TALLGDCODE = TALLGDCODE;
    }

    public String getTALNAME() {
        return TALNAME;
    }

    public void setTALNAME(String TALNAME) {
        this.TALNAME = TALNAME;
    }

    public String getReraId() {
        return ReraId;
    }

    public void setReraId(String reraId) {
        ReraId = reraId;
    }

    public String getReraNo() {
        return ReraNo;
    }

    public void setReraNo(String reraNo) {
        ReraNo = reraNo;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getCurrentLatitude() {
        return currentLatitude;
    }

    public void setCurrentLatitude(String currentLatitude) {
        this.currentLatitude = currentLatitude;
    }

    public String getCurrentLongitude() {
        return currentLongitude;
    }

    public void setCurrentLongitude(String currentLongitude) {
        this.currentLongitude = currentLongitude;
    }

    public String getBuilderID() {
        return builderID;
    }

    public void setBuilderID(String builderID) {
        this.builderID = builderID;
    }

    public String getFlag() {
        if (Flag == null) {
            return "0";
        } else {
            return Flag;
        }
    }

    public void setFlag(String flag) {
        Flag = flag;
    }
}
