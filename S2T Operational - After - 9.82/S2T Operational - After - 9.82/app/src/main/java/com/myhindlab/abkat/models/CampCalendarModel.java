package com.myhindlab.abkat.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CampCalendarModel implements Serializable {

    private String status;
    private String message;
    private ArrayList<OutputBean> output;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<OutputBean> getOutput() {
        return output;
    }

    public void setOutput(ArrayList<OutputBean> output) {
        this.output = output;
    }

    public static class OutputBean implements Serializable {
        private String CampId;
        private String CampNo;
        private String CampLocation;
        private String CampDate;
        private String Status;
        private String Description;
        private String DISTLGDCODE;
        private String DISTNAME;
        private String REGISTERWORKERS;
        private String UNREGISTEREDWORKERS;
        private String SurveyCoordinatorName;
        private String MOBNO;
        private String MOBNO1;
        private String CampName;
        private String ScreeningDone;
        private String ScreeningNotDone;
        private String CampStatus;
        private String CordinatorName;
        private String CampTypeDescription;

        private String Expectedbeneficiarycount;
        private String Organizedby;
        private Integer Total;
        private String RegularCamp;
        private String D2DCamp;
        private String SubOrgId;
        private String SubOrgName;

        public String getSubOrgId() {
            return SubOrgId;
        }

        public void setSubOrgId(String subOrgId) {
            SubOrgId = subOrgId;
        }

        public String getSubOrgName() {
            return SubOrgName;
        }

        public void setSubOrgName(String subOrgName) {
            SubOrgName = subOrgName;
        }

        public Integer getRegular() {
            return Regular;
        }

        public void setRegular(Integer regular) {
            Regular = regular;
        }

        public String getCSCRegular() {
            return CSCRegular;
        }

        public void setCSCRegular(String CSCRegular) {
            this.CSCRegular = CSCRegular;
        }

        public Integer getD2D() {
            return D2D;
        }

        public void setD2D(Integer d2D) {
            D2D = d2D;
        }

        public String getCSCD2D() {
            return CSCD2D;
        }

        public void setCSCD2D(String CSCD2D) {
            this.CSCD2D = CSCD2D;
        }

        private Integer Regular;
        private String CSCRegular;
        private Integer D2D;
        private String CSCD2D;

        public Integer getTotal() {
            return Total;
        }

        public void setTotal(Integer total) {
            Total = total;
        }

        public String getRegularCamp() {
            return RegularCamp;
        }

        public void setRegularCamp(String regularCamp) {
            RegularCamp = regularCamp;
        }

        public String getD2DCamp() {
            return D2DCamp;
        }

        public void setD2DCamp(String d2DCamp) {
            D2DCamp = d2DCamp;
        }

        public String getCampCount() {
            return CampCount;
        }

        public void setCampCount(String campCount) {
            CampCount = campCount;
        }

        private String CampCount;


        @SerializedName("Total Camps")
        @Expose
        private String totalCamps;


        public String getFinancialYear() {
            return financialYear;
        }

        public void setFinancialYear(String financialYear) {
            this.financialYear = financialYear;
        }

        @SerializedName("FinancialYear")
        @Expose
        private String financialYear;

        @SerializedName("Total Camp")
        @Expose
        private Integer totalConducted;

        public Integer getD2dConductedCamp() {
            return d2dConductedCamp;
        }

        public void setD2dConductedCamp(Integer d2dConductedCamp) {
            this.d2dConductedCamp = d2dConductedCamp;
        }

        @SerializedName("Regular Camp")
        @Expose
        private Integer regularConductedCamp;

        public Integer getRegularConductedCamp() {
            return regularConductedCamp;
        }

        public void setRegularConductedCamp(Integer regularConductedCamp) {
            this.regularConductedCamp = regularConductedCamp;
        }

        @SerializedName("D2D Camp")
        @Expose
        private Integer d2dConductedCamp;

        public Integer getTotalConducted() {
            return totalConducted;
        }

        public void setTotalConducted(Integer totalConducted) {
            this.totalConducted = totalConducted;
        }



        @SerializedName("Total Month's Beneficiary Count")
        @Expose
        private String TotalMonthsBeneficiaryCount;

        @SerializedName("Today's Beneficiary Count")
        @Expose
        private String TodayBeneficiaryCount;

        @SerializedName("Zero Count Camp")
        @Expose
        private String ZeroCountCamp;

        @SerializedName("Today's Date")
        @Expose
        private String TodaysDate;

        public String getTotalCamps() {
            return totalCamps;
        }

        public void setTotalCamps(String totalCamps) {
            this.totalCamps = totalCamps;
        }

        public String getTotalMonthsBeneficiaryCount() {
            return TotalMonthsBeneficiaryCount;
        }

        public void setTotalMonthsBeneficiaryCount(String totalMonthsBeneficiaryCount) {
            TotalMonthsBeneficiaryCount = totalMonthsBeneficiaryCount;
        }

        public String getTodayBeneficiaryCount() {
            return TodayBeneficiaryCount;
        }

        public void setTodayBeneficiaryCount(String todayBeneficiaryCount) {
            TodayBeneficiaryCount = todayBeneficiaryCount;
        }

        public String getZeroCountCamp() {
            return ZeroCountCamp;
        }

        public void setZeroCountCamp(String zeroCountCamp) {
            ZeroCountCamp = zeroCountCamp;
        }

        public String getTodaysDate() {
            return TodaysDate;
        }

        public void setTodaysDate(String todaysDate) {
            TodaysDate = todaysDate;
        }

        public String getExpectedbeneficiarycount() {
            return Expectedbeneficiarycount;
        }

        public void setExpectedbeneficiarycount(String expectedbeneficiarycount) {
            Expectedbeneficiarycount = expectedbeneficiarycount;
        }

        public String getOrganizedby() {
            return Organizedby;
        }

        public void setOrganizedby(String organizedby) {
            Organizedby = organizedby;
        }

        public String getAdvanceFundStatus() {
            return AdvanceFundStatus;
        }

        public void setAdvanceFundStatus(String advanceFundStatus) {
            AdvanceFundStatus = advanceFundStatus;
        }

        public String getApprovedDate() {
            return ApprovedDate;
        }

        public void setApprovedDate(String approvedDate) {
            ApprovedDate = approvedDate;
        }

        public String getApprovedAmt() {
            return ApprovedAmt;
        }

        public void setApprovedAmt(String approvedAmt) {
            ApprovedAmt = approvedAmt;
        }

        public String getCurrentApprovalLevel() {
            return CurrentApprovalLevel;
        }

        public void setCurrentApprovalLevel(String currentApprovalLevel) {
            CurrentApprovalLevel = currentApprovalLevel;
        }

        public String getDemandAdvAmount() {
            return DemandAdvAmount;
        }

        public void setDemandAdvAmount(String demandAdvAmount) {
            DemandAdvAmount = demandAdvAmount;
        }

        private String AdvanceFundStatus;
        private String ApprovedDate;
        private String ApprovedAmt;
        private String CurrentApprovalLevel;
        private String DemandAdvAmount;


        public String getCampTypeDescription() {
            return CampTypeDescription;
        }

        public void setCampTypeDescription(String campTypeDescription) {
            CampTypeDescription = campTypeDescription;
        }

        public String getCampId() {
            return CampId;
        }

        public void setCampId(String CampId) {
            this.CampId = CampId;
        }

        public String getCampNo() {
            return CampNo;
        }

        public void setCampNo(String CampNo) {
            this.CampNo = CampNo;
        }

        public String getCampLocation() {
            return CampLocation;
        }

        public void setCampLocation(String CampLocation) {
            this.CampLocation = CampLocation;
        }

        public String getCampDate() {
            return CampDate;
        }

        public void setCampDate(String CampDate) {
            this.CampDate = CampDate;
        }

        public String getStatus() {
            return Status;
        }

        public void setStatus(String Status) {
            this.Status = Status;
        }

        public String getDescription() {
            return Description;
        }

        public void setDescription(String Description) {
            this.Description = Description;
        }

        public String getDISTLGDCODE() {
            return DISTLGDCODE;
        }

        public void setDISTLGDCODE(String DISTLGDCODE) {
            this.DISTLGDCODE = DISTLGDCODE;
        }

        public String getDISTNAME() {
            return DISTNAME;
        }

        public void setDISTNAME(String DISTNAME) {
            this.DISTNAME = DISTNAME;
        }

        public String getREGISTERWORKERS() {
            return REGISTERWORKERS;
        }

        public void setREGISTERWORKERS(String REGISTERWORKERS) {
            this.REGISTERWORKERS = REGISTERWORKERS;
        }

        public String getUNREGISTEREDWORKERS() {
            return UNREGISTEREDWORKERS;
        }

        public void setUNREGISTEREDWORKERS(String UNREGISTEREDWORKERS) {
            this.UNREGISTEREDWORKERS = UNREGISTEREDWORKERS;
        }

        public String getSurveyCoordinatorName() {
            return SurveyCoordinatorName;
        }

        public void setSurveyCoordinatorName(String SurveyCoordinatorName) {
            this.SurveyCoordinatorName = SurveyCoordinatorName;
        }

        public String getMOBNO() {
            return MOBNO;
        }

        public void setMOBNO(String MOBNO) {
            this.MOBNO = MOBNO;
        }

        public String getScreeningDone() {
            return ScreeningDone;
        }

        public void setScreeningDone(String screeningDone) {
            ScreeningDone = screeningDone;
        }

        public String getScreeningNotDone() {
            return ScreeningNotDone;
        }

        public void setScreeningNotDone(String screeningNotDone) {
            ScreeningNotDone = screeningNotDone;
        }

        public String getCampStatus() {
            return CampStatus;
        }

        public void setCampStatus(String campStatus) {
            CampStatus = campStatus;
        }

        public String getCordinatorName() {
            return CordinatorName;
        }

        public void setCordinatorName(String cordinatorName) {
            CordinatorName = cordinatorName;
        }

        public String getMOBNO1() {
            return MOBNO1;
        }

        public void setMOBNO1(String MOBNO1) {
            this.MOBNO1 = MOBNO1;
        }

        public String getCampName() {
            if (CampName != null) {
                return CampName;
            } else {
                return "NA";
            }
        }

        public void setCampName(String campName) {
            CampName = campName;
        }
    }
}
