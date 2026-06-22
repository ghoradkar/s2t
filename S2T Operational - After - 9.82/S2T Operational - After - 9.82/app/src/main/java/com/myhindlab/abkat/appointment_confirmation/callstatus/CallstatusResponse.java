package com.myhindlab.abkat.appointment_confirmation.callstatus;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CallstatusResponse{

	@SerializedName("output")
	private List<OutputItem> output;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private String status;

	public List<OutputItem> getOutput(){
		return output;
	}

	public String getMessage(){
		return message;
	}

	public String getStatus(){
		return status;
	}
}