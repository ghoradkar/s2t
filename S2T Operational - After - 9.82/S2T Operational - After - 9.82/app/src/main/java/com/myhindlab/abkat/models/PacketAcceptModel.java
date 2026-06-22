package com.myhindlab.abkat.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class PacketAcceptModel {

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

        @SerializedName("DISTLGDCODE")
        @Expose
        private Integer distlgdcode;
        @SerializedName("District")
        @Expose
        private String district;
        @SerializedName("TALLGDCODE")
        @Expose
        private Object tallgdcode;
        @SerializedName("Taluka")
        @Expose
        private String taluka;
        @SerializedName("Labcode")
        @Expose
        private Integer labcode;
        @SerializedName("LabName")
        @Expose
        private String labName;
        @SerializedName("PINCODE")
        @Expose
        private Integer pincode;
        @SerializedName("PacketNumber")
        @Expose
        private String packetNumber;
        @SerializedName("PrescriptionID")
        @Expose
        private Integer prescriptionID;

        public String getOverallStatusID() {
            return overallStatusID;
        }

        public void setOverallStatusID(String overallStatusID) {
            this.overallStatusID = overallStatusID;
        }

        public String getOverallStatus() {
            return overallStatus;
        }

        public void setOverallStatus(String overallStatus) {
            this.overallStatus = overallStatus;
        }

        @SerializedName("PatientName")
        @Expose
        private String patientName;

        @SerializedName("OverallStatusID")
        @Expose
        private String overallStatusID;

        @SerializedName("OverallStatus")
        @Expose
        private String overallStatus;


        @SerializedName("DeliveryChallanID")
        @Expose
        private String deliveryChallanID;

        public String getDeliveryChallanID() {
            return deliveryChallanID;
        }

        public void setDeliveryChallanID(String deliveryChallanID) {
            this.deliveryChallanID = deliveryChallanID;
        }

        private boolean isChecked;

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
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

        public Object getTallgdcode() {
            return tallgdcode;
        }

        public void setTallgdcode(Object tallgdcode) {
            this.tallgdcode = tallgdcode;
        }

        public String getTaluka() {
            return taluka;
        }

        public void setTaluka(String taluka) {
            this.taluka = taluka;
        }

        public Integer getLabcode() {
            return labcode;
        }

        public void setLabcode(Integer labcode) {
            this.labcode = labcode;
        }

        public String getLabName() {
            return labName;
        }

        public void setLabName(String labName) {
            this.labName = labName;
        }

        public Integer getPincode() {
            return pincode;
        }

        public void setPincode(Integer pincode) {
            this.pincode = pincode;
        }

        public String getPacketNumber() {
            return packetNumber;
        }

        public void setPacketNumber(String packetNumber) {
            this.packetNumber = packetNumber;
        }

        public Integer getPrescriptionID() {
            return prescriptionID;
        }

        public void setPrescriptionID(Integer prescriptionID) {
            this.prescriptionID = prescriptionID;
        }

        public String getPatientName() {
            return patientName;
        }

        public void setPatientName(String patientName) {
            this.patientName = patientName;
        }

    }

}