package com.myhindlab.abkat.models.couriermodule;

public class CourierDetailsPojo {

    private String CourierId;
    private String CourierNumber;
    private String PhotopathSend;
    private String testsCount;

    public String getCourierId() {
        return CourierId;
    }

    public void setCourierId(String courierId) {
        CourierId = courierId;
    }

    public String getCourierNumber() {
        return CourierNumber;
    }

    public void setCourierNumber(String courierNumber) {
        CourierNumber = courierNumber;
    }

    public String getPhotopathSend() {
        return PhotopathSend;
    }

    public void setPhotopathSend(String photopathSend) {
        PhotopathSend = photopathSend;
    }

    public String getTestsCount() {
        return testsCount;
    }

    public void setTestsCount(String testsCount) {
        this.testsCount = testsCount;
    }
}
