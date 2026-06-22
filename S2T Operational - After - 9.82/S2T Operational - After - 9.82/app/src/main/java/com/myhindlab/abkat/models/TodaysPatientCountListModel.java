package com.myhindlab.abkat.models;

import java.util.List;

public class TodaysPatientCountListModel {

    /**
     * status : Success
     * message : Camp Details List
     * output : [{"DISTLGDCODE":471,"DISTNAME":"BHANDARA","CAMPID":0,"CampNo":0,"CampLocation":0,"CampDate":0,"CampStatus":0,"ScheduledCamps":1,"ConductedCamps":1,"FacilitatedWorkers":45,"Registration":0,"BasicDetails":45,"PhysicalExamination":42,"LungFunctioinTest":44,"AudioScreeningTest":45,"VisionScreening":44,"Barcode":44,"PPSampleCollection":43,"Ackowledgement":43,"UrineSampleCollection":44,"TotalTests":394,"TodayConductedCamps":1,"TodayFacilitatedWorkers":45},{"DISTLGDCODE":472,"DISTNAME":"BULDHANA","CAMPID":0,"CampNo":0,"CampLocation":0,"CampDate":0,"CampStatus":0,"ScheduledCamps":1,"ConductedCamps":1,"FacilitatedWorkers":21,"Registration":0,"BasicDetails":21,"PhysicalExamination":21,"LungFunctioinTest":21,"AudioScreeningTest":21,"VisionScreening":21,"Barcode":21,"PPSampleCollection":21,"Ackowledgement":21,"UrineSampleCollection":21,"TotalTests":189,"TodayConductedCamps":1,"TodayFacilitatedWorkers":21},{"DISTLGDCODE":476,"DISTNAME":"GONDIA","CAMPID":0,"CampNo":0,"CampLocation":0,"CampDate":0,"CampStatus":0,"ScheduledCamps":1,"ConductedCamps":1,"FacilitatedWorkers":51,"Registration":0,"BasicDetails":51,"PhysicalExamination":37,"LungFunctioinTest":13,"AudioScreeningTest":35,"VisionScreening":0,"Barcode":49,"PPSampleCollection":0,"Ackowledgement":0,"UrineSampleCollection":48,"TotalTests":233,"TodayConductedCamps":1,"TodayFacilitatedWorkers":51},{"DISTLGDCODE":479,"DISTNAME":"JALNA","CAMPID":0,"CampNo":0,"CampLocation":0,"CampDate":0,"CampStatus":0,"ScheduledCamps":1,"ConductedCamps":1,"FacilitatedWorkers":40,"Registration":0,"BasicDetails":40,"PhysicalExamination":37,"LungFunctioinTest":23,"AudioScreeningTest":19,"VisionScreening":37,"Barcode":28,"PPSampleCollection":0,"Ackowledgement":1,"UrineSampleCollection":0,"TotalTests":185,"TodayConductedCamps":1,"TodayFacilitatedWorkers":40},{"DISTLGDCODE":480,"DISTNAME":"KOLHAPUR","CAMPID":0,"CampNo":0,"CampLocation":0,"CampDate":0,"CampStatus":0,"ScheduledCamps":1,"ConductedCamps":1,"FacilitatedWorkers":98,"Registration":0,"BasicDetails":98,"PhysicalExamination":98,"LungFunctioinTest":0,"AudioScreeningTest":98,"VisionScreening":98,"Barcode":98,"PPSampleCollection":95,"Ackowledgement":98,"UrineSampleCollection":0,"TotalTests":683,"TodayConductedCamps":1,"TodayFacilitatedWorkers":98},{"DISTLGDCODE":482,"DISTNAME":"MUMBAI CITY","CAMPID":0,"CampNo":0,"CampLocation":0,"CampDate":0,"CampStatus":0,"ScheduledCamps":1,"ConductedCamps":1,"FacilitatedWorkers":37,"Registration":0,"BasicDetails":37,"PhysicalExamination":19,"LungFunctioinTest":0,"AudioScreeningTest":1,"VisionScreening":5,"Barcode":36,"PPSampleCollection":18,"Ackowledgement":0,"UrineSampleCollection":0,"TotalTests":116,"TodayConductedCamps":1,"TodayFacilitatedWorkers":37},{"DISTLGDCODE":485,"DISTNAME":"NANDED","CAMPID":0,"CampNo":0,"CampLocation":0,"CampDate":0,"CampStatus":0,"ScheduledCamps":1,"ConductedCamps":1,"FacilitatedWorkers":31,"Registration":0,"BasicDetails":31,"PhysicalExamination":29,"LungFunctioinTest":29,"AudioScreeningTest":31,"VisionScreening":31,"Barcode":30,"PPSampleCollection":31,"Ackowledgement":30,"UrineSampleCollection":31,"TotalTests":273,"TodayConductedCamps":1,"TodayFacilitatedWorkers":31},{"DISTLGDCODE":490,"DISTNAME":"PUNE","CAMPID":0,"CampNo":0,"CampLocation":0,"CampDate":0,"CampStatus":0,"ScheduledCamps":4,"ConductedCamps":3,"FacilitatedWorkers":81,"Registration":0,"BasicDetails":81,"PhysicalExamination":75,"LungFunctioinTest":74,"AudioScreeningTest":74,"VisionScreening":75,"Barcode":77,"PPSampleCollection":72,"Ackowledgement":58,"UrineSampleCollection":0,"TotalTests":586,"TodayConductedCamps":3,"TodayFacilitatedWorkers":81},{"DISTLGDCODE":493,"DISTNAME":"SANGLI","CAMPID":0,"CampNo":0,"CampLocation":0,"CampDate":0,"CampStatus":0,"ScheduledCamps":1,"ConductedCamps":1,"FacilitatedWorkers":127,"Registration":0,"BasicDetails":127,"PhysicalExamination":0,"LungFunctioinTest":127,"AudioScreeningTest":0,"VisionScreening":0,"Barcode":127,"PPSampleCollection":125,"Ackowledgement":126,"UrineSampleCollection":125,"TotalTests":757,"TodayConductedCamps":1,"TodayFacilitatedWorkers":127},{"DISTLGDCODE":495,"DISTNAME":"SINDHUDURG","CAMPID":0,"CampNo":0,"CampLocation":0,"CampDate":0,"CampStatus":0,"ScheduledCamps":1,"ConductedCamps":1,"FacilitatedWorkers":35,"Registration":0,"BasicDetails":35,"PhysicalExamination":5,"LungFunctioinTest":5,"AudioScreeningTest":32,"VisionScreening":32,"Barcode":23,"PPSampleCollection":32,"Ackowledgement":22,"UrineSampleCollection":22,"TotalTests":208,"TodayConductedCamps":1,"TodayFacilitatedWorkers":35},{"DISTLGDCODE":496,"DISTNAME":"SOLAPUR","CAMPID":0,"CampNo":0,"CampLocation":0,"CampDate":0,"CampStatus":0,"ScheduledCamps":1,"ConductedCamps":1,"FacilitatedWorkers":19,"Registration":0,"BasicDetails":19,"PhysicalExamination":19,"LungFunctioinTest":19,"AudioScreeningTest":19,"VisionScreening":19,"Barcode":19,"PPSampleCollection":16,"Ackowledgement":19,"UrineSampleCollection":19,"TotalTests":168,"TodayConductedCamps":1,"TodayFacilitatedWorkers":19},{"DISTLGDCODE":499,"DISTNAME":"WASHIM","CAMPID":0,"CampNo":0,"CampLocation":0,"CampDate":0,"CampStatus":0,"ScheduledCamps":1,"ConductedCamps":1,"FacilitatedWorkers":34,"Registration":0,"BasicDetails":34,"PhysicalExamination":33,"LungFunctioinTest":29,"AudioScreeningTest":31,"VisionScreening":33,"Barcode":34,"PPSampleCollection":0,"Ackowledgement":2,"UrineSampleCollection":33,"TotalTests":229,"TodayConductedCamps":1,"TodayFacilitatedWorkers":34}]
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

    public static class OutputBean implements Comparable {
        /**
         * DISTLGDCODE : 471
         * DISTNAME : BHANDARA
         * CAMPID : 0
         * CampNo : 0
         * CampLocation : 0
         * CampDate : 0
         * CampStatus : 0
         * ScheduledCamps : 1
         * ConductedCamps : 1
         * FacilitatedWorkers : 45
         * Registration : 0
         * BasicDetails : 45
         * PhysicalExamination : 42
         * LungFunctioinTest : 44
         * AudioScreeningTest : 45
         * VisionScreening : 44
         * Barcode : 44
         * PPSampleCollection : 43
         * Ackowledgement : 43
         * UrineSampleCollection : 44
         * TotalTests : 394
         * TodayConductedCamps : 1
         * TodayFacilitatedWorkers : 45
         */

