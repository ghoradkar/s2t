package com.myhindlab.abkat.pojos;

import com.myhindlab.abkat.models.TeamList;

import java.util.ArrayList;

public class TeamListPojo {

    private String message;

    private String status;

    private ArrayList<TeamList> output;

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

    public ArrayList<TeamList> getOutput ()
    {
        return output;
    }

    public void setOutput (ArrayList<TeamList> output)
    {
        this.output = output;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [message = "+message+", status = "+status+", output = "+output+"]";
    }
    
    
    
}
