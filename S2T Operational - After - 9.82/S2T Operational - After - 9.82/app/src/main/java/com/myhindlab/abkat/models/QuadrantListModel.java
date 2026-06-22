package com.myhindlab.abkat.models;

import java.util.List;

public class QuadrantListModel {


    /**
     * status : Success
     * message : Breast Quadrant Values
     * output : [{"QuadID":1,"RightQuadrant":"Upper Outer Quadrant UOQ: (12-3)","LeftQuadrant":"Upper Inner Quadrant UIQ: (12-3)","CreatedBy":1,"CreatedOn":"/Date(1603391400000)/","IsActive":1},{"QuadID":2,"RightQuadrant":"Lower Outer Quadrant LOQ: (3-6)","LeftQuadrant":"Lower Inner Quadrant LIQ: (3-6)","CreatedBy":1,"CreatedOn":"/Date(1603391400000)/","IsActive":1},{"QuadID":3,"RightQuadrant":"Lower Inner Quadrant LIQ: (6-9)","LeftQuadrant":"Lower Outer Quadrant LOQ: (6-9)","CreatedBy":1,"CreatedOn":"/Date(1603391400000)/","IsActive":1},{"QuadID":4,"RightQuadrant":"Upper Inner Quadrant UIQ: (9-12)","LeftQuadrant":"Upper Outer Quadrant UOQ: (9-12)","CreatedBy":1,"CreatedOn":"/Date(1603391400000)/","IsActive":1}]
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
         * QuadID : 1
         * RightQuadrant : Upper Outer Quadrant UOQ: (12-3)
         * LeftQuadrant : Upper Inner Quadrant UIQ: (12-3)
         * CreatedBy : 1
         * CreatedOn : /Date(1603391400000)/
         * IsActive : 1
         */

        private int QuadID;
        private String RightQuadrant;
        private String LeftQuadrant;
        private int CreatedBy;
        private String CreatedOn;
        private int IsActive;
        private boolean isLeftChecked;
        private boolean isRightChecked;

        public int getQuadID() {
            return QuadID;
        }

        public void setQuadID(int QuadID) {
            this.QuadID = QuadID;
        }

        public String getRightQuadrant() {
            return RightQuadrant;
        }

        public void setRightQuadrant(String RightQuadrant) {
            this.RightQuadrant = RightQuadrant;
        }

        public String getLeftQuadrant() {
            return LeftQuadrant;
        }

        public void setLeftQuadrant(String LeftQuadrant) {
            this.LeftQuadrant = LeftQuadrant;
        }

        public int getCreatedBy() {
            return CreatedBy;
        }

        public void setCreatedBy(int CreatedBy) {
            this.CreatedBy = CreatedBy;
        }

        public String getCreatedOn() {
            return CreatedOn;
        }

        public void setCreatedOn(String CreatedOn) {
            this.CreatedOn = CreatedOn;
        }

        public int getIsActive() {
            return IsActive;
        }

        public void setIsActive(int IsActive) {
            this.IsActive = IsActive;
        }

        public boolean isLeftChecked() {
            return isLeftChecked;
        }

        public void setLeftChecked(boolean leftChecked) {
            isLeftChecked = leftChecked;
        }

        public boolean isRightChecked() {
            return isRightChecked;
        }

        public void setRightChecked(boolean rightChecked) {
            isRightChecked = rightChecked;
        }
    }
}
