package com.myhindlab.abkat.models;


import java.util.ArrayList;

/**
 * Created by tejasz on 16-02-2018.
 */

public class UserLoginApp_Outputpojo {
    private String message;

    private String status;

//    private ArrayList<UserLoginDetails> output;

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

//    public ArrayList<UserLoginDetails> getOutput() {
//        return output;
//    }

//    public void setOutput(ArrayList<UserLoginDetails> output) {
//        this.output = output;
//    }

    @Override
    public String toString()
    {
//        return "ClassPojo [message = "+message+", status = "+status+", output = "+output+"]";
        return null;
    }
}