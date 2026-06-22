package com.myhindlab.abkat.models;

import java.util.List;

public class BreastDeviceModel {


    /**
     * status : Success
     * message : Device List
     * output : [{"DeviceId":1,"DeviceName":"IBE095F8","isActive":true,"CreatedBy":1,"CreatedOn":"/Date(1582789778217)/","ModifiyBy":null,"ModifiedOn":null}]
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
         * DeviceId : 1
         * DeviceName : IBE095F8
         * isActive : true
         * CreatedBy : 1
         * CreatedOn : /Date(1582789778217)/
         * ModifiyBy : null
         * ModifiedOn : null
         */

        private String DeviceId;
        private String DeviceName;
        private String isActive;
        private String CreatedBy;
        private String CreatedOn;
        private String ModifiyBy;
        private String ModifiedOn;

        public String getDeviceId() {
            return DeviceId;
        }

        public void setDeviceId(String DeviceId) {
            this.DeviceId = DeviceId;
        }

        public String getDeviceName() {
            return DeviceName;
        }

        public void setDeviceName(String DeviceName) {
            this.DeviceName = DeviceName;
        }

        public String isIsActive() {
            return isActive;
        }

        public void setIsActive(String isActive) {
            this.isActive = isActive;
        }

        public String getCreatedBy() {
            return CreatedBy;
        }

        public void setCreatedBy(String CreatedBy) {
            this.CreatedBy = CreatedBy;
        }

        public String getCreatedOn() {
            return CreatedOn;
        }

        public void setCreatedOn(String CreatedOn) {
            this.CreatedOn = CreatedOn;
        }

        public String getModifiyBy() {
            return ModifiyBy;
        }

        public void setModifiyBy(String ModifiyBy) {
            this.ModifiyBy = ModifiyBy;
        }

        public String getModifiedOn() {
            return ModifiedOn;
        }

        public void setModifiedOn(String ModifiedOn) {
            this.ModifiedOn = ModifiedOn;
        }
    }
}
