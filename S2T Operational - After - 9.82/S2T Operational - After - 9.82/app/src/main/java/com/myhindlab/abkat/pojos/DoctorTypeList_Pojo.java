package com.myhindlab.abkat.pojos;

import com.myhindlab.abkat.models.DoctorTypeList_Model;

import java.util.ArrayList;

public class DoctorTypeList_Pojo {
    private ArrayList<DoctorTypeList_Model> output;

    private String message;

    private String status;

    public ArrayList<DoctorTypeList_Model> getOutput() {
        return output;
    }

    public void setOutput(ArrayList<DoctorTypeList_Model> output) {
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

    @Override
    public String toString() {
        return "ClassPojo [output = " + output + ", message = " + message + ", status = " + status + "]";
    }
}
