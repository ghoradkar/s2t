package com.myhindlab.abkat.abha.models.patient_queue

import com.google.gson.annotations.SerializedName


data class Output (

  @SerializedName("identityID"     ) var identityID     : Int?    = null,
  @SerializedName("requestId"      ) var requestId      : String? = null,
  @SerializedName("time_stamp"     ) var timeStamp      : String? = null,
  @SerializedName("hipCode"        ) var hipCode        : String? = null,
  @SerializedName("healthId"       ) var healthId       : String? = null,
  @SerializedName("healthIdNumber" ) var healthIdNumber : String? = null,
  @SerializedName("name"           ) var name           : String? = null,
  @SerializedName("gender"         ) var gender         : String? = null,
  @SerializedName("address"        ) var address        : String? = null,
  @SerializedName("yearOfBirth"    ) var yearOfBirth    : String? = null,
  @SerializedName("dayOfBirth"     ) var dayOfBirth     : String? = null,
  @SerializedName("monthOfBirth"   ) var monthOfBirth   : String? = null,
  @SerializedName("identifiers"    ) var identifiers    : String? = null,
  @SerializedName("patient"        ) var patient        : String? = null,
  @SerializedName("response"       ) var response       : String? = null,
  @SerializedName("CreatedOn"      ) var CreatedOn      : String? = null,
  @SerializedName("ErrorMessage"   ) var ErrorMessage   : String? = null,
  @SerializedName("authtoken"      ) var authtoken      : String? = null,
  @SerializedName("IsRegistered"   ) var IsRegistered   : Int?    = null,
  @SerializedName("MobileNo"       ) var MobileNo       : Long?    = null,
  @SerializedName("Campid"         ) var Campid         : Int?    = null

)