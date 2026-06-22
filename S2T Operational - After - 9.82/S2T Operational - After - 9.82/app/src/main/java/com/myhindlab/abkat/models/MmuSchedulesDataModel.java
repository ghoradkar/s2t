package com.myhindlab.abkat.models;



import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MmuSchedulesDataModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("path")
    @Expose
    private String path;
    @SerializedName("dateTime")
    @Expose
    private String dateTime;
    @SerializedName("details")
    @Expose
    private List<Output> details;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public List<Output> getDetails() {
        return details;
    }

    public void setDetails(List<Output> details) {
        this.details = details;
    }


    public class Output {

        @SerializedName("mmuScheduleId")
        @Expose
        private Integer mmuScheduleId;
        @SerializedName("mmuIdentificationNo")
        @Expose
        private String mmuIdentificationNo;
        @SerializedName("district")
        @Expose
        private Integer district;
        @SerializedName("taluka")
        @Expose
        private Integer taluka;
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("pincode")
        @Expose
        private String pincode;
        @SerializedName("mmuContactNo")
        @Expose
        private String mmuContactNo;
        @SerializedName("longitude")
        @Expose
        private String longitude;
        @SerializedName("latitude")
        @Expose
        private String latitude;
        @SerializedName("dateOfVisit")
        @Expose
        private String dateOfVisit;
        @SerializedName("mmuPersonName")
        @Expose
        private String mmuPersonName;
        @SerializedName("mmuNoId")
        @Expose
        private Integer mmuNoId;
        @SerializedName("cwDistrictId")
        @Expose
        private Integer cwDistrictId;

        public Integer getMmuScheduleId() {
            return mmuScheduleId;
        }

        public void setMmuScheduleId(Integer mmuScheduleId) {
            this.mmuScheduleId = mmuScheduleId;
        }

        public String getMmuIdentificationNo() {
            return mmuIdentificationNo;
        }

        public void setMmuIdentificationNo(String mmuIdentificationNo) {
            this.mmuIdentificationNo = mmuIdentificationNo;
        }

        public Integer getDistrict() {
            return district;
        }

        public void setDistrict(Integer district) {
            this.district = district;
        }

        public Integer getTaluka() {
            return taluka;
        }

        public void setTaluka(Integer taluka) {
            this.taluka = taluka;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getPincode() {
            return pincode;
        }

        public void setPincode(String pincode) {
            this.pincode = pincode;
        }

        public String getMmuContactNo() {
            return mmuContactNo;
        }

        public void setMmuContactNo(String mmuContactNo) {
            this.mmuContactNo = mmuContactNo;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getDateOfVisit() {
            return dateOfVisit;
        }

        public void setDateOfVisit(String dateOfVisit) {
            this.dateOfVisit = dateOfVisit;
        }

        public String getMmuPersonName() {
            return mmuPersonName;
        }

        public void setMmuPersonName(String mmuPersonName) {
            this.mmuPersonName = mmuPersonName;
        }

        public Integer getMmuNoId() {
            return mmuNoId;
        }

        public void setMmuNoId(Integer mmuNoId) {
            this.mmuNoId = mmuNoId;
        }

        public Integer getCwDistrictId() {
            return cwDistrictId;
        }

        public void setCwDistrictId(Integer cwDistrictId) {
            this.cwDistrictId = cwDistrictId;
        }

    }
}