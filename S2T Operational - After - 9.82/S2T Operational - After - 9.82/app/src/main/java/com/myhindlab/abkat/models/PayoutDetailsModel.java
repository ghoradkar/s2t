package com.myhindlab.abkat.models;

import java.io.Serializable;
import java.util.ArrayList;

public class PayoutDetailsModel implements Serializable {

    private String status;
    private String message;
    private ArrayList<OutputBean> output;

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

    public ArrayList<OutputBean> getOutput() {
        return output;
    }

    public void setOutput(ArrayList<OutputBean> output) {
        this.output = output;
    }

    public static class OutputBean implements Serializable {

        private String CAMPID;

        private String DistName;

        private String DISTLGDCODE;

        private String TreatmentID;
        private String PaymentSheetID;
        private String PaymentSheetNo;
        private String BillingCycle;
        private String CampType;
        private String TeamID;
        private String UserID;
        private String BillableWorker;
        private Integer BillableDependent;
        private Double TotalPenalty;
        private String TDSAmount;
        private String PayableAmount;
        private String IsPaymentRecieved;

        public String getIsPaymentRecieved() {
            return IsPaymentRecieved;
        }

        public void setIsPaymentRecieved(String isPaymentRecieved) {
            IsPaymentRecieved = isPaymentRecieved;
        }

        public String getCampDate() {
            return CampDate;
        }

        public void setCampDate(String campDate) {
            CampDate = campDate;
        }

        public String getCampID() {
            return CampID;
        }

        public void setCampID(String campID) {
            CampID = campID;
        }

        public Integer getTotalRegistartions() {
            return TotalRegistartions;
        }

        public void setTotalRegistartions(Integer totalRegistartions) {
            TotalRegistartions = totalRegistartions;
        }

        public Integer getRejection() {
            return Rejection;
        }

        public void setRejection(Integer rejection) {
            Rejection = rejection;
        }

        public Integer getScreeningPending() {
            return ScreeningPending;
        }

        public void setScreeningPending(Integer screeningPending) {
            ScreeningPending = screeningPending;
        }

        public Integer getBillableWorkers() {
            return BillableWorkers;
        }

        public void setBillableWorkers(Integer billableWorkers) {
            BillableWorkers = billableWorkers;
        }

        public Double getPenaltyAmount() {
            return PenaltyAmount;
        }

        public void setPenaltyAmount(Double penaltyAmount) {
            PenaltyAmount = penaltyAmount;
        }

        private String CampDate;
        private String CampID;
        private Integer TotalRegistartions;
        private Integer Rejection;
        private Integer ScreeningPending;
        private Integer BillableWorkers;
        private Double PenaltyAmount;

        public String getPaymentSheetID() {
            return PaymentSheetID;
        }

        public void setPaymentSheetID(String paymentSheetID) {
            PaymentSheetID = paymentSheetID;
        }

        public String getPaymentSheetNo() {
            return PaymentSheetNo;
        }

        public void setPaymentSheetNo(String paymentSheetNo) {
            PaymentSheetNo = paymentSheetNo;
        }

        public String getBillingCycle() {
            return BillingCycle;
        }

        public void setBillingCycle(String billingCycle) {
            BillingCycle = billingCycle;
        }

        public String getCampType() {
            return CampType;
        }

        public void setCampType(String campType) {
            CampType = campType;
        }

        public String getTeamID() {
            return TeamID;
        }

        public void setTeamID(String teamID) {
            TeamID = teamID;
        }

        public String getUserID() {
            return UserID;
        }

        public void setUserID(String userID) {
            UserID = userID;
        }

        public String getBillableWorker() {
            return BillableWorker;
        }

        public void setBillableWorker(String billableWorker) {
            BillableWorker = billableWorker;
        }

        public Integer getBillableDependent() {
            return BillableDependent;
        }

        public void setBillableDependent(Integer billableDependent) {
            BillableDependent = billableDependent;
        }

        public Double getTotalPenalty() {
            return TotalPenalty;
        }

        public void setTotalPenalty(Double totalPenalty) {
            TotalPenalty = totalPenalty;
        }

        public String getTDSAmount() {
            return TDSAmount;
        }

        public void setTDSAmount(String TDSAmount) {
            this.TDSAmount = TDSAmount;
        }

        public String getPayableAmount() {
            return PayableAmount;
        }

        public void setPayableAmount(String payableAmount) {
            PayableAmount = payableAmount;
        }

        public String getMedicalDelivaryID() {
            return MedicalDelivaryID;
        }

        public void setMedicalDelivaryID(String medicalDelivaryID) {
            MedicalDelivaryID = medicalDelivaryID;
        }

        private String MedicalDelivaryID;

        public String getTreatmentID() {
            return TreatmentID;
        }

        public void setTreatmentID(String treatmentID) {
            TreatmentID = treatmentID;
        }


        public String getOTPStatus() {
            return OTPStatus;
        }

        public void setOTPStatus(String OTPStatus) {
            this.OTPStatus = OTPStatus;
        }

        private String OTPStatus;


        public String getCAMPID() {
            return CAMPID;
        }

        public void setCAMPID(String CAMPID) {
            this.CAMPID = CAMPID;
        }

        public String getDistName() {
            return DistName;
        }

        public void setDistName(String distName) {
            DistName = distName;
        }

        public String getDISTLGDCODE() {
            return DISTLGDCODE;
        }

        public void setDISTLGDCODE(String DISTLGDCODE) {
            this.DISTLGDCODE = DISTLGDCODE;
        }


    }
}
