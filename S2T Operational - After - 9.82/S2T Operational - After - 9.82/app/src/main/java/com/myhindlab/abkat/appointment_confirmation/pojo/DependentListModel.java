package com.myhindlab.abkat.appointment_confirmation.pojo;



import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class DependentListModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("output")
    @Expose
    private List<Output> output;

    public DependentListModel(String assignCallID, String relationId, Integer age, String firstName, String middleName, String lastName) {
    }

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
        @SerializedName("AssignCallID")
        @Expose
        private Integer assignCallID;

        @SerializedName("RelId")
        @Expose
        private Integer relId;

        @SerializedName("FirstName")
        @Expose
        private String firstName;
        @SerializedName("MiddleName")
        @Expose
        private String middleName;
        @SerializedName("LastName")
        @Expose
        private String lastName;

        @SerializedName("Gender")
        @Expose
        private String gender;

        @SerializedName("LastDependantScreeningDate")
        @Expose
        private String lastDependantScreeningDate;

        public String getLastDependantScreeningDate() {
            return lastDependantScreeningDate;
        }

        public void setLastDependantScreeningDate(String lastDependantScreeningDate) {
            this.lastDependantScreeningDate = lastDependantScreeningDate;
        }

        public Integer getAssignCallID() {
            return assignCallID;
        }

        public void setAssignCallID(Integer assignCallID) {
            this.assignCallID = assignCallID;
        }

        @SerializedName("Age")
        @Expose
        private Double age;

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

        public Integer getRelId() {
            return relId;
        }

        public void setRelId(Integer relId) {
            this.relId = relId;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public Double getAge() {
            return age;
        }

        public void setAge(Double age) {
            this.age = age;
        }

    }
}