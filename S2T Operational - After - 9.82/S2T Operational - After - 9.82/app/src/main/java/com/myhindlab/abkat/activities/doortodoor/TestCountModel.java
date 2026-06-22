package com.myhindlab.abkat.activities.doortodoor;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class TestCountModel {

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

        @SerializedName("CAMPID")
        @Expose
        private Integer campid;
        @SerializedName("ALLTESTDONE")
        @Expose
        private String alltestdone;

        public Integer getCampid() {
            return campid;
        }

        public void setCampid(Integer campid) {
            this.campid = campid;
        }

        public String getAlltestdone() {
            return alltestdone;
        }

        public void setAlltestdone(String alltestdone) {
            this.alltestdone = alltestdone;
        }

    }


}