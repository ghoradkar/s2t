package com.myhindlab.abkat.activities.regularcampcreation.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ResourceListForMappingModel implements Serializable{

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

        @SerializedName("USERID")
        @Expose
        private Integer userid;
        @SerializedName("ResourceName")
        @Expose
        private String resourceName;
        @SerializedName("DESGID")
        @Expose
        private Integer desgid;
        @SerializedName("TestId")
        @Expose
        private Integer testId;

        private boolean isChecked;

        public Output(Integer userid, String resourceName, Integer desgid, Integer testId, boolean isChecked) {
            this.userid = userid;
            this.resourceName = resourceName;
            this.desgid = desgid;
            this.testId = testId;
            this.isChecked = isChecked;
        }

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        public Integer getUserid() {
            return userid;
        }

        public void setUserid(Integer userid) {
            this.userid = userid;
        }

        public String getResourceName() {
            return resourceName;
        }

        public void setResourceName(String resourceName) {
            this.resourceName = resourceName;
        }

        public Integer getDesgid() {
            return desgid;
        }

        public void setDesgid(Integer desgid) {
            this.desgid = desgid;
        }

        public Integer getTestId() {
            return testId;
        }

        public void setTestId(Integer testId) {
            this.testId = testId;
        }
    }

}