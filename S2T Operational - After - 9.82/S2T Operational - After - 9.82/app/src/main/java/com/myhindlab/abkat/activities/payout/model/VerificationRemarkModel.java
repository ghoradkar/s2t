package com.myhindlab.abkat.activities.payout.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


import java.util.List;

public class VerificationRemarkModel {

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

        @SerializedName("VerificationRemarkID")
        @Expose
        private Integer verificationRemarkID;
        @SerializedName("VerificationRemark")
        @Expose
        private String verificationRemark;
        @SerializedName("RankID")
        @Expose
        private Integer rankID;

        public Integer getVerificationRemarkID() {
            return verificationRemarkID;
        }

        public void setVerificationRemarkID(Integer verificationRemarkID) {
            this.verificationRemarkID = verificationRemarkID;
        }

        public String getVerificationRemark() {
            return verificationRemark;
        }

        public void setVerificationRemark(String verificationRemark) {
            this.verificationRemark = verificationRemark;
        }

        public Integer getRankID() {
            return rankID;
        }

        public void setRankID(Integer rankID) {
            this.rankID = rankID;
        }

    }
}