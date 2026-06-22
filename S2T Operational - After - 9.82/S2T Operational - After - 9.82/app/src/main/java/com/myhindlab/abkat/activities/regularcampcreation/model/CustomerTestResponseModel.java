package com.myhindlab.abkat.activities.regularcampcreation.model;

import java.util.List;

public class CustomerTestResponseModel {

    private String customerId;
    private String customerCode;
    private String customerName;
    private List<TestDetailsDto> listTestDetailsDto;

    // Getters & Setters

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public List<TestDetailsDto> getListTestDetailsDto() {
        return listTestDetailsDto;
    }

    public void setListTestDetailsDto(List<TestDetailsDto> listTestDetailsDto) {
        this.listTestDetailsDto = listTestDetailsDto;
    }
}