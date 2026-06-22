package com.myhindlab.abkat.models.safey;

import java.util.List;

public class TrialResult {

    List<AirGraphData> graphDataList = null;
    List<TestMeasurements> mesurementlist = null;
    Boolean isBest = false;
    Boolean isPost = false;

    public List<AirGraphData> getGraphDataList() {
        return graphDataList;
    }

    public void setGraphDataList(List<AirGraphData> graphDataList) {
        this.graphDataList = graphDataList;
    }

    public List<TestMeasurements> getMesurementlist() {
        return mesurementlist;
    }

    public void setMesurementlist(List<TestMeasurements> mesurementlist) {
        this.mesurementlist = mesurementlist;
    }

    public Boolean getBest() {
        return isBest;
    }

    public void setBest(Boolean best) {
        isBest = best;
    }

    public Boolean getPost() {
        return isPost;
    }

    public void setPost(Boolean post) {
        isPost = post;
    }
}
