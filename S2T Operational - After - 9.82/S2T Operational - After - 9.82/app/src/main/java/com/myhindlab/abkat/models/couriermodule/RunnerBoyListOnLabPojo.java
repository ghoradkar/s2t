package com.myhindlab.abkat.models.couriermodule;

public class RunnerBoyListOnLabPojo {

    private String Name;

    private String MOBNO;

    private String USERNAME;

    private String EMAILID;

    private String USERID;

    public String getName ()
    {
        return Name;
    }

    public void setName (String Name)
    {
        this.Name = Name;
    }

    public String getMOBNO ()
    {
        return MOBNO;
    }

    public void setMOBNO (String MOBNO)
    {
        this.MOBNO = MOBNO;
    }

    public String getUSERNAME ()
    {
        return USERNAME;
    }

    public void setUSERNAME (String USERNAME)
    {
        this.USERNAME = USERNAME;
    }

    public String getEMAILID ()
    {
        return EMAILID;
    }

    public void setEMAILID (String EMAILID)
    {
        this.EMAILID = EMAILID;
    }

    public String getUSERID ()
    {
        return USERID;
    }

    public void setUSERID (String USERID)
    {
        this.USERID = USERID;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Name = "+Name+", MOBNO = "+MOBNO+", USERNAME = "+USERNAME+", EMAILID = "+EMAILID+", USERID = "+USERID+"]";
    }
}
