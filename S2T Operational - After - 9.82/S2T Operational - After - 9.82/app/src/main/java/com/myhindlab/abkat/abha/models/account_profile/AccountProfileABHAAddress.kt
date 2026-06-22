package com.myhindlab.abkat.abha.models.account_profile

import com.google.gson.annotations.SerializedName


data class AccountProfileABHAAddress(

    @SerializedName("abhaAddress") var abhaAddress: String? = null,
    @SerializedName("fullName") var fullName: String? = null,
    @SerializedName("profilePhoto") var profilePhoto: String? = null,
    @SerializedName("firstName") var firstName: String? = null,
    @SerializedName("middleName") var middleName: String? = null,
    @SerializedName("lastName") var lastName: String? = null,
    @SerializedName("dayOfBirth") var dayOfBirth: String? = null,
    @SerializedName("monthOfBirth") var monthOfBirth: String? = null,
    @SerializedName("yearOfBirth") var yearOfBirth: String? = null,
    @SerializedName("dateOfBirth") var dateOfBirth: String? = null,
    @SerializedName("gender") var gender: String? = null,
    @SerializedName("mobile") var mobile: String? = null,
    @SerializedName("abhaNumber") var abhaNumber: String? = null,
    @SerializedName("address") var address: String? = null,
    @SerializedName("stateName") var stateName: String? = null,
    @SerializedName("districtName") var districtName: String? = null,
    @SerializedName("subDistrictName") var subDistrictName: String? = null,
    @SerializedName("stateCode") var stateCode: String? = null,
    @SerializedName("districtCode") var districtCode: String? = null,
    @SerializedName("subDistrictCode") var subDistrictCode: String? = null,
    @SerializedName("authMethods") var authMethods: ArrayList<String> = arrayListOf(),
    @SerializedName("status") var status: String? = null,
    @SerializedName("emailVerified") var emailVerified: String? = null,
    @SerializedName("mobileVerified") var mobileVerified: String? = null,
    @SerializedName("kycStatus") var kycStatus: String? = null,
    @SerializedName("abhaLinkedCount") var abhaLinkedCount: String? = null,
    @SerializedName("pinCode") var pinCode: String? = null,


    )