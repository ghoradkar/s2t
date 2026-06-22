package com.myhindlab.abkat.models;

import java.util.List;

public class AutoLogoutResponse {
    private String status;
    private String message;
    private List<Output> output;

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<Output> getOutput() {
        return output;
    }

    public static class Output {
        private String DeviceStatus;
        private boolean IsActive;

        public String getDeviceStatus() {
            return DeviceStatus;
        }

        public boolean isIsActive() {
            return IsActive;
        }
    }
}
