package com.myhindlab.abkat.models;

import java.util.List;

public class BreastScreeningCampWiseModel {


    /**
     * status : Success
     * message : Breast Screening Details
     * output : [{"DISTLGDCODE":485,"DISTNAME":"NANDED","CAMPID":545,"CAMPDATE":"05-Dec-2019","CampNo":"CAMP_485_0000012","CampName":"narsi camp","CampLocation":"MH SH 225, Narsi, Maharashtra 431722, India","TOTAL_PATIENTS":0,"POSITIVE_PATIENTS":0,"NEGATIVE_PATIENTS":0,"NO_STATUS_PATIENTS":0},{"DISTLGDCODE":485,"DISTNAME":"NANDED","CAMPID":565,"CAMPDATE":"08-Dec-2019","CampNo":"CAMP_485_0000013","CampName":"khadakpura ","CampLocation":"Near Water Tank, Lalwadi Road, Khadakpura, Nanded, Maharashtra 431601, India","TOTAL_PATIENTS":0,"POSITIVE_PATIENTS":0,"NEGATIVE_PATIENTS":0,"NO_STATUS_PATIENTS":0},{"DISTLGDCODE":485,"DISTNAME":"NANDED","CAMPID":566,"CAMPDATE":"08-Dec-2019","CampNo":"CAMP_485_0000014","CampName":"degloor naka","CampLocation":"Unnamed Road, Deglour Naka, Nanded, Maharashtra 431601, India","TOTAL_PATIENTS":0,"POSITIVE_PATIENTS":0,"NEGATIVE_PATIENTS":0,"NO_STATUS_PATIENTS":0},{"DISTLGDCODE":485,"DISTNAME":"NANDED","CAMPID":567,"CAMPDATE":"08-Dec-2019","CampNo":"CAMP_485_0000015","CampName":"madha colony","CampLocation":"Unnamed Road, MHADA Colony, Nanded, Maharashtra 431603, India","TOTAL_PATIENTS":0,"POSITIVE_PATIENTS":0,"NEGATIVE_PATIENTS":0,"NO_STATUS_PATIENTS":0},{"DISTLGDCODE":485,"DISTNAME":"NANDED","CAMPID":583,"CAMPDATE":"09-Dec-2019","CampNo":"CAMP_485_0000016","CampName":"darga road","CampLocation":"Dargah Rd, Kandhar, Maharashtra 431714, India","TOTAL_PATIENTS":0,"POSITIVE_PATIENTS":0,"NEGATIVE_PATIENTS":0,"NO_STATUS_PATIENTS":0},{"DISTLGDCODE":485,"DISTNAME":"NANDED","CAMPID":865,"CAMPDATE":"10-Jan-2020","CampNo":"CAMP_100_0000185","CampName":"chandola camp","CampLocation":"NH63, Ashoknagar, Mukhed, Maharashtra 431715, India","TOTAL_PATIENTS":0,"POSITIVE_PATIENTS":0,"NEGATIVE_PATIENTS":0,"NO_STATUS_PATIENTS":0},{"DISTLGDCODE":485,"DISTNAME":"NANDED","CAMPID":883,"CAMPDATE":"12-Jan-2020","CampNo":"CAMP_100_0000203","CampName":"PARVANA NAGAR","CampLocation":"Vitthal Rukmini Mandir Rd, Ashtvinayak Nagar, Nanded, Maharashtra 431605, India","TOTAL_PATIENTS":0,"POSITIVE_PATIENTS":0,"NEGATIVE_PATIENTS":0,"NO_STATUS_PATIENTS":0},{"DISTLGDCODE":485,"DISTNAME":"NANDED","CAMPID":686,"CAMPDATE":"15-Dec-2019","CampNo":"CAMP_100_0000006","CampName":"KhupsarWadi","CampLocation":"Unnamed Road, Khupsarwadi, Maharashtra 431606, India","TOTAL_PATIENTS":0,"POSITIVE_PATIENTS":0,"NEGATIVE_PATIENTS":0,"NO_STATUS_PATIENTS":0},{"DISTLGDCODE":485,"DISTNAME":"NANDED","CAMPID":920,"CAMPDATE":"15-Jan-2020","CampNo":"CAMP_100_0000240","CampName":"MIDC NANDED CAMP","CampLocation":"MIDC Rd, MIDC, MIDC Nandad, Nanded, Maharashtra 431603, India","TOTAL_PATIENTS":0,"POSITIVE_PATIENTS":0,"NEGATIVE_PATIENTS":0,"NO_STATUS_PATIENTS":0},{"DISTLGDCODE":485,"DISTNAME":"NANDED","CAMPID":928,"CAMPDATE":"16-Jan-2020","CampNo":"CAMP_100_0000248","CampName":"MIDC NANDED CAMP","CampLocation":"NH161, MIDC Nandad, Nanded, Maharashtra 431603, India","TOTAL_PATIENTS":9,"POSITIVE_PATIENTS":0,"NEGATIVE_PATIENTS":0,"NO_STATUS_PATIENTS":9},{"DISTLGDCODE":485,"DISTNAME":"NANDED","CAMPID":938,"CAMPDATE":"17-Jan-2020","CampNo":"CAMP_100_0000258","CampName":"midc nanded","CampLocation":"NH161, Dhanegaon, Nanded, Maharashtra 431603, India","TOTAL_PATIENTS":0,"POSITIVE_PATIENTS":0,"NEGATIVE_PATIENTS":0,"NO_STATUS_PATIENTS":0},{"DISTLGDCODE":485,"DISTNAME":"NANDED","CAMPID":952,"CAMPDATE":"18-Jan-2020","CampNo":"CAMP_100_0000272","CampName":"MIDC","CampLocation":"NH161, Dhanegaon, Nanded, Maharashtra 431603, India","TOTAL_PATIENTS":39,"POSITIVE_PATIENTS":0,"NEGATIVE_PATIENTS":0,"NO_STATUS_PATIENTS":39},{"DISTLGDCODE":485,"DISTNAME":"NANDED","CAMPID":968,"CAMPDATE":"19-Jan-2020","CampNo":"CAMP_100_0000288","CampName":"BHAKTI LANS","CampLocation":"Kalpnakar Chowk Bus Stop, Nanded Malegaon Rd, Ashtvinayak Nagar, Nanded, Maharashtra 431605, India","TOTAL_PATIENTS":9,"POSITIVE_PATIENTS":0,"NEGATIVE_PATIENTS":0,"NO_STATUS_PATIENTS":9},{"DISTLGDCODE":485,"DISTNAME":"NANDED","CAMPID":969,"CAMPDATE":"19-Jan-2020","CampNo":"CAMP_100_0000289","CampName":"MIDC CAMP","CampLocation":"121, CIDCO Colony, Nanded, Maharashtra 431603, India","TOTAL_PATIENTS":13,"POSITIVE_PATIENTS":0,"NEGATIVE_PATIENTS":0,"NO_STATUS_PATIENTS":13},{"DISTLGDCODE":485,"DISTNAME":"NANDED","CAMPID":315,"CAMPDATE":"19-Sep-2019","CampNo":"CAMP_485_0000001","CampName":"ambedkar nagar","CampLocation":"Ambedkar nagar, Ganesh Nagar Rd, Nanded, Maharashtra 431602, India","TOTAL_PATIENTS":0,"POSITIVE_PATIENTS":0,"NEGATIVE_PATIENTS":0,"NO_STATUS_PATIENTS":0},{"DISTLGDCODE":485,"DISTNAME":"NANDED","CAMPID":983,"CAMPDATE":"20-Jan-2020","CampNo":"CAMP_100_0000303","CampName":"MIDC CAMP","CampLocation":"Chandasindh Corner, Tuppa, MIDC Nandad, Nanded, Maharashtra 431603, India","TOTAL_PATIENTS":3,"POSITIVE_PATIENTS":0,"NEGATIVE_PATIENTS":0,"NO_STATUS_PATIENTS":3},{"DISTLGDCODE":485,"DISTNAME":"NANDED","CAMPID":984,"CAMPDATE":"20-Jan-2020","CampNo":"CAMP_100_0000304","CampName":"BHAKTI LONS","CampLocation":"Shivaji Rd, Vivek Nagar, Kohinor City, Shri Nagar, Nanded, Maharashtra 431605, India","TOTAL_PATIENTS":1,"POSITIVE_PATIENTS":0,"NEGATIVE_PATIENTS":0,"NO_STATUS_PATIENTS":1},{"DISTLGDCODE":485,"DISTNAME":"NANDED","CAMPID":1001,"CAMPDATE":"21-Jan-2020","CampNo":"CAMP_100_0000321","CampName":"MIDC NANDED","CampLocation":"Cidco Main Rd, CIDCO Colony, Nanded, Maharashtra 431603, India","TOTAL_PATIENTS":1,"POSITIVE_PATIENTS":0,"NEGATIVE_PATIENTS":0,"NO_STATUS_PATIENTS":1},{"DISTLGDCODE":485,"DISTNAME":"NANDED","CAMPID":1002,"CAMPDATE":"21-Jan-2020","CampNo":"CAMP_100_0000322","CampName":"BHAKTI LONS","CampLocation":"Kalpnakar Chowk Bus Stop, Nanded Malegaon Rd, Ashtvinayak Nagar, Nanded, Maharashtra 431605, India","TOTAL_PATIENTS":0,"POSITIVE_PATIENTS":0,"NEGATIVE_PATIENTS":0,"NO_STATUS_PATIENTS":0},{"DISTLGDCODE":485,"DISTNAME":"NANDED","CAMPID":1019,"CAMPDATE":"22-Jan-2020","CampNo":"CAMP_100_0000339","CampName":"MIDC NANDED","CampLocation":"NH161, MIDC Nandad, Nanded, Maharashtra 431603, India","TOTAL_PATIENTS":28,"POSITIVE_PATIENTS":0,"NEGATIVE_PATIENTS":0,"NO_STATUS_PATIENTS":28},{"DISTLGDCODE":485,"DISTNAME":"NANDED","CAMPID":1021,"CAMPDATE":"22-Jan-2020","CampNo":"CAMP_100_0000341","CampName":"BHAKTI LONS","CampLocation":"Doctors Plaza, Dr Lane Rd Number 1, Doctor Lane, Khadakpura, Nanded, Maharashtra 431601, India","TOTAL_PATIENTS":0,"POSITIVE_PATIENTS":0,"NEGATIVE_PATIENTS":0,"NO_STATUS_PATIENTS":0},{"DISTLGDCODE":485,"DISTNAME":"NANDED","CAMPID":1037,"CAMPDATE":"23-Jan-2020","CampNo":"CAMP_100_0000357","CampName":"MIDC NANDED","CampLocation":"MIDC Rd, MIDC, MIDC Nandad, Nanded, Maharashtra 431603, India","TOTAL_PATIENTS":6,"POSITIVE_PATIENTS":0,"NEGATIVE_PATIENTS":0,"NO_STATUS_PATIENTS":6},{"DISTLGDCODE":485,"DISTNAME":"NANDED","CAMPID":1041,"CAMPDATE":"23-Jan-2020","CampNo":"CAMP_100_0000361","CampName":"bhakti lons","CampLocation":"Kalpnakar Chowk Bus Stop, Nanded Malegaon Rd, Ashtvinayak Nagar, Nanded, Maharashtra 431605, India","TOTAL_PATIENTS":0,"POSITIVE_PATIENTS":0,"NEGATIVE_PATIENTS":0,"NO_STATUS_PATIENTS":0},{"DISTLGDCODE":485,"DISTNAME":"NANDED","CAMPID":478,"CAMPDATE":"23-Nov-2019","CampNo":"CAMP_485_0000003","CampName":"Degloor","CampLocation":"NH161, Siddhart Nagar, Degloor, Maharashtra 431717, India","TOTAL_PATIENTS":0,"POSITIVE_PATIENTS":0,"NEGATIVE_PATIENTS":0,"NO_STATUS_PATIENTS":0},{"DISTLGDCODE":485,"DISTNAME":"NANDED","CAMPID":1061,"CAMPDATE":"24-Jan-2020","CampNo":"CAMP_100_0000381","CampName":"bhakti lons","CampLocation":"Taroda Bk Rd, Balaji Nagar, Nanded, Maharashtra 431605, India","TOTAL_PATIENTS":0,"POSITIVE_PATIENTS":0,"NEGATIVE_PATIENTS":0,"NO_STATUS_PATIENTS":0},{"DISTLGDCODE":485,"DISTNAME":"NANDED","CAMPID":490,"CAMPDATE":"24-Nov-2019","CampNo":"CAMP_485_0000005","CampName":"Balirampur","CampLocation":"Unnamed Road, Tirupatinagar, Nanded, Maharashtra 431603, India","TOTAL_PATIENTS":0,"POSITIVE_PATIENTS":0,"NEGATIVE_PATIENTS":0,"NO_STATUS_PATIENTS":0},{"DISTLGDCODE":485,"DISTNAME":"NANDED","CAMPID":1059,"CAMPDATE":"25-Jan-2020","CampNo":"CAMP_100_0000379","CampName":"midc nanded","CampLocation":"Chandasindh Corner, Tuppa, MIDC Nandad, Nanded, Maharashtra 431603, India","TOTAL_PATIENTS":18,"POSITIVE_PATIENTS":0,"NEGATIVE_PATIENTS":0,"NO_STATUS_PATIENTS":18},{"DISTLGDCODE":485,"DISTNAME":"NANDED","CAMPID":1082,"CAMPDATE":"25-Jan-2020","CampNo":"CAMP_100_0000402","CampName":"bhakti lons","CampLocation":"NH161, Dhanegaon, Nanded, Maharashtra 431603, India","TOTAL_PATIENTS":0,"POSITIVE_PATIENTS":0,"NEGATIVE_PATIENTS":0,"NO_STATUS_PATIENTS":0},{"DISTLGDCODE":485,"DISTNAME":"NANDED","CAMPID":493,"CAMPDATE":"25-Nov-2019","CampNo":"CAMP_485_0000006","CampName":"zoya colony","CampLocation":"Unnamed Road, Ziya Colony, Degloor, Maharashtra 431717, India","TOTAL_PATIENTS":0,"POSITIVE_PATIENTS":0,"NEGATIVE_PATIENTS":0,"NO_STATUS_PATIENTS":0},{"DISTLGDCODE":485,"DISTNAME":"NANDED","CAMPID":1096,"CAMPDATE":"26-Jan-2020","CampNo":"CAMP_100_0000416","CampName":"bhakti lons","CampLocation":"Doctors Plaza, Dr Lane Rd Number 1, Doctor Lane, Khadakpura, Nanded, Maharashtra 431601, India","TOTAL_PATIENTS":0,"POSITIVE_PATIENTS":0,"NEGATIVE_PATIENTS":0,"NO_STATUS_PATIENTS":0},{"DISTLGDCODE":485,"DISTNAME":"NANDED","CAMPID":499,"CAMPDATE":"26-Nov-2019","CampNo":"CAMP_485_0000007","CampName":"totewad gali camp","CampLocation":"Totawar Galli, Siddhart Nagar, Degloor, Maharashtra 431717, India","TOTAL_PATIENTS":0,"POSITIVE_PATIENTS":0,"NEGATIVE_PATIENTS":0,"NO_STATUS_PATIENTS":0},{"DISTLGDCODE":485,"DISTNAME":"NANDED","CAMPID":1095,"CAMPDATE":"27-Jan-2020","CampNo":"CAMP_100_0000415","CampName":"MIDC NANDED","CampLocation":"NH161, Dhanegaon, Nanded, Maharashtra 431603, India","TOTAL_PATIENTS":15,"POSITIVE_PATIENTS":0,"NEGATIVE_PATIENTS":0,"NO_STATUS_PATIENTS":15},{"DISTLGDCODE":485,"DISTNAME":"NANDED","CAMPID":1108,"CAMPDATE":"27-Jan-2020","CampNo":"CAMP_100_0000428","CampName":"bhakti lons","CampLocation":"Cidco Main Rd, CIDCO Colony, Nanded, Maharashtra 431603, India","TOTAL_PATIENTS":0,"POSITIVE_PATIENTS":0,"NEGATIVE_PATIENTS":0,"NO_STATUS_PATIENTS":0},{"DISTLGDCODE":485,"DISTNAME":"NANDED","CAMPID":500,"CAMPDATE":"27-Nov-2019","CampNo":"CAMP_485_0000008","CampName":"Degloor","CampLocation":"Old Bus Stand, Ambedkar Nagar, Degloor, Maharashtra 431717, India","TOTAL_PATIENTS":0,"POSITIVE_PATIENTS":0,"NEGATIVE_PATIENTS":0,"NO_STATUS_PATIENTS":0},{"DISTLGDCODE":485,"DISTNAME":"NANDED","CAMPID":1129,"CAMPDATE":"28-Jan-2020","CampNo":"CAMP_100_0000449","CampName":"MIDC NANDED","CampLocation":"Cidco Main Rd, CIDCO Colony, Nanded, Maharashtra 431603, India","TOTAL_PATIENTS":25,"POSITIVE_PATIENTS":0,"NEGATIVE_PATIENTS":0,"NO_STATUS_PATIENTS":25},{"DISTLGDCODE":485,"DISTNAME":"NANDED","CAMPID":1130,"CAMPDATE":"28-Jan-2020","CampNo":"CAMP_100_0000450","CampName":"BHAKTI LONS","CampLocation":"Kalpnakar Chowk Bus Stop, Nanded Malegaon Rd, Ashtvinayak Nagar, Nanded, Maharashtra 431605, India","TOTAL_PATIENTS":0,"POSITIVE_PATIENTS":0,"NEGATIVE_PATIENTS":0,"NO_STATUS_PATIENTS":0},{"DISTLGDCODE":485,"DISTNAME":"NANDED","CAMPID":501,"CAMPDATE":"28-Nov-2019","CampNo":"CAMP_485_0000009","CampName":"Degloor","CampLocation":"Near Kalamandir, Degloor, Maharashtra 431717, India","TOTAL_PATIENTS":0,"POSITIVE_PATIENTS":0,"NEGATIVE_PATIENTS":0,"NO_STATUS_PATIENTS":0},{"DISTLGDCODE":485,"DISTNAME":"NANDED","CAMPID":502,"CAMPDATE":"29-Nov-2019","CampNo":"CAMP_485_0000010","CampName":"Degloor","CampLocation":"Degloor Rd, Datta Nagar, Degloor, Maharashtra 431717, India","TOTAL_PATIENTS":0,"POSITIVE_PATIENTS":0,"NEGATIVE_PATIENTS":0,"NO_STATUS_PATIENTS":0},{"DISTLGDCODE":485,"DISTNAME":"NANDED","CAMPID":503,"CAMPDATE":"30-Nov-2019","CampNo":"CAMP_485_0000011","CampName":"Degloor","CampLocation":"Degloor Rd, Datta Nagar, Degloor, Maharashtra 431717, India","TOTAL_PATIENTS":0,"POSITIVE_PATIENTS":0,"NEGATIVE_PATIENTS":0,"NO_STATUS_PATIENTS":0}]
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
         * DISTLGDCODE : 485
         * DISTNAME : NANDED
         * CAMPID : 545
         * CAMPDATE : 05-Dec-2019
         * CampNo : CAMP_485_0000012
         * CampName : narsi camp
         * CampLocation : MH SH 225, Narsi, Maharashtra 431722, India
         * TOTAL_PATIENTS : 0
         * POSITIVE_PATIENTS : 0
         * NEGATIVE_PATIENTS : 0
         * NO_STATUS_PATIENTS : 0
         */

