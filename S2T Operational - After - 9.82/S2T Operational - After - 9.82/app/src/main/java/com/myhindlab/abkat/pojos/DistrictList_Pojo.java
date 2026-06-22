package com.myhindlab.abkat.pojos;

import com.myhindlab.abkat.models.DistrictList_Model;
import com.myhindlab.abkat.models.DistrictNewModel;

import java.util.ArrayList;

public class DistrictList_Pojo {
    private ArrayList<DistrictList_Model> output;

    private String message;

    private String status;

    public ArrayList<DistrictList_Model> getOutput() {
        return output;
    }

    public void setOutput(ArrayList<DistrictList_Model> output) {
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
