package com.myhindlab.abkat.models.doortodoor;



import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LungFunctionScreeningTestModel {

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

        @SerializedName("patientname")
        @Expose
        private String patientname;
        @SerializedName("RegdNo")
        @Expose
        private Long regdNo;
        @SerializedName("Age")
        @Expose
        private Integer age;
        @SerializedName("DOB")
        @Expose
        private String dob;
        @SerializedName("MobileNo")
        @Expose
        private String mobileNo;
        @SerializedName("BloodGroup")
        @Expose
        private String bloodGroup;
        @SerializedName("Height_CMs")
        @Expose
        private Integer heightCMs;
        @SerializedName("Weight_KGs")
        @Expose
        private Integer weightKGs;
        @SerializedName("BloodPressure")
        @Expose
        private String bloodPressure;
        @SerializedName("Gender")
        @Expose
        private String gender;
        @SerializedName("Smoking")
        @Expose
        private String smoking;
        @SerializedName("SpiroId")
        @Expose
        private Integer spiroId;
        @SerializedName("FEV1")
        @Expose
        private Double fev1;
        @SerializedName("FEVI_FVC")
        @Expose
        private Double feviFvc;
        @SerializedName("PEF")
        @Expose
        private Double pef;
        @SerializedName("FEF_25_75")
        @Expose
        private Double fef2575;
        @SerializedName("FIVC")
        @Expose
        private String fivc;
        @SerializedName("PIF")
        @Expose
        private String pif;
        @SerializedName("FET")
        @Expose
        private Double fet;
        @SerializedName("Result")
        @Expose
        private String result;
        @SerializedName("Resultfooter")
        @Expose
        private String resultfooter;
        @SerializedName("CreatedBy")
        @Expose
        private Integer createdBy;
        @SerializedName("Technician")
        @Expose
        private String technician;
        @SerializedName("TestDate")
        @Expose
        private String testDate;
        @SerializedName("FCV")
        @Expose
        private Double fcv;
        @SerializedName("DeviceId")
        @Expose
        private String deviceId;

        public String getPatientname() {
            return patientname;
        }

        public void setPatientname(String patientname) {
            this.patientname = patientname;
        }

        public Long getRegdNo() {
            return regdNo;
        }

        public void setRegdNo(Long regdNo) {
            this.regdNo = regdNo;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

        public String getMobileNo() {
            return mobileNo;
        }

        public void setMobileNo(String mobileNo) {
            this.mobileNo = mobileNo;
        }

        public String getBloodGroup() {
            return bloodGroup;
        }

        public void setBloodGroup(String bloodGroup) {
            this.bloodGroup = bloodGroup;
        }

        public Integer getHeightCMs() {
            return heightCMs;
        }

        public void setHeightCMs(Integer heightCMs) {
            this.heightCMs = heightCMs;
        }

        public Integer getWeightKGs() {
            return weightKGs;
        }

        public void setWeightKGs(Integer weightKGs) {
            this.weightKGs = weightKGs;
        }

        public String getBloodPressure() {
            return bloodPressure;
        }

        public void setBloodPressure(String bloodPressure) {
            this.bloodPressure = bloodPressure;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getSmoking() {
            return smoking;
        }

        public void setSmoking(String smoking) {
            this.smoking = smoking;
        }

        public Integer getSpiroId() {
            return spiroId;
        }

        public void setSpiroId(Integer spiroId) {
            this.spiroId = spiroId;
        }

        public Double getFev1() {
            return fev1;
        }

        public void setFev1(Double fev1) {
            this.fev1 = fev1;
        }

        public Double getFeviFvc() {
            return feviFvc;
        }

        public void setFeviFvc(Double feviFvc) {
            this.feviFvc = feviFvc;
        }

        public Double getPef() {
            return pef;
        }

        public void setPef(Double pef) {
            this.pef = pef;
        }

        public Double getFef2575() {
            return fef2575;
        }

        public void setFef2575(Double fef2575) {
            this.fef2575 = fef2575;
        }

        public String getFivc() {
            return fivc;
        }

        public void setFivc(String fivc) {
            this.fivc = fivc;
        }

        public String getPif() {
            return pif;
        }

        public void setPif(String pif) {
            this.pif = pif;
        }

        public Double getFet() {
            return fet;
        }

        public void setFet(Double fet) {
            this.fet = fet;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public String getResultfooter() {
            return resultfooter;
        }

        public void setResultfooter(String resultfooter) {
            this.resultfooter = resultfooter;
        }

        public Integer getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(Integer createdBy) {
            this.createdBy = createdBy;
        }

        public String getTechnician() {
            return technician;
        }

        public void setTechnician(String technician) {
            this.technician = technician;
        }

        public String getTestDate() {
            return testDate;
        }

        public void setTestDate(String testDate) {
            this.testDate = testDate;
        }

        public Double getFcv() {
            return fcv;
        }

        public void setFcv(Double fcv) {
            this.fcv = fcv;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

    }
}