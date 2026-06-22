package com.myhindlab.abkat.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DependentForSearchBeneficiaryModel {


    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("output")
    @Expose
    private List<DependentForSearchBeneficiaryModel> output;

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

    public List<DependentForSearchBeneficiaryModel> getOutput() {
        return output;
    }

    public void setOutput(List<DependentForSearchBeneficiaryModel> output) {
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
    @SerializedName("PatAge")
    @Expose
    private String patAge;
    @SerializedName("WorkersMob")
    @Expose
    private String workersMob;

    @SerializedName("AlternateMobNo")
    @Expose
    private String AlternateMobNo;

    public String getTeamid() {
        return teamid;
    }

    public void setTeamid(String teamid) {
        this.teamid = teamid;
    }

    @SerializedName("Teamid")
    @Expose
    private String teamid;


    @SerializedName("T2T_Order_Id")
    @Expose
    private String t2t_Order_Id;

    @SerializedName("TreatmentID")
    @Expose
    private String treatmentID;

    public String getT2t_Order_Id() {
        return t2t_Order_Id;
    }

    public void setT2t_Order_Id(String t2t_Order_Id) {
        this.t2t_Order_Id = t2t_Order_Id;
    }

    public String getTreatmentID() {
        return treatmentID;
    }

    public void setTreatmentID(String treatmentID) {
        this.treatmentID = treatmentID;
    }

    @SerializedName("count")
    @Expose
    private String count = "0";

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getPatAge() {
        return patAge;
    }

    public void setPatAge(String patAge) {
        this.patAge = patAge;
    }

    @SerializedName("ServiceCode")
    @Expose
    private String ServiceCode;

    @SerializedName("CatCode")
    @Expose
    private String catCode;

    @SerializedName("ServiceName")
    @Expose
    private String serviceName;

    @SerializedName("TubName")
    @Expose
    private String tubName;

    @SerializedName("TubeId")
    @Expose
    private String tubeId;

    @SerializedName("TubColor")
    @Expose
    private String tubColor;

    public String getLabCode() {
        return LabCode;
    }

    public void setLabCode(String labCode) {
        LabCode = labCode;
    }

    public String getLabName() {
        return LabName;
    }

    public void setLabName(String labName) {
        LabName = labName;
    }

    @SerializedName("LabCode")
    @Expose
    private String LabCode;

    @SerializedName("LabName")
    @Expose
    private String LabName;

    public String getTubName() {
        return tubName;
    }

    public void setTubName(String tubName) {
        this.tubName = tubName;
    }

    public String getTubeId() {
        return tubeId;
    }

    public void setTubeId(String tubeId) {
        this.tubeId = tubeId;
    }

    public String getTubColor() {
        return tubColor;
    }

    public void setTubColor(String tubColor) {
        this.tubColor = tubColor;
    }

    @SerializedName("IsAppointmentDone")
    @Expose
    private String isAppointmentDone;

    public String getIsAppointmentDone() {
        return isAppointmentDone;
    }

    public void setIsAppointmentDone(String isAppointmentDone) {
        this.isAppointmentDone = isAppointmentDone;
    }

    @SerializedName("CatName")
    @Expose
    private String CatName;

    public String getSampleQuantity() {
        return sampleQuantity;
    }

    public void setSampleQuantity(String sampleQuantity) {
        this.sampleQuantity = sampleQuantity;
    }

    @SerializedName("SampleQuantity(ml)")
    @Expose
    private String sampleQuantity;

    public String getServiceCode() {
        return ServiceCode;
    }

    public void setServiceCode(String serviceCode) {
        ServiceCode = serviceCode;
    }

    public String getCatCode() {
        return catCode;
    }

    public void setCatCode(String catCode) {
        this.catCode = catCode;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getCatName() {
        return CatName;
    }

    public void setCatName(String catName) {
        CatName = catName;
    }

    @SerializedName("AGE")
    @Expose
    private String aGE;
    @SerializedName("FirstName")
    @Expose
    private String firstName;

    @SerializedName("Name")
    @Expose
    private String name;

    @SerializedName("RegdNo")
    @Expose
    private String regdNo;

    public String getRegdNo() {
        return regdNo;
    }

    public void setRegdNo(String regdNo) {
        this.regdNo = regdNo;
    }

    @SerializedName("GENDER")
    @Expose
    private String gENDER;
    @SerializedName("Gender")
    @Expose
    private String gender;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @SerializedName("Regdid")
    @Expose
    private String regdid;

    @SerializedName("BeneficiaryName")
    @Expose
    private String beneficiaryName;

    @SerializedName("MobileNo")
    @Expose
    private String mobileNo;
    @SerializedName("DISTLGDCODE")
    @Expose
    private String dISTLGDCODE;

    @SerializedName("RelationWithWorker")
    @Expose
    private String relationWithWorker;

    public String getSampleCollection() {
        return sampleCollection;
    }

    public void setSampleCollection(String sampleCollection) {
        this.sampleCollection = sampleCollection;
    }

    @SerializedName("SampleCollection")
    @Expose
    private String sampleCollection;

    public String getdISTLGDCODE() {
        return dISTLGDCODE;
    }

    public void setdISTLGDCODE(String dISTLGDCODE) {
        this.dISTLGDCODE = dISTLGDCODE;
    }

    @SerializedName("Address")
    @Expose
    private String address;

    public String getRegdid() {
        return regdid;
    }

    public void setRegdid(String regdid) {
        this.regdid = regdid;
    }

    public String getBeneficiaryName() {
        return beneficiaryName;
    }

    public void setBeneficiaryName(String beneficiaryName) {
        this.beneficiaryName = beneficiaryName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getRelationWithWorker() {
        return relationWithWorker;
    }

    public void setRelationWithWorker(String relationWithWorker) {
        this.relationWithWorker = relationWithWorker;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    @SerializedName("Area")
    @Expose
    private String area;
    @SerializedName("DISTNAME")
    @Expose
    private String dISTNAME;

    @SerializedName("AppointmentDate")
    @Expose
    private String appointmentDate;

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getdISTNAME() {
        return dISTNAME;
    }

    public void setdISTNAME(String dISTNAME) {
        this.dISTNAME = dISTNAME;
    }

    public String getNext_renewal_date() {
        return next_renewal_date;
    }

    public void setNext_renewal_date(String next_renewal_date) {
        this.next_renewal_date = next_renewal_date;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    @SerializedName("next_renewal_date")
    @Expose
    private String next_renewal_date;


    @SerializedName("IsActive")
    @Expose
    private String isActive;

    @SerializedName("AssignmentRemarks")
    @Expose
    private String assignmentRemarks;

    @SerializedName("ArId")
    @Expose
    private String arId;

    public String getAssignmentRemarks() {
        return assignmentRemarks;
    }

    public void setAssignmentRemarks(String assignmentRemarks) {
        this.assignmentRemarks = assignmentRemarks;
    }

    public String getWorkersMob() {
        return workersMob;
    }

    public void setWorkersMob(String workersMob) {
        this.workersMob = workersMob;
    }

    public String getAlternateMobNo() {
        return AlternateMobNo;
    }

    public void setAlternateMobNo(String alternateMobNo) {
        AlternateMobNo = alternateMobNo;
    }

    public String getArId() {
        return arId;
    }

    public void setArId(String arId) {
        this.arId = arId;
    }

    @SerializedName("RelName")
    @Expose
    private String relName;

    public String getRelName() {
        return relName;
    }

    public void setRelName(String relName) {
        this.relName = relName;
    }

    public String getaGE() {
        return aGE;
    }


    public void setaGE(String aGE) {
        this.aGE = aGE;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getgENDER() {
        return gENDER;
    }

    public void setgENDER(String gENDER) {
        this.gENDER = gENDER;
    }

    public String getScreeningDate() {
        return screeningDate;
    }

    public void setScreeningDate(String screeningDate) {
        this.screeningDate = screeningDate;
    }

    @SerializedName("ScreeningDate")
    @Expose
    private String screeningDate;

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

    public DependentForSearchBeneficiaryModel(String assignCallID, String relId, String age, String firstName, String middleName, String lastName, String lastDependantScreeningDate) {
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