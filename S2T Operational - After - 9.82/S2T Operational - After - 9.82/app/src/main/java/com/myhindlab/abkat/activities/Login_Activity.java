package com.myhindlab.abkat.activities;

import static com.myhindlab.abkat.utilities.PermissionUtil.PERMISSION_ALL;
import static com.myhindlab.abkat.utilities.Utilities.hideSoftKeyboard;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.myhindlab.abkat.BuildConfig;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.UserLoginApp_Outputpojo;
import com.myhindlab.abkat.rest.ApiClient;
import com.myhindlab.abkat.rest.ApiInterface;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.DeviceIdUtil;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class Login_Activity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private UserSessionManager session;
    private TextInputEditText edt_username;

    private AlertDialog alertDialog;

    String tokenIDString;
    SharedPreferences pref;

    private boolean isResponded = false;
    private boolean isDialogOpen = false;
    private TextInputEditText edt_password;
    private TextView tv_forgotpassword, tv_register, tv_versionname;
    private Button btn_login;
    private String mobileNo, Empcode, otpnumber, versionNo;

    private int DESGID;
    private ProgressDialog pd;
    private RadioGroup rgLogin;
    private RadioButton rbAbkat, rbDoorToDoor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = Login_Activity.this;
        session = new UserSessionManager(context);


        init();
        setDefaults();
        setEventHandler();
        checkPermissions();


    }

    private void init() {

        pd = new ProgressDialog(context);
//
        edt_username = findViewById(R.id.edt_username);
        edt_password = findViewById(R.id.edt_password);
        tv_forgotpassword = findViewById(R.id.tv_forgotpassword);
        tv_register = findViewById(R.id.tv_register);
        rgLogin = findViewById(R.id.rgLogin);
        rbAbkat = findViewById(R.id.rbAbkat);
        rbDoorToDoor = findViewById(R.id.rbDoorToDoor);

        tv_versionname = findViewById(R.id.tv_versionname);

        btn_login = findViewById(R.id.btn_login);

        String imei = DeviceIdUtil.getDeviceId(context);

        tokenIDString = imei;

        Log.d("tokenIDString", tokenIDString);

    }


    private void forgotPassPromt(String mno, String otp) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.prompot_forgot_password, null);
        alertBuilder.setView(alertLayout);
        EditText tv_userName = alertLayout.findViewById(R.id.tv_userName);
        EditText tv_password = alertLayout.findViewById(R.id.tv_password);
        EditText tv_otp = alertLayout.findViewById(R.id.tv_otp);
        EditText tv_re_enter_pass = alertLayout.findViewById(R.id.tv_reEnterPassword);
        Button verifyOtpBtn = alertLayout.findViewById(R.id.verifyOtpBtn);
        Button resendOtpBtn = alertLayout.findViewById(R.id.resendOtpBtn);

        alertBuilder.setCancelable(false);
        alertBuilder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertBuilder.setPositiveButton("ok", (dialog, which) -> finish());
            }
        });


        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();


        resendOtpBtn.setOnClickListener(view -> {


        });

        verifyOtpBtn.setOnClickListener(view -> {


            if (!tv_re_enter_pass.getText().toString().matches(tv_password.getText().toString())) {
                Utilities.showAlertDialog(context, "Alert", "Password Not Matched.", false);
                return;
            }

            if (tv_password.getText().toString().trim().matches("")) {
                Utilities.showAlertDialog(context, "Alert", "Please enter password", false);
                return;
            }

            if (tv_password.getText().toString().length() < 8) {
                Utilities.showAlertDialog(context, "Alert", "Password should be minimum 8 digit", false);
                return;
            }
            if (tv_re_enter_pass.getText().toString().length() < 8) {
                Utilities.showAlertDialog(context, "Alert", "Password should be minimum 8 digit", false);
                return;
            }

            String password = tv_re_enter_pass.getText().toString().trim();

            String passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

            if (!password.matches(passwordPattern)) {
                tv_re_enter_pass.setError("Password must contain :\n" +
                        "Minimum 8 characters including letter,number & special character.");
                return;
            }


            if (tv_otp.getText().toString().trim().matches("")) {
                Utilities.showAlertDialog(context, "Alert", "Please enter otp", false);
                return;
            }

//            if (!tv_otp.getText().toString().trim().equals(otpnumber)) {
//                tv_otp.setError("Entered OTP is not matched");
//                return;
//            }


            inserForgotPass(tv_otp.getText().toString(), mno, tv_re_enter_pass.getText().toString(), alertDialog);


        });

    }


    private void verifyOtp(String mno, String otp) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.prompot_verify_otp, null);
        alertBuilder.setView(alertLayout);
        TextInputEditText edt_Otp = alertLayout.findViewById(R.id.edt_Otp);
        Button verifyOtpBtn = alertLayout.findViewById(R.id.verifyOtpBtn);
        Button resendOtpBtn = alertLayout.findViewById(R.id.resendOtpBtn);
        TextView tv_timer = alertLayout.findViewById(R.id.tv_timer); // <- New Timer TextView

        resendOtpBtn.setVisibility(View.GONE); // Hide resend initially
        alertBuilder.setCancelable(false);
        alertBuilder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertBuilder.setPositiveButton("ok", (dialog, which) -> finish());
            }
        });


        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();

        startResendTimer(resendOtpBtn, tv_timer); // Updated to pass TextView

        verifyOtpBtn.setOnClickListener(view -> {
            //verify api call

            if (edt_Otp.getText().toString().trim().matches("")) {
                edt_Otp.setError("Please enter otp");
                return;
            }
//            if (edt_Otp.getText().toString().trim().length() != 10) {
//                edt_Otp.setError("Please enter valid otp");
//                return;
//            }gET

//            if (!edt_Otp.getText().toString().equalsIgnoreCase(otpnumber)) {
//                edt_Otp.setError("Please enter valid otp");
//                return;
//
//            }

            new VerifyOtp(alertDialog).execute(mobileNo, edt_Otp.getText().toString().trim());


        });

        resendOtpBtn.setOnClickListener(view -> {
            //verify api call


            new GetOtp(1).execute(mobileNo, Empcode);

            resendOtpBtn.setVisibility(View.GONE);
            startResendTimer(resendOtpBtn, tv_timer);

        });
    }


    private void startResendTimer(Button resendOtpBtn, TextView tv_timer) {
        tv_timer.setVisibility(View.VISIBLE); // Show timer
        new CountDownTimer(120 * 1000, 1000) { // 120 seconds
            public void onTick(long millisUntilFinished) {
                int secondsRemaining = (int) (millisUntilFinished / 1000);
                tv_timer.setText("Resend OTP in : " + String.valueOf(secondsRemaining) + " sec");
                btn_login.setEnabled(false);
            }

            public void onFinish() {
                tv_timer.setVisibility(View.GONE);
                btn_login.setEnabled(true);
                resendOtpBtn.setVisibility(View.VISIBLE);
            }
        }.start();
    }


    private void setDefaults() {
        try {
            PackageInfo pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            int versionNumber = pinfo.versionCode;
            String versionName = pinfo.versionName;
            versionNo = versionName;
            tv_versionname.setText("Version : " + versionName);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        edt_username.setText("9845616415");
//        edt_password.setText("123456");
    }

    private void setEventHandler() {
        tv_forgotpassword.setOnClickListener(this);
        tv_register.setOnClickListener(this);
        btn_login.setOnClickListener(this);

        rgLogin.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                edt_password.setText("");
                edt_username.setText("");
            }
        });

        tv_forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.forgot_otp, null);
                alertBuilder.setView(alertLayout);
                TextInputEditText edt_Otp = alertLayout.findViewById(R.id.edt_Otp);
                Button verifyOtpBtn = alertLayout.findViewById(R.id.verifyOtpBtn);

                alertBuilder.setCancelable(false);
                alertBuilder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertBuilder.setPositiveButton("ok", (dialog, which) -> finish());
                    }
                });


                AlertDialog alertDialog = alertBuilder.create();
                alertDialog.show();

                verifyOtpBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (edt_Otp.getText().toString().trim().isEmpty()) {
                            edt_Otp.setError("Please enter mobile number");
                            return;

                        }
                        if (edt_Otp.getText().toString().trim().length() < 10) {
                            edt_Otp.setError("Please enter 10 digit mobile number");
                            return;
                        }

                        if (!Utilities.isMobileNo(edt_Otp.getText().toString().trim())) {
                            edt_Otp.setError("Please enter valid mobile number");
                            return;
                        }


                        getOTPForForgotPass(edt_Otp.getText().toString().trim(), alertDialog);

                    }
                });


            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:

                checkPermissions();


                if (edt_username.getText().toString().isEmpty()) {
                    edt_username.setError("Please enter username");
                    return;
                }

                if (edt_password.getText().toString().isEmpty()) {
                    edt_password.setError("Please enter password");
                    return;
                }


                if (Utilities.isNetworkAvailable(context)) {
                    if (rbAbkat.isChecked()) {
                        new UserLoginApp().execute(edt_username.getText().toString().trim(), edt_password.getText().toString().trim());
                    } else {
                        new HllUserLoginApp().execute(edt_username.getText().toString().trim(), edt_password.getText().toString().trim());
                    }

                } else {
                    Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                }
                break;

            case R.id.tv_register:

                startActivity(new Intent(context, DoctorRegistration_Activity.class));

        }
    }

