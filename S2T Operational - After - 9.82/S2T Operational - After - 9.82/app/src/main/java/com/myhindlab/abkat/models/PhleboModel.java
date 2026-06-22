package com.myhindlab.abkat.models;

import java.util.ArrayList;
import java.util.List;

public class PhleboModel {

    private String status;
    private String message;
    private ArrayList<PhleboModel.OutputBean> output;

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

    public ArrayList<PhleboModel.OutputBean> getOutput() {
        return output;
    }

    public void setOutput(ArrayList<PhleboModel.OutputBean> output) {
        this.output = output;
    }

    public static class OutputBean {
        private String USERID;



        private String Name;

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

        public String getUSERID() {
            return USERID;
        }

        public void setUSERID(String USERID) {
            this.USERID = USERID;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }
    }
}
