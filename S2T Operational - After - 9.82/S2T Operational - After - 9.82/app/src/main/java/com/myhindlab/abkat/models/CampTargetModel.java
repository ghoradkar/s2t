package com.myhindlab.abkat.models;

import java.util.List;

public class CampTargetModel {


    /**
     * status : Success
     * message : Camp Target Details
     * output : [{"DISTLGDCODE":490,"DISTNAME":"PUNE","CampCount":0,"PostCampCount":0,"SurveryCount":0,"TargetCount":0}]
     */

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
        /**
         * DISTLGDCODE : 490
         * DISTNAME : PUNE
         * CampCount : 0
         * PostCampCount : 0
         * SurveryCount : 0
         * TargetCount : 0
         */

        private int DISTLGDCODE;
        private String DISTNAME;
        private int CampCount;
        private int PostCampCount;
        private int SurveryCount;
        private int TargetCount;

        public int getDISTLGDCODE() {
            return DISTLGDCODE;
        }

        public void setDISTLGDCODE(int DISTLGDCODE) {
            this.DISTLGDCODE = DISTLGDCODE;
        }

        public String getDISTNAME() {
            return DISTNAME;
        }

        public void setDISTNAME(String DISTNAME) {
            this.DISTNAME = DISTNAME;
        }

        public int getCampCount() {
            return CampCount;
        }

        public void setCampCount(int CampCount) {
            this.CampCount = CampCount;
        }

        public int getPostCampCount() {
            return PostCampCount;
        }

        public void setPostCampCount(int PostCampCount) {
            this.PostCampCount = PostCampCount;
        }

        public int getSurveryCount() {
            return SurveryCount;
        }

        public void setSurveryCount(int SurveryCount) {
            this.SurveryCount = SurveryCount;
        }

        public int getTargetCount() {
            return TargetCount;
        }

        public void setTargetCount(int TargetCount) {
            this.TargetCount = TargetCount;
        }
    }
}
