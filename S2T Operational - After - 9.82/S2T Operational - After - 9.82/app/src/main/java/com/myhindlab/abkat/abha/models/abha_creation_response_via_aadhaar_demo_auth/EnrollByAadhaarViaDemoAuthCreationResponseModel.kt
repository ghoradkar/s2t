package com.myhindlab.abkat.abha.models.abha_creation_response_via_aadhaar_demo_auth

import com.erp.hllconnect.abha.models.abha_creation_response_via_aadhaar_demo_auth.JwtResponse
import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class EnrollByAadhaarViaDemoAuthCreationResponseModel(

    @SerializedName("photo") var photo: String? = null,
    @SerializedName("gender") var gender: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("email") var email: String? = null,
    @SerializedName("phone") var phone: String? = null,
    @SerializedName("pincode") var pincode: String? = null,
    @SerializedName("birthdate") var birthdate: String? = null,
    @SerializedName("careOf") var careOf: String? = null,
    @SerializedName("house") var house: String? = null,
    @SerializedName("street") var street: String? = null,
    @SerializedName("landmark") var landmark: String? = null,
    @SerializedName("locality") var locality: String? = null,
    @SerializedName("villageTownCity") var villageTownCity: String? = null,
    @SerializedName("subDist") var subDist: String? = null,
    @SerializedName("district") var district: String? = null,
    @SerializedName("state") var state: String? = null,
    @SerializedName("postOffice") var postOffice: String? = null,
    @SerializedName("aadhaar") var aadhaar: String? = null,
    @SerializedName("txnId") var txnId: String? = null,
    @SerializedName("healthIdNumber") var healthIdNumber: String? = null,
    @SerializedName("jwtResponse") var jwtResponse: JwtResponse? = JwtResponse(),
    @SerializedName("new") var new: Boolean? = null,
    @SerializedName("createdBy") var createdBy: Int? = null,
    @SerializedName("facilityCode") var facilityCode: Int? = null,
    @SerializedName("districtLGDCode") var districtLGDCode: Int? = null,
    @SerializedName("districtID") var districtID: Int? = null

) : Serializable