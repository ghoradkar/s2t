package com.myhindlab.abkat.abha.models

import com.google.gson.annotations.SerializedName


data class CreateHealthIdModel(

    @SerializedName("email") var email: String? = null,
    @SerializedName("firstName") var firstName: String? = null,
    @SerializedName("lastName") var lastName: String? = null,
    @SerializedName("middleName") var middleName: String? = null,
    @SerializedName("mobile") var mobile: Long? = null,
    @SerializedName("otp") var otp: Int? = null,
    @SerializedName("password") var password: String? = null,
    @SerializedName("profilePhoto") var profilePhoto: String? = null,
    @SerializedName("txnId") var txnId: String? = null,
    @SerializedName("username") var username: String? = null

)