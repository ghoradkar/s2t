package com.myhindlab.abkat.utilities;

import android.app.Activity;
import android.os.Build;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.myhindlab.abkat.BuildConfig;
import com.myhindlab.abkat.utilities.network_monitor.ProgressInterceptor;
import com.myhindlab.abkat.utilities.network_monitor.ProgressListener;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class WebServiceCall {


    public static String APICall(String MethodName,  List<ParamsPojo> list) {
        String serverResponse = "[]";
        try {
            HttpUrl.Builder urlBuilder = HttpUrl.parse(ApplicationConstants.webservice + MethodName)
                    .newBuilder();

            Log.i("Params", "APICall: " + new Gson().toJson(list));
            Log.i("URL", "APICall: " + ApplicationConstants.webservice + MethodName);

            for (int i = 0; i < list.size(); i++)
                urlBuilder.addQueryParameter(list.get(i).getParam_Key(),
                        list.get(i).getParam_Value());

            String url = urlBuilder.build().toString();

//            OkHttpClient client = UnsafeOkHttpClient.getUnsafeOkHttpClient().newBuilder()
//                    .connectTimeout(5, TimeUnit.MINUTES)
//                    .writeTimeout(5, TimeUnit.MINUTES)
//                    .readTimeout(5, TimeUnit.MINUTES)
//                    .build();
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(25, TimeUnit.SECONDS)
                    .readTimeout(25, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)   // IMPORTANT

                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();
            serverResponse = response.body().string();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return serverResponse;
    }

//    public static String APICall(String MethodName, List<ParamsPojo> list) {
//        String serverResponse = "[]";
//        try {
//            HttpUrl.Builder urlBuilder = HttpUrl.parse(ApplicationConstants.webservice + MethodName)
//                    .newBuilder();
//
//            for (ParamsPojo param : list) {
//                urlBuilder.addQueryParameter(param.getParam_Key(), param.getParam_Value());
//            }
//
//            String url = urlBuilder.build().toString();
//
//            OkHttpClient client;
//
//            client = UnsafeOkHttpClient.getUnsafeOkHttpClient().newBuilder()
//                    .connectTimeout(5, TimeUnit.MINUTES)
//                    .writeTimeout(5, TimeUnit.MINUTES)
//                    .readTimeout(5, TimeUnit.MINUTES)
//                    .build();
//
//            Request request = new Request.Builder()
//                    .url(url)
//                    .build();
//
//            Response response = client.newCall(request).execute();
//            serverResponse = response.body().string();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return serverResponse;
//    }

    public static String APICall(String MethodName, @Nullable String baseUrl, List<ParamsPojo> list) {
        String serverResponse = "[]";
        try {

            HttpUrl.Builder urlBuilder = HttpUrl.parse(baseUrl + MethodName)
                    .newBuilder();


            Log.i("Params", "APICall: " + new Gson().toJson(list));
            Log.i("URL", "APICall: " + baseUrl + MethodName);

            for (int i = 0; i < list.size(); i++)
                urlBuilder.addQueryParameter(list.get(i).getParam_Key(),
                        list.get(i).getParam_Value());

            String url = urlBuilder.build().toString();

//            OkHttpClient client = UnsafeOkHttpClient.getUnsafeOkHttpClient().newBuilder()
//                    .connectTimeout(5, TimeUnit.MINUTES)
//                    .writeTimeout(5, TimeUnit.MINUTES)
//                    .readTimeout(5, TimeUnit.MINUTES)
//                    .build();
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(25, TimeUnit.SECONDS)
                    .readTimeout(25, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)   // IMPORTANT

//                    .socketFactory(null).connectTimeout(1000,TimeUnit.MINUTES)

                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();
            serverResponse = response.body().string();
            Log.i("API_RESPONSE", "Response: " + serverResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serverResponse;
    }


    public static String APICall(String MethodName, @Nullable String baseUrl, List<ParamsPojo> list, Activity activity) {
        String serverResponse = "[]";
        try {

            HttpUrl.Builder urlBuilder = HttpUrl.parse(baseUrl + MethodName)
                    .newBuilder();


            Log.i("Params", "APICall: " + new Gson().toJson(list));
            Log.i("URL", "APICall: " + baseUrl + MethodName);

            for (int i = 0; i < list.size(); i++)
                urlBuilder.addQueryParameter(list.get(i).getParam_Key(),
                        list.get(i).getParam_Value());

            String url = urlBuilder.build().toString();

//            OkHttpClient client = UnsafeOkHttpClient.getUnsafeOkHttpClient().newBuilder()
//                    .connectTimeout(5, TimeUnit.MINUTES)
//                    .writeTimeout(5, TimeUnit.MINUTES)
//                    .readTimeout(5, TimeUnit.MINUTES)
//                    .build();
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(25, TimeUnit.SECONDS)
                    .readTimeout(25, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)   // IMPORTANT

//                    .socketFactory(null).connectTimeout(1000,TimeUnit.MINUTES)

                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();
            serverResponse = response.body().string();
            Log.i("API_RESPONSE", "Response: " + serverResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serverResponse;
    }



    public static String APICallWithProgress(String MethodName, @Nullable String baseUrl, List<ParamsPojo> list, ProgressListener progressListener) {
        String serverResponse = "[]";
        try {
            String realBase = (baseUrl == null) ? ApplicationConstants.webservice : baseUrl;
            HttpUrl.Builder urlBuilder = HttpUrl.parse(realBase + MethodName).newBuilder();

            for (ParamsPojo p : list) urlBuilder.addQueryParameter(p.getParam_Key(), p.getParam_Value());

            String url = urlBuilder.build().toString();
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(25, TimeUnit.SECONDS)
                    .readTimeout(25, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .addNetworkInterceptor(new ProgressInterceptor(progressListener))
                    .build();

            Request request = new Request.Builder().url(url).build();

            Response response = client.newCall(request).execute();
            ResponseBody body = response.body();
            if (body != null) {
                serverResponse = body.string(); // Note: reading .string() will consume the body and final read progress will be fired by CountingResponseBody
            } else serverResponse = "[]";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serverResponse;
    }


//    public static String APICall(String MethodName, @Nullable String baseUrl, List<ParamsPojo> list) {
//        String serverResponse = "[]";
//        try {
//            // 1. Validate baseUrl
//            if (baseUrl == null || baseUrl.isEmpty()) {
//                throw new IllegalArgumentException("Base URL cannot be null or empty.");
//            }
//
//            // 2. Build the URL with query parameters
//            HttpUrl.Builder urlBuilder = HttpUrl.parse(baseUrl + MethodName).newBuilder();
//            for (ParamsPojo param : list) {
//                urlBuilder.addQueryParameter(param.getParam_Key(), param.getParam_Value());
//            }
//            String url = urlBuilder.build().toString();
//
//            OkHttpClient client;
//
//                client = UnsafeOkHttpClient.getUnsafeOkHttpClient().newBuilder()
//                        .connectTimeout(5, TimeUnit.MINUTES)
//                        .writeTimeout(5, TimeUnit.MINUTES)
//                        .readTimeout(5, TimeUnit.MINUTES)
//                        .build();
//
//
//            // 4. Create request and execute
//            Request request = new Request.Builder()
//                    .url(url)
//                    .build();
//
//            Response response = client.newCall(request).execute();
//            serverResponse = response.body().string();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return serverResponse;
//    }


    public static String APICallHll(String MethodName, List<ParamsPojo> list) {
        String serverResponse = "[]";
        try {
            HttpUrl.Builder urlBuilder = HttpUrl.parse(BuildConfig.hllBaseURl + MethodName)
                    .newBuilder();

            for (int i = 0; i < list.size(); i++)
                urlBuilder.addQueryParameter(list.get(i).getParam_Key(),
                        list.get(i).getParam_Value());

            String url = urlBuilder.build().toString();

//            OkHttpClient client = UnsafeOkHttpClient.getUnsafeOkHttpClient().newBuilder()
//                    .connectTimeout(5, TimeUnit.MINUTES)
//                    .writeTimeout(5, TimeUnit.MINUTES)
//                    .readTimeout(5, TimeUnit.MINUTES)
//                    .build();
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(25, TimeUnit.SECONDS)
                    .readTimeout(25, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)   // IMPORTANT

                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();
            serverResponse = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serverResponse;
    }



//    public static String APICallHll(String MethodName, List<ParamsPojo> list) {
//        String serverResponse = "[]";
//        try {
//            HttpUrl.Builder urlBuilder = HttpUrl.parse(BuildConfig.hllBaseURl + MethodName)
//                    .newBuilder();
//
//            for (ParamsPojo param : list) {
//                urlBuilder.addQueryParameter(param.getParam_Key(), param.getParam_Value());
//            }
//
//            String url = urlBuilder.build().toString();
//
//            OkHttpClient client;
//
//
//    client = UnsafeOkHttpClient.getUnsafeOkHttpClient().newBuilder()
//                        .connectTimeout(5, TimeUnit.MINUTES)
//                        .writeTimeout(5, TimeUnit.MINUTES)
//                        .readTimeout(5, TimeUnit.MINUTES)
//                        .build();
//
//            Request request = new Request.Builder()
//                    .url(url)
//                    .build();
//
//            Response response = client.newCall(request).execute();
//            serverResponse = response.body().string();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return serverResponse;
//    }


    public static String MahabocwAPICall(String apiUrl) {
        String serverResponse = "[]";
        try {
            HttpUrl.Builder urlBuilder = HttpUrl.parse(apiUrl)
                    .newBuilder();

            String url = urlBuilder.build().toString();
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(5, TimeUnit.MINUTES)
                    .writeTimeout(5, TimeUnit.MINUTES)
                    .readTimeout(5, TimeUnit.MINUTES)
                    .build();
            Request request = new Request.Builder()
//                    .addHeader("x-auth", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.e30.8v_JCIO6_4Mg1LpMDxj9yUUhS2_ipjVy3MQst90Dz4o")
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();
            serverResponse = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serverResponse;
    }


//    public static String MahabocwAPICall(String apiUrl) {
//        String serverResponse = "[]";
//        try {
//            HttpUrl.Builder urlBuilder = HttpUrl.parse(apiUrl).newBuilder();
//            String url = urlBuilder.build().toString();
//
//            OkHttpClient client;
//
//
//            client = UnsafeOkHttpClient.getUnsafeOkHttpClient().newBuilder()
//                    .connectTimeout(5, TimeUnit.MINUTES)
//                    .writeTimeout(5, TimeUnit.MINUTES)
//                    .readTimeout(5, TimeUnit.MINUTES)
//                    .build();
//
//            Request request = new Request.Builder()
//                    .url(url)
//                    .build();
//
//            Response response = client.newCall(request).execute();
//            serverResponse = response.body().string();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return serverResponse;
//    }

    public static String GetMenuDashboard() {
        String serverResponse = "[]";
        try {
            RequestBody formBody = new FormBody.Builder()
                    .build();

            String url = ApplicationConstants.webservice + ApplicationConstants.GetMenuDashboard;

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(5, TimeUnit.MINUTES)
                    .writeTimeout(5, TimeUnit.MINUTES)
                    .readTimeout(5, TimeUnit.MINUTES)
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();

            Response response = client.newCall(request).execute();
            serverResponse = response.body().string();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return serverResponse;
    }

    public static String SelectVidieoLinkData(String ttp, String did, String sid) {
        String serverResponse = "[]";
        try {
            RequestBody formBody = new FormBody.Builder()
                    .add("Trainingtype", ttp)
                    .add("DesgId", did)
                    .add("ServicesTypeID", sid)
                    .build();

            String url = ApplicationConstants.webservice + ApplicationConstants.SelectVidieoLinkData;

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(5, TimeUnit.MINUTES)
                    .writeTimeout(5, TimeUnit.MINUTES)
                    .readTimeout(5, TimeUnit.MINUTES)
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();

            Response response = client.newCall(request).execute();
            serverResponse = response.body().string();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return serverResponse;
    }

    public static String InserTrainigVideoHistrory(String materialtrainingid, String UserID,
                                                   String trainingtype, String Videopercentage,
                                                   String rating) {
        String serverResponse = "[]";
        try {
            RequestBody formBody = new FormBody.Builder()
                    .add("MaterialTrainingId", materialtrainingid)
                    .add("UserID", UserID)
                    .add("Trainingtype", trainingtype)
                    .add("vidieopercentage", Videopercentage)
                    .add("RateFrmUser", rating)
                    .build();

            String url = ApplicationConstants.webservice + ApplicationConstants.InserTrainigVideoHistrory;

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(5, TimeUnit.MINUTES)
                    .writeTimeout(5, TimeUnit.MINUTES)
                    .readTimeout(5, TimeUnit.MINUTES)
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();

            Response response = client.newCall(request).execute();
            serverResponse = response.body().string();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return serverResponse;
    }

    public static String jsonApiCall(String urlString, String jsonAnsString) {
        String serverResponse = "";
        try {

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(5, TimeUnit.MINUTES)
                    .writeTimeout(5, TimeUnit.MINUTES)
                    .readTimeout(5, TimeUnit.MINUTES)
                    .build();

            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, jsonAnsString);
            Request request = new Request.Builder()
                    .addHeader("x-auth", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.e30.8v_JCIO6_4Mg1LpMDxj9yUUhS2_ipjVy3MQst90Dz4o")
                    .addHeader("content-type", "application/json")
                    .url(urlString)
                    .post(body)
                    .build();

            Response response = client.newCall(request).execute();
            serverResponse = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serverResponse;
    }


//    ----------------------------------Courier Module ----------------------------------------------

    public static String HLLAPICall(String MathodName, List<ParamsPojo> list) {
        String serverResponse = "[]";
        try {
            HttpUrl.Builder urlBuilder = HttpUrl.parse(ApplicationConstants.webservice + MathodName)
                    .newBuilder();

            for (int i = 0; i < list.size(); i++)
                urlBuilder.addQueryParameter(list.get(i).getParam_Key(),
                        list.get(i).getParam_Value());

            String url = urlBuilder.build().toString();

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();
            serverResponse = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serverResponse;
    }



}