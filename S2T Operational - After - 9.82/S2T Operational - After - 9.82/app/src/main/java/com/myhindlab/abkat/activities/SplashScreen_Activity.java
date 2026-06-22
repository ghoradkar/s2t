package com.myhindlab.abkat.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

//import com.google.android.play.core.appupdate.AppUpdateInfo;
//import com.google.android.play.core.appupdate.AppUpdateManager;
//import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
//import com.google.android.play.core.install.model.AppUpdateType;
//import com.google.android.play.core.install.model.UpdateAvailability;
//import com.google.android.play.core.tasks.Task;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.DeviceUtils;
import com.myhindlab.abkat.utilities.NetworkProgressDialog;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;
import com.myhindlab.abkat.utilities.network_monitor.ProgressListener;
import com.myhindlab.abkat.utilities.network_monitor.TrafficSpeedMonitor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class SplashScreen_Activity extends Activity {

    private Context context;
    private int secondsDelayed = 1;
    private UserSessionManager session;

    private ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

//        checkUpdate();
//        if (DeviceUtils.isDeviceRooted(getApplicationContext()) || DeviceUtils.isEmulator()) {
//            Utilities.showAlertDialog(context, "Security issue", "You can't use this app on this device.", false, "Okay", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    finishAffinity();
//                }
//            });
//
//        } else {


            init();
            setDefaults();
        }


    private void init() {
        context = SplashScreen_Activity.this;
        session = new UserSessionManager(context);

        pd = new ProgressDialog(context);

        if (session.getForceLogout()) {
            session.cleanLoginInfo();
        }


        Log.d("SessionDebug", "isLoggedIn: " + session.isLoggedIn());

//
//        if (session.isSessionExpired()) {
//            session.logoutUser(); // Clear session data
//
//            // Redirect to login screen
//            Intent intent = new Intent(this, Login_Activity.class);
//            startActivity(intent);
//            finish(); // Close the current activity
//        } else {
//            // If session is still valid, update the session date
//            session.createSession();
//        }



//        if (session.isSessionExpired()) {
//            session.logoutUser();
//        } else {
//            // Only update session if user is actually logged in
//            if (session.isLoggedIn()) {
//                session.createSession();
//            }
//        }




    }

