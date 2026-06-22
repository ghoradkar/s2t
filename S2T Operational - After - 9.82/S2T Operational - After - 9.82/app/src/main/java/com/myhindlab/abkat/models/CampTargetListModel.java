package com.myhindlab.abkat.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CampTargetListModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("output")
    @Expose
    private List<Output> output = null;

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

        @SerializedName("Distlgdcode")
        @Expose
        private Integer distlgdcode;
        @SerializedName("DistrictName")
        @Expose
        private String districtName;
        @SerializedName("MonthlyTarget")
        @Expose
        private Integer monthlyTarget;
        @SerializedName("DailyTarget")
        @Expose
        private Integer dailyTarget;
        @SerializedName("TotalMonthlyTargetAchived")
        @Expose
        private Integer totalMonthlyTargetAchived;
        @SerializedName("CurrentDateTargetAchived")
        @Expose
        private Integer currentDateTargetAchived;

        public Integer getDistlgdcode() {
            return distlgdcode;
        }

        public void setDistlgdcode(Integer distlgdcode) {
            this.distlgdcode = distlgdcode;
        }

        public String getDistrictName() {
            return districtName;
        }

        public void setDistrictName(String districtName) {
            this.districtName = districtName;
        }

        public Integer getMonthlyTarget() {
            return monthlyTarget;
        }

        public void setMonthlyTarget(Integer monthlyTarget) {
            this.monthlyTarget = monthlyTarget;
        }

        public Integer getDailyTarget() {
            return dailyTarget;
        }

        public void setDailyTarget(Integer dailyTarget) {
            this.dailyTarget = dailyTarget;
        }

        public Integer getTotalMonthlyTargetAchived() {
            return totalMonthlyTargetAchived;
        }

        public void setTotalMonthlyTargetAchived(Integer totalMonthlyTargetAchived) {
            this.totalMonthlyTargetAchived = totalMonthlyTargetAchived;
        }

        public Integer getCurrentDateTargetAchived() {
            return currentDateTargetAchived;
        }

        public void setCurrentDateTargetAchived(Integer currentDateTargetAchived) {
            this.currentDateTargetAchived = currentDateTargetAchived;
        }

    }
}