//    private void showForgotPasswordDialog() {
//        final MaterialEditText edt_entermobile = new MaterialEditText(context);
//        edt_entermobile.setMaxCharacters(10);
//        setEditTextMaxLength(edt_entermobile, 10);
//        edt_entermobile.setInputType(InputType.TYPE_CLASS_NUMBER);
//        edt_entermobile.setHighlightColor(getResources().getColor(R.color.colorPrimary));
//        float dpi = context.getResources().getDisplayMetrics().density;
//
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
//        alertDialogBuilder.setCancelable(false);
//        alertDialogBuilder.setTitle("Enter Registered Mobile");
//
//        alertDialogBuilder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                if (!Utilities.isMobileNo(edt_entermobile)) {
//                    Utilities.showMessageString("Please Enter Valid Mobile Number", context);
//                    showForgotPasswordDialog();
//                    return;
//                }
//
//                mobileNo = edt_entermobile.getText().toString().trim();
//                if (Utilities.isNetworkAvailable(context)) {
//                    new SendOTP().execute(mobileNo);
//                } else {
//                    Utilities.showMessage(R.string.msgt_nointernetconnection, context);
//                }
//            }
//        });
//
//        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//
//        final AlertDialog alertD = alertDialogBuilder.create();
//        alertD.setView(edt_entermobile, (int) (19 * dpi), (int) (5 * dpi), (int) (14 * dpi), (int) (5 * dpi));
//        alertD.show();
//        alertD.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
//        edt_entermobile.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (s.length() == 10) {
//                    if (!Utilities.isMobileNo(edt_entermobile)) {
//                        alertD.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
//                    } else {
//                        alertD.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
//                    }
//                } else {
//                    alertD.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
//                }
//            }
//        });
//    }
//
//    public void setEditTextMaxLength(final EditText editText, int length) {
//        InputFilter[] FilterArray = new InputFilter[1];
//        FilterArray[0] = new InputFilter.LengthFilter(length);
//        editText.setFilters(FilterArray);
//    }
//
//    public class SendOTP extends AsyncTask<String, String, String> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pd.setMessage("Please wait ...");
//            pd.setCancelable(false);
//            pd.show();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            String res = "";
//            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
//            param.add(new ParamsPojo("MobileNo", params[0]));
//            res = WebServiceCall.APICall(ApplicationConstants.ForgotPassword, param);
//            return res;
//        }
//
//        @Override
//        protected void onPostExecute(String res) {
//            super.onPostExecute(res);
//            pd.dismiss();
//
//            if (res != null && res.length() > 0 && !res.equalsIgnoreCase("[]")) {
//                try {
//                    JSONObject obj = new JSONObject(res);
//                    String status = obj.getString("status");
//                    String msg = obj.getString("message");
//                    if (status.equalsIgnoreCase("Success")) {
//                        Utilities.showMessageString(msg, context);
//                        resetPassword();
//                    } else
//                        Utilities.showAlertDialog(context, status, msg, false);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            } else
//                Utilities.showAlertDialog(context, "Please try again", "Server not responding.", false);
//        }
//    }
//
//    private void resetPassword() {
//        LayoutInflater layoutInflater = LayoutInflater.from(cfontext);
//        View promptView = layoutInflater.inflate(R.layout.dialog_resetpassword, null);
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
//        alertDialogBuilder.setView(promptView);
//        alertDialogBuilder.setTitle("Reset Password");
//
//        final EditText edt_otp = promptView.findViewById(R.id.edt_otp);
//        final EditText edt_password = promptView.findViewById(R.id.edt_password);
//        final EditText edt_confirmpassword = promptView.findViewById(R.id.edt_confirmpassword);
//
//        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                if (!edt_password.getText().toString().trim().equals(edt_confirmpassword.getText().toString().trim())) {
//                    Utilities.showMessageString("Passwords Did Not Match", context);
//                    resetPassword();
//                    return;
//                }
//                if (Utilities.isNetworkAvailable(context)) {
//                    new ResetPassword().execute(mobileNo, edt_otp.getText().toString(), edt_confirmpassword.getText().toString());
//                } else {
//                    Utilities.showMessage(R.string.msgt_nointernetconnection, context);
//                }
//            }
//        });
//
//        alertDialogBuilder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//            }
//        });
//
//        alertDialogBuilder.setNeutralButton("resend OTP", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                if (Utilities.isNetworkAvailable(context)) {
//                    new SendOTP().execute(mobileNo);
//                } else {
//                    Utilities.showMessage(R.string.msgt_nointernetconnection, context);
//                }
//            }
//        });
//
//
//        final AlertDialog alertD = alertDialogBuilder.create();
//        alertD.show();
//        alertD.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
//
//        edt_otp.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (edt_otp.getText().toString().isEmpty() || edt_password.getText().toString().isEmpty() || edt_confirmpassword.getText().toString().isEmpty()) {
//                    alertD.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
//                } else {
//                    alertD.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
//                }
//            }
//        });
//
//        edt_password.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (edt_otp.getText().toString().isEmpty() || edt_password.getText().toString().isEmpty() || edt_confirmpassword.getText().toString().isEmpty()) {
//                    alertD.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
//                } else {
//                    alertD.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
//                }
//            }
//        });
//
//        edt_confirmpassword.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (edt_otp.getText().toString().isEmpty() || edt_password.getText().toString().isEmpty() || edt_confirmpassword.getText().toString().isEmpty()) {
//                    alertD.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
//                } else {
//                    alertD.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
//                }
//            }
//        });
//    }
//


