package com.myhindlab.abkat.activities.payout;


import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class InvoiceGenModel {

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
        private String userInviceID;
        @SerializedName("InvoiceYear")
        @Expose
        private String invoiceYear;
        @SerializedName("InvoiceMonth")
        @Expose
        private String invoiceMonth;
        @SerializedName("Month")
        @Expose
        private String month;
        @SerializedName("UserID")
        @Expose
        private Integer userID;
        @SerializedName("TotalIndividualBillable")
        @Expose
        private Double totalIndividualBillable;
        @SerializedName("ServiceDays")
        @Expose
        private Integer serviceDays;
        @SerializedName("InvoiceStatus")
        @Expose
        private String invoiceStatus;
        @SerializedName("InvoiceStatusID")
        @Expose
        private Integer invoiceStatusID;
        @SerializedName("Rankno")
        @Expose
        private Integer rankno;

        @SerializedName("InvoiceUrl")
        @Expose
        private String invoiceUrl;


        public String getInvoiceApprovedStatus() {
            return invoiceApprovedStatus;
        }

        public void setInvoiceApprovedStatus(String invoiceApprovedStatus) {
            this.invoiceApprovedStatus = invoiceApprovedStatus;
        }

        @SerializedName("InvoiceApprovedStatus")
        @Expose
        private String invoiceApprovedStatus;


        @SerializedName("SendForVerification")
        @Expose
        private String sendForVerification;

        public String getSendForVerification() {
            return sendForVerification;
        }

        public void setSendForVerification(String sendForVerification) {
            this.sendForVerification = sendForVerification;
        }

        public String getInvoiceUrl() {
            return invoiceUrl;
        }

        public void setInvoiceUrl(String invoiceUrl) {
            this.invoiceUrl = invoiceUrl;
        }

        public String getUserInviceID() {
            return userInviceID;
        }

        public void setUserInviceID(String userInviceID) {
            this.userInviceID = userInviceID;
        }

        public String getInvoiceYear() {
            return invoiceYear;
        }

        public void setInvoiceYear(String invoiceYear) {
            this.invoiceYear = invoiceYear;
        }

        public String getInvoiceMonth() {
            return invoiceMonth;
        }

        public void setInvoiceMonth(String invoiceMonth) {
            this.invoiceMonth = invoiceMonth;
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public Integer getUserID() {
            return userID;
        }

        public void setUserID(Integer userID) {
            this.userID = userID;
        }

        public Double getTotalIndividualBillable() {
            return totalIndividualBillable;
        }

        public void setTotalIndividualBillable(Double totalIndividualBillable) {
            this.totalIndividualBillable = totalIndividualBillable;
        }

        public Integer getServiceDays() {
            return serviceDays;
        }

        public void setServiceDays(Integer serviceDays) {
            this.serviceDays = serviceDays;
        }

        public String getInvoiceStatus() {
            return invoiceStatus;
        }

        public void setInvoiceStatus(String invoiceStatus) {
            this.invoiceStatus = invoiceStatus;
        }

        public Integer getInvoiceStatusID() {
            return invoiceStatusID;
        }

        public void setInvoiceStatusID(Integer invoiceStatusID) {
            this.invoiceStatusID = invoiceStatusID;
        }

        public Integer getRankno() {
            return rankno;
        }

        public void setRankno(Integer rankno) {
            this.rankno = rankno;
        }

    }
}