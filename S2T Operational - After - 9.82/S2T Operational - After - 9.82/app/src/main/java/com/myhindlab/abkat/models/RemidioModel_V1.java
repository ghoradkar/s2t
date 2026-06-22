package com.myhindlab.abkat.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class RemidioModel_V1 {

    @SerializedName("examID")
    @Expose
    private String examID;
    @SerializedName("patientAge")
    @Expose
    private Integer patientAge;
    @SerializedName("Userid")
    @Expose
    private Integer userId;
    @SerializedName("result")
    @Expose
    private Result result;

    public String getExamID() {
        return examID;
    }

    public void setExamID(String examID) {
        this.examID = examID;
    }

    public Integer getPatientAge() {
        return patientAge;
    }

    public void setPatientAge(Integer patientAge) {
        this.patientAge = patientAge;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public class L1 {

        @SerializedName("A")
        @Expose
        private String a;
        @SerializedName("C")
        @Expose
        private String c;
        @SerializedName("S")
        @Expose
        private String s;

        public String getA() {
            return a;
        }

        public void setA(String a) {
            this.a = a;
        }

        public String getC() {
            return c;
        }

        public void setC(String c) {
            this.c = c;
        }

        public String getS() {
            return s;
        }

        public void setS(String s) {
            this.s = s;
        }

    }

    public class L2 {

        @SerializedName("A")
        @Expose
        private String a;
        @SerializedName("C")
        @Expose
        private String c;
        @SerializedName("S")
        @Expose
        private String s;

        public String getA() {
            return a;
        }

        public void setA(String a) {
            this.a = a;
        }

        public String getC() {
            return c;
        }

        public void setC(String c) {
            this.c = c;
        }

        public String getS() {
            return s;
        }

        public void setS(String s) {
            this.s = s;
        }

    }

    public class L3 {

        @SerializedName("A")
        @Expose
        private String a;
        @SerializedName("C")
        @Expose
        private String c;
        @SerializedName("S")
        @Expose
        private String s;

        public String getA() {
            return a;
        }

        public void setA(String a) {
            this.a = a;
        }

        public String getC() {
            return c;
        }

        public void setC(String c) {
            this.c = c;
        }

        public String getS() {
            return s;
        }

        public void setS(String s) {
            this.s = s;
        }

    }

    public class LAvg {

        @SerializedName("A")
        @Expose
        private String a;
        @SerializedName("C")
        @Expose
        private String c;
        @SerializedName("S")
        @Expose
        private String s;

        public String getA() {
            return a;
        }

        public void setA(String a) {
            this.a = a;
        }

        public String getC() {
            return c;
        }

        public void setC(String c) {
            this.c = c;
        }

        public String getS() {
            return s;
        }

        public void setS(String s) {
            this.s = s;
        }

    }

    public class R1 {

        @SerializedName("A")
        @Expose
        private String a;
        @SerializedName("C")
        @Expose
        private String c;
        @SerializedName("S")
        @Expose
        private String s;

        public String getA() {
            return a;
        }

        public void setA(String a) {
            this.a = a;
        }

        public String getC() {
            return c;
        }

        public void setC(String c) {
            this.c = c;
        }

        public String getS() {
            return s;
        }

        public void setS(String s) {
            this.s = s;
        }

    }

    public class R2 {

        @SerializedName("A")
        @Expose
        private String a;
        @SerializedName("C")
        @Expose
        private String c;
        @SerializedName("S")
        @Expose
        private String s;

        public String getA() {
            return a;
        }

        public void setA(String a) {
            this.a = a;
        }

        public String getC() {
            return c;
        }

        public void setC(String c) {
            this.c = c;
        }

        public String getS() {
            return s;
        }

        public void setS(String s) {
            this.s = s;
        }

    }

    public class R3 {

        @SerializedName("A")
        @Expose
        private String a;
        @SerializedName("C")
        @Expose
        private String c;
        @SerializedName("S")
        @Expose
        private String s;

        public String getA() {
            return a;
        }

        public void setA(String a) {
            this.a = a;
        }

        public String getC() {
            return c;
        }

        public void setC(String c) {
            this.c = c;
        }

        public String getS() {
            return s;
        }

        public void setS(String s) {
            this.s = s;
        }

    }

    public class RAvg {

        @SerializedName("A")
        @Expose
        private String a;
        @SerializedName("C")
        @Expose
        private String c;
        @SerializedName("S")
        @Expose
        private String s;

        public String getA() {
            return a;
        }

        public void setA(String a) {
            this.a = a;
        }

        public String getC() {
            return c;
        }

        public void setC(String c) {
            this.c = c;
        }

        public String getS() {
            return s;
        }

        public void setS(String s) {
            this.s = s;
        }

    }


    public class Result {

        @SerializedName("L1")
        @Expose
        private L1 l1;
        @SerializedName("R1")
        @Expose
        private R1 r1;
        @SerializedName("L_avg")
        @Expose
        private LAvg lAvg;
        @SerializedName("R3")
        @Expose
        private R3 r3;
        @SerializedName("L2")
        @Expose
        private L2 l2;
        @SerializedName("R2")
        @Expose
        private R2 r2;
        @SerializedName("L3")
        @Expose
        private L3 l3;
        @SerializedName("R_avg")
        @Expose
        private RAvg rAvg;

        public L1 getL1() {
            return l1;
        }

        public void setL1(L1 l1) {
            this.l1 = l1;
        }

        public R1 getR1() {
            return r1;
        }

        public void setR1(R1 r1) {
            this.r1 = r1;
        }

        public LAvg getLAvg() {
            return lAvg;
        }

        public void setLAvg(LAvg lAvg) {
            this.lAvg = lAvg;
        }

        public R3 getR3() {
            return r3;
        }

        public void setR3(R3 r3) {
            this.r3 = r3;
        }

        public L2 getL2() {
            return l2;
        }

        public void setL2(L2 l2) {
            this.l2 = l2;
        }

        public R2 getR2() {
            return r2;
        }

        public void setR2(R2 r2) {
            this.r2 = r2;
        }

        public L3 getL3() {
            return l3;
        }

        public void setL3(L3 l3) {
            this.l3 = l3;
        }

        public RAvg getRAvg() {
            return rAvg;
        }

        public void setRAvg(RAvg rAvg) {
            this.rAvg = rAvg;
        }

    }

}
