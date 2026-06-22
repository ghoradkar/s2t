package com.myhindlab.abkat.activities.regularcampcreation.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DeviceListModel {

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

        @SerializedName("DevicesId")
        @Expose
        private Integer devicesId;
        @SerializedName("DeviceName")
        @Expose
        private String deviceName;
        @SerializedName("DeviceDesc")
        @Expose
        private String deviceDesc;
        @SerializedName("NoOfDevice")
        @Expose
        private Object noOfDevice;
        @SerializedName("RequiredDevice")
        @Expose
        private Integer requiredDevice;

        @SerializedName("subDevices")
        @Expose
        private List<SubDeviceListModel.Output> subDevices;

        public List<SubDeviceListModel.Output> getSubDevices() {
            return subDevices;
        }

        public void setSubDevices(List<SubDeviceListModel.Output> subDevices) {
            this.subDevices = subDevices;
        }

        public Integer getDevicesId() {
            return devicesId;
        }

        public void setDevicesId(Integer devicesId) {
            this.devicesId = devicesId;
        }

        public String getDeviceName() {
            return deviceName;
        }

        public void setDeviceName(String deviceName) {
            this.deviceName = deviceName;
        }

        public String getDeviceDesc() {
            return deviceDesc;
        }

        public void setDeviceDesc(String deviceDesc) {
            this.deviceDesc = deviceDesc;
        }
        public Object getNoOfDevice() {
            return noOfDevice;
        }

        public void setNoOfDevice(Object noOfDevice) {
            this.noOfDevice = noOfDevice;
        }

        public Integer getRequiredDevice() {
            return requiredDevice;
        }

        public void setRequiredDevice(Integer requiredDevice) {
            this.requiredDevice = requiredDevice;
        }

    }
}
