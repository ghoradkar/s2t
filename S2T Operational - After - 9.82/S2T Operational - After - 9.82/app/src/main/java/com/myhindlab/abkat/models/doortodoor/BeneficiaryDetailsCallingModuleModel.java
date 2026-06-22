package com.myhindlab.abkat.models.doortodoor;


import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BeneficiaryDetailsCallingModuleModel {

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

        @SerializedName("AssignCallID")
        @Expose
        private Integer assignCallID;
        @SerializedName("BeneficiaryNo")
        @Expose
        private Long beneficiaryNo;
        @SerializedName("DIVID")
        @Expose
        private Integer divid;
        @SerializedName("Division")
        @Expose
        private String division;
        @SerializedName("DISTLGDCODE")
        @Expose
        private Integer distlgdcode;
        @SerializedName("District")
        @Expose
        private String district;
        @SerializedName("TALLGDCODE")
        @Expose
        private Integer tallgdcode;
        @SerializedName("Taluka")
        @Expose
        private String taluka;
        @SerializedName("HouseNo")
        @Expose
        private String houseNo;
        @SerializedName("Road")
        @Expose
        private String road;
        @SerializedName("LandMark")
        @Expose
        private String landMark;
        @SerializedName("Area")
        @Expose
        private String area;
        @SerializedName("RegAddress")
        @Expose
        private String regAddress;
        @SerializedName("Pincode")
        @Expose
        private Integer pincode;
        @SerializedName("InsertUpdate")
        @Expose
        private Integer insertUpdate;
        @SerializedName("IsAddressChanged")
        @Expose
        private Object isAddressChanged;
        @SerializedName("AppoinmentDate")
        @Expose
        private Object appoinmentDate;
        @SerializedName("AppoinmentTime")
        @Expose
        private Object appoinmentTime;

        @SerializedName("FirstName")
        @Expose
        private String firstName;


        @SerializedName("MiddleName")
        @Expose
        private String middleName;

        @SerializedName("AltMobileNo")
        private String altMobileNo;

        public String getAltMobileNo() {
            return altMobileNo;
        }

        public void setAltMobileNo(String altMobileNo) {
            this.altMobileNo = altMobileNo;
        }

        @SerializedName("WorkersMaritalStatus")
        private String workersMaritalStatus;

        @SerializedName("Gender")
        private String gender;

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        @SerializedName("WorkerScreeninPending")
        private String workerScreeninPending;

        public String getWorkersMaritalStatus() {
            return workersMaritalStatus;
        }

        public void setWorkersMaritalStatus(String workersMaritalStatus) {
            this.workersMaritalStatus = workersMaritalStatus;
        }

        public String getWorkerScreeninPending() {
            return workerScreeninPending;
        }

        public void setWorkerScreeninPending(String workerScreeninPending) {
            this.workerScreeninPending = workerScreeninPending;
        }

        public String getIsWorkerScreened() {
            return isWorkerScreened;
        }

        public void setIsWorkerScreened(String isWorkerScreened) {
            this.isWorkerScreened = isWorkerScreened;
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

        @SerializedName("IsWorkerScreened")
        private String isWorkerScreened;

        @SerializedName("Remark")
        private String remark;


        @SerializedName("WorkersGender")
        private String workersGender;



        public String getCallingLog() {
            return callingLog;
        }

        public void setCallingLog(String callingLog) {
            this.callingLog = callingLog;
        }

        @SerializedName("CallingLog")
        @Expose
        private String callingLog;


        @SerializedName("RemarkID")
        @Expose
        private String remarkID;

        public String getRemarkID() {
            return remarkID;
        }

        public void setRemarkID(String remarkID) {
            this.remarkID = remarkID;
        }

        public String  getFirstName() {
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

        @SerializedName("LastName")
        @Expose
        private String lastName;

        public Integer getAssignCallID() {
            return assignCallID;
        }

        public void setAssignCallID(Integer assignCallID) {
            this.assignCallID = assignCallID;
        }

        public Long getBeneficiaryNo() {
            return beneficiaryNo;
        }

        public void setBeneficiaryNo(Long beneficiaryNo) {
            this.beneficiaryNo = beneficiaryNo;
        }

        public Integer getDivid() {
            return divid;
        }

        public void setDivid(Integer divid) {
            this.divid = divid;
        }

        public String getDivision() {
            return division;
        }

        public void setDivision(String division) {
            this.division = division;
        }

        public Integer getDistlgdcode() {
            return distlgdcode;
        }

        public void setDistlgdcode(Integer distlgdcode) {
            this.distlgdcode = distlgdcode;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public Integer getTallgdcode() {
            return tallgdcode;
        }

        public void setTallgdcode(Integer tallgdcode) {
            this.tallgdcode = tallgdcode;
        }

        public String getTaluka() {
            return taluka;
        }

        public void setTaluka(String taluka) {
            this.taluka = taluka;
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

        public String getRegAddress() {
            return regAddress;
        }

        public void setRegAddress(String regAddress) {
            this.regAddress = regAddress;
        }

        public Integer getPincode() {
            return pincode;
        }

        public void setPincode(Integer pincode) {
            this.pincode = pincode;
        }

        public Integer getInsertUpdate() {
            return insertUpdate;
        }

        public void setInsertUpdate(Integer insertUpdate) {
            this.insertUpdate = insertUpdate;
        }

        public Object getIsAddressChanged() {
            return isAddressChanged;
        }

        public void setIsAddressChanged(Object isAddressChanged) {
            this.isAddressChanged = isAddressChanged;
        }

        public Object getAppoinmentDate() {
            return appoinmentDate;
        }

        public void setAppoinmentDate(Object appoinmentDate) {
            this.appoinmentDate = appoinmentDate;
        }

        public Object getAppoinmentTime() {
            return appoinmentTime;
        }

        public void setAppoinmentTime(Object appoinmentTime) {
            this.appoinmentTime = appoinmentTime;
        }

    }
}