package com.myhindlab.abkat.activities.regularcampcreation.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllocatedSubDeviceListModel {

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
        @SerializedName("DeviceStatus")
        @Expose
        private String DeviceStatus;
        @SerializedName("ModifiedBy")
        @Expose
        private Object modifiedBy;
        @SerializedName("IsApproved")
        @Expose
        private Object isApproved;
        @SerializedName("Remark")
        @Expose
        private Object remark;
        @SerializedName("campid")
        @Expose
        private Integer campid;

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

        public String getDeviceStatus() {
            return DeviceStatus.trim();
        }

        public void setDeviceStatus(String deviceStatus) {
            DeviceStatus = deviceStatus;
        }

        public Object getModifiedBy() {
            return modifiedBy;
        }

        public void setModifiedBy(Object modifiedBy) {
            this.modifiedBy = modifiedBy;
        }

        public Object getIsApproved() {
            return isApproved;
        }

        public void setIsApproved(Object isApproved) {
            this.isApproved = isApproved;
        }

        public Object getRemark() {
            return remark;
        }

        public void setRemark(Object remark) {
            this.remark = remark;
        }

        public Integer getCampid() {
            return campid;
        }

        public void setCampid(Integer campid) {
            this.campid = campid;
        }

    }
}
