package com.myhindlab.abkat.pojos;

import com.myhindlab.abkat.models.MappedCampList_Model;

import java.util.ArrayList;

public class MappedCampList_Pojo {

    private ArrayList<MappedCampList_Model> output;

    private String message;

    private String status;

    public ArrayList<MappedCampList_Model> getOutput() {
        return output;
    }

    public void setOutput(ArrayList<MappedCampList_Model> output) {
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