        private int DISTLGDCODE;
        private String DISTNAME;
        private int CAMPID;
        private String CAMPDATE;
        private String CampNo;
        private String CampName;
        private String CampLocation;
        private int TOTAL_PATIENTS;
        private int POSITIVE_PATIENTS;
        private int NEGATIVE_PATIENTS;
        private int NO_STATUS_PATIENTS;

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

        public String getCAMPDATE() {
            return CAMPDATE;
        }

        public void setCAMPDATE(String CAMPDATE) {
            this.CAMPDATE = CAMPDATE;
        }

        public String getCampNo() {
            return CampNo;
        }

        public void setCampNo(String CampNo) {
            this.CampNo = CampNo;
        }

        public String getCampName() {
            return CampName;
        }

        public void setCampName(String CampName) {
            this.CampName = CampName;
        }

        public String getCampLocation() {
            return CampLocation;
        }

        public void setCampLocation(String CampLocation) {
            this.CampLocation = CampLocation;
        }

        public int getTOTAL_PATIENTS() {
            return TOTAL_PATIENTS;
        }

        public void setTOTAL_PATIENTS(int TOTAL_PATIENTS) {
            this.TOTAL_PATIENTS = TOTAL_PATIENTS;
        }

        public int getPOSITIVE_PATIENTS() {
            return POSITIVE_PATIENTS;
        }

        public void setPOSITIVE_PATIENTS(int POSITIVE_PATIENTS) {
            this.POSITIVE_PATIENTS = POSITIVE_PATIENTS;
        }

        public int getNEGATIVE_PATIENTS() {
            return NEGATIVE_PATIENTS;
        }

        public void setNEGATIVE_PATIENTS(int NEGATIVE_PATIENTS) {
            this.NEGATIVE_PATIENTS = NEGATIVE_PATIENTS;
        }

        public int getNO_STATUS_PATIENTS() {
            return NO_STATUS_PATIENTS;
        }

        public void setNO_STATUS_PATIENTS(int NO_STATUS_PATIENTS) {
            this.NO_STATUS_PATIENTS = NO_STATUS_PATIENTS;
        }
    }
}
