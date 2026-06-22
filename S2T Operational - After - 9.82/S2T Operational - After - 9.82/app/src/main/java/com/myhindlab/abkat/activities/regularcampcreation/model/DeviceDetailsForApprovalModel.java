package com.myhindlab.abkat.activities.regularcampcreation.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DeviceDetailsForApprovalModel {

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
        @SerializedName("Devicecount")
        @Expose
        private Integer devicecount;
        @SerializedName("DeviceSerialNumbers")
        @Expose
        private String deviceSerialNumbers;
        @SerializedName("Remark")
        @Expose
        private String remark;
        @SerializedName("ApproveStatus")
        @Expose
        private int approveStatus;
        @SerializedName("CampId")
        @Expose
        private int campId;

        @SerializedName("ModifiedBy")
        @Expose
        private int modifiedBy;

        @SerializedName("subDevices")
        @Expose
        private List<AllocatedSubDeviceListModel.Output> subDevices;

        @SerializedName("RequiredDevice")
        @Expose
        private Integer requiredDevice;


        public List<AllocatedSubDeviceListModel.Output> getSubDevices() {
            return subDevices;
        }

        public void setSubDevices(List<AllocatedSubDeviceListModel.Output> subDevices) {
            this.subDevices = subDevices;
        }



        public int getCampId() {
            return campId;
        }

        public void setCampId(int campId) {
            this.campId = campId;
        }

        public int getModifiedBy() {
            return modifiedBy;
        }

        public void setModifiedBy(int modifiedBy) {
            this.modifiedBy = modifiedBy;
        }

        public int getApproveStatus() {
            return approveStatus;
        }

        public void setApproveStatus(int approveStatus) {
            this.approveStatus = approveStatus;
        }


        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
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

        public Integer getDevicecount() {
            return devicecount;
        }

        public void setDevicecount(Integer devicecount) {
            this.devicecount = devicecount;
        }

        public String getDeviceSerialNumbers() {
            return deviceSerialNumbers;
        }

        public void setDeviceSerialNumbers(String deviceSerialNumbers) {
            this.deviceSerialNumbers = deviceSerialNumbers;
        }

        public Integer getRequiredDevice() {
            return requiredDevice;
        }

        public void setRequiredDevice(Integer requiredDevice) {
            this.requiredDevice = requiredDevice;
        }
    }
}
