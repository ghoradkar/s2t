package com.myhindlab.abkat.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class PincodeListModel {

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

        @SerializedName("SubOrgId")
        @Expose
        private String subOrgId;
        @SerializedName("DIVID")
        @Expose
        private Integer divid;
        @SerializedName("DISTLGDCODE")
        @Expose
        private Integer distlgdcode;
        @SerializedName("TALLGDCODE")
        @Expose
        private Integer tallgdcode;
        @SerializedName("Pincode")
        @Expose
        private String pincode;

        private boolean isChecked;

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        public String getSubOrgId() {
            return subOrgId;
        }

        public void setSubOrgId(String subOrgId) {
            this.subOrgId = subOrgId;
        }

        public Integer getDivid() {
            return divid;
        }

        public void setDivid(Integer divid) {
            this.divid = divid;
        }

        public Integer getDistlgdcode() {
            return distlgdcode;
        }

        public void setDistlgdcode(Integer distlgdcode) {
            this.distlgdcode = distlgdcode;
        }

        public Integer getTallgdcode() {
            return tallgdcode;
        }

        public void setTallgdcode(Integer tallgdcode) {
            this.tallgdcode = tallgdcode;
        }

        public String getPincode() {
            return pincode;
        }

        public void setPincode(String pincode) {
            this.pincode = pincode;
        }

    }
}