package com.myhindlab.abkat.models.couriermodule;

import java.util.List;

public class CourierRemarkModel {

    /**
     * status : Success
     * message : Courier Remarks List
     * output : [{"RemarkId":1,"Remarks":"TFT SAMPLES"},{"RemarkId":2,"Remarks":"DELIVERED"},{"RemarkId":3,"Remarks":"REAGENT"},{"RemarkId":4,"Remarks":"LAB GOODS"}]
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
         * RemarkId : 1
         * Remarks : TFT SAMPLES
         */

        private int RemarkId;
        private String Remarks;

        public OutputBean(int remarkId, String remarks) {
            RemarkId = remarkId;
            Remarks = remarks;
        }

        public OutputBean() {
        }

        public int getRemarkId() {
            return RemarkId;
        }

        public void setRemarkId(int RemarkId) {
            this.RemarkId = RemarkId;
        }

        public String getRemarks() {
            return Remarks;
        }

        public void setRemarks(String Remarks) {
            this.Remarks = Remarks;
        }
    }
}
