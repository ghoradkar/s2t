package com.myhindlab.abkat.pojos;

import com.myhindlab.abkat.models.ConstructionSitesList_Model;

import java.util.ArrayList;

public class ConstructionSitesList_Pojo {

    private ArrayList<ConstructionSitesList_Model> output;

    private String message;

    private String status;

    public ArrayList<ConstructionSitesList_Model> getOutput() {
        return output;
    }

    public void setOutput(ArrayList<ConstructionSitesList_Model> output) {
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

    public ArrayList<ConstructionSitesList_Model> getSiteCompletedOutput() {
        if (output != null && output.size() > 0) {
            ArrayList<ConstructionSitesList_Model> siteList = new ArrayList<>();
            for (ConstructionSitesList_Model data : output) {
                if (data.getFlag().equalsIgnoreCase("1")) {
                    siteList.add(data);
                }
            }
            return siteList;
        } else {
            return output;
        }
    }

    public ArrayList<ConstructionSitesList_Model> getSiteNotCompletedOutput() {
        if (output != null && output.size() > 0) {
            ArrayList<ConstructionSitesList_Model> siteList = new ArrayList<>();
            for (ConstructionSitesList_Model data : output) {
                if (data.getFlag().equalsIgnoreCase("0")) {
                    siteList.add(data);
                }
            }
            return siteList;
        } else {
            return output;
        }
    }
}
