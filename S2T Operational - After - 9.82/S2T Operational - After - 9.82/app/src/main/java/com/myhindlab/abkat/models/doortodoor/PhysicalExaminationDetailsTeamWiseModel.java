package com.myhindlab.abkat.models.doortodoor;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class PhysicalExaminationDetailsTeamWiseModel {

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

        @SerializedName("DISTLGDCODE")
        @Expose
        private String distlgdcode;
        @SerializedName("District")
        @Expose
        private String district;
        @SerializedName("CampId")
        @Expose
        private String campId;
        @SerializedName("CampDate")
        @Expose
        private String campDate;
        @SerializedName("TeamNo")
        @Expose
        private String teamNo;
        @SerializedName("Teamid")
        @Expose
        private String teamid;
        @SerializedName("Assigned")
        @Expose
        private Integer assigned;
        @SerializedName("DoctorID")
        @Expose
        private Integer doctorID;
        @SerializedName("CallingPending")
        @Expose
        private Integer callingPending;
        @SerializedName("PhyExamPending")
        @Expose
        private Integer phyExamPending;

        public String getDistlgdcode() {
            return distlgdcode;
        }

        public void setDistlgdcode(String distlgdcode) {
            this.distlgdcode = distlgdcode;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getCampId() {
            return campId;
        }

        public void setCampId(String campId) {
            this.campId = campId;
        }

        public String getCampDate() {
            return campDate;
        }

        public void setCampDate(String campDate) {
            this.campDate = campDate;
        }

        public String getTeamNo() {
            return teamNo;
        }

        public void setTeamNo(String teamNo) {
            this.teamNo = teamNo;
        }

        public String getTeamid() {
            return teamid;
        }

        public void setTeamid(String teamid) {
            this.teamid = teamid;
        }

        public Integer getAssigned() {
            return assigned;
        }

        public void setAssigned(Integer assigned) {
            this.assigned = assigned;
        }

        public Integer getDoctorID() {
            return doctorID;
        }

        public void setDoctorID(Integer doctorID) {
            this.doctorID = doctorID;
        }

        public Integer getCallingPending() {
            return callingPending;
        }

        public void setCallingPending(Integer callingPending) {
            this.callingPending = callingPending;
        }

        public Integer getPhyExamPending() {
            return phyExamPending;
        }

        public void setPhyExamPending(Integer phyExamPending) {
            this.phyExamPending = phyExamPending;
        }

    }


}