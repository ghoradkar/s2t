package com.myhindlab.abkat.models.doortodoor;


import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AudioScreeningTestModel {

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

        @SerializedName("Frequency")
        @Expose
        private Integer frequency;
        @SerializedName("Right_DB")
        @Expose
        private Integer rightDB;
        @SerializedName("Left_DB")
        @Expose
        private Integer leftDB;
        @SerializedName("Remark")
        @Expose
        private String remark;
        @SerializedName("Right_Remark")
        @Expose
        private String rightRemark;

        public Integer getFrequency() {
            return frequency;
        }

        public void setFrequency(Integer frequency) {
            this.frequency = frequency;
        }

        public Integer getRightDB() {
            return rightDB;
        }

        public void setRightDB(Integer rightDB) {
            this.rightDB = rightDB;
        }

        public Integer getLeftDB() {
            return leftDB;
        }

        public void setLeftDB(Integer leftDB) {
            this.leftDB = leftDB;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getRightRemark() {
            return rightRemark;
        }

        public void setRightRemark(String rightRemark) {
            this.rightRemark = rightRemark;
        }

    }
}