package com.myhindlab.abkat.pojos;

import java.util.ArrayList;

public class SelectAddress_pojo {
    private ArrayList<SelectAddress_OutPut_pojo> output;

    private String message;

    private String status;

    public ArrayList<SelectAddress_OutPut_pojo> getOutput ()
    {
        return output;
    }

    public void setOutput (ArrayList<SelectAddress_OutPut_pojo> output)
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
