package com.myhindlab.abkat.models.doortodoor;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AssignedPhysicalExamModel {

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

        @SerializedName("CampId")
        @Expose
        private Integer campId;
        @SerializedName("Campdate")
        @Expose
        private String campdate;
        @SerializedName("RegdId")
        @Expose
        private Integer regdId;
        @SerializedName("EnglishName")
        @Expose
        private String englishName;
        @SerializedName("BenificiaryRegdID")
        @Expose
        private Integer benificiaryRegdID;
        @SerializedName("Type")
        @Expose
        private String type;
        @SerializedName("BenificiaryRegdNo")
        @Expose
        private String benificiaryRegdNo;

        public Integer getCampId() {
            return campId;
        }

        public void setCampId(Integer campId) {
            this.campId = campId;
        }

        public String getCampdate() {
            return campdate;
        }

        public void setCampdate(String campdate) {
            this.campdate = campdate;
        }

        public Integer getRegdId() {
            return regdId;
        }

        public void setRegdId(Integer regdId) {
            this.regdId = regdId;
        }

        public String getEnglishName() {
            return englishName;
        }

        public void setEnglishName(String englishName) {
            this.englishName = englishName;
        }

        public Integer getBenificiaryRegdID() {
            return benificiaryRegdID;
        }

        public void setBenificiaryRegdID(Integer benificiaryRegdID) {
            this.benificiaryRegdID = benificiaryRegdID;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getBenificiaryRegdNo() {
            return benificiaryRegdNo;
        }

        public void setBenificiaryRegdNo(String benificiaryRegdNo) {
            this.benificiaryRegdNo = benificiaryRegdNo;
        }

    }
}
