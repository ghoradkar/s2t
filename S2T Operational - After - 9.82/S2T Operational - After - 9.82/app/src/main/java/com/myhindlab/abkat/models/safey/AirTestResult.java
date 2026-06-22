package com.myhindlab.abkat.models.safey;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import info.safey.safey_sdk.Variance;

public class AirTestResult {
    List<TrialResult> trialResult = new ArrayList();
    int type = 0;
    int testtype = 0;
    Date createdAt = null;
    String createdDate = null;
    String testtime = null;
    Boolean active = null;
    String sessionScore = null;
    String suggestedDiagnosis = null;
    List<Variance> variance = new ArrayList();
    int isSync = 0;
    String id = "";

    public List<TrialResult> getTrialResult() {
        return trialResult;
    }

    public void setTrialResult(List<TrialResult> trialResult) {
        this.trialResult = trialResult;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getTesttype() {
        return testtype;
    }

    public void setTesttype(int testtype) {
        this.testtype = testtype;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getTesttime() {
        return testtime;
    }

    public void setTesttime(String testtime) {
        this.testtime = testtime;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getSessionScore() {
        return sessionScore;
    }

    public void setSessionScore(String sessionScore) {
        this.sessionScore = sessionScore;
    }

    public String getSuggestedDiagnosis() {
        return suggestedDiagnosis;
    }

    public void setSuggestedDiagnosis(String suggestedDiagnosis) {
        this.suggestedDiagnosis = suggestedDiagnosis;
    }

    public List<Variance> getVariance() {
        return variance;
    }

    public void setVariance(List<Variance> variance) {
        this.variance = variance;
    }

    public int getIsSync() {
        return isSync;
    }

    public void setIsSync(int isSync) {
        this.isSync = isSync;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    String guid = UUID.randomUUID().toString();

    Long creationDate = 0L;

    Long modificationDate = 0L;

    @NotNull
    public String getGuid() {
        return guid;
    }

    public void setGuid(@NotNull String guid) {
        this.guid = guid;
    }

    public Long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Long creationDate) {
        this.creationDate = creationDate;
    }

    public Long getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Long modificationDate) {
        this.modificationDate = modificationDate;
    }
}


