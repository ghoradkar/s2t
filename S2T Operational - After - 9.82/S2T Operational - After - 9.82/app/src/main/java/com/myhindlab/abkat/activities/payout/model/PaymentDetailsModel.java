package com.myhindlab.abkat.activities.payout.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class PaymentDetailsModel {

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
        @SerializedName("PayableAmount")
        @Expose
        private Double payableAmount;
        @SerializedName("PenaltyAmount")
        @Expose
        private Double penaltyAmount;
        @SerializedName("TDSAmount")
        @Expose
        private Double tDSAmount;

        public Double getGrossAmount() {
            return grossAmount;
        }

        public void setGrossAmount(Double grossAmount) {
            this.grossAmount = grossAmount;
        }

        @SerializedName("GrossAmount")
        @Expose
        private Double grossAmount;
        @SerializedName("FinalPayableAmount")
        @Expose
        private Double finalPayableAmount;
        @SerializedName("UTRNo")
        @Expose
        private String uTRNo;
        @SerializedName("PaymentDate")
        @Expose
        private String paymentDate;

        @SerializedName("InvoiceUrl")
        @Expose
        private String invoiceUrl;

        public String getIsPaymentRaised() {
            return isPaymentRaised;
        }

        public void setIsPaymentRaised(String isPaymentRaised) {
            this.isPaymentRaised = isPaymentRaised;
        }

        @SerializedName("IsPaymentRaised")
        @Expose
        private String isPaymentRaised;

        public String getInvoiceUrl() {
            return invoiceUrl;
        }

        public void setInvoiceUrl(String invoiceUrl) {
            this.invoiceUrl = invoiceUrl;
        }

        @SerializedName("PayementReceivedStatus")
        @Expose
        private String payementReceivedStatus;
        @SerializedName("SendForVerification")
        @Expose
        private String sendForVerification;
        @SerializedName("PayementNotReceivedStatus")
        @Expose
        private String payementNotReceivedStatus;

        public String getPayementNotReceivedStatus() {
            return payementNotReceivedStatus;
        }

        public void setPayementNotReceivedStatus(String payementNotReceivedStatus) {
            this.payementNotReceivedStatus = payementNotReceivedStatus;
        }

        public String getSendForVerification() {
            return sendForVerification;
        }

        public void setSendForVerification(String sendForVerification) {
            this.sendForVerification = sendForVerification;
        }

        public String getPayementReceivedStatus() {
            return payementReceivedStatus;
        }

        public void setPayementReceivedStatus(String payementReceivedStatus) {
            this.payementReceivedStatus = payementReceivedStatus;
        }

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

        public Double getPayableAmount() {
            return payableAmount;
        }

        public void setPayableAmount(Double payableAmount) {
            this.payableAmount = payableAmount;
        }

        public Double getPenaltyAmount() {
            return penaltyAmount;
        }

        public void setPenaltyAmount(Double penaltyAmount) {
            this.penaltyAmount = penaltyAmount;
        }

        public Double getTDSAmount() {
            return tDSAmount;
        }

        public void setTDSAmount(Double tDSAmount) {
            this.tDSAmount = tDSAmount;
        }

        public Double getFinalPayableAmount() {
            return finalPayableAmount;
        }

        public void setFinalPayableAmount(Double finalPayableAmount) {
            this.finalPayableAmount = finalPayableAmount;
        }

        public String getUTRNo() {
            return uTRNo;
        }

        public void setUTRNo(String uTRNo) {
            this.uTRNo = uTRNo;
        }

        public String getPaymentDate() {
            return paymentDate;
        }

        public void setPaymentDate(String paymentDate) {
            this.paymentDate = paymentDate;
        }

    }
}