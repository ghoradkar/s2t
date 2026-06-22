package com.myhindlab.abkat.abha.models

import com.google.gson.annotations.SerializedName


data class CreateHealthIdResponseModel(

    @SerializedName("token") var token: String? = null,
    @SerializedName("refreshToken") var refreshToken: String? = null,
    @SerializedName("healthIdNumber") var healthIdNumber: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("gender") var gender: String? = null,
    @SerializedName("yearOfBirth") var yearOfBirth: String? = null,
    @SerializedName("monthOfBirth") var monthOfBirth: String? = null,
    @SerializedName("dayOfBirth") var dayOfBirth: String? = null,
    @SerializedName("firstName") var firstName: String? = null,
    @SerializedName("healthId") var healthId: String? = null,
    @SerializedName("lastName") var lastName: String? = null,
    @SerializedName("middleName") var middleName: String? = null,
    @SerializedName("stateCode") var stateCode: String? = null,
    @SerializedName("districtCode") var districtCode: String? = null,
    @SerializedName("stateName") var stateName: String? = null,
    @SerializedName("districtName") var districtName: String? = null,
    @SerializedName("email") var email: String? = null,
    @SerializedName("kycPhoto") var kycPhoto: String? = null,
    @SerializedName("profilePhoto") var profilePhoto: String? = null,
    @SerializedName("mobile") var mobile: String? = null,
    @SerializedName("authMethods") var authMethods: ArrayList<String> = arrayListOf(),
    @SerializedName("pincode") var pincode: String? = null,
    @SerializedName("alreadyExists") var alreadyExists: String? = null,
    @SerializedName("new") var new: Boolean? = null

) : java.io.Serializable