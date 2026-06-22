package com.myhindlab.abkat.activities.payout.model;


import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class InvoiceMonthWiseDetailsnModel {

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

        @SerializedName("UserInviceID")
        @Expose
        private Integer userInviceID;
        @SerializedName("UserID")
        @Expose
        private Integer userID;
        @SerializedName("CampDate")
        @Expose
        private String campDate;
        @SerializedName("CampID")
        @Expose
        private String campID;
        @SerializedName("TotalBeneficiaries")
        @Expose
        private Integer totalBeneficiaries;


        @SerializedName("BillablesSameDay")
        @Expose
        private Double billablesSameDay;

        @SerializedName("BillablesAnotherDay")
        @Expose
        private Double billablesAnotherDay;

        public Double getBillablesSameDay() {
            return billablesSameDay;
        }

        public void setBillablesSameDay(Double billablesSameDay) {
            this.billablesSameDay = billablesSameDay;
        }

        public Double getBillablesAnotherDay() {
            return billablesAnotherDay;
        }

        public void setBillablesAnotherDay(Double billablesAnotherDay) {
            this.billablesAnotherDay = billablesAnotherDay;
        }

        @SerializedName("TotalIndividualBillable")
        @Expose
        private Double totalIndividualBillable;

        public Double getTotalIndividualBillable() {
            return totalIndividualBillable;
        }

        public void setTotalIndividualBillable(Double totalIndividualBillable) {
            this.totalIndividualBillable = totalIndividualBillable;
        }

        @SerializedName("TotalIndividualRejected")
        @Expose
        private Double totalIndividualRejected;
        @SerializedName("IndividualBillableWorker")
        @Expose
        private Double individualBillableWorker;
        @SerializedName("IndividualBillableDependent")
        @Expose
        private Double individualBillableDependent;
        @SerializedName("IndividualPenaltyAmount")
        @Expose
        private Double individualPenaltyAmount;

        public Integer getUserInviceID() {
            return userInviceID;
        }

        public void setUserInviceID(Integer userInviceID) {
            this.userInviceID = userInviceID;
        }

        public Integer getUserID() {
            return userID;
        }

        public void setUserID(Integer userID) {
            this.userID = userID;
        }

        public String getCampDate() {
            return campDate;
        }

        public void setCampDate(String campDate) {
            this.campDate = campDate;
        }

        public String getCampID() {
            return campID;
        }

        public void setCampID(String campID) {
            this.campID = campID;
        }

        public Integer getTotalBeneficiaries() {
            return totalBeneficiaries;
        }

        public void setTotalBeneficiaries(Integer totalBeneficiaries) {
            this.totalBeneficiaries = totalBeneficiaries;
        }

        public Double getTotalIndividualRejected() {
            return totalIndividualRejected;
        }

        public void setTotalIndividualRejected(Double totalIndividualRejected) {
            this.totalIndividualRejected = totalIndividualRejected;
        }

        public Double getIndividualBillableWorker() {
            return individualBillableWorker;
        }

        public void setIndividualBillableWorker(Double individualBillableWorker) {
            this.individualBillableWorker = individualBillableWorker;
        }

        public Double getIndividualBillableDependent() {
            return individualBillableDependent;
        }

        public void setIndividualBillableDependent(Double individualBillableDependent) {
            this.individualBillableDependent = individualBillableDependent;
        }

        public Double getIndividualPenaltyAmount() {
            return individualPenaltyAmount;
        }

        public void setIndividualPenaltyAmount(Double individualPenaltyAmount) {
            this.individualPenaltyAmount = individualPenaltyAmount;
        }

    }
}