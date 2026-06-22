package com.myhindlab.abkat.pojos;

import com.myhindlab.abkat.models.ScrenningTestModel;
import com.myhindlab.abkat.models.TeamList;

import java.util.ArrayList;

public class ScreeningTestPojo {
    private ArrayList<ScrenningTestModel> output;

    private String message;

    private String status;

    public ArrayList<ScrenningTestModel> getOutput() {
        return output;
    }

    public void setOutput(ArrayList<ScrenningTestModel> output) {
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
