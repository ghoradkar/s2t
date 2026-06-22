package org.maniteja.com.synclib.helper;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;

/**
 * Created by maniteja on 2/7/18.
 */

public class SerializeUUID implements Serializable
{

    private static final long serialVersionUID = 1240619917902L;
    private String sync_service;
    private String sync_charac;
    private String test_service;
    private String test_charac;
    private String write_first_service;
    private String write_first_charac;
    private String write_second_service;
    private String write_second_charac;
    private String battery_level_service;
    private String battery_level_charac;
    private String device_info_service;
    private String manufac_name_charac;
    private String serial_number_charac;
    private String model_number_charac;
    private String system_id_charac;
    private String firmware_revision_charac;
    private String hardware_revision_charac;
    private String pnp_id_charac;
    private String ieee_charac;

    public String getSync_service()
    {
        return sync_service;
    }

    public SerializeUUID setSync_service(String sync_service)
    {
        this.sync_service = sync_service;
        return this;
    }

    public String getSync_charac()
    {
        return sync_charac;
    }

    public SerializeUUID setSync_charac(String sync_charac)
    {
        this.sync_charac = sync_charac;
        return this;
    }

    public String getTest_service()
    {
        return test_service;
    }

    public SerializeUUID setTest_service(String test_service)
    {
        this.test_service = test_service;
        return this;
    }

    public String getTest_charac()
    {
        return test_charac;
    }

    public SerializeUUID setTest_charac(String test_charac)
    {
        this.test_charac = test_charac;
        return this;
    }

    public String getWrite_first_service()
    {
        return write_first_service;
    }

    public SerializeUUID setWrite_first_service(String write_first_service)
    {
        this.write_first_service = write_first_service;
        return this;
    }

    public String getWrite_first_charac()
    {
        return write_first_charac;
    }

    public SerializeUUID setWrite_first_charac(String write_first_charac)
    {
        this.write_first_charac = write_first_charac;
        return this;
    }

    public String getWrite_second_service()
    {
        return write_second_service;
    }

    public SerializeUUID setWrite_second_service(String write_second_service)
    {
        this.write_second_service = write_second_service;
        return this;
    }

    public String getWrite_second_charac()
    {
        return write_second_charac;
    }

    public SerializeUUID setWrite_second_charac(String write_second_charac)
    {
        this.write_second_charac = write_second_charac;
        return this;
    }

    public String getBattery_level_service()
    {
        return battery_level_service;
    }

    public SerializeUUID setBattery_level_service(String battery_level_service)
    {
        this.battery_level_service = battery_level_service;
        return this;
    }

    public String getBattery_level_charac()
    {
        return battery_level_charac;
    }

    public SerializeUUID setBattery_level_charac(String battery_level_charac)
    {
        this.battery_level_charac = battery_level_charac;
        return this;
    }

    public String getDevice_info_service()
    {
        return device_info_service;
    }

    public SerializeUUID setDevice_info_service(String device_info_service)
    {
        this.device_info_service = device_info_service;
        return this;
    }

    public String getManufac_name_charac()
    {
        return manufac_name_charac;
    }

    public SerializeUUID setManufac_name_charac(String manufac_name_charac)
    {
        this.manufac_name_charac = manufac_name_charac;
        return this;
    }

    public String getSerial_number_charac()
    {
        return serial_number_charac;
    }

    public SerializeUUID setSerial_number_charac(String serial_number_charac)
    {
        this.serial_number_charac = serial_number_charac;
        return this;
    }

    public String getModel_number_charac()
    {
        return model_number_charac;
    }

    public SerializeUUID setModel_number_charac(String model_number_charac)
    {
        this.model_number_charac = model_number_charac;
        return this;
    }

    public String getSystem_id_charac()
    {
        return system_id_charac;
    }

    public SerializeUUID setSystem_id_charac(String system_id_charac)
    {
        this.system_id_charac = system_id_charac;
        return this;
    }

    public String getFirmware_revision_charac()
    {
        return firmware_revision_charac;
    }

    public SerializeUUID setFirmware_revision_charac(String firmware_revision_charac)
    {
        this.firmware_revision_charac = firmware_revision_charac;
        return this;
    }

    public String getHardware_revision_charac()
    {
        return hardware_revision_charac;
    }

    public SerializeUUID setHardware_revision_charac(String hardware_revision_charac)
    {
        this.hardware_revision_charac = hardware_revision_charac;
        return this;
    }

    public String getPnp_id_charac()
    {
        return pnp_id_charac;
    }

    public SerializeUUID setPnp_id_charac(String pnp_id_charac)
    {
        this.pnp_id_charac = pnp_id_charac;
        return this;
    }

    public String getIeee_charac()
    {
        return ieee_charac;
    }

    public SerializeUUID setIeee_charac(String ieee_charac)
    {
        this.ieee_charac = ieee_charac;
        return this;
    }

    public void readFile(InputStream fileIn)
    {
        SerializeUUID e = null;
        try
        {
            //FileInputStream fileIn = new FileInputStream("/Users/sreyasvpariyath/Documents/TestData/uchekserializenew.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            e = (SerializeUUID) in.readObject();
            setSync_service(e.getSync_service());
            setSync_charac(e.getSync_charac());
            setTest_service(e.getTest_service());
            setTest_charac(e.getTest_charac());
            setWrite_first_service(e.getWrite_first_service());
            setWrite_first_charac(e.getWrite_first_charac());
            setWrite_second_service(e.getWrite_second_service());
            setWrite_second_charac(e.getWrite_second_charac());
            setBattery_level_service(e.getBattery_level_service());
            setBattery_level_charac(e.getBattery_level_charac());
            setDevice_info_service(e.getDevice_info_service());
            setManufac_name_charac(e.getManufac_name_charac());
            setSerial_number_charac(e.getSerial_number_charac());
            setModel_number_charac(e.getModel_number_charac());
            setSystem_id_charac(e.getSystem_id_charac());
            setFirmware_revision_charac(e.getFirmware_revision_charac());
            setHardware_revision_charac(e.getHardware_revision_charac());
            setPnp_id_charac(e.getPnp_id_charac());
            setIeee_charac(e.getIeee_charac());
            in.close();
            fileIn.close();
        }
        catch (IOException i)
        {
            i.printStackTrace();
            return;
        }
        catch (ClassNotFoundException c)
        {
            System.out.println("Serialize UUID class not found");
            c.printStackTrace();
            return;
        }
    }
}
