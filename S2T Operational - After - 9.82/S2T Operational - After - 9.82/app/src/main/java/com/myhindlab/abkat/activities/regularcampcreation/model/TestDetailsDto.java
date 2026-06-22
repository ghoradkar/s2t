package com.myhindlab.abkat.activities.regularcampcreation.model;

public class TestDetailsDto {

    private int subServiceId;
    private String subServiceCode;
    private String subServiceName;
    private int profileId;
    private double b2cCharges;
    private double b2bCharges;
    private String sampleTypeName;
    private int sampleTypeId;
    private String templateWise;
    private Integer serviceId; // nullable

    // Getters & Setters

    public int getSubServiceId() {
        return subServiceId;
    }

    public void setSubServiceId(int subServiceId) {
        this.subServiceId = subServiceId;
    }

    public String getSubServiceCode() {
        return subServiceCode;
    }

    public void setSubServiceCode(String subServiceCode) {
        this.subServiceCode = subServiceCode;
    }

    public String getSubServiceName() {
        return subServiceName;
    }

    public void setSubServiceName(String subServiceName) {
        this.subServiceName = subServiceName;
    }

    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }

    public double getB2cCharges() {
        return b2cCharges;
    }

    public void setB2cCharges(double b2cCharges) {
        this.b2cCharges = b2cCharges;
    }

    public double getB2bCharges() {
        return b2bCharges;
    }

    public void setB2bCharges(double b2bCharges) {
        this.b2bCharges = b2bCharges;
    }

    public String getSampleTypeName() {
        return sampleTypeName;
    }

    public void setSampleTypeName(String sampleTypeName) {
        this.sampleTypeName = sampleTypeName;
    }

    public int getSampleTypeId() {
        return sampleTypeId;
    }

    public void setSampleTypeId(int sampleTypeId) {
        this.sampleTypeId = sampleTypeId;
    }

    public String getTemplateWise() {
        return templateWise;
    }

    public void setTemplateWise(String templateWise) {
        this.templateWise = templateWise;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }
}