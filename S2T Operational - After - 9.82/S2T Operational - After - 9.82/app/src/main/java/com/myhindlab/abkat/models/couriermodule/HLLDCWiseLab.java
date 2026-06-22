package com.myhindlab.abkat.models.couriermodule;

public class HLLDCWiseLab {

    private String Userid;

    private String DISTNAME;

    private String LabCode;

    private String LabName;

    private String DISTLGDCODE;

    public HLLDCWiseLab(String labCode, String labName) {
        LabCode = labCode;
        LabName = labName;
    }

    public HLLDCWiseLab() {
    }

    public String getUserid ()
    {
        return Userid;
    }

    public void setUserid (String Userid)
    {
        this.Userid = Userid;
    }

    public String getDISTNAME ()
    {
        return DISTNAME;
    }

    public void setDISTNAME (String DISTNAME)
    {
        this.DISTNAME = DISTNAME;
    }

    public String getLabCode ()
    {
        return LabCode;
    }

    public void setLabCode (String LabCode)
    {
        this.LabCode = LabCode;
    }

    public String getLabName ()
    {
        return LabName;
    }

    public void setLabName (String LabName)
    {
        this.LabName = LabName;
    }

    public String getDISTLGDCODE ()
    {
        return DISTLGDCODE;
    }

    public void setDISTLGDCODE (String DISTLGDCODE)
    {
        this.DISTLGDCODE = DISTLGDCODE;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Userid = "+Userid+", DISTNAME = "+DISTNAME+", LabCode = "+LabCode+", LabName = "+LabName+", DISTLGDCODE = "+DISTLGDCODE+"]";
    }
}
