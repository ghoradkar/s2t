package com.myhindlab.abkat.bioland.packet;

import java.util.Calendar;

public class UAMSendPacket {

    //Packet Flag
    public final static byte Packet_Flag = 0x5A;

    //Packet Type
    public final static byte Packet_Type_Info = 0x00;
    public final static byte Packet_Type_Data = 0x03;
    public final static byte Packet_Type_Err = (byte) 0xEE;

    //
    private byte[] data;
    private boolean isVaidation = false;


    //
    public UAMSendPacket(byte type)
    {
        initData(type);
    }


    /**
     *
     * public function implementation Begin
     *
     */
    public byte getType()
    {
        byte retValue = Packet_Type_Err;

        if( isVaidation )
        {
            retValue = data[2];
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

    /**
     *
     * private function implementation Begin
     *
     */
    private void initData(byte type)
    {
        if( Packet_Type_Info != type && Packet_Type_Data != type)
        {
            return;
        }

        isVaidation = true;

        //
        data = new byte[0x0A];


        data[0] = Packet_Flag; //flag
        data[1] = 0x0A; //length
        data[2] = type; //type

        //Year Month Day Hour Minute Second
        Calendar calendar = Calendar.getInstance();

        //data[3] = (byte) calendar.get(Calendar.YEAR); //year
        int iYear = calendar.get(Calendar.YEAR);
        data[3] = (byte)( iYear - (iYear/100)*100) ; //year
        data[4] = (byte) (calendar.get(Calendar.MONTH) + 1 ); //month
        data[5] = (byte) calendar.get(Calendar.DAY_OF_MONTH); //day
        data[6] = (byte) calendar.get(Calendar.HOUR_OF_DAY); //hour
        data[7] = (byte) calendar.get(Calendar.MINUTE); //minute
        data[8] = (byte) calendar.get(Calendar.SECOND); //second

        //CheckSum
        data[9] = getCheckSum(data, data.length -1);
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

        checksum += 2;

        return checksum;
    }
}
