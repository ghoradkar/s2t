package com.myhindlab.abkat.models;

import java.util.List;

public class SampleProcessingPatientModel {


    /**
     * status : Success
     * message : Result
     * output : [{"DISTNAME":"AKOLA","CampId":233,"CampNo":"CAMP_467_0000002","CampLocation":"Unnamed Road, Sopinath Nagar, Bhaurad, Maharashtra 444002, India","CampDate":"08-Sep-2019","CampName":"Bhaurad Atal Camp","RegdId":491944,"RegdNo":252520013979,"RegdDate":"/Date(1567881000000)/","MobileNo":"","UID":"","EnglishName":"Santosh","MaratiName":null,"PermanentAddress":"Bhourad","LocalAddress":"Akola","UnRegWorkerId":null,"Title":"Mr.","DOB":"/Date(-2209008600000)/","Gender":"M","Pincode":"444002","Location":null,"Age":30,"STATUS":"REJECTED"},{"DISTNAME":"AKOLA","CampId":233,"CampNo":"CAMP_467_0000002","CampLocation":"Unnamed Road, Sopinath Nagar, Bhaurad, Maharashtra 444002, India","CampDate":"08-Sep-2019","CampName":"Bhaurad Atal Camp","RegdId":491951,"RegdNo":596380380963,"RegdDate":"/Date(1567881000000)/","MobileNo":"","UID":"","EnglishName":"Santosh Chadurkar","MaratiName":null,"PermanentAddress":"Bhourad","LocalAddress":"Akola","UnRegWorkerId":null,"Title":"Mr.","DOB":"/Date(-2209008600000)/","Gender":"M","Pincode":"444005","Location":null,"Age":46,"STATUS":"REJECTED"},{"DISTNAME":"AKOLA","CampId":233,"CampNo":"CAMP_467_0000002","CampLocation":"Unnamed Road, Sopinath Nagar, Bhaurad, Maharashtra 444002, India","CampDate":"08-Sep-2019","CampName":"Bhaurad Atal Camp","RegdId":491969,"RegdNo":252520013919,"RegdDate":"/Date(1567881000000)/","MobileNo":"","UID":"","EnglishName":"Santosh Pawar","MaratiName":null,"PermanentAddress":"Bhourad","LocalAddress":"Akola","UnRegWorkerId":null,"Title":"Mr.","DOB":"/Date(-2209008600000)/","Gender":"M","Pincode":"444002","Location":null,"Age":30,"STATUS":"REJECTED"}]
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
         * DISTNAME : AKOLA
         * CampId : 233
         * CampNo : CAMP_467_0000002
         * CampLocation : Unnamed Road, Sopinath Nagar, Bhaurad, Maharashtra 444002, India
         * CampDate : 08-Sep-2019
         * CampName : Bhaurad Atal Camp
         * RegdId : 491944
         * RegdNo : 252520013979
         * RegdDate : /Date(1567881000000)/
         * MobileNo :
         * UID :
         * EnglishName : Santosh
         * MaratiName : null
         * PermanentAddress : Bhourad
         * LocalAddress : Akola
         * UnRegWorkerId : null
         * Title : Mr.
         * DOB : /Date(-2209008600000)/
         * Gender : M
         * Pincode : 444002
         * Location : null
         * Age : 30
         * STATUS : REJECTED
         */

        private String DISTNAME;
        private int CampId;
        private String CampNo;
        private String CampLocation;
        private String CampDate;
        private String CampName;
        private String OrderId;
        private int RegdId;
        private long RegdNo;
        private String RegdDate;
        private String MobileNo;
        private String UID;
        private String EnglishName;
        private Object MaratiName;
        private String PermanentAddress;
        private String LocalAddress;
        private Object UnRegWorkerId;
        private String Title;
        private String DOB;
        private String Gender;
        private String Pincode;
        private Object Location;
        private int Age;
        private String STATUS;

        public String getDISTNAME() {
            return DISTNAME;
        }

        public void setDISTNAME(String DISTNAME) {
            this.DISTNAME = DISTNAME;
        }

        public int getCampId() {
            return CampId;
        }

        public void setCampId(int CampId) {
            this.CampId = CampId;
        }

        public String getCampNo() {
            return CampNo;
        }

        public void setCampNo(String CampNo) {
            this.CampNo = CampNo;
        }

        public String getCampLocation() {
            return CampLocation;
        }

        public void setCampLocation(String CampLocation) {
            this.CampLocation = CampLocation;
        }

        public String getCampDate() {
            return CampDate;
        }

        public void setCampDate(String CampDate) {
            this.CampDate = CampDate;
        }

        public String getCampName() {
            return CampName;
        }

        public void setCampName(String CampName) {
            this.CampName = CampName;
        }

        public String getOrderId() {
            return OrderId;
        }

        public void setOrderId(String orderId) {
            OrderId = orderId;
        }

        public int getRegdId() {
            return RegdId;
        }

        public void setRegdId(int RegdId) {
            this.RegdId = RegdId;
        }

        public long getRegdNo() {
            return RegdNo;
        }

        public void setRegdNo(long RegdNo) {
            this.RegdNo = RegdNo;
        }

        public String getRegdDate() {
            return RegdDate;
        }

        public void setRegdDate(String RegdDate) {
            this.RegdDate = RegdDate;
        }

        public String getMobileNo() {
            return MobileNo;
        }

        public void setMobileNo(String MobileNo) {
            this.MobileNo = MobileNo;
        }

        public String getUID() {
            return UID;
        }

        public void setUID(String UID) {
            this.UID = UID;
        }

        public String getEnglishName() {
            return EnglishName;
        }

        public void setEnglishName(String EnglishName) {
            this.EnglishName = EnglishName;
        }

        public Object getMaratiName() {
            return MaratiName;
        }

        public void setMaratiName(Object MaratiName) {
            this.MaratiName = MaratiName;
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

        public Object getUnRegWorkerId() {
            return UnRegWorkerId;
        }

        public void setUnRegWorkerId(Object UnRegWorkerId) {
            this.UnRegWorkerId = UnRegWorkerId;
        }

        public String getTitle() {
            return Title;
        }

        public void setTitle(String Title) {
            this.Title = Title;
        }

        public String getDOB() {
            return DOB;
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

        public String getPincode() {
            return Pincode;
        }

        public void setPincode(String Pincode) {
            this.Pincode = Pincode;
        }

        public Object getLocation() {
            return Location;
        }

        public void setLocation(Object Location) {
            this.Location = Location;
        }

        public int getAge() {
            return Age;
        }

        public void setAge(int Age) {
            this.Age = Age;
        }

        public String getSTATUS() {
            return STATUS;
        }

        public void setSTATUS(String STATUS) {
            this.STATUS = STATUS;
        }
    }
}
