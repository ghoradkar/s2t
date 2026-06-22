package com.myhindlab.abkat.models;

import java.util.ArrayList;

public class TeamsDetailsModel {

    private String status;
    private String message;
    private String ID;
    private ArrayList<OutputBean> output;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

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


        private String    TeamID;
        private String    TeamNumber;
        private String    TeamName;
        private String    UserName;
        private String    MemberUserID1;
        private String    MemberUserID2;
        private String    Member2;
        private String    Member1;

        private String     Campid;
        private String     CampDate;
        private String     UserID;
        private String     MemberName;
        private String     DesgId;

        private String    Teamid;

        public String getTeamid() {
            return Teamid;
        }

        public void setTeamid(String teamid) {
            Teamid = teamid;
        }

        public String getCampid() {
            return Campid;
        }

        public void setCampid(String campid) {
            Campid = campid;
        }

        public String getCampDate() {
            return CampDate;
        }

        public void setCampDate(String campDate) {
            CampDate = campDate;
        }

        public String getUserID() {
            return UserID;
        }

        public void setUserID(String userID) {
            UserID = userID;
        }

        public String getTeamNumber() {
            return TeamNumber;
        }

        public void setTeamNumber(String teamNumber) {
            TeamNumber = teamNumber;
        }

        public String getMemberName() {
            return MemberName;
        }

        public void setMemberName(String memberName) {
            MemberName = memberName;
        }

        public String getDesgId() {
            return DesgId;
        }

        public void setDesgId(String desgId) {
            DesgId = desgId;
        }

        public String getDesgName() {
            return DesgName;
        }

        public void setDesgName(String desgName) {
            DesgName = desgName;
        }

        private String     DesgName;

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
