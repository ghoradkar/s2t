package com.myhindlab.abkat.pojos;

import com.myhindlab.abkat.models.DistrictList_Model;
import com.myhindlab.abkat.models.DistrictNewModel;

import java.util.ArrayList;

public class DistrictListNew_Pojo {
    private ArrayList<DistrictNewModel> output;

    private String message;

    private String status;

    public ArrayList<DistrictNewModel> getOutput() {
        return output;
    }

    public void setOutput(ArrayList<DistrictNewModel> output) {
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
