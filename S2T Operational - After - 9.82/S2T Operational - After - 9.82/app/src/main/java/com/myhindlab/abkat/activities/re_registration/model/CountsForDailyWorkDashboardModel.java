package com.myhindlab.abkat.activities.re_registration.model;


import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CountsForDailyWorkDashboardModel {

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

        @SerializedName("SequenceNo")
        @Expose
        private Integer sequenceNo;
        @SerializedName("Arid")
        @Expose
        private Integer arid;
        @SerializedName("AssignmentRemarks")
        @Expose
        private String assignmentRemarks;
        @SerializedName("PatientCount")
        @Expose
        private Integer patientCount;

        public Integer getSequenceNo() {
            return sequenceNo;
        }

        public void setSequenceNo(Integer sequenceNo) {
            this.sequenceNo = sequenceNo;
        }

        public Integer getArid() {
            return arid;
        }

        public void setArid(Integer arid) {
            this.arid = arid;
        }

        public String getAssignmentRemarks() {
            return assignmentRemarks;
        }

        public void setAssignmentRemarks(String assignmentRemarks) {
            this.assignmentRemarks = assignmentRemarks;
        }

        public Integer getPatientCount() {
            return patientCount;
        }

        public void setPatientCount(Integer patientCount) {
            this.patientCount = patientCount;
        }

    }
}