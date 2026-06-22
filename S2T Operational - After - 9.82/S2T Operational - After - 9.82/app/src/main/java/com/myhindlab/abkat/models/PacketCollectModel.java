package com.myhindlab.abkat.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


import java.util.List;


public class PacketCollectModel {

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

        @SerializedName("Labcode")
        @Expose
        private Integer labcode;
        @SerializedName("LabName")
        @Expose
        private String labName;
        @SerializedName("CollectFrom")
        @Expose
        private String collectFrom;
        @SerializedName("PacketNumber")
        @Expose
        private String packetNumber;

        @SerializedName("DISTNAME")
        @Expose
        private String DISTNAME;

        private boolean isChecked;

        public String getDISTNAME() {
            return DISTNAME;
        }

        public void setDISTNAME(String DISTNAME) {
            this.DISTNAME = DISTNAME;
        }

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        public Integer getLabcode() {
            return labcode;
        }

        public void setLabcode(Integer labcode) {
            this.labcode = labcode;
        }

        public String getLabName() {
            return labName;
        }

        public void setLabName(String labName) {
            this.labName = labName;
        }

        public String getCollectFrom() {
            return collectFrom;
        }

        public void setCollectFrom(String collectFrom) {
            this.collectFrom = collectFrom;
        }

        public String getPacketNumber() {
            return packetNumber;
        }

        public void setPacketNumber(String packetNumber) {
            this.packetNumber = packetNumber;
        }

    }


}