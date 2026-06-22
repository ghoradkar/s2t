package com.myhindlab.abkat.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ScreenedDependentModel {

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

        @SerializedName("RegdNo")
        @Expose
        private Long regdNo;
        @SerializedName("AssignCallID")
        @Expose
        private Integer assignCallID;

        @SerializedName("RelId")
        @Expose
        private String relId;

        @SerializedName("Age")
        @Expose
        private String age;

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        @SerializedName("Relation")
        @Expose
        private String relation;

        @SerializedName("FirstName")
        @Expose
        private String firstName;

        public String getRelId() {
            return relId;
        }

        public void setRelId(String relId) {
            this.relId = relId;
        }

        public String getRelation() {
            return relation;
        }

        public void setRelation(String relation) {
            this.relation = relation;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getMiddleName() {
            return middleName;
        }

        public void setMiddleName(String middleName) {
            this.middleName = middleName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getScreeningDate() {
            return screeningDate;
        }

        public void setScreeningDate(String screeningDate) {
            this.screeningDate = screeningDate;
        }

        @SerializedName("MiddleName")
        @Expose
        private String middleName;

        @SerializedName("LastName")
        @Expose
        private String lastName;
        @SerializedName("NoOFScreenedDepedent")
        @Expose
        private String noOFScreenedDepedent;

        @SerializedName("ScreeningDate")
        @Expose
        private String screeningDate;

        public Long getRegdNo() {
            return regdNo;
        }

        public void setRegdNo(Long regdNo) {
            this.regdNo = regdNo;
        }

        public Integer getAssignCallID() {
            return assignCallID;
        }

        public void setAssignCallID(Integer assignCallID) {
            this.assignCallID = assignCallID;
        }

        public String getNoOFScreenedDepedent() {
            return noOFScreenedDepedent;
        }

        public void setNoOFScreenedDepedent(String noOFScreenedDepedent) {
            this.noOFScreenedDepedent = noOFScreenedDepedent;
        }

    }
}
