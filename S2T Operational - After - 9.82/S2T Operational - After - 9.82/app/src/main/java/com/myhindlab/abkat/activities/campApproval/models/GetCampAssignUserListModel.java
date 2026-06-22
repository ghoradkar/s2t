package com.myhindlab.abkat.activities.campApproval.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetCampAssignUserListModel {

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
        @SerializedName("ResourceUserId")
        @Expose
        private Integer resourceUserId;
        @SerializedName("StatusRes")
        @Expose
        private Integer statusRes;

        public Integer getCampId() {
            return campId;
        }

        public void setCampId(Integer campId) {
            this.campId = campId;
        }

        public Integer getResourceUserId() {
            return resourceUserId;
        }

        public void setResourceUserId(Integer resourceUserId) {
            this.resourceUserId = resourceUserId;
        }

        public Integer getStatusRes() {
            return statusRes;
        }

        public void setStatusRes(Integer statusRes) {
            this.statusRes = statusRes;
        }

    }
}
