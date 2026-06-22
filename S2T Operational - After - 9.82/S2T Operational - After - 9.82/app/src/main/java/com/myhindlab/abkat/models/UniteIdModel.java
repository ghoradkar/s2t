package com.myhindlab.abkat.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class UniteIdModel {

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

        @SerializedName("ID")
        @Expose
        private Integer id;
        @SerializedName("DISTLGDCODE")
        @Expose
        private Integer distlgdcode;
        @SerializedName("DISTNAME")
        @Expose
        private String distname;
        @SerializedName("DistrictID")
        @Expose
        private Integer districtID;
        @SerializedName("UnitID")
        @Expose
        private Integer unitID;
        @SerializedName("UnitName")
        @Expose
        private String unitName;
        @SerializedName("UnitCode")
        @Expose
        private String unitCode;
        @SerializedName("CREATEDBY")
        @Expose
        private Integer createdby;
        @SerializedName("CREATEDON")
        @Expose
        private String createdon;
        @SerializedName("ISACTIVE")
        @Expose
        private Integer isactive;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getDistlgdcode() {
            return distlgdcode;
        }

        public void setDistlgdcode(Integer distlgdcode) {
            this.distlgdcode = distlgdcode;
        }

        public String getDistname() {
            return distname;
        }

        public void setDistname(String distname) {
            this.distname = distname;
        }

        public Integer getDistrictID() {
            return districtID;
        }

        public void setDistrictID(Integer districtID) {
            this.districtID = districtID;
        }

        public Integer getUnitID() {
            return unitID;
        }

        public void setUnitID(Integer unitID) {
            this.unitID = unitID;
        }

        public String getUnitName() {
            return unitName;
        }

        public void setUnitName(String unitName) {
            this.unitName = unitName;
        }

        public String getUnitCode() {
            return unitCode;
        }

        public void setUnitCode(String unitCode) {
            this.unitCode = unitCode;
        }

        public Integer getCreatedby() {
            return createdby;
        }

        public void setCreatedby(Integer createdby) {
            this.createdby = createdby;
        }

        public String getCreatedon() {
            return createdon;
        }

        public void setCreatedon(String createdon) {
            this.createdon = createdon;
        }

        public Integer getIsactive() {
            return isactive;
        }

        public void setIsactive(Integer isactive) {
            this.isactive = isactive;
        }

    }

}