package com.myhindlab.abkat.models;

public class TeamList {

    private String USERS_COUNT;

    private String DESGNAME;

    private String DESGID;

    public String getUSERS_COUNT ()
    {
        return USERS_COUNT;
    }

    public void setUSERS_COUNT (String USERS_COUNT)
    {
        this.USERS_COUNT = USERS_COUNT;
    }

    public String getDESGNAME ()
    {
        return DESGNAME;
    }

    public void setDESGNAME (String DESGNAME)
    {
        this.DESGNAME = DESGNAME;
    }

    public String getDESGID ()
    {
        return DESGID;
    }

    public void setDESGID (String DESGID)
    {
        this.DESGID = DESGID;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [USERS_COUNT = "+USERS_COUNT+", DESGNAME = "+DESGNAME+", DESGID = "+DESGID+"]";
    }
}
