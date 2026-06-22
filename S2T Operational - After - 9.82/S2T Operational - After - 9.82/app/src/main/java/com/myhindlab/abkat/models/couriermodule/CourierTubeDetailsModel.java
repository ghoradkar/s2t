package com.myhindlab.abkat.models.couriermodule;

import java.util.List;

public class CourierTubeDetailsModel {


    /**
     * status : Success
     * message : Tube Details
     * output : [{"CourierID":22,"TubeId":1,"TubeContent":"EDTA","TotalCount":0},{"CourierID":22,"TubeId":2,"TubeContent":"Urine Container","TotalCount":4},{"CourierID":22,"TubeId":3,"TubeContent":"Plain Tube","TotalCount":5},{"CourierID":22,"TubeId":4,"TubeContent":"NBS Card","TotalCount":1}]
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
         * CourierID : 22
         * TubeId : 1
         * TubeContent : EDTA
         * TotalCount : 0
         */

        private String CourierID;
        private String TubeId;
        private String TubeContent;
        private String TotalCount;
        private String count;

        public String getCourierID() {
            return CourierID;
        }

        public void setCourierID(String CourierID) {
            this.CourierID = CourierID;
        }

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

        public String getTotalCount() {
            return TotalCount;
        }

        public void setTotalCount(String TotalCount) {
            this.TotalCount = TotalCount;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }
    }
}
