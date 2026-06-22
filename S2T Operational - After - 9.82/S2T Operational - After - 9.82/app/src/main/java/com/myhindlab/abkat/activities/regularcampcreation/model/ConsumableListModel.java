package com.myhindlab.abkat.activities.regularcampcreation.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ConsumableListModel implements Serializable{

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


    public class Output implements Serializable {

        @SerializedName("Productid")
        @Expose
        private Integer productid;
        @SerializedName("ProductName")
        @Expose
        private String productName;
        @SerializedName("ProductUnit")
        @Expose
        private String productUnit;
        @SerializedName("ProductQuantity")
        @Expose
        private Integer productQuantity;
        @SerializedName("ExpectedQuantity")
        @Expose
        private Integer expectedQuantity;

        @SerializedName("AVAILABELSTOCK")
        @Expose
        private Integer availabelstock;


        public Integer getProductid() {
            return productid;
        }

        public void setProductid(Integer productid) {
            this.productid = productid;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getProductUnit() {
            return productUnit;
        }

        public void setProductUnit(String productUnit) {
            this.productUnit = productUnit;
        }

        public Integer getProductQuantity() {
            return productQuantity;
        }

        public void setProductQuantity(Integer productQuantity) {
            this.productQuantity = productQuantity;
        }

        public Integer getExpectedQuantity() {
            return expectedQuantity;
        }

        public void setExpectedQuantity(Integer expectedQuantity) {
            this.expectedQuantity = expectedQuantity;
        }

        public Integer getAvailabelstock() {
            return availabelstock;
        }

        public void setAvailabelstock(Integer availabelstock) {
            this.availabelstock = availabelstock;
        }
    }
}
