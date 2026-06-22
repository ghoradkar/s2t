package com.myhindlab.abkat.models.doortodoor;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class AdminActiveInactiveModel {

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
        private Integer distlgdcode;
        @SerializedName("DISTNAME")
        @Expose
        private String distname;
        @SerializedName("CampCoId")
        @Expose
        private Integer campCoId;
        @SerializedName("CampCoordinatorName")
        @Expose
        private String campCoordinatorName;
        @SerializedName("TotalTeamCount")
        @Expose
        private Integer totalTeamCount;
        @SerializedName("WorkingTeamCount")
        @Expose
        private Integer workingTeamCount;
        @SerializedName("LabCode")
        @Expose
        private Integer labCode;
        @SerializedName("CampType")
        @Expose
        private Integer campType;
        @SerializedName("CampCoId1")
        @Expose
        private Object campCoId1;


        @SerializedName("Total Camps")
        @Expose
        private String totalCamps;


        @SerializedName("DataFlag")
        @Expose
        private String dataFlag;

        public String getDataFlag() {
            return dataFlag;
        }

        public void setDataFlag(String dataFlag) {
            this.dataFlag = dataFlag;
        }

        @SerializedName("Total Month's Beneficiary Count")
        @Expose
        private String TotalMonthsBeneficiaryCount;

        public String getTotalCamps() {
            return totalCamps;
        }

        public void setTotalCamps(String totalCamps) {
            this.totalCamps = totalCamps;
        }

        public String getTotalMonthsBeneficiaryCount() {
            return TotalMonthsBeneficiaryCount;
        }

        public void setTotalMonthsBeneficiaryCount(String totalMonthsBeneficiaryCount) {
            TotalMonthsBeneficiaryCount = totalMonthsBeneficiaryCount;
        }

        public String getTodayBeneficiaryCount() {
            return TodayBeneficiaryCount;
        }

        public void setTodayBeneficiaryCount(String todayBeneficiaryCount) {
            TodayBeneficiaryCount = todayBeneficiaryCount;
        }

        public String getZeroCountCamp() {
            return ZeroCountCamp;
        }

        public void setZeroCountCamp(String zeroCountCamp) {
            ZeroCountCamp = zeroCountCamp;
        }

        public String getTodaysDate() {
            return TodaysDate;
        }

        public void setTodaysDate(String todaysDate) {
            TodaysDate = todaysDate;
        }

        @SerializedName("Today's Beneficiary Count")
        @Expose
        private String TodayBeneficiaryCount;

        @SerializedName("Zero Count Camp")
        @Expose
        private String ZeroCountCamp;

        @SerializedName("Today's Date")
        @Expose
        private String TodaysDate;

        public Object getCampCoId1() {
            return campCoId1;
        }

        public void setCampCoId1(Object campCoId1) {
            this.campCoId1 = campCoId1;
        }

        public Integer getLabCode() {
            return labCode;
        }

        public void setLabCode(Integer labCode) {
            this.labCode = labCode;
        }

        public Integer getCampType() {
            return campType;
        }

        public void setCampType(Integer campType) {
            this.campType = campType;
        }

        public Integer getDivId() {
            return divId;
        }

        public void setDivId(Integer divId) {
            this.divId = divId;
        }

        public Integer getDesgId() {
            return desgId;
        }

        public void setDesgId(Integer desgId) {
            this.desgId = desgId;
        }

        @SerializedName("DivId")
        @Expose
        private Integer divId;

        @SerializedName("DesgId")
        @Expose
        private Integer desgId;

        public String getCampCoordinatorMobNo() {
            return campCoordinatorMobNo;
        }

        public void setCampCoordinatorMobNo(String campCoordinatorMobNo) {
            this.campCoordinatorMobNo = campCoordinatorMobNo;
        }

        @SerializedName("NonWorkingTeamCount")
        @Expose
        private Integer nonWorkingTeamCount;
        @SerializedName("CampCoordinatorMobNo")
        @Expose
        private String campCoordinatorMobNo;

        public Integer getDistlgdcode() {
            return distlgdcode;
        }

        public void setDistlgdcode(Integer distlgdcode) {
            this.distlgdcode = distlgdcode;
        }

        public String getDistname() {
            return distname;
        }

        public void setDistname(String distname) {
            this.distname = distname;
        }

        public Integer getCampCoId() {
            return campCoId;
        }

        public void setCampCoId(Integer campCoId) {
            this.campCoId = campCoId;
        }

        public String getCampCoordinatorName() {
            return campCoordinatorName;
        }

        public void setCampCoordinatorName(String campCoordinatorName) {
            this.campCoordinatorName = campCoordinatorName;
        }

        public Integer getTotalTeamCount() {
            return totalTeamCount;
        }

        public void setTotalTeamCount(Integer totalTeamCount) {
            this.totalTeamCount = totalTeamCount;
        }

        public Integer getWorkingTeamCount() {
            return workingTeamCount;
        }

        public void setWorkingTeamCount(Integer workingTeamCount) {
            this.workingTeamCount = workingTeamCount;
        }

        public Integer getNonWorkingTeamCount() {
            return nonWorkingTeamCount;
        }

        public void setNonWorkingTeamCount(Integer nonWorkingTeamCount) {
            this.nonWorkingTeamCount = nonWorkingTeamCount;
        }

    }
}