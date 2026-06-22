package com.myhindlab.abkat.abha.models.enrol_by_aadhaar_via_demo_auth

import com.google.gson.annotations.SerializedName
import com.myhindlab.abkat.abha.models.enrol_by_aadhaar_via_demo_auth.AuthData
import com.myhindlab.abkat.abha.models.enrol_by_aadhaar_via_demo_auth.Consent


data class EnrollByAadhaarViaDemoAuth (

    @SerializedName("authData" ) var authData : AuthData? = AuthData(),
    @SerializedName("consent"  ) var consent  : Consent?  = Consent()

)