package com.myhindlab.abkat.utilities;

public class Communicator {

    public interface OnCustomStateListener {
        void stateChanged();
    }

    private static Communicator mInstance;
    private OnCustomStateListener mListener;

    private Communicator() {}

    public static Communicator getInstance() {
        if(mInstance == null) {
            mInstance = new Communicator();
        }
        return mInstance;
    }

    public void setListener(OnCustomStateListener listener) {
        mListener = listener;
    }

    public void changeState() {
        if(mListener != null) {
            mListener.stateChanged();
        }
    }
}
