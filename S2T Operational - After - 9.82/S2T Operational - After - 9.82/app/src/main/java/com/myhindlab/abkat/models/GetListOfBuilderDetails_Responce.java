package com.myhindlab.abkat.models;

import java.util.ArrayList;

public class GetListOfBuilderDetails_Responce {

    private String message;

    private String status;

    private ArrayList<GetListOfBuilderDetails> output;

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

    public ArrayList<GetListOfBuilderDetails> getOutput() {
        return output;
    }

    public void setOutput(ArrayList<GetListOfBuilderDetails> output) {
        this.output = output;
    }
}