package com.myhindlab.abkat.models;

import java.util.List;

public class DoctorModel {

    private String status;
    private String message;
    private List<OutputBean> output;

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

    public List<OutputBean> getOutput() {
        return output;
    }

    public void setOutput(List<OutputBean> output) {
        this.output = output;
    }

    public static class OutputBean {
        private Integer USERID;

        public Integer getUSERID() {
            return USERID;
        }

        public void setUSERID(Integer USERID) {
            this.USERID = USERID;
        }

//        private String userid;

        private String Name;
        private String   ResourceName;


        private boolean isChecked;

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

//        public String getUSERID() {
//            return userid;
//        }

//        public void setUSERID(String USERID) {
//            this.userid = USERID;
//        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public String getResourceName() {
            return ResourceName;
        }

        public void setResourceName(String resourceName) {
            ResourceName = resourceName;
        }
    }
}
