package com.myhindlab.abkat.activities.confirmatoryTest.Model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.processing.Generated;

@Generated("jsonschema2pojo")
public class GetMobileNumberModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("output")
    @Expose
    private List<Output> output;

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

    public List<Output> getOutput() {
        return output;
    }

    public void setOutput(List<Output> output) {
        this.output = output;
    }


    @Generated("jsonschema2pojo")
    public class Output {

        @SerializedName("RegdNo")
        @Expose
        private String regdNo;
        @SerializedName("MobileNo")
        @Expose
        private String mobileNo;
        @SerializedName("MTypeID")
        @Expose
        private Integer mTypeID;
        @SerializedName("MobileNoType")
        @Expose
        private String mobileNoType;

        public String getRegdNo() {
            return regdNo;
        }

        public void setRegdNo(String regdNo) {
            this.regdNo = regdNo;
        }

        public String getMobileNo() {
            return mobileNo;
        }

        public void setMobileNo(String mobileNo) {
            this.mobileNo = mobileNo;
        }

        public Integer getMTypeID() {
            return mTypeID;
        }

        public void setMTypeID(Integer mTypeID) {
            this.mTypeID = mTypeID;
        }

        public String getMobileNoType() {
            return mobileNoType;
        }

        public void setMobileNoType(String mobileNoType) {
            this.mobileNoType = mobileNoType;
        }

    }

}