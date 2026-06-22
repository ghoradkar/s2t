package com.myhindlab.abkat.bioland.packet;


public class TaiDocBGMSendPacket {


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
    public final static byte Packet_Stop = (byte)0xA3;


    /**
     *
     * 暂时只处理指令 [0x26]
     *[0x26] Read the storage data with index, part 2(result)
     *
     */

    //Packet Type
    public final static byte Packet_Type_26 = (byte)0x26; //读取测量数据

    public final static byte Packet_Type_Err = (byte) 0xEE;

    //
    private byte[] data;
    private boolean isVaidation = false;


    //
    public TaiDocBGMSendPacket(byte type)
    {
        byte[] args = { 0x00, 0x00, 0x00, 0x00};

        initData(type, args);
    }

    public TaiDocBGMSendPacket(byte type, byte[] args)
    {
        initData(type, args);
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
            retValue = data[3];
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
    private void initData(byte type, byte[] args)
    {
        //
        isVaidation = true;

        /**
         * 包数据长度固定为 8 byte
         */
        byte iLength;

        iLength = 8;


        data = new byte[iLength];

        int index;

        //
        index = 0;
        data[index] = Packet_Flag;

        //
        index = 1;
        data[index] = type;

        //
        index = 2;
        for(int i = 0; i < args.length; i++)
        {
            //请求指令的参数
            data[i + index] = args[i];
        }

        //
        index = 6;
        data[index] = Packet_Stop;

        //CheckSum
        index = 7;
        data[index] = getCheckSum(data, 7);
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
}