        private int DISTLGDCODE;
        private String DISTNAME;
        private int CAMPID;
        private int CampNo;
        private int CampLocation;
        private int CampDate;
        private int CampStatus;
        private int ScheduledCamps;
        private int ConductedCamps;
        private int FacilitatedWorkers;
        private int Registration;
        private int BasicDetails;
        private int PhysicalExamination;
        private int LungFunctioinTest;
        private int AudioScreeningTest;
        private int VisionScreening;
        private int Barcode;
        private int PPSampleCollection;
        private int Ackowledgement;
        private int UrineSampleCollection;
        private int TotalTests;
        private int TodayConductedCamps;
        private int TodayFacilitatedWorkers;

        public int getDISTLGDCODE() {
            return DISTLGDCODE;
        }

        public void setDISTLGDCODE(int DISTLGDCODE) {
            this.DISTLGDCODE = DISTLGDCODE;
        }

        public String getDISTNAME() {
            return DISTNAME;
        }

        public void setDISTNAME(String DISTNAME) {
            this.DISTNAME = DISTNAME;
        }

        public int getCAMPID() {
            return CAMPID;
        }

        public void setCAMPID(int CAMPID) {
            this.CAMPID = CAMPID;
        }

        public int getCampNo() {
            return CampNo;
        }

