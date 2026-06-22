package com.myhindlab.abkat.bioland.packet;


import android.util.Log;

import java.math.BigDecimal;


public class TaiDocBGMRecvPacket {

    //
    protected final String TAG = this.getClass().getSimpleName();


    /**
     * Frame Structure
     *
     * Example : 8-byte frame
     * Format HEX
     *
     * Byte           0    |   1   |    2   |    3   |    4   |    5   |   6  |   7
     * Name         Start  |  CMD  | Data_0 | Data_1 | Data_2 | Data_3 | Stop | ChkSum
     * GW -> MD      51    |  CMD  | Data_0 | Data_1 | Data_2 | Data_3 |  A3  | [1..7]
     * GW <- MD      51    |  ACK  | Data_0 | Data_1 | Data_2 | Data_3 |  A5  | [1..7]
     *
     */


    //Packet Flag
    public final static byte Packet_Flag = (byte)0x51;

    //Packet Stop
    public final static byte Packet_Stop = (byte)0xA5;

    /**
     *
     * 暂时只处理指令 [0x26]
     *[0x26] MD response measure result
     *
     */

    //Packet Type
    public final static byte Packet_Type_26 = (byte)0x26;

    public final static byte Packet_Type_Err = (byte) 0xEE;

    //
    private byte[] data;
    private boolean isVaidation = false;


    //
    public TaiDocBGMRecvPacket(byte[] data)
    {
        this.data = new byte[data.length];
        for(int i = 0; i < data.length; i++)
        {
            this.data[i] = data[i];
        }

        validateData();
    }

    /**
     *
     * public static function implementation Begin
     *
     */


    /**
     *
     * public function implementation Begin
     *
     */
    public byte getType()
    {
        byte retValue = Packet_Type_Err;

        if( 8 == data.length )
        {
            retValue = data[1];
        }

        return retValue;
    }

    public byte[] getData()
    {
        return data;
    }

    public String getHexData()
    {
        String retValue = "";

        for (int i = 0; i < data.length; i++)
        {
            String temp = Integer.toHexString(data[i] & 0x0FF);
            if ( 1 == temp.length() )
            {
                temp = "0" + temp;
            }
            retValue += temp.toUpperCase();
        }

        return retValue;
    }

    public double getDataOfmmol()
    {
        double retValue;

        String valStr = "";

        if( isVaidation && Packet_Type_26 == getType() )
        {
            byte[] arrByte = { data[2], data[3] };
            valStr = bytes2mmol(arrByte);
        }

        try
        {
            retValue = Double.parseDouble(valStr);
        }
        catch (NumberFormatException e)
        {
            retValue = 0.0;
            e.printStackTrace();
        }

        return retValue;
    }

    public String getDisplayStr()
    {
        Log.e(TAG, ">>>getDisplayStr.");

        String retValue = "";

        byte type = getType();

        Log.e(TAG, "type = " + (type & 0x0FF));

        if( Packet_Type_26 == type )
        {
            retValue = "机器测试出血糖数据结果：" + getDataOfmmol() + " mmol";
        }



        return retValue;
    }

    /**
     *
     * private function implementation Begin
     *
     */
    private void validateData()
    {
        Log.e(TAG, ">>>validateData.");

        isVaidation = true;

        //1.Flag检测
        if( Packet_Flag != data[0] )
        {
            isVaidation = false;
            Log.e(TAG, "Flag is error.");
            return;
        }

        //2.长度检测
        if( 8 != data.length )
        {
            Log.e(TAG, "data.length = " + data.length);

            isVaidation = false;
            Log.e(TAG, "Length is error.");
            return;
        }

        //3.类型检测
        if( Packet_Type_26 != data[1] )
        {
            isVaidation = false;
            Log.e(TAG, "Type is error.");
            return;
        }

        //3.stop 检测
        if( Packet_Stop != data[6] )
        {
            isVaidation = false;
            Log.e(TAG, "Packet_Stop is error.");
            return;
        }

        //4.校验和检测
        byte checksum = getCheckSum(data, data.length - 1);
        if( checksum != data[data.length - 1] )
        {
            isVaidation = false;
            Log.e(TAG, "CheckSum is error.");
            return;
        }


    }

    //
    private byte getCheckSum(byte[] data, int length)
    {
        byte checksum;

        checksum = 0;
        for(int i = 0; i < length; i++)
        {
            byte byteValue = data[i];

            checksum += byteValue;

        }

        return checksum;
    }

    private String bytes2mmol(byte[] bytes)
    {
        String retValue;

        int iTempValue =  (bytes[1] & 0x0FF)*256 + (bytes[0] & 0x0FF);
        double fTempValue = (double)iTempValue / 18;
        BigDecimal bgValue = BigDecimal.valueOf(fTempValue);

        retValue = bgValue.setScale(1, BigDecimal.ROUND_HALF_UP).toString();

        return retValue;
    }
}

