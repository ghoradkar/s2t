package com.myhindlab.abkat.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.myhindlab.abkat.appointment_confirmation.pojo.DependentListModel;

import java.util.List;

public class DependentModel {


    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("output")
    @Expose
    private List<DependentModel> output;

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

    public List<DependentModel> getOutput() {
        return output;
    }

    public void setOutput(List<DependentModel> output) {
        this.output = output;
    }

    @SerializedName("AssignCallID")
    @Expose
    private String assignCallID;
    @SerializedName("RelId")
    @Expose
    private String relId;
    @SerializedName("Age")
    @Expose
    private String age;
    @SerializedName("FirstName")
    @Expose
    private String firstName;
    @SerializedName("MiddleName")
    @Expose
    private String middleName;
    @SerializedName("LastName")
    @Expose
    private String lastName;

    public String getLastDependantScreeningDate() {
        return lastDependantScreeningDate;
    }

    public void setLastDependantScreeningDate(String lastDependantScreeningDate) {
        this.lastDependantScreeningDate = lastDependantScreeningDate;
    }

    @SerializedName("LastDependantScreeningDate")
    @Expose
    private String lastDependantScreeningDate;

    public DependentModel(String assignCallID, String relId, String age, String firstName, String middleName, String lastName ,String lastDependantScreeningDate) {
        this.assignCallID = assignCallID;
        this.relId = relId;
        this.age = age;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.lastDependantScreeningDate = lastDependantScreeningDate;
    }

    public String getAssignCallID() {
        return assignCallID;
    }

    public void setAssignCallID(String assignCallID) {
        this.assignCallID = assignCallID;
    }

    public String getRelId() {
        return relId;
    }

    public void setRelId(String relId) {
        this.relId = relId;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
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

}