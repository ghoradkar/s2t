package com.myhindlab.abkat.models;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class DeliveryRemarkStatusModel {

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

        @SerializedName("DeliveryRemarkID")
        @Expose
        private Integer deliveryRemarkID;
        @SerializedName("DeliveryRemark")
        @Expose
        private String deliveryRemark;

        public Integer getDeliveryRemarkID() {
            return deliveryRemarkID;
        }

        public void setDeliveryRemarkID(Integer deliveryRemarkID) {
            this.deliveryRemarkID = deliveryRemarkID;
        }

        public String getDeliveryRemark() {
            return deliveryRemark;
        }

        public void setDeliveryRemark(String deliveryRemark) {
            this.deliveryRemark = deliveryRemark;
        }

    }
}