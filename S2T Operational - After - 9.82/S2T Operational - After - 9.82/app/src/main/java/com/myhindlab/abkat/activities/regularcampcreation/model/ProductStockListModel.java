package com.myhindlab.abkat.activities.regularcampcreation.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ProductStockListModel {

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

        @SerializedName("ProjectProductMapId")
        @Expose
        private Integer projectProductMapId;
        @SerializedName("ProjectCode")
        @Expose
        private Integer projectCode;
        @SerializedName("PROJECTNAME")
        @Expose
        private String projectname;
        @SerializedName("ProductId")
        @Expose
        private Integer productId;
        @SerializedName("MediProductId")
        @Expose
        private Integer mediProductId;
        @SerializedName("PRODUCTNAME")
        @Expose
        private String productname;
        @SerializedName("AVAILABELSTOCK")
        @Expose
        private Integer availabelstock;

        public Integer getProjectProductMapId() {
            return projectProductMapId;
        }

        public void setProjectProductMapId(Integer projectProductMapId) {
            this.projectProductMapId = projectProductMapId;
        }

        public Integer getProjectCode() {
            return projectCode;
        }

        public void setProjectCode(Integer projectCode) {
            this.projectCode = projectCode;
        }

        public String getProjectname() {
            return projectname;
        }

        public void setProjectname(String projectname) {
            this.projectname = projectname;
        }

        public Integer getProductId() {
            return productId;
        }

        public void setProductId(Integer productId) {
            this.productId = productId;
        }

        public Integer getMediProductId() {
            return mediProductId;
        }

        public void setMediProductId(Integer mediProductId) {
            this.mediProductId = mediProductId;
        }

        public String getProductname() {
            return productname;
        }

        public void setProductname(String productname) {
            this.productname = productname;
        }

        public Integer getAvailabelstock() {
            return availabelstock;
        }

        public void setAvailabelstock(Integer availabelstock) {
            this.availabelstock = availabelstock;
        }

    }
}