package com.myhindlab.abkat.models.doortodoor;


public class CallRequestForVodaphone {
    public String cli;
    public String apartyno;
    public String bpartyno;
    public String reference_id;
    public String channelflag;
    public String dtmfflag;
    public String recordingflag;

    public CallRequestForVodaphone(String cli, String apartyno, String bpartyno,
                                   String reference_id, String channelflag, String dtmfflag, String recordingflag) {
        this.cli = cli;
        this.apartyno = apartyno;
        this.bpartyno = bpartyno;
        this.reference_id = reference_id;
        this.channelflag = channelflag;
        this.dtmfflag = dtmfflag;
        this.recordingflag = recordingflag;
    }
}
