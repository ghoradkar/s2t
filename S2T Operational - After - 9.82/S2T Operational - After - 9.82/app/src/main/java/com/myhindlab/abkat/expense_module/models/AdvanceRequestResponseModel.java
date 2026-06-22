package com.myhindlab.abkat.expense_module.models;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AdvanceRequestResponseModel {

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

        @SerializedName("campid")
        @Expose
        private Integer campid;
        @SerializedName("DISTNAME")
        @Expose
        private String distname;
        @SerializedName("CampDate")
        @Expose
        private String campDate;
        @SerializedName("Expectedbeneficiarycount")
        @Expose
        private Integer expectedbeneficiarycount;
        @SerializedName("FundRequestedBy")
        @Expose
        private String fundRequestedBy;
        @SerializedName("ApprovedStatus")
        @Expose
        private String approvedStatus;
        @SerializedName("ApprovedDate")
        @Expose
        private Object approvedDate;

        public Integer getCampid() {
            return campid;
        }

        public void setCampid(Integer campid) {
            this.campid = campid;
        }

        public String getDistname() {
            return distname;
        }

        public void setDistname(String distname) {
            this.distname = distname;
        }

        public String getCampDate() {
            return campDate;
        }

        public void setCampDate(String campDate) {
            this.campDate = campDate;
        }

        public Integer getExpectedbeneficiarycount() {
            return expectedbeneficiarycount;
        }

        public void setExpectedbeneficiarycount(Integer expectedbeneficiarycount) {
            this.expectedbeneficiarycount = expectedbeneficiarycount;
        }

        public String getFundRequestedBy() {
            return fundRequestedBy;
        }

        public void setFundRequestedBy(String fundRequestedBy) {
            this.fundRequestedBy = fundRequestedBy;
        }

        public String getApprovedStatus() {
            return approvedStatus;
        }

        public void setApprovedStatus(String approvedStatus) {
            this.approvedStatus = approvedStatus;
        }

        public Object getApprovedDate() {
            return approvedDate;
        }

        public void setApprovedDate(Object approvedDate) {
            this.approvedDate = approvedDate;
        }

    }

}
