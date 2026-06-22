/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.maniteja.com.synclib.services;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import org.maniteja.com.synclib.helper.HelperC;
import org.maniteja.com.synclib.helper.SampleGattAttributes;
import org.maniteja.com.synclib.helper.SerializeUUID;
import org.maniteja.com.synclib.helper.Util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.UUID;


/**
 * Service for managing connection and data communication with a GATT server hosted on a
 * given Bluetooth LE device.
 */
public class BluetoothLeService extends Service
{
    private final static String TAG = BluetoothLeService.class.getSimpleName();

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private String mBluetoothDeviceAddress;
    private BluetoothGatt mBluetoothGatt;
    private int mConnectionState = STATE_DISCONNECTED;

    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;

    public final static String ACTION_GATT_CONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED =
            "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE =
            "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    /*public final static UUID UUID_HEART_RATE_MEASUREMENT =
            UUID.fromString(SampleGattAttributes.TEST_CHARACTERISTIC_UUID);
    public final static UUID UUID_SYNC_MEASUREMENT =
            UUID.fromString(SampleGattAttributes.SYNCING_CHARACTERISTIC_UUID);*/

    public final static String EXTRAS_DEVICE_BATTERY = "EXTRAS_DEVICE_BATTERY";

    SerializeUUID serializeUUID;

    public SerializeUUID getSerializeUUID()
    {
        return serializeUUID;
    }

    public BluetoothLeService setSerializeUUID(SerializeUUID serializeUUID)
    {
        this.serializeUUID = serializeUUID;
        return this;
    }
    /*//Additions
    private static final UUID Battery_Service_UUID = UUID.fromString(SampleGattAttributes.BATTERY_LEVEL_SERVICE_UUID);
    private static final UUID Battery_Level_UUID = UUID.fromString(SampleGattAttributes.BATTERY_LEVEL_CHARACTERISTIC_UUID);


    //Additions
    private static final UUID Device_Information_Service_UUID = UUID.fromString(SampleGattAttributes.DEVICE_INFO_SERVICE_UUID);
    private static final UUID System_ID_UUID = UUID.fromString(SampleGattAttributes.SYSTEM_ID);
    private static final UUID Model_Number_UUID = UUID.fromString(SampleGattAttributes.MODEL_NUMBER_CHARACTERISTIC_UUID);
    private static final UUID Serial_Number_UUID = UUID.fromString(SampleGattAttributes.SERIAL_NUMBER_CHARACTERISTIC_UUID);
    private static final UUID Firmware_Revision_UUID = UUID.fromString(SampleGattAttributes.FIRMWARE_REVISION_STRING);
    private static final UUID Software_Revision_UUID = UUID.fromString(SampleGattAttributes.HARDWARE_REVISION_STRING);
    private static final UUID Manufacturer_Name_UUID = UUID.fromString(SampleGattAttributes.MANUFACTURE_NAME_CHARACTERISTIC_UUID);
    private static final UUID PNP_ID_UUID = UUID.fromString(SampleGattAttributes.PNP_ID);
    private static final UUID IEEE_UUID = UUID.fromString(SampleGattAttributes.IEEE);*/

    public final static String EXTRAS_DEVICE_INFORMATION_MFG_NAME = "EXTRAS_DEVICE_INFORMATION_MFG_NAME";
    public final static String EXTRAS_DEVICE_INFORMATION_SERIAL_NUMBER = "EXTRAS_DEVICE_INFORMATION_SERIAL_NUMBER";
    public final static String EXTRAS_DEVICE_INFORMATION_MODEL_NUMBER = "EXTRAS_DEVICE_INFORMATION_MODEL_NUMBER";

    String variable = "TEST";

