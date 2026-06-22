package com.myhindlab.abkat.models;


import java.util.List;

import javax.annotation.processing.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class GpModel {

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

        @SerializedName("GPNAME")
        @Expose
        private String gpname;
        @SerializedName("GPLGDCODE")
        @Expose
        private String gplgdcode;

        public String getGpname() {
            return gpname;
        }

        public void setGpname(String gpname) {
            this.gpname = gpname;
        }

        public String getGplgdcode() {
            return gplgdcode;
        }

        public void setGplgdcode(String gplgdcode) {
            this.gplgdcode = gplgdcode;
        }

    }

}