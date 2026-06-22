package com.myhindlab.abkat.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PostCampDoctorRemarkListModel {

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

        @SerializedName("REGDID")
        @Expose
        private Integer regdid;
        @SerializedName("FinalRemark")
        @Expose
        private String finalRemark;
        @SerializedName("DocReccomandation")
        @Expose
        private String docReccomandation;

        public Integer getRegdid() {
            return regdid;
        }

        public void setRegdid(Integer regdid) {
            this.regdid = regdid;
        }

        public String getFinalRemark() {
            return finalRemark;
        }

        public void setFinalRemark(String finalRemark) {
            this.finalRemark = finalRemark;
        }

        public String getDocReccomandation() {
            return docReccomandation;
        }

        public void setDocReccomandation(String docReccomandation) {
            this.docReccomandation = docReccomandation;
        }

    }

}