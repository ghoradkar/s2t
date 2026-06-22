package com.myhindlab.abkat.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class SuperAdminCountModel {

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
    private Details details;

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

    public Details getDetails() {
        return details;
    }

    public void setDetails(Details details) {
        this.details = details;
    }


    public class Details {

        @SerializedName("treatmentGivenHSCC")
        @Expose
        private Integer treatmentGivenHSCC;
        @SerializedName("treatmentGivenHLL")
        @Expose
        private Integer treatmentGivenHLL;
        @SerializedName("treatmentGivenTotal")
        @Expose
        private Integer treatmentGivenTotal;
        @SerializedName("ipdRegisteredHSCC")
        @Expose
        private Integer ipdRegisteredHSCC;
        @SerializedName("ipdRegisteredHLL")
        @Expose
        private Integer ipdRegisteredHLL;
        @SerializedName("ipdRegisteredTotal")
        @Expose
        private Integer ipdRegisteredTotal;
        @SerializedName("dischargePatientHSCC")
        @Expose
        private Integer dischargePatientHSCC;
        @SerializedName("dischargePatientHLL")
        @Expose
        private Integer dischargePatientHLL;
        @SerializedName("dischargePatientTotal")
        @Expose
        private Integer dischargePatientTotal;
        @SerializedName("prescriptionGivenHSCC")
        @Expose
        private Integer prescriptionGivenHSCC;
        @SerializedName("prescriptionGivenHLL")
        @Expose
        private Integer prescriptionGivenHLL;
        @SerializedName("prescriptionGivenTotal")
        @Expose
        private Integer prescriptionGivenTotal;
        @SerializedName("prescriptionIssuedHSCC")
        @Expose
        private Integer prescriptionIssuedHSCC;
        @SerializedName("prescriptionIssuedHLL")
        @Expose
        private Integer prescriptionIssuedHLL;
        @SerializedName("prescriptionIssuedTotal")
        @Expose
        private Integer prescriptionIssuedTotal;

        public Integer getTreatmentGivenHSCC() {
            return treatmentGivenHSCC;
        }

        public void setTreatmentGivenHSCC(Integer treatmentGivenHSCC) {
            this.treatmentGivenHSCC = treatmentGivenHSCC;
        }

        public Integer getTreatmentGivenHLL() {
            return treatmentGivenHLL;
        }

        public void setTreatmentGivenHLL(Integer treatmentGivenHLL) {
            this.treatmentGivenHLL = treatmentGivenHLL;
        }

        public Integer getTreatmentGivenTotal() {
            return treatmentGivenTotal;
        }

        public void setTreatmentGivenTotal(Integer treatmentGivenTotal) {
            this.treatmentGivenTotal = treatmentGivenTotal;
        }

        public Integer getIpdRegisteredHSCC() {
            return ipdRegisteredHSCC;
        }

        public void setIpdRegisteredHSCC(Integer ipdRegisteredHSCC) {
            this.ipdRegisteredHSCC = ipdRegisteredHSCC;
        }

        public Integer getIpdRegisteredHLL() {
            return ipdRegisteredHLL;
        }

        public void setIpdRegisteredHLL(Integer ipdRegisteredHLL) {
            this.ipdRegisteredHLL = ipdRegisteredHLL;
        }

        public Integer getIpdRegisteredTotal() {
            return ipdRegisteredTotal;
        }

        public void setIpdRegisteredTotal(Integer ipdRegisteredTotal) {
            this.ipdRegisteredTotal = ipdRegisteredTotal;
        }

        public Integer getDischargePatientHSCC() {
            return dischargePatientHSCC;
        }

        public void setDischargePatientHSCC(Integer dischargePatientHSCC) {
            this.dischargePatientHSCC = dischargePatientHSCC;
        }

        public Integer getDischargePatientHLL() {
            return dischargePatientHLL;
        }

        public void setDischargePatientHLL(Integer dischargePatientHLL) {
            this.dischargePatientHLL = dischargePatientHLL;
        }

        public Integer getDischargePatientTotal() {
            return dischargePatientTotal;
        }

        public void setDischargePatientTotal(Integer dischargePatientTotal) {
            this.dischargePatientTotal = dischargePatientTotal;
        }

        public Integer getPrescriptionGivenHSCC() {
            return prescriptionGivenHSCC;
        }

        public void setPrescriptionGivenHSCC(Integer prescriptionGivenHSCC) {
            this.prescriptionGivenHSCC = prescriptionGivenHSCC;
        }

        public Integer getPrescriptionGivenHLL() {
            return prescriptionGivenHLL;
        }

        public void setPrescriptionGivenHLL(Integer prescriptionGivenHLL) {
            this.prescriptionGivenHLL = prescriptionGivenHLL;
        }

        public Integer getPrescriptionGivenTotal() {
            return prescriptionGivenTotal;
        }

        public void setPrescriptionGivenTotal(Integer prescriptionGivenTotal) {
            this.prescriptionGivenTotal = prescriptionGivenTotal;
        }

        public Integer getPrescriptionIssuedHSCC() {
            return prescriptionIssuedHSCC;
        }

        public void setPrescriptionIssuedHSCC(Integer prescriptionIssuedHSCC) {
            this.prescriptionIssuedHSCC = prescriptionIssuedHSCC;
        }

        public Integer getPrescriptionIssuedHLL() {
            return prescriptionIssuedHLL;
        }

        public void setPrescriptionIssuedHLL(Integer prescriptionIssuedHLL) {
            this.prescriptionIssuedHLL = prescriptionIssuedHLL;
        }

        public Integer getPrescriptionIssuedTotal() {
            return prescriptionIssuedTotal;
        }

        public void setPrescriptionIssuedTotal(Integer prescriptionIssuedTotal) {
            this.prescriptionIssuedTotal = prescriptionIssuedTotal;
        }

    }
}