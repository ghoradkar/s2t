package com.myhindlab.abkat.appointment_confirmation.insertapi;

import com.google.gson.annotations.SerializedName;

public class ResponseModel{

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