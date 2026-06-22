package com.myhindlab.abkat.expense_module.models;


import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExpenseHeadModel {

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
        @SerializedName("ExpenseHeadName")
        @Expose
        private String expenseHeadName;
        @SerializedName("CreatedOn")
        @Expose
        private String createdOn;
        @SerializedName("CreatedBy")
        @Expose
        private Integer createdBy;
        @SerializedName("IsActive")
        @Expose
        private Integer isActive;

        public Integer getExpenseHead() {
            return expenseHead;
        }

        public void setExpenseHead(Integer expenseHead) {
            this.expenseHead = expenseHead;
        }

        public String getExpenseHeadName() {
            return expenseHeadName;
        }

        public void setExpenseHeadName(String expenseHeadName) {
            this.expenseHeadName = expenseHeadName;
        }

        public String getCreatedOn() {
            return createdOn;
        }

        public void setCreatedOn(String createdOn) {
            this.createdOn = createdOn;
        }

        public Integer getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(Integer createdBy) {
            this.createdBy = createdBy;
        }

        public Integer getIsActive() {
            return isActive;
        }

        public void setIsActive(Integer isActive) {
            this.isActive = isActive;
        }

    }
}