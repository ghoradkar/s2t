package com.myhindlab.abkat.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GetDocListD2D {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("output")
    @Expose
    private ArrayList<Output> output = null;

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

    public ArrayList<Output> getOutput() {
        return output;
    }

    public void setOutput(ArrayList<Output> output) {
        this.output = output;
    }

    public class Output {

        @SerializedName("USERID")
        @Expose
        private Integer userId;
        @SerializedName("DoctorName")
        @Expose
        private String doctorName;
        @SerializedName("DocStatus")
        @Expose
        private Integer docStatus;
        @SerializedName("MOBNO")
        @Expose
        private String mobno;

        public String getDoctorName() {
            return doctorName;
        }

        public void setDoctorName(String doctorName) {
            this.doctorName = doctorName;
        }

        public Integer getDocStatus() {
            return docStatus;
        }

        public void setDocStatus(Integer docStatus) {
            this.docStatus = docStatus;
        }

        public String getMobno() {
            return mobno;
        }

        public void setMobno(String mobno) {
            this.mobno = mobno;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }
    }
}
