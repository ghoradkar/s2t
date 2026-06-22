package com.myhindlab.abkat.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class PendingCountModel {

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

        @SerializedName("DIVID")
        @Expose
        private Integer divid;
        @SerializedName("DIVNAME")
        @Expose
        private String divname;
        @SerializedName("HomeLabProcessingDelayed")
        @Expose
        private Integer homeLabProcessingDelayed;
        @SerializedName("HubLabProcessingDelayed")
        @Expose
        private Integer hubLabProcessingDelayed;
        @SerializedName("DoctorScreeningDelayed")
        @Expose
        private Integer doctorScreeningDelayed;

        public Integer getDivid() {
            return divid;
        }

        public void setDivid(Integer divid) {
            this.divid = divid;
        }

        public String getDivname() {
            return divname;
        }

        public void setDivname(String divname) {
            this.divname = divname;
        }

        public Integer getHomeLabProcessingDelayed() {
            return homeLabProcessingDelayed;
        }

        public void setHomeLabProcessingDelayed(Integer homeLabProcessingDelayed) {
            this.homeLabProcessingDelayed = homeLabProcessingDelayed;
        }

        public Integer getHubLabProcessingDelayed() {
            return hubLabProcessingDelayed;
        }

        public void setHubLabProcessingDelayed(Integer hubLabProcessingDelayed) {
            this.hubLabProcessingDelayed = hubLabProcessingDelayed;
        }

        public Integer getDoctorScreeningDelayed() {
            return doctorScreeningDelayed;
        }

        public void setDoctorScreeningDelayed(Integer doctorScreeningDelayed) {
            this.doctorScreeningDelayed = doctorScreeningDelayed;
        }

    }
}

