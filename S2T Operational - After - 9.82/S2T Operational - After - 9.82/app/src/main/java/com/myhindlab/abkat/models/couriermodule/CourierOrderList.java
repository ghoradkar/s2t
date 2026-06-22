package com.myhindlab.abkat.models.couriermodule;

public class CourierOrderList {
    private String orderId;
    private String testName;
    private String testId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }
}


//{status: "Success",message: "Center List",output: [{CourierNo: 10,Photopath: "Manchar",testList: [{orderId: 10,testName: "Manchar",testId: "Manchar"},{orderId: 10,testName: "Manchar",testId:"Manchar"}]},{CourierNo: 11,Photopath: "Shirpur",testList: [{orderId: 10,testName: "Manchar",testId: "Manchar"},{orderId: 10,testName: "Manchar",testId: "Manchar"}]}]}
