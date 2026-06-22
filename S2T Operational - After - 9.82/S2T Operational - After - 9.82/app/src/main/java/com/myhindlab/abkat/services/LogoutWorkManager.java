package com.myhindlab.abkat.services;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.myhindlab.abkat.BuildConfig;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LogoutWorkManager extends Worker {
    private static final String TAG = LogoutWorkManager.class.getName();
    private String userId = "";
    private UserSessionManager userSessionManager;


    public LogoutWorkManager(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
//        Context context = getApplicationContext();

        userSessionManager = new UserSessionManager(context);
        try {
            JSONArray user_info = new JSONArray(userSessionManager.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
//                    name = json.getString("name");
//                    Designation = json.getString("Designation");
//                    DESGID = json.getInt("DESGID");
//                    DISTLGDCODE = json.getString("DISTLGDCODE");
                userId = String.valueOf(json.getInt("EmpCode"));


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public Result doWork() {

        Context context = getApplicationContext();
        try {
            Log.d(TAG, "doWork Called");
//            Utilities.sendNotification(context);
            new LogoutUser(userId).execute(userId, BuildConfig.VERSION_NAME);
            return Result.success();
        } catch (Throwable throwable) {
            Log.d(TAG, "Error Sending Notification" + throwable.getMessage());
            return Result.failure();
        }


    }

    private class LogoutUser extends AsyncTask<String, Void, String> {

//        private ProgressDialog pd;

        String userId;

        public LogoutUser(String userId) {
            this.userId = userId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            pd = new ProgressDialog(context);
//            pd.setMessage("Please wait . . . ");
//            pd.setCancelable(false);
//            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "";
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("Userid", params[0]));
            param.add(new ParamsPojo("MobVersion", params[1]));
            res = WebServiceCall.APICall(ApplicationConstants.GetUserForceLogout, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            pd.dismiss();
            String status = "", message = "";
            Log.d(TAG, "GetUserForceLogout: " + result);
            try {
                if (!result.equals("")) {
                    JSONObject jsonObject = new JSONObject(result);
                    status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("success")) {
                        message = jsonObject.getString("message");
                        JSONArray jsonArray = jsonObject.getJSONArray("output");
                        JSONObject j = jsonArray.getJSONObject(0);
                        int isLogOutFlag = j.getInt("IsLogOutFlag");
                        if (isLogOutFlag == 1) {
                            new UpdateLogoutUser().execute(userId, BuildConfig.VERSION_NAME, "0");
                        }
                    } else {
//                        Utilities.showAlertDialog(context, "Fail", message, false);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    private class UpdateLogoutUser extends AsyncTask<String, Void, String> {

//        private ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            pd = new ProgressDialog(context);
//            pd.setMessage("Please wait . . . ");
//            pd.setCancelable(false);
//            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "";
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("Userid", params[0]));
            param.add(new ParamsPojo("MobVersion", params[1]));
            param.add(new ParamsPojo("IsLogOutFlag", params[2]));
            res = WebServiceCall.APICall(ApplicationConstants.UpdateLogoutUser, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            pd.dismiss();
            Log.d(TAG, "UpdateLogoutUser: " + result);

            String status = "", message = "";
            try {
                if (!result.equals("")) {
                    JSONObject jsonObject = new JSONObject(result);
                    status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("Sucess")) {
//                        message = jsonObject.getString("message");
                        userSessionManager.cleanLoginInfoForceLogout();

//                        System.exit(0);


                    } else {
//                        Utilities.showAlertDialog(context, "Fail", message, false);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }
}
