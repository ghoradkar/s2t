package com.myhindlab.abkat.pojos;

public class GetAttandanceReportUserwiseOutPut_Pojo {

    private String MOBNO;

    private String DesgName;

    private String DISTLGDCODE;

    private String FullName;

    private String USERID;

    private String ATTENDANCE;

    private String DESGID;

    private String DISTNAME;

    public String getMOBNO ()
    {
        return MOBNO;
    }

    public void setMOBNO (String MOBNO)
    {
        this.MOBNO = MOBNO;
    }

    public String getDesgName ()
    {
        return DesgName;
    }

    public void setDesgName (String DesgName)
    {
        this.DesgName = DesgName;
    }

    public String getDISTLGDCODE ()
    {
        return DISTLGDCODE;
    }

    public void setDISTLGDCODE (String DISTLGDCODE)
    {
        this.DISTLGDCODE = DISTLGDCODE;
    }

    public String getFullName ()
    {
        return FullName;
    }

    public void setFullName (String FullName)
    {
        this.FullName = FullName;
    }

    public String getUSERID ()
    {
        return USERID;
    }

    public void setUSERID (String USERID)
    {
        this.USERID = USERID;
    }

    public String getATTENDANCE ()
    {
        return ATTENDANCE;
    }

    public void setATTENDANCE (String ATTENDANCE)
    {
        this.ATTENDANCE = ATTENDANCE;
    }

    public String getDESGID ()
    {
        return DESGID;
    }

    public void setDESGID (String DESGID)
    {
        this.DESGID = DESGID;
    }

    public String getDISTNAME ()
    {
        return DISTNAME;
    }

    public void setDISTNAME (String DISTNAME)
    {
        this.DISTNAME = DISTNAME;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [MOBNO = "+MOBNO+", DesgName = "+DesgName+", DISTLGDCODE = "+DISTLGDCODE+", FullName = "+FullName+", USERID = "+USERID+", ATTENDANCE = "+ATTENDANCE+", DESGID = "+DESGID+", DISTNAME = "+DISTNAME+"]";
    }
}
