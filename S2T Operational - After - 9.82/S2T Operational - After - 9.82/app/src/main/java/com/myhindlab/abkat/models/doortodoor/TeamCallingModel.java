package com.myhindlab.abkat.models.doortodoor;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class TeamCallingModel implements Serializable {

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

        @SerializedName("Teamid")
        @Expose
        private Integer teamid;
        @SerializedName("TeamName")
        @Expose
        private String teamName;
        @SerializedName("UserID")
        @Expose
        private Integer userID;

        @SerializedName("RegdDate")
        @Expose
        private String regdDate;

        public String getRegdDate() {
            return regdDate;
        }

        public void setRegdDate(String regdDate) {
            this.regdDate = regdDate;
        }

        @SerializedName("CampType")
        @Expose
        private Integer campType;

        @SerializedName("CampId")
        @Expose
        private Integer campId;


        @SerializedName("RegdNo")
        @Expose
        private String regdNo;

        @SerializedName("Regdno")
        @Expose
        private String regdno;

        @SerializedName("NumberType")
        @Expose
        private String NumberType;

        public String getNumberType() {
            return NumberType;
        }

        public void setNumberType(String numberType) {
            NumberType = numberType;
        }

        public String getRegdno() {
            return regdno;
        }

        public void setRegdno(String regdno) {
            this.regdno = regdno;
        }

        @SerializedName("BeneficiaryName")
        @Expose
        private String beneficiaryName;

        @SerializedName("MemberCount")
        @Expose
        private String memberCount;


        @SerializedName("ArId")
        @Expose
        private String arId;

        public String getArId() {
            return arId;
        }

        public void setArId(String arId) {
            this.arId = arId;
        }

        public String getBeneficiaryName() {
            return beneficiaryName;
        }

        public void setBeneficiaryName(String beneficiaryName) {
            this.beneficiaryName = beneficiaryName;
        }

        public String getMemberCount() {
            return memberCount;
        }

        public void setMemberCount(String memberCount) {
            this.memberCount = memberCount;
        }

        public String getAppointmentDate() {
            return appointmentDate;
        }

        public void setAppointmentDate(String appointmentDate) {
            this.appointmentDate = appointmentDate;
        }

        @SerializedName("AppointmentDate")
        @Expose
        private String appointmentDate;





        public String getRegdNo() {
            return regdNo;
        }

        public void setRegdNo(String regdNo) {
            this.regdNo = regdNo;
        }

        public Integer getCampId() {
            return campId;
        }

        public void setCampId(Integer campId) {
            this.campId = campId;
        }

        public Integer getCampType() {
            return campType;
        }

        public void setCampType(Integer campType) {
            this.campType = campType;
        }

        public Integer getUserID() {
            return userID;
        }

        public void setUserID(Integer userID) {
            this.userID = userID;
        }

        public String getMemberName() {
            return memberName;
        }

        public void setMemberName(String memberName) {
            this.memberName = memberName;
        }

        public String getMobno() {
            return mobno;
        }

        public void setMobno(String mobno) {
            this.mobno = mobno;
        }

        @SerializedName("MemberName")
        @Expose
        private String memberName;

        public String getTeamNumberName() {
            return teamNumberName;
        }

        public void setTeamNumberName(String teamNumberName) {
            this.teamNumberName = teamNumberName;
        }

        @SerializedName("TeamNumberName")
        @Expose
        private String teamNumberName;
        @SerializedName("MOBNO")
        @Expose
        private String mobno;
        @SerializedName("MobileNo")
        @Expose
        private String mobileNo;

        public String getMobileNo() {
            return mobileNo;
        }

        public void setMobileNo(String mobileNo) {
            this.mobileNo = mobileNo;
        }

        @SerializedName("DesgName")
        @Expose
        private String desgName;

        public String getDesgName() {
            return desgName;
        }

        public void setDesgName(String desgName) {
            this.desgName = desgName;
        }

        public String getDesgShortCode() {
            return desgShortCode;
        }

        public void setDesgShortCode(String desgShortCode) {
            this.desgShortCode = desgShortCode;
        }

        @SerializedName("DesgShortCode")
        @Expose
        private String desgShortCode;

        public Integer getTeamid() {
            return teamid;
        }

        public void setTeamid(Integer teamid) {
            this.teamid = teamid;
        }

        public String getTeamName() {
            return teamName;
        }

        public void setTeamName(String teamName) {
            this.teamName = teamName;
        }




    }

}