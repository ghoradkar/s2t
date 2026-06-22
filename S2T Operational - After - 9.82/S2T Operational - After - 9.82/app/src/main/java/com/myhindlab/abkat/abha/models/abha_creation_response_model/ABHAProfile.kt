package com.myhindlab.abkat.abha.models.abha_creation_response_model

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class ABHAProfile (

  @SerializedName("firstName"    ) var firstName    : String?           = null,
  @SerializedName("middleName"   ) var middleName   : String?           = null,
  @SerializedName("lastName"     ) var lastName     : String?           = null,
  @SerializedName("dob"          ) var dob          : String?           = null,
  @SerializedName("mobile"       ) var mobile       : String?           = null,
  @SerializedName("gender"       ) var gender       : String?           = null,
  @SerializedName("photo"        ) var photo        : String?           = null,
  @SerializedName("phrAddress"   ) var phrAddress   : ArrayList<String> = arrayListOf(),
  @SerializedName("address"      ) var address      : String?           = null,
  @SerializedName("districtCode" ) var districtCode : String?           = null,
  @SerializedName("stateCode"    ) var stateCode    : String?           = null,
  @SerializedName("pinCode"      ) var pinCode      : String?           = null,
  @SerializedName("abhaType"     ) var abhaType     : String?           = null,
  @SerializedName("stateName"    ) var stateName    : String?           = null,
  @SerializedName("districtName" ) var districtName : String?           = null,
  @SerializedName("ABHANumber"   ) var ABHANumber   : String?           = null,
  @SerializedName("abhaStatus"   ) var abhaStatus   : String?           = null

):Serializable