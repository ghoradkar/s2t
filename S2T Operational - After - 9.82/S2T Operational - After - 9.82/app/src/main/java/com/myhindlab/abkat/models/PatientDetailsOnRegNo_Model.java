package com.myhindlab.abkat.models;

public class PatientDetailsOnRegNo_Model {
    private String PermanentAddress;

    private String TALLGDCODE;

    private String RegdId;

    private String Gender;

    private String ModifiedBy;

    private String ModifiedDate;

    private String DISTLGDCODE;

    private String DOB;

    private String Age;

    private String MobileNo;

    private String CreatedDateFormated;

    private String LocalAddress;

    private String CreatedBy;

    private String RegdDate;

    private String MaratiName;

    private String BankDetails;

    private String DOBFormated;

    private String Title;

    private String UID;

    private String CreatedDate;

    private String UnRegWorkerId;

    private String RegdNo;

    private String Pincode;

    private String EnglishName;

    private String Location;

    private String isThumbExist;

    public String getPermanentAddress() {
        return PermanentAddress;
    }

    public void setPermanentAddress(String PermanentAddress) {
        this.PermanentAddress = PermanentAddress;
    }

    public String getTALLGDCODE() {
        return TALLGDCODE;
    }

    public void setTALLGDCODE(String TALLGDCODE) {
        this.TALLGDCODE = TALLGDCODE;
    }

    public String getRegdId() {
        return RegdId;
    }

    public void setRegdId(String RegdId) {
        this.RegdId = RegdId;
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

    public String getModifiedBy() {
        return ModifiedBy;
    }

    public void setModifiedBy(String ModifiedBy) {
        this.ModifiedBy = ModifiedBy;
    }

    public String getModifiedDate() {
        return ModifiedDate;
    }

    public void setModifiedDate(String ModifiedDate) {
        this.ModifiedDate = ModifiedDate;
    }

    public String getDISTLGDCODE() {
        return DISTLGDCODE;
    }

    public void setDISTLGDCODE(String DISTLGDCODE) {
        this.DISTLGDCODE = DISTLGDCODE;
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

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String MobileNo) {
        this.MobileNo = MobileNo;
    }

    public String getCreatedDateFormated() {
        return CreatedDateFormated;
    }

    public void setCreatedDateFormated(String CreatedDateFormated) {
        this.CreatedDateFormated = CreatedDateFormated;
    }

    public String getLocalAddress() {
        return LocalAddress;
    }

    public void setLocalAddress(String LocalAddress) {
        this.LocalAddress = LocalAddress;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String CreatedBy) {
        this.CreatedBy = CreatedBy;
    }

    public String getRegdDate() {
        return RegdDate;
    }

    public void setRegdDate(String RegdDate) {
        this.RegdDate = RegdDate;
    }

    public String getMaratiName() {
        return MaratiName;
    }

    public void setMaratiName(String MaratiName) {
        this.MaratiName = MaratiName;
    }

    public String getBankDetails() {
        return BankDetails;
    }

    public void setBankDetails(String BankDetails) {
        this.BankDetails = BankDetails;
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

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String CreatedDate) {
        this.CreatedDate = CreatedDate;
    }

    public String getUnRegWorkerId() {
        return UnRegWorkerId;
    }

    public void setUnRegWorkerId(String UnRegWorkerId) {
        this.UnRegWorkerId = UnRegWorkerId;
    }

    public String getRegdNo() {
        return RegdNo;
    }

    public void setRegdNo(String RegdNo) {
        this.RegdNo = RegdNo;
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

    public String getEnglishName() {
        return EnglishName;
    }

    public void setEnglishName(String EnglishName) {
        this.EnglishName = EnglishName;
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

    public String getIsThumbExist() {
        if (isThumbExist == null) {
            return "0";
        } else {
            return isThumbExist;
        }
    }

    public void setIsThumbExist(String isThumbExist) {
        this.isThumbExist = isThumbExist;
    }
}
