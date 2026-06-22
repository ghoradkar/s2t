package com.myhindlab.abkat.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WorkerList {
    @SerializedName("")
    List<WorkerInfoModel> workerInfoModels;

    public List<WorkerInfoModel> getWorkerInfoModels() {
        return workerInfoModels;
    }

    public void setWorkerInfoModels(List<WorkerInfoModel> workerInfoModels) {
        this.workerInfoModels = workerInfoModels;
    }

    class WorkerInfoModel {

        @SerializedName("bocw_id")
        @Expose
        private Integer bocwId;
        @SerializedName("payment_status")
        @Expose
        private String paymentStatus;
        @SerializedName("registration_no")
        @Expose
        private String registrationNo;
        @SerializedName("subscription")
        @Expose
        private String subscription;
        @SerializedName("next_renewal_date")
        @Expose
        private String nextRenewalDate;
        @SerializedName("firstNamePersonal")
        @Expose
        private String firstNamePersonal;
        @SerializedName("middleNamePersonal")
        @Expose
        private String middleNamePersonal;
        @SerializedName("lastNamePersonal")
        @Expose
        private String lastNamePersonal;
        @SerializedName("age")
        @Expose
        private Integer age;
        @SerializedName("gender")
        @Expose
        private String gender;
        @SerializedName("residential_address_houseNo")
        @Expose
        private String residentialAddressHouseNo;
        @SerializedName("residential_address_road")
        @Expose
        private String residentialAddressRoad;
        @SerializedName("residential_address_area")
        @Expose
        private String residentialAddressArea;
        @SerializedName("residential_address_city")
        @Expose
        private String residentialAddressCity;
        @SerializedName("residential_address_importantPlace")
        @Expose
        private String residentialAddressImportantPlace;
        @SerializedName("residential_address_postOffice")
        @Expose
        private String residentialAddressPostOffice;
        @SerializedName("residential_address_taluka")
        @Expose
        private String residentialAddressTaluka;
        @SerializedName("residential_address_district")
        @Expose
        private String residentialAddressDistrict;
        @SerializedName("residential_address_state")
        @Expose
        private String residentialAddressState;
        @SerializedName("residential_address_pincode")
        @Expose
        private String residentialAddressPincode;
        @SerializedName("permanent_address_houseNo")
        @Expose
        private String permanentAddressHouseNo;
        @SerializedName("permanent_address_road")
        @Expose
        private String permanentAddressRoad;
        @SerializedName("permanent_address_area")
        @Expose
        private String permanentAddressArea;
        @SerializedName("permanent_address_city")
        @Expose
        private String permanentAddressCity;
        @SerializedName("permanent_address_importantPlace")
        @Expose
        private String permanentAddressImportantPlace;
        @SerializedName("permanent_address_postOffice")
        @Expose
        private String permanentAddressPostOffice;
        @SerializedName("permanent_address_taluka")
        @Expose
        private String permanentAddressTaluka;
        @SerializedName("permanent_address_district")
        @Expose
        private String permanentAddressDistrict;
        @SerializedName("permanent_address_state")
        @Expose
        private String permanentAddressState;
        @SerializedName("permanent_address_pincode")
        @Expose
        private String permanentAddressPincode;
        @SerializedName("mobile")
        @Expose
        private String mobile;
        @SerializedName("aadhaar")
        @Expose
        private String aadhaar;

        public Integer getBocwId() {
            return bocwId;
        }

        public void setBocwId(Integer bocwId) {
            this.bocwId = bocwId;
        }

        public String getPaymentStatus() {
            return paymentStatus;
        }

        public void setPaymentStatus(String paymentStatus) {
            this.paymentStatus = paymentStatus;
        }

        public String getRegistrationNo() {
            return registrationNo;
        }

        public void setRegistrationNo(String registrationNo) {
            this.registrationNo = registrationNo;
        }

        public String getSubscription() {
            return subscription;
        }

        public void setSubscription(String subscription) {
            this.subscription = subscription;
        }

        public String getNextRenewalDate() {
            return nextRenewalDate;
        }

        public void setNextRenewalDate(String nextRenewalDate) {
            this.nextRenewalDate = nextRenewalDate;
        }

        public String getFirstNamePersonal() {
            return firstNamePersonal;
        }

        public void setFirstNamePersonal(String firstNamePersonal) {
            this.firstNamePersonal = firstNamePersonal;
        }

        public String getMiddleNamePersonal() {
            return middleNamePersonal;
        }

        public void setMiddleNamePersonal(String middleNamePersonal) {
            this.middleNamePersonal = middleNamePersonal;
        }

        public String getLastNamePersonal() {
            return lastNamePersonal;
        }

        public void setLastNamePersonal(String lastNamePersonal) {
            this.lastNamePersonal = lastNamePersonal;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getResidentialAddressHouseNo() {
            return residentialAddressHouseNo;
        }

        public void setResidentialAddressHouseNo(String residentialAddressHouseNo) {
            this.residentialAddressHouseNo = residentialAddressHouseNo;
        }

        public String getResidentialAddressRoad() {
            return residentialAddressRoad;
        }

        public void setResidentialAddressRoad(String residentialAddressRoad) {
            this.residentialAddressRoad = residentialAddressRoad;
        }

        public String getResidentialAddressArea() {
            return residentialAddressArea;
        }

        public void setResidentialAddressArea(String residentialAddressArea) {
            this.residentialAddressArea = residentialAddressArea;
        }

        public String getResidentialAddressCity() {
            return residentialAddressCity;
        }

        public void setResidentialAddressCity(String residentialAddressCity) {
            this.residentialAddressCity = residentialAddressCity;
        }

        public String getResidentialAddressImportantPlace() {
            return residentialAddressImportantPlace;
        }

        public void setResidentialAddressImportantPlace(String residentialAddressImportantPlace) {
            this.residentialAddressImportantPlace = residentialAddressImportantPlace;
        }

        public String getResidentialAddressPostOffice() {
            return residentialAddressPostOffice;
        }

        public void setResidentialAddressPostOffice(String residentialAddressPostOffice) {
            this.residentialAddressPostOffice = residentialAddressPostOffice;
        }

        public String getResidentialAddressTaluka() {
            return residentialAddressTaluka;
        }

        public void setResidentialAddressTaluka(String residentialAddressTaluka) {
            this.residentialAddressTaluka = residentialAddressTaluka;
        }

        public String getResidentialAddressDistrict() {
            return residentialAddressDistrict;
        }

        public void setResidentialAddressDistrict(String residentialAddressDistrict) {
            this.residentialAddressDistrict = residentialAddressDistrict;
        }

        public String getResidentialAddressState() {
            return residentialAddressState;
        }

        public void setResidentialAddressState(String residentialAddressState) {
            this.residentialAddressState = residentialAddressState;
        }

        public String getResidentialAddressPincode() {
            return residentialAddressPincode;
        }

        public void setResidentialAddressPincode(String residentialAddressPincode) {
            this.residentialAddressPincode = residentialAddressPincode;
        }

        public String getPermanentAddressHouseNo() {
            return permanentAddressHouseNo;
        }

        public void setPermanentAddressHouseNo(String permanentAddressHouseNo) {
            this.permanentAddressHouseNo = permanentAddressHouseNo;
        }

        public String getPermanentAddressRoad() {
            return permanentAddressRoad;
        }

        public void setPermanentAddressRoad(String permanentAddressRoad) {
            this.permanentAddressRoad = permanentAddressRoad;
        }

        public String getPermanentAddressArea() {
            return permanentAddressArea;
        }

        public void setPermanentAddressArea(String permanentAddressArea) {
            this.permanentAddressArea = permanentAddressArea;
        }

        public String getPermanentAddressCity() {
            return permanentAddressCity;
        }

        public void setPermanentAddressCity(String permanentAddressCity) {
            this.permanentAddressCity = permanentAddressCity;
        }

        public String getPermanentAddressImportantPlace() {
            return permanentAddressImportantPlace;
        }

        public void setPermanentAddressImportantPlace(String permanentAddressImportantPlace) {
            this.permanentAddressImportantPlace = permanentAddressImportantPlace;
        }

        public String getPermanentAddressPostOffice() {
            return permanentAddressPostOffice;
        }

        public void setPermanentAddressPostOffice(String permanentAddressPostOffice) {
            this.permanentAddressPostOffice = permanentAddressPostOffice;
        }

        public String getPermanentAddressTaluka() {
            return permanentAddressTaluka;
        }

        public void setPermanentAddressTaluka(String permanentAddressTaluka) {
            this.permanentAddressTaluka = permanentAddressTaluka;
        }

        public String getPermanentAddressDistrict() {
            return permanentAddressDistrict;
        }

        public void setPermanentAddressDistrict(String permanentAddressDistrict) {
            this.permanentAddressDistrict = permanentAddressDistrict;
        }

        public String getPermanentAddressState() {
            return permanentAddressState;
        }

        public void setPermanentAddressState(String permanentAddressState) {
            this.permanentAddressState = permanentAddressState;
        }

        public String getPermanentAddressPincode() {
            return permanentAddressPincode;
        }

        public void setPermanentAddressPincode(String permanentAddressPincode) {
            this.permanentAddressPincode = permanentAddressPincode;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getAadhaar() {
            return aadhaar;
        }

        public void setAadhaar(String aadhaar) {
            this.aadhaar = aadhaar;
        }

    }
}
