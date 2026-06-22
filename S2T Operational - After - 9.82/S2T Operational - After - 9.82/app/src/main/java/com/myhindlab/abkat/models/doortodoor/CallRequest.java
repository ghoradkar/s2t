package com.myhindlab.abkat.models.doortodoor;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class CallRequest {
    public String company_id;
    public String secret_token;
    public String type;
    public String user_id;
    public String number;
    public String public_ivr_id;
    public String reference_id;

    public CallRequest(String company_id, String secret_token, String type, String user_id,
                       String number, String public_ivr_id, String reference_id) {
        this.company_id = company_id;
        this.secret_token = secret_token;
        this.type = type;
        this.user_id = user_id;
        this.number = number;
        this.public_ivr_id = public_ivr_id;
        this.reference_id = reference_id;
    }
}
