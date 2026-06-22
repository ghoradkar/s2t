package com.myhindlab.abkat.bioland.packet;


public class LosonBSSendPacket {

    //Packet Flag
    public final static byte Packet_Flag = (byte)0xF5;

    //Packet Direction
    public final static byte Packet_Direction = (byte)0xB0;

    //Packet Protocol
    public final static byte Packet_Protocol = (byte)0x50;

    //Packet Type
    public final static byte Packet_Type_A7 = (byte)0xA7; //读出记忆数据总条数
    public final static byte Packet_Type_A8 = (byte)0xA8; //读出全部记忆数据
    public final static byte Packet_Type_A9 = (byte)0xA9; //读出某一条记忆数据
    public final static byte Packet_Type_AA = (byte)0xAA; //关闭机器
    public final static byte Packet_Type_Err = (byte) 0xEE;

    //
    private byte[] data;
    private boolean isVaidation = false;


    //
    public LosonBSSendPacket(byte type)
    {
        byte[] args = { 0x00, 0x00};

        initData(type, args);
    }

    public LosonBSSendPacket(byte type, byte[] args)
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
         * 计算包数据长度
         * Flag + Direction + Protocol + Length + Type + Data + CheckSum
         */
        byte iLength;

        iLength = (byte) ((1 + 1 + 1) + 1 + ( 1 + args.length ) + 1);


        data = new byte[iLength];

        int index;

        //
        index = 0;
        data[index] = Packet_Flag;

        //
        index = 1;
        data[index] = Packet_Direction;

        //
        index = 2;
        data[index] = Packet_Protocol;

        //
        index = 3;
        data[index] = iLength;

        //
        index = 4;
        data[index] = type;

        //
        index = 5;
        for(int i = 0; i < args.length; i++)
        {
            //请求指令的参数
            data[i + index] = args[i];
        }

        //CheckSum
        index = data.length -1;
        data[index] = getCheckSum(data, data.length -1);
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
