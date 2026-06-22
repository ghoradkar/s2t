package com.myhindlab.abkat.activities.campredinessnew;

import com.google.gson.annotations.SerializedName;

public class OutputItem{

	@SerializedName("ItemStatus")
	private int itemStatus;

	@SerializedName("IsActive")
	private int isActive;

	@SerializedName("ItemType")
	private int itemType;

	@SerializedName("ItemName")
	private String itemName;

	@SerializedName("ItemId")
	private int itemId;

	private int radioButtonState = 1;




	public OutputItem(int itemStatus, int isActive, int itemType, String itemName, int itemId) {
		this.itemStatus = itemStatus;
		this.isActive = isActive;
		this.itemType = itemType;
		this.itemName = itemName;
		this.itemId = itemId;
	}

	public int getRadioButtonState() {
		return radioButtonState;
	}

	public void setRadioButtonState(int radioButtonState) {
		this.radioButtonState = radioButtonState;
	}

	public void setItemStatus(int itemStatus) {
		this.itemStatus = itemStatus;
	}

	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}

	public void setItemType(int itemType) {
		this.itemType = itemType;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public int getItemStatus(){
		return itemStatus;
	}

	public int getIsActive(){
		return isActive;
	}

	public int getItemType(){
		return itemType;
	}

	public String getItemName(){
		return itemName;
	}

	public int getItemId(){
		return itemId;
	}
}