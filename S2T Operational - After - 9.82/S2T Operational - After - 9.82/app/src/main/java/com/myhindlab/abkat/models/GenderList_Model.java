package com.myhindlab.abkat.models;

public class GenderList_Model {

    private String gender;
    private String genderId;

    public GenderList_Model(String gender, String genderId) {
        this.gender = gender;
        this.genderId = genderId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGenderId() {
        return genderId;
    }

    public void setGenderId(String genderId) {
        this.genderId = genderId;
    }
}
