package com.myhindlab.abkat.models.couriermodule;

import java.util.ArrayList;

public class ReceivedCourierPojo {
    private ArrayList<ReceivedCourierModel> output;

    private String message;

    private String status;

    public ArrayList<ReceivedCourierModel> getOutput ()
    {
        return output;
    }

    public void setOutput (ArrayList<ReceivedCourierModel> output)
    {
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
