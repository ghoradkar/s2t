package com.myhindlab.abkat.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class SampleProcessingCampModel implements Parcelable {


    /**
     * status : Success
     * message : Result
     * output : [{"DISTNAME":"AKOLA","CampId":233,"CampNo":"CAMP_467_0000002","CampLocation":"Unnamed Road, Sopinath Nagar, Bhaurad, Maharashtra 444002, India","CampDate":"08-Sep-2019","CampName":"Bhaurad Atal Camp","TOTAL":70,"REJECTED":3,"PENDING":1,"COMPLETED":66},{"DISTNAME":"AKOLA","CampId":298,"CampNo":"CAMP_467_0000003","CampLocation":"Akot Fail","CampDate":"14-Sep-2019","CampName":"Akot Fail Akola","TOTAL":93,"REJECTED":0,"PENDING":93,"COMPLETED":0},{"DISTNAME":"AKOLA","CampId":310,"CampNo":"CAMP_467_0000005","CampLocation":"Akot fail","CampDate":"15-Sep-2019","CampName":"Akot fail","TOTAL":174,"REJECTED":0,"PENDING":173,"COMPLETED":1}]
     */

    private String status;
    private String message;
    private List<OutputBean> output;

    protected SampleProcessingCampModel(Parcel in) {
        status = in.readString();
        message = in.readString();
    }

    public static final Creator<SampleProcessingCampModel> CREATOR = new Creator<SampleProcessingCampModel>() {
        @Override
        public SampleProcessingCampModel createFromParcel(Parcel in) {
            return new SampleProcessingCampModel(in);
        }

        @Override
        public SampleProcessingCampModel[] newArray(int size) {
            return new SampleProcessingCampModel[size];
        }
    };

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<OutputBean> getOutput() {
        return output;
    }

    public void setOutput(List<OutputBean> output) {
        this.output = output;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(status);
        dest.writeString(message);
    }

    public static class OutputBean implements Parcelable {
        /**
         * DISTNAME : AKOLA
         * CampId : 233
         * CampNo : CAMP_467_0000002
         * CampLocation : Unnamed Road, Sopinath Nagar, Bhaurad, Maharashtra 444002, India
         * CampDate : 08-Sep-2019
         * CampName : Bhaurad Atal Camp
         * TOTAL : 70
         * REJECTED : 3
         * PENDING : 1
         * COMPLETED : 66
         */

        private String DISTNAME;
        private int CampId;
        private String CampNo;
        private String CampLocation;
        private String CampDate;
        private String CampName;
        private int TOTAL;
        private int REJECTED;
        private int PENDING;
        private int COMPLETED;

        protected OutputBean(Parcel in) {
            DISTNAME = in.readString();
            CampId = in.readInt();
            CampNo = in.readString();
            CampLocation = in.readString();
            CampDate = in.readString();
            CampName = in.readString();
            TOTAL = in.readInt();
            REJECTED = in.readInt();
            PENDING = in.readInt();
            COMPLETED = in.readInt();
        }

        public static final Creator<OutputBean> CREATOR = new Creator<OutputBean>() {
            @Override
            public OutputBean createFromParcel(Parcel in) {
                return new OutputBean(in);
            }

            @Override
            public OutputBean[] newArray(int size) {
                return new OutputBean[size];
            }
        };

        public String getDISTNAME() {
            return DISTNAME;
        }

        public void setDISTNAME(String DISTNAME) {
            this.DISTNAME = DISTNAME;
        }

        public int getCampId() {
            return CampId;
        }

        public void setCampId(int CampId) {
            this.CampId = CampId;
        }

        public String getCampNo() {
            return CampNo;
        }

        public void setCampNo(String CampNo) {
            this.CampNo = CampNo;
        }

        public String getCampLocation() {
            return CampLocation;
        }

        public void setCampLocation(String CampLocation) {
            this.CampLocation = CampLocation;
        }

        public String getCampDate() {
            return CampDate;
        }

        public void setCampDate(String CampDate) {
            this.CampDate = CampDate;
        }

        public String getCampName() {
            return CampName;
        }

        public void setCampName(String CampName) {
            this.CampName = CampName;
        }

        public int getTOTAL() {
            return TOTAL;
        }

        public void setTOTAL(int TOTAL) {
            this.TOTAL = TOTAL;
        }

        public int getREJECTED() {
            return REJECTED;
        }

        public void setREJECTED(int REJECTED) {
            this.REJECTED = REJECTED;
        }

        public int getPENDING() {
            return PENDING;
        }

        public void setPENDING(int PENDING) {
            this.PENDING = PENDING;
        }

        public int getCOMPLETED() {
            return COMPLETED;
        }

        public void setCOMPLETED(int COMPLETED) {
            this.COMPLETED = COMPLETED;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(DISTNAME);
            dest.writeInt(CampId);
            dest.writeString(CampNo);
            dest.writeString(CampLocation);
            dest.writeString(CampDate);
            dest.writeString(CampName);
            dest.writeInt(TOTAL);
            dest.writeInt(REJECTED);
            dest.writeInt(PENDING);
            dest.writeInt(COMPLETED);
        }
    }
}
