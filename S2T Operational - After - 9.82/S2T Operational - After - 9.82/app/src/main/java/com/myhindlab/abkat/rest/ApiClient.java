package com.myhindlab.abkat.rest;

import android.os.Build;
import android.os.RecoverySystem;

import com.myhindlab.abkat.BuildConfig;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.network_monitor.CountingRequestBody;
import com.myhindlab.abkat.utilities.network_monitor.ProgressInterceptor;
import com.myhindlab.abkat.utilities.network_monitor.ProgressListener;
import com.myhindlab.abkat.utilities.network_monitor.ProgressResponseBody;

import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit retrofit = null;
    private static Retrofit MAHABOC = null;

    private static Retrofit ABHARetrofit = null;

    private static Retrofit ABHAAPIRetrofit = null;

    private static Retrofit ABDMRetrofit = null;
    private static Retrofit PHRSBXRetrofit = null;

    private static Retrofit reportMyHindlabClient = null;


    private static HttpLoggingInterceptor logging = new HttpLoggingInterceptor();


    public static Retrofit getClient() {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(25, TimeUnit.SECONDS)
                .writeTimeout(25, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)   // IMPORTANT

                .build();

        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(ApplicationConstants.webservice)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }


    public static Retrofit getABHAClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .build();

        if (ABHARetrofit == null) {
            ABHARetrofit = new Retrofit.Builder()
                    .baseUrl(com.myhindlab.abkat.BuildConfig.ABHA)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return ABHARetrofit;
    }


    public static Retrofit getABHAClientAPI() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .build();

        if (ABHAAPIRetrofit == null) {
            ABHAAPIRetrofit = new Retrofit.Builder()
                    .baseUrl(com.myhindlab.abkat.BuildConfig.ABHAAPI)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return ABHAAPIRetrofit;
    }

    public static Retrofit getABHAHealthIDClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .build();

        if (ABDMRetrofit == null) {
            ABDMRetrofit = new Retrofit.Builder()
                    .baseUrl(com.myhindlab.abkat.BuildConfig.HealthIDSBX.trim())
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return ABDMRetrofit;
    }

    public static Retrofit getABHAPHRSBXClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .build();

        if (PHRSBXRetrofit == null) {
            PHRSBXRetrofit = new Retrofit.Builder()
                    .baseUrl(com.myhindlab.abkat.BuildConfig.PHRSBX)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return PHRSBXRetrofit;
    }


    public static Retrofit getMyHindlabClient() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .connectTimeout(30, TimeUnit.SECONDS)
//                .writeTimeout(30, TimeUnit.SECONDS)
//                .readTimeout(30,TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(logging)
                .build();

        reportMyHindlabClient = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(com.myhindlab.abkat.BuildConfig.reportMyhindlab)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return reportMyHindlabClient;
    }

//    public static Retrofit getClient() {
//        OkHttpClient okHttpClient;
//
//            // Use unsafe client on older devices or in debug mode
//            okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient().newBuilder()
//                    .connectTimeout(5, TimeUnit.MINUTES)
//                    .readTimeout(5, TimeUnit.MINUTES)
//                    .writeTimeout(5, TimeUnit.MINUTES)
//                    .build();
//
//
//        return new Retrofit.Builder()
//                .client(okHttpClient)
//                .baseUrl(ApplicationConstants.webservice)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//    }

    public static Retrofit getUploadClient() {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(25, TimeUnit.SECONDS)
                .writeTimeout(25, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)   // IMPORTANT

                .build();

        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(ApplicationConstants.webserviceHandlerExpense)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }


