package com.myhindlab.abkat.activities.campApproval.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ResourceDetailsForApprovalModel {

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

        @SerializedName("CampId")
        @Expose
        private Integer campId;
        @SerializedName("TestId")
        @Expose
        private Integer testId;
        @SerializedName("DesgName")
        @Expose
        private String desgName;
        @SerializedName("ResourceUserId")
        @Expose
        private Integer resourceUserId;
        @SerializedName("ResourceName")
        @Expose
        private String resourceName;

        @SerializedName("Remark")
        @Expose
        private String remark;

        @SerializedName("ApproveStatus")
        @Expose
        private int approveStatus;

        @SerializedName("ModifiedBy")
        @Expose
        private int modifiedBy;

        @SerializedName("TestName")
        @Expose
        private String testName;


        @SerializedName("resources")
        @Expose
        private List<AllocatedResourceListModel.Output> resources;

        public Output(Integer campId, Integer testId, String desgName, Integer resourceUserId, String resourceName, String remark, int approveStatus, int modifiedBy, String testName, List<AllocatedResourceListModel.Output> resources) {
            this.campId = campId;
            this.testId = testId;
            this.desgName = desgName;
            this.resourceUserId = resourceUserId;
            this.resourceName = resourceName;
            this.remark = remark;
            this.approveStatus = approveStatus;
            this.modifiedBy = modifiedBy;
            this.testName = testName;
            this.resources = resources;
        }

        public List<AllocatedResourceListModel.Output> getResources() {
            return resources;
        }

        public void setResources(List<AllocatedResourceListModel.Output> subDevices) {
            this.resources = subDevices;
        }

        public String getTestName() {
            return testName;
        }

        public void setTestName(String testName) {
            this.testName = testName;
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


        public Integer getCampId() {
            return campId;
        }

        public void setCampId(Integer campId) {
            this.campId = campId;
        }

        public Integer getTestId() {
            return testId;
        }

        public void setTestId(Integer testId) {
            this.testId = testId;
        }

        public String getDesgName() {
            return desgName;
        }

        public void setDesgName(String desgName) {
            this.desgName = desgName;
        }

        public Integer getResourceUserId() {
            return resourceUserId;
        }

        public void setResourceUserId(Integer resourceUserId) {
            this.resourceUserId = resourceUserId;
        }

        public String getResourceName() {
            return resourceName;
        }

        public void setResourceName(String resourceName) {
            this.resourceName = resourceName;
        }

    }

}