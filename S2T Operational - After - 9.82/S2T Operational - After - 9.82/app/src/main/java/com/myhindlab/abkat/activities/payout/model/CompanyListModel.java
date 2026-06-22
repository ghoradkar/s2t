package com.myhindlab.abkat.activities.payout.model;


import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CompanyListModel {

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

        @SerializedName("PaidByCompanyID")
        @Expose
        private Integer paidByCompanyID;
        @SerializedName("PaidByCompany")
        @Expose
        private String paidByCompany;

        public Integer getPaidByCompanyID() {
            return paidByCompanyID;
        }

        public void setPaidByCompanyID(Integer paidByCompanyID) {
            this.paidByCompanyID = paidByCompanyID;
        }

        public String getPaidByCompany() {
            return paidByCompany;
        }

        public void setPaidByCompany(String paidByCompany) {
            this.paidByCompany = paidByCompany;
        }

    }

}