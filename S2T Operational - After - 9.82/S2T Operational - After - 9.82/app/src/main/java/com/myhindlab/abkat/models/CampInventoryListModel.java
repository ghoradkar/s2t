package com.myhindlab.abkat.models;

import java.util.ArrayList;

public class CampInventoryListModel {

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

    public static class OutputBean {

        private String CampId;
        private String Inventory;
        private String QuantityRequired;
        private String InventoryID;
        private String useQuantity;

        public String getInventoryID() {
            return InventoryID;
        }

        public void setInventoryID(String inventoryID) {
            InventoryID = inventoryID;
        }

        public String getUseQuantity() {
            return useQuantity;
        }

        public void setUseQuantity(String useQuantity) {
            this.useQuantity = useQuantity;
        }

        public String getCampId() {
            return CampId;
        }

        public void setCampId(String CampId) {
            this.CampId = CampId;
        }

        public String getInventory() {
            return Inventory;
        }

        public void setInventory(String Inventory) {
            this.Inventory = Inventory;
        }

        public String getQuantityRequired() {
            return QuantityRequired;
        }

        public void setQuantityRequired(String QuantityRequired) {
            this.QuantityRequired = QuantityRequired;
        }
    }
}
