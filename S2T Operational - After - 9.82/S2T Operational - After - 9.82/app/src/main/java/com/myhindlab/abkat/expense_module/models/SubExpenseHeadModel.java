package com.myhindlab.abkat.expense_module.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SubExpenseHeadModel {

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

        @SerializedName("ExpenseHead")
        @Expose
        private Integer expenseHead;
        @SerializedName("subExpenseID")
        @Expose
        private Integer subExpenseID;
        @SerializedName("SubexpenseName")
        @Expose
        private String subexpenseName;
        @SerializedName("Unit")
        @Expose
        private String unit;
        @SerializedName("isbillrequired")
        @Expose
        private Boolean isbillrequired;
        @SerializedName("MaxAllowedAmt")
        @Expose
        private Integer maxAllowedAmt;
        @SerializedName("meghaMaxAllowedAmt")
        @Expose
        private Integer meghaMaxAllowedAmt;
        @SerializedName("subExpenseIDPk")
        @Expose
        private Integer subExpenseIDPk;

        public Integer getExpenseHead() {
            return expenseHead;
        }

        public void setExpenseHead(Integer expenseHead) {
            this.expenseHead = expenseHead;
        }

        public Integer getSubExpenseID() {
            return subExpenseID;
        }

        public void setSubExpenseID(Integer subExpenseID) {
            this.subExpenseID = subExpenseID;
        }

        public String getSubexpenseName() {
            return subexpenseName;
        }

        public void setSubexpenseName(String subexpenseName) {
            this.subexpenseName = subexpenseName;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public Boolean getIsbillrequired() {
            return isbillrequired;
        }

        public void setIsbillrequired(Boolean isbillrequired) {
            this.isbillrequired = isbillrequired;
        }

        public Integer getMaxAllowedAmt() {
            return maxAllowedAmt;
        }

        public void setMaxAllowedAmt(Integer maxAllowedAmt) {
            this.maxAllowedAmt = maxAllowedAmt;
        }

        public Integer getMeghaMaxAllowedAmt() {
            return meghaMaxAllowedAmt;
        }

        public void setMeghaMaxAllowedAmt(Integer meghaMaxAllowedAmt) {
            this.meghaMaxAllowedAmt = meghaMaxAllowedAmt;
        }

        public Integer getSubExpenseIDPk() {
            return subExpenseIDPk;
        }

        public void setSubExpenseIDPk(Integer subExpenseIDPk) {
            this.subExpenseIDPk = subExpenseIDPk;
        }

    }


}