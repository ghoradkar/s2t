package com.myhindlab.abkat.models;

import java.util.List;

public class BreastScreeningExaminarNoteModel {

    /**
     * status : Success
     * message : Examinar Note List
     * output : [{"ExaId":1,"ExaminarNote":"Hard Tissue Present in Left Breast"},{"ExaId":2,"ExaminarNote":"Hard Tissue present in Right Breast"},{"ExaId":3,"ExaminarNote":"No Right Breast"},{"ExaId":4,"ExaminarNote":"No Left Breast"},{"ExaId":5,"ExaminarNote":"Skin Burn"},{"ExaId":6,"ExaminarNote":"Skin Infection"}]
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
         * ExaId : 1
         * ExaminarNote : Hard Tissue Present in Left Breast
         */

        private String ExaId;
        private String ExaminarNote;

        public String getExaId() {
            return ExaId;
        }

        public void setExaId(String ExaId) {
            this.ExaId = ExaId;
        }

        public String getExaminarNote() {
            return ExaminarNote;
        }

        public void setExaminarNote(String ExaminarNote) {
            this.ExaminarNote = ExaminarNote;
        }
    }
}
