package com.myhindlab.abkat.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FaceDetectionModel {

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

        @SerializedName("IsFaceDetetctionEnabled")
        @Expose
        private Integer isFaceDetetctionEnabled;

        @SerializedName("ActiveRegFlag")
        @Expose
        private Integer activeRegFlag;

        public Integer getActiveRegFlag() {
            return activeRegFlag;
        }

        public void setActiveRegFlag(Integer activeRegFlag) {
            this.activeRegFlag = activeRegFlag;
        }

        public Integer getIsFaceDetetctionEnabled() {
            return isFaceDetetctionEnabled;
        }

        public void setIsFaceDetetctionEnabled(Integer isFaceDetetctionEnabled) {
            this.isFaceDetetctionEnabled = isFaceDetetctionEnabled;
        }
    }
}