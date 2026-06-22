package com.myhindlab.abkat.models;

import java.util.ArrayList;

public class CampListModel {

    private String status;
    private String message;
    private ArrayList<CampListModel.OutputBean> output;

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

    public ArrayList<CampListModel.OutputBean> getOutput() {
        return output;
    }

    public void setOutput(ArrayList<CampListModel.OutputBean> output) {
        this.output = output;
    }

    public static class OutputBean {


        private String    TeamID;
        private String    TeamName;
        private String    UserName;
        private String    MemberUserID1;
        private String    MemberUserID2;
        private String    Member2;
        private String    Member1;


        private String    USERNAME;
        private String    USERMOBNO;

        public String getUSERNAME() {
            return USERNAME;
        }

        public void setUSERNAME(String USERNAME) {
            this.USERNAME = USERNAME;
        }

        public String getUSERMOBNO() {
            return USERMOBNO;
        }

        public void setUSERMOBNO(String USERMOBNO) {
            this.USERMOBNO = USERMOBNO;
        }

        public String getMapStatus() {
            return MapStatus = "1";
        }

        public void setMapStatus(String mapStatus) {
            MapStatus = mapStatus;
        }

        private String    USERID;
        private String    CampName;
        private String    ResourceMappingFlag;

        private String  MapStatus;

        private String CampReadinessFlag;

        public String getCampReadinessFlag() {
            return CampReadinessFlag;
        }

        public void setCampReadinessFlag(String campReadinessFlag) {
            CampReadinessFlag = campReadinessFlag;
        }

        public String getResourceMappingFlag() {
            return ResourceMappingFlag;
        }

        public void setResourceMappingFlag(String resourceMappingFlag) {
            ResourceMappingFlag = resourceMappingFlag;
        }

        public String getCampName() {
            return CampName;
        }

        public void setCampName(String campName) {
            CampName = campName;
        }

        private String        CampId;
        private String        SiteDetailId;
        private String       CampNo;
        private String       CampLocation;
        private String        CampDate;
        private String       DISTLGDCODE;
        private String        DISTNAME;
        private String        Remark;
        private String        CampType;
        private String        Expectedbeneficiarycount;
        private String        LABCODE;
        private String        CreatedBy;
        private String        LabName;
        private String        CampConfirmation;

        public String getLabName() {
            return LabName;
        }

        public String getCampConfirmation() {
            return CampConfirmation;
        }

        public void setCampConfirmation(String campConfirmation) {
            CampConfirmation = campConfirmation;
        }

        public void setLabName(String labName) {
            LabName = labName;
        }

        public String getCreatedBy() {
            return CreatedBy;
        }

        public void setCreatedBy(String createdBy) {
            CreatedBy = createdBy;
        }

        public String getExpectedbeneficiarycount() {
            return Expectedbeneficiarycount;
        }

        public void setExpectedbeneficiarycount(String expectedbeneficiarycount) {
            Expectedbeneficiarycount = expectedbeneficiarycount;
        }

        public String getLABCODE() {
            return LABCODE;
        }

        public void setLABCODE(String LABCODE) {
            this.LABCODE = LABCODE;
        }

        public String getCampCreatedBy() {
            return CampCreatedBy;
        }

        public void setCampCreatedBy(String campCreatedBy) {
            CampCreatedBy = campCreatedBy;
        }

        private String        CampCreatedBy;

        public String getCampId() {
            return CampId;
        }

        public void setCampId(String campId) {
            CampId = campId;
        }

        public String getSiteDetailId() {
            return SiteDetailId;
        }

        public void setSiteDetailId(String siteDetailId) {
            SiteDetailId = siteDetailId;
        }

        public String getCampNo() {
            return CampNo;
        }

        public void setCampNo(String campNo) {
            CampNo = campNo;
        }

        public String getCampLocation() {
            return CampLocation;
        }

        public void setCampLocation(String campLocation) {
            CampLocation = campLocation;
        }

        public String getCampDate() {
            return CampDate;
        }

        public void setCampDate(String campDate) {
            CampDate = campDate;
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

        public String getRemark() {
            return Remark;
        }

        public void setRemark(String remark) {
            Remark = remark;
        }

        public String getCampType() {
            return CampType;
        }

        public void setCampType(String campType) {
            CampType = campType;
        }

        public String getUSERID() {
            return USERID;
        }

        public void setUSERID(String USERID) {
            this.USERID = USERID;
        }

        public String getResourceName() {
            return ResourceName;
        }

        public void setResourceName(String resourceName) {
            ResourceName = resourceName;
        }

        private String    ResourceName;

        public String getMemberUserID1() {
            return MemberUserID1;
        }

        public void setMemberUserID1(String memberUserID1) {
            MemberUserID1 = memberUserID1;
        }

        public String getMemberUserID2() {
            return MemberUserID2;
        }

        public void setMemberUserID2(String memberUserID2) {
            MemberUserID2 = memberUserID2;
        }

        public String getMember2() {
            return Member2;
        }

        public void setMember2(String member2) {
            Member2 = member2;
        }

        public String getMember1() {
            return Member1;
        }

        public void setMember1(String member1) {
            Member1 = member1;
        }

        public String getUserName() {
            return UserName;
        }

        public void setUserName(String userName) {
            UserName = userName;
        }

        private boolean isChecked;

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        public String getTeamID() {
            return TeamID;
        }

        public void setTeamID(String teamID) {
            TeamID = teamID;
        }

        public String getTeamName() {
            return TeamName;
        }

        public void setTeamName(String teamName) {
            TeamName = teamName;
        }
    }



}
