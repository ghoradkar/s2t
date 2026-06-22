package com.myhindlab.abkat.models;

import java.util.ArrayList;
import java.util.List;

public class GetTeamsModel {

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
        public String getTeamname() {
            return teamname;
        }

        public void setTeamname(String teamname) {
            this.teamname = teamname;
        }

        private String    TeamID;
        private String    teamid;

        public String getTeamid() {
            return teamid;
        }

        public void setTeamid(String teamid) {
            this.teamid = teamid;
        }

        private String    TeamName;
        private String    UserName;
        private String    MemberUserID1;
        private String    MemberUserID2;
        private String    Member2;
        private String    teamname;
        private String    Member1;
        private String    CampId;
        private String    CampType;
        private String    CampDate;


        public String getCampDate() {
            return CampDate;
        }

        public void setCampDate(String campDate) {
            CampDate = campDate;
        }

        private String    IsTeamActive;
        private String    Member1MOB;
        private String    Member2MOB;
        private String    AssignTeamName;
        private String    AssignTeamid;
        private String    IsTeamAssign;
        private String    USERNAME;
        private String    USERID;
        private String    Regdid;

        public String getRegdid() {
            return Regdid;
        }

        public void setRegdid(String regdid) {
            Regdid = regdid;
        }

        public String getUSERNAME() {
            return USERNAME;
        }

        public void setUSERNAME(String USERNAME) {
            this.USERNAME = USERNAME;
        }

        public String getUSERID() {
            return USERID;
        }

        public void setUSERID(String USERID) {
            this.USERID = USERID;
        }

        public String getAssignTeamName() {
            return AssignTeamName;
        }

        public void setAssignTeamName(String assignTeamName) {
            AssignTeamName = assignTeamName;
        }

        public String getAssignTeamid() {
            return AssignTeamid;
        }

        public void setAssignTeamid(String assignTeamid) {
            AssignTeamid = assignTeamid;
        }

        public String getIsTeamAssign() {
            return IsTeamAssign;
        }

        public void setIsTeamAssign(String isTeamAssign) {
            IsTeamAssign = isTeamAssign;
        }

        public String getMember1MOB() {
            return Member1MOB;
        }

        public void setMember1MOB(String member1MOB) {
            Member1MOB = member1MOB;
        }

        public String getMember2MOB() {
            return Member2MOB;
        }

        public void setMember2MOB(String member2MOB) {
            Member2MOB = member2MOB;
        }

        public String getCampId() {
            return CampId;
        }

        public void setCampId(String campId) {
            CampId = campId;
        }

        public String getCampType() {
            return CampType;
        }

        public void setCampType(String campType) {
            CampType = campType;
        }

        public String getIsTeamActive() {
            return IsTeamActive;
        }

        public void setIsTeamActive(String isTeamActive) {
            IsTeamActive = isTeamActive;
        }

        public String getMemberUserID1() {
            return MemberUserID1;
        }

        public void setMemberUserID1(String memberUserID1) {
            MemberUserID1 = memberUserID1;
        }

        public String getMemberUserID2() {
            return MemberUserID2;
        }

        public void setMemberUserID2(String memberUserID2) {
            MemberUserID2 = memberUserID2;
        }

        public String getMember2() {
            return Member2;
        }

        public void setMember2(String member2) {
            Member2 = member2;
        }

        public String getMember1() {
            return Member1;
        }

        public void setMember1(String member1) {
            Member1 = member1;
        }

        public String getUserName() {
            return UserName;
        }

        public void setUserName(String userName) {
            UserName = userName;
        }

        private boolean isChecked;

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        public String getTeamID() {
            return TeamID;
        }

        public void setTeamID(String teamID) {
            TeamID = teamID;
        }

        public String getTeamName() {
            return TeamName;
        }

        public void setTeamName(String teamName) {
            TeamName = teamName;
        }
    }



}
