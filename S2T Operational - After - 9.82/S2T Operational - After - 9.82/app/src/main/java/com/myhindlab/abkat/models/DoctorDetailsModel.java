package com.myhindlab.abkat.models;

import java.util.ArrayList;
import java.util.List;

public class DoctorDetailsModel {

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
        private String USERID;

        private String Name;

        private String        Campid;
        private String        CampDate;
        private String        UserID;
        private String        TeamNumber;
        private String        MemberName;
        private String        IsActive;

        public String getIsActive() {
            return IsActive;
        }

        public void setIsActive(String isActive) {
            IsActive = isActive;
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

        private String        DesgId;
        private String        DesgName;

        private boolean isChecked;

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        public String getUSERID() {
            return USERID;
        }

        public void setUSERID(String USERID) {
            this.USERID = USERID;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }
    }
}
