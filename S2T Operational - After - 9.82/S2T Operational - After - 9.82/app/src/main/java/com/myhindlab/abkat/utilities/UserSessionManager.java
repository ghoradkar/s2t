package com.myhindlab.abkat.utilities;

import static com.myhindlab.abkat.utilities.ApplicationConstants.IS_USER_LOGIN;
import static com.myhindlab.abkat.utilities.ApplicationConstants.KEY_LAST_LOGIN_DATE;
import static com.myhindlab.abkat.utilities.Utilities.getCurrentDate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.work.WorkManager;

import com.google.gson.Gson;
import com.myhindlab.abkat.BuildConfig;
import com.myhindlab.abkat.activities.Login_Activity;
import com.myhindlab.abkat.activities.SplashScreen_Activity;
import com.myhindlab.abkat.models.BluetoothDeviceModel;
import com.myhindlab.abkat.models.UserDetailsModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class UserSessionManager {
    private SharedPreferences pref;
    private Editor editor;
    private Context _context;
    private int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "UserSession";
    private static final String KEY_LAST_LOGIN_DATE = "lastLoginDate";

    // Constructor
    public UserSessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(ApplicationConstants.PREFER_NAME,
                PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createUserLoginSession(String login) {
        pref = _context.getSharedPreferences(ApplicationConstants.PREFER_NAME,
                Context.MODE_PRIVATE);
        editor = pref.edit();
//        editor.putBoolean(ApplicationConstants.IS_USER_LOGIN, true);
        editor.putBoolean(ApplicationConstants.IS_HLL_USER, false);
        editor.putString(ApplicationConstants.KEY_LOGIN_INFO, login);
        editor.commit();
    }

    public void createUserLoginSessionAfterOtp() {
        pref = _context.getSharedPreferences(ApplicationConstants.PREFER_NAME,
                Context.MODE_PRIVATE);
        editor = pref.edit();
        editor.putBoolean(ApplicationConstants.IS_USER_LOGIN, true);;
        editor.commit();
    }


    public void createSession() {
        editor.putBoolean(IS_USER_LOGIN, true);
        editor.putString(KEY_LAST_LOGIN_DATE, getCurrentDate());
        editor.apply();
    }


    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date());
    }

    public boolean isSessionExpired() {
        String lastLoginDate = pref.getString(KEY_LAST_LOGIN_DATE, ""); // Use sharedPreferences correctly
        return !lastLoginDate.equals(getCurrentDate());
    }


    public void createHllUserLoginSession(String login) {
        pref = _context.getSharedPreferences(ApplicationConstants.PREFER_NAME,
                Context.MODE_PRIVATE);
        editor = pref.edit();
        editor.putBoolean(ApplicationConstants.IS_USER_LOGIN, true);
        editor.putBoolean(ApplicationConstants.IS_HLL_USER, true);
        editor.putString(ApplicationConstants.KEY_LOGIN_INFO, login);
        editor.commit();
    }
    public void loginTrue() {
        pref = _context.getSharedPreferences(ApplicationConstants.PREFER_NAME,
                Context.MODE_PRIVATE);
        editor = pref.edit();
        editor.putBoolean(ApplicationConstants.IS_USER_LOGIN, true);
//        editor.putBoolean(ApplicationConstants.IS_HLL_USER, true);
//        editor.putString(ApplicationConstants.KEY_LOGIN_INFO, login);
        editor.commit();
    }



    public void createHllUserSession(boolean isHllUser) {
        pref = _context.getSharedPreferences(ApplicationConstants.PREFER_NAME,
                Context.MODE_PRIVATE);
        editor = pref.edit();
        editor.putBoolean(ApplicationConstants.IS_HLL_USER, isHllUser);
        editor.commit();
    }

    public String getSessionMobile() {
        pref = _context.getSharedPreferences(ApplicationConstants.PREFER_NAME,
                Context.MODE_PRIVATE);
        return pref.getString(ApplicationConstants.SESSION_USER_MOBILE, "");
    }

    public String getSessionDistrict() {
        pref = _context.getSharedPreferences(ApplicationConstants.PREFER_NAME,
                Context.MODE_PRIVATE);
        return pref.getString(ApplicationConstants.SESSION_USER_DISTRICT, "");
    }




    public HashMap<String, String> getUserDetails() {
        pref = _context.getSharedPreferences(ApplicationConstants.PREFER_NAME,
                Context.MODE_PRIVATE);

        HashMap<String, String> user = new HashMap<String, String>();

        user.put(ApplicationConstants.KEY_LOGIN_INFO,
                pref.getString(ApplicationConstants.KEY_LOGIN_INFO, null));

        return user;
    }

    public UserDetailsModel.Output getUserDetailsJson() {
        pref = _context.getSharedPreferences(ApplicationConstants.PREFER_NAME,
                Context.MODE_PRIVATE);

        HashMap<String, String> user = new HashMap<String, String>();

        user.put(ApplicationConstants.KEY_LOGIN_INFO,
                pref.getString(ApplicationConstants.KEY_LOGIN_INFO, null));

        try {
            JSONArray user_info = new JSONArray(getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                return new Gson().fromJson(user_info.getString(j), UserDetailsModel.Output.class);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isHllUser() {
        pref = _context.getSharedPreferences(ApplicationConstants.PREFER_NAME,
                Context.MODE_PRIVATE);
//        HashMap<String, boolean> user = new HashMap<String, String>();
//        user.put(ApplicationConstants.USER_LOGIN_TYPE,
//                pref.getString(ApplicationConstants.USER_LOGIN_TYPE, null));
        return pref.getBoolean(ApplicationConstants.IS_HLL_USER, false);
    }

//    public void logoutUser() {
//        WorkManager.getInstance(_context).cancelAllWork();
//
//        cleanLoginInfo();
//        Intent i = new Intent(_context, SplashScreen_Activity.class);
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        _context.startActivity(i);
//
//        if (Build.VERSION.SDK_INT >= 16) {
//            ((Activity) _context).finishAffinity();
//        } else {
//            ActivityCompat.finishAffinity((Activity) _context);
//        }
//
//    }

    public void logoutUser() {
        WorkManager.getInstance(_context).cancelAllWork();

        cleanLoginInfo();
        Intent i = new Intent(_context, Login_Activity.class); // Change to LoginActivity
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);

        if (Build.VERSION.SDK_INT >= 16) {
            ((Activity) _context).finishAffinity();
        } else {
            ActivityCompat.finishAffinity((Activity) _context);
        }
    }//


    public boolean isLoggedIn() {
        return pref.getBoolean(IS_USER_LOGIN, false);
    }

    public void saveDeviceID(String deviceID) {
        pref = _context.getSharedPreferences(ApplicationConstants.PREFER_NAME,
                Context.MODE_PRIVATE);
        editor = pref.edit();
        editor.putString(ApplicationConstants.KEY_DEVICE_INFO, deviceID);
        editor.commit();
    }

    public boolean getForceLogout() {
        pref = _context.getSharedPreferences(ApplicationConstants.PREFER_NAME,
                Context.MODE_PRIVATE);
        return pref.getBoolean(ApplicationConstants.FORCE_LOGOUT, false);


    }

    public void logoutUser(Context _context) {
        WorkManager.getInstance(_context).cancelAllWork();

        cleanLoginInfoForceLogout();

//        System.exit(0);
        try {
            Runtime.getRuntime().exec("pm clear " + BuildConfig.APPLICATION_ID);
        } catch (IOException e) {
            e.printStackTrace();
        }
//
//        Intent i = new Intent(_context, SplashScreen_Activity.class);
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        _context.startActivity(i);
//        ActivityCompat.finishAffinity((Activity) _context);


//
//        if (Build.VERSION.SDK_INT >= 16) {
//            ((Activity) _context).finishAffinity();
//        } else {
//            ActivityCompat.finishAffinity((Activity) _context);
//        }


    }

    public void cleanLoginInfoForceLogout() {
        editor = pref.edit();
        editor.remove(ApplicationConstants.KEY_LOGIN_INFO);
        editor.remove(ApplicationConstants.IS_USER_LOGIN);
        editor.remove(ApplicationConstants.IS_HLL_USER);
        editor.putBoolean(ApplicationConstants.FORCE_LOGOUT, true);

        editor.apply();
        editor.commit();
    }

    public void cleanLoginInfo() {
        editor = pref.edit();
        editor.remove(ApplicationConstants.KEY_LOGIN_INFO);
        editor.remove(ApplicationConstants.IS_USER_LOGIN);
        editor.remove(ApplicationConstants.IS_HLL_USER);
        editor.remove(ApplicationConstants.FORCE_LOGOUT);
        editor.apply();
        editor.commit();
    }

    public void updateSession(String loginInfo) {
        editor = pref.edit();
        editor.remove(ApplicationConstants.KEY_LOGIN_INFO);
        editor.remove(ApplicationConstants.IS_USER_LOGIN);
        editor.apply();
        editor.commit();

        createUserLoginSession(loginInfo);
    }

    public boolean isUserLoggedIn() {
        return pref.getBoolean(ApplicationConstants.IS_USER_LOGIN, false);
    }


    public void setFakeLocationFlag(int flag) {
        pref = _context.getSharedPreferences(ApplicationConstants.PREFER_NAME,
                Context.MODE_PRIVATE);
        editor = pref.edit();
        editor.putInt(ApplicationConstants.KEY_LOCATIONFLAG, flag);
        editor.commit();
    }


    public void setDeviceName(String examinerNoteName) {
        pref = _context.getSharedPreferences(ApplicationConstants.PREFER_NAME,
                Context.MODE_PRIVATE);
        editor = pref.edit();
        editor.putString(ApplicationConstants.DEVICE_NAME, examinerNoteName);
        editor.commit();
    }

    public HashMap<String, String> getDeviceName() {
        pref = _context.getSharedPreferences(ApplicationConstants.PREFER_NAME, Context.MODE_PRIVATE);
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(ApplicationConstants.DEVICE_NAME, pref.getString(ApplicationConstants.DEVICE_NAME, ""));
        return user;
    }


    public void setDeviceId(String examinerNoteId) {
        pref = _context.getSharedPreferences(ApplicationConstants.PREFER_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
        editor.putString(ApplicationConstants.DEVICE_ID, examinerNoteId);
        editor.commit();
    }

    public HashMap<String, String> getDeviceId() {
        pref = _context.getSharedPreferences(ApplicationConstants.PREFER_NAME, Context.MODE_PRIVATE);
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(ApplicationConstants.DEVICE_ID, pref.getString(ApplicationConstants.DEVICE_ID, ""));
        return user;
    }


//  public HashMap<String, String> getIsStaticalView() {
//    pref = _context.getSharedPreferences(ApplicationConstants.PREFER_NAME,
//            Context.MODE_PRIVATE);
//    HashMap<String, String> flag = new HashMap<String, String>();
//    flag.put(ApplicationConstants.KEY_STATICALVIEW,
//            pref.getString(ApplicationConstants.KEY_STATICALVIEW, "0"));
//    return flag;
//  }
//
//  public void setStaticalView(int flag) {
//    pref = _context.getSharedPreferences(ApplicationConstants.PREFER_NAME,
//            Context.MODE_PRIVATE);
//    editor = pref.edit();
//    editor.putString(ApplicationConstants.KEY_STATICALVIEW, String.valueOf(flag));
//    editor.commit();
//  }

    public void setBluetoothDevice(String name, String macAddress) {
        pref = _context.getSharedPreferences(ApplicationConstants.PREFER_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
        editor.putString(ApplicationConstants.KEY_BLUETOOTH_NAME, name);
        editor.putString(ApplicationConstants.KEY_BLUETOOTH_MAC_ADDRESS, macAddress);
        editor.commit();
    }


    public HashMap<String, String> getBluetoothName() {
        pref = _context.getSharedPreferences(ApplicationConstants.PREFER_NAME,
                Context.MODE_PRIVATE);
        HashMap<String, String> androidTokenID = new HashMap<>();
        androidTokenID.put(ApplicationConstants.KEY_BLUETOOTH_NAME,
                pref.getString(ApplicationConstants.KEY_BLUETOOTH_NAME, ""));
        return androidTokenID;
    }

    public HashMap<String, String> getBluetoothMacAddress() {
        pref = _context.getSharedPreferences(ApplicationConstants.PREFER_NAME,
                Context.MODE_PRIVATE);
        HashMap<String, String> androidTokenID = new HashMap<>();
        androidTokenID.put(ApplicationConstants.KEY_BLUETOOTH_MAC_ADDRESS,
                pref.getString(ApplicationConstants.KEY_BLUETOOTH_MAC_ADDRESS, ""));
        return androidTokenID;
    }

    public void setSPO(String spo2) {
        pref = _context.getSharedPreferences(ApplicationConstants.PREFER_NAME,
                Context.MODE_PRIVATE);
        editor = pref.edit();
        editor.putString(ApplicationConstants.KEY_SPO, spo2);
        editor.apply();
        editor.commit();

    }

    public void setTemp(String temp) {
        pref = _context.getSharedPreferences(ApplicationConstants.PREFER_NAME,
                Context.MODE_PRIVATE);
        editor = pref.edit();
        editor.putString(ApplicationConstants.KEY_TEMPERATURE, temp);
        editor.apply();
        editor.commit();

    }

    public String getSPO() {
        pref = _context.getSharedPreferences(ApplicationConstants.PREFER_NAME,
                Context.MODE_PRIVATE);

        return pref.getString(ApplicationConstants.KEY_SPO, ApplicationConstants.KEY_SPO);
    }

    public String getTemp() {
        pref = _context.getSharedPreferences(ApplicationConstants.PREFER_NAME,
                Context.MODE_PRIVATE);

        return pref.getString(ApplicationConstants.KEY_TEMPERATURE, ApplicationConstants.KEY_TEMPERATURE);
    }


    public boolean getIsLFT1DeviceConfigured() {
        pref = _context.getSharedPreferences(ApplicationConstants.PREFER_NAME, Context.MODE_PRIVATE);
        return pref.getBoolean(ApplicationConstants.IS_LFT1_DEVICE_CONFIGURED, false);
    }


    public void createLFT2MachineSession(String device) {
        pref = _context.getSharedPreferences(ApplicationConstants.PREFER_NAME,
                Context.MODE_PRIVATE);
        editor = pref.edit();
        editor.putString(ApplicationConstants.LFT_DEVICE_2, device);
        editor.putBoolean(ApplicationConstants.IS_LFT2_DEVICE_CONFIGURED, true);

        editor.commit();
    }

    public BluetoothDeviceModel getLFT2Device() {
        pref = _context.getSharedPreferences(ApplicationConstants.PREFER_NAME, Context.MODE_PRIVATE);
        return new Gson().fromJson(pref.getString(ApplicationConstants.LFT_DEVICE_2, ""), BluetoothDeviceModel.class);
    }

    public boolean getIsLFT2DeviceConfigured() {
        pref = _context.getSharedPreferences(ApplicationConstants.PREFER_NAME, Context.MODE_PRIVATE);
        return pref.getBoolean(ApplicationConstants.IS_LFT2_DEVICE_CONFIGURED, false);
    }

    public void createLFT1MachineSession(String device) {
        pref = _context.getSharedPreferences(ApplicationConstants.PREFER_NAME,
                Context.MODE_PRIVATE);
        editor = pref.edit();
        editor.putString(ApplicationConstants.LFT_DEVICE_1, device);
        editor.putBoolean(ApplicationConstants.IS_LFT1_DEVICE_CONFIGURED, true);

        editor.commit();
    }

    public BluetoothDeviceModel getLFT1Device() {
        pref = _context.getSharedPreferences(ApplicationConstants.PREFER_NAME, Context.MODE_PRIVATE);
        return new Gson().fromJson(pref.getString(ApplicationConstants.LFT_DEVICE_1, ""), BluetoothDeviceModel.class);
    }

    public void setOmronBPMac(String value) {
        pref = _context.getSharedPreferences(ApplicationConstants.PREFER_NAME,
                Context.MODE_PRIVATE);
        editor = pref.edit();
        editor.putBoolean(ApplicationConstants.IS_OMRON_BP_MAC, true);
        editor.putString(ApplicationConstants.OMRON_BP_MAC, value);
        editor.commit();
    }

    public String getOmronBPMac() {
        pref = _context.getSharedPreferences(ApplicationConstants.PREFER_NAME,
                Context.MODE_PRIVATE);

        return pref.getString(ApplicationConstants.OMRON_BP_MAC,"");
    }

    public Boolean isOmronBPPaired() {
        pref = _context.getSharedPreferences(ApplicationConstants.PREFER_NAME,
                Context.MODE_PRIVATE);

        return pref.getBoolean(ApplicationConstants.IS_OMRON_BP_MAC, false);
    }

    public void storeFCMToken(String token){
        pref=_context.getSharedPreferences(ApplicationConstants.PREFER_NAME,Context.MODE_PRIVATE);
        editor=pref.edit();

        editor.putString(ApplicationConstants.FCM_TOKEN,token);
        editor.commit();
        editor.apply();

    }


    public String getFCMToken(){
        pref=_context.getSharedPreferences(ApplicationConstants.PREFER_NAME,Context.MODE_PRIVATE);
        return pref.getString(ApplicationConstants.FCM_TOKEN,"");
    }
}