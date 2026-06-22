package com.myhindlab.abkat.pojos;

import java.util.ArrayList;

public class GetNearestHospital_Pojo {
    private ArrayList<GetNearestHospital_OutPut_Pojo> output;

    private String message;

    private String status;

    public ArrayList<GetNearestHospital_OutPut_Pojo> getOutput ()
    {
        return output;
    }

    public void setOutput (ArrayList<GetNearestHospital_OutPut_Pojo> output)
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
