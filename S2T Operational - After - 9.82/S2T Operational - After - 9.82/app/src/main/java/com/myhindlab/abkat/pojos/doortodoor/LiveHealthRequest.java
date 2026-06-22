
package com.myhindlab.abkat.pojos.doortodoor;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LiveHealthRequest {

    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("designation")
    @Expose
    private String designation;
    @SerializedName("fullName")
    @Expose
    private String fullName;
    @SerializedName("age")
    @Expose
    private Integer age;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("area")
    @Expose
    private String area;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("patientType")
    @Expose
    private String patientType;
    @SerializedName("labPatientId")
    @Expose
    private String labPatientId;
    @SerializedName("pincode")
    @Expose
    private String pincode;
    @SerializedName("patientId")
    @Expose
    private String patientId;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("passportNo")
    @Expose
    private String passportNo;
    @SerializedName("panNumber")
    @Expose
    private String panNumber;
    @SerializedName("aadharNumber")
    @Expose
    private String aadharNumber;
    @SerializedName("insuranceNo")
    @Expose
    private String insuranceNo;
    @SerializedName("nationality")
    @Expose
    private String nationality;
    @SerializedName("ethnicity")
    @Expose
    private String ethnicity;
    @SerializedName("nationalIdentityNumber")
    @Expose
    private String nationalIdentityNumber;
    @SerializedName("workerCode")
    @Expose
    private String workerCode;
    @SerializedName("doctorCode")
    @Expose
    private String doctorCode;
    @SerializedName("billDetails")
    @Expose
    private BillDetails billDetails;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPatientType() {
        return patientType;
    }

    public void setPatientType(String patientType) {
        this.patientType = patientType;
    }

    public String getLabPatientId() {
        return labPatientId;
    }

    public void setLabPatientId(String labPatientId) {
        this.labPatientId = labPatientId;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPassportNo() {
        return passportNo;
    }

    public void setPassportNo(String passportNo) {
        this.passportNo = passportNo;
    }

    public String getPanNumber() {
        return panNumber;
    }

    public void setPanNumber(String panNumber) {
        this.panNumber = panNumber;
    }

    public String getAadharNumber() {
        return aadharNumber;
    }

    public void setAadharNumber(String aadharNumber) {
        this.aadharNumber = aadharNumber;
    }

    public String getInsuranceNo() {
        return insuranceNo;
    }

    public void setInsuranceNo(String insuranceNo) {
        this.insuranceNo = insuranceNo;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getEthnicity() {
        return ethnicity;
    }

    public void setEthnicity(String ethnicity) {
        this.ethnicity = ethnicity;
    }

    public String getNationalIdentityNumber() {
        return nationalIdentityNumber;
    }

    public void setNationalIdentityNumber(String nationalIdentityNumber) {
        this.nationalIdentityNumber = nationalIdentityNumber;
    }

    public String getWorkerCode() {
        return workerCode;
    }

    public void setWorkerCode(String workerCode) {
        this.workerCode = workerCode;
    }

    public String getDoctorCode() {
        return doctorCode;
    }

    public void setDoctorCode(String doctorCode) {
        this.doctorCode = doctorCode;
    }

    public BillDetails getBillDetails() {
        return billDetails;
    }

    public void setBillDetails(BillDetails billDetails) {
        this.billDetails = billDetails;
    }


    public static class BillDetails {

        @SerializedName("emergencyFlag")
        @Expose
        private Integer emergencyFlag;
        @SerializedName("totalAmount")
        @Expose
        private String totalAmount;
        @SerializedName("advance")
        @Expose
        private String advance;
        @SerializedName("billDate")
        @Expose
        private String billDate;
        @SerializedName("paymentType")
        @Expose
        private String paymentType;
        @SerializedName("referralName")
        @Expose
        private String referralName;
        @SerializedName("otherReferral")
        @Expose
        private String otherReferral;
        @SerializedName("sampleId")
        @Expose
        private String sampleId;
        @SerializedName("orderNumber")
        @Expose
        private String orderNumber;
        @SerializedName("referralIdLH")
        @Expose
        private Integer referralIdLH;
        @SerializedName("organisationName")
        @Expose
        private String organisationName;
        @SerializedName("additionalAmount")
        @Expose
        private String additionalAmount;
        @SerializedName("organizationIdLH")
        @Expose
        private Integer organizationIdLH;
        @SerializedName("comments")
        @Expose
        private String comments;
        @SerializedName("testList")
        @Expose
        private List<Test> testList = null;
        @SerializedName("paymentList")
        @Expose
        private List<Payment> paymentList = null;

        public Integer getEmergencyFlag() {
            return emergencyFlag;
        }

        public void setEmergencyFlag(Integer emergencyFlag) {
            this.emergencyFlag = emergencyFlag;
        }

        public String getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(String totalAmount) {
            this.totalAmount = totalAmount;
        }

        public String getAdvance() {
            return advance;
        }

        public void setAdvance(String advance) {
            this.advance = advance;
        }

        public String getBillDate() {
            return billDate;
        }

        public void setBillDate(String billDate) {
            this.billDate = billDate;
        }

        public String getPaymentType() {
            return paymentType;
        }

        public void setPaymentType(String paymentType) {
            this.paymentType = paymentType;
        }

        public String getReferralName() {
            return referralName;
        }

        public void setReferralName(String referralName) {
            this.referralName = referralName;
        }

        public String getOtherReferral() {
            return otherReferral;
        }

        public void setOtherReferral(String otherReferral) {
            this.otherReferral = otherReferral;
        }

        public String getSampleId() {
            return sampleId;
        }

        public void setSampleId(String sampleId) {
            this.sampleId = sampleId;
        }

        public String getOrderNumber() {
            return orderNumber;
        }

        public void setOrderNumber(String orderNumber) {
            this.orderNumber = orderNumber;
        }

        public Integer getReferralIdLH() {
            return referralIdLH;
        }

        public void setReferralIdLH(Integer referralIdLH) {
            this.referralIdLH = referralIdLH;
        }

        public String getOrganisationName() {
            return organisationName;
        }

        public void setOrganisationName(String organisationName) {
            this.organisationName = organisationName;
        }

        public String getAdditionalAmount() {
            return additionalAmount;
        }

        public void setAdditionalAmount(String additionalAmount) {
            this.additionalAmount = additionalAmount;
        }

        public Integer getOrganizationIdLH() {
            return organizationIdLH;
        }

        public void setOrganizationIdLH(Integer organizationIdLH) {
            this.organizationIdLH = organizationIdLH;
        }

        public String getComments() {
            return comments;
        }

        public void setComments(String comments) {
            this.comments = comments;
        }

        public List<Test> getTestList() {
            return testList;
        }

        public void setTestList(List<Test> testList) {
            this.testList = testList;
        }

        public List<Payment> getPaymentList() {
            return paymentList;
        }

        public void setPaymentList(List<Payment> paymentList) {
            this.paymentList = paymentList;
        }


        public static class Payment {

            @SerializedName("paymentType")
            @Expose
            private String paymentType;
            @SerializedName("paymentAmount")
            @Expose
            private String paymentAmount;
            @SerializedName("issueBank")
            @Expose
            private String issueBank;

            public String getPaymentType() {
                return paymentType;
            }

            public void setPaymentType(String paymentType) {
                this.paymentType = paymentType;
            }

            public String getPaymentAmount() {
                return paymentAmount;
            }

            public void setPaymentAmount(String paymentAmount) {
                this.paymentAmount = paymentAmount;
            }

            public String getIssueBank() {
                return issueBank;
            }

            public void setIssueBank(String issueBank) {
                this.issueBank = issueBank;
            }

        }

        public static class Test {

            @SerializedName("testID")
            @Expose
            private Integer testID;
            @SerializedName("testCode")
            @Expose
            private String testCode;
            @SerializedName("integrationCode")
            @Expose
            private String integrationCode;
            @SerializedName("dictionaryId")
            @Expose
            private Integer dictionaryId;
            @SerializedName("sampleId")
            @Expose
            private String sampleId;

            public Integer getTestID() {
                return testID;
            }

            public void setTestID(Integer testID) {
                this.testID = testID;
            }

            public String getTestCode() {
                return testCode;
            }

            public void setTestCode(String testCode) {
                this.testCode = testCode;
            }

            public String getIntegrationCode() {
                return integrationCode;
            }

            public void setIntegrationCode(String integrationCode) {
                this.integrationCode = integrationCode;
            }

            public Integer getDictionaryId() {
                return dictionaryId;
            }

            public void setDictionaryId(Integer dictionaryId) {
                this.dictionaryId = dictionaryId;
            }

            public String getSampleId() {
                return sampleId;
            }

            public void setSampleId(String sampleId) {
                this.sampleId = sampleId;
            }

        }

    }


}

