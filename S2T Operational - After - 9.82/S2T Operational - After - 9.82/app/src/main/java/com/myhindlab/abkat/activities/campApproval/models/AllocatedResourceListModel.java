package com.myhindlab.abkat.activities.campApproval.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllocatedResourceListModel {

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

        @SerializedName("ModifiedBy")
        @Expose
        private int modifiedBy;

        public int getModifiedBy() {
            return modifiedBy;
        }

        public void setModifiedBy(int modifiedBy) {
            this.modifiedBy = modifiedBy;
        }

        @SerializedName("ResStatus")
        @Expose
        private String resStatus;

        @SerializedName("ResourceUserId")
        @Expose
        private Integer resourceUserId;

        public Integer getResourceUserId() {
            return resourceUserId;
        }

        public void setResourceUserId(Integer resourceUserId) {
            this.resourceUserId = resourceUserId;
        }

        @SerializedName("ApproveStatus")
        @Expose
        private int approveStatus;

        public int getApproveStatus() {
            return approveStatus;
        }

        public void setApproveStatus(int approveStatus) {
            this.approveStatus = approveStatus;
        }

        @SerializedName("IsApproved")
        @Expose
        private Integer isApproved;
        @SerializedName("Remark")
        @Expose
        private String remark;
        @SerializedName("CampId")
        @Expose
        private Integer campId;

        private boolean isChecked;

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

        public String getResStatus() {
            return resStatus.trim();
        }

        public void setResStatus(String resStatus) {
            this.resStatus = resStatus;
        }

        public Integer getIsApproved() {
            return isApproved;
        }

        public void setIsApproved(Integer isApproved) {
            this.isApproved = isApproved;
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

    }
}
