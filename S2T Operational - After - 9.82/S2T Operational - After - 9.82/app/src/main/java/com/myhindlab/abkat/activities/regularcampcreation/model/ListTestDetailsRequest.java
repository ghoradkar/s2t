package com.myhindlab.abkat.activities.regularcampcreation.model;

import java.util.List;

public class ListTestDetailsRequest {

    private List<ListTestDetails> listTestDetails;

    public ListTestDetailsRequest(List<ListTestDetails> listTestDetails) {
        this.listTestDetails = listTestDetails;
    }

    public List<ListTestDetails> getListTestDetails() {
        return listTestDetails;
    }
}