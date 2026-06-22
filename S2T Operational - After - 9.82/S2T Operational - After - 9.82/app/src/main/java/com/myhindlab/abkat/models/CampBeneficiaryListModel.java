package com.myhindlab.abkat.models;

import java.io.Serializable;
import java.util.ArrayList;

public class CampBeneficiaryListModel implements Serializable {

    private String status;
    private String message;
    private ArrayList<OutputBean> output;

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

    public ArrayList<OutputBean> getOutput() {
        return output;
    }

    public void setOutput(ArrayList<OutputBean> output) {
        this.output = output;
    }

    public static class OutputBean implements Serializable {

        private int srNo;
        private String RegdId;
        private String Regdid;
        private String T2T_Order_Id;

        public String getIsEmergency() {
            return IsEmergency;
        }

        public void setIsEmergency(String isEmergency) {
            IsEmergency = isEmergency;
        }

        private String IsEmergency;

        public String getT2T_Order_Id() {
            return T2T_Order_Id;
        }

        public void setT2T_Order_Id(String t2T_Order_Id) {
            T2T_Order_Id = t2T_Order_Id;
        }

        private String BeneficiaryName;
        private String LandingLab;
        private String LandingLabName;
        private String PinCode;
        private String Area;
        private String Address;
        private String SampleCollection;
        private String ArId;
        private String TreatmentID;

        public String getTreatmentID() {
            return TreatmentID;
        }

        public void setTreatmentID(String treatmentID) {
            TreatmentID = treatmentID;
        }

        public String getArId() {
            return ArId;
        }

        public void setArId(String arId) {
            ArId = arId;
        }

        private String DISTNAME;
        private String DISTLGDCODE;
        private String IsTeamAssign;
        private String IsAudioTestReverification;
        private String IsVisionTestReverification;
        private String IsLFTTestReverification;
        private Integer PhotoVerificationStatusID;
        private String PhotoVerificationStatusDescription;
        private String PhotoSentForVerification;
        private String AssignmentRemarks;
        private String AppointmentDate;
        private String PhotoVerifiedByCT;
        private String ScreeningPatientID;

        public String getScreeningPatientID() {
            return ScreeningPatientID;
        }

        public void setScreeningPatientID(String screeningPatientID) {
            ScreeningPatientID = screeningPatientID;
        }

        public String getPhotoVerifiedByCT() {
            return PhotoVerifiedByCT;
        }

        public void setPhotoVerifiedByCT(String photoVerifiedByCT) {
            PhotoVerifiedByCT = photoVerifiedByCT;
        }

        public int getSrNo() {
            return srNo;
        }

        public void setSrNo(int srNo) {
            this.srNo = srNo;
        }

        public String getAssignmentRemarks() {
            return AssignmentRemarks;
        }

        public void setAssignmentRemarks(String assignmentRemarks) {
            AssignmentRemarks = assignmentRemarks;
        }

        public String getAppointmentDate() {
            return AppointmentDate;
        }

        public void setAppointmentDate(String appointmentDate) {
            AppointmentDate = appointmentDate;
        }

        public String getPhotoSentForVerification() {
            return PhotoSentForVerification;
        }

        public void setPhotoSentForVerification(String photoSentForVerification) {
            PhotoSentForVerification = photoSentForVerification;
        }

        public String getIsAudioTestReverification() {
            return IsAudioTestReverification;
        }

        public void setIsAudioTestReverification(String isAudioTestReverification) {
            IsAudioTestReverification = isAudioTestReverification;
        }

        public String getIsVisionTestReverification() {
            return IsVisionTestReverification;
        }

        public void setIsVisionTestReverification(String isVisionTestReverification) {
            IsVisionTestReverification = isVisionTestReverification;
        }

        public String getIsLFTTestReverification() {
            return IsLFTTestReverification;
        }

        public void setIsLFTTestReverification(String isLFTTestReverification) {
            IsLFTTestReverification = isLFTTestReverification;
        }

        public Integer getPhotoVerificationStatusID() {
            return PhotoVerificationStatusID;
        }

