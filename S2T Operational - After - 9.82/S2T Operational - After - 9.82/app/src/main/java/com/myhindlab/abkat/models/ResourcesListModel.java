package com.myhindlab.abkat.models;

import java.util.ArrayList;

public class ResourcesListModel {

    private String status;
    private String message;
    private ArrayList<ResourcesListModel.OutputBean> output;

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

    public ArrayList<ResourcesListModel.OutputBean> getOutput() {
        return output;
    }

    public void setOutput(ArrayList<ResourcesListModel.OutputBean> output) {
        this.output = output;
    }

    public static class OutputBean {

        private String DesgId;
        private String DesgName;

        public String getDesgId() {
            return DesgId;
        }

        public void setDesgId(String desgId) {
            DesgId = desgId;
        }

        public String getDesgName() {
            return DesgName;
        }

        public void setDesgName(String desgName) {
            DesgName = desgName;
        }

        public String getResourceName() {
            return ResourceName;
        }

        public void setResourceName(String resourceName) {
            ResourceName = resourceName;
        }

        private String ResourceName;

        private boolean isChecked;

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

    }
}
