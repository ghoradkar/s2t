package com.myhindlab.abkat.activities.regularcampcreation.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.myhindlab.abkat.models.DistrictList_Model;
import com.myhindlab.abkat.models.LandingLabModel;
import com.myhindlab.abkat.models.SubDivisionModel;
import com.myhindlab.abkat.models.TalukaModel;

import java.io.Serializable;
import java.util.List;

public class FilterDataModel implements Serializable {
    @SerializedName("selectedDivisions")
    @Expose
    private  List<SubDivisionModel.Output> divisionList;


    @SerializedName("selectedDistrict")
    @Expose
    private  List<DistrictList_Model> districtList;



    @SerializedName("selectedTaluka")
    @Expose
    private  List<TalukaModel.Output> talukaList;



    @SerializedName("selectedLab")
    @Expose
    private  List<LandingLabModel.Output> labList;


    @SerializedName("selectedArea")
    @Expose
    private  List<LandingLabModel.Output> areaList;








    public List<SubDivisionModel.Output> getSiteList() {
        return divisionList;
    }

    public void setSiteList(List<SubDivisionModel.Output> siteList) {
        this.divisionList = siteList;
    }









    @SerializedName("ISD2DCamp")
    @Expose
    private int ISD2DCamp;

    public int getISD2DCamp() {
        return ISD2DCamp;
    }

    public void setISD2DCamp(int ISD2DCamp) {
        this.ISD2DCamp = ISD2DCamp;
    }


}