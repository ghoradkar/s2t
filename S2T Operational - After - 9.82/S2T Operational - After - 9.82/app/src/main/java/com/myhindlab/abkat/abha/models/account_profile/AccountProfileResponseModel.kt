package com.myhindlab.abkat.abha.models.account_profile

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class AccountProfileResponseModel(

    @SerializedName("ABHANumber") var ABHANumber: String? = null,
    @SerializedName("preferredAbhaAddress") var preferredAbhaAddress: String? = null,
    @SerializedName("mobile") var mobile: String? = null,
    @SerializedName("firstName") var firstName: String? = null,
    @SerializedName("middleName") var middleName: String? = null,
    @SerializedName("lastName") var lastName: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("yearOfBirth") var yearOfBirth: String? = null,
    @SerializedName("dayOfBirth") var dayOfBirth: String? = null,
    @SerializedName("monthOfBirth") var monthOfBirth: String? = null,
    @SerializedName("gender") var gender: String? = null,
    @SerializedName("email") var email: String? = null,
    @SerializedName("profilePhoto") var profilePhoto: String? = null,
    @SerializedName("stateCode") var stateCode: String? = null,
    @SerializedName("districtCode") var districtCode: String? = null,
    @SerializedName("subDistrictCode") var subDistrictCode: String? = null,
    @SerializedName("villageCode") var villageCode: String? = null,
    @SerializedName("townCode") var townCode: String? = null,
    @SerializedName("wardCode") var wardCode: String? = null,
    @SerializedName("pincode") var pincode: String? = null,
    @SerializedName("address") var address: String? = null,
    @SerializedName("kycPhoto") var kycPhoto: String? = null,
    @SerializedName("stateName") var stateName: String? = null,
    @SerializedName("districtName") var districtName: String? = null,
    @SerializedName("subdistrictName") var subdistrictName: String? = null,
    @SerializedName("villageName") var villageName: String? = null,
    @SerializedName("townName") var townName: String? = null,
    @SerializedName("wardName") var wardName: String? = null,
    @SerializedName("authMethods") var authMethods: ArrayList<String> = arrayListOf(),
    @SerializedName("kycVerified") var kycVerified: Boolean? = null,
    @SerializedName("verificationStatus") var verificationStatus: String? = null,
    @SerializedName("verificationType") var verificationType: String? = null,
    @SerializedName("emailVerified") var emailVerified: Boolean? = null,
    @SerializedName("createdBy")
    var createdBy: Int? = null,
    @SerializedName("campId")
    var campId: Int? = null

) : Serializable