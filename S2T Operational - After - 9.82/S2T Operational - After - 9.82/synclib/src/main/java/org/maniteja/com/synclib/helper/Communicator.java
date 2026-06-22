package org.maniteja.com.synclib.helper;

import java.util.ArrayList;

/**
 * Created by Sreyas V Pariyath on 6/15/16.
 */
public interface Communicator
    {
        public boolean go(String text);
        public void setLog(String text);
        public void testStarted(boolean testStarted);
        public void stopNotiFication();
        public void setConnectionStatus(String status, boolean connectionStatus);
        public void setSwitchActivity();
        public void setBatteryLevel(int value);
        public void setManufacturerName(String manufacturerName);
        public void setSerialNumber(String serialNumber);
        public void setModelNumber(String modelNumber);
        public void getOfflineResults(ArrayList<String> arrayList);
    }
