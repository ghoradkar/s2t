package com.myhindlab.abkat.activities.confirmatoryTest.Model;


import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommonBeneficiaryListModel implements Serializable {

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


    public class Output implements Serializable {

        @SerializedName("Regdid")
        @Expose
        private Integer regdid;
        @SerializedName("Regdno")
        @Expose
        private String regdno;
        @SerializedName("Patient_Name")
        @Expose
        private String patientName;
        @SerializedName("ArId")
        @Expose
        private Integer arId;
        @SerializedName("CTStatus")
        @Expose
        private String cTStatus;
        @SerializedName("OverallStatusID")
        @Expose
        private Integer overallStatusID;
        @SerializedName("MedicineStatus")
        @Expose
        private String medicineStatus;
        @SerializedName("Pincode")
        @Expose
        private String pincode;
        @SerializedName("Teamid")
        @Expose
        private String teamid;
        @SerializedName("Area")
        @Expose
        private String area;
        @SerializedName("TALLGDCODE")
        @Expose
        private Integer tallgdcode;
        @SerializedName("Taluka")
        @Expose
        private String taluka;

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

        public String getMedicineStatus() {
            return medicineStatus;
        }

        public void setMedicineStatus(String medicineStatus) {
            this.medicineStatus = medicineStatus;
        }

        public String getPincode() {
            return pincode;
        }

        public void setPincode(String pincode) {
            this.pincode = pincode;
        }

        public String getTeamid() {
            return teamid;
        }

        public void setTeamid(String teamid) {
            this.teamid = teamid;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
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

    }

}