//    public class ResetPassword extends AsyncTask<String, String, String> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pd.setMessage("Please wait ...");
//            pd.setCancelable(false);
//            pd.show();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            String res = "";
//            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
//            param.add(new ParamsPojo("MobileNo", params[0]));
//            param.add(new ParamsPojo("Otp", params[1]));
//            param.add(new ParamsPojo("Password", params[2]));
//
//            res = WebServiceCall.APICall(ApplicationConstants.ResetPassword, param);
//            return res;
//        }
//
//        @Override
//        protected void onPostExecute(String res) {
//            super.onPostExecute(res);
//            pd.dismiss();
//
//            if (res != null && res.length() > 0 && !res.equalsIgnoreCase("[]")) {
//                try {
//                    JSONObject obj1 = new JSONObject(res);
//
//                    String status = obj1.getString("status");
//                    String msg = obj1.getString("message");
//                    if (status.equalsIgnoreCase("Success"))
//                        Utilities.showAlertDialog(context, status, msg, true);
//                    else
//                        Utilities.showAlertDialog(context, status, msg, false);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            } else
//                Utilities.showAlertDialog(context, "Please try again",
//                        "Server not responding.", false);
//        }
//
//    }

    public class UserLoginApp extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Please wait . . . ");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "";
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("UserName", params[0]));
            param.add(new ParamsPojo("Password", params[1]));

            res = WebServiceCall.APICall(ApplicationConstants.UserLoginApp, ApplicationConstants.webservice, param);
            return res;
        }



        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();

            if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
                try {
                    JSONObject obj1 = new JSONObject(result);
                    String status = obj1.getString("status");
                    String message = obj1.getString("message");
                    if (status.equalsIgnoreCase("Success")) {
                        JSONArray jsonarr = obj1.getJSONArray("output");
                        if (jsonarr.length() > 0) {
                            for (int i = 0; i < jsonarr.length(); i++) {
                                session.createUserLoginSession(jsonarr.toString());
                            }

                            try {
                                JSONArray user_info = new JSONArray(session.getUserDetails().get(
                                        ApplicationConstants.KEY_LOGIN_INFO));
                                for (int j = 0; j < user_info.length(); j++) {
                                    JSONObject json = user_info.getJSONObject(j);
                                    mobileNo = json.getString("BMobile");
                                    Empcode = json.getString("EmpCode");
                                    DESGID = Integer.parseInt(json.getString("DESGID"));

//                                    mobileNo = "7057394055";
//                                    mobileNo = "9764568835";
//                                    mobileNo = "8668258532";

//
//                                    String token=session.getFCMToken();
//                                    insertAndroidToken(token);
//
                                    
                                    
                                    


                                    if (BuildConfig.isBeta) {

//                                        new GetOtp(1).execute(mobileNo, Empcode);

                                        session.createSession();
                                        startActivity(new Intent(context, SiteSurvey_Menu_Activity.class));

                                    } else {


                                        if (DESGID == 51){
                                            session.createSession();
                                            startActivity(new Intent(context, SiteSurvey_Menu_Activity.class));
                                        }else {
                                            new GetOtp(1).execute(mobileNo, Empcode);
                                        }


//                                        session.createSession();
//                                        startActivity(new Intent(context, SiteSurvey_Menu_Activity.class));

                                    }

//                                    new GetOtp(1).execute(mobileNo, Empcode);

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
//                            startActivity(new Intent(context, MainDrawer_Activity.class));
//                            finish();
                        } else {
                            Utilities.showAlertDialog(context, "Alert", "Empty response from server.", false);
                        }
                    } else {
                        Utilities.showAlertDialog(context, status, message, false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
                }
            } else {
                Utilities.showToastMessage(R.string.msgt_emptyresponse, context, false);
            }
        }
    }

    public class HllUserLoginApp extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Please wait . . . ");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "";
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("UserEmail", params[0]));
            param.add(new ParamsPojo("Password", params[1]));

            res = WebServiceCall.APICallHll(ApplicationConstants.UserLoginApp, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();

            if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
                try {
                    JSONObject obj1 = new JSONObject(result);
                    String status = obj1.getString("status");
                    String message = obj1.getString("message");

                    if (status.equalsIgnoreCase("Success")) {
                        JSONArray jsonarr = obj1.getJSONArray("output");
                        if (jsonarr.length() > 0) {
                            for (int i = 0; i < jsonarr.length(); i++) {
                                session.createHllUserLoginSession(jsonarr.toString());
                            }

                            try {
                                JSONArray user_info = new JSONArray(session.getUserDetails().get(
                                        ApplicationConstants.KEY_LOGIN_INFO));
                                for (int j = 0; j < user_info.length(); j++) {
                                    JSONObject json = user_info.getJSONObject(j);
                                    mobileNo = json.getString("BMobile");
                                    Empcode = json.getString("EmpCode");
                                    String DESGID = json.getString("DESGID");

//                                    new CheckAndroidID().execute(Empcode, Utilities.getDeviceId(context));


//                                    mobileNo = "8668258532";


//                                    new GetOtp(1).execute(mobileNo, Empcode);


//                                    if (DESGID.equalsIgnoreCase("34")) {
//                                        // --- District Coordinator
//                                        startActivity(new Intent(context, MainDrawer_Activity.class));
//
//                                    } else {
                                    // --- Surveyor, Camp Admin
//                                    startActivity(new Intent(context, SiteSurvey_Menu_Activity.class));
////                                    }
//                                    finish();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
//                            startActivity(new Intent(context, MainDrawer_Activity.class));
//                            finish();
                        } else {
                            Utilities.showAlertDialog(context, "Alert", "Empty response from server.", false);
                        }
                    } else {
                        Utilities.showAlertDialog(context, status, message, false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
                }
            } else {
                Utilities.showToastMessage(R.string.msgt_emptyresponse, context, false);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideSoftKeyboard(Login_Activity.this);
    }


    private void checkPermissions() {
//        if (!PermissionUtil.askPermissions(this)) {
//        }
        String[] perms = new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE,};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            perms = new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.POST_NOTIFICATIONS};
        }
        for (String permission : perms) {
            if (ContextCompat.checkSelfPermission(context, permission) !=
                    PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        requestPermissions(
                                new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.BLUETOOTH_SCAN,Manifest.permission.POST_NOTIFICATIONS},
                                PERMISSION_ALL);
                    } else {
                        requestPermissions(
                                new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE},
                                PERMISSION_ALL);

                    }
                }

            }
        }

    }


