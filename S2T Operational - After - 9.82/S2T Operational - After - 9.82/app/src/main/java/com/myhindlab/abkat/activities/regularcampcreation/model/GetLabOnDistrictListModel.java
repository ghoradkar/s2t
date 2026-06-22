package com.myhindlab.abkat.activities.regularcampcreation.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetLabOnDistrictListModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("output")
    @Expose
    private List<Output> output = null;

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

        @SerializedName("LabCode")
        @Expose
        private Integer labCode;
        @SerializedName("DISTLGDCODE")
        @Expose
        private Integer distlgdcode;
        @SerializedName("LabName")
        @Expose
        private String labName;
        @SerializedName("IsHubLab")
        @Expose
        private Integer isHubLab;

        public Integer getLabCode() {
            return labCode;
        }

        public void setLabCode(Integer labCode) {
            this.labCode = labCode;
        }

        public Integer getDistlgdcode() {
            return distlgdcode;
        }

        public void setDistlgdcode(Integer distlgdcode) {
            this.distlgdcode = distlgdcode;
        }

        public String getLabName() {
            return labName;
        }

        public void setLabName(String labName) {
            this.labName = labName;
        }

        public Integer getIsHubLab() {
            return isHubLab;
        }

        public void setIsHubLab(Integer isHubLab) {
            this.isHubLab = isHubLab;
        }

    }
}

