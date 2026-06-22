package com.myhindlab.abkat.models;

import java.io.Serializable;
import java.util.ArrayList;

public class PostCampBeneficiaryListModel implements Serializable {

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

        private String CAMPID;

        private String DistName;

        private String DISTLGDCODE;

        private String DOB;

        private String RegdId;
        private String RegdNo;

        private String BeneficiaryName;

        private String Gender;

        private String BeneficiaryStatus;

        private String Age;

        private String PostRemarkRemark;
        private String DeliveryChallan_PhotoPath;

        private String PostFilePath;

        public String getDeliveryChallan_PhotoPath() {
            return DeliveryChallan_PhotoPath;
        }

        public void setDeliveryChallan_PhotoPath(String deliveryChallan_PhotoPath) {
            DeliveryChallan_PhotoPath = deliveryChallan_PhotoPath;
        }

        private String PostcampDocRemark;

        private String PostCampAcknowlegedmentFile;

        private String DoctorId;

        private String DoctorName;

        private String MobileNo;
        private String AckReceiptPath;
        private String ID;
        private String ReferTo;
        private String PhotoPath;
        private String Beneficiary_photoPath;
        private String Consent_Form_PhotoPath;
        private String DeliveryStatusRemarkID;
        private String deliveryStatusName;
        private String OtherRemark;
        private String WorkersMob;

        public String getWorkersMob() {
            return WorkersMob;
        }

        public void setWorkersMob(String workersMob) {
            WorkersMob = workersMob;
        }

        public String getAlternateMobNo() {
            return AlternateMobNo;
        }

        public void setAlternateMobNo(String alternateMobNo) {
            AlternateMobNo = alternateMobNo;
        }

        private String AlternateMobNo;

        public String getOtherRemark() {
            return OtherRemark;
        }

        public void setOtherRemark(String otherRemark) {
            OtherRemark = otherRemark;
        }

        public String getDeliveryStatusRemarkID() {
            return DeliveryStatusRemarkID;
        }

        public void setDeliveryStatusRemarkID(String deliveryStatusRemarkID) {
            DeliveryStatusRemarkID = deliveryStatusRemarkID;
        }

        public String getDeliveryStatusName() {
            return deliveryStatusName;
        }

        public void setDeliveryStatusName(String deliveryStatusName) {
            this.deliveryStatusName = deliveryStatusName;
        }

        public String getDeliveryRemarkID() {
            return DeliveryRemarkID;
        }

        public void setDeliveryRemarkID(String deliveryRemarkID) {
            DeliveryRemarkID = deliveryRemarkID;
        }

        public String getDeliveryRemark() {
            return DeliveryRemark;
        }

        public void setDeliveryRemark(String deliveryRemark) {
            DeliveryRemark = deliveryRemark;
        }

        private String DeliveryRemarkID;
        private String DeliveryRemark;

        public String getBeneficiary_photoPath() {
            return Beneficiary_photoPath;
        }

        public void setBeneficiary_photoPath(String beneficiary_photoPath) {
            Beneficiary_photoPath = beneficiary_photoPath;
        }

        public String getConsent_Form_PhotoPath() {
            return Consent_Form_PhotoPath;
        }

        public void setConsent_Form_PhotoPath(String consent_Form_PhotoPath) {
            Consent_Form_PhotoPath = consent_Form_PhotoPath;
        }

        private String TreatmentID;
        private String campid;
        private String Beneficiry_Number;
        private String Patient_Name;
        private String Beneficiry_Address;
        private String Pincode;
        private String TALNAME;
        private String DISTNAME;
        private String Delivarystatus;
        private String DeliveryChallanID;

        public String getDeliveryChallanID() {
            return DeliveryChallanID;
        }

        public void setDeliveryChallanID(String deliveryChallanID) {
            DeliveryChallanID = deliveryChallanID;
        }

        public String getMedicalDelivaryID() {
            return MedicalDelivaryID;
        }

        public void setMedicalDelivaryID(String medicalDelivaryID) {
            MedicalDelivaryID = medicalDelivaryID;
        }

        private String MedicalDelivaryID;

        public String getTreatmentID() {
            return TreatmentID;
        }

        public void setTreatmentID(String treatmentID) {
            TreatmentID = treatmentID;
        }

        public String getCampid() {
            return campid;
        }

        public void setCampid(String campid) {
            this.campid = campid;
        }

        public String getBeneficiry_Number() {
            return Beneficiry_Number;
        }

        public void setBeneficiry_Number(String beneficiry_Number) {
            Beneficiry_Number = beneficiry_Number;
        }

