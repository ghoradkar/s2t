package com.myhindlab.abkat.models.Remark;

import com.google.gson.annotations.SerializedName;

public class OutputItem{

	@SerializedName("ArId")
	private int arId;

	@SerializedName("AssignmentRemarks")
	private String assignmentRemarks;

	public int getArId(){
		return arId;
	}

	public String getAssignmentRemarks(){
		return assignmentRemarks;
	}
}