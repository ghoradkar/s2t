package com.myhindlab.abkat.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetCampCloseDetailsModel {

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

        @SerializedName("CloseCampID")
        @Expose
        private Integer closeCampID;
        @SerializedName("CampID")
        @Expose
        private Integer campID;
        @SerializedName("CampCloseUserid")
        @Expose
        private Integer campCloseUserid;
        @SerializedName("CampCloseDate")
        @Expose
        private String campCloseDate;
        @SerializedName("CampCloseUpdatedBy")
        @Expose
        private Object campCloseUpdatedBy;
        @SerializedName("CampCloseUpdatedDate")
        @Expose
        private Object campCloseUpdatedDate;
        @SerializedName("ConsumeID")
        @Expose
        private Integer consumeID;
        @SerializedName("CloseCampID1")
        @Expose
        private Integer closeCampID1;
        @SerializedName("CampID1")
        @Expose
        private Integer campID1;
        @SerializedName("PlainTubeCount")
        @Expose
        private Integer plainTubeCount;
        @SerializedName("GelTubeCount")
        @Expose
        private Integer gelTubeCount;
        @SerializedName("FlorideCount")
        @Expose
        private Integer florideCount;
        @SerializedName("UrineContainer")
        @Expose
        private Integer urineContainer;
        @SerializedName("OtherRemark")
        @Expose
        private String otherRemark;
        @SerializedName("CreatedBy")
        @Expose
        private Integer createdBy;
        @SerializedName("CreatedDate")
        @Expose
        private String createdDate;
        @SerializedName("UpdatedBy")
        @Expose
        private Object updatedBy;
        @SerializedName("UpdatedDate")
        @Expose
        private Object updatedDate;
        @SerializedName("CloseSummaryID")
        @Expose
        private Integer closeSummaryID;
        @SerializedName("CloseCampID2")
        @Expose
        private Integer closeCampID2;
        @SerializedName("CampID2")
        @Expose
        private Integer campID2;
        @SerializedName("TotalBenificiary")
        @Expose
        private Integer totalBenificiary;
        @SerializedName("SampleCollectionCount")
        @Expose
        private Integer sampleCollectionCount;
        @SerializedName("SampleSendToHubLabCount")
        @Expose
        private Integer sampleSendToHubLabCount;
        @SerializedName("SampleSendToHomeLabCount")
        @Expose
        private Integer sampleSendToHomeLabCount;
        @SerializedName("OtherRemark1")
        @Expose
        private String otherRemark1;
        @SerializedName("CreatedBy1")
        @Expose
        private Integer createdBy1;
        @SerializedName("CreatedDate1")
        @Expose
        private String createdDate1;
        @SerializedName("UpdatedBy1")
        @Expose
        private Object updatedBy1;
        @SerializedName("UpdatedDate1")
        @Expose
        private Object updatedDate1;

        public Integer getCloseCampID() {
            return closeCampID;
        }

        public void setCloseCampID(Integer closeCampID) {
            this.closeCampID = closeCampID;
        }

        public Integer getCampID() {
            return campID;
        }

        public void setCampID(Integer campID) {
            this.campID = campID;
        }

        public Integer getCampCloseUserid() {
            return campCloseUserid;
        }

        public void setCampCloseUserid(Integer campCloseUserid) {
            this.campCloseUserid = campCloseUserid;
        }

        public String getCampCloseDate() {
            return campCloseDate;
        }

        public void setCampCloseDate(String campCloseDate) {
            this.campCloseDate = campCloseDate;
        }

        public Object getCampCloseUpdatedBy() {
            return campCloseUpdatedBy;
        }

        public void setCampCloseUpdatedBy(Object campCloseUpdatedBy) {
            this.campCloseUpdatedBy = campCloseUpdatedBy;
        }

        public Object getCampCloseUpdatedDate() {
            return campCloseUpdatedDate;
        }

        public void setCampCloseUpdatedDate(Object campCloseUpdatedDate) {
            this.campCloseUpdatedDate = campCloseUpdatedDate;
        }

        public Integer getConsumeID() {
            return consumeID;
        }

        public void setConsumeID(Integer consumeID) {
            this.consumeID = consumeID;
        }

        public Integer getCloseCampID1() {
            return closeCampID1;
        }

        public void setCloseCampID1(Integer closeCampID1) {
            this.closeCampID1 = closeCampID1;
        }

        public Integer getCampID1() {
            return campID1;
        }

        public void setCampID1(Integer campID1) {
            this.campID1 = campID1;
        }

        public Integer getPlainTubeCount() {
            return plainTubeCount;
        }

        public void setPlainTubeCount(Integer plainTubeCount) {
            this.plainTubeCount = plainTubeCount;
        }

        public Integer getGelTubeCount() {
            return gelTubeCount;
        }

        public void setGelTubeCount(Integer gelTubeCount) {
            this.gelTubeCount = gelTubeCount;
        }

        public Integer getFlorideCount() {
            return florideCount;
        }

        public void setFlorideCount(Integer florideCount) {
            this.florideCount = florideCount;
        }

        public Integer getUrineContainer() {
            return urineContainer;
        }

        public void setUrineContainer(Integer urineContainer) {
            this.urineContainer = urineContainer;
        }

        public String getOtherRemark() {
            return otherRemark;
        }

        public void setOtherRemark(String otherRemark) {
            this.otherRemark = otherRemark;
        }

        public Integer getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(Integer createdBy) {
            this.createdBy = createdBy;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

        public Object getUpdatedBy() {
            return updatedBy;
        }

        public void setUpdatedBy(Object updatedBy) {
            this.updatedBy = updatedBy;
        }

        public Object getUpdatedDate() {
            return updatedDate;
        }

        public void setUpdatedDate(Object updatedDate) {
            this.updatedDate = updatedDate;
        }

        public Integer getCloseSummaryID() {
            return closeSummaryID;
        }

        public void setCloseSummaryID(Integer closeSummaryID) {
            this.closeSummaryID = closeSummaryID;
        }

        public Integer getCloseCampID2() {
            return closeCampID2;
        }

        public void setCloseCampID2(Integer closeCampID2) {
            this.closeCampID2 = closeCampID2;
        }

        public Integer getCampID2() {
            return campID2;
        }

        public void setCampID2(Integer campID2) {
            this.campID2 = campID2;
        }

        public Integer getTotalBenificiary() {
            return totalBenificiary;
        }

        public void setTotalBenificiary(Integer totalBenificiary) {
            this.totalBenificiary = totalBenificiary;
        }

        public Integer getSampleCollectionCount() {
            return sampleCollectionCount;
        }

        public void setSampleCollectionCount(Integer sampleCollectionCount) {
            this.sampleCollectionCount = sampleCollectionCount;
        }

        public Integer getSampleSendToHubLabCount() {
            return sampleSendToHubLabCount;
        }

        public void setSampleSendToHubLabCount(Integer sampleSendToHubLabCount) {
            this.sampleSendToHubLabCount = sampleSendToHubLabCount;
        }

        public Integer getSampleSendToHomeLabCount() {
            return sampleSendToHomeLabCount;
        }

        public void setSampleSendToHomeLabCount(Integer sampleSendToHomeLabCount) {
            this.sampleSendToHomeLabCount = sampleSendToHomeLabCount;
        }

        public String getOtherRemark1() {
            return otherRemark1;
        }

        public void setOtherRemark1(String otherRemark1) {
            this.otherRemark1 = otherRemark1;
        }

        public Integer getCreatedBy1() {
            return createdBy1;
        }

        public void setCreatedBy1(Integer createdBy1) {
            this.createdBy1 = createdBy1;
        }

        public String getCreatedDate1() {
            return createdDate1;
        }

        public void setCreatedDate1(String createdDate1) {
            this.createdDate1 = createdDate1;
        }

        public Object getUpdatedBy1() {
            return updatedBy1;
        }

        public void setUpdatedBy1(Object updatedBy1) {
            this.updatedBy1 = updatedBy1;
        }

        public Object getUpdatedDate1() {
            return updatedDate1;
        }

        public void setUpdatedDate1(Object updatedDate1) {
            this.updatedDate1 = updatedDate1;
        }

    }
}