        public void setCampNo(int CampNo) {
            this.CampNo = CampNo;
        }

        public int getCampLocation() {
            return CampLocation;
        }

        public void setCampLocation(int CampLocation) {
            this.CampLocation = CampLocation;
        }

        public int getCampDate() {
            return CampDate;
        }

        public void setCampDate(int CampDate) {
            this.CampDate = CampDate;
        }

        public int getCampStatus() {
            return CampStatus;
        }

        public void setCampStatus(int CampStatus) {
            this.CampStatus = CampStatus;
        }

        public int getScheduledCamps() {
            return ScheduledCamps;
        }

        public void setScheduledCamps(int ScheduledCamps) {
            this.ScheduledCamps = ScheduledCamps;
        }

        public int getConductedCamps() {
            return ConductedCamps;
        }

        public void setConductedCamps(int ConductedCamps) {
            this.ConductedCamps = ConductedCamps;
        }

        public int getFacilitatedWorkers() {
            return FacilitatedWorkers;
        }

        public void setFacilitatedWorkers(int FacilitatedWorkers) {
            this.FacilitatedWorkers = FacilitatedWorkers;
        }

        public int getRegistration() {
            return Registration;
        }

        public void setRegistration(int Registration) {
            this.Registration = Registration;
        }

        public int getBasicDetails() {
            return BasicDetails;
        }

        public void setBasicDetails(int BasicDetails) {
            this.BasicDetails = BasicDetails;
        }

        public int getPhysicalExamination() {
            return PhysicalExamination;
        }

        public void setPhysicalExamination(int PhysicalExamination) {
            this.PhysicalExamination = PhysicalExamination;
        }

        public int getLungFunctioinTest() {
            return LungFunctioinTest;
        }

        public void setLungFunctioinTest(int LungFunctioinTest) {
            this.LungFunctioinTest = LungFunctioinTest;
        }

        public int getAudioScreeningTest() {
            return AudioScreeningTest;
        }

        public void setAudioScreeningTest(int AudioScreeningTest) {
            this.AudioScreeningTest = AudioScreeningTest;
        }

        public int getVisionScreening() {
            return VisionScreening;
        }

        public void setVisionScreening(int VisionScreening) {
            this.VisionScreening = VisionScreening;
        }

        public int getBarcode() {
            return Barcode;
        }

        public void setBarcode(int Barcode) {
            this.Barcode = Barcode;
        }

        public int getPPSampleCollection() {
            return PPSampleCollection;
        }

        public void setPPSampleCollection(int PPSampleCollection) {
            this.PPSampleCollection = PPSampleCollection;
        }

        public int getAckowledgement() {
            return Ackowledgement;
        }

        public void setAckowledgement(int Ackowledgement) {
            this.Ackowledgement = Ackowledgement;
        }

        public int getUrineSampleCollection() {
            return UrineSampleCollection;
        }

        public void setUrineSampleCollection(int UrineSampleCollection) {
            this.UrineSampleCollection = UrineSampleCollection;
        }

        public int getTotalTests() {
            return TotalTests;
        }

        public void setTotalTests(int TotalTests) {
            this.TotalTests = TotalTests;
        }

        public int getTodayConductedCamps() {
            return TodayConductedCamps;
        }

        public void setTodayConductedCamps(int TodayConductedCamps) {
            this.TodayConductedCamps = TodayConductedCamps;
        }

        public int getTodayFacilitatedWorkers() {
            return TodayFacilitatedWorkers;
        }

        public void setTodayFacilitatedWorkers(int TodayFacilitatedWorkers) {
            this.TodayFacilitatedWorkers = TodayFacilitatedWorkers;
        }

        @Override
        public int compareTo(Object o) {
            int compareage = ((OutputBean) o).getTodayFacilitatedWorkers();
            return this.TodayFacilitatedWorkers - compareage;
        }
    }
}
