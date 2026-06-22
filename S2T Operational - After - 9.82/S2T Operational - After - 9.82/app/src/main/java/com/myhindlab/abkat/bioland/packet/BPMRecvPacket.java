package com.myhindlab.abkat.bioland.packet;

import android.util.Log;

import com.myhindlab.abkat.bioland.util.MathUtil;

public class BPMRecvPacket {

    //
    protected final String TAG = this.getClass().getSimpleName();

    //Packet Flag
    public final static byte Packet_Flag = 0x55;

    //Packet Length
    public final static byte Packet_Length_Info = 0x12;
    public final static byte Packet_Length_CountDown = 0x08;
    public final static byte Packet_Length_Data = 0x0E;
    public final static byte Packet_Length_EE = 0x05;
    public final static byte Packet_Length_Over = 0x05;

    //Packet Type
    public final static byte Packet_Type_Info = 0x00;
    public final static byte Packet_Type_CountDown = 0x02;
    public final static byte Packet_Type_Data = 0x03;
    public final static byte Packet_Type_EE = (byte) 0xEE;
    public final static byte Packet_Type_Over = 0x05;

    public final static byte Packet_Type_Err = (byte) 0xFF;

    //
    private byte[] data;
    private boolean isVaidation = false;


    //
    public BPMRecvPacket(byte[] data) {
        this.data = new byte[data.length];
        for (int i = 0; i < data.length; i++) {
            this.data[i] = data[i];
        }

        validateData();
    }


    /**
     * public function implementation Begin
     */
    public byte getType() {
        byte retValue = Packet_Type_Err;

        if (isVaidation) {
            retValue = data[2];
        }

        return retValue;
    }

    public byte[] getData() {
        return data;
    }

    public String getHexData() {
        String retValue = "";

        for (int i = 0; i < data.length; i++) {
            String temp = Integer.toHexString(data[i] & 0x0FF);
            if (1 == temp.length()) {
                temp = "0" + temp;
            }
            retValue += temp.toUpperCase();
        }

        return retValue;
    }

    //SN
    public String getDeviceSN() {
        String retValue = "";

        if (isVaidation && Packet_Type_Info == getType()) {
            byte[] SNData = new byte[9];
            for (int i = 0; i < SNData.length; i++) {
                SNData[i] = data[i + 8];
            }

            retValue = new String(SNData);
        }

        return retValue;
    }

    //过程压力值
    public int getCountDown() {
        int retValue = 0;

        if (isVaidation && Packet_Type_CountDown == getType()) {
            retValue = (data[6] & 0x0FF) * 16 * 16 + (data[5] & 0x0FF);
        }

        return retValue;
    }

    //
    //测量时间
    public String getMeasureTime() {
        String retValue = "";

        if (isVaidation && Packet_Type_Data == getType()) {
            int iYear;
            int iMonth;
            int iDay;
            int iHour;
            int iMin;

            iYear = (data[3] & 0x0FF) + 2000;
            iMonth = (data[4] & 0x0FF);
            iDay = (data[5] & 0x0FF);
            iHour = (data[6] & 0x0FF);
            iMin = (data[7] & 0x0FF);

            retValue = String.format("%d-%02d-%02d %02d:%02d", iYear, iMonth, iDay, iHour, iMin);

        }

        return retValue;
    }

    public int getHRValue() {
        int retValue = 0;

        if (isVaidation && Packet_Type_Data == getType()) {
            retValue = (data[12] & 0x0FF);
        }

        return retValue;
    }

    public int getSystolicValue() {
        int retValue = 0;
        int retValueLast = 0;

        if (isVaidation && Packet_Type_Data == getType()) {
            String s = MathUtil.HToB(MathUtil.byteToHexString(data[10]));
            if (s.length() == 8) {
                if ("1".equals(s.substring(7,8))) {
                    retValueLast  = 256;
                }
            }
            retValue =  (data[9] & 0x0FF) + retValueLast;
        }

        return retValue;
    }

    public int getDiastolicValue() {
        int retValue = 0;

        if (isVaidation && Packet_Type_Data == getType()) {
            retValue = (data[11] & 0x0FF);
        }

        return retValue;
    }

    /**
     * 获取用户编号
     *
     * @return
     */
    public String getUserInfo() {
        String  retValue = "";

        if (isVaidation && Packet_Type_Data == getType()) {
            String s = MathUtil.HToB(MathUtil.byteToHexString(data[10]));
            if (s.length() == 8) {
                if ("0".equals(s.substring(2,3))){
                    retValue = " 用户1";
                }else {
                    retValue = " 用户2";
                }
                if ("1".equals(s.substring(0,1))){
                    retValue = retValue + " - 心律不齐";
                }
            }
        }

        return retValue;
    }


    /**
     * private function implementation Begin
     */
    private void validateData() {
        Log.e(TAG, ">>>validateData.");

        isVaidation = true;

        //1.Flag检测
        if (Packet_Flag != data[0]) {
            isVaidation = false;
            Log.e(TAG, "Flag is error.");
            return;
        }

        //2.长度检测
        if (Packet_Length_Info != data.length &&
                Packet_Length_CountDown != data.length &&
                Packet_Length_Data != data.length &&
                Packet_Length_Over != data.length) {
            Log.e(TAG, "data.length = " + data.length);
            Log.e(TAG, "Packet_Length_Info = " + Packet_Length_Info);


            isVaidation = false;
            Log.e(TAG, "Length is error.");
            return;
        }

        //3.类型检测
        if (Packet_Type_Info != data[2] &&
                Packet_Type_CountDown != data[2] &&
                Packet_Type_Data != data[2] &&
                Packet_Type_Over != data[2]) {
            isVaidation = false;
            Log.e(TAG, "Type is error.");
            return;
        }

        //4.校验和检测
        byte checksum = getCheckSum(data, data.length - 1);
        if (checksum != data[data.length - 1]) {
            isVaidation = false;
            Log.e(TAG, "CheckSum is error.");
            return;
        }


    }

    //
    private byte getCheckSum(byte[] data, int length) {
        byte checksum;

        checksum = 0;
        for (int i = 0; i < length; i++) {
            byte byteValue = data[i];

            checksum += byteValue;

        }

        checksum += 2;

        return checksum;
    }
}