//    private void checkPermissions() {
////        if (!PermissionUtil.askPermissions(this)) {
////        }
////        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
////            for (String permission : PermissionUtil.getPermissions(context)) {
////                if (ContextCompat.checkSelfPermission(context, permission) !=
////                        PackageManager.PERMISSION_GRANTED) {
////                    requestPermissions(
////                            PermissionUtil.getPermissions(context),
////                            PERMISSION_ALL);
////
////                }
////            }
////        } else {
//
//
//        for (String permission : new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.CALL_PHONE, Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.READ_MEDIA_IMAGES,Manifest.permission.POST_NOTIFICATIONS}) {
//            if (ContextCompat.checkSelfPermission(context, permission) !=
//                    PackageManager.PERMISSION_GRANTED) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    requestPermissions(
//                            new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.CALL_PHONE, Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.READ_MEDIA_IMAGES,Manifest.permission.POST_NOTIFICATIONS},
//                            PermissionUtil.PERMISSION_ALL);
//                }
//            }
//        }
//
////        }
//
//    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_ALL: {

                if (grantResults.length > 0) {

                    List<Integer> indexesOfPermissionsNeededToShow = new ArrayList<>();

                    for (int i = 0; i < permissions.length; ++i) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i])) {
                            indexesOfPermissionsNeededToShow.add(i);
                        }
                    }

                    int size = indexesOfPermissionsNeededToShow.size();
                    if (size != 0) {
                        int i = 0;
                        boolean isPermissionGranted = true;

                        while (i < size && isPermissionGranted) {
                            isPermissionGranted = grantResults[indexesOfPermissionsNeededToShow.get(i)]
                                    == PackageManager.PERMISSION_GRANTED;
                            i++;
                        }

                        if (!isPermissionGranted) {
                            new AlertDialog.Builder(context)
                                    .setTitle("Permissions mandatory")
                                    .setMessage("All the permissions are required for this app")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            checkPermissions();
                                        }
                                    })
                                    .setCancelable(false)
                                    .create()
                                    .show();
                        }
                    }
                }
            }
        }


    }


