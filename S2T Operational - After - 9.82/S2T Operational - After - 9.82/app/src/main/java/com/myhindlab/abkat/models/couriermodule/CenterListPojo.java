package com.myhindlab.abkat.models.couriermodule;

/**
 * Created by shrik on 30-08-2017.
 */

public class CenterListPojo {

    private String centername;

    private String centerId;

    private String status;

    public String getCenterName() {
        return centername;
    }

    public void setCenterName(String centerName) {
        this.centername = centerName;
    }

    public String getCenterId() {
        return centerId;
    }

    public void setCenterId(String centerId) {
        this.centerId = centerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return getCenterName();
    }
}
