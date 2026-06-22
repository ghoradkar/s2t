//package com.myhindlab.abkat.services;
//
//import android.content.SharedPreferences;
//import android.util.Log;
//
//import com.google.firebase.messaging.FirebaseMessagingService;
//import com.myhindlab.abkat.utilities.ApplicationConstants;
//import com.myhindlab.abkat.utilities.UserSessionManager;
//
//
///**
// * Created by shahsank on 17-02-2025.
// */
//
//public class MyFirebaseInstanceIDService extends FirebaseMessagingService {
//
//    private static final String TAG = "MyFirebaseIIDService";
//    UserSessionManager session;
//
//    String userid;
//    SharedPreferences pref;
//    SharedPreferences.Editor editor;
//
//    @Override
//    public void onNewToken(String token) {
//        super.onNewToken(token);
//
//        // Initialize UserSessionManager and SharedPreferences
//        session = new UserSessionManager(this);
//        pref = getSharedPreferences(ApplicationConstants.PREFER_NAME, MODE_PRIVATE);
//        editor = pref.edit();
//
//        // Store the correct token
//        editor.putString(ApplicationConstants.Shared_Pref_Notification_Key, token);
//        editor.apply(); // Use apply() instead of commit() for better performance
//
//        // Log the token for debugging
//        Log.d(TAG, "New Firebase Token: " + token);
//    }
//
//
//}
//
//
