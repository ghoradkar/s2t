package com.myhindlab.abkat.models;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NestedCampListModel implements Serializable{

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

        @SerializedName("CampId")
        @Expose
        private Integer campId;
        @SerializedName("CampNo")
        @Expose
        private String campNo;
        @SerializedName("CampTypeDescription")
        @Expose
        private String campTypeDescription;
        @SerializedName("CampLocation")
        @Expose
        private String campLocation;
        @SerializedName("CampDate")
        @Expose
        private String campDate;
        @SerializedName("CampStatus")
        @Expose
        private String campStatus;
        @SerializedName("Status")
        @Expose
        private String status;
        @SerializedName("Description")
        @Expose
        private String description;
        @SerializedName("DISTLGDCODE")
        @Expose
        private Integer distlgdcode;
        @SerializedName("DISTNAME")
        @Expose
        private String distname;
        @SerializedName("REGISTERWORKERS")
        @Expose
        private Integer registerworkers;
        @SerializedName("SurveyCoordinatorName")
        @Expose
        private String surveyCoordinatorName;
        @SerializedName("CordinatorName")
        @Expose
        private String cordinatorName;
        @SerializedName("MOBNO")
        @Expose
        private String mobno;
        @SerializedName("CampName")
        @Expose
        private String campName;
        @SerializedName("Total")
        @Expose
        private Integer total;
        @SerializedName("ScreeningDone")
        @Expose
        private Integer screeningDone;
        @SerializedName("ScreeningNotDone")
        @Expose
        private Integer screeningNotDone;

        public Integer getCampId() {
            return campId;
        }

        public void setCampId(Integer campId) {
            this.campId = campId;
        }

        public String getCampNo() {
            return campNo;
        }

        public void setCampNo(String campNo) {
            this.campNo = campNo;
        }

        public String getCampTypeDescription() {
            return campTypeDescription;
        }

        public void setCampTypeDescription(String campTypeDescription) {
            this.campTypeDescription = campTypeDescription;
        }

        public String getCampLocation() {
            return campLocation;
        }

        public void setCampLocation(String campLocation) {
            this.campLocation = campLocation;
        }

        public String getCampDate() {
            return campDate;
        }

        public void setCampDate(String campDate) {
            this.campDate = campDate;
        }

        public String getCampStatus() {
            return campStatus;
        }

        public void setCampStatus(String campStatus) {
            this.campStatus = campStatus;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

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

        public Integer getRegisterworkers() {
            return registerworkers;
        }

        public void setRegisterworkers(Integer registerworkers) {
            this.registerworkers = registerworkers;
        }

        public String getSurveyCoordinatorName() {
            return surveyCoordinatorName;
        }

        public void setSurveyCoordinatorName(String surveyCoordinatorName) {
            this.surveyCoordinatorName = surveyCoordinatorName;
        }

        public String getCordinatorName() {
            return cordinatorName;
        }

        public void setCordinatorName(String cordinatorName) {
            this.cordinatorName = cordinatorName;
        }

        public String getMobno() {
            return mobno;
        }

        public void setMobno(String mobno) {
            this.mobno = mobno;
        }

        public String getCampName() {
            return campName;
        }

        public void setCampName(String campName) {
            this.campName = campName;
        }

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }

        public Integer getScreeningDone() {
            return screeningDone;
        }

        public void setScreeningDone(Integer screeningDone) {
            this.screeningDone = screeningDone;
        }

        public Integer getScreeningNotDone() {
            return screeningNotDone;
        }

        public void setScreeningNotDone(Integer screeningNotDone) {
            this.screeningNotDone = screeningNotDone;
        }

    }
}
