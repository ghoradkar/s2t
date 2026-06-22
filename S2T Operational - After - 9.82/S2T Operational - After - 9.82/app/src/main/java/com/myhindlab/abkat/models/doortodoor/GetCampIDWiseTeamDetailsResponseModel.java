package com.myhindlab.abkat.models.doortodoor;


import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetCampIDWiseTeamDetailsResponseModel {

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

        @SerializedName("TeamNumber")
        @Expose
        private String teamNumber;
        @SerializedName("CampNo")
        @Expose
        private String campNo;
        @SerializedName("CampId")
        @Expose
        private Integer campId;

        @SerializedName("TeamID")
        @Expose
        private Integer TeamId;
        @SerializedName("Member1ContactNo")
        @Expose
        private String Member1ContactNo;
        @SerializedName("Member2ContactNo")
        @Expose
        private String Member2ContactNo;
        @SerializedName("Member1")
        @Expose
        private String Member1;
 @SerializedName("Member2")
        @Expose
        private String Member2;


        public Output(String teamNumber, String campNo, Integer campId, Integer teamId, String member1ContactNo, String member2ContactNo) {
            this.teamNumber = teamNumber;
            this.campNo = campNo;
            this.campId = campId;
            TeamId = teamId;
            Member1ContactNo = member1ContactNo;
            Member2ContactNo = member2ContactNo;
        }

        public String getTeamNumber() {
            return teamNumber;
        }

        public void setTeamNumber(String teamNumber) {
            this.teamNumber = teamNumber;
        }

        public String getCampNo() {
            return campNo;
        }

        public void setCampNo(String campNo) {
            this.campNo = campNo;
        }

        public Integer getCampId() {
            return campId;
        }

        public void setCampId(Integer campId) {
            this.campId = campId;
        }


        public String getMember1ContactNo() {
            return Member1ContactNo;
        }

        public void setMember1ContactNo(String member1ContactNo) {
            Member1ContactNo = member1ContactNo;
        }

        public String getMember2ContactNo() {
            return Member2ContactNo;
        }

        public void setMember2ContactNo(String member2ContactNo) {
            Member2ContactNo = member2ContactNo;
        }

        public Integer getTeamId() {
            return TeamId;
        }

        public void setTeamId(Integer teamId) {
            TeamId = teamId;
        }

        public String getMember1() {
            return Member1;
        }

        public void setMember1(String member1) {
            Member1 = member1;
        }

        public String getMember2() {
            return Member2;
        }

        public void setMember2(String member2) {
            Member2 = member2;
        }
    }

}