//    ArrayList<String> permissionsList;
//    String[] permissionsStr = {
//            Manifest.permission.CAMERA,
//            Manifest.permission.RECORD_AUDIO,
//            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.ACCESS_FINE_LOCATION
//    };
//    int permissionsCount = 0;
//
//
//    private boolean hasPermission(Context context, String permissionStr) {
//        return ContextCompat.checkSelfPermission(context, permissionStr) == PackageManager.PERMISSION_GRANTED;
//    }
//
//    AlertDialog alertDialog;
//
//    private void showPermissionDialog() {
////        binding.txtStatus.setText("Showing settings dialog");
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Permission required")
//                .setMessage("Some permissions are needed to be allowed to use this app without any problems.")
//                .setPositiveButton("Ok", (dialog, which) -> {
//                    dialog.dismiss();
//                });
//        if (alertDialog == null) {
//            alertDialog = builder.create();
//            if (!alertDialog.isShowing()) {
//                alertDialog.show();
//            }
//        }
//    }
//
//    private void askForPermissions(ArrayList<String> permissionsList) {
//        String[] newPermissionStr = new String[permissionsList.size()];
//        for (int i = 0; i < newPermissionStr.length; i++) {
//            newPermissionStr[i] = permissionsList.get(i);
//        }
//        if (newPermissionStr.length > 0) {
////            binding.txtStatus.setText("Asking for permissions");
//
//        } else {
//            /* User has pressed 'Deny & Don't ask again' so we have to show the enable permissions dialog
//            which will lead them to app details page to enable permissions from there. */
//            showPermissionDialog();
//        }
//    }


    public class GetOtp extends AsyncTask<String, Void, String> {
        //ProgressDialog pd;

        int type;

        public GetOtp(int type) {
            this.type = type;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Please wait . . . ");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "";
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("MOBNO", params[0]));
            Random r = new Random(System.currentTimeMillis());
            otpnumber = String.valueOf(((1 + r.nextInt(2)) * 10000 + r.nextInt(10000)));
            param.add(new ParamsPojo("OTP", otpnumber));
            param.add(new ParamsPojo("CreatedBy", params[1]));

            // res = WebServiceCall.APICall(ApplicationConstants.GetOTPforRegistration, ApplicationConstants.webservice_d2d, param);
            res = WebServiceCall.APICall(ApplicationConstants.GetOTPForLogin, ApplicationConstants.webservice_d2d, param);
            return res;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            pd.dismiss();
            String status = "", message = "";
            try {
                if (!result.equals("")) {

//                    ResourcesListModel resourcesListModel = new Gson().fromJson(result, ResourcesListModel.class);
                    JSONObject jsonObject = new JSONObject(result);
                    status = jsonObject.getString("status");
                    message = jsonObject.getString("message");
                    if (status.equalsIgnoreCase("success")) {
//                        ArrayList<ResourcesListModel.OutputBean> resourceList = resourcesListModel.getOutput();
//                        if (resourceList.size() > 0) {

//                        Utilities.showAlertDialog(context, "Alert", "Otp sent"+""+ mobileNo + " "+ "number successfully", true);

                        Utilities.showToastMessage("Otp sent" + " " + "on" + " " + mobileNo + " " + "number successfully", context, true);


                        if (type == 1) {
//                                // verifyOtp(mno, resourceList.get(0).getDesgId());
                            verifyOtp(mobileNo, otpnumber);
                        }


                    } else {
                        Utilities.showAlertDialog(context, "Alert", message, false);

                    }
                } else {
                    Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                }
            } catch (Exception e) {
                e.printStackTrace();

                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
            }
        }
    }


    public class VerifyOtp extends AsyncTask<String, Void, String> {

        AlertDialog alertDialog;
        // String otp;

        public VerifyOtp(AlertDialog alertDialog) {
            this.alertDialog = alertDialog;
            //  this.otp = otp;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Please wait . . . ");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "";
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("MOBNO", params[0]));
            param.add(new ParamsPojo("OTP", params[1]));
            res = WebServiceCall.APICall(ApplicationConstants.VerifyOTPForLogin, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);

            pd.dismiss();
            String status = "", message = "";
            try {
                if (!result.equals("")) {

//                    ResourcesListModel resourcesListModel = new Gson().fromJson(result, ResourcesListModel.class);
//                    type = resourcesListModel.getStatus();
//                    message = resourcesListModel.getMessage();
                    JSONObject jsonObject = new JSONObject(result);
                    status = jsonObject.getString("status");
                    message = jsonObject.getString("message");
                    if (status.equalsIgnoreCase("success")) {


//                        session.createUserLoginSessionAfterOtp();


                        alertDialog.dismiss();


                        new CheckAndroidID().execute(Empcode, tokenIDString);

//                        {
//                            // Utilities.showAlertDialog(context, "Alert", message, true);
//                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                            builder.setIcon(R.drawable.icon_success);
//                            builder.setTitle("Success");
//                            builder.setCancelable(false);
//                            builder.setMessage("OTP Verification successfully");
//
////                            new UserLoginAppForUserData().execute(edt_username.getText().toString().trim(), edt_password.getText().toString().trim(), android_id);
//
//                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//
////                                    session.createSession();
////
////
////                                    Intent intent = new Intent(context, SiteSurvey_Menu_Activity.class);
////                                    startActivity(intent);
////                                    ((Activity) context).finish();
//
//
//                                    new CheckAndroidID().execute(Empcode, tokenIDString);
//
//                                }
//
//
//                            });
//                            builder.show();
//                        }
                    } else {
                        Utilities.showAlertDialog(context, "Fail", message, false);
                    }
                } else {
                    Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                }
            } catch (Exception e) {
                e.printStackTrace();

                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
            }
        }
    }
