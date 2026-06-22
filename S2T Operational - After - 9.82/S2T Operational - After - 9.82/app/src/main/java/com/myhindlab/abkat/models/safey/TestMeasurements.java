package com.myhindlab.abkat.models.safey;

public class TestMeasurements {
    String measurement = null;
    Double measuredValue = 0.0;
    String predictedValue = null;
    String unit = null;
    String lln = null;
    String uln = null;
    String zScore = null;
    Double predictedPer  = 0.0;

    public String getMeasurement() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }

    public Double getMeasuredValue() {
        return measuredValue;
    }

    public void setMeasuredValue(Double measuredValue) {
        this.measuredValue = measuredValue;
    }

    public String getPredictedValue() {
        return predictedValue;
    }

    public void setPredictedValue(String predictedValue) {
        this.predictedValue = predictedValue;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getLln() {
        return lln;
    }

    public void setLln(String lln) {
        this.lln = lln;
    }

    public String getUln() {
        return uln;
    }

    public void setUln(String uln) {
        this.uln = uln;
    }

    public String getzScore() {
        return zScore;
    }

    public void setzScore(String zScore) {
        this.zScore = zScore;
    }

    public Double getPredictedPer() {
        return predictedPer;
    }

    public void setPredictedPer(Double predictedPer) {
        this.predictedPer = predictedPer;
    }
}
