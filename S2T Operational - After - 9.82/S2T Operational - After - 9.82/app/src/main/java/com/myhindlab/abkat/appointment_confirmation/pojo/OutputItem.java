package com.myhindlab.abkat.appointment_confirmation.pojo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OutputItem implements Serializable {

    @SerializedName("ColourSrNo")
    private int colourSrNo;

    @SerializedName("AssignStatusID")
    private int assignStatusID;

    @SerializedName("AssignCallID")
    private int assignCallID;

    @SerializedName("BeneficiaryNo")
    private String beneficiaryNo;

    @SerializedName("BeneficiaryName")
    private String beneficiaryName;

    @SerializedName("PhleboRemark")
    private String phleboRemark;

    public String getPhleboRemark() {
        return phleboRemark;
    }

    public void setPhleboRemark(String phleboRemark) {
        this.phleboRemark = phleboRemark;
    }

    @SerializedName("Age")
    private String age;

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    @SerializedName("Mobile")
    private String mobile;

    @SerializedName("AssignStatus")
    private String assignStatus;

    @SerializedName("Pincode")
    private int pincode;

    @SerializedName("NextRenewalDate")
    private String nextRenewalDate;

    @SerializedName("PhleboRemarkID")
    private String phleboRemarkID;

    public String getPhleboRemarkID() {
        return phleboRemarkID;
    }

    public void setPhleboRemarkID(String phleboRemarkID) {
        this.phleboRemarkID = phleboRemarkID;
    }

    @SerializedName("Gender")
    private String gender;

    @SerializedName("GroupID")
    private String groupID;


    @SerializedName("CallingStatus")
    private String CallingStatus;

    public String getCallingStatus() {
        return CallingStatus;
    }

    public void setCallingStatus(String callingStatus) {
        CallingStatus = callingStatus;
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    @SerializedName("FirstName")
    private String firstName;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @SerializedName("MiddleName")
    private String middleName;

    @SerializedName("LastName")
    private String lastName;

    @SerializedName("HouseNo")
    private String houseNo;

    @SerializedName("Road")
    private String road;

    @SerializedName("LandMark")
    private String landMark;

    @SerializedName("Area")
    private String area;

    public Integer getAppointmentCount() {
        return appointmentCount;
    }

    public void setAppointmentCount(Integer appointmentCount) {
        this.appointmentCount = appointmentCount;
    }

    @SerializedName("AppointmentCount")
    private Integer appointmentCount;


    @SerializedName("DISTLGDCODE")
    private String dISTLGDCODE;

    @SerializedName("District")
    private String district;

    @SerializedName("DependantScreeningPending")
    private String dependantScreeningPending;

    @SerializedName("WorkerScreeninPending")
    private String workerScreeninPending;

    @SerializedName("LastScreeningDate")
    private String lastScreeningDate;

    public String getLastScreeningDate() {
        return lastScreeningDate;
    }

    public void setLastScreeningDate(String lastScreeningDate) {
        this.lastScreeningDate = lastScreeningDate;
    }

    public String getWorkerScreeninPending() {
        return workerScreeninPending;
    }

    public void setWorkerScreeninPending(String workerScreeninPending) {
        this.workerScreeninPending = workerScreeninPending;
    }

    @SerializedName("InsertUpdate")
    private String insertUpdate;

    @SerializedName("IsWorkerScreened")
    private String isWorkerScreened;


    public String getIsWorkerScreened() {
        return isWorkerScreened;
    }

    public void setIsWorkerScreened(String isWorkerScreened) {
        this.isWorkerScreened = isWorkerScreened;
    }

    @SerializedName("IsAddressChanged")
    private String isAddressChanged;

    @SerializedName("AltMobileNo")
    private String altMobileNo;

    @SerializedName("NoOfDependants")
    private String noOfDependants;

    @SerializedName("AppoinmentDate")
    private String appoinmentDate;

    @SerializedName("AppoinmentTime")
    private String appoinmentTime;

    @SerializedName("ConcatNameMobile")
    private String concatNameMobile;

    public String getCallingLog() {
        return callingLog;
    }

    public void setCallingLog(String callingLog) {
        this.callingLog = callingLog;
    }

    @SerializedName("CallingLog")
    private String callingLog;

    public String getConcatNameMobile() {
        return concatNameMobile;
    }

    public void setConcatNameMobile(String concatNameMobile) {
        this.concatNameMobile = concatNameMobile;
    }

    @SerializedName("Remark")
    private String remark;

    public void setAssignCallID(int assignCallID) {
        this.assignCallID = assignCallID;
    }

    public String getInsertUpdate() {
        return insertUpdate;
    }

    public void setInsertUpdate(String insertUpdate) {
        this.insertUpdate = insertUpdate;
    }

    public String getIsAddressChanged() {
        return isAddressChanged;
    }

    public void setIsAddressChanged(String isAddressChanged) {
        this.isAddressChanged = isAddressChanged;
    }

    public String getAltMobileNo() {
        return altMobileNo;
    }

    public void setAltMobileNo(String altMobileNo) {
        this.altMobileNo = altMobileNo;
    }

    public String getNoOfDependants() {
        return noOfDependants;
    }

    public void setNoOfDependants(String noOfDependants) {
        this.noOfDependants = noOfDependants;
    }

    public String getAppoinmentDate() {
        return appoinmentDate;
    }

    public void setAppoinmentDate(String appoinmentDate) {
        this.appoinmentDate = appoinmentDate;
    }

    public String getAppoinmentTime() {
        return appoinmentTime;
    }

    public void setAppoinmentTime(String appoinmentTime) {
        this.appoinmentTime = appoinmentTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getWorkersGender() {
        return workersGender;
    }

    public void setWorkersGender(String workersGender) {
        this.workersGender = workersGender;
    }

    public String getWorkersMaritalStatus() {
        return workersMaritalStatus;
    }

    public void setWorkersMaritalStatus(String workersMaritalStatus) {
        this.workersMaritalStatus = workersMaritalStatus;
    }

    @SerializedName("WorkersGender")
    private String workersGender;

    @SerializedName("WorkersMaritalStatus")
    private String workersMaritalStatus;

    public void setAssignStatusID(int assignStatusID) {
        this.assignStatusID = assignStatusID;
    }

    public String getDependantScreeningPending() {
        return dependantScreeningPending;
    }

    public void setDependantScreeningPending(String dependantScreeningPending) {
        this.dependantScreeningPending = dependantScreeningPending;
    }

    public String getAppoinmentDateTime() {
        return appoinmentDateTime;
    }

    public void setAppoinmentDateTime(String appoinmentDateTime) {
        this.appoinmentDateTime = appoinmentDateTime;
    }

    @SerializedName("AppoinmentDateTime")
    private String appoinmentDateTime;

    public void setBeneficiaryNo(String beneficiaryNo) {
        this.beneficiaryNo = beneficiaryNo;
    }

    public String getD2DCallingRemarkID() {
        return d2DCallingRemarkID;
    }

    public void setD2DCallingRemarkID(String d2DCallingRemarkID) {
        this.d2DCallingRemarkID = d2DCallingRemarkID;
    }

    public String getD2DCallingRemark() {
        return d2DCallingRemark;
    }

    public void setD2DCallingRemark(String d2DCallingRemark) {
        this.d2DCallingRemark = d2DCallingRemark;
    }

    @SerializedName("TALLGDCODE")
    private String tALLGDCODE;

    @SerializedName("D2DCallingRemarkID")
    private String d2DCallingRemarkID;

    @SerializedName("D2DCallingRemark")
    private String d2DCallingRemark;

    public void setColourSrNo(int colourSrNo) {
        this.colourSrNo = colourSrNo;
    }

    public String getdISTLGDCODE() {
        return dISTLGDCODE;
    }

    public void setdISTLGDCODE(String dISTLGDCODE) {
        this.dISTLGDCODE = dISTLGDCODE;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String gettALLGDCODE() {
        return tALLGDCODE;
    }

    public void settALLGDCODE(String tALLGDCODE) {
        this.tALLGDCODE = tALLGDCODE;
    }

    public String getTaluka() {
        return taluka;
    }

    public void setTaluka(String taluka) {
        this.taluka = taluka;
    }

    @SerializedName("Taluka")
    private String taluka;


    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHouseNo() {
        return houseNo;
    }

    public void setHouseNo(String houseNo) {
        this.houseNo = houseNo;
    }

    public String getRoad() {
        return road;
    }

    public void setRoad(String road) {
        this.road = road;
    }

    public String getLandMark() {
        return landMark;
    }

    public void setLandMark(String landMark) {
        this.landMark = landMark;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    @SerializedName("RegAddress")
    private String regAddress;

    public int getColourSrNo() {
        return colourSrNo;
    }

    public int getAssignStatusID() {
        return assignStatusID;
    }

    public int getAssignCallID() {
        return assignCallID;
    }

    public String getBeneficiaryNo() {
        return beneficiaryNo;
    }

    public String getBeneficiaryName() {
        return beneficiaryName;
    }

    public String getMobile() {
        return mobile;
    }

    public String getAssignStatus() {
        return assignStatus;
    }

    public int getPincode() {
        return pincode;
    }

    public String getNextRenewalDate() {
        return nextRenewalDate;
    }

    public String getRegAddress() {
        return regAddress;
    }
}