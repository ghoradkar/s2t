package com.myhindlab.abkat.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PresentPatientList_Model implements Serializable {

    @SerializedName("CreatedDate")
    @Expose
    private String createdDate;
    @SerializedName("CampId")
    @Expose
    private Integer campId;
    @SerializedName("SiteId")
    @Expose
    private Integer siteId;
    @SerializedName("Barcode1")
    @Expose
    private Object barcode1;
    @SerializedName("AntiBarcode")
    @Expose
    private String AntiBarcode;
    @SerializedName("RegdId")
    @Expose
    private Integer regdId;
    @SerializedName("EnglishName")
    @Expose
    private String englishName;

    public String getSubOrgId() {
        return subOrgId;
    }

    public void setSubOrgId(String subOrgId) {
        this.subOrgId = subOrgId;
    }

    @SerializedName("SubOrgId")
    @Expose
    private String subOrgId;


    public String getABHANumber() {
        return ABHANumber;
    }

    public void setABHANumber(String ABHANumber) {
        this.ABHANumber = ABHANumber;
    }

    public String getABHAAddress() {
        return ABHAAddress;
    }

    public void setABHAAddress(String ABHAAddress) {
        this.ABHAAddress = ABHAAddress;
    }

    @SerializedName("ABHANumber")
    @Expose
    private String ABHANumber;
    @SerializedName("ABHAAddress")
    @Expose
    private String ABHAAddress;

    @SerializedName("IsPhy")
    @Expose
    private String isPhy;


    @SerializedName("CampTypeID")
    @Expose
    private String campTypeID;

    public String getCampTypeID() {
        return campTypeID;
    }

    public void setCampTypeID(String campTypeID) {
        this.campTypeID = campTypeID;
    }

    public String getIsPhy() {
        return isPhy;
    }

    public void setIsPhy(String isPhy) {
        this.isPhy = isPhy;
    }

    @SerializedName("MobileNo")
    @Expose
    private String mobileNo;
    @SerializedName("DOB")
    @Expose
    private String dob;
    @SerializedName("Height_CMs")
    @Expose
    private double heightCMs;
    @SerializedName("Weight_KGs")
    @Expose
    private double weightKGs;
    @SerializedName("UID")
    @Expose
    private String uid;
    @SerializedName("PermanentAddress")
    @Expose
    private String permanentAddress;
    @SerializedName("LocalAddress")
    @Expose
    private String localAddress;
    @SerializedName("Pincode")
    @Expose
    private String pincode;
    @SerializedName("Gender")
    @Expose
    private String gender;
    @SerializedName("BrId")
    @Expose
    private Object brId;
    @SerializedName("BloodSugar_PP")
    @Expose
    private String bloodSugarPP;
    @SerializedName("IsSignature")
    @Expose
    private Integer isSignature;
    @SerializedName("Age")
    @Expose
    private Integer age;
    @SerializedName("RegdNo")
    @Expose
    private Long regdNo;
    @SerializedName("patientPhoto")
    @Expose
    private String patientPhoto;
    @SerializedName("HealthCardPath")
    @Expose
    private String healthCardPath;
    @SerializedName("IsHCRenewal")
    @Expose
    private String isHCRenewal;
    @SerializedName("HCRenewalFilePath")
    @Expose
    private String hCRenewalFilePath;
    @SerializedName("UserThumbPath")
    @Expose
    private String userThumbPath;
    @SerializedName("AudioImage")
    @Expose
    private String audioImage;
    @SerializedName("ScreeningDoneCnt")
    @Expose
    private Integer screeningDoneCnt;
    @SerializedName("IsRtpcr")
    @Expose
    private Integer isRtpcr;


    @SerializedName("IsDependent")
    @Expose
    private Integer isDependent;


    @SerializedName("IsAckAndRCPending")
    @Expose
    private Integer isAckAndRCPending;

    @SerializedName("CurrentAddress")
    @Expose
    private String currentAddress;


    @SerializedName("Bocw_idDepend")
    @Expose
    private String bocw_idDepend;


    @SerializedName("RationCardNo")
    @Expose
    private String rationCardNo;

    public String getRationCardNo() {
        return rationCardNo;
    }

    public void setRationCardNo(String rationCardNo) {
        this.rationCardNo = rationCardNo;
    }

    public String getBocw_idDepend() {
        return bocw_idDepend;
    }

    public void setBocw_idDepend(String bocw_idDepend) {
        this.bocw_idDepend = bocw_idDepend;
    }

    public Integer getIsAckAndRCPending() {
        return isAckAndRCPending;
    }

    public void setIsAckAndRCPending(Integer isAckAndRCPending) {
        this.isAckAndRCPending = isAckAndRCPending;
    }

    public String getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }

    @SerializedName("ReleationID")
    @Expose
    private Integer releationID;


    @SerializedName("RelName")
    @Expose
    private String relName;



