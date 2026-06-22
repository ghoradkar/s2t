package com.myhindlab.abkat.models.doortodoor;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


    public class TeamNumberModel {

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
            @SerializedName("Campid")
            @Expose
            private Integer campid;
            @SerializedName("UserID")
            @Expose
            private Integer userID;


            public String getTeamName() {
                return teamName;
            }

            public void setTeamName(String teamName) {
                this.teamName = teamName;
            }

            @SerializedName("TeamName")
            @Expose
            private String teamName;

            public String getTeamNumber() {
                return teamNumber;
            }

            public void setTeamNumber(String teamNumber) {
                this.teamNumber = teamNumber;
            }

            public Integer getCampid() {
                return campid;
            }

            public void setCampid(Integer campid) {
                this.campid = campid;
            }

            public Integer getUserID() {
                return userID;
            }

            public void setUserID(Integer userID) {
                this.userID = userID;
            }

        }
    }