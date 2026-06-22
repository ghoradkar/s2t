package com.myhindlab.abkat.pojos;

import com.myhindlab.abkat.models.UnregisteredWorkersModel;

import java.util.ArrayList;

public class UnregisteredWorkersPojo {
    private ArrayList<UnregisteredWorkersModel> output;

    private String message;

    private String status;

    public ArrayList<UnregisteredWorkersModel> getOutput() {
        return output;
    }

    public void setOutput(ArrayList<UnregisteredWorkersModel> output) {
        this.output = output;
    }

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

}
