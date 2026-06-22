package com.myhindlab.abkat.pojos;

public class Lab_OutPut_Pojo {
    private String LabName;

    private String LabCode;

    private String IsHubLab;

    public String getIsHubLab() {
        return IsHubLab;
    }

    public void setIsHubLab(String isHubLab) {
        IsHubLab = isHubLab;
    }

    public String getLabName ()
    {
        return LabName;
    }

    public void setLabName (String LabName)
    {
        this.LabName = LabName;
    }

    public String getLabCode ()
    {
        return LabCode;
    }

    public void setLabCode (String LabCode)
    {
        this.LabCode = LabCode;
    }
}
