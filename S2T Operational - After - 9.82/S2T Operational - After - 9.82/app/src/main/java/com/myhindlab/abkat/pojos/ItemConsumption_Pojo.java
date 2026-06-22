package com.myhindlab.abkat.pojos;

public class ItemConsumption_Pojo {

   private String CampId;
   private String Inventory;
   private String InventoryID;
   private String QuantityRequired;
   private String useQuantity;


    public String getCampId() {
        return CampId;
    }

    public void setCampId(String campId) {
        CampId = campId;
    }

    public String getInventory() {
        return Inventory;
    }

    public void setInventory(String inventory) {
        Inventory = inventory;
    }

    public String getInventoryID() {
        return InventoryID;
    }

    public void setInventoryID(String inventoryID) {
        InventoryID = inventoryID;
    }

    public String getQuantityRequired() {
        return QuantityRequired;
    }

    public void setQuantityRequired(String quantityRequired) {
        QuantityRequired = quantityRequired;
    }

    public String getUseQuantity() {
        return useQuantity;
    }

    public void setUseQuantity(String useQuantity) {
        this.useQuantity = useQuantity;
    }
}
