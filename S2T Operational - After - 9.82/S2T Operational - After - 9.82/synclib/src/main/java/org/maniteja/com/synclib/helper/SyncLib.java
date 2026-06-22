package org.maniteja.com.synclib.helper;

import android.app.Activity;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import org.maniteja.com.synclib.services.BluetoothLeService;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

/**
 * Created by maniteja on 2/8/18.
 */

public class SyncLib
{
    BluetoothLeService bluetoothLeService;

    Util util;
    private String mDeviceAddress;

    public static boolean devTestGoingOn;
    public static boolean mConnected = false;
    boolean mBound = false;
    int x = 0;
    private BluetoothGattCharacteristic mWSyncCharacteristic;
    private BluetoothGattCharacteristic mNotifyCharacteristic;
    private BluetoothGattCharacteristic mWriteCharacteristic;
    private BluetoothGattCharacteristic mWriteCharacteristic2;

    int progressStatus = 0;
    int temp;
    private Handler handler = new Handler();
    public static boolean conFlag = false;

    private final static String TAG = SyncLib.class.getSimpleName();

    Context context;
    Activity activity;
    SerializeUUID serializeUUID;
    Communicator communicator;

    ArrayList<String> arrayList;

    public String getmDeviceAddress()
    {
        return mDeviceAddress;
    }

    public SyncLib setmDeviceAddress(String mDeviceAddress)
    {
        this.mDeviceAddress = mDeviceAddress;
        return this;
    }

