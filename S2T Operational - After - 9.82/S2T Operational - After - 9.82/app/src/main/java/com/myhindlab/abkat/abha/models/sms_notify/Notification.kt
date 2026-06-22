package com.myhindlab.abkat.abha.models.sms_notify
import com.google.gson.annotations.SerializedName


data class Notification (

  @SerializedName("phoneNo" ) var phoneNo : String? = null,
  @SerializedName("hip"     ) var hip     : Hip?    = Hip()

)