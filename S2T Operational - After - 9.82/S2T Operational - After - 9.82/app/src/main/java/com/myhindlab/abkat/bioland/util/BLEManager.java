package com.myhindlab.abkat.bioland.util;

//import static com.inuker.bluetooth.library.Constants.REQUEST_FAILED;
//import static com.inuker.bluetooth.library.Constants.REQUEST_NOTIFY;
//import static com.inuker.bluetooth.library.Constants.REQUEST_READ;
//import static com.inuker.bluetooth.library.Constants.REQUEST_SUCCESS;
//import static com.inuker.bluetooth.library.Constants.REQUEST_WRITE;
//import static com.inuker.bluetooth.library.Constants.STATUS_CONNECTED;
//import static com.inuker.bluetooth.library.Constants.STATUS_DISCONNECTED;

import android.content.Context;


//import com.inuker.bluetooth.library.BluetoothClient;
//import com.inuker.bluetooth.library.Constants;
//import com.inuker.bluetooth.library.connect.listener.BleConnectStatusListener;
//import com.inuker.bluetooth.library.connect.listener.BluetoothStateListener;
//import com.inuker.bluetooth.library.connect.options.BleConnectOptions;
//import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
//import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
//import com.inuker.bluetooth.library.connect.response.BleUnnotifyResponse;
//import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
//import com.inuker.bluetooth.library.model.BleGattProfile;
//import com.inuker.bluetooth.library.search.SearchRequest;
//import com.inuker.bluetooth.library.search.SearchResult;
//import com.inuker.bluetooth.library.search.response.SearchResponse;
import com.myhindlab.abkat.bioland.Interface.IBleConnectListener;
import com.myhindlab.abkat.bioland.Interface.IBleIndicateListener;
//import com.myhindlab.abkat.bioland.Interface.IBleScanListener;
import com.myhindlab.abkat.bioland.Interface.IBleStateListener;
import com.myhindlab.abkat.bioland.Interface.IBleWriteListener;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

