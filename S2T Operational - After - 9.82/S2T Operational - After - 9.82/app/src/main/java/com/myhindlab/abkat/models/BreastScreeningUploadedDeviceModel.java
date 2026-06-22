package com.myhindlab.abkat.models;

import java.util.List;

public class BreastScreeningUploadedDeviceModel {

    /**
     * status : Success
     * message : Device List
     * output : [{"DeviceId":2,"CampId":780,"UserId":2341,"DeviceName":"07B043","DeviceImagePath":"http://testmcwwb.myhindlab.com/BETA_MYHINDLABDOCS/CampDocs/BreastDeviceImage/2_2341_780_.png"}]
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
         * DeviceId : 2
         * CampId : 780
         * UserId : 2341
         * DeviceName : 07B043
         * DeviceImagePath : http://testmcwwb.myhindlab.com/BETA_MYHINDLABDOCS/CampDocs/BreastDeviceImage/2_2341_780_.png
         */

        private int DeviceId;
        private int CampId;
        private int UserId;
        private String DeviceName;
        private String DeviceImagePath;

        public int getDeviceId() {
            return DeviceId;
        }

        public void setDeviceId(int DeviceId) {
            this.DeviceId = DeviceId;
        }

        public int getCampId() {
            return CampId;
        }

        public void setCampId(int CampId) {
            this.CampId = CampId;
        }

        public int getUserId() {
            return UserId;
        }

        public void setUserId(int UserId) {
            this.UserId = UserId;
        }

        public String getDeviceName() {
            return DeviceName;
        }

        public void setDeviceName(String DeviceName) {
            this.DeviceName = DeviceName;
        }

        public String getDeviceImagePath() {
            return DeviceImagePath;
        }

        public void setDeviceImagePath(String DeviceImagePath) {
            this.DeviceImagePath = DeviceImagePath;
        }
    }
}
