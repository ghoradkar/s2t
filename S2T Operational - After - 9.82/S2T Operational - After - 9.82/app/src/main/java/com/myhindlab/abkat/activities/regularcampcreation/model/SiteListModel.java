package com.myhindlab.abkat.activities.regularcampcreation.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class SiteListModel implements Serializable{

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

    public class Output implements Serializable {

        @SerializedName("SiteDetailId")
        @Expose
        private Integer siteDetailId;
        @SerializedName("SiteName")
        @Expose
        private String siteName;
        @SerializedName("SiteAddress")
        @Expose
        private String siteAddress;
        @SerializedName("CampLocation")
        @Expose
        private String campLocation;
        @SerializedName("PinCode")
        @Expose
        private Object pinCode;
        @SerializedName("City")
        @Expose
        private Object city;
        @SerializedName("Latitude")
        @Expose
        private Object latitude;
        @SerializedName("Longitude")
        @Expose
        private Object longitude;
        @SerializedName("DISTLGDCODE")
        @Expose
        private Integer distlgdcode;
        @SerializedName("DISTNAME")
        @Expose
        private String distname;
        @SerializedName("TALLGDCODE")
        @Expose
        private Object tallgdcode;
        @SerializedName("ReraId")
        @Expose
        private Object reraId;
        @SerializedName("NoWorkersRegister")
        @Expose
        private Integer noWorkersRegister;
        @SerializedName("NoWorkerNonRegister")
        @Expose
        private Integer noWorkerNonRegister;

        @SerializedName("Expectedbeneficiarycount")
        @Expose
        private Integer expectedbeneficiarycount;

        private boolean isChecked;

        public String getCampLocation() {
            return campLocation;
        }

        public void setCampLocation(String campLocation) {
            this.campLocation = campLocation;
        }

        public Output(Integer siteDetailId, String siteName, String siteAddress, Object pinCode, Object city, Object latitude, Object longitude, Integer distlgdcode, String distname, Object reraId, Integer noWorkersRegister, Integer noWorkerNonRegister, boolean isChecked) {
            this.siteDetailId = siteDetailId;
            this.siteName = siteName;
            this.siteAddress = siteAddress;
            this.pinCode = pinCode;
            this.city = city;
            this.latitude = latitude;
            this.longitude = longitude;
            this.distlgdcode = distlgdcode;
            this.distname = distname;
            this.reraId = reraId;
            this.noWorkersRegister = noWorkersRegister;
            this.noWorkerNonRegister = noWorkerNonRegister;
            this.isChecked = isChecked;
        }

        public Integer getNoWorkersNonRegister() {
            return noWorkerNonRegister;
        }

        public void setNoWorkersNonRegister(Integer noWorkersNonRegister) {
            this.noWorkerNonRegister = noWorkersNonRegister;
        }

        public Integer getNoWorkersRegister() {
            return noWorkersRegister == null ? 0 : noWorkersRegister;
        }

        public void setNoWorkersRegister(Integer noWorkersRegister) {
            this.noWorkersRegister = noWorkersRegister;
        }

        public Integer getSiteDetailId() {
            return siteDetailId;
        }

        public void setSiteDetailId(Integer siteDetailId) {
            this.siteDetailId = siteDetailId;
        }

        public String getSiteName() {
            return siteName;
        }

        public void setSiteName(String siteName) {
            this.siteName = siteName;
        }

        public String getSiteAddress() {
            return siteAddress;
        }

        public void setSiteAddress(String siteAddress) {
            this.siteAddress = siteAddress;
        }

        public Object getPinCode() {
            return pinCode;
        }

        public void setPinCode(Object pinCode) {
            this.pinCode = pinCode;
        }

        public Object getCity() {
            return city;
        }

        public void setCity(Object city) {
            this.city = city;
        }

        public Object getLatitude() {
            return latitude;
        }

        public void setLatitude(Object latitude) {
            this.latitude = latitude;
        }

        public Object getLongitude() {
            return longitude;
        }

        public void setLongitude(Object longitude) {
            this.longitude = longitude;
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

        public Object getTallgdcode() {
            return tallgdcode;
        }

        public void setTallgdcode(Object tallgdcode) {
            this.tallgdcode = tallgdcode;
        }

        public Object getReraId() {
            return reraId;
        }

        public void setReraId(Object reraId) {
            this.reraId = reraId;
        }

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        public Integer getNoWorkerNonRegister() {
            return noWorkerNonRegister;
        }

        public void setNoWorkerNonRegister(Integer noWorkerNonRegister) {
            this.noWorkerNonRegister = noWorkerNonRegister;
        }

        public Integer getExpectedbeneficiarycount() {
            return expectedbeneficiarycount;
        }

        public void setExpectedbeneficiarycount(Integer expectedbeneficiarycount) {
            this.expectedbeneficiarycount = expectedbeneficiarycount;
        }
    }

}