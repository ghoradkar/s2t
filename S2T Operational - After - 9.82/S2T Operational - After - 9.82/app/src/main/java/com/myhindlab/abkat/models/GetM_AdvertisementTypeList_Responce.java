package com.myhindlab.abkat.models;

import java.util.ArrayList;

public class GetM_AdvertisementTypeList_Responce {

    private String message;

    private String status;

    private ArrayList<GetM_AdvertisementTypeList> output;

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

    public ArrayList<GetM_AdvertisementTypeList> getOutput() {
        return output;
    }

    public void setOutput(ArrayList<GetM_AdvertisementTypeList> output) {
        this.output = output;
    }
}