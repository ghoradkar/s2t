package com.myhindlab.abkat.pojos;

import com.myhindlab.abkat.models.PatientAttendanceList_Model;

import java.util.ArrayList;

public class PatientAttendanceList_Pojo {

    private ArrayList<PatientAttendanceList_Model> output;

    private String message;

    private String status;

    public ArrayList<PatientAttendanceList_Model> getOutput() {
        return output;
    }

    public void setOutput(ArrayList<PatientAttendanceList_Model> output) {
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
