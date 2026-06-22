package com.myhindlab.abkat.models;

import java.io.Serializable;
import java.util.ArrayList;

public class CampSampleProcessingModel implements Serializable {

    private String status;
    private String message;
    private ArrayList<OutputBean> output;

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

    public ArrayList<OutputBean> getOutput() {
        return output;
    }

    public void setOutput(ArrayList<OutputBean> output) {
        this.output = output;
    }

    public static class OutputBean implements Serializable{
        private String CampId;
        private String RegdId;
        private String PatientName;
        private String RegdNo;
        private String BarCode;
        private String TotalTests;
        private String PROCESSED;
        private String PENDING;
        private String Rejected;

        public String getCampId() {
            if (CampId == null) {
                return "NA";
            } else {
                return CampId;
            }
        }

        public void setCampId(String CampId) {
            this.CampId = CampId;
        }

        public String getRegdId() {
            if (RegdId == null) {
                return "NA";
            } else {
                return RegdId;
            }
        }

        public void setRegdId(String RegdId) {
            this.RegdId = RegdId;
        }

        public String getPatientName() {
            if (PatientName == null) {
                return "NA";
            } else {
                return PatientName;
            }
        }

        public void setPatientName(String PatientName) {
            this.PatientName = PatientName;
        }

        public String getRegdNo() {
            if (RegdNo == null) {
                return "NA";
            } else {
                return RegdNo;
            }
        }

        public void setRegdNo(String RegdNo) {
            this.RegdNo = RegdNo;
        }

        public String getBarCode() {
            if (BarCode == null) {
                return "NA";
            } else {
                return BarCode;
            }
        }

        public void setBarCode(String BarCode) {
            this.BarCode = BarCode;
        }

        public String getTotalTests() {
            if (TotalTests == null) {
                return "NA";
            } else {
                return TotalTests;
            }
        }

        public void setTotalTests(String TotalTests) {
            this.TotalTests = TotalTests;
        }

        public String getPROCESSED() {
            if (PROCESSED == null) {
                return "NA";
            } else {
                return PROCESSED;
            }
        }

        public void setPROCESSED(String PROCESSED) {
            this.PROCESSED = PROCESSED;
        }

        public String getPENDING() {
            if (PENDING == null) {
                return "NA";
            } else {
                return PENDING;
            }
        }

        public void setPENDING(String PENDING) {
            this.PENDING = PENDING;
        }

        public String getRejected() {
            if (Rejected == null) {
                return "NA";
            } else {
                return Rejected;
            }
        }

        public void setRejected(String Rejected) {
            this.Rejected = Rejected;
        }
    }
}
