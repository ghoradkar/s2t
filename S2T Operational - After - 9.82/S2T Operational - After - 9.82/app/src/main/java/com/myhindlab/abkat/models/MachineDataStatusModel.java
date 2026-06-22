package com.myhindlab.abkat.models;


import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MachineDataStatusModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("output")
    @Expose
    private List<Output> output;

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

    public List<Output> getOutput() {
        return output;
    }

    public void setOutput(List<Output> output) {
        this.output = output;
    }


    public class Output {

        @SerializedName("IsWeightingMachineAvailable")
        @Expose
        private String isWeightingMachineAvailable;
        @SerializedName("IsBPMachineAvailable")
        @Expose
        private String isBPMachineAvailable;
        @SerializedName("IsSugarDeviceAvailable")
        @Expose
        private String isSugarDeviceAvailable;

        public String getIsWeightingMachineAvailable() {
            return isWeightingMachineAvailable;
        }

        public void setIsWeightingMachineAvailable(String isWeightingMachineAvailable) {
            this.isWeightingMachineAvailable = isWeightingMachineAvailable;
        }

        public String getIsBPMachineAvailable() {
            return isBPMachineAvailable;
        }

        public void setIsBPMachineAvailable(String isBPMachineAvailable) {
            this.isBPMachineAvailable = isBPMachineAvailable;
        }

        public String getIsSugarDeviceAvailable() {
            return isSugarDeviceAvailable;
        }

        public void setIsSugarDeviceAvailable(String isSugarDeviceAvailable) {
            this.isSugarDeviceAvailable = isSugarDeviceAvailable;
        }
    }
}