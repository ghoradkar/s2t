package com.myhindlab.abkat.models.doortodoor.d2d_camp_activity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TeamDetailsByCampIDResponseModel {

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

        @SerializedName("Campid")
        @Expose
        private Integer campid;
        @SerializedName("CampNo")
        @Expose
        private String campNo;
        @SerializedName("CampDate")
        @Expose
        private String campDate;
        @SerializedName("TeamNumber")
        @Expose
        private String teamNumber;
        @SerializedName("TeamName")
        @Expose
        private String teamName;
        @SerializedName("Member1")
        @Expose
        private String member1;
        @SerializedName("Member2")
        @Expose
        private String member2;
        @SerializedName("DISTNAME")
        @Expose
        private String distname;

        public Integer getCampid() {
            return campid;
        }

        public void setCampid(Integer campid) {
            this.campid = campid;
        }

        public String getCampNo() {
            return campNo;
        }

        public void setCampNo(String campNo) {
            this.campNo = campNo;
        }

        public String getCampDate() {
            return campDate;
        }

        public void setCampDate(String campDate) {
            this.campDate = campDate;
        }

        public String getTeamNumber() {
            return teamNumber;
        }

        public void setTeamNumber(String teamNumber) {
            this.teamNumber = teamNumber;
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

        public String getDistname() {
            return distname;
        }

        public void setDistname(String distname) {
            this.distname = distname;
        }

    }

}