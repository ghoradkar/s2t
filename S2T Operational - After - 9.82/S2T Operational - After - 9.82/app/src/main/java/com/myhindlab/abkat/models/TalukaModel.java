package com.myhindlab.abkat.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TalukaModel {

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


    public class Output {

        @SerializedName("TALLGDCODE")
        @Expose
        private Integer tLLGDCODE;

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        @SerializedName("DISTLGDCODE")
        @Expose
        private Integer dISTLGDCODE;

        private boolean isChecked;


        public Integer gettLLGDCODE() {
            return tLLGDCODE;
        }

        public void settLLGDCODE(Integer tLLGDCODE) {
            this.tLLGDCODE = tLLGDCODE;
        }

        public String gettALNAME() {
            return tALNAME;
        }

        public void settALNAME(String tALNAME) {
            this.tALNAME = tALNAME;
        }

        @SerializedName("TALNAME")
        @Expose
        private String tALNAME;

        public Output(String TALLNAME, int TALLGCODE) {
            this.tALNAME = TALLNAME;
            this.tLLGDCODE = TALLGCODE;
        }



    }
}