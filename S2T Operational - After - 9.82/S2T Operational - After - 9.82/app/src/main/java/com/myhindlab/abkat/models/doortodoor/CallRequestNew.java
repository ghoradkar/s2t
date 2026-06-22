package com.myhindlab.abkat.models.doortodoor;


public class CallRequestNew {
    public String company_id;
    public String secret_token;
    public String type;
    public String number_2;
    public String number;
    public String public_ivr_id;
    public String reference_id;
    public Integer max_call_duration;
    public String region;
    public String caller_id;
    public String group;
    public boolean call_hold;

    public CallRequestNew(String company_id, String secret_token, String type, String number_2,
                          String number, String public_ivr_id, String reference_id,Integer max_call_duration,String region,String caller_id,String group, boolean call_hold ) {
        this.company_id = company_id;
        this.secret_token = secret_token;
        this.type = type;
        this.number_2 = number_2;
        this.number = number;
        this.public_ivr_id = public_ivr_id;
        this.reference_id = reference_id;
        this.max_call_duration = max_call_duration;
        this.region = region;
        this.caller_id = caller_id;
        this.group = group;
        this.call_hold = call_hold;
    }
}
