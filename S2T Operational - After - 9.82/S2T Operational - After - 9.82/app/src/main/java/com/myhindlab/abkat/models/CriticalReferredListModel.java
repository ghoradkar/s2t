package com.myhindlab.abkat.models;

import java.util.ArrayList;

public class CriticalReferredListModel {

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
        private String CampDate;
        private String DISTLGDCODE;
        private String RegdId;
        private String RegdNo;
        private String EnglishName;
        private String Status;

        public String getCampId() {
            return CampId;
        }

        public void setCampId(String CampId) {
            this.CampId = CampId;
        }

        public String getCampDate() {
            return CampDate;
        }

        public void setCampDate(String CampDate) {
            this.CampDate = CampDate;
        }

        public String getDISTLGDCODE() {
            return DISTLGDCODE;
        }

        public void setDISTLGDCODE(String DISTLGDCODE) {
            this.DISTLGDCODE = DISTLGDCODE;
        }

        public String getRegdId() {
            return RegdId;
        }

        public void setRegdId(String RegdId) {
            this.RegdId = RegdId;
        }

        public String getRegdNo() {
            return RegdNo;
        }

        public void setRegdNo(String RegdNo) {
            this.RegdNo = RegdNo;
        }

        public String getEnglishName() {
            return EnglishName;
        }

        public void setEnglishName(String EnglishName) {
            this.EnglishName = EnglishName;
        }

        public String getStatus() {
            if (Status.equals("")) {
                return "NA";
            } else {
                return Status;
            }
        }

        public void setStatus(String status) {
            Status = status;
        }
    }
}
