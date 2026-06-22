package com.myhindlab.abkat;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;



import androidx.appcompat.app.AppCompatDelegate;
import androidx.multidex.MultiDexApplication;

import com.myhindlab.abkat.models.safey.AirTestResult;
import com.myhindlab.abkat.rest.ApiClient;
import com.myhindlab.abkat.rest.ApiInterface;
import com.myhindlab.abkat.utilities.ApplicationConstants;

import java.lang.ref.WeakReference;

public class HealthCheckup extends MultiDexApplication {

    public static Context context;
    public static Context AppContext;
    public  static AirTestResult firstTestResult = null;
    public static AirTestResult postTestResult  = null;

    private static HealthCheckup mInstance = null;

    public HealthCheckup() {
        mInstance = this;
    }

    public static HealthCheckup getInstance() {
        return mInstance;
    }
//    public static ABKATDatabase abkatDatabase;

    public static ApiInterface abkatApiClient;
    public static ApiInterface getD2DClient;
    public static ApiInterface abhaClient;
    public static ApiInterface ABDMClient;
    public static ApiInterface PHRSBXClient;
    public static ApiInterface myhindlabClient;

    private static WeakReference<Activity> currentActivity;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        AppContext = this;
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        createNotificationChannel();
//        abkatDatabase = Room.databaseBuilder(getApplicationContext(),
//                ABKATDatabase.class, ApplicationConstants.ABKAT_DB).fallbackToDestructiveMigration().build();


        /// for createAbhaSession  use abhaClientAPI

        getD2DClient = ApiClient.getD2DClient().create(ApiInterface.class);
        abkatApiClient = ApiClient.getClient().create(ApiInterface.class);

        if (BuildConfig.isBeta){
            abhaClient = ApiClient.getABHAClient().create(ApiInterface.class);

        }else {
            abhaClient = ApiClient.getABHAClientAPI().create(ApiInterface.class);
        }

        if (BuildConfig.isBeta){
            ABDMClient = ApiClient.getABHAHealthIDClient().create(ApiInterface.class);

        }else {
            ABDMClient = ApiClient.getABHAClient().create(ApiInterface.class);

        }
        PHRSBXClient = ApiClient.getABHAPHRSBXClient().create(ApiInterface.class);
        myhindlabClient = ApiClient.getMyHindlabClient().create(ApiInterface.class);
    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    ApplicationConstants.CHANNEL_ID,
                    ApplicationConstants.CHANNEL_TITLE,
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(serviceChannel);
        }
    }

    public static void setCurrentActivity(Activity activity) {
        currentActivity = new WeakReference<>(activity);
    }

    public static Activity getCurrentActivity() {
        return currentActivity != null ? currentActivity.get() : null;
    }
}
