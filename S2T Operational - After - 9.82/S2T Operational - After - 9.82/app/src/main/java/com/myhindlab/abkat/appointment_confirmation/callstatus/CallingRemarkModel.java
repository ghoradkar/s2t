package com.myhindlab.abkat.appointment_confirmation.callstatus;


import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CallingRemarkModel {

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

        @SerializedName("CReamrkID")
        @Expose
        private Integer cReamrkID;
        @SerializedName("CallingRemark")
        @Expose
        private String callingRemark;

        public Integer getCReamrkID() {
            return cReamrkID;
        }

        public void setCReamrkID(Integer cReamrkID) {
            this.cReamrkID = cReamrkID;
        }

        public String getCallingRemark() {
            return callingRemark;
        }

        public void setCallingRemark(String callingRemark) {
            this.callingRemark = callingRemark;
        }

    }
}