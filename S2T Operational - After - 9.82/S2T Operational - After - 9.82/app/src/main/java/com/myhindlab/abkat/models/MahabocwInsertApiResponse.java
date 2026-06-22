package com.myhindlab.abkat.models;

public class MahabocwInsertApiResponse {


    /**
     * statusCode : 500
     * message : Internal server error
     */

    private int statusCode;
    private String message;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
