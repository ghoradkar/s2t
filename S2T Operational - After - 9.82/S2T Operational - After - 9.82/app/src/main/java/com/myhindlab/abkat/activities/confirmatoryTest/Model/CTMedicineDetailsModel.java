package com.myhindlab.abkat.activities.confirmatoryTest.Model;


import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CTMedicineDetailsModel {

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

        @SerializedName("Regdid")
        @Expose
        private Integer regdid;
        @SerializedName("Regdno")
        @Expose
        private String regdno;
        @SerializedName("Patient_Name")
        @Expose
        private String patientName;
        @SerializedName("CTTeamid")
        @Expose
        private String cTTeamid;
        @SerializedName("CTTeamName")
        @Expose
        private String cTTeamName;
        @SerializedName("ArId")
        @Expose
        private Integer arId;
        @SerializedName("CTStatus")
        @Expose
        private String cTStatus;
        @SerializedName("OverallStatusID")
        @Expose
        private Integer overallStatusID;
        @SerializedName("MDStatus")
        @Expose
        private String mDStatus;

        public String getmDDeliveryExecutiveID() {
            return mDDeliveryExecutiveID;
        }

        public void setmDDeliveryExecutiveID(String mDDeliveryExecutiveID) {
            this.mDDeliveryExecutiveID = mDDeliveryExecutiveID;
        }

        @SerializedName("MDDeliveryExecutiveID")
        @Expose
        private String mDDeliveryExecutiveID;

        @SerializedName("MDDeliveryExecutiveName")
        @Expose
        private String mDDeliveryExecutiveName;

        public String getmDDeliveryExecutiveName() {
            return mDDeliveryExecutiveName;
        }

        public void setmDDeliveryExecutiveName(String mDDeliveryExecutiveName) {
            this.mDDeliveryExecutiveName = mDDeliveryExecutiveName;
        }

        @SerializedName("MDTeamId")
        @Expose
        private Integer mDTeamId;
        @SerializedName("MDTeamName")
        @Expose
        private String mDTeamName;
        @SerializedName("CTPrescribedDate")
        @Expose
        private String cTPrescribedDate;
        @SerializedName("MDPrescribedDate")
        @Expose
        private String mDPrescribedDate;
        @SerializedName("PinCode")
        @Expose
        private String pinCode;
        @SerializedName("Area")
        @Expose
        private String area;
        @SerializedName("Address")
        @Expose
        private String address;

        public Integer getRegdid() {
            return regdid;
        }

        public void setRegdid(Integer regdid) {
            this.regdid = regdid;
        }

        public String getRegdno() {
            return regdno;
        }

        public void setRegdno(String regdno) {
            this.regdno = regdno;
        }

        public String getPatientName() {
            return patientName;
        }

        public void setPatientName(String patientName) {
            this.patientName = patientName;
        }

        public String getCTTeamid() {
            return cTTeamid;
        }

        public void setCTTeamid(String cTTeamid) {
            this.cTTeamid = cTTeamid;
        }

        public String getCTTeamName() {
            return cTTeamName;
        }

        public void setCTTeamName(String cTTeamName) {
            this.cTTeamName = cTTeamName;
        }

        public Integer getArId() {
            return arId;
        }

        public void setArId(Integer arId) {
            this.arId = arId;
        }

        public String getCTStatus() {
            return cTStatus;
        }

        public void setCTStatus(String cTStatus) {
            this.cTStatus = cTStatus;
        }

        public Integer getOverallStatusID() {
            return overallStatusID;
        }

        public void setOverallStatusID(Integer overallStatusID) {
            this.overallStatusID = overallStatusID;
        }

        public String getMDStatus() {
            return mDStatus;
        }

        public void setMDStatus(String mDStatus) {
            this.mDStatus = mDStatus;
        }

        public Integer getMDTeamId() {
            return mDTeamId;
        }

        public void setMDTeamId(Integer mDTeamId) {
            this.mDTeamId = mDTeamId;
        }

        public String getMDTeamName() {
            return mDTeamName;
        }

        public void setMDTeamName(String mDTeamName) {
            this.mDTeamName = mDTeamName;
        }

        public String getCTPrescribedDate() {
            return cTPrescribedDate;
        }

        public void setCTPrescribedDate(String cTPrescribedDate) {
            this.cTPrescribedDate = cTPrescribedDate;
        }

        public String getMDPrescribedDate() {
            return mDPrescribedDate;
        }

        public void setMDPrescribedDate(String mDPrescribedDate) {
            this.mDPrescribedDate = mDPrescribedDate;
        }

        public String getPinCode() {
            return pinCode;
        }

        public void setPinCode(String pinCode) {
            this.pinCode = pinCode;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

    }
}