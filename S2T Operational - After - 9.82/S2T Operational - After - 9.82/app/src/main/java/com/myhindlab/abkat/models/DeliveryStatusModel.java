package com.myhindlab.abkat.models;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class DeliveryStatusModel {

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

        @SerializedName("DeliveryStatusRemarkID")
        @Expose
        private Integer deliveryStatusRemarkID;
        @SerializedName("StatusRemark")
        @Expose
        private String statusRemark;

        public Integer getDeliveryStatusRemarkID() {
            return deliveryStatusRemarkID;
        }

        public void setDeliveryStatusRemarkID(Integer deliveryStatusRemarkID) {
            this.deliveryStatusRemarkID = deliveryStatusRemarkID;
        }

        public String getStatusRemark() {
            return statusRemark;
        }

        public void setStatusRemark(String statusRemark) {
            this.statusRemark = statusRemark;
        }

    }

}

