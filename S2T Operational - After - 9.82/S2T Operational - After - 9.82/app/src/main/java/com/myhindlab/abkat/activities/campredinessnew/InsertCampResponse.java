package com.myhindlab.abkat.activities.campredinessnew;

import com.google.gson.annotations.SerializedName;

public class InsertCampResponse{

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private String status;

	public String getMessage(){
		return message;
	}

	public String getStatus(){
		return status;
	}
}