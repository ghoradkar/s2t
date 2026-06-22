package com.myhindlab.abkat.models;

import java.util.ArrayList;

public class GetListOfLungandAudioImageDetails_Responce {

    private String message;

    private String status;

    private ArrayList<GetListOfLungansAudioImageDetails_Model> output;

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

    public ArrayList<GetListOfLungansAudioImageDetails_Model> getOutput() {
        return output;
    }

    public void setOutput(ArrayList<GetListOfLungansAudioImageDetails_Model> output) {
        this.output = output;
    }
}