package com.myhindlab.abkat.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class RemarkModel {

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

        @SerializedName("DISTLGDCODE")
        @Expose
        private Integer distlgdcode;
        @SerializedName("DISTNAME")
        @Expose
        private String distname;
        @SerializedName("LabCode")
        @Expose
        private Integer labCode;
        @SerializedName("LabName")
        @Expose
        private String labName;

        @SerializedName("Remark")
        @Expose
        private String remark;

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public Integer getRemarkID() {
            return remarkID;
        }

        public void setRemarkID(Integer remarkID) {
            this.remarkID = remarkID;
        }

        @SerializedName("RemarkID")
        @Expose
        private Integer remarkID;

        public Integer getDistlgdcode() {
            return distlgdcode;
        }

        public void setDistlgdcode(Integer distlgdcode) {
            this.distlgdcode = distlgdcode;
        }

        public String getDistname() {
            return distname;
        }

        public void setDistname(String distname) {
            this.distname = distname;
        }

        public Integer getLabCode() {
            return labCode;
        }

        public void setLabCode(Integer labCode) {
            this.labCode = labCode;
        }

        public String getLabName() {
            return labName;
        }

        public void setLabName(String labName) {
            this.labName = labName;
        }

    }
}