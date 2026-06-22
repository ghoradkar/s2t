package com.myhindlab.abkat.pojos;

import com.myhindlab.abkat.models.PatientDetailsOnRegNo_Model;

import java.util.ArrayList;

public class PatientDetailsOnRegNo_Pojo {
    private ArrayList<PatientDetailsOnRegNo_Model> output;

    private String message;

    private String status;

    public ArrayList<PatientDetailsOnRegNo_Model> getOutput() {
        return output;
    }

    public void setOutput(ArrayList<PatientDetailsOnRegNo_Model> output) {
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
