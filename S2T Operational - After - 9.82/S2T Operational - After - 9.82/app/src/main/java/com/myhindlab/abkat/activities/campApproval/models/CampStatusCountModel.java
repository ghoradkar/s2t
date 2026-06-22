package com.myhindlab.abkat.activities.campApproval.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class CampStatusCountModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("output")
    @Expose
    private List<Output> output = null;

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

        @SerializedName("PendingApproval")
        @Expose
        private Integer pendingApproval;
        @SerializedName("ApprovedCampCount")
        @Expose
        private Integer approvedCampCount;
        @SerializedName("HoldCampCount")
        @Expose
        private Integer holdCampCount;
        @SerializedName("RejectedCampCount")
        @Expose
        private Integer rejectedCampCount;

        public Integer getRejectedCampCount() {
            return rejectedCampCount;
        }

        public void setRejectedCampCount(Integer rejectedCampCount) {
            this.rejectedCampCount = rejectedCampCount;
        }

        public Integer getPendingApproval() {
            return pendingApproval;
        }

        public void setPendingApproval(Integer pendingApproval) {
            this.pendingApproval = pendingApproval;
        }

        public Integer getApprovedCampCount() {
            return approvedCampCount;
        }

        public void setApprovedCampCount(Integer approvedCampCount) {
            this.approvedCampCount = approvedCampCount;
        }

        public Integer getHoldCampCount() {
            return holdCampCount;
        }

        public void setHoldCampCount(Integer holdCampCount) {
            this.holdCampCount = holdCampCount;
        }

    }

}
