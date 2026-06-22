package com.myhindlab.abkat.bioland.packet;

import android.util.Log;

import java.math.BigDecimal;

public class UAMRecvPacket {

    //
    protected final String TAG = this.getClass().getSimpleName();

    //Packet Flag
    public final static byte Packet_Flag = 0x55;

    //Packet Length
    public final static byte Packet_Length_Info = 0x12;
    public final static byte Packet_Length_CountDown = 0x06;
    public final static byte Packet_Length_Data = 0x0C;
    public final static byte Packet_Length_Over = 0x05;

    //Packet Type
    public final static byte Packet_Type_Info = 0x00;
    public final static byte Packet_Type_CountDown = 0x02;
    public final static byte Packet_Type_Data = 0x03;
    public final static byte Packet_Type_Over = 0x05;
    public final static byte Packet_Type_Err = (byte) 0xEE;

    //
    private byte[] data;
    private boolean isVaidation = false;


    //
    public UAMRecvPacket(byte[] data) {
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

    public byte getCountDown() {
        byte retValue = 0;

        if (isVaidation && Packet_Type_CountDown == getType()) {
            retValue = data[4];
        }

        return retValue;
    }

    public int getDataOfumol() {
        int retValue = -1;

        if (isVaidation && Packet_Type_Data == getType()) {
            retValue = (data[10] & 0x0FF) * 16 * 16 + (data[9] & 0x0FF);
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

    public String getDataOfmmol() {
        String retValue = "";

        if (isVaidation && Packet_Type_Data == getType()) {
            int iTempValue = (data[10] & 0x0FF) * 16 * 16 + (data[9] & 0x0FF);
            double fTempValue = (double) iTempValue / 18;

            BigDecimal bgValue = BigDecimal.valueOf(fTempValue);
            BigDecimal bgResult = bgValue.setScale(1, BigDecimal.ROUND_HALF_UP);


            if (bgResult.doubleValue() >= 33.4) {
                retValue = "HI";

            } else if (bgResult.doubleValue() <= 1.0) {
                retValue = "LO";
            } else {
                retValue = bgResult.toString();
            }

        }

        return retValue;
    }

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
}