@SerializedName("isDependent")
    @Expose
    private Integer issDependent;

    public Integer getIssDependent() {
        return issDependent;
    }

    public void setIssDependent(Integer issDependent) {
        this.issDependent = issDependent;
    }

    public String getScreeningPatientID() {
        return screeningPatientID;
    }

    public void setScreeningPatientID(String screeningPatientID) {
        this.screeningPatientID = screeningPatientID;
    }

    @SerializedName("ScreeningPatientID")
    @Expose
    private String screeningPatientID;

    public String getIsCall() {
        return isCall;
    }

    public void setIsCall(String isCall) {
        this.isCall = isCall;
    }

    @SerializedName("IsCall")
    @Expose
    private String isCall;


    private String antigenResult;

    public String getAntiBarcode() {
        return AntiBarcode;
    }

    public void setAntiBarcode(String antiBarcode) {
        AntiBarcode = antiBarcode;
    }

    public String gethCRenewalFilePath() {
        return hCRenewalFilePath;
    }

    public void sethCRenewalFilePath(String hCRenewalFilePath) {
        this.hCRenewalFilePath = hCRenewalFilePath;
    }

    public String getAntigenResult() {
        return antigenResult == null ? "" : antigenResult;
    }

    public void setAntigenResult(String antigenResult) {
        this.antigenResult = antigenResult;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getCampId() {
        return campId;
    }

    public void setCampId(Integer campId) {
        this.campId = campId;
    }

    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    public String getBarcode1() {
        return String.valueOf(barcode1);
    }

    public void setBarcode1(Object barcode1) {
        this.barcode1 = barcode1;
    }

    public Integer getRegdId() {
        return regdId;
    }

    public void setRegdId(Integer regdId) {
        this.regdId = regdId;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public double getHeightCMs() {
        return heightCMs;
    }

    public void setHeightCMs(double heightCMs) {
        this.heightCMs = heightCMs;
    }

    public double getWeightKGs() {
        return weightKGs;
    }

    public void setWeightKGs(double weightKGs) {
        this.weightKGs = weightKGs;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public String getLocalAddress() {
        return localAddress;
    }

    public void setLocalAddress(String localAddress) {
        this.localAddress = localAddress;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Object getBrId() {
        return brId;
    }

    public void setBrId(Object brId) {
        this.brId = brId;
    }

    public String getBloodSugarPP() {
        return bloodSugarPP;
    }

    public void setBloodSugarPP(String bloodSugarPP) {
        this.bloodSugarPP = bloodSugarPP;
    }

    public Integer getIsSignature() {
        return isSignature;
    }

    public void setIsSignature(Integer isSignature) {
        this.isSignature = isSignature;
    }

    public String getAge() {
        return String.valueOf(age);
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Long getRegdNo() {
        return regdNo;
    }

    public void setRegdNo(Long regdNo) {
        this.regdNo = regdNo;
    }

    public String getPatientPhoto() {
        return patientPhoto;
    }

    public void setPatientPhoto(String patientPhoto) {
        this.patientPhoto = patientPhoto;
    }

    public String getHealthCardPath() {
        return healthCardPath;
    }

    public void setHealthCardPath(String healthCardPath) {
        this.healthCardPath = healthCardPath;
    }

    public String getIsHCRenewal() {
        return isHCRenewal;
    }

    public void setIsHCRenewal(String isHCRenewal) {
        this.isHCRenewal = isHCRenewal;
    }

    public String getHCRenewalFilePath() {
        return hCRenewalFilePath;
    }

    public void setHCRenewalFilePath(String hCRenewalFilePath) {
        this.hCRenewalFilePath = hCRenewalFilePath;
    }

    public String getUserThumbPath() {
        return userThumbPath;
    }

    public void setUserThumbPath(String userThumbPath) {
        this.userThumbPath = userThumbPath;
    }

    public String getAudioImage() {
        return audioImage;
    }

    public void setAudioImage(String audioImage) {
        this.audioImage = audioImage;
    }

    public Integer getScreeningDoneCnt() {
        return screeningDoneCnt;
    }

    public void setScreeningDoneCnt(Integer screeningDoneCnt) {
        this.screeningDoneCnt = screeningDoneCnt;
    }

    public Integer getIsRtpcr() {
        return isRtpcr;
    }

    public void setIsRtpcr(Integer isRtpcr) {
        this.isRtpcr = isRtpcr;
    }

    public Integer getIsDependent() {
        return isDependent;
    }

    public void setIsDependent(Integer isDependent) {
        this.isDependent = isDependent;
    }

    public Integer getReleationID() {
        return releationID;
    }

    public void setReleationID(Integer releationID) {
        this.releationID = releationID;
    }

    public String getRelName() {
        return relName;
    }

    public void setRelName(String relName) {
        this.relName = relName;
    }

}