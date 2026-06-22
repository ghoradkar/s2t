package com.myhindlab.abkat.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PatientStatusModel implements Serializable {
    private String CampId;

    private String Ackowledgement;

    private String RegdId;

    private String PhysicalExamination;

    private String PPSampleCollection;

    private String PatientName;

    private String LungFunctioinTest;

    private String VisionScreening;

    private String AudioScreeningTest;

    private String Registration;

    private String Barcode;

    private String BasicDetails;

    private String UrineSampleCollection;

    private String RegdNo;

    public String getIsApproved() {
        return IsApproved;
    }

    public void setIsApproved(String isApproved) {
        IsApproved = isApproved;
    }

    private String IsApproved;

    @SerializedName("Breast Screening")
    private String BreastScreening;

    private String Gender;
    private String RTPCR;
    private String Antigen;

    public String getRTPCR() {
        return RTPCR;
    }

    public void setRTPCR(String RTPCR) {
        this.RTPCR = RTPCR;
    }

    public String getAntigen() {
        return Antigen;
    }

    public void setAntigen(String antigen) {
        Antigen = antigen;
    }

    public String getCampId ()
    {
        return CampId;
    }

    public void setCampId (String CampId)
    {
        this.CampId = CampId;
    }

    public String getAckowledgement ()
    {
        return Ackowledgement;
    }

    public void setAckowledgement (String Ackowledgement)
    {
        this.Ackowledgement = Ackowledgement;
    }

    public String getRegdId ()
    {
        return RegdId;
    }

    public void setRegdId (String RegdId)
    {
        this.RegdId = RegdId;
    }

    public String getPhysicalExamination ()
    {
        return PhysicalExamination;
    }

    public void setPhysicalExamination (String PhysicalExamination)
    {
        this.PhysicalExamination = PhysicalExamination;
    }

    public String getPPSampleCollection ()
    {
        return PPSampleCollection;
    }

    public void setPPSampleCollection (String PPSampleCollection)
    {
        this.PPSampleCollection = PPSampleCollection;
    }

    public String getPatientName ()
    {
        return PatientName;
    }

    public void setPatientName (String PatientName)
    {
        this.PatientName = PatientName;
    }

    public String getLungFunctioinTest ()
    {
        return LungFunctioinTest;
    }

    public void setLungFunctioinTest (String LungFunctioinTest)
    {
        this.LungFunctioinTest = LungFunctioinTest;
    }

    public String getVisionScreening ()
    {
        return VisionScreening;
    }

    public void setVisionScreening (String VisionScreening)
    {
        this.VisionScreening = VisionScreening;
    }

    public String getAudioScreeningTest ()
    {
        return AudioScreeningTest;
    }

    public void setAudioScreeningTest (String AudioScreeningTest)
    {
        this.AudioScreeningTest = AudioScreeningTest;
    }

    public String getRegistration ()
    {
        return Registration;
    }

    public void setRegistration (String Registration)
    {
        this.Registration = Registration;
    }

    public String getBarcode ()
    {
        return Barcode;
    }

    public void setBarcode (String Barcode)
    {
        this.Barcode = Barcode;
    }

    public String getBasicDetails ()
    {
        return BasicDetails;
    }

    public void setBasicDetails (String BasicDetails)
    {
        this.BasicDetails = BasicDetails;
    }

    public String getUrineSampleCollection ()
    {
        return UrineSampleCollection;
    }

    public void setUrineSampleCollection (String UrineSampleCollection)
    {
        this.UrineSampleCollection = UrineSampleCollection;
    }

    public String getRegdNo() {
        return RegdNo;
    }

    public void setRegdNo(String regdNo) {
        RegdNo = regdNo;
    }

    public String getBreastScreening() {
        return BreastScreening;
    }

    public void setBreastScreening(String breastScreening) {
        BreastScreening = breastScreening;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }
}
