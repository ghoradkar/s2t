package com.myhindlab.abkat.models;

import java.util.List;

public class SpecimenTypeListModel {

    /**
     * status : Success
     * message : Specimen Type List
     * output : [{"SPECTYPEID":1,"SPECTYPE":"Oro-pharyngeal swab","CREATEDON":"/Date(1602766266170)/","CREATEDBY":1},{"SPECTYPEID":2,"SPECTYPE":"Naso-pharyngeal swab","CREATEDON":"/Date(1602766266170)/","CREATEDBY":1},{"SPECTYPEID":3,"SPECTYPE":"Both NP&OP","CREATEDON":"/Date(1602766266170)/","CREATEDBY":1},{"SPECTYPEID":4,"SPECTYPE":"Blood Sample","CREATEDON":"/Date(1602766266170)/","CREATEDBY":1}]
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
         * SPECTYPEID : 1
         * SPECTYPE : Oro-pharyngeal swab
         * CREATEDON : /Date(1602766266170)/
         * CREATEDBY : 1
         */

        private String SPECTYPEID;
        private String SPECTYPE;
        private String CREATEDON;
        private String CREATEDBY;

        public String getSPECTYPEID() {
            return SPECTYPEID;
        }

        public void setSPECTYPEID(String SPECTYPEID) {
            this.SPECTYPEID = SPECTYPEID;
        }

        public String getSPECTYPE() {
            return SPECTYPE;
        }

        public void setSPECTYPE(String SPECTYPE) {
            this.SPECTYPE = SPECTYPE;
        }

        public String getCREATEDON() {
            return CREATEDON;
        }

        public void setCREATEDON(String CREATEDON) {
            this.CREATEDON = CREATEDON;
        }

        public String getCREATEDBY() {
            return CREATEDBY;
        }

        public void setCREATEDBY(String CREATEDBY) {
            this.CREATEDBY = CREATEDBY;
        }
    }
}
