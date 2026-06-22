package com.myhindlab.abkat.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class InvoiceDashboardModel implements Serializable {

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

    public static class OutputBean implements Serializable {
        /**
         * DISTLGDCODE : 466
         * DISTNAME : AHMEDNAGAR
         * CampTarget : 0
         * Post Camps Completed : 0
         * Post Camps Pending : 2
         * Total Camps : 2
         * RegisteredWorkers : 140
         * ActualWorkers : 135
         * ApplicableForBilling : 135
         * PendingWorkers : 0
         * TotalServices : 3645
         * ServiceDone : 3645
         * PendingService : 0
         * Post Camp Readiness : READY FOR BILLING
         */

        private int DISTLGDCODE;
        private String DISTNAME;
        private int CampTarget;
        @SerializedName("Post Camps Completed")
        private int PostCampsCompleted; // FIXME check this code
        @SerializedName("Post Camps Pending")
        private int PostCampsPending; // FIXME check this code
        @SerializedName("Total Camps")
        private int TotalCamps; // FIXME check this code
        private int RegisteredWorkers;
        private int ActualWorkers;
        private int ApplicableForBilling;
        private int PendingWorkers;
        private int TotalServices;
        private int ServiceDone;
        private int PendingService;
        @SerializedName("Post Camp Readiness")
        private String PostCampReadiness; // FIXME check this code

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

        public int getCampTarget() {
            return CampTarget;
        }

        public void setCampTarget(int CampTarget) {
            this.CampTarget = CampTarget;
        }

        public int getRegisteredWorkers() {
            return RegisteredWorkers;
        }

        public void setRegisteredWorkers(int RegisteredWorkers) {
            this.RegisteredWorkers = RegisteredWorkers;
        }

        public int getActualWorkers() {
            return ActualWorkers;
        }

        public void setActualWorkers(int ActualWorkers) {
            this.ActualWorkers = ActualWorkers;
        }

        public int getApplicableForBilling() {
            return ApplicableForBilling;
        }

        public void setApplicableForBilling(int ApplicableForBilling) {
            this.ApplicableForBilling = ApplicableForBilling;
        }

        public int getPendingWorkers() {
            return PendingWorkers;
        }

        public void setPendingWorkers(int PendingWorkers) {
            this.PendingWorkers = PendingWorkers;
        }

        public int getTotalServices() {
            return TotalServices;
        }

        public void setTotalServices(int TotalServices) {
            this.TotalServices = TotalServices;
        }

        public int getServiceDone() {
            return ServiceDone;
        }

        public void setServiceDone(int ServiceDone) {
            this.ServiceDone = ServiceDone;
        }

        public int getPendingService() {
            return PendingService;
        }

        public void setPendingService(int PendingService) {
            this.PendingService = PendingService;
        }

        public int getPostCampsCompleted() {
            return PostCampsCompleted;
        }

        public void setPostCampsCompleted(int postCampsCompleted) {
            PostCampsCompleted = postCampsCompleted;
        }

        public int getPostCampsPending() {
            return PostCampsPending;
        }

        public void setPostCampsPending(int postCampsPending) {
            PostCampsPending = postCampsPending;
        }

        public int getTotalCamps() {
            return TotalCamps;
        }

        public void setTotalCamps(int totalCamps) {
            TotalCamps = totalCamps;
        }

        public String getPostCampReadiness() {
            return PostCampReadiness;
        }

        public void setPostCampReadiness(String postCampReadiness) {
            PostCampReadiness = postCampReadiness;
        }
    }
}
