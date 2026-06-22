package com.myhindlab.abkat.models;

import java.util.ArrayList;

public class InstantPatientDetailsModel {

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

    public static class OutputBean {
        private String Status;
        private String RegdId;
        private String DISTLGDCODE;
        private String TALLGDCODE;
        private String RegdNo;
        private String RegdDate;
        private String MobileNo;
        private String UID;
        private String EnglishName;
        private String MaratiName;
        private String PermanentAddress;
        private String LocalAddress;
        private String BankDetails;
        private String CreatedBy;
        private String CreatedDate;
        private String ModifiedBy;
        private String ModifiedDate;
        private String UnRegWorkerId;
        private String Title;
        private String DOB;
        private String Gender;
        private String Pincode;
        private String Location;
        private String Age;
        private String FingerPrStringId;
        private String RegId;
        private String Filetype;
        private String UploadedFilePath;
        private String CreatedBy1;
        private String CreatedOn;
        private String UnRegWorkerId1;
        private String CreatedDateFormated;
        private String DOBFormated;
        private String isThumbExist;

        public String getStatus() {

            if (Status == null) {
                return "";
            } else {
                return Status;
            }
        }

        public void setStatus(String Status) {
            this.Status = Status;
        }

        public String getRegdId() {

            if (RegdId == null) {
                return "";
            } else {
                return RegdId;
            }
        }

        public void setRegdId(String RegdId) {
            this.RegdId = RegdId;
        }

        public String getDISTLGDCODE() {

            if (DISTLGDCODE == null) {
                return "";
            } else {
                return DISTLGDCODE;
            }
        }

        public void setDISTLGDCODE(String DISTLGDCODE) {
            this.DISTLGDCODE = DISTLGDCODE;
        }

        public String getTALLGDCODE() {

            if (TALLGDCODE == null) {
                return "";
            } else {
                return TALLGDCODE;
            }
        }

        public void setTALLGDCODE(String TALLGDCODE) {
            this.TALLGDCODE = TALLGDCODE;
        }

        public String getRegdNo() {

            if (RegdNo == null) {
                return "";
            } else {
                return RegdNo;
            }
        }

        public void setRegdNo(String RegdNo) {
            this.RegdNo = RegdNo;
        }

        public String getRegdDate() {

            if (RegdDate == null) {
                return "";
            } else {
                return RegdDate;
            }
        }

        public void setRegdDate(String RegdDate) {
            this.RegdDate = RegdDate;
        }

        public String getMobileNo() {

            if (MobileNo == null) {
                return "";
            } else {
                return MobileNo;
            }
        }

        public void setMobileNo(String MobileNo) {
            this.MobileNo = MobileNo;
        }

        public String getUID() {

            if (UID == null) {
                return "";
            } else {
                return UID;
            }
        }

        public void setUID(String UID) {
            this.UID = UID;
        }

        public String getEnglishName() {

            if (EnglishName == null) {
                return "";
            } else {
                return EnglishName;
            }
        }

        public void setEnglishName(String EnglishName) {
            this.EnglishName = EnglishName;
        }

        public String getMaratiName() {

            if (MaratiName == null) {
                return "";
            } else {
                return MaratiName;
            }
        }

        public void setMaratiName(String MaratiName) {
            this.MaratiName = MaratiName;
        }

        public String getPermanentAddress() {

            if (PermanentAddress == null) {
                return "";
            } else {
                return PermanentAddress;
            }
        }

        public void setPermanentAddress(String PermanentAddress) {
            this.PermanentAddress = PermanentAddress;
        }

        public String getLocalAddress() {

            if (LocalAddress == null) {
                return "";
            } else {
                return LocalAddress;
            }
        }

        public void setLocalAddress(String LocalAddress) {
            this.LocalAddress = LocalAddress;
        }

        public String getBankDetails() {

            if (BankDetails == null) {
                return "";
            } else {
                return BankDetails;
            }
        }

        public void setBankDetails(String BankDetails) {
            this.BankDetails = BankDetails;
        }

        public String getCreatedBy() {

            if (CreatedBy == null) {
                return "";
            } else {
                return CreatedBy;
            }
        }

        public void setCreatedBy(String CreatedBy) {
            this.CreatedBy = CreatedBy;
        }

        public String getCreatedDate() {

            if (CreatedDate == null) {
                return "";
            } else {
                return CreatedDate;
            }
        }

        public void setCreatedDate(String CreatedDate) {
            this.CreatedDate = CreatedDate;
        }

