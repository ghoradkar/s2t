package com.myhindlab.abkat.models;

import java.util.List;

public class PendingTestModel {


    /**
     * status : Success
     * message : Result
     * output : [{"ServiceName":"Blood Urea","SERVICECODE":1,"TOTAL":287,"REJECTED":0,"PENDING":287,"COMPLETED":0},{"ServiceName":"Complete blood count","SERVICECODE":1638,"TOTAL":769,"REJECTED":0,"PENDING":611,"COMPLETED":158},{"ServiceName":"Erythrocyte Sedimentation Rate","SERVICECODE":1341,"TOTAL":721,"REJECTED":0,"PENDING":563,"COMPLETED":158},{"ServiceName":"Gamma Glutamyl Transferase (GGT)","SERVICECODE":1647,"TOTAL":287,"REJECTED":0,"PENDING":287,"COMPLETED":0},{"ServiceName":"LDL Cholesterol (Direct)","SERVICECODE":1162,"TOTAL":287,"REJECTED":0,"PENDING":287,"COMPLETED":0},{"ServiceName":"Malaria parasite","SERVICECODE":1357,"TOTAL":769,"REJECTED":0,"PENDING":611,"COMPLETED":158},{"ServiceName":"Serum Phosphorus","SERVICECODE":19,"TOTAL":286,"REJECTED":0,"PENDING":286,"COMPLETED":0},{"ServiceName":"Serum Albumin","SERVICECODE":9,"TOTAL":287,"REJECTED":0,"PENDING":287,"COMPLETED":0},{"ServiceName":"Serum Alkaline Phosphatase","SERVICECODE":7,"TOTAL":287,"REJECTED":0,"PENDING":287,"COMPLETED":0},{"ServiceName":"Serum Bilirubin (D)","SERVICECODE":4,"TOTAL":287,"REJECTED":0,"PENDING":287,"COMPLETED":0},{"ServiceName":"Serum Bilirubin(T)","SERVICECODE":3,"TOTAL":287,"REJECTED":0,"PENDING":287,"COMPLETED":0},{"ServiceName":"Serum Calcium","SERVICECODE":15,"TOTAL":286,"REJECTED":0,"PENDING":286,"COMPLETED":0},{"ServiceName":"Serum Creatinine","SERVICECODE":2,"TOTAL":287,"REJECTED":0,"PENDING":287,"COMPLETED":0},{"ServiceName":"Serum HDL-Cholesterol","SERVICECODE":13,"TOTAL":287,"REJECTED":0,"PENDING":287,"COMPLETED":0},{"ServiceName":"Serum Iron","SERVICECODE":1209,"TOTAL":286,"REJECTED":0,"PENDING":286,"COMPLETED":0},{"ServiceName":"Serum Magnesium","SERVICECODE":1213,"TOTAL":287,"REJECTED":0,"PENDING":287,"COMPLETED":0},{"ServiceName":"Serum Total Protein and Albumin","SERVICECODE":8,"TOTAL":287,"REJECTED":0,"PENDING":287,"COMPLETED":0},{"ServiceName":"Serum Triglycerides","SERVICECODE":11,"TOTAL":287,"REJECTED":0,"PENDING":287,"COMPLETED":0},{"ServiceName":"Serum Uric Acid","SERVICECODE":17,"TOTAL":287,"REJECTED":0,"PENDING":287,"COMPLETED":0},{"ServiceName":"Serum VLDL-Cholesterol","SERVICECODE":12,"TOTAL":287,"REJECTED":0,"PENDING":287,"COMPLETED":0},{"ServiceName":"SGOT","SERVICECODE":5,"TOTAL":287,"REJECTED":0,"PENDING":287,"COMPLETED":0},{"ServiceName":"SGPT","SERVICECODE":6,"TOTAL":287,"REJECTED":0,"PENDING":287,"COMPLETED":0},{"ServiceName":"T3","SERVICECODE":50,"TOTAL":287,"REJECTED":0,"PENDING":287,"COMPLETED":0},{"ServiceName":"T4","SERVICECODE":51,"TOTAL":287,"REJECTED":0,"PENDING":287,"COMPLETED":0},{"ServiceName":"Total Cholesterol","SERVICECODE":10,"TOTAL":287,"REJECTED":0,"PENDING":287,"COMPLETED":0},{"ServiceName":"TSH","SERVICECODE":52,"TOTAL":287,"REJECTED":0,"PENDING":287,"COMPLETED":0},{"ServiceName":"Urine Routine","SERVICECODE":1640,"TOTAL":769,"REJECTED":0,"PENDING":611,"COMPLETED":158}]
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
         * ServiceName : Blood Urea
         * SERVICECODE : 1
         * TOTAL : 287
         * REJECTED : 0
         * PENDING : 287
         * COMPLETED : 0
         */

        private String ServiceName;
        private int SERVICECODE;
        private int TOTAL;
        private int REJECTED;
        private int PENDING;
        private int COMPLETED;

        public String getServiceName() {
            return ServiceName;
        }

        public void setServiceName(String ServiceName) {
            this.ServiceName = ServiceName;
        }

        public int getSERVICECODE() {
            return SERVICECODE;
        }

        public void setSERVICECODE(int SERVICECODE) {
            this.SERVICECODE = SERVICECODE;
        }

        public int getTOTAL() {
            return TOTAL;
        }

        public void setTOTAL(int TOTAL) {
            this.TOTAL = TOTAL;
        }

        public int getREJECTED() {
            return REJECTED;
        }

        public void setREJECTED(int REJECTED) {
            this.REJECTED = REJECTED;
        }

        public int getPENDING() {
            return PENDING;
        }

        public void setPENDING(int PENDING) {
            this.PENDING = PENDING;
        }

        public int getCOMPLETED() {
            return COMPLETED;
        }

        public void setCOMPLETED(int COMPLETED) {
            this.COMPLETED = COMPLETED;
        }
    }
}
