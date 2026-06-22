package com.myhindlab.abkat.models;

import java.util.ArrayList;

public class CampResourceListModel {

    private String status;
    private String message;
    private ArrayList<OutputBean> output;

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

    public ArrayList<OutputBean> getOutput() {
        return output;
    }

    public void setOutput(ArrayList<OutputBean> output) {
        this.output = output;
    }

    public static class OutputBean {

        private String CampId;
        private String Name;
        private String DesgName;
        private String MOBNO;

        public String getCampId() {
            return CampId;
        }

        public void setCampId(String CampId) {
            this.CampId = CampId;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public String getDesgName() {
            return DesgName;
        }

        public void setDesgName(String DesgName) {
            this.DesgName = DesgName;
        }

        public String getMOBNO() {
            return MOBNO;
        }

        public void setMOBNO(String MOBNO) {
            this.MOBNO = MOBNO;
        }
    }
}