//    public static Retrofit getUploadClient() {
//        OkHttpClient okHttpClient;
//
//            // Use unsafe client only in debug mode or on older Android versions
//            okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient().newBuilder()
//                    .connectTimeout(5, TimeUnit.MINUTES)
//                    .readTimeout(5, TimeUnit.MINUTES)
//                    .writeTimeout(5, TimeUnit.MINUTES)
//                    .build();
//
//
//        return new Retrofit.Builder()
//                .client(okHttpClient)
//                .baseUrl(ApplicationConstants.webserviceHandlerExpense)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//    }


    public static Retrofit getMyOperator() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);


        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .build();

        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://obd-api.myoperator.co/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static Retrofit getVodaphoneOperator() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .build();

        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://cts.myvi.in:8443/Cpaas/api/v1/clicktocall/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }



    public static Retrofit getDishaAPI() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);


        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .build();

        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BuildConfig.DISHAAPI)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    public static Retrofit getD2DClient() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(25, TimeUnit.SECONDS)
                .writeTimeout(25, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)   // IMPORTANT
                .addInterceptor(logging)
                .build();

        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(ApplicationConstants.webservice_d2d)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

    public static Retrofit getD2DClient(ProgressListener pl) {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(25, TimeUnit.SECONDS)
                .writeTimeout(25, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)   // IMPORTANT
                .addInterceptor(new ProgressInterceptor(pl))
                .addInterceptor(logging)
                .build();

        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(ApplicationConstants.webservice_d2d)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

//
//    public class ApiClientProgress {
//
//        private static final int TIMEOUT_SECONDS = 60;
//
//        // Use this to create a Retrofit service wired with progress listener
//        public static <T> T createServiceWithProgress(Class<T> serviceClass,
//                                                      String baseUrl,
//                                                      RecoverySystem.ProgressListener progressListener,
//                                                      boolean enableLogging) {
//            OkHttpClient base = baseClientBuilder(enableLogging).build();
//
//            OkHttpClient clientWithProgress = base.newBuilder()
//                    .addInterceptor(new CountingRequestInterceptor(progressListener))
//                    .addNetworkInterceptor(new ProgressResponseInterceptor(progressListener))
//                    .build();
//
//            Retrofit retrofit = new Retrofit.Builder()
//                    .baseUrl(baseUrl)
//                    .client(clientWithProgress)
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//
//            return retrofit.create(serviceClass);
//        }
//
//        // Shared base client config (timeouts, optionally logging)
//        private static OkHttpClient.Builder baseClientBuilder(boolean enableLogging) {
//            OkHttpClient.Builder b = new OkHttpClient.Builder()
//                    .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
//                    .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
//                    .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS);
//
//            if (enableLogging) {
//                // Optional: add HttpLoggingInterceptor if needed (make sure it's in gradle)
//                // HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//                // logging.setLevel(HttpLoggingInterceptor.Level.BODY);
//                // b.addInterceptor(logging);
//            }
//
//            return b;
//        }
//
//        // Interceptor to wrap request body for upload progress
//        private static class CountingRequestInterceptor implements Interceptor {
//            private final RecoverySystem.ProgressListener listener;
//            CountingRequestInterceptor(RecoverySystem.ProgressListener listener) { this.listener = listener; }
//
//            @Override
//            public Response intercept(Chain chain) throws java.io.IOException {
//                Request original = chain.request();
//                RequestBody body = original.body();
//                if (body == null || listener == null) {
//                    return chain.proceed(original);
//                }
//                Request.Builder reqBuilder = original.newBuilder();
//                RequestBody countingBody = new CountingRequestBody(body, listener);
//                reqBuilder.method(original.method(), countingBody);
//                return chain.proceed(reqBuilder.build());
//            }
//        }
//
//        // Network interceptor to wrap response body for download progress
//        private static class ProgressResponseInterceptor implements Interceptor {
//            private final RecoverySystem.ProgressListener listener;
//            ProgressResponseInterceptor(RecoverySystem.ProgressListener listener) { this.listener = listener; }
//
//            @Override
//            public Response intercept(Chain chain) throws java.io.IOException {
//                Response originalResponse = chain.proceed(chain.request());
//                ResponseBody body = originalResponse.body();
//                if (body == null || listener == null) return originalResponse;
//                ProgressResponseBody progressBody = new ProgressResponseBody(body, listener);
//                return originalResponse.newBuilder().body(progressBody).build();
//            }
//        }
//    }

    //dfnaE_BSS8mYvqab2SNRD-%3AAPA91bF0z-ClouqHi36FXIsfXfojWgq8Auz2K98DJDjeaEWs6U6DyDb7NHaRSceyC8qtqrdflnpQf-XNcH9fCy366oqKHetTTWCaXDHCrBIf9-0XjtZXi2c


//    public static Retrofit getD2DClient() {
//        OkHttpClient okHttpClient;
//
//
//            // Use unsafe client in debug builds or older devices
//            okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient().newBuilder()
//                    .connectTimeout(5, TimeUnit.MINUTES)
//                    .readTimeout(5, TimeUnit.MINUTES)
//                    .writeTimeout(5, TimeUnit.MINUTES)
//                    .build();
//
//
//        return new Retrofit.Builder()
//                .client(okHttpClient)
//                .baseUrl(ApplicationConstants.webservice_d2d)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//    }

    public static Retrofit GetDashboardCountForLiverScanning() {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .build();

        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(ApplicationConstants.webservice_php)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }


//    public static Retrofit GetDashboardCountForLiverScanning() {
//        OkHttpClient okHttpClient;
//
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N || BuildConfig.DEBUG) {
//            // Allow unsafe SSL on older devices or in debug mode
//            okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient().newBuilder()
//                    .connectTimeout(1, TimeUnit.MINUTES)
//                    .readTimeout(5, TimeUnit.MINUTES)
//                    .writeTimeout(5, TimeUnit.MINUTES)
//                    .build();
//        } else {
//            okHttpClient = new OkHttpClient.Builder()
//                    .connectTimeout(1, TimeUnit.MINUTES)
//                    .readTimeout(5, TimeUnit.MINUTES)
//                    .writeTimeout(5, TimeUnit.MINUTES)
//                    .build();
//        }
//
//        return new Retrofit.Builder()
//                .client(okHttpClient)
//                .baseUrl(ApplicationConstants.webservice_php)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//    }


    public static Retrofit MahabocwAPICall() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .connectTimeout(30, TimeUnit.SECONDS)
//                .writeTimeout(30, TimeUnit.SECONDS)
//                .readTimeout(30,TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(logging)
                .build();

        MAHABOC = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(ApplicationConstants.MAHABOCBASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return MAHABOC;
    }

//    public static Retrofit MahabocwAPICall() {
//
//        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
//
//        OkHttpClient okHttpClient;
//
//            // Use unsafe client in older devices or during debug
//            okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient().newBuilder()
//                    .retryOnConnectionFailure(true)
//                    .addInterceptor(logging)
//                    .build();
//
//
//        MAHABOC = new Retrofit.Builder()
//                .client(okHttpClient)
//                .baseUrl(ApplicationConstants.MAHABOCBASEURL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        return MAHABOC;
//    }


    public static Retrofit web_forcalllist() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(25, TimeUnit.SECONDS)
                .writeTimeout(25, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)   // IMPORTANT

                .addInterceptor(logging)
                .build();

        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(ApplicationConstants.webservice_forcallist)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }


