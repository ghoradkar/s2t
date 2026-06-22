package com.myhindlab.abkat.models;

import java.util.ArrayList;

public class DataEntryOperatorDetailsModel {

    private String status;
    private String message;
    private ArrayList<DataEntryOperatorDetailsModel.OutputBean> output;

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

    public ArrayList<DataEntryOperatorDetailsModel.OutputBean> getOutput() {
        return output;
    }

    public void setOutput(ArrayList<DataEntryOperatorDetailsModel.OutputBean> output) {
        this.output = output;
    }

    public static class OutputBean {


        private String    TeamID;
        private String    TeamName;
        private String    UserName;
        private String    MemberUserID1;
        private String    MemberUserID2;
        private String    Member2;
        private String    Member1;
        private String    USERID;
        private String     Campid;
        private String     CampDate;
        private String     UserID;
        private String     TeamNumber;
        private String     MemberName;
        private String     DesgId;
        private String     DesgName;

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

        public String getUSERID() {
            return USERID;
        }

        public void setUSERID(String USERID) {
            this.USERID = USERID;
        }

        public String getResourceName() {
            return ResourceName;
        }

        public void setResourceName(String resourceName) {
            ResourceName = resourceName;
        }

        private String    ResourceName;

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
