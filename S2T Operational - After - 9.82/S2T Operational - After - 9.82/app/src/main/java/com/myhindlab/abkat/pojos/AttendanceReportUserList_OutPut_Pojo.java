package com.myhindlab.abkat.pojos;

public class AttendanceReportUserList_OutPut_Pojo {
    private String MOBNO;

    private String DesgShortCode;

    private String Atime;

    private String OnBoardUsersCount;

    private String DISTNAME;

    private String WebAttendance;

    private String TotalAttendance;

    private String DesgName;

    private String DISTLGDCODE;

    private String MobileAttendance;

    private String USERID;

    private String fullname;

    private String DesgId;

    private String NotMarked;

    public String getMOBNO ()
    {
        return MOBNO;
    }

    public void setMOBNO (String MOBNO)
    {
        this.MOBNO = MOBNO;
    }

    public String getDesgShortCode ()
    {
        return DesgShortCode;
    }

    public void setDesgShortCode (String DesgShortCode)
    {
        this.DesgShortCode = DesgShortCode;
    }

    public String getAtime ()
    {
        return Atime;
    }

    public void setAtime (String Atime)
    {
        this.Atime = Atime;
    }

    public String getOnBoardUsersCount ()
    {
        return OnBoardUsersCount;
    }

    public void setOnBoardUsersCount (String OnBoardUsersCount)
    {
        this.OnBoardUsersCount = OnBoardUsersCount;
    }

    public String getDISTNAME ()
    {
        return DISTNAME;
    }

    public void setDISTNAME (String DISTNAME)
    {
        this.DISTNAME = DISTNAME;
    }

    public String getWebAttendance ()
    {
        return WebAttendance;
    }

    public void setWebAttendance (String WebAttendance)
    {
        this.WebAttendance = WebAttendance;
    }

    public String getTotalAttendance ()
    {
        return TotalAttendance;
    }

    public void setTotalAttendance (String TotalAttendance)
    {
        this.TotalAttendance = TotalAttendance;
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

    public String getMobileAttendance ()
    {
        return MobileAttendance;
    }

    public void setMobileAttendance (String MobileAttendance)
    {
        this.MobileAttendance = MobileAttendance;
    }

    public String getUSERID ()
    {
        return USERID;
    }

    public void setUSERID (String USERID)
    {
        this.USERID = USERID;
    }

    public String getFullname ()
    {
        return fullname;
    }

    public void setFullname (String fullname)
    {
        this.fullname = fullname;
    }

    public String getDesgId ()
    {
        return DesgId;
    }

    public void setDesgId (String DesgId)
    {
        this.DesgId = DesgId;
    }

    public String getNotMarked ()
    {
        return NotMarked;
    }

    public void setNotMarked (String NotMarked)
    {
        this.NotMarked = NotMarked;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [MOBNO = "+MOBNO+", DesgShortCode = "+DesgShortCode+", Atime = "+Atime+", OnBoardUsersCount = "+OnBoardUsersCount+", DISTNAME = "+DISTNAME+", WebAttendance = "+WebAttendance+", TotalAttendance = "+TotalAttendance+", DesgName = "+DesgName+", DISTLGDCODE = "+DISTLGDCODE+", MobileAttendance = "+MobileAttendance+", USERID = "+USERID+", fullname = "+fullname+", DesgId = "+DesgId+", NotMarked = "+NotMarked+"]";
    }
}
