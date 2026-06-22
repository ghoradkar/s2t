package com.myhindlab.abkat.activities.campredinessnew;

import java.util.List;

public class Type_CampReadinessForm {
    String ItemId;
    String ItemStatus;

    public Type_CampReadinessForm(String itemId, String itemStatus) {
        ItemId = itemId;
        ItemStatus = itemStatus;
    }




    public String getItemId() {
        return ItemId;
    }

    public void setItemId(String itemId) {
        ItemId = itemId;
    }

    public String getItemStatus() {
        return ItemStatus;
    }

    public void setItemStatus(String itemStatus) {
        ItemStatus = itemStatus;
    }
}
