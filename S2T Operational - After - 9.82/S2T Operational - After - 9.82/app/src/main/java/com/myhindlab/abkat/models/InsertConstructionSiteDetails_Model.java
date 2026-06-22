package com.myhindlab.abkat.models;

public class InsertConstructionSiteDetails_Model {

    private String DISTLGDCODE;
    private String TALLGDCODE;
    private String Area;
    private String SiteName;
    private String SiteAddress;
    private String PinCode;
    private String City;
    private String Latitude;
    private String Longitude;
    private String CreatedBy;
    private String ReraId;
    private String ReraNo;
    private String sitetypeid;

    public String getDISTLGDCODE() {
        return DISTLGDCODE;
    }

    public void setDISTLGDCODE(String DISTLGDCODE) {
        this.DISTLGDCODE = DISTLGDCODE;
    }

    public String getTALLGDCODE() {
        return TALLGDCODE;
    }

    public void setTALLGDCODE(String TALLGDCODE) {
        this.TALLGDCODE = TALLGDCODE;
    }

    public String getArea() {
        return Area;
    }

    public void setArea(String area) {
        Area = area;
    }

    public String getSiteName() {
        return SiteName;
    }

    public void setSiteName(String siteName) {
        SiteName = siteName;
    }

    public String getSiteAddress() {
        return SiteAddress;
    }

    public void setSiteAddress(String siteAddress) {
        SiteAddress = siteAddress;
    }

    public String getPinCode() {
        return PinCode;
    }

    public void setPinCode(String pinCode) {
        PinCode = pinCode;
    }

    public String getCity() {
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

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
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

    public String getSitetypeid() {
        return sitetypeid;
    }

    public void setSitetypeid(String sitetypeid) {
        this.sitetypeid = sitetypeid;
    }
}