        public void setPhotoVerificationStatusID(Integer photoVerificationStatusID) {
            PhotoVerificationStatusID = photoVerificationStatusID;
        }

        public String getPhotoVerificationStatusDescription() {
            return PhotoVerificationStatusDescription;
        }

        public void setPhotoVerificationStatusDescription(String photoVerificationStatusDescription) {
            PhotoVerificationStatusDescription = photoVerificationStatusDescription;
        }

        public String getIsTeamAssign() {
            return IsTeamAssign;
        }

        public void setIsTeamAssign(String isTeamAssign) {
            IsTeamAssign = isTeamAssign;
        }

        public String getDISTNAME() {
            return DISTNAME;
        }

        public void setDISTNAME(String DISTNAME) {
            this.DISTNAME = DISTNAME;
        }

        public String getDISTLGDCODE() {
            return DISTLGDCODE;
        }

        public void setDISTLGDCODE(String DISTLGDCODE) {
            this.DISTLGDCODE = DISTLGDCODE;
        }

        public String getRegdid() {
            return Regdid;
        }

        public void setRegdid(String regdid) {
            Regdid = regdid;
        }

        public String getBeneficiaryName() {
            return BeneficiaryName;
        }

        public void setBeneficiaryName(String beneficiaryName) {
            BeneficiaryName = beneficiaryName;
        }

        public String getLandingLab() {
            return LandingLab;
        }

        public void setLandingLab(String landingLab) {
            LandingLab = landingLab;
        }

        public String getLandingLabName() {
            return LandingLabName;
        }

        public void setLandingLabName(String landingLabName) {
            LandingLabName = landingLabName;
        }

        public String getPinCode() {
            return PinCode;
        }

        public void setPinCode(String pinCode) {
            PinCode = pinCode;
        }

        public String getArea() {
            return Area;
        }

        public void setArea(String area) {
            Area = area;
        }

        public String getAddress() {
            return Address;
        }

        public void setAddress(String address) {
            Address = address;
        }

        public String getSampleCollection() {
            return SampleCollection;
        }

        public void setSampleCollection(String sampleCollection) {
            SampleCollection = sampleCollection;
        }

        public String getAssignTeamid() {
            return AssignTeamid;
        }

        public void setAssignTeamid(String assignTeamid) {
            AssignTeamid = assignTeamid;
        }

        private String AssignTeamid;
        private String RegdNo;
        private String Regdno;
        private String CampId;

        public String getRegdno() {
            return Regdno;
        }

        public void setRegdno(String regdno) {
            Regdno = regdno;
        }

        private String SiteDetailId;
        private String SiteName;
        private String Name;
        private String MOBILE;
        private String DOB;
        private String AGE;
        private String TotalDays;

        public String getTotalDays() {
            return TotalDays;
        }

        public void setTotalDays(String totalDays) {
            TotalDays = totalDays;
        }

        private String Print;
        private String GENDER;
        private String PermanentAddress;
        private String LocalAddress;
        private String Location;
        private String Pincode;
        private String Height_CMs;
        private String Weight_KGs;
        private String BloodPressure;
        private String RegdImagePath;
        private String CardImagePath;
        private String RegdImageName;
        private String CardImageName;
        private String RelName;
        private String PhleboName;
        private String RationCardNo1;
        private String AadharCardNo;
        private String RCImagePath1;
        private String RCImagePath2;
        private String RCID1;

        public String getRCImagePath2() {
            return RCImagePath2;
        }

        public void setRCImagePath2(String RCImagePath2) {
            this.RCImagePath2 = RCImagePath2;
        }

        public String getRationCardNo1() {
            return RationCardNo1;
        }

        public void setRationCardNo1(String rationCardNo1) {
            RationCardNo1 = rationCardNo1;
        }

        public String getAadharCardNo() {
            return AadharCardNo;
        }

        public void setAadharCardNo(String aadharCardNo) {
            AadharCardNo = aadharCardNo;
        }

        public String getRCImagePath1() {
            return RCImagePath1;
        }

        public void setRCImagePath1(String RCImagePath1) {
            this.RCImagePath1 = RCImagePath1;
        }

