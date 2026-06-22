package com.myhindlab.abkat.models;

public class DistrictList_Model {
    private String DISTLGDCODE;

    private String DISTNAME;
    private String District;
    private String STATELGDCODE;

    private boolean isChecked;
    private Integer AppointmentDateCount;



    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public Integer getAppointmentDateCount() {
        return AppointmentDateCount;
    }

    public void setAppointmentDateCount(Integer appointmentDateCount) {
        AppointmentDateCount = appointmentDateCount;
    }

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


    public DistrictList_Model() {
    }

    public DistrictList_Model(String DISTLGDCODE, String DISTNAME) {
        this.DISTLGDCODE = DISTLGDCODE;
        this.DISTNAME = DISTNAME;
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
