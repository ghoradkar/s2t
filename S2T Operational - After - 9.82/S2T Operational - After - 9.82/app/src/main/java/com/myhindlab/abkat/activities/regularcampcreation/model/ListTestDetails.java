package com.myhindlab.abkat.activities.regularcampcreation.model;

public class ListTestDetails {

    private int subServiceId;
    private int amount;
    private String subServiceName;
    private int sampleTypeId;
    private String barCode;
    private int quantity;
    private String templateWise;

    public ListTestDetails(int subServiceId, int amount, String subServiceName,
                           int sampleTypeId, String barCode, int quantity, String templateWise) {
        this.subServiceId = subServiceId;
        this.amount = amount;
        this.subServiceName = subServiceName;
        this.sampleTypeId = sampleTypeId;
        this.barCode = barCode;
        this.quantity = quantity;
        this.templateWise = templateWise;
    }
}