//    public class CheckAndroidID extends AsyncTask<String, Void, String> {
//
//        // String otp;
//
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pd.setMessage("Please wait . . . ");
//            pd.setCancelable(false);
//            pd.show();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            String res = "";
//            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
//            param.add(new ParamsPojo("UserId", params[0]));
//            param.add(new ParamsPojo("AndroidID", params[1]));
//
//            res = WebServiceCall.APICall(ApplicationConstants.GetUSERAndroidID, ApplicationConstants.webservice_d2d, param);
//            return res;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//
//            super.onPostExecute(result);
//
//            pd.dismiss();
//            String status = "", message = "";
//            try {
//                if (!result.equals("")) {
//
//                    JSONObject jsonObject = new JSONObject(result);
//                    status = jsonObject.getString("status");
//                    message = jsonObject.getString("message");
//                    if (status.equalsIgnoreCase("fail")) {
//                        if (Integer.parseInt(message) == 0) {
//
//                            {
//                                // Utilities.showAlertDialog(context, "Alert", message, true);
//                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                                builder.setIcon(R.drawable.icon_success);
//                                builder.setTitle("Success");
//                                builder.setCancelable(false);
//                                builder.setMessage("This device (" + Utilities.getDeviceId(context) + ") will be treated as authorised device for login, make sure you're logging in using your own device.");
//
//
//                                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                                        isResponded = true;
//                                        isDialogOpen = false;
//                                        new SaveAndroidID().execute(Empcode, Utilities.getDeviceId(context));
//                                        dialogInterface.dismiss();
//
//
//
//                                    }
//
//                                });
//                                builder.show();
//                            }
//
//                        }
//
//
//
//
//
//                    } else {
//                        Utilities.showAlertDialog(context, "Fail", message, false);
//                    }
//                } else {
//                    Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//
//                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
//            }
//        }
//    }


    public class CheckAndroidID extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            String res = "";
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("UserId", params[0]));
            param.add(new ParamsPojo("AndroidID", params[1]));


            res = WebServiceCall.APICall(ApplicationConstants.GetUSERAndroidID, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            Log.d("CheckAndroidID", result);
            try {
                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {

                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("message");
                    String isLoginAllowed = jsonObject.getString("AllowedForLogin");


                    if (status.equalsIgnoreCase("fail")) {
                        if (Integer.parseInt(msg) == 0) {
                            isDialogOpen = true;
                            isResponded = false;


//                            AlertDialog.Builder builder = new AlertDialog.Builder(Login_Activity.this);
//                            builder.setIcon(R.drawable.icon_success);
//                            builder.setTitle("Alert");
//                            builder.setCancelable(false);
//                            builder.setMessage("This device (" + Utilities.getDeviceId(context) + ") will be treated as authorised device for login, make sure you're logging in using your own device.");
//                            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    isResponded = true;
//                                    isDialogOpen = false;
//                                    new SaveAndroidID().execute(Empcode, tokenIDString);
//                                    dialog.dismiss();
//                                }
//                            });
//                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//
//                                    isResponded = true;
//                                    isDialogOpen = false;
//                                    session.logoutUser();
//                                    dialog.dismiss();
//
//                                }
//                            });
//                            builder.show();


                            Utilities.showAlertDialogNew(context, "Alert",
                                    "हा मोबाईल (" + Utilities.getDeviceId(context) + ") लॉगिनसाठी अधिकृत मोबाईल मानला जाईल, तुम्ही तुमच्या स्वतःच्या मोबाईलचा वापर करून लॉग इन करत आहात याची खात्री करा.",
                                    "Confirm", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            isResponded = true;
                                            isDialogOpen = false;
                                            new SaveAndroidID().execute(Empcode, Utilities.getDeviceId(context), versionNo);
                                            dialogInterface.dismiss();
                                        }
                                    }, "Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            isResponded = true;
                                            isDialogOpen = false;
                                            session.logoutUser();
                                            dialogInterface.dismiss();

                                        }
                                    }

                            );

                        } else {
                            isDialogOpen = true;
                            isResponded = false;

                            Utilities.showAlertDialogNew(context, "Alert", "You're trying to login from different device (" + Utilities.getDeviceId(context) + "). Do you want to change your device?", "Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            isResponded = true;
                                            isDialogOpen = false;
//                                            if(Integer.parseInt(msg)>=1){
//                                                isDialogOpen=true;
//                                                isResponded=false;
//                                                Utilities.showAlertDialog(context, "Alert", "You have changed device for "+msg+" times", "Continue", new DialogInterface.OnClickListener() {
//                                                            @Override
//                                                            public void onClick(DialogInterface dialogInterface, int i) {
//                                                                isResponded=true;
//                                                                isDialogOpen=false;
                                            new SaveAndroidID().execute(Empcode, Utilities.getDeviceId(context), versionNo);
                                            dialogInterface.dismiss();
//                                                            }
//                                                        }
//                                                );
//                                            }
                                        }
                                    }, "No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            isResponded = true;
                                            isDialogOpen = false;
                                            session.logoutUser();
                                            dialogInterface.dismiss();

                                        }
                                    }

                            );


                        }


                    } else {


                        if (isLoginAllowed.equals("0")) {

                            Utilities.showAlertDialog(context, "Alert", "तुम्ही तुमचा मोबाईल बदलण्याची मर्यादा गाठली आहे. लॉगिन करण्यासाठी तुमचा नोंदणीकृत मोबाईल वापरा.", "ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    session.logoutUser();
                                }
                            });

                        } else {

                            SaveAndroidToken setNotificationData = new SaveAndroidToken();
                            setNotificationData.execute();

                        }

                    }

                } else
                    Utilities.showAlertDialog(context, "Please try again",
                            "Server not responding.", false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(values);
        }
    }


    public class SaveAndroidID extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pd.setMessage("Please wait ...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.d("SaveAndroidID", Arrays.toString(params));
            String res = "[]";
            try {
                RequestBody formBody1 = new FormBody.Builder()
                        .add("UserId", params[0])
                        .add("AndroidID", params[1])
                        .add("VersionNo", params[2])
                        .build();
                String url = ApplicationConstants.webservice_d2d + ApplicationConstants.InsertUSERAndroidID;
                OkHttpClient client1 = new OkHttpClient.Builder()
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .writeTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .build();
                Request request = new Request.Builder()
                        .url(url)
                        .post(formBody1)
                        .build();
                Response response1 = client1.newCall(request).execute();
                res = response1.body().string();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            pd.dismiss();
            Log.d("SaveAndroidID", result);
            try {
                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {

                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("message");
                    String isLoginAllowed = jsonObject.getString("AllowedForLogin");


                    if (status.equalsIgnoreCase("fail")) {
//                        Utilities.alertShowMsgWithAlert(context, status,msg);


                        if (msg.equalsIgnoreCase("AndroidID Already Exists With Another User")) {
                            isDialogOpen = true;
                            isResponded = false;
                            edt_password.setText("");
                            edt_username.setText("");

                            Utilities.showAlertDialog(context, "Alert", "हा मोबाईल  (" + Utilities.getDeviceId(context) + ") आधीच दुसऱ्या वापरकर्त्याशी लिंक केलेला आहे. कृपया या ॲप्लिकेशनमध्ये प्रवेश करण्यासाठी तुमचा स्वतःचा मोबाईल वापरा.", "Okay", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            isResponded = true;
                                            isDialogOpen = false;
                                            session.logoutUser();
                                            dialogInterface.dismiss();
//
////
//                                                                        session.createSession();
//
//
//                                            startActivity(new Intent(context, SiteSurvey_Menu_Activity.class));


                                        }
                                    }

                            );
                        } else {
                            Utilities.showAlertDialog(context, status, msg, false);
//                            SaveAndroidToken setNotificationData = new SaveAndroidToken();
//                            setNotificationData.execute();
                        }

                    } else {


                        if (isLoginAllowed.equalsIgnoreCase("0")) {

                            Utilities.showAlertDialog(context, "Alert", "तुम्ही तुमचा मोबाईल बदलण्याची मर्यादा गाठली आहे. लॉगिन करण्यासाठी तुमचा नोंदणीकृत मोबाईल वापरा.", "ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    session.logoutUser();
                                }
                            });


                        } else {


                            Utilities.showToastMessage("You have changed device for" + " " + msg + " " + "times ", context, true);


                            SaveAndroidToken setNotificationData = new SaveAndroidToken();
                            setNotificationData.execute();


                        }

                    }

                } else
                    Utilities.showAlertDialog(context, "Please try again",
                            "Server not responding.", false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(values);
        }
    }

