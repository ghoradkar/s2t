package org.maniteja.com.synclib.helper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Sreyas V Pariyath on 4/22/16.
 */
public class Util
{
    Context context;
    Activity activity;

    public Util(Context context)
    {
        this.context = context;
    }

    public Util(Context context, Activity activity)
    {
        this.activity = activity;
        this.context = context;
    }

    public Util()
    {
    }

    public void putString(String key, String data)
    {
        SharedPreferences sp = context.getSharedPreferences("toucHBShared", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, data);
        editor.commit();
    }

    public String readString(String key, String defaultv)
    {
        SharedPreferences sp = context.getSharedPreferences("toucHBShared", Context.MODE_PRIVATE);
        return sp.getString(key, defaultv);
    }

    public double readDouble(String key, String defaultv)
    {
        SharedPreferences sp = context.getSharedPreferences("toucHBShared", Context.MODE_PRIVATE);
        return Double.parseDouble(sp.getString(key, defaultv));
    }

    public void putInt(String key, int data)
    {
        SharedPreferences sp = context.getSharedPreferences("toucHBShared", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, data);
        editor.commit();
    }

    public int readInt(String key, int def)
    {
        SharedPreferences sp = context.getSharedPreferences("toucHBShared", Context.MODE_PRIVATE);

        return sp.getInt(key, def);
    }

    public void putBoolean(String key, boolean data)
    {
        SharedPreferences sp = context.getSharedPreferences("toucHBShared", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, data);
        editor.commit();
    }

    public boolean readboolean(String key, boolean def)
    {
        SharedPreferences sp = context.getSharedPreferences("toucHBShared", Context.MODE_PRIVATE);
        return sp.getBoolean(key, def);
    }

    public String[] longToDateTime(long ms)
    {
        String datetime[] = getDate(ms, "dd MMM yyyy_hh:mmaa").split("_");

        return datetime;
    }

    public void splitWriteData(String str)
    {
        String[] writeStr = str.split(":");
        if(writeStr != null && writeStr.length == 2)
        {
            String[] firstdata = writeStr[0].split("_");
            String[] seconddata = writeStr[1].split("_");
            if(firstdata != null && firstdata.length == 7)
            {
                putInt(HelperC.key_M1, Integer.parseInt(firstdata[1]));
                putInt(HelperC.key_M2, Integer.parseInt(firstdata[2]));
                putInt(HelperC.key_M3, Integer.parseInt(firstdata[3]));
                putInt(HelperC.key_M4, Integer.parseInt(firstdata[4]));
                putInt(HelperC.key_HP, Integer.parseInt(firstdata[5]));
                putInt(HelperC.key_Eq, Integer.parseInt(firstdata[6]));
            }
            if(seconddata != null && seconddata.length == 3)
            {
                putInt(HelperC.key_M5, Integer.parseInt(seconddata[0]));
                putInt(HelperC.key_M6, Integer.parseInt(seconddata[1]));
                putInt(HelperC.key_MuFact, Integer.parseInt(seconddata[2]));
            }
        }
    }

    private String getDate(long milliSeconds, String dateFormat)
    {
        DateFormat formatter = new SimpleDateFormat(dateFormat);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        print("Time sync " + formatter.format(calendar.getTime()) + " " + milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public void print(String data)
    {
        System.out.println(data);
    }

    public String getVersion()
    {
        PackageManager manager = context.getPackageManager();
        PackageInfo info = null;
        try
        {
            info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        }catch (Exception e)
        {
            e.printStackTrace();
            return "";
        }
    }
}


