package com.myhindlab.abkat.models.couriermodule;

import java.util.ArrayList;

/**
 * Created by tejasz on 14-11-2017.
 */

public class CenterList_OutputPojo {

    private String message;
    private String status;
    private ArrayList<CenterListPojo> output;

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

    public ArrayList<CenterListPojo>  getOutput() {
        return output;
    }

    public void setOutput(ArrayList<CenterListPojo>  output) {
        this.output = output;
    }

    @Override
    public String toString() {
        return "ClassPojo [message = " + message + ", status = " + status + ", output = " + output + "]";
    }
}