package com.myhindlab.abkat.models.couriermodule;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BarcodeViewListModel {

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

        @SerializedName("CourierID")
        @Expose
        private Integer courierID;
        @SerializedName("Barcode")
        @Expose
        private String barcode;
        @SerializedName("SampleBarcode")
        @Expose
        private String sampleBarcode;
        @SerializedName("ServiceCode")
        @Expose
        private Integer serviceCode;
        @SerializedName("ServiceName")
        @Expose
        private String serviceName;

        boolean isChecked;

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        public Integer getCourierID() {
            return courierID;
        }

        public void setCourierID(Integer courierID) {
            this.courierID = courierID;
        }

        public String getBarcode() {
            return barcode;
        }

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }

        public String getSampleBarcode() {
            return sampleBarcode;
        }

        public void setSampleBarcode(String sampleBarcode) {
            this.sampleBarcode = sampleBarcode;
        }

        public Integer getServiceCode() {
            return serviceCode;
        }

        public void setServiceCode(Integer serviceCode) {
            this.serviceCode = serviceCode;
        }

        public String getServiceName() {
            return serviceName;
        }

        public void setServiceName(String serviceName) {
            this.serviceName = serviceName;
        }

    }

}
