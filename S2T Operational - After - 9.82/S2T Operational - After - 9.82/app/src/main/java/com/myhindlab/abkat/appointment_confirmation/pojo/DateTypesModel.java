package com.myhindlab.abkat.appointment_confirmation.pojo;


import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DateTypesModel {

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

        @SerializedName("CallingDateID")
        @Expose
        private Integer callingDateID;
        @SerializedName("CallingDateType")
        @Expose
        private String callingDateType;

        public Integer getCallingDateID() {
            return callingDateID;
        }

        public void setCallingDateID(Integer callingDateID) {
            this.callingDateID = callingDateID;
        }

        public String getCallingDateType() {
            return callingDateType;
        }

        public void setCallingDateType(String callingDateType) {
            this.callingDateType = callingDateType;
        }

    }
}