package com.myhindlab.abkat.models;

import java.util.List;

public class MainScreenCountsModel {


    /**
     * status : Success
     * message : Registration List
     * output : [{"ScheduledCamps":26,"ConductedCamps":18,"RegisteredWorkers":56649,"FacilitatedWorkers":1988,"TotalTests":0,"ReportedTests":1,"TestsInProgress":2,"NormalPatient":1093,"RefeeredPatient":67,"Critical":3,"ActualPatient":1163}]
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
         * ScheduledCamps : 26
         * ConductedCamps : 18
         * RegisteredWorkers : 56649
         * FacilitatedWorkers : 1988
         * TotalTests : 0
         * ReportedTests : 1
         * TestsInProgress : 2
         * NormalPatient : 1093
         * RefeeredPatient : 67
         * Critical : 3
         * ActualPatient : 1163
         */

        private int ScheduledCamps;
        private int ConductedCamps;
        private int RegisteredWorkers;
        private int FacilitatedWorkers;
        private int TotalTests;
        private int ReportedTests;
        private int TestsInProgress;
        private int NormalPatient;
        private int RefeeredPatient;
        private int Critical;
        private int ActualPatient;

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

        public int getRegisteredWorkers() {
            return RegisteredWorkers;
        }

        public void setRegisteredWorkers(int RegisteredWorkers) {
            this.RegisteredWorkers = RegisteredWorkers;
        }

        public int getFacilitatedWorkers() {
            return FacilitatedWorkers;
        }

        public void setFacilitatedWorkers(int FacilitatedWorkers) {
            this.FacilitatedWorkers = FacilitatedWorkers;
        }

        public int getTotalTests() {
            return TotalTests;
        }

        public void setTotalTests(int TotalTests) {
            this.TotalTests = TotalTests;
        }

        public int getReportedTests() {
            return ReportedTests;
        }

        public void setReportedTests(int ReportedTests) {
            this.ReportedTests = ReportedTests;
        }

        public int getTestsInProgress() {
            return TestsInProgress;
        }

        public void setTestsInProgress(int TestsInProgress) {
            this.TestsInProgress = TestsInProgress;
        }

        public int getNormalPatient() {
            return NormalPatient;
        }

        public void setNormalPatient(int NormalPatient) {
            this.NormalPatient = NormalPatient;
        }

        public int getRefeeredPatient() {
            return RefeeredPatient;
        }

        public void setRefeeredPatient(int RefeeredPatient) {
            this.RefeeredPatient = RefeeredPatient;
        }

        public int getCritical() {
            return Critical;
        }

        public void setCritical(int Critical) {
            this.Critical = Critical;
        }

        public int getActualPatient() {
            return ActualPatient;
        }

        public void setActualPatient(int ActualPatient) {
            this.ActualPatient = ActualPatient;
        }


    }
}
