package com.myhindlab.abkat.bioland.packet;

import android.util.Log;

import java.math.BigDecimal;
import java.util.Arrays;

public class LosonBSRecvPacket {

    //
    protected final String TAG = this.getClass().getSimpleName();


    //Packet Flag
    public final static byte Packet_Flag = (byte)0xF5;

    //Packet Direction
    public final static byte Packet_Direction = (byte)0xA0;

    //Packet Protocol
    public final static byte Packet_Protocol = (byte)0x50;

    //Packet Type
    public final static byte Packet_Type_B0 = (byte)0xB0; //在连续测试模式，提示插入试纸
    public final static byte Packet_Type_B1 = (byte)0xB1; //机器检测到试纸插入，准备开始测试
    public final static byte Packet_Type_B2 = (byte)0xB2; //试纸插入，机器各种自检错误
    public final static byte Packet_Type_B3 = (byte)0xB3; //机器上报使用环境温度
    public final static byte Packet_Type_B4 = (byte)0xB4; //机器自检正常后，提示滴入血液
    public final static byte Packet_Type_B5 = (byte)0xB5; //滴入血液后，机器开始倒计数
    public final static byte Packet_Type_B6 = (byte)0xB6; //机器测试出血糖数据结果
    public final static byte Packet_Type_B7 = (byte)0xB7; //应答机器存储的记忆数量
    public final static byte Packet_Type_B8 = (byte)0xB8; //应答机器存储的全部记忆数据
    public final static byte Packet_Type_B9 = (byte)0xB9; //应答机器存储的某一条记忆数据
    public final static byte Packet_Type_BA = (byte)0xBA; //关闭机器
    public final static byte Packet_Type_CC = (byte)0xCC; //错误命令
    public final static byte Packet_Type_Err = (byte) 0xEE;

    //
    private byte[] data;
    private boolean isVaidation = false;


    //
    public LosonBSRecvPacket(byte[] data)
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
    public static boolean isIntegrity(byte[] data)
    {
        boolean retValue = false;

        if( data.length > 4 )
        {
            int length = data[3];
            if( length == data.length )
            {
                retValue = true;
            }
        }

        return retValue;
    }

    /**
     *
     * public function implementation Begin
     *
     */
    public byte getType()
    {
        byte retValue = Packet_Type_Err;

        if( data.length > 4 )
        {
            retValue = data[4];
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

    public String getDisplayStr()
    {
        Log.e(TAG, ">>>getDisplayStr.");

        String retValue = "";

        byte type = getType();

        Log.e(TAG, "type = " + (type & 0x0FF));

        if( Packet_Type_B0 == type )
        {
            retValue = "在连续测试模式，提示插入试纸";
        }
        else if( Packet_Type_B1 == type )
        {
            retValue = "机器检测到试纸插入，准备开始测试";
        }
        else if( Packet_Type_B2 == type )
        {
            byte subType = data[6];
            if( 0x51 == subType)
            {
                retValue = "机器检测当前试纸已经使用过，提示试纸错误";

            }
            else if( 0x52 == subType)
            {
                retValue = "机器检测使用环境温度过低，提示低温错误";
            }
            else if( 0x53 == subType)
            {
                retValue = "机器检测使用环境温度过高，提示高温错误";
            }
            else if( 0x54 == subType)
            {
                retValue = "机器检测供电不足，提示电量低错误";
            }

        }
        else if( Packet_Type_B3 == type )
        {
            retValue = "环境温度: " + getTemperature();
        }
        else if( Packet_Type_B4 == type )
        {
            retValue = "滴入血液后，机器开始倒计数";
        }
        else if( Packet_Type_B5 == type )
        {
            retValue = "倒计时 " + getCountDown() + " 秒";
        }
        else if( Packet_Type_B6 == type )
        {
            retValue = "机器测试出血糖数据结果：" + getDataOfmmol() + " mmol";
        }
        else if( Packet_Type_B7 == type )
        {
            retValue = "记忆数量 " + getTotal() + " 条";
        }
        else if( Packet_Type_B8 == type ||  Packet_Type_B9 == type)
        {
            int total =  (data[5] & 0x0FF)*256 + (data[6] & 0x0FF);

            int index =  (data[7] & 0x0FF)*256 + (data[8] & 0x0FF);

            String timeStr = data[9] + "年" + data[10] + "月" + data[11] + "日" + data[12] + "时" + data[13] + "分" + data[14] + "秒";

            byte[] arrByte = { data[15], data[16] };

            String meterStr = bytes2mmol(arrByte);

            retValue = "第 " + index + "/" + total + " 记忆数据：" + timeStr + " " + meterStr + " mmol";

        }
        else if( Packet_Type_BA == type )
        {
            retValue = "关闭机器";
        }
        else if( Packet_Type_CC == type )
        {
            byte data5 = data[5];
            byte data6 = data[6];

            if( 0xCC == data5 && 0xCC == data6)
            {
                retValue = "错误命令";
            }
        }

        return retValue;
    }


    //设备使用环境温度
    public String getTemperature()
    {
        String retValue = "";

        if( isVaidation && Packet_Type_B3 == getType() )
        {
            int iTempValue =  (data[5] & 0x0FF)*256 + (data[6] & 0x0FF);
            double fTempValue = (double)iTempValue / 10;
            BigDecimal bgValue = BigDecimal.valueOf(fTempValue);

            retValue = bgValue.setScale(1, BigDecimal.ROUND_HALF_UP).toString();

            retValue += "℃";
        }

        return retValue;
    }

    public byte getCountDown()
    {
        byte retValue = 0;

        if( isVaidation && Packet_Type_B5 == getType() )
        {
            retValue = data[7];
        }

        return retValue;
    }

    public String getDataOfmmol()
    {
        String retValue = "";

        if( isVaidation && Packet_Type_B6 == getType() )
        {
            byte[] arrByte = { data[11], data[12] };
            retValue = bytes2mmol(arrByte);
        }

        return retValue;
    }

    public String getTotal()
    {
        String retValue = "";

        if( isVaidation && Packet_Type_B7 == getType() )
        {
            int iTempValue =  (data[5] & 0x0FF)*256 + (data[6] & 0x0FF);

            retValue = "" + iTempValue;
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

        //1.帧头检测
        byte[] headerData = { Packet_Flag, Packet_Direction, Packet_Protocol };
        String headerStr = Arrays.toString(headerData);

        byte[] tempData = { data[0], data[1], data[2] };
        for(int i = 0; i < tempData.length; i++)
        {
            //请求指令的参数
            tempData[i] = data[i];
        }
        String tempStr = Arrays.toString(headerData);

        if( !headerStr.equals(tempStr))
        {
            isVaidation = false;
            Log.e(TAG, "Flag is error.");
            return;
        }

        //2.类型检测
        //暂不处理，后续添加


        //3.校验和检测
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

        int iTempValue =  (bytes[0] & 0x0FF)*256 + (bytes[1] & 0x0FF);
        double fTempValue = (double)iTempValue / 18;
        BigDecimal bgValue = BigDecimal.valueOf(fTempValue);

        retValue = bgValue.setScale(1, BigDecimal.ROUND_HALF_UP).toString();

        return retValue;
    }
}
