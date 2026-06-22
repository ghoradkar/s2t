package com.myhindlab.abkat.activities.attendance_details.model;


import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class AttendanceDetailsModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("output")
    @Expose
    private List<Output> output;

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

    public List<Output> getOutput() {
        return output;
    }

    public void setOutput(List<Output> output) {
        this.output = output;
    }


    public class Output {

        @SerializedName("USERID")
        @Expose
        private Integer userid;
        @SerializedName("MemberName")
        @Expose
        private String memberName;
        @SerializedName("InTime")
        @Expose
        private String inTime;
        @SerializedName("INDistanceInKM")
        @Expose
        private String iNDistanceInKM;
        @SerializedName("OUTTIME")
        @Expose
        private String outtime;
        @SerializedName("OutDistanceInKM")
        @Expose
        private String outDistanceInKM;

        public Integer getUserid() {
            return userid;
        }

        public void setUserid(Integer userid) {
            this.userid = userid;
        }

        public String getMemberName() {
            return memberName;
        }

        public void setMemberName(String memberName) {
            this.memberName = memberName;
        }

        public String getInTime() {
            return inTime;
        }

        public void setInTime(String inTime) {
            this.inTime = inTime;
        }

        public String getINDistanceInKM() {
            return iNDistanceInKM;
        }

        public void setINDistanceInKM(String iNDistanceInKM) {
            this.iNDistanceInKM = iNDistanceInKM;
        }

        public String getOuttime() {
            return outtime;
        }

        public void setOuttime(String outtime) {
            this.outtime = outtime;
        }

        public String getOutDistanceInKM() {
            return outDistanceInKM;
        }

        public void setOutDistanceInKM(String outDistanceInKM) {
            this.outDistanceInKM = outDistanceInKM;
        }

    }
}