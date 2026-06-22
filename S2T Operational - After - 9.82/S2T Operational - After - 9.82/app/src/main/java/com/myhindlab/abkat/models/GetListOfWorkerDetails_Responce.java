package com.myhindlab.abkat.models;

import java.util.ArrayList;

public class GetListOfWorkerDetails_Responce {

    private String message;

    private String status;

    private ArrayList<GetListOfWorkerDetails> output;

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

    public ArrayList<GetListOfWorkerDetails> getOutput() {
        return output;
    }

    public void setOutput(ArrayList<GetListOfWorkerDetails> output) {
        this.output = output;
    }
}