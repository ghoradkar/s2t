package com.myhindlab.abkat.models.couriermodule;


import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BarcodeListCountNewModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("output")
    @Expose
    private ArrayList<Output> output;

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

    public ArrayList<Output> getOutput() {
        return output;
    }

    public void setOutput(ArrayList<Output> output) {
        this.output = output;
    }


    public class Output {

        @SerializedName("TotalBarcode")
        @Expose
        private String totalBarcode;



        public String getTotalBarcode() {
            return totalBarcode;
        }

        public void setTotalBarcode(String totalBarcode) {
            this.totalBarcode = totalBarcode;
        }

    }
}