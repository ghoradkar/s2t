package com.myhindlab.abkat.models;

public class HearingData_New {

    private int frequency;
    private int rightVolume;
    private int lefttVolume;
    private String observation;
    private String patientRegId;
    private String campId;
    private String RightRemark;

    public HearingData_New() {
    }

    public HearingData_New(int frequency, int rightVolume, int lefttVolume) {
        this.frequency = frequency;
        this.rightVolume = rightVolume;
        this.lefttVolume = lefttVolume;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getRightVolume() {
        return rightVolume;
    }

    public void setRightVolume(int rightVolume) {
        this.rightVolume = rightVolume;
    }

    public int getLefttVolume() {
        return lefttVolume;
    }

    public void setLefttVolume(int lefttVolume) {
        this.lefttVolume = lefttVolume;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public String getPatientRegId() {
        return patientRegId;
    }

    public void setPatientRegId(String patientRegId) {
        this.patientRegId = patientRegId;
    }

    public String getCampId() {
        return campId;
    }

    public void setCampId(String campId) {
        this.campId = campId;
    }

    public String getRightRemark() {
        return RightRemark;
    }

    public void setRightRemark(String rightRemark) {
        RightRemark = rightRemark;
    }
}
