package com.myhindlab.abkat.bioland;

public class Constant {


    //爱奥乐蓝牙设备特征值
    public static String GATT_SERVICE_PRIMARY = "00001000-0000-1000-8000-00805f9b34fb";
    public static String GATT_CHAR_WRITE = "00001001-0000-1000-8000-00805f9b34fb";
    public static String GATT_CHAR_READ = "00001002-0000-1000-8000-00805f9b34fb";

    //台湾乐生蓝牙设备特征值
    public static String GATT_LOSON_SERVICE = "0000ffe0-0000-1000-8000-00805f9b34fb";
    public static String GATT_LOSON_CHAR_WRITE = "0000ffe1-0000-1000-8000-00805f9b34fb";
    public static String GATT_LOSON_CHAR_READ = "0000ffe2-0000-1000-8000-00805f9b34fb";

    //TAIDOC 蓝牙血糖仪设备 特征值
    public static String GATT_TAIDOC_SERVICE = "00001523-1212-efde-1523-785feabcd123";
    public static String GATT_TAIDOC_CHAR_WRITE = "00001524-1212-efde-1523-785feabcd123";
    public static String GATT_TAIDOC_CHAR_READ = "00001524-1212-efde-1523-785feabcd123";

    /**
     *
     * 蓝牙已连接
     * 蓝牙已断开
     * 发现GATT服务
     * 收到蓝牙数据
     * 连接失败
     *
     */
    public final static String ACTION_GATT_CONNECTED = "com.ble.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED = "com.ble.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED = "com.ble.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE = "com.ble.ACTION_DATA_AVAILABLE";
    public final static String ACTION_CONNECTING_FAIL = "com.ble.ACTION_CONNECTING_FAIL";




    /**
     *
     * 设备类型
     *
     */
    public static int DeviceType_BSugar = 0x01; //血糖仪
    public static int DeviceType_BPres = 0x02; //血压计
    public static int DeviceType_themo = 0x03; //额温枪
    public static int DeviceType_UricAcid = 0x04; //尿酸仪

    public static int DeviceType_LosonBSugar = 0x05; //乐生血糖仪
    public static int DeviceType_TAIDOCBGM = 0x06; //泰博血糖仪

    public static int DeviceType_BSugarAndUric = 0x07; //血糖尿酸二合一


}
