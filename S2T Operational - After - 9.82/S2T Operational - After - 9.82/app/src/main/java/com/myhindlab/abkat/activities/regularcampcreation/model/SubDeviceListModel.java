package com.myhindlab.abkat.activities.regularcampcreation.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SubDeviceListModel implements Serializable {

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


    public class Output implements Serializable{

        @SerializedName("SubDevicesId")
        @Expose
        private Integer subDevicesId;
        @SerializedName("DevicesId")
        @Expose
        private Integer devicesId;
        @SerializedName("DeviceCompName")
        @Expose
        private String deviceCompName;
        @SerializedName("DeviceModel")
        @Expose
        private String deviceModel;
        @SerializedName("DeviceSerial")
        @Expose
        private String deviceSerial;
        @SerializedName("ISActive")
        @Expose
        private Integer iSActive;

        private boolean isChecked;

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        public Integer getSubDevicesId() {
            return subDevicesId;
        }

        public void setSubDevicesId(Integer subDevicesId) {
            this.subDevicesId = subDevicesId;
        }

        public Integer getDevicesId() {
            return devicesId;
        }

        public void setDevicesId(Integer devicesId) {
            this.devicesId = devicesId;
        }

        public String getDeviceCompName() {
            return deviceCompName;
        }

        public void setDeviceCompName(String deviceCompName) {
            this.deviceCompName = deviceCompName;
        }

        public String getDeviceModel() {
            return deviceModel;
        }

        public void setDeviceModel(String deviceModel) {
            this.deviceModel = deviceModel;
        }

        public String getDeviceSerial() {
            return deviceSerial;
        }

        public void setDeviceSerial(String deviceSerial) {
            this.deviceSerial = deviceSerial;
        }

        public Integer getISActive() {
            return iSActive;
        }

        public void setISActive(Integer iSActive) {
            this.iSActive = iSActive;
        }

    }
}
