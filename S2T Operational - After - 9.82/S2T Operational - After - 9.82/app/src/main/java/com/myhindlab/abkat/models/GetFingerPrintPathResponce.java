package com.myhindlab.abkat.models;

import java.util.ArrayList;

public class GetFingerPrintPathResponce {

    private String message;

    private String status = "";

    private ArrayList<GetFingerPrintPath_Model> output;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<GetFingerPrintPath_Model> getOutput() {
        return output;
    }

    public void setOutput(ArrayList<GetFingerPrintPath_Model> output) {
        this.output = output;
    }
}