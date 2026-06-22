package com.myhindlab.abkat.models.Remark;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class CtRemarkResponse{

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