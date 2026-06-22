package com.myhindlab.abkat.services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import com.myhindlab.abkat.BuildConfig;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.WebServiceCall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class LogoutService extends Service {
    private static final String TAG = LogoutService.class.getSimpleName();

    public LogoutService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        try {


            String userId = intent.getStringExtra("userId");
            Log.d(TAG, "onStartCommand: " + userId);

            TimerTask timerTask = new TimerTask() {

                @Override
                public void run() {
//                System.out.println("TimerTask executing counter is: " + counter);
                    new LogoutUser(userId).execute(userId, BuildConfig.VERSION_NAME);

                }
            };

            Timer timer = new Timer("MyTimer");//create a new Timer

            timer.scheduleAtFixedRate(timerTask, 500, 36000);


        }catch (Exception e){
            e.printStackTrace();
        }
        return START_STICKY;


    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
        return null;
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
                        new UserSessionManager(LogoutService.this).logoutUser(LogoutService.this);

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