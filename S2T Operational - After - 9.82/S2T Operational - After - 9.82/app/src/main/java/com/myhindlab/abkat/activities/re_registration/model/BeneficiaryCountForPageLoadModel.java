package com.myhindlab.abkat.activities.re_registration.model;


import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BeneficiaryCountForPageLoadModel {

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

        @SerializedName("District")
        @Expose
        private String district;
        @SerializedName("DISTLGDCODE")
        @Expose
        private Integer distlgdcode;
        @SerializedName("RejectedBeneficiaries")
        @Expose
        private Integer rejectedBeneficiaries;
        @SerializedName("AssignedForScreening")
        @Expose
        private Integer assignedForScreening;
        @SerializedName("NotAssignedForScreening")
        @Expose
        private Integer notAssignedForScreening;
        @SerializedName("InterestedInScreening")
        @Expose
        private Integer interestedInScreening;
        @SerializedName("NotInterestedInScreening")
        @Expose
        private Integer notInterestedInScreening;
        @SerializedName("NotAvailableForScreening")
        @Expose
        private Integer notAvailableForScreening;
        @SerializedName("DeniedForScreening")
        @Expose
        private Integer deniedForScreening;
        @SerializedName("ReScreeningPendingAsCardExpired")
        @Expose
        private Integer reScreeningPendingAsCardExpired;
        @SerializedName("ReScreenedBeneficiaries")
        @Expose
        private Integer reScreenedBeneficiaries;

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public Integer getDistlgdcode() {
            return distlgdcode;
        }

        public void setDistlgdcode(Integer distlgdcode) {
            this.distlgdcode = distlgdcode;
        }

        public Integer getRejectedBeneficiaries() {
            return rejectedBeneficiaries;
        }

        public void setRejectedBeneficiaries(Integer rejectedBeneficiaries) {
            this.rejectedBeneficiaries = rejectedBeneficiaries;
        }

        public Integer getAssignedForScreening() {
            return assignedForScreening;
        }

        public void setAssignedForScreening(Integer assignedForScreening) {
            this.assignedForScreening = assignedForScreening;
        }

        public Integer getNotAssignedForScreening() {
            return notAssignedForScreening;
        }

        public void setNotAssignedForScreening(Integer notAssignedForScreening) {
            this.notAssignedForScreening = notAssignedForScreening;
        }

        public Integer getInterestedInScreening() {
            return interestedInScreening;
        }

        public void setInterestedInScreening(Integer interestedInScreening) {
            this.interestedInScreening = interestedInScreening;
        }

        public Integer getNotInterestedInScreening() {
            return notInterestedInScreening;
        }

        public void setNotInterestedInScreening(Integer notInterestedInScreening) {
            this.notInterestedInScreening = notInterestedInScreening;
        }

        public Integer getNotAvailableForScreening() {
            return notAvailableForScreening;
        }

        public void setNotAvailableForScreening(Integer notAvailableForScreening) {
            this.notAvailableForScreening = notAvailableForScreening;
        }

        public Integer getDeniedForScreening() {
            return deniedForScreening;
        }

        public void setDeniedForScreening(Integer deniedForScreening) {
            this.deniedForScreening = deniedForScreening;
        }

        public Integer getReScreeningPendingAsCardExpired() {
            return reScreeningPendingAsCardExpired;
        }

        public void setReScreeningPendingAsCardExpired(Integer reScreeningPendingAsCardExpired) {
            this.reScreeningPendingAsCardExpired = reScreeningPendingAsCardExpired;
        }

        public Integer getReScreenedBeneficiaries() {
            return reScreenedBeneficiaries;
        }

        public void setReScreenedBeneficiaries(Integer reScreenedBeneficiaries) {
            this.reScreenedBeneficiaries = reScreenedBeneficiaries;
        }

    }
}