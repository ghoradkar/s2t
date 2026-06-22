package com.myhindlab.abkat.models.doortodoor;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckListResponseModel {

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

        @SerializedName("CheckListID")
        @Expose
        private Integer checkListID;
        @SerializedName("CheckListDescription")
        @Expose
        private String checkListDescription;

        private boolean isChecked;

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        public Integer getCheckListID() {
            return checkListID;
        }

        public void setCheckListID(Integer checkListID) {
            this.checkListID = checkListID;
        }

        public String getCheckListDescription() {
            return checkListDescription;
        }

        public void setCheckListDescription(String checkListDescription) {
            this.checkListDescription = checkListDescription;
        }

    }
}
