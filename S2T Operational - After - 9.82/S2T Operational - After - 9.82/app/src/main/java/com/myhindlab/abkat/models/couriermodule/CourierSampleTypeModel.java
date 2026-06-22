package com.myhindlab.abkat.models.couriermodule;

import java.util.List;

public class CourierSampleTypeModel {


    /**
     * status : Success
     * message : Courier Sample Type List
     * output : [{"SampleTypeID":1,"SampleType":"Special"},{"SampleTypeID":2,"SampleType":"Thyrocare"},{"SampleTypeID":3,"SampleType":"EQUAS"},{"SampleTypeID":4,"SampleType":"TFT"},{"SampleTypeID":5,"SampleType":"NBS"},{"SampleTypeID":6,"SampleType":"Immunassay"},{"SampleTypeID":7,"SampleType":"CBC"},{"SampleTypeID":8,"SampleType":"Biochemistry"},{"SampleTypeID":9,"SampleType":"Other"}]
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
         * SampleTypeID : 1
         * SampleType : Special
         */

        private int SampleTypeID;
        private String SampleType;
        private boolean isChecked;

        public int getSampleTypeID() {
            return SampleTypeID;
        }

        public void setSampleTypeID(int SampleTypeID) {
            this.SampleTypeID = SampleTypeID;
        }

        public String getSampleType() {
            return SampleType;
        }

        public void setSampleType(String SampleType) {
            this.SampleType = SampleType;
        }

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }
    }
}
