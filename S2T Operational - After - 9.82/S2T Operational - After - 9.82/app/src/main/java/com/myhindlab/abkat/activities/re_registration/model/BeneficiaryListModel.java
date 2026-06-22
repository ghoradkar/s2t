package com.myhindlab.abkat.activities.re_registration.model;


import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class BeneficiaryListModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("output")
    @Expose
    private List<Output> output;

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


    public class Output {

        @SerializedName("BeneficiaryNumber")
        @Expose
        private String beneficiaryNumber;
        @SerializedName("BeneficiaryName")
        @Expose
        private String beneficiaryName;
        @SerializedName("Area")
        @Expose
        private String area;
        @SerializedName("MobileNo")
        @Expose
        private String mobileNo;
        @SerializedName("Pincode")
        @Expose
        private String pincode;
        @SerializedName("Arid")
        @Expose
        private Integer arid;

        @SerializedName("RegdNo")
        @Expose
        private String regdNo;

        public String getRegdNo() {
            return regdNo;
        }

        public void setRegdNo(String regdNo) {
            this.regdNo = regdNo;
        }

        @SerializedName("RelationWithWorker")
        @Expose
        private String relationWithWorker;


        @SerializedName("AppointmentDate")
        @Expose
        private String appointmentDate;

        @SerializedName("Remarks")
        @Expose
        private String remarks;

        public String getNextRenewalDate() {
            return nextRenewalDate;
        }

        public void setNextRenewalDate(String nextRenewalDate) {
            this.nextRenewalDate = nextRenewalDate;
        }

        @SerializedName("NextRenewalDate")
        @Expose
        private String nextRenewalDate;

        @SerializedName("RejectedLabName")
        @Expose
        private String rejectedLabName;

        public String getRejectedLabName() {
            return rejectedLabName;
        }

        public void setRejectedLabName(String rejectedLabName) {
            this.rejectedLabName = rejectedLabName;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public String getAppointmentDate() {
            return appointmentDate;
        }

        public void setAppointmentDate(String appointmentDate) {
            this.appointmentDate = appointmentDate;
        }

        public String getRelationWithWorker() {
            return relationWithWorker;
        }

        public void setRelationWithWorker(String relationWithWorker) {
            this.relationWithWorker = relationWithWorker;
        }

        @SerializedName("Rej_Regdid")
        @Expose
        private String rej_Regdid;

        public String getRej_Regdid() {
            return rej_Regdid;
        }

        public void setRej_Regdid(String rej_Regdid) {
            this.rej_Regdid = rej_Regdid;
        }

        @SerializedName("Address")
        @Expose
        private String address;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCampType() {
            return campType;
        }

        public void setCampType(String campType) {
            this.campType = campType;
        }

        public String getCampTypeDescription() {
            return campTypeDescription;
        }

        public void setCampTypeDescription(String campTypeDescription) {
            this.campTypeDescription = campTypeDescription;
        }

        public String getRejCampID() {
            return rejCampID;
        }

        public void setRejCampID(String rejCampID) {
            this.rejCampID = rejCampID;
        }

        public String getRejCampDate() {
            return rejCampDate;
        }

        public void setRejCampDate(String rejCampDate) {
            this.rejCampDate = rejCampDate;
        }

        public String getIsRejectedFrom() {
            return isRejectedFrom;
        }

        public void setIsRejectedFrom(String isRejectedFrom) {
            this.isRejectedFrom = isRejectedFrom;
        }

        public String getRejectedFrom() {
            return rejectedFrom;
        }

        public void setRejectedFrom(String rejectedFrom) {
            this.rejectedFrom = rejectedFrom;
        }

        public String getRejectedReason() {
            return rejectedReason;
        }

        public void setRejectedReason(String rejectedReason) {
            this.rejectedReason = rejectedReason;
        }

        public String getRejectionDate() {
            return rejectionDate;
        }

        public void setRejectionDate(String rejectionDate) {
            this.rejectionDate = rejectionDate;
        }

        public Integer getTeamID() {
            return teamID;
        }

        public void setTeamID(Integer teamID) {
            this.teamID = teamID;
        }

        public String getTeamName() {
            return teamName;
        }

        public void setTeamName(String teamName) {
            this.teamName = teamName;
        }

        public String getUseridm1() {
            return useridm1;
        }

        public void setUseridm1(String useridm1) {
            this.useridm1 = useridm1;
        }

        public String getUsernamem1() {
            return usernamem1;
        }

        public void setUsernamem1(String usernamem1) {
            this.usernamem1 = usernamem1;
        }

        public String getMobnom1() {
            return mobnom1;
        }

        public void setMobnom1(String mobnom1) {
            this.mobnom1 = mobnom1;
        }

        public String getUseridm2() {
            return useridm2;
        }

        public void setUseridm2(String useridm2) {
            this.useridm2 = useridm2;
        }

        public String getUsernamem2() {
            return usernamem2;
        }

        public void setUsernamem2(String usernamem2) {
            this.usernamem2 = usernamem2;
        }

        public String getMobnom2() {
            return mobnom2;
        }

        public void setMobnom2(String mobnom2) {
            this.mobnom2 = mobnom2;
        }

        public String getIsTeamActive() {
            return isTeamActive;
        }

        public void setIsTeamActive(String isTeamActive) {
            this.isTeamActive = isTeamActive;
        }

        public String getIsTeamMapped() {
            return isTeamMapped;
        }

        public void setIsTeamMapped(String isTeamMapped) {
            this.isTeamMapped = isTeamMapped;
        }

        @SerializedName("CampType")
        @Expose
        private String campType;
        @SerializedName("CampTypeDescription")
        @Expose
        private String campTypeDescription;
        @SerializedName("Rej_CampID")
        @Expose
        private String rejCampID;
        @SerializedName("Rej_CampDate")
        @Expose
        private String rejCampDate;
        @SerializedName("IsRejectedFrom")
        @Expose
        private String isRejectedFrom;
        @SerializedName("RejectedFrom")
        @Expose
        private String rejectedFrom;
        @SerializedName("RejectedReason")
        @Expose
        private String rejectedReason;

        public String getIsAppointmentConfirm() {
            return isAppointmentConfirm;
        }

        public void setIsAppointmentConfirm(String isAppointmentConfirm) {
            this.isAppointmentConfirm = isAppointmentConfirm;
        }

        @SerializedName("IsAppointmentConfirm")
        @Expose
        private String isAppointmentConfirm;
        @SerializedName("RejectionDate")
        @Expose
        private String rejectionDate;
        @SerializedName("TeamID")
        @Expose
        private Integer teamID;
        @SerializedName("TeamName")
        @Expose
        private String teamName;
        @SerializedName("USERIDM1")
        @Expose
        private String useridm1;
        @SerializedName("USERNAMEM1")
        @Expose
        private String usernamem1;
        @SerializedName("MOBNOM1")
        @Expose
        private String mobnom1;
        @SerializedName("USERIDM2")
        @Expose
        private String useridm2;
        @SerializedName("USERNAMEM2")
        @Expose
        private String usernamem2;
        @SerializedName("MOBNOM2")
        @Expose
        private String mobnom2;
        @SerializedName("IsTeamActive")
        @Expose
        private String isTeamActive;
        @SerializedName("IsTeamMapped")
        @Expose
        private String isTeamMapped;


        public String getMemberUserID1() {
            return memberUserID1;
        }

        public void setMemberUserID1(String memberUserID1) {
            this.memberUserID1 = memberUserID1;
        }

        public String getMemberUserID2() {
            return memberUserID2;
        }

        public void setMemberUserID2(String memberUserID2) {
            this.memberUserID2 = memberUserID2;
        }

        @SerializedName("MemberUserID1")
        @Expose
        private String memberUserID1;

        @SerializedName("MemberUserID2")
        @Expose
        private String memberUserID2;


        public String getCampDate() {
            return campDate;
        }

        public void setCampDate(String campDate) {
            this.campDate = campDate;
        }

        @SerializedName("CampDate")
        @Expose
        private String campDate;


        public String getTaluka() {
            return taluka;
        }

        public void setTaluka(String taluka) {
            this.taluka = taluka;
        }

        @SerializedName("Taluka")
        @Expose
        private String taluka;


        public String getBeneficiaryNumber() {
            return beneficiaryNumber;
        }

        public void setBeneficiaryNumber(String beneficiaryNumber) {
            this.beneficiaryNumber = beneficiaryNumber;
        }

        public String getBeneficiaryName() {
            return beneficiaryName;
        }

        public void setBeneficiaryName(String beneficiaryName) {
            this.beneficiaryName = beneficiaryName;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getMobileNo() {
            return mobileNo;
        }

        public void setMobileNo(String mobileNo) {
            this.mobileNo = mobileNo;
        }

        public String getPincode() {
            return pincode;
        }

        public void setPincode(String pincode) {
            this.pincode = pincode;
        }

        public Integer getArid() {
            return arid;
        }

        public void setArid(Integer arid) {
            this.arid = arid;
        }

    }
}