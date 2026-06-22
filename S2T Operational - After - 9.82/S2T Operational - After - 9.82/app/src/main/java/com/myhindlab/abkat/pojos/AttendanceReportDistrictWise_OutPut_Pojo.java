package com.myhindlab.abkat.pojos;

public class AttendanceReportDistrictWise_OutPut_Pojo {
    private String TotalAttendance;

    private String DISTLGDCODE;

    private String OnBoardUsersCount;

    private String MobileAttendance;

    private String DISTNAME;

    private String WebAttendance;

    private String NotMarked;

    public String getTotalAttendance ()
    {
        return TotalAttendance;
    }

    public void setTotalAttendance (String TotalAttendance)
    {
        this.TotalAttendance = TotalAttendance;
    }

    public String getDISTLGDCODE ()
    {
        return DISTLGDCODE;
    }

    public void setDISTLGDCODE (String DISTLGDCODE)
    {
        this.DISTLGDCODE = DISTLGDCODE;
    }

    public String getOnBoardUsersCount ()
    {
        return OnBoardUsersCount;
    }

    public void setOnBoardUsersCount (String OnBoardUsersCount)
    {
        this.OnBoardUsersCount = OnBoardUsersCount;
    }

    public String getMobileAttendance ()
    {
        return MobileAttendance;
    }

    public void setMobileAttendance (String MobileAttendance)
    {
        this.MobileAttendance = MobileAttendance;
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
        return "ClassPojo [TotalAttendance = "+TotalAttendance+", DISTLGDCODE = "+DISTLGDCODE+", OnBoardUsersCount = "+OnBoardUsersCount+", MobileAttendance = "+MobileAttendance+", DISTNAME = "+DISTNAME+", WebAttendance = "+WebAttendance+", NotMarked = "+NotMarked+"]";
    }
}
