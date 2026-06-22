package com.myhindlab.abkat.bioland.Interface;


public interface IBleIndicateListener {

    void onIndicateSuccess();

    void onIndicateFailure(int code);

    void onCharacteristicChanged(byte[] data);
}
