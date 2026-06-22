package com.myhindlab.abkat.models.couriermodule;

import java.util.List;

public class CourierTransportModeModel {


    /**
     * status : Success
     * message : Courier TransporT Mode Details
     * output : [{"ModeId":1,"ModeName":"Courier Service"},{"ModeId":2,"ModeName":"Bus Service"},{"ModeId":3,"ModeName":"Runner Boy"},{"ModeId":4,"ModeName":"Private Vehicle"}]
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
         * ModeId : 1
         * ModeName : Courier Service
         */

        private String ModeId;
        private String ModeName;

        public String getModeId() {
            return ModeId;
        }

        public void setModeId(String ModeId) {
            this.ModeId = ModeId;
        }

        public String getModeName() {
            return ModeName;
        }

        public void setModeName(String ModeName) {
            this.ModeName = ModeName;
        }
    }
}
