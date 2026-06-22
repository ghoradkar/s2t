package com.myhindlab.abkat.pojos;

import com.myhindlab.abkat.models.HealthHistoryListModel;

import java.util.ArrayList;

public class HealthHistoryListPojo {

    private ArrayList<HealthHistoryListModel> output;

    private String message;

    private String status;

    public ArrayList<HealthHistoryListModel> getOutput ()
    {
        return output;
    }

    public void setOutput (ArrayList<HealthHistoryListModel> output)
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
}
