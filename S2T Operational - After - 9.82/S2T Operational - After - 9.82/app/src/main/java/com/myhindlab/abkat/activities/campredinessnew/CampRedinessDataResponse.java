package com.myhindlab.abkat.activities.campredinessnew;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class CampRedinessDataResponse{

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