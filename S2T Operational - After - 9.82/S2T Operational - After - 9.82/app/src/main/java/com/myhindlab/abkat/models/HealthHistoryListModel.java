package com.myhindlab.abkat.models;

public class HealthHistoryListModel {

    private String CreatedBy;

    private String DiseaseName;

    private String IsSince;

    private String CreatedOn;

    private String Diseaseid;

    public String getCreatedBy ()
    {
        return CreatedBy;
    }

    public void setCreatedBy (String CreatedBy)
    {
        this.CreatedBy = CreatedBy;
    }

    public String getDiseaseName ()
    {
        if(DiseaseName==null)
            return "";
        else
        return DiseaseName;
    }

    public void setDiseaseName (String DiseaseName)
    {
        this.DiseaseName = DiseaseName;
    }

    public String getIsSince ()
    {

        if(IsSince==null)
            return "";
        else
        return IsSince;
    }

    public void setIsSince (String IsSince)
    {
        this.IsSince = IsSince;
    }

    public String getCreatedOn ()
    {
        return CreatedOn;
    }

    public void setCreatedOn (String CreatedOn)
    {
        this.CreatedOn = CreatedOn;
    }

    public String getDiseaseid ()
    {
        return Diseaseid;
    }

    public void setDiseaseid (String Diseaseid)
    {
        this.Diseaseid = Diseaseid;
    }
}
