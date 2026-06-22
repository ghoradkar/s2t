package com.myhindlab.abkat.models.couriermodule;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CourierTubesModel {

    /**
     * status : Success
     * message : Service Tubes Details
     * output : [{"TubeId":1,"TubeContent":"EDTA"},{"TubeId":2,"TubeContent":"Urine Container"},{"TubeId":3,"TubeContent":"Plain Tube"},{"TubeId":4,"TubeContent":"NBS Card"}]
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
         * TubeId : 1
         * TubeContent : EDTA
         */

        private String TubeId;
        private String TubeContent;
        private String count = "0";

        public String getTubName() {
            return tubName;
        }

        public void setTubName(String tubName) {
            this.tubName = tubName;
        }

        @SerializedName("TubName")
        @Expose
        private String tubName;

        public String getTubeId() {
            return TubeId;
        }

        public void setTubeId(String TubeId) {
            this.TubeId = TubeId;
        }

        public String getTubeContent() {
            return TubeContent;
        }

        public void setTubeContent(String TubeContent) {
            this.TubeContent = TubeContent;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }
    }
}
