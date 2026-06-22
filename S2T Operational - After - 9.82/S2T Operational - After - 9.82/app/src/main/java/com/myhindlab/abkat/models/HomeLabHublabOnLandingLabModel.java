package com.myhindlab.abkat.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HomeLabHublabOnLandingLabModel {

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

        @SerializedName("HomeLabcode")
        @Expose
        private Integer homeLabcode;
        @SerializedName("HomeLab")
        @Expose
        private String homeLab;
        @SerializedName("HubLabcode")
        @Expose
        private Integer hubLabcode;
        @SerializedName("HubLab")
        @Expose
        private String hubLab;

        @SerializedName("isFlag")
        @Expose
        private String isFlag;
        @SerializedName("OptionFlag")
        @Expose
        private String optionFlag;

        public String getOptionFlag() {
            return optionFlag;
        }

        public void setOptionFlag(String optionFlag) {
            this.optionFlag = optionFlag;
        }

        @SerializedName("TestId")
        @Expose
        private String testId;

        public String getTestId() {
            return testId;
        }

        public void setTestId(String testId) {
            this.testId = testId;
        }

        public String getIsFlag() {
            return isFlag;
        }

        public void setIsFlag(String isFlag) {
            this.isFlag = isFlag;
        }

        public Integer getHomeLabcode() {
            return homeLabcode;
        }

        public void setHomeLabcode(Integer homeLabcode) {
            this.homeLabcode = homeLabcode;
        }

        public String getHomeLab() {
            return homeLab;
        }

        public void setHomeLab(String homeLab) {
            this.homeLab = homeLab;
        }

        public Integer getHubLabcode() {
            return hubLabcode;
        }

        public void setHubLabcode(Integer hubLabcode) {
            this.hubLabcode = hubLabcode;
        }

        public String getHubLab() {
            return hubLab;
        }

        public void setHubLab(String hubLab) {
            this.hubLab = hubLab;
        }

    }
}