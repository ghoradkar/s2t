package com.myhindlab.abkat.models.couriermodule;

import java.util.List;

public class CourierSampleTemperatureModel {


    /**
     * status : Success
     * message : Sample Temperature Details
     * output : [{"SampleTempID":1,"SampleTempName":"Room Temp"},{"SampleTempID":2,"SampleTempName":"2°C to 8°C"}]
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
         * SampleTempID : 1
         * SampleTempName : Room Temp
         */

        private String SampleTempID;
        private String SampleTempName;

        public String getSampleTempID() {
            return SampleTempID;
        }

        public void setSampleTempID(String SampleTempID) {
            this.SampleTempID = SampleTempID;
        }

        public String getSampleTempName() {
            return SampleTempName;
        }

        public void setSampleTempName(String SampleTempName) {
            this.SampleTempName = SampleTempName;
        }
    }
}
