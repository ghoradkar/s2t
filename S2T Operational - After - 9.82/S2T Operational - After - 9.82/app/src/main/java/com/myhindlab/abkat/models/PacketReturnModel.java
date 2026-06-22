package com.myhindlab.abkat.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


import java.util.List;

public class PacketReturnModel {

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

        @SerializedName("MedicalDelivaryID")
        @Expose
        private Integer medicalDelivaryID;
        @SerializedName("TreatmentID")
        @Expose
        private Integer treatmentID;
        @SerializedName("RegdId")
        @Expose
        private Integer regdId;
        @SerializedName("Beneficiry_Number")
        @Expose
        private String beneficiryNumber;
        @SerializedName("Patient_Name")
        @Expose
        private String patientName;
        @SerializedName("PacketNo")
        @Expose
        private String packetNo;
        @SerializedName("DeliveryChallanID")
        @Expose
        private String deliveryChallanID;
        @SerializedName("DeniedDate")
        @Expose
        private String deniedDate;
        @SerializedName("DISTLGDCODE")
        @Expose
        private Integer distlgdcode;
        @SerializedName("TALLGDCODE")
        @Expose
        private Integer tallgdcode;

        public String getReturnStatus() {
            return returnStatus;
        }

        public void setReturnStatus(String returnStatus) {
            this.returnStatus = returnStatus;
        }

        @SerializedName("ReturnStatus")
        @Expose
        private String returnStatus;



        private boolean isChecked;

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        public Integer getMedicalDelivaryID() {
            return medicalDelivaryID;
        }

        public void setMedicalDelivaryID(Integer medicalDelivaryID) {
            this.medicalDelivaryID = medicalDelivaryID;
        }

        public Integer getTreatmentID() {
            return treatmentID;
        }

        public void setTreatmentID(Integer treatmentID) {
            this.treatmentID = treatmentID;
        }

        public Integer getRegdId() {
            return regdId;
        }

        public void setRegdId(Integer regdId) {
            this.regdId = regdId;
        }

        public String getBeneficiryNumber() {
            return beneficiryNumber;
        }

        public void setBeneficiryNumber(String beneficiryNumber) {
            this.beneficiryNumber = beneficiryNumber;
        }

        public String getPatientName() {
            return patientName;
        }

        public void setPatientName(String patientName) {
            this.patientName = patientName;
        }

        public String getPacketNo() {
            return packetNo;
        }

        public void setPacketNo(String packetNo) {
            this.packetNo = packetNo;
        }

        public String getDeliveryChallanID() {
            return deliveryChallanID;
        }

        public void setDeliveryChallanID(String deliveryChallanID) {
            this.deliveryChallanID = deliveryChallanID;
        }

        public String getDeniedDate() {
            return deniedDate;
        }

        public void setDeniedDate(String deniedDate) {
            this.deniedDate = deniedDate;
        }

        public Integer getDistlgdcode() {
            return distlgdcode;
        }

        public void setDistlgdcode(Integer distlgdcode) {
            this.distlgdcode = distlgdcode;
        }

        public Integer getTallgdcode() {
            return tallgdcode;
        }

        public void setTallgdcode(Integer tallgdcode) {
            this.tallgdcode = tallgdcode;
        }

    }

}