//public class BLEManager {
//
//
//    /**
//     * BLEManager 单实例
//     */
//    private BLEManager() {
//
//    }
//
//    public static BLEManager getInstance() {
//        return BLEManagerHolder.sIntance;
//    }
//
//    //静态内部类
//    private static class BLEManagerHolder {
//
//        private static final BLEManager sIntance = new BLEManager();
//
//    }
//
//    /**
//     * Common Source Code Begin
//     */
//    private BluetoothClient mClient;
//
//    public void initBLEMgr(Context context) {
//        mClient = new BluetoothClient(context);
//        ;
//
//    }
//
//    public void unInitBLEMgr(String mac) {
//        if (null == mClient) {
//            return;
//        }
//
//
//        //取消订阅通知 ==> 在该方法调用之前，由外部先调用取消订阅方法。
//
//        //连接处理
//        mClient.clearRequest(mac, REQUEST_READ);
//        mClient.clearRequest(mac, REQUEST_WRITE);
//        mClient.clearRequest(mac, REQUEST_NOTIFY);
//        mClient.refreshCache(mac);
//        mClient.unregisterConnectStatusListener(mac, mConnectStatusListener);
//        mClient.unregisterBluetoothStateListener(mBluetoothStateListener);
//
//        //连接处理
//        disConnect(mac);
//
//        mIBleStateListener = null;
//        mIBleScanListener = null;
//        mIBleConnectListener = null;
//        mIBleIndicateListener = null;
//        mIBleWriteListener = null;
//
//        if (null != mDisposable) {
//            mDisposable.dispose();
//        }
//
//        //释放 BluetoothClient 实例
//        mClient = null;
//    }
//
//    public void unInitBLEMgr() {
//        if (null == mClient) {
//            return;
//        }
//
//        mClient.unregisterBluetoothStateListener(mBluetoothStateListener);
//
//        //无 Mac 地址，蓝牙操作只局限于搜索
//        mIBleStateListener = null;
//        mIBleScanListener = null;
//
//
//        //释放 BluetoothClient 实例
//        mClient = null;
//    }
//
//    /**
//     * 开启本地蓝牙相关代码 Begin
//     */
//    private IBleStateListener mIBleStateListener;
//
//    private BluetoothStateListener mBluetoothStateListener = new BluetoothStateListener() {
//
//        @Override
//        public void onBluetoothStateChanged(boolean openOrClosed) {
//            if (null != mIBleStateListener && openOrClosed) {
//                mIBleStateListener.onBleOpen();
//            }
//
//        }
//    };
//
//    public boolean isBluetoothOpened() {
//        if (null == mClient) {
//            return false;
//        }
//
//        return mClient.isBluetoothOpened();
//    }
//
//
//    public void openBluetooth(IBleStateListener listener) {
//        if (null == mClient) {
//            return;
//        }
//
//        mIBleStateListener = listener;
//
//        mClient.openBluetooth();
//        mClient.registerBluetoothStateListener(mBluetoothStateListener);
//
//    }
//
//    public void closeBluetooth() {
//        if (null == mClient) {
//            return;
//        }
//
//        mClient.closeBluetooth();
//    }
//
//    /**
//     * 搜索蓝牙外设相关代码 Begin
//     */
//
//    private IBleScanListener mIBleScanListener;
//
//    private SearchRequest mSearchRequest = new SearchRequest.Builder()
//            .searchBluetoothLeDevice(3000, 3)   // 先扫BLE设备3次，每次3s
//            .searchBluetoothClassicDevice(5000) // 再扫经典蓝牙5s,在实际工作中没用到经典蓝牙的扫描
//            .searchBluetoothLeDevice(2000)      // 再扫BLE设备2s
//            .build();
//
//    private SearchResponse mSearchResponse = new SearchResponse() {
//
//        //开始搜素
//        @Override
//        public void onSearchStarted() {
//            if (null != mIBleScanListener) {
//                mIBleScanListener.onScanStarted();
//            }
//        }
//
//        //找到设备 可通过manufacture过滤
//        @Override
//        public void onDeviceFounded(SearchResult device) {
//            if (null != mIBleScanListener) {
//                mIBleScanListener.onDeviceFounded(device);
//            }
//        }
//
//        //搜索停止
//        @Override
//        public void onSearchStopped() {
//            if (null != mIBleScanListener) {
//                mIBleScanListener.onScanStop();
//            }
//        }
//
//        //搜索取消
//        @Override
//        public void onSearchCanceled() {
//            if (null != mIBleScanListener) {
//                mIBleScanListener.onScanCanceled();
//            }
//        }
//    };
//
//    public void scan(IBleScanListener listener) {
//        if (null == mClient) {
//            return;
//        }
//
//        mIBleScanListener = listener;
//
//        mClient.search(mSearchRequest, mSearchResponse);
//    }
//
//    public void stopScan() {
//        if (null == mClient) {
//            return;
//        }
//
//        mClient.stopSearch();
//    }
//
//
//    /**
//     * 连接蓝牙外设相关代码 Begin
//     */
//
//    //连接状态
//    private boolean isConnected = false;
//    private Disposable mDisposable;
//
//    //连接状态监听
//    private IBleConnectListener mIBleConnectListener;
//
//    private BleConnectStatusListener mConnectStatusListener = new BleConnectStatusListener() {
//
//        @Override
//        public void onConnectStatusChanged(String mac, int status) {
//            if (STATUS_CONNECTED == status) {
//                isConnected = true;
//            } else if (STATUS_DISCONNECTED == status) {
//                isConnected = false;
//                if (null != mIBleConnectListener) {
//                    mIBleConnectListener.onDisConnected(mac);
//                }
//            }
//        }
//    };
//
//
//    //public function implementation
//
//    public boolean isConnected() {
//        return isConnected;
//    }
//
//    public int getConnectStatus(String mac) {
//        if (null == mClient) {
//            return Constants.STATUS_UNKNOWN;
//        }
//
//        return mClient.getConnectStatus(mac);
//    }
//
//    public void connect(final String mac, IBleConnectListener listener) {
//        if (null == mClient) {
//            return;
//        }
//
//        //
//        mIBleConnectListener = listener;
//
//        //
//        mDisposable = Flowable.timer(500, TimeUnit.MILLISECONDS)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<Long>() {
//                    @Override
//                    public void accept(Long aLong) throws Exception {
//                        final BleConnectOptions options = new BleConnectOptions.Builder()
//                                .setConnectRetry(3)   // Retry the connection 3 times if it fails
//                                .setConnectTimeout(5000)   // Connect Timeout 5s
//                                .setServiceDiscoverRetry(3)  // Retry service discovery 3 times if it fails
//                                .setServiceDiscoverTimeout(5000)  // Discovery of service timeout 5s
//                                .build();
//
//                        if (null != mConnectStatusListener) {
//                            mIBleConnectListener.onStartConnect();
//                        }
//
//                        //连接蓝牙外设
//                        mClient.connect(mac, options, new BleConnectResponse() {
//
//                            @Override
//                            public void onResponse(int code, BleGattProfile data) {
//                                if (REQUEST_SUCCESS == code) {
//                                    if (null != mIBleConnectListener) {
//                                        mIBleConnectListener.onConnectSuccess(code, data, mac);
//                                    }
//                                } else if (code == REQUEST_FAILED) {
//                                    if (null != mIBleConnectListener) {
//                                        mIBleConnectListener.onConnectFail();
//                                    }
//                                }
//                            }//onResponse end
//
//                        });//mClient.connect end
//
//                    }//accept end
//
//                });//Flowable.timer end
//        //
//        mClient.registerConnectStatusListener(mac, mConnectStatusListener);
//    }
//
//    public void disConnect(String mac) {
//        if (null == mClient) {
//            return;
//        }
//
//        mClient.disconnect(mac);
//    }
//
//    //private function implementation
//
//
//    /**
//     * 订阅通知相关代码 Begin
//     */
//
//    //
//    private IBleIndicateListener mIBleIndicateListener;
//
//
//    //public function implementation
//    public void unIndicate(String MAC, String serviceUUID, String characterUUID) {
//        if (null == mClient) {
//            return;
//        }
//
//        mClient.unnotify(MAC, UUID.fromString(serviceUUID), UUID.fromString(characterUUID), new BleUnnotifyResponse() {
//
//            @Override
//            public void onResponse(int code) {
//                //do nothing
//            }
//        });
//    }
//
//    public void indicate(String MAC, String serviceUUID, String characterUUID, IBleIndicateListener listener) {
//        if (null == mClient) {
//            return;
//        }
//        //
//        this.mIBleIndicateListener = listener;
//
//        //
//        mClient.notify(MAC, UUID.fromString(serviceUUID), UUID.fromString(characterUUID), new BleNotifyResponse() {
//
//            @Override
//            public void onNotify(UUID service, UUID character, byte[] value) {
//                if (null != mIBleIndicateListener) {
//                    mIBleIndicateListener.onCharacteristicChanged(value);
//                }
//            }
//
//            @Override
//            public void onResponse(int code) {
//                if (REQUEST_SUCCESS == code) {
//                    if (null != mIBleIndicateListener) {
//                        mIBleIndicateListener.onIndicateSuccess();
//                    }
//                } else if (REQUEST_FAILED == code) {
//                    if (null != mIBleIndicateListener) {
//                        mIBleIndicateListener.onIndicateFailure(code);
//                    }
//                }
//            }
//        });
//    }
//
//    /**
//     * 发送数据相关代码 Begin
//     */
//
//    //
//    private IBleWriteListener mIBleWriteListener;
//
//
//    //public function implementation
//    public void write(String MAC, String uuid_service, String uuid_write, final byte[] data, IBleWriteListener listener) {
//        if (null == mClient) {
//            return;
//        }
//        //
//        this.mIBleWriteListener = listener;
//
//        //
//        mClient.write(MAC, UUID.fromString(uuid_service), UUID.fromString(uuid_write), data, new BleWriteResponse() {
//
//            @Override
//            public void onResponse(int code) {
//
//                if (REQUEST_SUCCESS == code) {
//                    if (null != mIBleIndicateListener) {
//                        mIBleWriteListener.onWriteSuccess();
//                    }
//                } else if (REQUEST_FAILED == code) {
//                    if (null != mIBleIndicateListener) {
//                        mIBleWriteListener.onWriteFailure(code);
//                    }
//                }
//
//            }//end onResponse
//        });
//    }
//}


