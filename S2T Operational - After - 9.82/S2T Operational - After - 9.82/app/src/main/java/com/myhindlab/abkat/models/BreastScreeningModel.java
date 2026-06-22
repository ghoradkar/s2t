package com.myhindlab.abkat.models;

import java.util.List;

public class BreastScreeningModel {


    /**
     * status : Success
     * message : Breast Screnning Details List
     * output : [{"BsID":2,"RegdId":488324,"CancerHisComment":"dbjsjsj","PSObservation":0,"PSComment":"jdjsjsja","LumpObservation":0,"FinalRemark":"Normal","ReportPhoto":"47189_BS.png","CreatedBy":1292,"CreatedOn":"/Date(1578921492980)/","ModifiedBy":null,"ModifiedOn":null},{"BsID":3,"RegdId":544821,"CancerHisComment":"dhdhdg","PSObservation":0,"PSComment":"","LumpObservation":0,"FinalRemark":"To be referred","ReportPhoto":"","CreatedBy":1292,"CreatedOn":"/Date(1578922008810)/","ModifiedBy":null,"ModifiedOn":null},{"BsID":4,"RegdId":544852,"CancerHisComment":"gshh","PSObservation":1,"PSComment":"it's clear","LumpObservation":0,"FinalRemark":"Normal","ReportPhoto":"17774_BS.png","CreatedBy":1219,"CreatedOn":"/Date(1578995446173)/","ModifiedBy":null,"ModifiedOn":null},{"BsID":5,"RegdId":544820,"CancerHisComment":"thhshshh","PSObservation":0,"PSComment":"","LumpObservation":1,"FinalRemark":"To be referred","ReportPhoto":"3814_BS.png","CreatedBy":1219,"CreatedOn":"/Date(1578995984683)/","ModifiedBy":null,"ModifiedOn":null},{"BsID":6,"RegdId":488304,"CancerHisComment":"hhh","PSObservation":1,"PSComment":"eereer","LumpObservation":1,"FinalRemark":"To be referred","ReportPhoto":"90221_BS.png","CreatedBy":1219,"CreatedOn":"/Date(1578996266370)/","ModifiedBy":null,"ModifiedOn":null}]
     */

    private String status;
    private String message;
    private List<OutputBean> output;

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

    public List<OutputBean> getOutput() {
        return output;
    }

    public void setOutput(List<OutputBean> output) {
        this.output = output;
    }

    public static class OutputBean {
        /**
         * BsID : 2
         * RegdId : 488324
         * CancerHisComment : dbjsjsj
         * PSObservation : 0
         * PSComment : jdjsjsja
         * LumpObservation : 0
         * FinalRemark : Normal
         * ReportPhoto : 47189_BS.png
         * CreatedBy : 1292
         * CreatedOn : /Date(1578921492980)/
         * ModifiedBy : null
         * ModifiedOn : null
         */

        private String BsID;
        private String RegdId;
        private String CancerHisComment;
        private String PSObservation;
        private String PSComment;
        private String LumpObservation;
        private String FinalRemark;
        private String ReportPhoto;
        private String CreatedBy;
        private String CreatedOn;
        private String ModifiedBy;
        private String ModifiedOn;

        public String getBsID() {
            return BsID;
        }

        public void setBsID(String BsID) {
            this.BsID = BsID;
        }

        public String getRegdId() {
            return RegdId;
        }

        public void setRegdId(String RegdId) {
            this.RegdId = RegdId;
        }

        public String getCancerHisComment() {
            return CancerHisComment;
        }

        public void setCancerHisComment(String CancerHisComment) {
            this.CancerHisComment = CancerHisComment;
        }

        public String getPSObservation() {
            return PSObservation;
        }

        public void setPSObservation(String PSObservation) {
            this.PSObservation = PSObservation;
        }

        public String getPSComment() {
            return PSComment;
        }

        public void setPSComment(String PSComment) {
            this.PSComment = PSComment;
        }

        public String getLumpObservation() {
            return LumpObservation;
        }

        public void setLumpObservation(String LumpObservation) {
            this.LumpObservation = LumpObservation;
        }

        public String getFinalRemark() {
            return FinalRemark;
        }

        public void setFinalRemark(String FinalRemark) {
            this.FinalRemark = FinalRemark;
        }

        public String getReportPhoto() {
            return ReportPhoto;
        }

        public void setReportPhoto(String ReportPhoto) {
            this.ReportPhoto = ReportPhoto;
        }

        public String getCreatedBy() {
            return CreatedBy;
        }

        public void setCreatedBy(String CreatedBy) {
            this.CreatedBy = CreatedBy;
        }

        public String getCreatedOn() {
            return CreatedOn;
        }

        public void setCreatedOn(String CreatedOn) {
            this.CreatedOn = CreatedOn;
        }

        public String getModifiedBy() {
            return ModifiedBy;
        }

        public void setModifiedBy(String ModifiedBy) {
            this.ModifiedBy = ModifiedBy;
        }

        public String getModifiedOn() {
            return ModifiedOn;
        }

        public void setModifiedOn(String ModifiedOn) {
            this.ModifiedOn = ModifiedOn;
        }
    }
}