//    public static Retrofit web_forcalllist() {
//
//        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
//
//        OkHttpClient okHttpClient;
//
//            okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient().newBuilder()
//                    .connectTimeout(5, TimeUnit.MINUTES)
//                    .readTimeout(5, TimeUnit.MINUTES)
//                    .writeTimeout(5, TimeUnit.MINUTES)
//                    .addInterceptor(logging)
//                    .build();
//
//
//        retrofit = new Retrofit.Builder()
//                .client(okHttpClient)
//                .baseUrl(ApplicationConstants.webservice_forcallist)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        return retrofit;
//    }

    public static Retrofit twentyFourBySeven_call() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .addInterceptor(logging)
                .build();

        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(ApplicationConstants.webservice_twentyFourBySeven)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

    public static Retrofit twentyFourBySeven_callNew() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .addInterceptor(logging)
                .build();

        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(ApplicationConstants.webservice_twentyFourBySevenNew)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

    public static Retrofit mmuCamp_call() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .addInterceptor(logging)
                .build();

        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(ApplicationConstants.webservice_mmuCamp)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

    public static Retrofit super_admin_call() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .addInterceptor(logging)
                .build();

        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(ApplicationConstants.webservice_countSuper)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

    public static Retrofit super_admin_call_download_excel() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .addInterceptor(logging)
                .build();

        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(ApplicationConstants.webservice_countSuperForDownload)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }


    public static Retrofit getMediProcClient() {
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .addInterceptor(logging)
                .build();

        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(ApplicationConstants.mediProcsWebservice)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }


}
