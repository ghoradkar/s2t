package com.myhindlab.abkat.appointment_confirmation.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


import java.util.List;

public class VodaphoneCallModel {

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

        @SerializedName("virtualNumber")
        @Expose
        private String virtualNumber;
        @SerializedName("Token")
        @Expose
        private String token;
        @SerializedName("channelflag")
        @Expose
        private String channelflag;
        @SerializedName("dtmfflag")
        @Expose
        private String dtmfflag;
        @SerializedName("recordingflag")
        @Expose
        private String recordingflag;


        @SerializedName("ModifiedOn")
        @Expose
        private String modifiedOn;

        public String getModifiedOn() {
            return modifiedOn;
        }

        public void setModifiedOn(String modifiedOn) {
            this.modifiedOn = modifiedOn;
        }

        public String getVirtualNumber() {
            return virtualNumber;
        }

        public void setVirtualNumber(String virtualNumber) {
            this.virtualNumber = virtualNumber;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getChannelflag() {
            return channelflag;
        }

        public void setChannelflag(String channelflag) {
            this.channelflag = channelflag;
        }

        public String getDtmfflag() {
            return dtmfflag;
        }

        public void setDtmfflag(String dtmfflag) {
            this.dtmfflag = dtmfflag;
        }

        public String getRecordingflag() {
            return recordingflag;
        }

        public void setRecordingflag(String recordingflag) {
            this.recordingflag = recordingflag;
        }

    }

}