package com.myhindlab.abkat.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.processing.Generated;

@Generated("jsonschema2pojo")
public class GetDependentListModel {

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

        @SerializedName("Bocw_idDepend")
        @Expose
        private String Bocw_idDepend;
        @SerializedName("BOCWRegNO")
        @Expose
        private String bOCWRegNO;
        @SerializedName("DOB")
        @Expose
        private String dOB;
        @SerializedName("relation")
        @Expose
        private String relation;
        @SerializedName("family_id")
        @Expose
        private String familyid;


        @SerializedName("full_name")
        @Expose
        private String fullname;
        @SerializedName("RelId")
        @Expose
        private String relId;


        @SerializedName("GPLGDCODE")
        @Expose
        private String gPLGDCODE;


        @SerializedName("RelName")
        @Expose
        private String relName;


        @SerializedName("EnglishName")
        @Expose
        private String EnglishName;

        public String getRelName() {
            return relName;
        }

        public void setRelName(String relName) {
            this.relName = relName;
        }

        public String getEnglishName() {
            return EnglishName;
        }

        public void setEnglishName(String englishName) {
            EnglishName = englishName;
        }

        @SerializedName("GPNAME")
        @Expose
        private String gPNAME;



        @SerializedName("IsUrban")
        @Expose
        private String isUrban;

        public String getgPLGDCODE() {
            return gPLGDCODE;
        }

        public void setgPLGDCODE(String gPLGDCODE) {
            this.gPLGDCODE = gPLGDCODE;
        }

        public String getgPNAME() {
            return gPNAME;
        }

        public void setgPNAME(String gPNAME) {
            this.gPNAME = gPNAME;
        }

        public String getIsUrban() {
            return isUrban;
        }

        public void setIsUrban(String isUrban) {
            this.isUrban = isUrban;
        }

        public String getGender() {
            return Gender;
        }

        public void setGender(String gender) {
            Gender = gender;
        }

        public String getBocw_idDepend() {
            return Bocw_idDepend;
        }

        public void setBocw_idDepend(String bocw_idDepend) {
            Bocw_idDepend = bocw_idDepend;
        }

        public String getbOCWRegNO() {
            return bOCWRegNO;
        }

        public void setbOCWRegNO(String bOCWRegNO) {
            this.bOCWRegNO = bOCWRegNO;
        }

        public String getdOB() {
            return dOB;
        }

        public void setdOB(String dOB) {
            this.dOB = dOB;
        }

        public String getRelation() {
            return relation;
        }

        public void setRelation(String relation) {
            this.relation = relation;
        }

        public String getFamilyid() {
            return familyid;
        }

        public void setFamilyid(String familyid) {
            this.familyid = familyid;
        }

        public String getFullname() {
            return fullname;
        }

        public void setFullname(String fullname) {
            this.fullname = fullname;
        }

        public String getRelId() {
            return relId;
        }

        public void setRelId(String relId) {
            this.relId = relId;
        }

        public String getuID() {
            return uID;
        }

        public void setuID(String uID) {
            this.uID = uID;
        }

        @SerializedName("Gender")
        @Expose
        private String Gender;

        @SerializedName("UID")
        @Expose
        private String uID;



    }

}