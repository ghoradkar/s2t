package com.myhindlab.abkat.pojos;

import com.myhindlab.abkat.models.GetApprovedCampListDetailsForAppList;

import java.util.ArrayList;

public class GetApprovedCampListDetailsForApp_Pojo {

    private ArrayList<GetApprovedCampListDetailsForAppList> output;

    private String message;

    private String status;

    public ArrayList<GetApprovedCampListDetailsForAppList> getOutput() {
        return output;
    }

    public void setOutput(ArrayList<GetApprovedCampListDetailsForAppList> output) {
        this.output = output;
    }

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
}
