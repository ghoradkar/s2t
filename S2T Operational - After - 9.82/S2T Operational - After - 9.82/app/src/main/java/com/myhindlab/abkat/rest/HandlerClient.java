package com.myhindlab.abkat.rest;

import android.content.Context;

import com.myhindlab.abkat.BuildConfig;
import com.myhindlab.abkat.utilities.ApplicationConstants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HandlerClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }
}
