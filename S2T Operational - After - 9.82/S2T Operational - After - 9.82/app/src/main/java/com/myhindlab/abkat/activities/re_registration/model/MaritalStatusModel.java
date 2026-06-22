package com.myhindlab.abkat.activities.re_registration.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MaritalStatusModel {

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

        @SerializedName("MARITALSTATUSID")
        @Expose
        private Integer maritalstatusid;
        @SerializedName("MARITALSTATUS")
        @Expose
        private String maritalstatus;

        public Integer getMaritalstatusid() {
            return maritalstatusid;
        }

        public void setMaritalstatusid(Integer maritalstatusid) {
            this.maritalstatusid = maritalstatusid;
        }

        public String getMaritalstatus() {
            return maritalstatus;
        }

        public void setMaritalstatus(String maritalstatus) {
            this.maritalstatus = maritalstatus;
        }

    }
}