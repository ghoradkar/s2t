package com.myhindlab.abkat.expense_module.models.advance_detail_response

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class Output(

    @SerializedName("campid") var campid: Int? = null,
    @SerializedName("AdvRaisedbyUserid") var AdvRaisedbyUserid: Int? = null,
    @SerializedName("Distname") var Distname: String? = null,
    @SerializedName("campdate") var campdate: String? = null,
    @SerializedName("RequestType") var RequestType: Int? = null,
    @SerializedName("CampOrganisedBy") var CampOrganisedBy: Int? = null,
    @SerializedName("InitiatedBy") var InitiatedBy: Int? = null,
    @SerializedName("CampType") var CampType: Int? = null,
    @SerializedName("Expectedbeneficiarycount") var Expectedbeneficiarycount: Int? = null,
    @SerializedName("Registeredbeneficiarycount") var Registeredbeneficiarycount: Int? = null,
    @SerializedName("ProcessName") var ProcessName: String? = null,
    @SerializedName("FundRequestedBy") var FundRequestedBy: String? = null,
    @SerializedName("ApprovedDate") var ApprovedDate: String? = null,
    @SerializedName("AdvanceFundStatus") var AdvanceFundStatus: String? = null,
    @SerializedName("ExpenseAmount") var ExpenseAmount: Int? = null,
    @SerializedName("ApprovedStatus") var ApprovedStatus: String? = null,
    @SerializedName("ActualBillSubmit") var ActualBillSubmit: Int? = null,
    @SerializedName("ActualAmountApporved") var ActualAmountApporved: String? = null,
    @SerializedName("ExpenseSaved") var ExpenseSaved: String? = null,
    @SerializedName("IsBillUploaded") var IsBillUploaded: String? = null,
    @SerializedName("IsSecondLevelApproval") var IsSecondLevelApproval: String? = null,
    @SerializedName("ActualExpenseStatus") var ActualExpenseStatus: String? = null,
    @SerializedName("FinalSettelment") var FinalSettelment: String? = null,
    @SerializedName("Beneficiary refreshment") var BeneficiaryRefreshment: Int? = null,
    @SerializedName("Doctor Physical Screening Expense") var Doctorphysicalscreeningexpense: Int? = null,
    @SerializedName("Doctor Report Screening Expense") var Doctorreportscreeningexpense: Int? = null,
    @SerializedName("Camp Awareness using Bhopu") var CampAwarenessUsingBhopu: Int? = null,
    @SerializedName("Camp Hall Gram Panchayat/School/Govt Office/Tent") var CampHall
    : Int? = null,
    @SerializedName("Chairs") var Chairs: Int? = null,
    @SerializedName("Cleaning Charges") var CleaningCharges
    : Int? = null,
    @SerializedName("Drinking                    Water") var DrinkingWater
    : Int? = null,
    @SerializedName("Food to Staff (TA) Allowance") var FoodToStaffTAAllowance: Int? = null,
    @SerializedName("Post Camp Expense")
    var PostCampExpense: Int? = null,
    @SerializedName("Sample movement to lab - Runner boy")
    var SampleMovementToLabRunnerBoy: Int? = null,
    @SerializedName("Sample movement to lab - TSRTC or any other Cargo")
    var SampleMovementToLabTSRTCOrAnyOtherCargo: Int? = null,

    @SerializedName("Tables")
    var Tables: Int? = null,

    @SerializedName("Transportation of Staff (TA) Group of Transport")
    var TransportationOfStaffTAAllowanceToStaffGroupTransportationLikeAutoTumTum: Int? = null,

    @SerializedName("Transportation of Staff (TA) Individual")
    var TransportationOfStaffTAAllowanceToStaffIndividuals: Int? = null

) : Serializable