        public String getRCID1() {
            return RCID1;
        }

        public void setRCID1(String RCID1) {
            this.RCID1 = RCID1;
        }

        public String getRationCardNo2() {
            return RationCardNo2;
        }

        public void setRationCardNo2(String rationCardNo2) {
            RationCardNo2 = rationCardNo2;
        }

        public String getRCID2() {
            return RCID2;
        }

        public void setRCID2(String RCID2) {
            this.RCID2 = RCID2;
        }

        public String getRCID3() {
            return RCID3;
        }

        public void setRCID3(String RCID3) {
            this.RCID3 = RCID3;
        }

        public String getRCImagePath3() {
            return RCImagePath3;
        }

        public void setRCImagePath3(String RCImagePath3) {
            this.RCImagePath3 = RCImagePath3;
        }

        private String RationCardNo2;
        private String RCID2;
        private String RCID3;
        private String RCImagePath3;
        private String PhleboMobNo;
        private String Systolic;
        private String Diastolic;
        private String IsApproved;
        private String TestId;
        private String Reason;
        private String ALLTESTDONE;
        private String CampCreatedBy;
        private String OtherDescription;

        private String WorkerAge;
        private String SampleCollectedBarcode;

        public String getSampleCollectedBarcode() {
            return SampleCollectedBarcode;
        }

        public void setSampleCollectedBarcode(String sampleCollectedBarcode) {
            SampleCollectedBarcode = sampleCollectedBarcode;
        }

        public String getWorkerAge() {
            return WorkerAge;
        }

        public void setWorkerAge(String workerAge) {
            WorkerAge = workerAge;
        }

        public String getWorkerGender() {
            return WorkerGender;
        }

        public void setWorkerGender(String workerGender) {
            WorkerGender = workerGender;
        }

        private String WorkerGender;

        public String getOtherDescription() {
            return OtherDescription;
        }

        public void setOtherDescription(String otherDescription) {
            OtherDescription = otherDescription;
        }

        public String getWorkerName() {
            return WorkerName;
        }

        public void setWorkerName(String workerName) {
            WorkerName = workerName;
        }

        private String WorkerName;

        public String getCampCreatedBy() {
            return CampCreatedBy;
        }

        public void setCampCreatedBy(String campCreatedBy) {
            CampCreatedBy = campCreatedBy;
        }

        public String getALLTESTDONE() {
            return ALLTESTDONE;
        }

        public void setALLTESTDONE(String ALLTESTDONE) {
            this.ALLTESTDONE = ALLTESTDONE;
        }

        public String getReason() {
            return Reason;
        }

        public void setReason(String reason) {
            Reason = reason;
        }

        public String getTestId() {
            return TestId;
        }

        public void setTestId(String testId) {
            TestId = testId;
        }

        public String getIsApproved() {
            return IsApproved;
        }

        public void setIsApproved(String isApproved) {
            IsApproved = isApproved;
        }

        public String getSystolic() {
            return Systolic;
        }

        public void setSystolic(String systolic) {
            Systolic = systolic;
        }

        public String getDiastolic() {
            return Diastolic;
        }

        public void setDiastolic(String diastolic) {
            Diastolic = diastolic;
        }

        public String getBloodSugar_R() {
            return BloodSugar_R;
        }

        public void setBloodSugar_R(String bloodSugar_R) {
            BloodSugar_R = bloodSugar_R;
        }

        private String BloodSugar_R;

        public String getPhleboName() {
            return PhleboName;
        }

        public void setPhleboName(String phleboName) {
            PhleboName = phleboName;
        }

        public String getPhleboMobNo() {
            return PhleboMobNo;
        }

        public void setPhleboMobNo(String phleboMobNo) {
            PhleboMobNo = phleboMobNo;
        }

        public String getRelName() {
            return RelName;
        }

        public void setRelName(String relName) {
            RelName = relName;
        }

        public String getRegdImageName() {
            return RegdImageName;
        }

        public void setRegdImageName(String regdImageName) {
            RegdImageName = regdImageName;
        }

