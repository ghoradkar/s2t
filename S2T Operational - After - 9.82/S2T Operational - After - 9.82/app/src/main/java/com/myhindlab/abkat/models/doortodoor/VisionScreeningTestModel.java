package com.myhindlab.abkat.models.doortodoor;



import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VisionScreeningTestModel {

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

        @SerializedName("SiteId")
        @Expose
        private Object siteId;
        @SerializedName("Sensory")
        @Expose
        private String sensory;
        @SerializedName("Motor")
        @Expose
        private String motor;
        @SerializedName("CampId")
        @Expose
        private Integer campId;
        @SerializedName("Campdate")
        @Expose
        private Object campdate;
        @SerializedName("CampLocation")
        @Expose
        private Object campLocation;
        @SerializedName("SiteAddress")
        @Expose
        private Object siteAddress;
        @SerializedName("BeneficiaryName")
        @Expose
        private String beneficiaryName;
        @SerializedName("HealthCardNo")
        @Expose
        private Object healthCardNo;
        @SerializedName("BeneficiaryRegdNo")
        @Expose
        private Long beneficiaryRegdNo;
        @SerializedName("Gender")
        @Expose
        private String gender;
        @SerializedName("Age")
        @Expose
        private Integer age;
        @SerializedName("MobNo")
        @Expose
        private String mobNo;
        @SerializedName("Height_CMs")
        @Expose
        private Integer heightCMs;
        @SerializedName("Weight_KGs")
        @Expose
        private Integer weightKGs;
        @SerializedName("BloodPressure")
        @Expose
        private String bloodPressure;
        @SerializedName("BloodPressure_High")
        @Expose
        private Object bloodPressureHigh;
        @SerializedName("BloodPressure_Low")
        @Expose
        private Object bloodPressureLow;
        @SerializedName("BloodSugar_F")
        @Expose
        private String bloodSugarF;
        @SerializedName("BloodSugar_PP")
        @Expose
        private String bloodSugarPP;
        @SerializedName("BloodSugar_R")
        @Expose
        private String bloodSugarR;
        @SerializedName("BMI")
        @Expose
        private Double bmi;
        @SerializedName("BMIStatus")
        @Expose
        private String bMIStatus;
        @SerializedName("UnderWT")
        @Expose
        private Object underWT;
        @SerializedName("Temparture")
        @Expose
        private Object temparture;
        @SerializedName("Systolic")
        @Expose
        private Integer systolic;
        @SerializedName("Diastolic")
        @Expose
        private Integer diastolic;
        @SerializedName("BMIStatus1")
        @Expose
        private Integer bMIStatus1;
        @SerializedName("BloodGroup")
        @Expose
        private String bloodGroup;
        @SerializedName("MaritalStatus")
        @Expose
        private String maritalStatus;
        @SerializedName("NoOfChildren")
        @Expose
        private Integer noOfChildren;
        @SerializedName("FamilyPlanOperation")
        @Expose
        private String familyPlanOperation;
        @SerializedName("Alcohol")
        @Expose
        private String alcohol;
        @SerializedName("Smoking")
        @Expose
        private String smoking;
        @SerializedName("Tobaco")
        @Expose
        private String tobaco;
        @SerializedName("PatBarCode")
        @Expose
        private Object patBarCode;
        @SerializedName("PhysicalDoctor")
        @Expose
        private String physicalDoctor;
        @SerializedName("LungClear")
        @Expose
        private String lungClear;
        @SerializedName("LungComment")
        @Expose
        private Object lungComment;
        @SerializedName("AbnormalSound")
        @Expose
        private String abnormalSound;
        @SerializedName("AbnormalCheck")
        @Expose
        private Object abnormalCheck;
        @SerializedName("AbnormalComment")
        @Expose
        private Object abnormalComment;
        @SerializedName("OtherAbnormality")
        @Expose
        private Object otherAbnormality;
        @SerializedName("SIS2Normal")
        @Expose
        private String sIS2Normal;
        @SerializedName("AnyMurmurs")
        @Expose
        private String anyMurmurs;
        @SerializedName("CVSComment")
        @Expose
        private Object cVSComment;
        @SerializedName("Palpationabdomen")
        @Expose
        private String palpationabdomen;
        @SerializedName("Anyhernia")
        @Expose
        private String anyhernia;
        @SerializedName("MassperAbdomen")
        @Expose
        private String massperAbdomen;
        @SerializedName("GITComment")
        @Expose
        private Object gITComment;
        @SerializedName("PatwellOriented")
        @Expose
        private String patwellOriented;
        @SerializedName("CNSComment")
        @Expose
        private Object cNSComment;
        @SerializedName("FinalRemark")
        @Expose
        private String finalRemark;
        @SerializedName("FinalComment")
        @Expose
        private Object finalComment;
        @SerializedName("LMP")
        @Expose
        private Object lmp;
        @SerializedName("GynecProblem")
        @Expose
        private String gynecProblem;
        @SerializedName("ISBreastCancer")
        @Expose
        private String iSBreastCancer;
        @SerializedName("LMPDate")
        @Expose
        private Object lMPDate;
        @SerializedName("EvidanceThyroid")
        @Expose
        private String evidanceThyroid;
        @SerializedName("FamillyPlaning")
        @Expose
        private String famillyPlaning;
        @SerializedName("Comment")
        @Expose
        private Object comment;
        @SerializedName("IsPregnant")
        @Expose
        private Object isPregnant;
        @SerializedName("PregComment")
        @Expose
        private Object pregComment;
        @SerializedName("FSH")
        @Expose
        private String fsh;
        @SerializedName("FSHComment")
        @Expose
        private Object fSHComment;
        @SerializedName("RecentDelivery")
        @Expose
        private String recentDelivery;
        @SerializedName("DeliveryDate")
        @Expose
        private Object deliveryDate;
        @SerializedName("DeliveryComment")
        @Expose
        private Object deliveryComment;
        @SerializedName("FamillyAdvice")
        @Expose
        private Object famillyAdvice;
        @SerializedName("FinalComment1")
        @Expose
        private Object finalComment1;
        @SerializedName("PulseRate")
        @Expose
        private Integer pulseRate;
        @SerializedName("IsAlcohol")
        @Expose
        private String isAlcohol;
        @SerializedName("AlcoholSinceMonth")
        @Expose
        private Integer alcoholSinceMonth;
        @SerializedName("AlcoholSinceYear")
        @Expose
        private Integer alcoholSinceYear;
        @SerializedName("IsSmoking")
        @Expose
        private String isSmoking;
        @SerializedName("SmokingSinceMonth")
        @Expose
        private Integer smokingSinceMonth;
        @SerializedName("SmokingSinceYear")
        @Expose
        private Integer smokingSinceYear;
        @SerializedName("IsTobaco")
        @Expose
        private String isTobaco;
        @SerializedName("TobacoSinceMonth")
        @Expose
        private Integer tobacoSinceMonth;
        @SerializedName("TobacoSinceYear")
        @Expose
        private Integer tobacoSinceYear;
        @SerializedName("IsDrug")
        @Expose
        private String isDrug;
        @SerializedName("DrugSinceMonth")
        @Expose
        private Integer drugSinceMonth;
        @SerializedName("DrugSinceYear")
        @Expose
        private Integer drugSinceYear;
        @SerializedName("Vision_Injury_Evidence_right")
        @Expose
        private String visionInjuryEvidenceRight;
        @SerializedName("Vision_Injury_Evidence_Left")
        @Expose
        private String visionInjuryEvidenceLeft;
        @SerializedName("Vision_Snellelchart_R")
        @Expose
        private String visionSnellelchartR;
        @SerializedName("Vision_Snellelchart_L")
        @Expose
        private String visionSnellelchartL;
        @SerializedName("Vision_Snellelchart_L1")
        @Expose
        private String visionSnellelchartL1;
        @SerializedName("iSGlasses")
        @Expose
        private Integer iSGlasses;
        @SerializedName("Suggestion")
        @Expose
        private Object suggestion;
        @SerializedName("other_remark")
        @Expose
        private Object otherRemark;
        @SerializedName("RightRemark")
        @Expose
        private String rightRemark;
        @SerializedName("LeftRemark")
        @Expose
        private String leftRemark;
        @SerializedName("NearVisionRemarkfooter")
        @Expose
        private String nearVisionRemarkfooter;
        @SerializedName("NearVisionRemark")
        @Expose
        private String nearVisionRemark;

        public Object getSiteId() {
            return siteId;
        }

        public void setSiteId(Object siteId) {
            this.siteId = siteId;
        }

        public String getSensory() {
            return sensory;
        }

        public void setSensory(String sensory) {
            this.sensory = sensory;
        }

        public String getMotor() {
            return motor;
        }

        public void setMotor(String motor) {
            this.motor = motor;
        }

        public Integer getCampId() {
            return campId;
        }

        public void setCampId(Integer campId) {
            this.campId = campId;
        }

        public Object getCampdate() {
            return campdate;
        }

        public void setCampdate(Object campdate) {
            this.campdate = campdate;
        }

        public Object getCampLocation() {
            return campLocation;
        }

        public void setCampLocation(Object campLocation) {
            this.campLocation = campLocation;
        }

        public Object getSiteAddress() {
            return siteAddress;
        }

        public void setSiteAddress(Object siteAddress) {
            this.siteAddress = siteAddress;
        }

        public String getBeneficiaryName() {
            return beneficiaryName;
        }

        public void setBeneficiaryName(String beneficiaryName) {
            this.beneficiaryName = beneficiaryName;
        }

        public Object getHealthCardNo() {
            return healthCardNo;
        }

        public void setHealthCardNo(Object healthCardNo) {
            this.healthCardNo = healthCardNo;
        }

        public Long getBeneficiaryRegdNo() {
            return beneficiaryRegdNo;
        }

        public void setBeneficiaryRegdNo(Long beneficiaryRegdNo) {
            this.beneficiaryRegdNo = beneficiaryRegdNo;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public String getMobNo() {
            return mobNo;
        }

        public void setMobNo(String mobNo) {
            this.mobNo = mobNo;
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

        public Object getBloodPressureHigh() {
            return bloodPressureHigh;
        }

        public void setBloodPressureHigh(Object bloodPressureHigh) {
            this.bloodPressureHigh = bloodPressureHigh;
        }

        public Object getBloodPressureLow() {
            return bloodPressureLow;
        }

        public void setBloodPressureLow(Object bloodPressureLow) {
            this.bloodPressureLow = bloodPressureLow;
        }

        public String getBloodSugarF() {
            return bloodSugarF;
        }

        public void setBloodSugarF(String bloodSugarF) {
            this.bloodSugarF = bloodSugarF;
        }

        public String getBloodSugarPP() {
            return bloodSugarPP;
        }

        public void setBloodSugarPP(String bloodSugarPP) {
            this.bloodSugarPP = bloodSugarPP;
        }

        public String getBloodSugarR() {
            return bloodSugarR;
        }

        public void setBloodSugarR(String bloodSugarR) {
            this.bloodSugarR = bloodSugarR;
        }

        public Double getBmi() {
            return bmi;
        }

        public void setBmi(Double bmi) {
            this.bmi = bmi;
        }

        public String getBMIStatus() {
            return bMIStatus;
        }

        public void setBMIStatus(String bMIStatus) {
            this.bMIStatus = bMIStatus;
        }

        public Object getUnderWT() {
            return underWT;
        }

        public void setUnderWT(Object underWT) {
            this.underWT = underWT;
        }

        public Object getTemparture() {
            return temparture;
        }

        public void setTemparture(Object temparture) {
            this.temparture = temparture;
        }

        public Integer getSystolic() {
            return systolic;
        }

        public void setSystolic(Integer systolic) {
            this.systolic = systolic;
        }

        public Integer getDiastolic() {
            return diastolic;
        }

        public void setDiastolic(Integer diastolic) {
            this.diastolic = diastolic;
        }

        public Integer getBMIStatus1() {
            return bMIStatus1;
        }

        public void setBMIStatus1(Integer bMIStatus1) {
            this.bMIStatus1 = bMIStatus1;
        }

        public String getBloodGroup() {
            return bloodGroup;
        }

        public void setBloodGroup(String bloodGroup) {
            this.bloodGroup = bloodGroup;
        }

        public String getMaritalStatus() {
            return maritalStatus;
        }

        public void setMaritalStatus(String maritalStatus) {
            this.maritalStatus = maritalStatus;
        }

        public Integer getNoOfChildren() {
            return noOfChildren;
        }

        public void setNoOfChildren(Integer noOfChildren) {
            this.noOfChildren = noOfChildren;
        }

        public String getFamilyPlanOperation() {
            return familyPlanOperation;
        }

        public void setFamilyPlanOperation(String familyPlanOperation) {
            this.familyPlanOperation = familyPlanOperation;
        }

        public String getAlcohol() {
            return alcohol;
        }

        public void setAlcohol(String alcohol) {
            this.alcohol = alcohol;
        }

        public String getSmoking() {
            return smoking;
        }

        public void setSmoking(String smoking) {
            this.smoking = smoking;
        }

        public String getTobaco() {
            return tobaco;
        }

        public void setTobaco(String tobaco) {
            this.tobaco = tobaco;
        }

        public Object getPatBarCode() {
            return patBarCode;
        }

        public void setPatBarCode(Object patBarCode) {
            this.patBarCode = patBarCode;
        }

        public String getPhysicalDoctor() {
            return physicalDoctor;
        }

        public void setPhysicalDoctor(String physicalDoctor) {
            this.physicalDoctor = physicalDoctor;
        }

        public String getLungClear() {
            return lungClear;
        }

        public void setLungClear(String lungClear) {
            this.lungClear = lungClear;
        }

        public Object getLungComment() {
            return lungComment;
        }

        public void setLungComment(Object lungComment) {
            this.lungComment = lungComment;
        }

        public String getAbnormalSound() {
            return abnormalSound;
        }

        public void setAbnormalSound(String abnormalSound) {
            this.abnormalSound = abnormalSound;
        }

        public Object getAbnormalCheck() {
            return abnormalCheck;
        }

        public void setAbnormalCheck(Object abnormalCheck) {
            this.abnormalCheck = abnormalCheck;
        }

        public Object getAbnormalComment() {
            return abnormalComment;
        }

        public void setAbnormalComment(Object abnormalComment) {
            this.abnormalComment = abnormalComment;
        }

        public Object getOtherAbnormality() {
            return otherAbnormality;
        }

        public void setOtherAbnormality(Object otherAbnormality) {
            this.otherAbnormality = otherAbnormality;
        }

        public String getSIS2Normal() {
            return sIS2Normal;
        }

        public void setSIS2Normal(String sIS2Normal) {
            this.sIS2Normal = sIS2Normal;
        }

        public String getAnyMurmurs() {
            return anyMurmurs;
        }

        public void setAnyMurmurs(String anyMurmurs) {
            this.anyMurmurs = anyMurmurs;
        }

        public Object getCVSComment() {
            return cVSComment;
        }

        public void setCVSComment(Object cVSComment) {
            this.cVSComment = cVSComment;
        }

        public String getPalpationabdomen() {
            return palpationabdomen;
        }

        public void setPalpationabdomen(String palpationabdomen) {
            this.palpationabdomen = palpationabdomen;
        }

        public String getAnyhernia() {
            return anyhernia;
        }

        public void setAnyhernia(String anyhernia) {
            this.anyhernia = anyhernia;
        }

        public String getMassperAbdomen() {
            return massperAbdomen;
        }

        public void setMassperAbdomen(String massperAbdomen) {
            this.massperAbdomen = massperAbdomen;
        }

        public Object getGITComment() {
            return gITComment;
        }

        public void setGITComment(Object gITComment) {
            this.gITComment = gITComment;
        }

        public String getPatwellOriented() {
            return patwellOriented;
        }

        public void setPatwellOriented(String patwellOriented) {
            this.patwellOriented = patwellOriented;
        }

        public Object getCNSComment() {
            return cNSComment;
        }

        public void setCNSComment(Object cNSComment) {
            this.cNSComment = cNSComment;
        }

        public String getFinalRemark() {
            return finalRemark;
        }

        public void setFinalRemark(String finalRemark) {
            this.finalRemark = finalRemark;
        }

        public Object getFinalComment() {
            return finalComment;
        }

        public void setFinalComment(Object finalComment) {
            this.finalComment = finalComment;
        }

        public Object getLmp() {
            return lmp;
        }

        public void setLmp(Object lmp) {
            this.lmp = lmp;
        }

        public String getGynecProblem() {
            return gynecProblem;
        }

        public void setGynecProblem(String gynecProblem) {
            this.gynecProblem = gynecProblem;
        }

        public String getISBreastCancer() {
            return iSBreastCancer;
        }

        public void setISBreastCancer(String iSBreastCancer) {
            this.iSBreastCancer = iSBreastCancer;
        }

        public Object getLMPDate() {
            return lMPDate;
        }

        public void setLMPDate(Object lMPDate) {
            this.lMPDate = lMPDate;
        }

        public String getEvidanceThyroid() {
            return evidanceThyroid;
        }

        public void setEvidanceThyroid(String evidanceThyroid) {
            this.evidanceThyroid = evidanceThyroid;
        }

        public String getFamillyPlaning() {
            return famillyPlaning;
        }

        public void setFamillyPlaning(String famillyPlaning) {
            this.famillyPlaning = famillyPlaning;
        }

        public Object getComment() {
            return comment;
        }

        public void setComment(Object comment) {
            this.comment = comment;
        }

        public Object getIsPregnant() {
            return isPregnant;
        }

        public void setIsPregnant(Object isPregnant) {
            this.isPregnant = isPregnant;
        }

        public Object getPregComment() {
            return pregComment;
        }

        public void setPregComment(Object pregComment) {
            this.pregComment = pregComment;
        }

        public String getFsh() {
            return fsh;
        }

        public void setFsh(String fsh) {
            this.fsh = fsh;
        }

        public Object getFSHComment() {
            return fSHComment;
        }

        public void setFSHComment(Object fSHComment) {
            this.fSHComment = fSHComment;
        }

        public String getRecentDelivery() {
            return recentDelivery;
        }

        public void setRecentDelivery(String recentDelivery) {
            this.recentDelivery = recentDelivery;
        }

        public Object getDeliveryDate() {
            return deliveryDate;
        }

        public void setDeliveryDate(Object deliveryDate) {
            this.deliveryDate = deliveryDate;
        }

        public Object getDeliveryComment() {
            return deliveryComment;
        }

        public void setDeliveryComment(Object deliveryComment) {
            this.deliveryComment = deliveryComment;
        }

        public Object getFamillyAdvice() {
            return famillyAdvice;
        }

        public void setFamillyAdvice(Object famillyAdvice) {
            this.famillyAdvice = famillyAdvice;
        }

        public Object getFinalComment1() {
            return finalComment1;
        }

        public void setFinalComment1(Object finalComment1) {
            this.finalComment1 = finalComment1;
        }

        public Integer getPulseRate() {
            return pulseRate;
        }

        public void setPulseRate(Integer pulseRate) {
            this.pulseRate = pulseRate;
        }

        public String getIsAlcohol() {
            return isAlcohol;
        }

        public void setIsAlcohol(String isAlcohol) {
            this.isAlcohol = isAlcohol;
        }

        public Integer getAlcoholSinceMonth() {
            return alcoholSinceMonth;
        }

        public void setAlcoholSinceMonth(Integer alcoholSinceMonth) {
            this.alcoholSinceMonth = alcoholSinceMonth;
        }

        public Integer getAlcoholSinceYear() {
            return alcoholSinceYear;
        }

        public void setAlcoholSinceYear(Integer alcoholSinceYear) {
            this.alcoholSinceYear = alcoholSinceYear;
        }

        public String getIsSmoking() {
            return isSmoking;
        }

        public void setIsSmoking(String isSmoking) {
            this.isSmoking = isSmoking;
        }

        public Integer getSmokingSinceMonth() {
            return smokingSinceMonth;
        }

        public void setSmokingSinceMonth(Integer smokingSinceMonth) {
            this.smokingSinceMonth = smokingSinceMonth;
        }

        public Integer getSmokingSinceYear() {
            return smokingSinceYear;
        }

        public void setSmokingSinceYear(Integer smokingSinceYear) {
            this.smokingSinceYear = smokingSinceYear;
        }

        public String getIsTobaco() {
            return isTobaco;
        }

        public void setIsTobaco(String isTobaco) {
            this.isTobaco = isTobaco;
        }

        public Integer getTobacoSinceMonth() {
            return tobacoSinceMonth;
        }

        public void setTobacoSinceMonth(Integer tobacoSinceMonth) {
            this.tobacoSinceMonth = tobacoSinceMonth;
        }

        public Integer getTobacoSinceYear() {
            return tobacoSinceYear;
        }

        public void setTobacoSinceYear(Integer tobacoSinceYear) {
            this.tobacoSinceYear = tobacoSinceYear;
        }

        public String getIsDrug() {
            return isDrug;
        }

        public void setIsDrug(String isDrug) {
            this.isDrug = isDrug;
        }

        public Integer getDrugSinceMonth() {
            return drugSinceMonth;
        }

        public void setDrugSinceMonth(Integer drugSinceMonth) {
            this.drugSinceMonth = drugSinceMonth;
        }

        public Integer getDrugSinceYear() {
            return drugSinceYear;
        }

        public void setDrugSinceYear(Integer drugSinceYear) {
            this.drugSinceYear = drugSinceYear;
        }

        public String getVisionInjuryEvidenceRight() {
            return visionInjuryEvidenceRight;
        }

        public void setVisionInjuryEvidenceRight(String visionInjuryEvidenceRight) {
            this.visionInjuryEvidenceRight = visionInjuryEvidenceRight;
        }

        public String getVisionInjuryEvidenceLeft() {
            return visionInjuryEvidenceLeft;
        }

        public void setVisionInjuryEvidenceLeft(String visionInjuryEvidenceLeft) {
            this.visionInjuryEvidenceLeft = visionInjuryEvidenceLeft;
        }

        public String getVisionSnellelchartR() {
            return visionSnellelchartR;
        }

        public void setVisionSnellelchartR(String visionSnellelchartR) {
            this.visionSnellelchartR = visionSnellelchartR;
        }

        public String getVisionSnellelchartL() {
            return visionSnellelchartL;
        }

        public void setVisionSnellelchartL(String visionSnellelchartL) {
            this.visionSnellelchartL = visionSnellelchartL;
        }

        public String getVisionSnellelchartL1() {
            return visionSnellelchartL1;
        }

        public void setVisionSnellelchartL1(String visionSnellelchartL1) {
            this.visionSnellelchartL1 = visionSnellelchartL1;
        }

        public Integer getiSGlasses() {
            return iSGlasses;
        }

        public void setiSGlasses(Integer iSGlasses) {
            this.iSGlasses = iSGlasses;
        }

        public Object getSuggestion() {
            return suggestion;
        }

        public void setSuggestion(Object suggestion) {
            this.suggestion = suggestion;
        }

        public Object getOtherRemark() {
            return otherRemark;
        }

        public void setOtherRemark(Object otherRemark) {
            this.otherRemark = otherRemark;
        }

        public String getRightRemark() {
            return rightRemark;
        }

        public void setRightRemark(String rightRemark) {
            this.rightRemark = rightRemark;
        }

        public String getLeftRemark() {
            return leftRemark;
        }

        public void setLeftRemark(String leftRemark) {
            this.leftRemark = leftRemark;
        }

        public String getNearVisionRemarkfooter() {
            return nearVisionRemarkfooter;
        }

        public void setNearVisionRemarkfooter(String nearVisionRemarkfooter) {
            this.nearVisionRemarkfooter = nearVisionRemarkfooter;
        }

        public String getNearVisionRemark() {
            return nearVisionRemark;
        }

        public void setNearVisionRemark(String nearVisionRemark) {
            this.nearVisionRemark = nearVisionRemark;
        }

    }

}