//    private void sendOtpForUpdatePassword(String mobileNo, int type) {
//        final ProgressDialog progressDialog = new ProgressDialog(YourActivity.this); // replace with your Activity
//        progressDialog.setMessage("Please wait . . . ");
//        progressDialog.setCancelable(false);
//        progressDialog.show();
//
//        String otpnumber = String.valueOf(((1 + new Random(System.currentTimeMillis()).nextInt(2)) * 10000 + new Random().nextInt(10000)));
//
//        ApiInterface apiService = ApiClient.d2d_call().create(ApiInterface.class); // use correct base URL setup
//        Call<JsonObject> call = apiService.sendOtp(mobileNo, otpnumber);
//
//        call.enqueue(new Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                progressDialog.dismiss();
//                if (response.isSuccessful() && response.body() != null) {
//                    try {
//                        JsonObject json = response.body();
//                        String status = json.get("status").getAsString();
//                        String message = json.get("message").getAsString();
//
//                        if (status.equalsIgnoreCase("success")) {
//                            Utilities.showToastMessage("OTP sent successfully on " + mobileNo, context, true);
//
//                            if (type == 1) {
//                                verificationRemark("1", otpnumber);
//                            } else if (type == 3) {
//                                // Handle other case if needed
//                            }
//                        } else {
//                            Utilities.showAlertDialog(context, "Alert", message, false);
//                        }
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        Utilities.showAlertDialog(context, "Error", "Something went wrong", false);
//                    }
//                } else {
//                    Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable t) {
//                progressDialog.dismiss();
//                t.printStackTrace();
//                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
//            }
//        });
//    }


    private void getOTPForForgotPass(String mobileNo, AlertDialog alertDialog) {

        final ProgressDialog progressDialog = new ProgressDialog(Login_Activity.this);
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String otpnumber = String.valueOf(((1 + new Random(System.currentTimeMillis()).nextInt(2)) * 10000 + new Random().nextInt(10000)));


        ApiInterface apiInterface = ApiClient.getD2DClient().create(ApiInterface.class);
        apiInterface.getOtp(mobileNo, otpnumber).enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                progressDialog.dismiss();

                try {
                    if (response.isSuccessful() && response.body() != null) {
                        String responseString = response.body().string();
                        Log.i("TAG", "onResponse: " + responseString);


                        JSONObject jsonObject = new JSONObject(responseString);


                        String status = jsonObject.optString("status");
                        String message = jsonObject.optString("message");

                        if (status.equalsIgnoreCase("Success")) {

                            alertDialog.dismiss();


                            Utilities.showToastMessage("Otp sent" + " " + "on" + " " + mobileNo + " " + "number successfully", context, true);


                            forgotPassPromt(mobileNo, otpnumber);


                        } else {
                            Utilities.showAlertDialog(context, "Alert", message, false);
                        }


                    } else {
                        Log.e("TAG", "Response error: " + response.code() + " - " + response.message());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("TAG", "Exception: " + e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("TAG", "API call failed: " + t.getMessage());
            }

        });
    }


    void inserForgotPass(String otp, String mobile, String pwd, AlertDialog alertDialog) {
        pd.setMessage("Please wait . . . ");
        pd.setCancelable(false);
        pd.show();
        ApiInterface apiInterface = ApiClient.getD2DClient().create(ApiInterface.class);
        apiInterface.insertForgotPassword(pwd, mobile, otp).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                pd.dismiss();
                if (response.isSuccessful()) {
                    try {
                        pd.dismiss();
                        String result = response.body().string();
                        Log.i("TAG", "onPostExecute: " + result);

                        if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
                            JSONObject obj = new JSONObject(result);
                            String status = obj.getString("status");
                            String message = obj.getString("message");
                            if (status.equalsIgnoreCase("Success")) {
//
                                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                                builder.setIcon(R.drawable.icon_success);
                                builder.setTitle("Success");
                                builder.setCancelable(false);
                                builder.setMessage("Password Reset Successfully");
                                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        alertDialog.dismiss();
//                                        session.logoutUser();

                                    }
                                });
                                builder.show();


                            } else {
                                Utilities.showAlertDialog(context, status, message, false);
                            }
                        } else
                            Utilities.showAlertDialog(context, "Please try again", "Server not responding.", false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    try {
                        Utilities.showAlertDialog(context, "Failure", response.errorBody().string(), false);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Utilities.showAlertDialog(context, "Failure", e.getMessage(), false);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pd.dismiss();

                Utilities.showAlertDialog(context, "Failure", t.getMessage(), false);

            }
        });
    }


    public class SaveAndroidToken extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pd.setMessage("Please wait ...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            // TODO Auto-generated method stub
            String res = "[]";
//            tokenIDString = pref.getString(ApplicationConstants.Shared_Pref_Notification_Key, null);
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("USERID", Empcode));
            param.add(new ParamsPojo("ANDROIEDTOKEN", tokenIDString != null ? tokenIDString : ""));
            param.add(new ParamsPojo("ActiveStatus", "1"));

            res = WebServiceCall.APICall(ApplicationConstants.SaveAndroidToken, ApplicationConstants.webservice_d2d, param);


            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            pd.dismiss();
            try {
                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
                    try {
                        Gson gson = new Gson();
                        UserLoginApp_Outputpojo pojo = gson.fromJson(result, UserLoginApp_Outputpojo.class);
                        String status = pojo.getStatus();
                        String msg = pojo.getMessage();
                        if (status.equalsIgnoreCase("Success")) {

                            //////////commented by shashank///////////////////////////


                            session.createSession();


                            Intent intent = new Intent(context, SiteSurvey_Menu_Activity.class);
                            startActivity(intent);
                            ((Activity) context).finish();

//                            new GetOtp(1).execute(mobileNo, Empcode);


                        } else
                            Utilities.showAlertDialog(context, status, msg, false);

                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else
                    Utilities.showAlertDialog(context, "Please try again",
                            "Server not responding.", false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(values);
        }

    }


//    private void sendTokenToServer(String token) {
//        OkHttpClient client = new OkHttpClient();
//
//        // Modify this based on your server requirements
//        RequestBody requestBody = new FormBody.Builder()
//                .add("token", token)
//                .build();
//
//        Request request = new Request.Builder()
//                .url("https://your.api.url/save-token") // 🔁 Replace with your API endpoint
//                .post(requestBody)
//                .build();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                Log.e(TAG, "Token send failed: ", e);
//            }
//
//            @Override
//            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                if (response.isSuccessful()) {
//                    Log.d(TAG, "Token successfully sent to server");
//                } else {
//                    Log.e(TAG, "Server error: " + response.code());
//                }
//            }
//        });
//    }


    void insertAndroidToken(String token) {
//        List<ParamsPojo> param = new ArrayList<ParamsPojo>();


        ApiInterface apiInterface = ApiClient.getD2DClient().create(ApiInterface.class);


        apiInterface.insertSaveToken(session.getUserDetailsJson().getEmpCode(),token,"1").enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
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
                                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
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
                                Utilities.showAlertDialog(context, status, message, false);
                            }
                        } else
                            Utilities.showAlertDialog(context, "Please try again", "Server not responding.", false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    try {
                        Utilities.showAlertDialog(context, "Failure", response.errorBody().string(), false);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Utilities.showAlertDialog(context, "Failure", e.getMessage(), false);
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Utilities.showAlertDialog(context, "Failure", t.getMessage(), false);

            }
        });
    }



}