        public String getModifiedBy() {

            if (ModifiedBy == null) {
                return "";
            } else {
                return ModifiedBy;
            }
        }

        public void setModifiedBy(String ModifiedBy) {
            this.ModifiedBy = ModifiedBy;
        }

        public String getModifiedDate() {

            if (ModifiedDate == null) {
                return "";
            } else {
                return ModifiedDate;
            }
        }

        public void setModifiedDate(String ModifiedDate) {
            this.ModifiedDate = ModifiedDate;
        }

        public String getUnRegWorkerId() {

            if (UnRegWorkerId == null) {
                return "";
            } else {
                return UnRegWorkerId;
            }
        }

        public void setUnRegWorkerId(String UnRegWorkerId) {
            this.UnRegWorkerId = UnRegWorkerId;
        }

        public String getTitle() {

            if (Title == null) {
                return "";
            } else {
                return Title;
            }
        }

        public void setTitle(String Title) {
            this.Title = Title;
        }

        public String getDOB() {

            if (DOB == null) {
                return "";
            } else {
                return DOB;
            }
        }

        public void setDOB(String DOB) {
            this.DOB = DOB;
        }

        public String getGender() {

            if (Gender == null) {
                return "";
            } else {
                return Gender;
            }
        }

        public void setGender(String Gender) {
            this.Gender = Gender;
        }

        public String getPincode() {

            if (Pincode == null) {
                return "";
            } else {
                return Pincode;
            }
        }

        public void setPincode(String Pincode) {
            this.Pincode = Pincode;
        }

        public String getLocation() {

            if (Location == null) {
                return "";
            } else {
                return Location;
            }
        }

        public void setLocation(String Location) {
            this.Location = Location;
        }

        public String getAge() {

            if (Age == null) {
                return "";
            } else {
                return Age;
            }
        }

        public void setAge(String Age) {
            this.Age = Age;
        }

        public String getFingerPrStringId() {

            if (FingerPrStringId == null) {
                return "";
            } else {
                return FingerPrStringId;
            }
        }

        public void setFingerPrStringId(String FingerPrStringId) {
            this.FingerPrStringId = FingerPrStringId;
        }

        public String getRegId() {

            if (RegId == null) {
                return "";
            } else {
                return RegId;
            }
        }

        public void setRegId(String RegId) {
            this.RegId = RegId;
        }

        public String getFiletype() {

            if (Filetype == null) {
                return "";
            } else {
                return Filetype;
            }
        }

        public void setFiletype(String Filetype) {
            this.Filetype = Filetype;
        }

        public String getUploadedFilePath() {

            if (UploadedFilePath == null) {
                return "";
            } else {
                return UploadedFilePath;
            }
        }

        public void setUploadedFilePath(String UploadedFilePath) {
            this.UploadedFilePath = UploadedFilePath;
        }

        public String getCreatedBy1() {
            if (CreatedBy1 == null) {
                return "";
            } else {
                return CreatedBy1;
            }
        }

        public void setCreatedBy1(String CreatedBy1) {
            this.CreatedBy1 = CreatedBy1;
        }

        public String getCreatedOn() {

            if (CreatedOn == null) {
                return "";
            } else {
                return CreatedOn;
            }
        }

        public void setCreatedOn(String CreatedOn) {
            this.CreatedOn = CreatedOn;
        }

        public String getUnRegWorkerId1() {

            if (UnRegWorkerId1 == null) {
                return "";
            } else {
                return UnRegWorkerId1;
            }
        }

        public void setUnRegWorkerId1(String UnRegWorkerId1) {
            this.UnRegWorkerId1 = UnRegWorkerId1;
        }

        public String getCreatedDateFormated() {

            if (CreatedDateFormated == null) {
                return "";
            } else {
                return CreatedDateFormated;
            }
        }

        public void setCreatedDateFormated(String CreatedDateFormated) {
            this.CreatedDateFormated = CreatedDateFormated;
        }

        public String getDOBFormated() {

            if (DOBFormated == null) {
                return "";
            } else {
                return DOBFormated;
            }
        }

        public void setDOBFormated(String DOBFormated) {
            this.DOBFormated = DOBFormated;
        }

        public String getIsThumbExist() {

            if (isThumbExist == null) {
                return "";
            } else {
                return isThumbExist;
            }
        }

        public void setIsThumbExist(String isThumbExist) {
            this.isThumbExist = isThumbExist;
        }
    }
}
