package com.myhindlab.abkat.abha.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ABHADataResponse {

    @SerializedName("requestId")
    @Expose
    private String requestId;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;
    @SerializedName("auth")
    @Expose
    private Auth auth;
    @SerializedName("error")
    @Expose
    private Object error;
    @SerializedName("resp")
    @Expose
    private Resp resp;

    public class Resp {

        @SerializedName("requestId")
        @Expose
        private String requestId;

        public String getRequestId() {
            return requestId;
        }

        public void setRequestId(String requestId) {
            this.requestId = requestId;
        }

    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Auth getAuth() {
        return auth;
    }

    public void setAuth(Auth auth) {
        this.auth = auth;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }

    public Resp getResp() {
        return resp;
    }

    public void setResp(Resp resp) {
        this.resp = resp;
    }


    public class Address {

        @SerializedName("line")
        @Expose
        private Object line;
        @SerializedName("district")
        @Expose
        private String district;
        @SerializedName("state")
        @Expose
        private String state;
        @SerializedName("pincode")
        @Expose
        private Object pincode;

        public Object getLine() {
            return line;
        }

        public void setLine(Object line) {
            this.line = line;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public Object getPincode() {
            return pincode;
        }

        public void setPincode(Object pincode) {
            this.pincode = pincode;
        }

    }


    public class Auth {

        @SerializedName("accessToken")
        @Expose
        private String accessToken;
        @SerializedName("patient")
        @Expose
        private Patient patient;

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public Patient getPatient() {
            return patient;
        }

        public void setPatient(Patient patient) {
            this.patient = patient;
        }

    }


    public class Identifier {

        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("value")
        @Expose
        private String value;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }


    public class Patient {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("gender")
        @Expose
        private String gender;
        @SerializedName("yearOfBirth")
        @Expose
        private Integer yearOfBirth;
        @SerializedName("monthOfBirth")
        @Expose
        private Integer monthOfBirth;
        @SerializedName("dayOfBirth")
        @Expose
        private Integer dayOfBirth;
        @SerializedName("address")
        @Expose
        private Address address;
        @SerializedName("identifiers")
        @Expose
        private List<Identifier> identifiers;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public Integer getYearOfBirth() {
            return yearOfBirth;
        }

        public void setYearOfBirth(Integer yearOfBirth) {
            this.yearOfBirth = yearOfBirth;
        }

        public Integer getMonthOfBirth() {
            return monthOfBirth;
        }

        public void setMonthOfBirth(Integer monthOfBirth) {
            this.monthOfBirth = monthOfBirth;
        }

        public Integer getDayOfBirth() {
            return dayOfBirth;
        }

        public void setDayOfBirth(Integer dayOfBirth) {
            this.dayOfBirth = dayOfBirth;
        }

        public Address getAddress() {
            return address;
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public List<Identifier> getIdentifiers() {
            return identifiers;
        }

        public void setIdentifiers(List<Identifier> identifiers) {
            this.identifiers = identifiers;
        }

    }
}


