package com.myhindlab.abkat.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class PostCampReadinessModel implements Serializable {


    /**
     * status : Success
     * message : Invoice And Post Camp Details
     * output : [{"DISTLGDCODE":490,"DISTNAME":"PUNE","CampId":517,"CampNo":"CAMP_490_0000148","CampDate":"01-Dec-2019","CampLocation":"A1, Sambhaji Nagar Rd, Vidya Vihar, Sambhajinagar, Chinchwad, Pimpri-Chinchwad, Maharashtra 411019, ","actualpostcampdate":null,"RegisteredWorkers":218,"ActualWorkers":216,"ApplicableForBilling":214,"PendingWorkers":113,"TotalServices":5859,"ServiceDone":5854,"PendingService":0,"Post Camp Status":"Post Camp Pending","Readiness Status":"0%"},{"DISTLGDCODE":490,"DISTNAME":"PUNE","CampId":505,"CampNo":"CAMP_490_0000146","CampDate":"03-Dec-2019","CampLocation":"Sutarwadi Baner Gaon Rd, Baner, Pune, Maharashtra 411045, India","actualpostcampdate":null,"RegisteredWorkers":14,"ActualWorkers":14,"ApplicableForBilling":14,"PendingWorkers":113,"TotalServices":378,"ServiceDone":378,"PendingService":0,"Post Camp Status":"Post Camp Pending","Readiness Status":"100%"},{"DISTLGDCODE":490,"DISTNAME":"PUNE","CampId":446,"CampNo":"CAMP_490_0000139","CampDate":"04-Dec-2019","CampLocation":"Junnar-Yenere Rd, Yenere, Maharashtra 410502, India","actualpostcampdate":null,"RegisteredWorkers":104,"ActualWorkers":103,"ApplicableForBilling":102,"PendingWorkers":113,"TotalServices":2727,"ServiceDone":2704,"PendingService":0,"Post Camp Status":"Post Camp Pending","Readiness Status":"0%"},{"DISTLGDCODE":490,"DISTNAME":"PUNE","CampId":448,"CampNo":"CAMP_490_0000140","CampDate":"05-Dec-2019","CampLocation":"Royal Pride, At post Yenere, Tal- Junnar, Yenere, Maharashtra 410502, India","actualpostcampdate":null,"RegisteredWorkers":111,"ActualWorkers":111,"ApplicableForBilling":108,"PendingWorkers":113,"TotalServices":2997,"ServiceDone":2950,"PendingService":0,"Post Camp Status":"Post Camp Pending","Readiness Status":"0%"},{"DISTLGDCODE":490,"DISTNAME":"PUNE","CampId":458,"CampNo":"CAMP_490_0000144","CampDate":"06-Dec-2019","CampLocation":"57, Hills & Dales, Undri, Pune, Maharashtra 411028, India","actualpostcampdate":null,"RegisteredWorkers":34,"ActualWorkers":34,"ApplicableForBilling":34,"PendingWorkers":113,"TotalServices":918,"ServiceDone":918,"PendingService":0,"Post Camp Status":"Post Camp Pending","Readiness Status":"100%"},{"DISTLGDCODE":490,"DISTNAME":"PUNE","CampId":544,"CampNo":"CAMP_490_0000150","CampDate":"07-Dec-2019","CampLocation":"Chikhali - Kaspate Vasti BRTS Rd, Park Street, Wakad, Pimpri-Chinchwad, Maharashtra 411057, India","actualpostcampdate":null,"RegisteredWorkers":10,"ActualWorkers":10,"ApplicableForBilling":10,"PendingWorkers":113,"TotalServices":270,"ServiceDone":270,"PendingService":0,"Post Camp Status":"Post Camp Pending","Readiness Status":"100%"},{"DISTLGDCODE":490,"DISTNAME":"PUNE","CampId":550,"CampNo":"CAMP_490_0000152","CampDate":"10-Dec-2019","CampLocation":"2271, Navi Ali, Bhor, Maharashtra 412206, India","actualpostcampdate":null,"RegisteredWorkers":105,"ActualWorkers":105,"ApplicableForBilling":93,"PendingWorkers":113,"TotalServices":2835,"ServiceDone":2823,"PendingService":0,"Post Camp Status":"Post Camp Pending","Readiness Status":"0%"},{"DISTLGDCODE":490,"DISTNAME":"PUNE","CampId":560,"CampNo":"CAMP_490_0000153","CampDate":"14-Dec-2019","CampLocation":"Durga&P.K Classes, Sambhaji Nagar Rd, Sambhajinagar, Chinchwad, Pimpri-Chinchwad, Maharashtra 411019","actualpostcampdate":null,"RegisteredWorkers":177,"ActualWorkers":172,"ApplicableForBilling":162,"PendingWorkers":113,"TotalServices":4698,"ServiceDone":4666,"PendingService":0,"Post Camp Status":"Post Camp Pending","Readiness Status":"0%"},{"DISTLGDCODE":490,"DISTNAME":"PUNE","CampId":601,"CampNo":"CAMP_490_0000155","CampDate":"15-Dec-2019","CampLocation":"Unnamed Road, Maharashtra 410511, India","actualpostcampdate":null,"RegisteredWorkers":96,"ActualWorkers":96,"ApplicableForBilling":94,"PendingWorkers":113,"TotalServices":2592,"ServiceDone":2546,"PendingService":0,"Post Camp Status":"Post Camp Pending","Readiness Status":"0%"},{"DISTLGDCODE":490,"DISTNAME":"PUNE","CampId":549,"CampNo":"CAMP_490_0000151","CampDate":"17-Dec-2019","CampLocation":"411014, Ubale Nagar, Sainagar, Kharadi, Pune, Maharashtra 412207, India","actualpostcampdate":null,"RegisteredWorkers":68,"ActualWorkers":67,"ApplicableForBilling":54,"PendingWorkers":113,"TotalServices":1809,"ServiceDone":1763,"PendingService":0,"Post Camp Status":"Post Camp Pending","Readiness Status":"0%"},{"DISTLGDCODE":490,"DISTNAME":"PUNE","CampId":602,"CampNo":"CAMP_490_0000156","CampDate":"19-Dec-2019","CampLocation":"\u200bPune - Pandharpur Rd, Walhe, Maharashtra 412305, India","actualpostcampdate":null,"RegisteredWorkers":0,"ActualWorkers":0,"ApplicableForBilling":0,"PendingWorkers":113,"TotalServices":0,"ServiceDone":0,"PendingService":0,"Post Camp Status":"Post Camp Pending","Readiness Status":"0%"},{"DISTLGDCODE":490,"DISTNAME":"PUNE","CampId":561,"CampNo":"CAMP_490_0000154","CampDate":"22-Dec-2019","CampLocation":"RH20, shambhajiNagar, Chinchwad, Pimpri-Chinchwad, Maharashtra 411019, India","actualpostcampdate":null,"RegisteredWorkers":52,"ActualWorkers":52,"ApplicableForBilling":50,"PendingWorkers":113,"TotalServices":1404,"ServiceDone":1381,"PendingService":0,"Post Camp Status":"Post Camp Pending","Readiness Status":"0%"},{"DISTLGDCODE":490,"DISTNAME":"PUNE","CampId":779,"CampNo":"CAMP_100_0000099","CampDate":"28-Dec-2019","CampLocation":"Unnamed Road, Pimpri-Chinchwad, Maharashtra 410506, India","actualpostcampdate":null,"RegisteredWorkers":113,"ActualWorkers":113,"ApplicableForBilling":0,"PendingWorkers":113,"TotalServices":3051,"ServiceDone":0,"PendingService":3051,"Post Camp Status":"Post Camp Pending","Readiness Status":"0%"}]
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

    public static class OutputBean implements Serializable{
        /**
         * DISTLGDCODE : 490
         * DISTNAME : PUNE
         * CampId : 517
         * CampNo : CAMP_490_0000148
         * CampDate : 01-Dec-2019
         * CampLocation : A1, Sambhaji Nagar Rd, Vidya Vihar, Sambhajinagar, Chinchwad, Pimpri-Chinchwad, Maharashtra 411019,
         * actualpostcampdate : null
         * RegisteredWorkers : 218
         * ActualWorkers : 216
         * ApplicableForBilling : 214
         * PendingWorkers : 113
         * TotalServices : 5859
         * ServiceDone : 5854
         * PendingService : 0
         * Post Camp Status : Post Camp Pending
         * Readiness Status : 0%
         */

        private int DISTLGDCODE;
        private String DISTNAME;
        private int CampId;
        private String CampNo;
        private String CampDate;
        private String CampLocation;
        private String actualpostcampdate;
        private int RegisteredWorkers;
        private int ActualWorkers;
        private int ApplicableForBilling;
        private int PendingWorkers;
        private int TotalServices;
        private int ServiceDone;
        private int PendingService;
        private String CampName;
        @SerializedName("Post Camp Status")
        private String PostCampStatus; // FIXME check this code
        @SerializedName("Readiness Status")
        private String ReadinessStatus; // FIXME check this code
        @SerializedName("Readiness StatusId")
        private String ReadinessStatusId; // FIXME check this code

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

        public int getCampId() {
            return CampId;
        }

        public void setCampId(int CampId) {
            this.CampId = CampId;
        }

        public String getCampNo() {
            return CampNo;
        }

        public void setCampNo(String CampNo) {
            this.CampNo = CampNo;
        }

        public String getCampDate() {
            return CampDate;
        }

        public void setCampDate(String CampDate) {
            this.CampDate = CampDate;
        }

        public String getCampLocation() {
            return CampLocation;
        }

        public void setCampLocation(String CampLocation) {
            this.CampLocation = CampLocation;
        }

        public String getActualpostcampdate() {
            return actualpostcampdate;
        }

        public void setActualpostcampdate(String actualpostcampdate) {
            this.actualpostcampdate = actualpostcampdate;
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

        public String getCampName() {
            return CampName;
        }

        public void setCampName(String campName) {
            CampName = campName;
        }

        public String getPostCampStatus() {
            return PostCampStatus;
        }

        public void setPostCampStatus(String postCampStatus) {
            this.PostCampStatus = postCampStatus;
        }

        public String getReadinessStatus() {
            return ReadinessStatus;
        }

        public void setReadinessStatus(String readinessStatus) {
            this.ReadinessStatus = readinessStatus;
        }

        public String getReadinessStatusId() {
            return ReadinessStatusId;
        }

        public void setReadinessStatusId(String readinessStatusId) {
            ReadinessStatusId = readinessStatusId;
        }
    }
}
