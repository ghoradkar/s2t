package com.myhindlab.abkat.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


import java.util.List;

public class ProcessingLabCountModel {

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

        @SerializedName("MonthYear")
        @Expose
        private String monthYear;

        @SerializedName("LastUpdatedDate")
        @Expose
        private String lastUpdatedDate;

        public String getLastUpdatedDate() {
            return lastUpdatedDate;
        }

        public void setLastUpdatedDate(String lastUpdatedDate) {
            this.lastUpdatedDate = lastUpdatedDate;
        }

        @SerializedName("HomeLab")
        @Expose
        private Integer homeLab;
        @SerializedName("HubLab")
        @Expose
        private Integer hubLab;


        @SerializedName("LabName")
        @Expose
        private String labName;

        @SerializedName("LabType")
        @Expose
        private String labType;

        public String getLabName() {
            return labName;
        }

        public void setLabName(String labName) {
            this.labName = labName;
        }

        public String getLabType() {
            return labType;
        }

        public void setLabType(String labType) {
            this.labType = labType;
        }

        public Integer getProcessingCount() {
            return processingCount;
        }

        public void setProcessingCount(Integer processingCount) {
            this.processingCount = processingCount;
        }

        @SerializedName("ProcessingCount")
        @Expose
        private Integer processingCount;

        public String getMonthYear() {
            return monthYear;
        }

        public void setMonthYear(String monthYear) {
            this.monthYear = monthYear;
        }

        public Integer getHomeLab() {
            return homeLab;
        }

        public void setHomeLab(Integer homeLab) {
            this.homeLab = homeLab;
        }

        public Integer getHubLab() {
            return hubLab;
        }

        public void setHubLab(Integer hubLab) {
            this.hubLab = hubLab;
        }

    }

}