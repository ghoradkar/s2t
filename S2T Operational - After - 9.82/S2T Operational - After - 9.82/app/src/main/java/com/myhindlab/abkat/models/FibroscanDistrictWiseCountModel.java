package com.myhindlab.abkat.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FibroscanDistrictWiseCountModel {

    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("Output")
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

        @SerializedName("VPName")
        @Expose
        private String vPName;
        @SerializedName("DISTLGDCODE")
        @Expose
        private Integer distlgdcode;
        @SerializedName("District")
        @Expose
        private String district;
        @SerializedName("PatientCount")
        @Expose
        private Integer patientCount;
        @SerializedName("AbnormalPatientCount")
        @Expose
        private Integer abnormalPatientCount;

        @SerializedName("PatientID")
        @Expose
        private String patientID;


        public Integer getModerateSevereCount() {
            return moderateSevereCount;
        }

        public void setModerateSevereCount(Integer moderateSevereCount) {
            this.moderateSevereCount = moderateSevereCount;
        }

        @SerializedName("ModerateSevereCount")
        @Expose
        private Integer moderateSevereCount;


        @SerializedName("Stiffness")
        @Expose
        private String stiffness;

        @SerializedName("UAP")
        @Expose
        private String uAP;
        @SerializedName("SuccessfullShots")
        @Expose
        private Integer successfullShots;
        @SerializedName("TotalShots")
        @Expose
        private Integer totalShots;

        public String getVPName() {
            return vPName;
        }

        public void setVPName(String vPName) {
            this.vPName = vPName;
        }

        public String getPatientID() {
            return patientID;
        }

        public void setPatientID(String patientID) {
            this.patientID = patientID;
        }

        public String getStiffness() {
            return stiffness;
        }

        public void setStiffness(String stiffness) {
            this.stiffness = stiffness;
        }

        public String getuAP() {
            return uAP;
        }

        public void setuAP(String uAP) {
            this.uAP = uAP;
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

        public Integer getPatientCount() {
            return patientCount;
        }

        public void setPatientCount(Integer patientCount) {
            this.patientCount = patientCount;
        }

        public Integer getAbnormalPatientCount() {
            return abnormalPatientCount;
        }

        public void setAbnormalPatientCount(Integer abnormalPatientCount) {
            this.abnormalPatientCount = abnormalPatientCount;
        }

        public Integer getSuccessfullShots() {
            return successfullShots;
        }

        public void setSuccessfullShots(Integer successfullShots) {
            this.successfullShots = successfullShots;
        }

        public Integer getTotalShots() {
            return totalShots;
        }

        public void setTotalShots(Integer totalShots) {
            this.totalShots = totalShots;
        }

    }
}