    // Implements callback methods for GATT events that the app cares about.  For example,
    // connection change and services discovered.
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback()
    {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState)
        {
            String intentAction;
            if (newState == BluetoothProfile.STATE_CONNECTED)
            {
                intentAction = ACTION_GATT_CONNECTED;
                mConnectionState = STATE_CONNECTED;
                broadcastUpdate(intentAction);
                Log.i(TAG, "Connected to GATT server.");
                // Attempts to discover services after successful connection.
                Log.i(TAG, "Attempting to start service discovery:" +
                        mBluetoothGatt.discoverServices());

            } else if (newState == BluetoothProfile.STATE_DISCONNECTED)
            {
                intentAction = ACTION_GATT_DISCONNECTED;
                mConnectionState = STATE_DISCONNECTED;
                Log.i(TAG, "Disconnected from GATT server.");
                broadcastUpdate(intentAction);
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status)
        {
            if (status == BluetoothGatt.GATT_SUCCESS)
            {
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
            } else
            {
                Log.w(TAG, "onServicesDiscovered received: " + status);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic,
                                         int status)
        {
            if (status == BluetoothGatt.GATT_SUCCESS)
            {
                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            }
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status)
        {
            if (status == BluetoothGatt.GATT_SUCCESS)
            {
                //System.out.println("Gatt Log : Success "+ characteristic.getUuid());
//                    broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
                if (characteristic.getUuid().toString().equals(serializeUUID.getWrite_first_charac()))
                {
                    //System.out.println("Gatt Log : Success Inside"+ characteristic.getUuid());
                    broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
                }
            } else if (status == BluetoothGatt.GATT_FAILURE)
            {
                //System.out.println("Gatt Log : Failure "+ characteristic.getUuid());
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic)
        {
            broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
        }
    };

    private void broadcastUpdate(final String action)
    {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
//        createFile();
        //Toast.makeText(getBaseContext(),"Create",Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {

        // Toast.makeText(getBaseContext(),"Start",Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        // Toast.makeText(getBaseContext(),"Destroyed",Toast.LENGTH_SHORT).show();
    }

    private void broadcastUpdate(final String action, final BluetoothGattCharacteristic characteristic)
    {
        final Intent intent = new Intent(action);
        final byte[] data = characteristic.getValue();
        System.out.println("OFFLINE VALUE "+data.toString()+" :: "+characteristic.getValue() + " :: "+characteristic.getUuid());
        if (UUID.fromString(serializeUUID.getBattery_level_charac()).equals(characteristic.getUuid()))
        {
            Log.v(TAG, "1characteristic.getStringValue(0) = " + characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 0));
            intent.putExtra(EXTRAS_DEVICE_BATTERY, characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 0));
            Log.v(TAG, "2characteristic.getStringValue(0) = " + characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 0));
        } else if (UUID.fromString(serializeUUID.getManufac_name_charac()).equals(characteristic.getUuid()))
        {
            intent.putExtra(EXTRAS_DEVICE_INFORMATION_MFG_NAME, characteristic.getStringValue(0));
        } else if (UUID.fromString(serializeUUID.getSerial_number_charac()).equals(characteristic.getUuid()))
        {
            String cleanSerial = characteristic.getStringValue(0).replaceAll("\\P{Print}", "");
            cleanSerial = cleanSerial.trim();
            intent.putExtra(EXTRAS_DEVICE_INFORMATION_SERIAL_NUMBER, cleanSerial);
            //System.out.println("SERIAL NUMBER "+cleanSerial);
        } else if (UUID.fromString(serializeUUID.getModel_number_charac()).equals(characteristic.getUuid()))
        {
            intent.putExtra(EXTRAS_DEVICE_INFORMATION_MODEL_NUMBER, characteristic.getStringValue(0));
        } else if (characteristic.getUuid().equals(UUID.fromString(serializeUUID.getWrite_first_charac())))
        {
            //System.out.println("Gatt Log : Success Inside "+ characteristic.getUuid());
            intent.putExtra(HelperC.key_notifyDataFlag, 1);
        } else if (!UUID.fromString(serializeUUID.getTest_charac()).equals(characteristic.getUuid()))
        {
            int glucose = 0;
            long time = 0;
            System.out.println("OFFLINE OUTSIDE ACTUAL DATA "+data);
            if (data.length == 10)
            {
                byte[] gl = new byte[2];
                gl[0] = data[0];
                gl[1] = data[1];
                StringBuffer sb = new StringBuffer();
                for (byte byteChar : gl)
                {
                    sb.append(String.format("%02x", byteChar));
                }
                String raw = sb.toString().replaceAll("\\s+", "");
                glucose = Integer.parseInt(raw, 16);

                System.out.println("OFFLINE ACTUAL DATA "+glucose);

                time = 0;
                for (int i = 2; i < data.length; i++)
                {
                    time = (time << 8) + (data[i] & 0xff);
                }
                time = time * 1000;
            }
            if (new Util(getApplicationContext()).readInt(HelperC.key_HP, HelperC.key_def_hip) == 0)
            {
                time = System.currentTimeMillis();
            }
            intent.putExtra(HelperC.key_cFlag, HelperC.syncFlag);
            intent.putExtra(HelperC.KEY_RESULTS, "" + glucose);
            intent.putExtra(HelperC.KEY_TIMESTAMP, time);
            //}
            //intent.putExtra(EXTRA_DATA, String.valueOf(glucose));
        } else
        {
            //System.out.println("ss");
            if (data != null && data.length > 0)
            {
                StringBuffer sb = new StringBuffer();
                for (byte byteChar : data)
                {
                    sb.append(String.format("%02x", byteChar));
                }
                String raw = sb.toString().replaceAll("\\s+", "");
                int temp1 = Integer.parseInt(raw, 16);
                intent.putExtra(HelperC.key_cFlag, 0);
                intent.putExtra(HelperC.KEY_RESULTS, "" + temp1);
            }
        }
        sendBroadcast(intent);
    }

    public void getbattery() throws Exception
    {

        if (mBluetoothGatt == null)
        {
            Log.e(TAG, "lost connection");
        }

        BluetoothGattService batteryService = mBluetoothGatt.getService(UUID.fromString(serializeUUID.getBattery_level_service()));
        if (batteryService == null)
        {
            Log.d(TAG, "Battery service not found!");
            return;
        }

        BluetoothGattCharacteristic batteryLevel = batteryService.getCharacteristic(UUID.fromString(serializeUUID.getBattery_level_charac()));
        if (batteryLevel == null)
        {
            Log.d(TAG, "Battery level not found!");
            return;
        }

        mBluetoothGatt.readCharacteristic(batteryLevel);
    }

    public void getDeviceInformation() throws Exception
    {

        if (mBluetoothGatt == null)
        {
            Log.e(TAG, "lost connection");
        }

        BluetoothGattService deviceInformationService = mBluetoothGatt.getService(UUID.fromString(serializeUUID.getDevice_info_service()));
        if (deviceInformationService == null)
        {
            Log.d(TAG, "Battery service not found!");
            return;
        }

        BluetoothGattCharacteristic manuNameLevel = deviceInformationService.getCharacteristic(UUID.fromString(serializeUUID.getManufac_name_charac()));
        if (manuNameLevel == null)
        {
            Log.d(TAG, "Battery level not found!");
            return;
        }

        mBluetoothGatt.readCharacteristic(manuNameLevel);
    }

    public void getDeviceInfroamtionSerial() throws Exception
    {

        if (mBluetoothGatt == null)
        {
            Log.e(TAG, "lost connection");
        }

        BluetoothGattService deviceInformationService = mBluetoothGatt.getService(UUID.fromString(serializeUUID.getDevice_info_service()));
        if (deviceInformationService == null)
        {
            Log.d(TAG, "Battery service not found!");
            return;
        }
        BluetoothGattCharacteristic serialNumberLevel = deviceInformationService.getCharacteristic(UUID.fromString(serializeUUID.getSerial_number_charac()));
        if (serialNumberLevel == null)
        {
            Log.d(TAG, "Battery level not found!");
            return;
        }

        mBluetoothGatt.readCharacteristic(serialNumberLevel);
    }

    public void getDeviceInformationMfgDate() throws Exception
    {
        if (mBluetoothGatt == null)
        {
            Log.e(TAG, "lost connection");
        }

        BluetoothGattService deviceInformationService = mBluetoothGatt.getService(UUID.fromString(serializeUUID.getDevice_info_service()));
        if (deviceInformationService == null)
        {
            Log.d(TAG, "Battery service not found!");
            return;
        }

        BluetoothGattCharacteristic modelNumberLevel = deviceInformationService.getCharacteristic(UUID.fromString(serializeUUID.getModel_number_charac()));
        if (modelNumberLevel == null)
        {
            Log.d(TAG, "Battery level not found!");
            return;
        }

        mBluetoothGatt.readCharacteristic(modelNumberLevel);
    }

    public class LocalBinder extends Binder
    {
        public BluetoothLeService getService()
        {
            return BluetoothLeService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent)
    {
        // After using a given device, you should make sure that BluetoothGatt.close() is called
        // such that resources are cleaned up properly.  In this particular example, close() is
        // invoked when the UI is disconnected from the Service.
        close();
        return super.onUnbind(intent);
    }

    private final IBinder mBinder = new LocalBinder();

    /**
     * Initializes a reference to the local Bluetooth adapter.
     *
     * @return Return true if the initialization is successful.
     */
    public boolean initialize()
    {
        // For API level 18 and above, get a reference to BluetoothAdapter through
        // BluetoothManager.
        if (mBluetoothManager == null)
        {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null)
            {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }

        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null)
        {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }

        return true;
    }

    /**
     * Connects to the GATT server hosted on the Bluetooth LE device.
     *
     * @param address The device address of the destination device.
     * @return Return true if the connection is initiated successfully. The connection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public boolean connect(final String address) throws Exception
    {
        if (mBluetoothAdapter == null || address == null)
        {
            Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }

        // Previously connected device.  Try to reconnect.
        if (mBluetoothDeviceAddress != null && address.equals(mBluetoothDeviceAddress)
                && mBluetoothGatt != null)
        {
            Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");
            if (mBluetoothGatt.connect())
            {
                mConnectionState = STATE_CONNECTING;
                return true;
            } else
            {
                return false;
            }
        }

        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (device == null)
        {
            Log.w(TAG, "Device not found.  Unable to connect.");
            return false;
        }
        // We want to directly connect to the device, so we are setting the autoConnect
        // parameter to false.
        mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
        Log.d(TAG, "Trying to create a new connection.");
        mBluetoothDeviceAddress = address;
        mConnectionState = STATE_CONNECTING;
        return true;
    }

    /**
     * Disconnects an existing connection or cancel a pending connection. The disconnection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public void disconnect()
    {
        if (mBluetoothAdapter == null || mBluetoothGatt == null)
        {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.disconnect();
    }

    /**
     * After using a given BLE device, the app must call this method to ensure resources are
     * released properly.
     */
    public void close()
    {
        if (mBluetoothGatt == null)
        {
            return;
        }
        mBluetoothGatt.close();
        mBluetoothGatt = null;
    }

    /**
     * Request a read on a given {@code BluetoothGattCharacteristic}. The read result is reported
     * asynchronously through the {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
     * callback.
     *
     * @param characteristic The characteristic to read from.
     */
    public void readCharacteristic(BluetoothGattCharacteristic characteristic) throws Exception
    {
        if (mBluetoothAdapter == null || mBluetoothGatt == null)
        {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.readCharacteristic(characteristic);
    }

        /*public void writeCustomCharacteristic(String data) {
            if (mBluetoothAdapter == null || mBluetoothGatt == null) {
                Log.w(TAG, "BluetoothAdapter not initialized");
                return;
            }
        *//*check if the service is available on the device*//*
            BluetoothGattService mCustomService = mBluetoothGatt.getService(UUID.fromString("0003abcd-0000-1000-8000-00805f9b0131"));
            if(mCustomService == null){
                Log.w(TAG, "Custom BLE Service not found");
                return;
            }
        *//*get the read characteristic from the service*//*
            BluetoothGattCharacteristic mWriteCharacteristic = mCustomService.getCharacteristic(UUID.fromString("0003abcd-0000-1000-8000-00805f9b0131"));
            mWriteCharacteristic.setValue(data);
            if(mBluetoothGatt.writeCharacteristic(mWriteCharacteristic) == false){
                Log.w(TAG, "Failed to write characteristic");
            }
        }*/

    /**
     * Enables or disables notification on a give characteristic.
     *
     * @param characteristic Characteristic to act on.
     * @param enabled        If true, enable notification.  False otherwise.
     */
    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic,
                                              boolean enabled) throws Exception
    {
        if (mBluetoothAdapter == null || mBluetoothGatt == null)
        {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);

        // This is specific to Heart Rate Measurement.
        if (UUID.fromString(serializeUUID.getTest_charac()).equals(characteristic.getUuid()))
        {
            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
                    UUID.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            mBluetoothGatt.writeDescriptor(descriptor);
        }
    }

    public void setCharacteristicNotificationBattery(BluetoothGattCharacteristic characteristic,
                                                     boolean enabled) throws Exception
    {
        if (mBluetoothAdapter == null || mBluetoothGatt == null)
        {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.readCharacteristic(characteristic);
        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);

            /*// This is specific to Heart Rate Measurement.
            if (Battery_Service_UUID.equals(characteristic.getUuid())) {
                BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
                        UUID.fromString(SampleGattAttributes.Battery_Level_UUID));
                descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                mBluetoothGatt.writeDescriptor(descriptor);
            }*/
    }

    public void setCharacteristicNotificationStop(BluetoothGattCharacteristic characteristic,
                                                  boolean enabled) throws Exception
    {
        if (mBluetoothAdapter == null || mBluetoothGatt == null)
        {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);

        // This is specific to Heart Rate Measurement.
        if (UUID.fromString(serializeUUID.getTest_charac()).equals(characteristic.getUuid()))
        {
            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
                    UUID.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
            descriptor.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
            mBluetoothGatt.writeDescriptor(descriptor);
        }
    }

    public void setCharacteristicNotificationSync(BluetoothGattCharacteristic characteristic,
                                                  boolean enabled) throws Exception
    {
        if (mBluetoothAdapter == null || mBluetoothGatt == null)
        {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);

        // This is specific to Heart Rate Measurement.
        if (UUID.fromString(serializeUUID.getSync_charac()).equals(characteristic.getUuid()))
        {
            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
                    UUID.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            mBluetoothGatt.writeDescriptor(descriptor);
        }
    }

    public void setCharacteristicNotificationStopSync(BluetoothGattCharacteristic characteristic,
                                                      boolean enabled) throws Exception
    {
        if (mBluetoothAdapter == null || mBluetoothGatt == null)
        {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);

        // This is specific to Heart Rate Measurement.
        if (UUID.fromString(serializeUUID.getSync_charac()).equals(characteristic.getUuid()))
        {
            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
                    UUID.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
            descriptor.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
            mBluetoothGatt.writeDescriptor(descriptor);
        }
    }

    /**
     * Retrieves a list of supported GATT services on the connected device. This should be
     * invoked only after {@code BluetoothGatt#discoverServices()} completes successfully.
     *
     * @return A {@code List} of supported services.
     */
    public void writeCharacteristic(BluetoothGattCharacteristic characteristic) throws Exception
    {
        if (mBluetoothAdapter == null || mBluetoothGatt == null)
        {

            return;
        }
        mBluetoothGatt.writeCharacteristic(characteristic);
    }

    public List<BluetoothGattService> getSupportedGattServices() throws Exception
    {
        if (mBluetoothGatt == null)
        {
            return null;
        }

        return mBluetoothGatt.getServices();
    }

    public BluetoothGattCharacteristic getSpecificCharacteristic(UUID serviceUuid, UUID characteristicUuid)
            throws Exception
    {
        BluetoothGattCharacteristic characteristic = mBluetoothGatt.getService(serviceUuid).getCharacteristic(characteristicUuid);
        return characteristic;
    }

    public void writeData(final BluetoothGattCharacteristic mWriteCharacteristic, BluetoothGattCharacteristic mWriteCharacteristic2, final Util util)
    {
        try
        {
            try
            {
                writeData2(mWriteCharacteristic2, util);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
            Handler handler = new Handler();
            handler.postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    int charaProp = mWriteCharacteristic.getProperties();
                    if (((mWriteCharacteristic.getProperties() & BluetoothGattCharacteristic.PROPERTY_WRITE) |
                            (charaProp & BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE)) > 0)
                    {

                        long x = (System.currentTimeMillis() / 1000);
                        int m1 = util.readInt(HelperC.key_M1, HelperC.key_def_m1);
                        int m2 = util.readInt(HelperC.key_M2, HelperC.key_def_m2);
                        int m3 = util.readInt(HelperC.key_M3, HelperC.key_def_m3);
                        int m4 = util.readInt(HelperC.key_M4, HelperC.key_def_m4);
                        int hp = util.readInt(HelperC.key_HP, HelperC.key_def_hip);
                        int eq = util.readInt(HelperC.key_Eq, HelperC.key_def_eq);
                        util.print("Time : " + hp);
                        //Toast.makeText(getApplicationContext(), ""+hp, Toast.LENGTH_LONG).show();
                        mWriteCharacteristic.setValue(new byte[]{
                                (byte)
                                        (x & 0xFF), (byte) ((x >> 8) & 0xFF), (byte) ((x >> 16) & 0xFF), (byte) ((x >> 24) & 0xFF), (byte) ((x >> 32) & 0xFF)
                                , (byte) ((x >> 40) & 0xFF), (byte) ((x >> 48) & 0xFF), (byte) ((x >> 56) & 0xFF),
                                (byte) (m1 & 0xFF), (byte) ((m1 >> 8) & 0xFF), (byte) (m2 & 0xFF), (byte) ((m2 >> 8) & 0xFF),
                                (byte) (m3 & 0xFF), (byte) ((m3 >> 8) & 0xFF), (byte) (m4 & 0xFF), (byte) ((m4 >> 8) & 0xFF), (byte) hp, (byte) eq
                        });
                        writeCharacteristic1(mWriteCharacteristic);
                    }
                }
            }, 500);
        } catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Please check whether the devices is connected", Toast.LENGTH_LONG).show();
        }
    }

    public void writeData2(BluetoothGattCharacteristic mWriteCharacteristic2, Util util)
    {
        try
        {
            if (mWriteCharacteristic2 != null)
            {
                int charaProp2 = mWriteCharacteristic2.getProperties();
                if (((mWriteCharacteristic2.getProperties() & BluetoothGattCharacteristic.PROPERTY_WRITE) |
                        (charaProp2 & BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE)) > 0)
                {
                    int m5 = util.readInt(HelperC.key_M5, HelperC.key_def_m5);
                    int m6 = util.readInt(HelperC.key_M6, HelperC.key_def_m6);
                    int mf = util.readInt(HelperC.key_MuFact, HelperC.key_def_mf);

                    mWriteCharacteristic2.setValue(new byte[]{(byte) (m5 & 0xFF), (byte) ((m5 >> 8) & 0xFF), (byte) (m6 & 0xFF), (byte) ((m6 >> 8) & 0xFF), (byte) (mf & 0xFF), (byte) ((mf >> 8) & 0xFF)});
                    writeCharacteristic1(mWriteCharacteristic2);
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Please check whether the devices is connected", Toast.LENGTH_LONG).show();
        }
    }

    private void writeCharacteristic1(BluetoothGattCharacteristic characteristic)
    {
        try
        {
            writeCharacteristic(characteristic);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void add()
    {

    }

}
