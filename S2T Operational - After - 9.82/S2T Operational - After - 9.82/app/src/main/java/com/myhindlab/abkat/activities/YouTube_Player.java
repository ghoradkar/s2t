package com.myhindlab.abkat.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.PlayerStyle;
import com.google.android.youtube.player.YouTubePlayerView;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;

import org.json.JSONArray;
import org.json.JSONObject;

public class YouTube_Player extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
    private ProgressDialog dialog;
    private UserSessionManager session;
    Context context;
    String EmpCode;
    String DESGID = "";
    String VID, rating, materialtrainingid, trainingtype;
    String duration;
    YouTubePlayerView player_view;
    String Videopercentage = "0.0";
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    private static final String DEVELOPER_KEY = "AIzaSyDB-DrjjhTCreQ0dE0lxrW8A8yCSHBDfaY";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_tube__player);

        init();
        setDefaults();

        Bundle extras = getIntent().getExtras();
        String stringVariableName = extras.getString("YoutubeURL");
        materialtrainingid = extras.getString("MaterialTrainingId");
        trainingtype = extras.getString("Trainingtype");
        VID = stringVariableName;

        player_view = (YouTubePlayerView) findViewById(R.id.player_view);

        player_view.initialize(DEVELOPER_KEY, this);
    }

    private void init() {
        context = YouTube_Player.this;
        session = new UserSessionManager(context);
        dialog = new ProgressDialog(context);
    }

    private void setDefaults() {
        try {
            JSONArray user_info = new JSONArray(session.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                EmpCode = json.getString("EmpCode");
                DESGID = json.getString("DESGID").trim();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = String.format(
                    getString(R.string.error_player), errorReason.toString());
            Utilities.showMessageString(errorMessage, this);
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                        YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored) {
            try {
                player.loadVideo(VID);
            } catch (Exception e) {
                e.toString();
            }
            player.setPlayerStyle(PlayerStyle.DEFAULT);
            duration = String.valueOf(player.getCurrentTimeMillis());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            getYouTubePlayerProvider().initialize(DEVELOPER_KEY, this);
        }
    }

    private YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.player_view);
    }

    public void onBackPressed() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.layout_custom_dialog, null);
        final RatingBar rating_bar = (RatingBar) alertLayout.findViewById(R.id.rating_bar);

        androidx.appcompat.app.AlertDialog.Builder alert = new androidx.appcompat.app.AlertDialog.Builder(this);
        alert.setTitle("Please Rate...!");
        alert.setView(alertLayout);
        alert.setCancelable(false);
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                rating = String.valueOf(rating_bar.getRating());
                if (rating.equals("0.0"))
                    Utilities.showMessageString("Please rate " + rating + " stars", context);
                else
                    new InserTrainigVideoHistrory().execute();

            }
        });
        androidx.appcompat.app.AlertDialog dialog = alert.create();
        dialog.show();
    }

    private class InserTrainigVideoHistrory extends AsyncTask<String, Void, String> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(YouTube_Player.this);
            pDialog.setMessage("Please Wait ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "[]";
            try {
                res = WebServiceCall.InserTrainigVideoHistrory(materialtrainingid,
                        EmpCode, trainingtype, Videopercentage, rating);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            pDialog.dismiss();

            if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
                try {
                    JSONObject obj1 = new JSONObject(result);
                    String status = obj1.getString("status");
                    String message = obj1.getString("message");
                    if (status.equalsIgnoreCase("Success")) {
                        Utilities.showMessageString(message, YouTube_Player.this);
                        finish();
                    } else
                        Utilities.showAlertDialog(YouTube_Player.this, status, message, false);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
}
