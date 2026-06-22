package com.myhindlab.abkat.models;

import java.util.List;

public class PatientDetailsModel {


    /**
     * status : Success
     * message : Patient List
     * output : [{"Title":"Mrs.","patientname":"Jayashree A Sutar","MobileNo":"7058008152","UID":"","Age":32,"DOB":"01-01-1900","Gender":"F","PermanentAddress":"Sambhajinagar chinchawad","LocalAddress":"Sambhajinagar chinchawad","Pincode":"411019","IsHCRenewal":"No","patientPhoto":"http://testmcwwb.myhindlab.com/BETA_MYHINDLABDOCS/CampDocs/PatientImage/111060025356_cropped8301139453773385618.jpg","HealthCardPath":"http://testmcwwb.myhindlab.com/BETA_MYHINDLABDOCS/CampDocs/BarcodeImage/54003_544363-cropped3588615282207702092.jpg","HCRenewalFilePath":"http://testmcwwb.myhindlab.com/BETA_MYHINDLABDOCS/CampDocs/BarcodeImage/","UserThumbPath":"http://testmcwwb.myhindlab.com/BETA_MYHINDLABDOCS/CampDocs/ImpressionRawFile/544363_544363_1_1351_fingerImage.png","RenewalDate":null,"RegistrationDate":"05-01-2020"}]
     */

    private String status;
    private String message;
    private List<OutputBean> output;

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

    public List<OutputBean> getOutput() {
        return output;
    }

    public void setOutput(List<OutputBean> output) {
        this.output = output;
    }

    public static class OutputBean {
        /**
         * Title : Mrs.
         * patientname : Jayashree A Sutar
         * MobileNo : 7058008152
         * UID :
         * Age : 32
         * DOB : 01-01-1900
         * Gender : F
         * PermanentAddress : Sambhajinagar chinchawad
         * LocalAddress : Sambhajinagar chinchawad
         * Pincode : 411019
         * IsHCRenewal : No
         * patientPhoto : http://testmcwwb.myhindlab.com/BETA_MYHINDLABDOCS/CampDocs/PatientImage/111060025356_cropped8301139453773385618.jpg
         * HealthCardPath : http://testmcwwb.myhindlab.com/BETA_MYHINDLABDOCS/CampDocs/BarcodeImage/54003_544363-cropped3588615282207702092.jpg
         * HCRenewalFilePath : http://testmcwwb.myhindlab.com/BETA_MYHINDLABDOCS/CampDocs/BarcodeImage/
         * UserThumbPath : http://testmcwwb.myhindlab.com/BETA_MYHINDLABDOCS/CampDocs/ImpressionRawFile/544363_544363_1_1351_fingerImage.png
         * RenewalDate : null
         * RegistrationDate : 05-01-2020
         */

        private String Title;
        private String patientname;
        private String MobileNo;
        private String UID;
        private String Age;
        private String DOB;
        private String Gender;
        private String PermanentAddress;
        private String LocalAddress;
        private String Pincode;
        private String IsHCRenewal;
        private String patientPhoto;
        private String HealthCardPath;
        private String HCRenewalFilePath;
        private String UserThumbPath;
        private String RenewalDate;
        private String RegistrationDate;
        private String RegistrationDate_New;
        private String ExpirtyDate;

        public String getTitle() {
            return Title;
        }

        public void setTitle(String Title) {
            this.Title = Title;
        }

        public String getPatientname() {
            return patientname;
        }

        public void setPatientname(String patientname) {
            this.patientname = patientname;
        }

        public String getMobileNo() {
            return MobileNo;
        }

        public void setMobileNo(String MobileNo) {
            this.MobileNo = MobileNo;
        }

        public String getUID() {
            if (UID != null) {
                return UID;
            } else {
                return "";
            }
        }

        public void setUID(String UID) {
            this.UID = UID;
        }

        public String getAge() {
            return Age;
        }

        public void setAge(String Age) {
            this.Age = Age;
        }

        public String getDOB() {
            if (DOB != null) {
                return DOB;
            } else {
                return "";
            }
        }

        public void setDOB(String DOB) {
            this.DOB = DOB;
        }

        public String getGender() {
            return Gender;
        }

        public void setGender(String Gender) {
            this.Gender = Gender;
        }

        public String getPermanentAddress() {
            return PermanentAddress;
        }

        public void setPermanentAddress(String PermanentAddress) {
            this.PermanentAddress = PermanentAddress;
        }

        public String getLocalAddress() {
            return LocalAddress;
        }

        public void setLocalAddress(String LocalAddress) {
            this.LocalAddress = LocalAddress;
        }

        public String getPincode() {
            return Pincode;
        }

        public void setPincode(String Pincode) {
            this.Pincode = Pincode;
        }

        public String getIsHCRenewal() {
            return IsHCRenewal;
        }

        public void setIsHCRenewal(String IsHCRenewal) {
            this.IsHCRenewal = IsHCRenewal;
        }

        public String getPatientPhoto() {
            return patientPhoto;
        }

        public void setPatientPhoto(String patientPhoto) {
            this.patientPhoto = patientPhoto;
        }

        public String getHealthCardPath() {
            return HealthCardPath;
        }

        public void setHealthCardPath(String HealthCardPath) {
            this.HealthCardPath = HealthCardPath;
        }

        public String getHCRenewalFilePath() {
            return HCRenewalFilePath;
        }

        public void setHCRenewalFilePath(String HCRenewalFilePath) {
            this.HCRenewalFilePath = HCRenewalFilePath;
        }

        public String getUserThumbPath() {
            return UserThumbPath;
        }

        public void setUserThumbPath(String UserThumbPath) {
            this.UserThumbPath = UserThumbPath;
        }

        public String getRenewalDate() {
            return RenewalDate;
        }

        public void setRenewalDate(String RenewalDate) {
            this.RenewalDate = RenewalDate;
        }

        public String getRegistrationDate() {
            return RegistrationDate;
        }

        public void setRegistrationDate(String RegistrationDate) {
            this.RegistrationDate = RegistrationDate;
        }

        public String getRegistrationDate_New() {
            if (RegistrationDate_New != null) {
                return RegistrationDate_New;
            } else {
                return "";
            }
        }

        public void setRegistrationDate_New(String registrationDate_New) {
            RegistrationDate_New = registrationDate_New;
        }

        public String getExpirtyDate() {
            if (ExpirtyDate != null) {
                return ExpirtyDate;
            } else {
                return "";
            }
        }

        public void setExpirtyDate(String expirtyDate) {
            ExpirtyDate = expirtyDate;
        }
    }
}
