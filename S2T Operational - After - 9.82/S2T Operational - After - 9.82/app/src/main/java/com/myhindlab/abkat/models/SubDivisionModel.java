package com.myhindlab.abkat.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class SubDivisionModel {

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
        @SerializedName("SubOrgId")
        @Expose
        private Integer subOrgId;
        @SerializedName("SubOrgName")
        @Expose
        private String subOrgName;

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        private boolean isChecked;

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

        public Integer getSubOrgId() {
            return subOrgId;
        }

        public void setSubOrgId(Integer subOrgId) {
            this.subOrgId = subOrgId;
        }

        public String getSubOrgName() {
            return subOrgName;
        }

        public void setSubOrgName(String subOrgName) {
            this.subOrgName = subOrgName;
        }

    }

}