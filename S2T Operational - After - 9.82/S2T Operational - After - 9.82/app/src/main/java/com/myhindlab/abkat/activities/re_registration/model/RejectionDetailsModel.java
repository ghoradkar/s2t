package com.myhindlab.abkat.activities.re_registration.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


import java.util.List;


public class RejectionDetailsModel {

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


    public class Output {

        @SerializedName("Rej_Regdid")
        @Expose
        private String rejRegdid;
        @SerializedName("Rej_CampID")
        @Expose
        private String rejCampID;
        @SerializedName("Rej_CampDate")
        @Expose
        private String rejCampDate;
        @SerializedName("RegdNo")
        @Expose
        private String regdNo;
        @SerializedName("EnglishName")
        @Expose
        private String englishName;
        @SerializedName("Age")
        @Expose
        private String age;
        @SerializedName("Gender")
        @Expose
        private String gender;
        @SerializedName("IsDependent")
        @Expose
        private String isDependent;
        @SerializedName("ReleationID")
        @Expose
        private String releationID;
        @SerializedName("MobileNo")
        @Expose
        private String mobileNo;

        public String getRejRegdid() {
            return rejRegdid;
        }

        public void setRejRegdid(String rejRegdid) {
            this.rejRegdid = rejRegdid;
        }

        public String getRejCampID() {
            return rejCampID;
        }

        public void setRejCampID(String rejCampID) {
            this.rejCampID = rejCampID;
        }

        public String getRejCampDate() {
            return rejCampDate;
        }

        public void setRejCampDate(String rejCampDate) {
            this.rejCampDate = rejCampDate;
        }

        public String getRegdNo() {
            return regdNo;
        }

        public void setRegdNo(String regdNo) {
            this.regdNo = regdNo;
        }

        public String getEnglishName() {
            return englishName;
        }

        public void setEnglishName(String englishName) {
            this.englishName = englishName;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getIsDependent() {
            return isDependent;
        }

        public void setIsDependent(String isDependent) {
            this.isDependent = isDependent;
        }

        public String getReleationID() {
            return releationID;
        }

        public void setReleationID(String releationID) {
            this.releationID = releationID;
        }

        public String getMobileNo() {
            return mobileNo;
        }

        public void setMobileNo(String mobileNo) {
            this.mobileNo = mobileNo;
        }

    }
}