package com.myhindlab.abkat.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class SampleProcessingLabModel implements Parcelable {

    private String status;
    private String message;
    private List<OutputBean> output;

    protected SampleProcessingLabModel(Parcel in) {
        status = in.readString();
        message = in.readString();
        output = in.createTypedArrayList(OutputBean.CREATOR);
    }

    public static final Creator<SampleProcessingLabModel> CREATOR = new Creator<SampleProcessingLabModel>() {
        @Override
        public SampleProcessingLabModel createFromParcel(Parcel in) {
            return new SampleProcessingLabModel(in);
        }

        @Override
        public SampleProcessingLabModel[] newArray(int size) {
            return new SampleProcessingLabModel[size];
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
        dest.writeTypedList(output);
    }

    public static class OutputBean implements Parcelable {
        /**
         * LabCode : 67
         * LabName : Akola
         * TOTAL : 337
         * REJECTED : 3
         * PENDING : 267
         * COMPLETED : 67
         */

        private int LabCode;
        private String LabName;
        private int TOTAL;
        private int REJECTED;
        private int PENDING;
        private int COMPLETED;

        protected OutputBean(Parcel in) {
            LabCode = in.readInt();
            LabName = in.readString();
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

        public int getLabCode() {
            return LabCode;
        }

        public void setLabCode(int LabCode) {
            this.LabCode = LabCode;
        }

        public String getLabName() {
            return LabName;
        }

        public void setLabName(String LabName) {
            this.LabName = LabName;
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
            dest.writeInt(LabCode);
            dest.writeString(LabName);
            dest.writeInt(TOTAL);
            dest.writeInt(REJECTED);
            dest.writeInt(PENDING);
            dest.writeInt(COMPLETED);
        }
    }
}
