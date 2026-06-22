package com.myhindlab.abkat.models;


import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserAttendanceModel implements Serializable {

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

    public class Output implements Serializable {

        @SerializedName("AttendanceFlag")
        @Expose
        private Integer AttendanceFlag;
        @SerializedName("CampFlag")
        @Expose
        private Integer CampFlag;
        @SerializedName("ReadinessFlag")
        @Expose
        private Integer ReadinessFlag;
        @SerializedName("LocationFlag")
        @Expose
        private Integer LocationFlag;


        public Integer getIsOldCampClosed() {
            return isOldCampClosed;
        }

        public void setIsOldCampClosed(Integer isOldCampClosed) {
            this.isOldCampClosed = isOldCampClosed;
        }

        @SerializedName("IsOldCampClosed")
        @Expose
        private Integer isOldCampClosed;

        @SerializedName("TeamMemberAttendance")
        @Expose
        private Integer teamMemberAttendance;

        public Integer getTeamMemberAttendance() {
            return teamMemberAttendance;
        }

        public void setTeamMemberAttendance(Integer teamMemberAttendance) {
            this.teamMemberAttendance = teamMemberAttendance;
        }

        public Integer getTestFlag() {
            return testFlag;
        }

        public void setTestFlag(Integer testFlag) {
            this.testFlag = testFlag;
        }

        @SerializedName("CampClosingFlag")
        @Expose
        private Integer CampClosingFlag;
        @SerializedName("TestFlag")
        @Expose
        private Integer testFlag;

        public Integer getIsCampClosed() {
            return isCampClosed;
        }

        public void setIsCampClosed(Integer isCampClosed) {
            this.isCampClosed = isCampClosed;
        }

        @SerializedName("IsCampClosed")
        @Expose
        private Integer isCampClosed;

        public Integer getIsReadinessFormFilled() {
            return isReadinessFormFilled;
        }

        public void setIsReadinessFormFilled(Integer isReadinessFormFilled) {
            this.isReadinessFormFilled = isReadinessFormFilled;
        }

        @SerializedName("IsReadinessFormFilled")
        @Expose
        private Integer isReadinessFormFilled;

        public Integer getAttendanceFlag() {
            return AttendanceFlag;
        }

        public void setAttendanceFlag(Integer attendanceFlag) {
            AttendanceFlag = attendanceFlag;
        }

        public Integer getCampFlag() {
            return CampFlag;
        }

        public void setCampFlag(Integer campFlag) {
            CampFlag = campFlag;
        }

        public Integer getReadinessFlag() {
            return ReadinessFlag;
        }

        public void setReadinessFlag(Integer readinessFlag) {
            ReadinessFlag = readinessFlag;
        }

        public Integer getLocationFlag() {
            return LocationFlag;
        }

        public void setLocationFlag(Integer locationFlag) {
            LocationFlag = locationFlag;
        }

        public Integer getCampClosingFlag() {
            return CampClosingFlag;
        }

        public void setCampClosingFlag(Integer campClosingFlag) {
            CampClosingFlag = campClosingFlag;
        }
    }
}