        public String getCardImageName() {
            return CardImageName;
        }

        public void setCardImageName(String cardImageName) {
            CardImageName = cardImageName;
        }

        public String getHeight_CMs() {
            return Height_CMs;
        }

        public void setHeight_CMs(String height_CMs) {
            Height_CMs = height_CMs;
        }

        public String getWeight_KGs() {
            return Weight_KGs;
        }

        public void setWeight_KGs(String weight_KGs) {
            Weight_KGs = weight_KGs;
        }

        public String getBloodPressure() {
            return BloodPressure;
        }

        public void setBloodPressure(String bloodPressure) {
            BloodPressure = bloodPressure;
        }

        public String getRegdImagePath() {
            return RegdImagePath;
        }

        public void setRegdImagePath(String regdImagePath) {
            RegdImagePath = regdImagePath;
        }

        public String getCardImagePath() {
            return CardImagePath;
        }

        public void setCardImagePath(String cardImagePath) {
            CardImagePath = cardImagePath;
        }

        public String getRegdId() {
            if (RegdId == null) {
                return "NA";
            } else {
                return RegdId;
            }
        }

        public void setRegdId(String RegdId) {
            this.RegdId = RegdId;
        }

        public String getRegdNo() {
            if (RegdNo == null) {
                return "NA";
            } else {
                return RegdNo;
            }
        }

        public void setRegdNo(String RegdNo) {
            this.RegdNo = RegdNo;
        }

        public String getCampId() {

            if (CampId == null) {
                return "NA";
            } else {
                return CampId;
            }
        }

        public void setCampId(String CampId) {
            this.CampId = CampId;
        }

        public String getSiteDetailId() {

            if (SiteDetailId == null) {
                return "NA";
            } else {
                return SiteDetailId;
            }
        }

        public void setSiteDetailId(String SiteDetailId) {
            this.SiteDetailId = SiteDetailId;
        }

        public String getSiteName() {

            if (SiteName == null) {
                return "NA";
            } else {
                return SiteName;
            }
        }

        public void setSiteName(String SiteName) {
            this.SiteName = SiteName;
        }

        public String getName() {

            if (Name == null) {
                return "NA";
            } else {
                return Name;
            }
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public String getMOBILE() {

            if (MOBILE == null) {
                return "NA";
            } else {
                return MOBILE;
            }
        }

        public void setMOBILE(String MOBILE) {
            this.MOBILE = MOBILE;
        }

        public String getDOB() {
            if (DOB == null) {
                return "NA";
            } else {
                return DOB;
            }
        }

        public void setDOB(String DOB) {
            this.DOB = DOB;
        }

        public String getAGE() {

            if (AGE == null) {
                return "NA";
            } else {
                return AGE;
            }
        }

        public void setAGE(String AGE) {
            this.AGE = AGE;
        }

        public String getPrint() {

            if (Print == null) {
                return "NA";
            } else {
                return Print;
            }
        }

        public void setPrint(String Print) {
            this.Print = Print;
        }

        public String getGENDER() {

            if (GENDER == null) {
                return "NA";
            } else {
                return GENDER;
            }
        }

        public void setGENDER(String GENDER) {
            this.GENDER = GENDER;
        }

        public String getPermanentAddress() {

            if (PermanentAddress == null) {
                return "NA";
            } else {
                return PermanentAddress;
            }
        }

        public void setPermanentAddress(String PermanentAddress) {
            this.PermanentAddress = PermanentAddress;
        }

        public String getLocalAddress() {

            if (LocalAddress == null) {
                return "NA";
            } else {
                return LocalAddress;
            }
        }

        public void setLocalAddress(String LocalAddress) {
            this.LocalAddress = LocalAddress;
        }

        public String getLocation() {

            if (Location == null) {
                return "NA";
            } else {
                return Location;
            }
        }

        public void setLocation(String Location) {
            this.Location = Location;
        }

        public String getPincode() {
            if (Pincode == null) {
                return "NA";
            } else {
                return Pincode;
            }
        }

        public void setPincode(String Pincode) {
            this.Pincode = Pincode;
        }
    }
}
