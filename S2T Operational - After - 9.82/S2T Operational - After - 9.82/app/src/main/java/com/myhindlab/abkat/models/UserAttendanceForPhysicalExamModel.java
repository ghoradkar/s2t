package com.myhindlab.abkat.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class UserAttendanceForPhysicalExamModel implements Serializable{

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("output")
    @Expose
    private List<Output> output;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Output> getOutput() {
        return output;
    }

    public void setOutput(List<Output> output) {
        this.output = output;
    }


    public class Output implements Serializable {

        @SerializedName("CreatedDate")
        @Expose
        private String createdDate;
        @SerializedName("CampId")
        @Expose
        private Integer campId;
        @SerializedName("SiteId")
        @Expose
        private Integer siteId;
        @SerializedName("RegdId")
        @Expose
        private Integer regdId;
        @SerializedName("EnglishName")
        @Expose
        private String englishName;
        @SerializedName("MobileNo")
        @Expose
        private String mobileNo;

        public String getIsCall() {
            return isCall;
        }

        public void setIsCall(String isCall) {
            this.isCall = isCall;
        }

        @SerializedName("IsCall")
        @Expose
        private String isCall;
        @SerializedName("DOB")
        @Expose
        private String dob;
        @SerializedName("Height_CMs")
        @Expose
        private Double heightCMs;
        @SerializedName("Weight_KGs")
        @Expose
        private Double weightKGs;
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
        @SerializedName("AntigenResult")
        @Expose
        private String antigenResult;
        @SerializedName("AntiBarcode")
        @Expose
        private Object antiBarcode;
        @SerializedName("IsDependent")
        @Expose
        private Integer isDependent;
        @SerializedName("ReleationID")
        @Expose
        private Integer releationID;
        @SerializedName("RelName")
        @Expose
        private String relName;
        @SerializedName("regdid1")
        @Expose
        private Object regdid1;
        @SerializedName("DoctorMapStatus")
        @Expose
        private String doctorMapStatus;
        @SerializedName("IsDependentStatus")
        @Expose
        private String isDependentStatus;

        @SerializedName("DoctorName")
        @Expose
        private String doctorName;

        @SerializedName("DoctorMobile")
        @Expose
        private String doctorMobile;

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

        public Integer getRegdId() {
            return regdId;
        }

        public void setRegdId(Integer regdId) {
            this.regdId = regdId;
        }

        public String getDoctorName() {
            return doctorName;
        }

        public void setDoctorName(String doctorName) {
            this.doctorName = doctorName;
        }

        public String getDoctorMobile() {
            return doctorMobile;
        }

        public void setDoctorMobile(String doctorMobile) {
            this.doctorMobile = doctorMobile;
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

        public Double getHeightCMs() {
            return heightCMs;
        }

        public void setHeightCMs(Double heightCMs) {
            this.heightCMs = heightCMs;
        }

        public Double getWeightKGs() {
            return weightKGs;
        }

        public void setWeightKGs(Double weightKGs) {
            this.weightKGs = weightKGs;
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

        public Integer getAge() {
            return age;
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

        public String getAntigenResult() {
            return antigenResult;
        }

        public void setAntigenResult(String antigenResult) {
            this.antigenResult = antigenResult;
        }

        public Object getAntiBarcode() {
            return antiBarcode;
        }

        public void setAntiBarcode(Object antiBarcode) {
            this.antiBarcode = antiBarcode;
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

        public Object getRegdid1() {
            return regdid1;
        }

        public void setRegdid1(Object regdid1) {
            this.regdid1 = regdid1;
        }

        public String getDoctorMapStatus() {
            return doctorMapStatus;
        }

        public void setDoctorMapStatus(String doctorMapStatus) {
            this.doctorMapStatus = doctorMapStatus;
        }

        public String getIsDependentStatus() {
            return isDependentStatus;
        }

        public void setIsDependentStatus(String isDependentStatus) {
            this.isDependentStatus = isDependentStatus;
        }

    }
}