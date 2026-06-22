package com.myhindlab.abkat.models.couriermodule;

import java.util.ArrayList;

public class CourierListPojo {

    private String message;
    private String status;
    private ArrayList<CourierDetailsPojo> output;

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

    public ArrayList<CourierDetailsPojo> getOutput() {
        return output;
    }

    public void setOutput(ArrayList<CourierDetailsPojo> output) {
        this.output = output;
    }

    @Override
    public String toString() {
        return "ClassPojo [message = " + message + ", status = " + status + ", list = " + output + "]";
    }
}
