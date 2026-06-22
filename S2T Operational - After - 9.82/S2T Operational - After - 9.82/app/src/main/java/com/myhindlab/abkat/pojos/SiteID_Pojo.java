package com.myhindlab.abkat.pojos;

public class SiteID_Pojo {
    String SITELOCATION;

    String SiteDetailId;

    public String getSITELOCATION() {
        return SITELOCATION;
    }

    public void setSITELOCATION(String SITELOCATION) {
        this.SITELOCATION = SITELOCATION;
    }


    public String getSiteDetailId() {
        return SiteDetailId;
    }

    public void setSiteDetailId(String siteDetailId) {
        SiteDetailId = siteDetailId;
    }

    @Override
    public String toString()
    {
        return getSITELOCATION();
    }
}
