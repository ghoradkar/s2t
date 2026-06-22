package com.myhindlab.abkat.models.doortodoor;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class D2dWorkingTeamModel {

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

        @SerializedName("AppointmentDateCount")
        @Expose
        private Integer appointmentDateCount;

        public Integer getAppointmentDateCount() {
            return appointmentDateCount;
        }

        public void setAppointmentDateCount(Integer appointmentDateCount) {
            this.appointmentDateCount = appointmentDateCount;
        }

        @SerializedName("TeamName")
        @Expose
        private String teamName;
        @SerializedName("Member1")
        @Expose
        private String member1;
        @SerializedName("Member2")
        @Expose
        private String member2;
        @SerializedName("RegBeneficieries")
        @Expose
        private Integer regBeneficieries;

        public Integer getCampId() {
            return campId;
        }

        public void setCampId(Integer campId) {
            this.campId = campId;
        }

        @SerializedName("CampId")
        @Expose
        private Integer campId;

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

        public String getMember1() {
            return member1;
        }

        public void setMember1(String member1) {
            this.member1 = member1;
        }

        public String getMember2() {
            return member2;
        }

        public void setMember2(String member2) {
            this.member2 = member2;
        }

        public Integer getRegBeneficieries() {
            return regBeneficieries;
        }

        public void setRegBeneficieries(Integer regBeneficieries) {
            this.regBeneficieries = regBeneficieries;
        }

    }

}