        public String getPatient_Name() {
            return Patient_Name;
        }

        public void setPatient_Name(String patient_Name) {
            Patient_Name = patient_Name;
        }

        public String getBeneficiry_Address() {
            return Beneficiry_Address;
        }

        public void setBeneficiry_Address(String beneficiry_Address) {
            Beneficiry_Address = beneficiry_Address;
        }

        public String getPincode() {
            return Pincode;
        }

        public void setPincode(String pincode) {
            Pincode = pincode;
        }

        public String getTALNAME() {
            return TALNAME;
        }

        public void setTALNAME(String TALNAME) {
            this.TALNAME = TALNAME;
        }

        public String getDISTNAME() {
            return DISTNAME;
        }

        public void setDISTNAME(String DISTNAME) {
            this.DISTNAME = DISTNAME;
        }

        public String getDelivarystatus() {
            return Delivarystatus;
        }

        public void setDelivarystatus(String delivarystatus) {
            Delivarystatus = delivarystatus;
        }

        public String getPhotoPath() {
            return PhotoPath;
        }

        public void setPhotoPath(String photoPath) {
            PhotoPath = photoPath;
        }

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public String getReferTo() {
            return ReferTo;
        }

        public void setReferTo(String referTo) {
            ReferTo = referTo;
        }

        public String getAckReceiptPath() {
            return AckReceiptPath;
        }

        public void setAckReceiptPath(String ackReceiptPath) {
            AckReceiptPath = ackReceiptPath;
        }

        public String getOTPStatus() {
            return OTPStatus;
        }

        public void setOTPStatus(String OTPStatus) {
            this.OTPStatus = OTPStatus;
        }

        private String OTPStatus;

        public String getMobileNo() {
            return MobileNo;
        }

        public void setMobileNo(String mobileNo) {
            MobileNo = mobileNo;
        }

        public String getCAMPID() {
            return CAMPID;
        }

        public void setCAMPID(String CAMPID) {
            this.CAMPID = CAMPID;
        }

        public String getDistName() {
            return DistName;
        }

        public void setDistName(String distName) {
            DistName = distName;
        }

        public String getDISTLGDCODE() {
            return DISTLGDCODE;
        }

        public void setDISTLGDCODE(String DISTLGDCODE) {
            this.DISTLGDCODE = DISTLGDCODE;
        }

        public String getDOB() {
            return DOB;
        }

        public void setDOB(String DOB) {
            this.DOB = DOB;
        }

        public String getRegdId() {
            return RegdId;
        }

        public void setRegdId(String regdId) {
            RegdId = regdId;
        }

        public String getBeneficiaryName() {
            return BeneficiaryName;
        }

        public void setBeneficiaryName(String beneficiaryName) {
            BeneficiaryName = beneficiaryName;
        }

        public String getGender() {
            return Gender;
        }

        public void setGender(String gender) {
            Gender = gender;
        }

        public String getBeneficiaryStatus() {
            return BeneficiaryStatus;
        }

        public void setBeneficiaryStatus(String beneficiaryStatus) {
            BeneficiaryStatus = beneficiaryStatus;
        }

        public String getAge() {
            return Age;
        }

        public void setAge(String age) {
            Age = age;
        }

        public String getPostRemarkRemark() {
            return PostRemarkRemark;
        }

        public void setPostRemarkRemark(String postRemarkRemark) {
            PostRemarkRemark = postRemarkRemark;
        }

        public String getPostFilePath() {
            return PostFilePath;
//            return "NA";
        }

        public void setPostFilePath(String postFilePath) {
            PostFilePath = postFilePath;
        }

        public String getPostcampDocRemark() {
            return PostcampDocRemark;
        }

        public void setPostcampDocRemark(String postcampDocRemark) {
            PostcampDocRemark = postcampDocRemark;
        }

        public String getPostCampAcknowlegedmentFile() {
            return PostCampAcknowlegedmentFile;
        }

        public void setPostCampAcknowlegedmentFile(String postCampAcknowlegedmentFile) {
            PostCampAcknowlegedmentFile = postCampAcknowlegedmentFile;
        }

        public String getDoctorId() {
            return DoctorId;
        }

        public void setDoctorId(String DoctorId) {
            this.DoctorId = DoctorId;
        }

        public String getDoctorName() {
            return DoctorName;
        }

        public void setDoctorName(String DoctorName) {
            this.DoctorName = DoctorName;
        }

        public String getRegdNo() {
            return RegdNo;
        }

        public void setRegdNo(String regdNo) {
            RegdNo = regdNo;
        }
    }
}
