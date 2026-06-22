package com.myhindlab.abkat.activities.campredinessnew;

import com.google.gson.annotations.SerializedName;
import com.myhindlab.abkat.activities.campredinessnew.Type_CampReadinessForm;

import java.util.ArrayList;

public class Campredinessmodel {

    @SerializedName("Type_CampReadinessForm")
    ArrayList<Type_CampReadinessForm> surverlists;

    public Campredinessmodel(ArrayList<Type_CampReadinessForm> surverlists) {
        this.surverlists = surverlists;
    }
}
