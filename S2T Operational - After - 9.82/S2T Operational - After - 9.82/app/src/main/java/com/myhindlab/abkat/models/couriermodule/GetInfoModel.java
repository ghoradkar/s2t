package com.myhindlab.abkat.models.couriermodule;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GetInfoModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("output")
    @Expose
    private ArrayList<Output> output;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<Output> getOutput() {
        return output;
    }

    public void setOutput(ArrayList<Output> output) {
        this.output = output;
    }


    public class Output {

        @SerializedName("CourierID")
        @Expose
        private Integer courierID;
        @SerializedName("CampID")
        @Expose
        private Integer campID;
        @SerializedName("CampNo")
        @Expose
        private String campNo;
        @SerializedName("CampDate")
        @Expose
        private String campDate;
        @SerializedName("CampType")
        @Expose
        private Integer campType;

        public Integer getCourierID() {
            return courierID;
        }

        public void setCourierID(Integer courierID) {
            this.courierID = courierID;
        }

        public Integer getCampID() {
            return campID;
        }

        public void setCampID(Integer campID) {
            this.campID = campID;
        }

        public String getCampNo() {
            return campNo;
        }

        public void setCampNo(String campNo) {
            this.campNo = campNo;
        }

        public String getCampDate() {
            return campDate;
        }

        public void setCampDate(String campDate) {
            this.campDate = campDate;
        }

        public Integer getCampType() {
            return campType;
        }

        public void setCampType(Integer campType) {
            this.campType = campType;
        }

    }
}