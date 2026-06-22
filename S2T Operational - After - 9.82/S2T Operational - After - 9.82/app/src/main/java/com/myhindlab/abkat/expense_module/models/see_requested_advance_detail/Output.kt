package com.myhindlab.abkat.expense_module.models.see_requested_advance_detail

import com.google.gson.annotations.SerializedName


data class Output(

    @SerializedName("campid") var campid: Int? = null,
    @SerializedName("DISTNAME") var DISTNAME: String? = null,
    @SerializedName("CampDate") var CampDate: String? = null,
    @SerializedName("Expectedbeneficiarycount") var Expectedbeneficiarycount: Int? = null,
    @SerializedName("FundRequestedBy") var FundRequestedBy: String? = null,
    @SerializedName("DesgName") var DesgName: String? = null,
    @SerializedName("TotalAdvanceTaken") var TotalAdvanceTaken: Int? = null,
    @SerializedName("TotalBillSubmittedAmt") var TotalBillSubmittedAmt: String? = null,
    @SerializedName("TotalRequestedExpense") var TotalRequestedExpense: Int? = null,
    @SerializedName("PriviousApprovalStatus") var PriviousApprovalStatus: String? = null,
    @SerializedName("ApprovalStatus") var ApprovalStatus: String? = null,
    @SerializedName("StateMgrAdvApprove") var StateMgrAdvApprove: Int? = null,
    @SerializedName("Beneficiary refreshment") var Beneficiaryrefreshment: Int? = null,
    @SerializedName("Camp Awareness using Bhopu") var CampAwarenessusingBhopu: Int? = null,
    @SerializedName("Camp Hall Gram Panchayat/School/Govt Office/Tent") var CampHallGramPanchayatSchoolGovtOfficeTent: Int? = null,
    @SerializedName("Chairs") var Chairs: Int? = null,
    @SerializedName("Cleaning Charges") var CleaningCharges: Int? = null,
    @SerializedName("Drinking Water") var DrinkingWater: Int? = null,
    @SerializedName("Food to Staff (TA) Allowance") var FoodtoStaffTAAllowance: Int? = null,
    @SerializedName("Post Camp Expense") var PostCampExpense: Int? = null,
    @SerializedName("Sample movement to lab - Runner boy") var SamplemovementtolabRunnerboy: Int? = null,
    @SerializedName("Sample movement to lab - TSRTC or any other Cargo") var SamplemovementtolabTSRTCoranyotherCargo: Int? = null,
    @SerializedName("Tables") var Tables: Int? = null,
    @SerializedName("Transportation of Staff (TA) Group of Transport") var TransportationofStaffTAGroupofTransport: Int? = null,
    @SerializedName("Transportation of Staff (TA) Individual") var TransportationofStaffTAIndividual: Int? = null

)