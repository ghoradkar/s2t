package com.myhindlab.abkat.services;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.rest.ApiClient;
import com.myhindlab.abkat.rest.ApiInterface;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

//    private Context context;

    UserSessionManager session;

    private static final String CHANNEL_ID = "S2T_Channel"; // Custom Channel ID

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.i(TAG, "onMessageReceived: "+remoteMessage.getNotification().getTitle());

        if (remoteMessage.getNotification() != null) {
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
            sendNotification(title, body);
        }
    }

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);

        session = new UserSessionManager(this);


        // Save the new token in SharedPreferences
        session.storeFCMToken(token);
        Log.d(TAG, "New Firebase Token: " + token);

//        insertAndroidToken(token);

    }



    void insertAndroidToken(String token) {
//        List<ParamsPojo> param = new ArrayList<ParamsPojo>();


        ApiInterface apiInterface = ApiClient.getD2DClient().create(ApiInterface.class);


        apiInterface.insertSaveToken(session.getUserDetailsJson().getEmpCode(),token,"1").enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String result = response.body().string();
                        Log.i("TAG", "onPostExecute: " + result);

                        if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
                            JSONObject obj = new JSONObject(result);
                            String status = obj.getString("status");
                            String message = obj.getString("message");
                            if (status.equalsIgnoreCase("Success")) {
//
                                AlertDialog.Builder builder = new AlertDialog.Builder(MyFirebaseMessagingService.this);
                                builder.setIcon(R.drawable.icon_success);
                                builder.setTitle("Success");
                                builder.setCancelable(false);
                                builder.setMessage(message);
                                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                                builder.show();

                            } else {
                                Utilities.showAlertDialog(MyFirebaseMessagingService.this, status, message, false);
                            }
                        } else
                            Utilities.showAlertDialog(MyFirebaseMessagingService.this, "Please try again", "Server not responding.", false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    try {
                        Utilities.showAlertDialog(MyFirebaseMessagingService.this, "Failure", response.errorBody().string(), false);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Utilities.showAlertDialog(MyFirebaseMessagingService.this, "Failure", e.getMessage(), false);
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Utilities.showAlertDialog(MyFirebaseMessagingService.this, "Failure", t.getMessage(), false);

            }
        });
    }

    private void sendNotification(String title, String body) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "FCM Notifications",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Firebase Cloud Messaging Notifications");
            notificationManager.createNotificationChannel(channel);

        }




        // Build and show the notification
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.abkat_notification)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.abkat_notification))
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        notificationManager.notify(0, notificationBuilder.build());
    }
}
