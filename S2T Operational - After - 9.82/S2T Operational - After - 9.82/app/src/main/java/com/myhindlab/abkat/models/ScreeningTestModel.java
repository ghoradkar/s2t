package com.myhindlab.abkat.models;

import java.io.Serializable;
import java.util.List;

public class ScreeningTestModel implements Serializable {

    /**
     * status : Success
     * message : Test List
     * output : [{"TestId":1,"TestName":"Registration","IsCompulsary":null},{"TestId":2,"TestName":"Basic Details","IsCompulsary":true},{"TestId":3,"TestName":"Physical Examination","IsCompulsary":true},{"TestId":4,"TestName":"Lung Functioin Test","IsCompulsary":null},{"TestId":5,"TestName":"Audio Screening Test","IsCompulsary":null},{"TestId":6,"TestName":"Vision Screening","IsCompulsary":null},{"TestId":7,"TestName":"Barcode","IsCompulsary":null},{"TestId":8,"TestName":"PP Sample Collection","IsCompulsary":true},{"TestId":9,"TestName":"Ackowledgement","IsCompulsary":null},{"TestId":11,"TestName":"Urine Sample Collection","IsCompulsary":null},{"TestId":12,"TestName":"Questionnaire Details","IsCompulsary":null},{"TestId":13,"TestName":"Breast Screening","IsCompulsary":null}]
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

        private String TestId;
        private String TestName;
        private String IsCompulsary;
        private boolean isChecked;

        private String ReasonId;
        private String D2DCRemarkID;
        private String D2DCallingRemark;
        private String ReasonDescription;

        public String getD2DCRemarkID() {
            return D2DCRemarkID;
        }

        public void setD2DCRemarkID(String d2DCRemarkID) {
            D2DCRemarkID = d2DCRemarkID;
        }

        public String getD2DCallingRemark() {
            return D2DCallingRemark;
        }

        public void setD2DCallingRemark(String d2DCallingRemark) {
            D2DCallingRemark = d2DCallingRemark;
        }

        public String getReasonId() {
            return ReasonId;
        }

        public void setReasonId(String reasonId) {
            ReasonId = reasonId;
        }

        public String getReasonDescription() {
            return ReasonDescription;
        }

        public void setReasonDescription(String reasonDescription) {
            ReasonDescription = reasonDescription;
        }

        public String getTestId() {
            return TestId;
        }

        public void setTestId(String TestId) {
            this.TestId = TestId;
        }

        public String getTestName() {
            return TestName;
        }

        public void setTestName(String TestName) {
            this.TestName = TestName;
        }

        public String getIsCompulsary() {
            if (IsCompulsary != null) {
                return IsCompulsary;
            } else {
                return "0";
            }
        }

        public void setIsCompulsary(String IsCompulsary) {
            this.IsCompulsary = IsCompulsary;
        }

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }
    }
}
