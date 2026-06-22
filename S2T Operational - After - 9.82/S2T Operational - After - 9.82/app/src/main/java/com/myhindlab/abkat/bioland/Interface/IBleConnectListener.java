package com.myhindlab.abkat.bioland.Interface;

//import com.inuker.bluetooth.library.model.BleGattProfile;

public interface IBleConnectListener {
    void onStartConnect();

    void onConnectFail();

//    void onConnectSuccess(int code, BleGattProfile data, String mac);

    void onDisConnected(String mac);
}