//    private void checkUpdate() {
//        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(getApplicationContext());
//
//        // Returns an intent object that you use to check for an update.
//        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
//
//        // Checks that the platform will allow the specified type of update.
//        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
//            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
//                    // This example applies an immediate update. To apply a flexible update
//                    // instead, pass in AppUpdateType.FLEXIBLE
//                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
//                // Request the update.
//
//                try {
//                    appUpdateManager.startUpdateFlowForResult(
//                            // Pass the intent that is returned by 'getAppUpdateInfo()'.
//                            appUpdateInfo,
//                            // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
//                            AppUpdateType.IMMEDIATE,
//                            // The current activity making the update request.
//                            this,
//                            // Include a request code to later monitor this update request.
//                            1010);
//                } catch (IntentSender.SendIntentException e) {
//                    e.printStackTrace();
//                }
//            } else {
//                Utilities.showToastMessage("Version is updated", context, true);
//
//            }
//        });
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1010) {
            if (resultCode != RESULT_OK) {
                Log.d("Update flow failed!: ", String.valueOf(resultCode));
                // If the update is cancelled or fails,
                // you can request to start the update again.
            }
        }
    }

    private void setDefaults() {
        try {
            PackageInfo pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String versionName = pinfo.versionName.trim();

            if (Utilities.isNetworkAvailable(context)) {
                new APKDownloader().execute(ApplicationConstants.ApplicationID, versionName);
            } else {
                Utilities.showAlertDialog(context, "Alert",
                        "Internet is not connected, Please enable internet connect and try again.",
                        false, "OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
//        new Handler().postDelayed(new Runnable() {
//            public void run() {
//                startActivity(new Intent(context, Login_Activity.class));
//                finish();
//            }
//        }, secondsDelayed * 2500);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class APKDownloader extends AsyncTask<String, String, String> {

        private NetworkProgressDialog netDialog;
        private TrafficSpeedMonitor speedMonitor;
        // track per-request totals if you want
        private volatile long lastRespBytes = 0L;
        private volatile long lastReqBytes = 0L;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // existing pd usage kept if you want, but we'll show netDialog too
            pd.setMessage("Please wait . . . ");
            pd.setCancelable(false);
            pd.show();

//            netDialog = new NetworkProgressDialog((Activity) context);
//            netDialog.setMessage("Contacting server...");
//            netDialog.show();

            // start system-wide speed monitor
            // start system-wide or app-only speed monitor (prefer app-only)
//            speedMonitor = new TrafficSpeedMonitor((sessionTx, sessionRx, upBps, downBps) -> {
//                final String up = humanReadableSpeed(upBps);
//                final String down = humanReadableSpeed(downBps);
//                // sessionTx/sessionRx are baseline-subtracted totals for this session
//                ((Activity) context).runOnUiThread(() -> {
//                    netDialog.updateUploadSpeed(up + ""); // per second
//                    netDialog.updateDownloadSpeed(down + "");
//                    netDialog.updateSentBytes(sessionTx);    // session total sent (B)
//                    netDialog.updateReceivedBytes(sessionRx); // session total received (B)
//                });
//            }, true); // <-- pass true to try per-UID (app-only)
//            speedMonitor.start();

        }

        @Override
        protected String doInBackground(String... params) {
            String res = "";
            ArrayList<ParamsPojo> param = new ArrayList<>();
            param.add(new ParamsPojo("aplicationId", params[0]));
            param.add(new ParamsPojo("versionname", params[1]));

            // progress listener for per-request progress
//            ProgressListener pl = new ProgressListener() {
//                @Override
//                public void onRequestProgress(long bytesWritten, long contentLength) {
//                    lastReqBytes = bytesWritten;
//                    // you can publish progress or update UI via runOnUiThread if needed
//                }
//
//                @Override
//                public void onResponseProgress(long bytesRead, long contentLength) {
//                    lastRespBytes = bytesRead;
//                    // update netDialog from background via runOnUiThread
//                    ((Activity) context).runOnUiThread(() -> {
//                        netDialog.updateReceivedBytes(bytesRead);
//                        // if contentLength > 0 you can show percent
//                        if (contentLength > 0) {
//                            // optional: netDialog.setProgress((int)((bytesRead*100)/contentLength));
//                        }
//                    });
//                }
//            };

            // call the new APICallWithProgress (baseUrl = ApplicationConstants.webservice)

//            res = WebServiceCall.APICallWithProgress(ApplicationConstants.APKDownloader, ApplicationConstants.webservice, param, pl);
            res = WebServiceCall.APICall(ApplicationConstants.APKDownloader, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String res) {
            super.onPostExecute(res);
            // stop monitor + dismiss dialog(s)

//            if (speedMonitor != null) speedMonitor.stop();
//            if (netDialog != null && netDialog.isShowing()) netDialog.dismiss();

            pd.dismiss();

            Log.e("Splash Activity", "APK Downloader Resp: " + res);

            // rest of your existing onPostExecute code unchanged
            if (res != null && res.length() > 0 && !res.equalsIgnoreCase("[]")) {
                try {
                    JSONObject objnew = new JSONObject(res);
                    String status = objnew.getString("status");
                    String msg = objnew.getString("message");

                    if (status.equalsIgnoreCase("Success")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("ABKAT Update");
                        builder.setMessage(Html.fromHtml(msg));
                        builder.setCancelable(false);

                        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                final String appPackageName = context.getPackageName();
                                Intent intent = new Intent(Intent.ACTION_VIEW,
                                        Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName));
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                                context.startActivity(intent);
                                finish();
                            }
                        });

                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                            }
                        });

                        AlertDialog alert = builder.create();
                        alert.show();
                    } else {
                        Utilities.showToastMessage(msg, context, true);
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                if (session.isLoggedIn()) {
                                    if (session.isSessionExpired()) {
                                        session.logoutUser();
                                    } else {
                                        session.createSession();
                                        Intent intent = new Intent(context, SiteSurvey_Menu_Activity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                } else {
                                    Intent intent = new Intent(context, Login_Activity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        }, secondsDelayed * 2500);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Utilities.showToastMessage(R.string.msgt_emptyresponse, context, false);
                new Handler().postDelayed(new Runnable() {
                    public void run() { finish(); }
                }, secondsDelayed * 2500);
            }
        }

        private String humanReadableSpeed(double bytesPerSec) {
            if (bytesPerSec < 1024) return String.format(Locale.getDefault(), "%.0f B/s", bytesPerSec);
            double kb = bytesPerSec / 1024.0;
            if (kb < 1024) return String.format(Locale.getDefault(), "%.2f KB/s", kb);
            return String.format(Locale.getDefault(), "%.2f MB/s", kb / 1024.0);
        }
    }



    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }


    public void showAlertDialogAndExitApp(String message) {

        AlertDialog alertDialog = new AlertDialog.Builder(SplashScreen_Activity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                });

        alertDialog.show();
    }

}