    public SyncLib(Communicator communicator, Context context, Activity activity, SerializeUUID serializeUUID, String mDeviceAddress)
    {
        this.communicator = communicator;
        this.context = context;
        this.activity = activity;
        this.serializeUUID = serializeUUID;
        this.mDeviceAddress = mDeviceAddress;

        util = new Util(context,activity);
        arrayList = new ArrayList<String>();

        Intent gattServiceIntent = new Intent(context, BluetoothLeService.class);
        context.bindService(gattServiceIntent, mServiceConnection, context.BIND_AUTO_CREATE);
    }

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action))
            {
                mConnected = true;
                communicator.setConnectionStatus("Connected",mConnected);
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action))
            {
                mConnected = false;
                communicator.setConnectionStatus("Disconnected",mConnected);
                try
                {
                    clearUI();
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action))
            {

            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action))
            {
                int value = intent.getIntExtra(BluetoothLeService.EXTRAS_DEVICE_BATTERY, -1);
                System.out.println("PRINTING " + value);
                int notifyDataFlag = intent.getIntExtra(HelperC.key_notifyDataFlag, 0);
                if (notifyDataFlag == 1)
                {
                    notifyGetData();
                }
                communicator.setBatteryLevel(value);
                String mfg_name = intent.getStringExtra(BluetoothLeService.EXTRAS_DEVICE_INFORMATION_MFG_NAME);
                String serial_number = intent.getStringExtra(BluetoothLeService.EXTRAS_DEVICE_INFORMATION_SERIAL_NUMBER);
                String model_number = intent.getStringExtra(BluetoothLeService.EXTRAS_DEVICE_INFORMATION_MODEL_NUMBER);
                if (mfg_name != null)
                {
                    util.putString(HelperC.key_devmfgname, mfg_name);
                    communicator.setManufacturerName(mfg_name);
                }
                if (serial_number != null)
                {
                    util.putString(HelperC.key_devsrno, serial_number);
                    communicator.setSerialNumber(serial_number);
                }
                if (model_number != null)
                {
                    util.putString(HelperC.key_devmfgdate, model_number);
                    communicator.setModelNumber(model_number);
                }
                int cf = intent.getIntExtra(HelperC.key_cFlag, 0);
                String result = intent.getStringExtra(HelperC.KEY_RESULTS);
                long time = intent.getLongExtra(HelperC.KEY_TIMESTAMP, 0);
                displayData(result, time, cf);
            }
        }
    };

    private void clearUI()
    {

    }

    public void startTest()
    {
        try
        {
            communicator.testStarted(true);
            communicator.setLog("Insert Strip!");
            final BluetoothGattCharacteristic characteristic = bluetoothLeService.getSpecificCharacteristic(UUID.fromString(serializeUUID.getTest_service()), UUID.fromString(serializeUUID.getTest_charac()));
            System.out.println("UUID :: " + characteristic.getUuid());
            final int charaProp = characteristic.getProperties();
            if ((charaProp | BluetoothGattCharacteristic.PROPERTY_READ) > 0)
            {
                System.out.println("UUID :: 1 " + characteristic.getUuid());
                // If there is an active notification on a characteristic, clear
                // it first so it doesn't update the data field on the user interface.
                if (mNotifyCharacteristic != null)
                {
                    bluetoothLeService.setCharacteristicNotification(
                            mNotifyCharacteristic, false);
                    mNotifyCharacteristic = null;
                }
                bluetoothLeService.readCharacteristic(characteristic);
                // mBluetoothLeService.wr
            }
            if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0)
            {
                System.out.println("UUID :: 2 " + characteristic.getUuid());
                mNotifyCharacteristic = characteristic;
                bluetoothLeService.setCharacteristicNotification(
                        characteristic, true);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void stopTest()
    {
        BluetoothGattCharacteristic characteristic = null;
        try
        {
            communicator.testStarted(false);
            characteristic = bluetoothLeService.getSpecificCharacteristic(UUID.fromString(serializeUUID.getTest_service()), UUID.fromString(serializeUUID.getTest_charac()));
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        try
        {
            bluetoothLeService.setCharacteristicNotificationStop(characteristic, true);
        } catch (Exception e)
        {
            e.printStackTrace();
            mConnected = false;
            clearUI();
        }
    }

    public  void writeCalibData(String result)
    {
        String str = result;
        if(str.startsWith(HelperC.qrcode_start_text))
        {
            util.splitWriteData(str);
            try
            {
                mWriteCharacteristic = bluetoothLeService.getSpecificCharacteristic(UUID.fromString(serializeUUID.getWrite_first_service()), UUID.fromString(serializeUUID.getWrite_first_charac()));
                mWriteCharacteristic2 = bluetoothLeService.getSpecificCharacteristic(UUID.fromString(serializeUUID.getWrite_second_service()), UUID.fromString(serializeUUID.getWrite_second_charac()));
                bluetoothLeService.writeData(mWriteCharacteristic, mWriteCharacteristic2, util);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public void connectOrDisconnect()
    {
        if (mConnected)
        {
            if (!devTestGoingOn)
            {
                bluetoothLeService.disconnect();
                mConnected = false;
            } else
            {
                Toast.makeText(context, "Test under progress. Please wait", Toast.LENGTH_SHORT).show();
            }
        } else
        {
            try
            {
                if (mDeviceAddress.length() == 0)
                {
                    context.unregisterReceiver(mGattUpdateReceiver);
                    Toast.makeText(context, "No device found.Please scan the device first", Toast.LENGTH_LONG).show();
                    communicator.setSwitchActivity();
                }
                if (util.readboolean(HelperC.key_autoconnectflag, false))
                {
                    boolean test = bluetoothLeService.connect(mDeviceAddress);
                    util.print("Control 11 " + test + " :: " + mDeviceAddress.length() + " :: " + mDeviceAddress);
                } else
                {
                    context.unregisterReceiver(mGattUpdateReceiver);
                    Toast.makeText(context, "No device found.Please scan the device first", Toast.LENGTH_LONG).show();
                    communicator.setSwitchActivity();
                }
                if (!util.readboolean(HelperC.key_autoconnectflag, false))
                {
                    context.unregisterReceiver(mGattUpdateReceiver);
                    Toast.makeText(context, "No device found.Please scan the device first", Toast.LENGTH_LONG).show();
                    communicator.setSwitchActivity();
                }

            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public void notifyGetData()
    {
        try
        {
            BluetoothGattCharacteristic gattCharacteristic = bluetoothLeService.getSpecificCharacteristic(UUID.fromString(serializeUUID.getSync_service()), UUID.fromString(serializeUUID.getSync_charac()));
            notifyTogetData(gattCharacteristic, true);
        } catch (Exception e)
        {
            e.printStackTrace();
            mConnected = false;
            clearUI();
        }
    }

    public void startReceiver()
    {
        context.registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());

        if (bluetoothLeService != null)
        {
            boolean result = false;
            try
            {
                if (util.readboolean(HelperC.key_autoconnectflag, false))
                {
                    boolean test = bluetoothLeService.connect(mDeviceAddress);
                    util.print("Control 11 " + test + " :: " + mDeviceAddress.length() + " :: " + mDeviceAddress);
                } else
                {
                    if (mConnected)
                    {
                        result = bluetoothLeService.connect(mDeviceAddress);
                    } else
                    {

                    }
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }
            Log.d(TAG, "Connect request result=" + result);
        } else
        {
            Log.d(TAG, "Nullllllllll");
        }
    }

    public void stopReceiver()
    {

        if (bluetoothLeService != null)
        {
            try
            {
                context.unregisterReceiver(mGattUpdateReceiver);
                context.unbindService(mServiceConnection);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
            bluetoothLeService = null;
        }
    }

    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection()
    {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service)
        {
            bluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!bluetoothLeService.initialize())
            {
                Log.e(TAG, "Unable to initialize Bluetooth");
                return;
            }
            // Automatically connects to the device upon successful start-up initialization.
            try
            {
                boolean test = bluetoothLeService.connect(mDeviceAddress);
                bluetoothLeService.setSerializeUUID(serializeUUID);
                util.print("Control 10 :" + test);
                mBound = true;
                final Timer timer = new Timer("batteryTimer");
                TimerTask task = new TimerTask()
                {
                    @Override
                    public void run()
                    {
                        try
                        {
                            if (bluetoothLeService != null)
                                bluetoothLeService.getbattery();
//                            mBluetoothLeService.getDeviceInformation();
                        } catch (Exception e)
                        {
                            e.printStackTrace();
                            timer.cancel();
                        }
                    }
                };
                timer.scheduleAtFixedRate(task, 0, 5000);

                final Timer timer1 = new Timer("batteryTimer");
                TimerTask task1 = new TimerTask()
                {
                    @Override
                    public void run()
                    {
                        try
                        {
//                            mBluetoothLeService.getbattery();
                            if (bluetoothLeService != null)
                                bluetoothLeService.getDeviceInformation();
                        } catch (Exception e)
                        {
                            e.printStackTrace();
                            timer1.cancel();
                        }
                    }
                };
                timer1.scheduleAtFixedRate(task1, 0, 2000);

                final Timer timer2 = new Timer("batteryTimer");
                TimerTask task2 = new TimerTask()
                {
                    @Override
                    public void run()
                    {
                        try
                        {
//                            mBluetoothLeService.getbattery();
                            if (bluetoothLeService != null)
                                bluetoothLeService.getDeviceInfroamtionSerial();
                        } catch (Exception e)
                        {
                            e.printStackTrace();
                            timer2.cancel();
                        }
                    }
                };
                timer2.scheduleAtFixedRate(task2, 0, 3000);

                final Timer timer3 = new Timer("batteryTimer");
                TimerTask task3 = new TimerTask()
                {
                    @Override
                    public void run()
                    {
                        try
                        {
//                            mBluetoothLeService.getbattery();
                            if (bluetoothLeService != null)
                                bluetoothLeService.getDeviceInformationMfgDate();
                        } catch (Exception e)
                        {
                            e.printStackTrace();
                            timer3.cancel();
                        }
                    }
                };
                timer3.scheduleAtFixedRate(task3, 0, 7000);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName)
        {
            bluetoothLeService = null;
        }
    };

    private void displayData(String data, long time, int cF)
    {
        if (data != null)
        {
            devTestGoingOn = false;
            if (data.equals(HelperC.add_blood_flag))
            {
                //Add Blood
                communicator.setLog("Add Blood");
            } else if (data.equals(HelperC.test_progress_flag))
            {
                //Progress
                progressStatus = 6;
                new Thread(new Runnable()
                {
                    public void run()
                    {
                        while (progressStatus > 0)
                        {
                            progressStatus -= 1;
                            // Update the progress bar and display the
                            //current value in the text view
                            handler.post(new Runnable()
                            {
                                public void run()
                                {
                                    if (progressStatus > 0)
                                    {
                                        activity.runOnUiThread(new Runnable()
                                        {
                                            @Override
                                            public void run()
                                            {
                                                communicator.setLog("" + progressStatus);
                                            }
                                        });
                                    }
                                    if (progressStatus == 0)
                                    {
                                        temp = 0;
                                        if (!conFlag)
                                        {
                                            devTestGoingOn = false;
                                        }
                                    }
                                }
                            });
                            try
                            {
                                Thread.sleep(1000);
                            } catch (InterruptedException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            } else if (data.equals(HelperC.used_strip_error_flag1))
            {
                communicator.setLog("Used Strip" + "\n" + "Please remove the used strip and insert a new strip");
                communicator.testStarted(false);
            } else if (data.equals(HelperC.used_strip_error_flag2))
            {
                communicator.setLog("Used Strip" + "\n" + "Please remove the used strip and insert a new strip");
                communicator.testStarted(false);
            } else if (data.equals(HelperC.data_sync_done_flag))
            {
                //Notify For Stoping the Data
                BluetoothGattCharacteristic gattCharacteristic = null;
                try
                {
                    gattCharacteristic = bluetoothLeService.getSpecificCharacteristic(UUID.fromString(serializeUUID.getSync_service()), UUID.fromString(serializeUUID.getSync_charac()));
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
                notifyTogetDataStop(gattCharacteristic, false);//mGattCharacteristics.get(0).get(2)

                if(arrayList != null)
                {
                    if(arrayList.size() > 0)
                    {
                        communicator.getOfflineResults(arrayList);
                        arrayList = new ArrayList<String>();
                    }
                }

                //Adding data to database
            } else if (data.equals(HelperC.incomplete_test_flag1))
            {
                communicator.setLog("Incomplete Test" + "\n" + "Strip Error/Fill Error");
                communicator.testStarted(false);
            } else if (data.equals(HelperC.incomplete_test_flag_temp))
            {
                communicator.setLog("Incomplete Test" + "\n" + "Temp Error");
                communicator.testStarted(false);
            } else if (data.equals(HelperC.incomplete_test_flag2))
            {
                communicator.setLog("Incomplete Test" + "\n" + "Strip Error/Fill Error");
                communicator.testStarted(false);
            } else if (data.equals(HelperC.incomplete_test_flag3))
            {
                communicator.setLog("Incomplete Test" + "\n" + "Strip Error/Fill Error");
                communicator.testStarted(false);
            } else
            {
                try
                {

                    x++;
                    if (cF == HelperC.syncFlag)
                    {
                        util.putString(HelperC.key_last, data);
                        util.print("OFFLINE READING "+data);
                        String[] datetinme = new Util().longToDateTime(time);
                        arrayList.add(data+"_"+datetinme[0]+"_"+datetinme[1]);
                        //fetch previous data from sync
                    } else
                    {
                        // Result Display
                        communicator.setLog("Result is " + data);
                    }

                } catch (Exception e1)
                {
                    e1.printStackTrace();
                    context.unregisterReceiver(mGattUpdateReceiver);

                }
            }
        }
    }

    public void notifyTogetDataStop(BluetoothGattCharacteristic characteristic, boolean flag)
    {
        final int charaProp = characteristic.getProperties();
        if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0)
        {
            mWSyncCharacteristic = characteristic;
            try
            {
                bluetoothLeService.setCharacteristicNotificationStopSync(
                        characteristic, flag);
            } catch (Exception e)
            {
                e.printStackTrace();
                mConnected = false;
                clearUI();
            }
            util.print("Notify Inside 2");
        }
    }

    private static IntentFilter makeGattUpdateIntentFilter()
    {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    public void notifyTogetData(BluetoothGattCharacteristic characteristic, boolean flag)
    {
        final int charaProp = characteristic.getProperties();
        if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0)
        {
            mWSyncCharacteristic = characteristic;
            try
            {
                bluetoothLeService.setCharacteristicNotificationSync(
                        characteristic, flag);
            } catch (Exception e)
            {
                e.printStackTrace();
                mConnected = false;
                clearUI();
            }
            util.print("Notify Inside 2");
        }
    }
}
