package com.myhindlab.abkat.models.couriermodule;

import java.io.Serializable;
import java.util.List;

public class CourierReceivedRunnerBoy implements Serializable {

    /**
     * status : Success
     * message : Courier Details By Runner Boy
     * output : [{"CourierID":27,"RegistrationLabID":22,"RegistrationLab":"Latur","CourierToLabID":168,"CourierToLab":"Bhamragad","ProcessLabID":65,"ProcessLab":"Allapali","CourierDate":"2020-09-17","CategoryID":2,"CategoryName":"Consumables","Barcode":"84838288282919","RunnerBoyName":"ARSHAD MAKHMUR HUSSAIN SHEIKH"}]
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
         * CourierID : 27
         * RegistrationLabID : 22
         * RegistrationLab : Latur
         * CourierToLabID : 168
         * CourierToLab : Bhamragad
         * ProcessLabID : 65
         * ProcessLab : Allapali
         * CourierDate : 2020-09-17
         * CategoryID : 2
         * CategoryName : Consumables
         * Barcode : 84838288282919
         * RunnerBoyName : ARSHAD MAKHMUR HUSSAIN SHEIKH
         */

        private String CourierID;
        private String RegistrationLabID;
        private String RegistrationLab;
        private String CourierToLabID;
        private String CourierToLab;
        private String ProcessLabID;
        private String ProcessLab;
        private String CourierDate;
        private String CategoryID;
        private String CategoryName;
        private String Barcode;
        private String RunnerBoyName;

        public String getCourierID() {
            return CourierID;
        }

        public void setCourierID(String CourierID) {
            this.CourierID = CourierID;
        }

        public String getRegistrationLabID() {
            return RegistrationLabID;
        }

        public void setRegistrationLabID(String RegistrationLabID) {
            this.RegistrationLabID = RegistrationLabID;
        }

        public String getRegistrationLab() {
            return RegistrationLab;
        }

        public void setRegistrationLab(String RegistrationLab) {
            this.RegistrationLab = RegistrationLab;
        }

        public String getCourierToLabID() {
            return CourierToLabID;
        }

        public void setCourierToLabID(String CourierToLabID) {
            this.CourierToLabID = CourierToLabID;
        }

        public String getCourierToLab() {
            return CourierToLab;
        }

        public void setCourierToLab(String CourierToLab) {
            this.CourierToLab = CourierToLab;
        }

        public String getProcessLabID() {
            return ProcessLabID;
        }

        public void setProcessLabID(String ProcessLabID) {
            this.ProcessLabID = ProcessLabID;
        }

        public String getProcessLab() {
            return ProcessLab;
        }

        public void setProcessLab(String ProcessLab) {
            this.ProcessLab = ProcessLab;
        }

        public String getCourierDate() {
            return CourierDate;
        }

        public void setCourierDate(String CourierDate) {
            this.CourierDate = CourierDate;
        }

        public String getCategoryID() {
            return CategoryID;
        }

        public void setCategoryID(String CategoryID) {
            this.CategoryID = CategoryID;
        }

        public String getCategoryName() {
            return CategoryName;
        }

        public void setCategoryName(String CategoryName) {
            this.CategoryName = CategoryName;
        }

        public String getBarcode() {
            return Barcode;
        }

        public void setBarcode(String Barcode) {
            this.Barcode = Barcode;
        }

        public String getRunnerBoyName() {
            return RunnerBoyName;
        }

        public void setRunnerBoyName(String RunnerBoyName) {
            this.RunnerBoyName = RunnerBoyName;
        }
    }
}
