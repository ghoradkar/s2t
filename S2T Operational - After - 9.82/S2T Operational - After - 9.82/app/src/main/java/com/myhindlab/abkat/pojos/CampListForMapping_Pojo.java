package com.myhindlab.abkat.pojos;

import com.myhindlab.abkat.models.CampListForMapping_Model;

import java.util.ArrayList;

public class CampListForMapping_Pojo {
    private ArrayList<CampListForMapping_Model> output;

    private String message;

    private String status;

    public ArrayList<CampListForMapping_Model> getOutput() {
        return output;
    }

    public void setOutput(ArrayList<CampListForMapping_Model> output) {
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
