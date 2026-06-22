package com.myhindlab.abkat.activities.regularcampcreation.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ConsumableDetailsForApprovalModel {

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

        @SerializedName("Productid")
        @Expose
        private Integer productid;
        @SerializedName("ProductName")
        @Expose
        private String productName;
        @SerializedName("ExpectedQuantity")
        @Expose
        private Integer expectedQuantity;
        @SerializedName("StockAsPerPhelbo")
        @Expose
        private Integer stockAsPerPhelbo;
        @SerializedName("RequiredQuantity")
        @Expose
        private Integer requiredQuantity;
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

        @SerializedName("AVAILABELSTOCK")
        @Expose
        private Integer availabelstock;

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

        public Integer getExpectedQuantity() {
            return expectedQuantity;
        }

        public void setExpectedQuantity(Integer expectedQuantity) {
            this.expectedQuantity = expectedQuantity;
        }

        public Integer getStockAsPerPhelbo() {
            return stockAsPerPhelbo;
        }

        public void setStockAsPerPhelbo(Integer stockAsPerPhelbo) {
            this.stockAsPerPhelbo = stockAsPerPhelbo;
        }

        public Integer getRequiredQuantity() {
            return requiredQuantity;
        }

        public void setRequiredQuantity(Integer requiredQuantity) {
            this.requiredQuantity = requiredQuantity;
        }

        public Integer getAvailabelstock() {
            return availabelstock;
        }

        public void setAvailabelstock(Integer availabelstock) {
            this.availabelstock = availabelstock;
        }
    }
}