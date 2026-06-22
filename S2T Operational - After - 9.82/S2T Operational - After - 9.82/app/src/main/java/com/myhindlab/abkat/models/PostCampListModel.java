package com.myhindlab.abkat.models;

import java.io.Serializable;
import java.util.ArrayList;

public class PostCampListModel implements Serializable {

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

    public static class OutputBean implements Serializable {
        private String CAMPID;

        private String DistName;

        private String DISTLGDCODE;

        private String CampLocation;

        private String CampName;

        private String CampDate;

        public String getCAMPID() {
            return CAMPID;
        }

        public void setCAMPID(String CAMPID) {
            this.CAMPID = CAMPID;
        }

        public String getDistName() {
            return DistName;
        }

        public void setDistName(String DistName) {
            this.DistName = DistName;
        }

        public String getDISTLGDCODE() {
            return DISTLGDCODE;
        }

        public void setDISTLGDCODE(String DISTLGDCODE) {
            this.DISTLGDCODE = DISTLGDCODE;
        }

        public String getCampLocation() {
            return CampLocation;
        }

        public void setCampLocation(String CampLocation) {
            this.CampLocation = CampLocation;
        }

        public String getCampName() {
            return CampName;
        }

        public void setCampName(String CampName) {
            this.CampName = CampName;
        }

        public String getCampDate() {
            return CampDate;
        }

        public void setCampDate(String CampDate) {
            this.CampDate = CampDate;
        }
    }
}
