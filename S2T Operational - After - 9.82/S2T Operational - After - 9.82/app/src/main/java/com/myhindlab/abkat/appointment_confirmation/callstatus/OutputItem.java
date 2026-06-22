package com.myhindlab.abkat.appointment_confirmation.callstatus;

import com.google.gson.annotations.SerializedName;

public class OutputItem{

	@SerializedName("AssignStatusID")
	private int assignStatusID;

	@SerializedName("CallingStatus")
	private String callingStatus;

	@SerializedName("AppointmentStatus")
	private String appointmentStatus;

	public void setAssignStatusID(int assignStatusID) {
		this.assignStatusID = assignStatusID;
	}

	public void setCallingStatus(String callingStatus) {
		this.callingStatus = callingStatus;
	}

	public String getAppointmentStatus() {
		return appointmentStatus;
	}

	public void setAppointmentStatus(String appointmentStatus) {
		this.appointmentStatus = appointmentStatus;
	}

	public int getAssignStatusID(){
		return assignStatusID;
	}

	public String getCallingStatus(){
		return callingStatus;
	}

	public OutputItem(int assignStatusID, String callingStatus) {
		this.assignStatusID = assignStatusID;
		this.callingStatus = callingStatus;
	}
}