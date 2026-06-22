package com.myhindlab.abkat.pojos;

import java.io.Serializable;

public class SiteSurveyRequestList_OutPut_Pojo implements Serializable {
    private String Status;

    private String REGISTERWORKERS;

    private String CampId;

    private String Description;

    private String SiteName;

    private String UNREGISTEREDWORKERS;

    private String Latitude;

    private String DESGID;

    private String SITELOCATION;

    private String DESGNAME;

    private String Longitude;

    private String PinCode;

    private String SiteTypeId;

    private String SURVEYORID;

    private String SURVEYORNAME;

    private String CampName;

    private String SITESURVEYDATE;

    private String SiteTypeName;

    private String CreatedDate;

    private String CampNo;

    private String FinalStatus;

    private String SiteDetailId;

    private boolean isChecked;

    public String getStatus ()
    {
        return Status;
    }

    public void setStatus (String Status)
    {
        this.Status = Status;
    }

    public String getREGISTERWORKERS ()
    {
        return REGISTERWORKERS;
    }

    public void setREGISTERWORKERS (String REGISTERWORKERS)
    {
        this.REGISTERWORKERS = REGISTERWORKERS;
    }

    public String getCampId ()
    {
        return CampId;
    }

    public void setCampId (String CampId)
    {
        this.CampId = CampId;
    }

    public String getDescription ()
    {
        return Description;
    }

    public void setDescription (String Description)
    {
        this.Description = Description;
    }

    public String getSiteName ()
    {
        return SiteName;
    }

    public void setSiteName (String SiteName)
    {
        this.SiteName = SiteName;
    }

    public String getUNREGISTEREDWORKERS ()
    {
        return UNREGISTEREDWORKERS;
    }

    public void setUNREGISTEREDWORKERS (String UNREGISTEREDWORKERS)
    {
        this.UNREGISTEREDWORKERS = UNREGISTEREDWORKERS;
    }

    public String getLatitude ()
    {
        return Latitude;
    }

    public void setLatitude (String Latitude)
    {
        this.Latitude = Latitude;
    }

    public String getDESGID ()
    {
        return DESGID;
    }

    public void setDESGID (String DESGID)
    {
        this.DESGID = DESGID;
    }

    public String getSITELOCATION ()
    {
        return SITELOCATION;
    }

    public void setSITELOCATION (String SITELOCATION)
    {
        this.SITELOCATION = SITELOCATION;
    }

    public String getDESGNAME ()
    {
        return DESGNAME;
    }

    public void setDESGNAME (String DESGNAME)
    {
        this.DESGNAME = DESGNAME;
    }

    public String getLongitude ()
    {
        return Longitude;
    }

    public void setLongitude (String Longitude)
    {
        this.Longitude = Longitude;
    }

    public String getPinCode ()
    {
        return PinCode;
    }

    public void setPinCode (String PinCode)
    {
        this.PinCode = PinCode;
    }

    public String getSiteTypeId ()
    {
        return SiteTypeId;
    }

    public void setSiteTypeId (String SiteTypeId)
    {
        this.SiteTypeId = SiteTypeId;
    }

    public String getSURVEYORID ()
    {
        return SURVEYORID;
    }

    public void setSURVEYORID (String SURVEYORID)
    {
        this.SURVEYORID = SURVEYORID;
    }

    public String getSURVEYORNAME ()
    {
        return SURVEYORNAME;
    }

    public void setSURVEYORNAME (String SURVEYORNAME)
    {
        this.SURVEYORNAME = SURVEYORNAME;
    }

    public String getCampName ()
    {
        return CampName;
    }

    public void setCampName (String CampName)
    {
        this.CampName = CampName;
    }

    public String getSITESURVEYDATE ()
    {
        return SITESURVEYDATE;
    }

    public void setSITESURVEYDATE (String SITESURVEYDATE)
    {
        this.SITESURVEYDATE = SITESURVEYDATE;
    }

    public String getSiteTypeName ()
    {
        return SiteTypeName;
    }

    public void setSiteTypeName (String SiteTypeName)
    {
        this.SiteTypeName = SiteTypeName;
    }

    public String getCreatedDate ()
    {
        return CreatedDate;
    }

    public void setCreatedDate (String CreatedDate)
    {
        this.CreatedDate = CreatedDate;
    }

    public String getCampNo ()
    {
        return CampNo;
    }

    public void setCampNo (String CampNo)
    {
        this.CampNo = CampNo;
    }

    public String getFinalStatus ()
    {
        return FinalStatus;
    }

    public void setFinalStatus (String FinalStatus)
    {
        this.FinalStatus = FinalStatus;
    }

    public String getSiteDetailId ()
    {
        return SiteDetailId;
    }

    public void setSiteDetailId (String SiteDetailId)
    {
        this.SiteDetailId = SiteDetailId;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public String toString()
    {
        return getSITELOCATION();
    }
}
