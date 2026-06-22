package com.myhindlab.abkat.appointment_confirmation.pojo;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CallingApiVirtualNumberModel {

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

        @SerializedName("OrgKeyID")
        @Expose
        private Integer orgKeyID;
        @SerializedName("OrgId")
        @Expose
        private Integer orgId;
        @SerializedName("UserId")
        @Expose
        private Integer userId;
        @SerializedName("VirtualNo")
        @Expose
        private String virtualNo;
        @SerializedName("APIKey")
        @Expose
        private String aPIKey;




        @SerializedName("CompanyID")
        @Expose
        private String companyID;

        @SerializedName("Public_IVR_ID")
        @Expose
        private String publicId;

        @SerializedName("SecrateToken")
        @Expose
        private String secrateToken;

        @SerializedName("Type")
        @Expose
        private String type;

        @SerializedName("rider_company_id")
        @Expose
        private String riderCompanyId;


        @SerializedName("rider_public_ivr_id")
        @Expose
        private String riderPublicIvrId;

        public String getRiderCompanyId() {
            return riderCompanyId;
        }

        public void setRiderCompanyId(String riderCompanyId) {
            this.riderCompanyId = riderCompanyId;
        }

        public String getRiderPublicIvrId() {
            return riderPublicIvrId;
        }

        public void setRiderPublicIvrId(String riderPublicIvrId) {
            this.riderPublicIvrId = riderPublicIvrId;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCompanyID() {
            return companyID;
        }

        public void setCompanyID(String companyID) {
            this.companyID = companyID;
        }

        public String getPublicId() {
            return publicId;
        }

        public void setPublicId(String publicId) {
            this.publicId = publicId;
        }

        public String getSecrateToken() {
            return secrateToken;
        }

        public void setSecrateToken(String secrateToken) {
            this.secrateToken = secrateToken;
        }

        @SerializedName("Is24By7IsAccountCreated")
        @Expose
        private String is24By7IsAccountCreated;

        public String getIs24By7IsAccountCreated() {
            return is24By7IsAccountCreated;
        }

        public void setIs24By7IsAccountCreated(String is24By7IsAccountCreated) {
            this.is24By7IsAccountCreated = is24By7IsAccountCreated;
        }

        public String getaPIKey() {
            return aPIKey;
        }

        public void setaPIKey(String aPIKey) {
            this.aPIKey = aPIKey;
        }

        public String getServieNumber() {
            return servieNumber;
        }

        public void setServieNumber(String servieNumber) {
            this.servieNumber = servieNumber;
        }

        @SerializedName("ServieNumber")
        @Expose
        private String servieNumber;

        public Integer getOrgKeyID() {
            return orgKeyID;
        }

        public void setOrgKeyID(Integer orgKeyID) {
            this.orgKeyID = orgKeyID;
        }

        public Integer getOrgId() {
            return orgId;
        }

        public void setOrgId(Integer orgId) {
            this.orgId = orgId;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public String getVirtualNo() {
            return virtualNo;
        }

        public void setVirtualNo(String virtualNo) {
            this.virtualNo = virtualNo;
        }

        public String getAPIKey() {
            return aPIKey;
        }

        public void setAPIKey(String aPIKey) {
            this.aPIKey = aPIKey;
        }

    }

}