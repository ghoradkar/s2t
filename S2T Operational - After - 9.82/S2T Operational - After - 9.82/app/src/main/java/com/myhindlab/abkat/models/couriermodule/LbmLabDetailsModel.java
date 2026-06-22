package com.myhindlab.abkat.models.couriermodule;

import java.util.List;

public class LbmLabDetailsModel {


    /**
     * status : Success
     * message : LBM Labs Details
     * output : [{"labcode":22,"LabName":"Latur","DISTLGDCODE":481},{"labcode":23,"LabName":"Udgir","DISTLGDCODE":481}]
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
         * labcode : 22
         * LabName : Latur
         * DISTLGDCODE : 481
         */

        private int labcode;
        private String LabName;
        private int DISTLGDCODE;

        public int getLabcode() {
            return labcode;
        }

        public void setLabcode(int labcode) {
            this.labcode = labcode;
        }

        public String getLabName() {
            return LabName;
        }

        public void setLabName(String LabName) {
            this.LabName = LabName;
        }

        public int getDISTLGDCODE() {
            return DISTLGDCODE;
        }

        public void setDISTLGDCODE(int DISTLGDCODE) {
            this.DISTLGDCODE = DISTLGDCODE;
        }
    }
}
