package com.myhindlab.abkat.models;



import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DistrictNewModel {
    private String DISTLGDCODE;

    private String DISTNAME;
    private String District;
    private String STATELGDCODE;

    public String getDistrict() {
        return District;
    }

    public void setDistrict(String district) {
        District = district;
    }

    private String STATENAME;
    private String LOCALDISTNAME;

    public String getSTATELGDCODE() {
        return STATELGDCODE;
    }

    public void setSTATELGDCODE(String STATELGDCODE) {
        this.STATELGDCODE = STATELGDCODE;
    }

    public String getSTATENAME() {
        return STATENAME;
    }

    public void setSTATENAME(String STATENAME) {
        this.STATENAME = STATENAME;
    }

    public String getLOCALDISTNAME() {
        return LOCALDISTNAME;
    }

    public void setLOCALDISTNAME(String LOCALDISTNAME) {
        this.LOCALDISTNAME = LOCALDISTNAME;
    }



    public DistrictNewModel(String DISTLGDCODE, String District) {
        this.DISTLGDCODE = DISTLGDCODE;
        this.District = District;
    }

    public String getDISTLGDCODE() {
        return DISTLGDCODE;
    }

    public void setDISTLGDCODE(String DISTLGDCODE) {
        this.DISTLGDCODE = DISTLGDCODE;
    }

    public String getDISTNAME() {
        return DISTNAME;
    }

    public void setDISTNAME(String DISTNAME) {
        this.DISTNAME = DISTNAME;
    }
}