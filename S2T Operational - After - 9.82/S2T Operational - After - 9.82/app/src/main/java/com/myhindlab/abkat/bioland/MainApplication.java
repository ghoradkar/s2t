package com.myhindlab.abkat.bioland;


import android.app.Application;



public class MainApplication extends Application {

    protected final String TAG = this.getClass().getSimpleName();



    private static MainApplication instance;




    /**
     * 生命周期函数 implementation Begin
     */

    @Override
    public void onCreate()
    {
        super.onCreate();

        instance = this;


    }

    @Override
    public void onTerminate()
    {


        //do thing


        super.onTerminate();
    }


    @Override
    public void onLowMemory()
    {
        super.onLowMemory();


    }

    @Override
    public void onTrimMemory(int level)
    {
        super.onTrimMemory(level);


    }

    /**
     *  接口函数 implementation Begin
     */


    public static MainApplication getInstance()
    {
        return instance;
    }

    public static MainApplication getContext()
    {
        return instance;
    }



}
