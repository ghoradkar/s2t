package com.myhindlab.abkat.pojos;

import com.myhindlab.abkat.models.PatientDetailsOnRegNo_Model;
import com.myhindlab.abkat.models.PatientStatusModel;

import java.util.ArrayList;

public class PatientStatusPojo {
    private ArrayList<PatientStatusModel> output;

    private String message;

    private String status;

    public ArrayList<PatientStatusModel> getOutput() {
        return output;
    }

    public void setOutput(ArrayList<PatientStatusModel> output) {
        this.output = output;
    }

    public String getMessage ()
    {
        return message;
    }

    public void setMessage (String message)
    {
        this.message = message;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [output = "+output+", message = "+message+", status = "+status+"]";
    }
}
