package com.myhindlab.abkat.activities.attendance_details;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.myhindlab.abkat.BuildConfig;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.Login_Activity;
import com.myhindlab.abkat.activities.attendance_details.adapter.AttendanceDetailsAdapter;
import com.myhindlab.abkat.activities.attendance_details.model.AttendanceDetailsModel;
import com.myhindlab.abkat.activities.attendance_details.model.AttendanceImageModel;
import com.myhindlab.abkat.activities.doortodoor.D2DPatientRegistration_Activity;
import com.myhindlab.abkat.activities.payout.InvoiceGenModel;
import com.myhindlab.abkat.activities.payout.adapter.InvoiceAdapter;
import com.myhindlab.abkat.activities.payout.model.YearModel;
import com.myhindlab.abkat.activities.re_registration.model.BeneficiaryCountForPageLoadModel;
import com.myhindlab.abkat.adapters.TeamsDetailsAssignedAdapter;
import com.myhindlab.abkat.appointment_confirmation.pojo.AppointmentListModel;
import com.myhindlab.abkat.appointment_confirmation.pojo.OutputItem;
import com.myhindlab.abkat.camera.CameraActivity;
import com.myhindlab.abkat.camera.utils.CaptureMode;
import com.myhindlab.abkat.databinding.ActivityAttendanceDetailsBinding;
import com.myhindlab.abkat.databinding.ActivityAttendanceDetailsForCcBinding;
import com.myhindlab.abkat.models.AreaListModel;
import com.myhindlab.abkat.models.AvailabilityTestModel;
import com.myhindlab.abkat.models.CampListModel;
import com.myhindlab.abkat.models.DistrictOrgModel;
import com.myhindlab.abkat.models.GetTeamsModel;
import com.myhindlab.abkat.models.LandingLabModel;
import com.myhindlab.abkat.models.PincodeListModel;
import com.myhindlab.abkat.models.SubDivisionModel;
import com.myhindlab.abkat.models.SubOrganizationModel;
import com.myhindlab.abkat.models.TalukaListForFilterModel;
import com.myhindlab.abkat.models.TeamsDetailsModel;
import com.myhindlab.abkat.rest.ApiClient;
import com.myhindlab.abkat.rest.ApiInterface;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.RecyclerItemClickListener;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AttendanceDetailsForCCActivity extends AppCompatActivity implements InvoiceAdapter.onTouchListner {
    ActivityAttendanceDetailsForCcBinding binding;
    private UserSessionManager sessionManager;

    private Bitmap patientPicBm = null, ackPicBm = null;

    private List<AttendanceDetailsModel.Output> attandanceDeatilsList;


    private List<AppointmentListModel.Output> districtList_models;

    private LocalBroadcastManager localBroadcastManager;


    private String TAG = D2DPatientRegistration_Activity.class.getSimpleName();


    private List<OutputItem> campBeneficiaryList;

    private List<BeneficiaryCountForPageLoadModel.Output> adminlist;
    private Calendar calMonth;


    private int mYear, mMonth, mDay, mYear1, mMonth1, mDay1;

    private String CampDate, otpnumber, CampDateToDate, type, toDate, yearid, monthId;

    private ProgressDialog pd;
    private AlertDialog teamDialog;
    private Context mContext;

    private ArrayList<TeamsDetailsModel> assignteamlist;

    private ArrayList<GetTeamsModel.OutputBean> teamList;


    private List<DistrictOrgModel.Output> districtList;
    private List<AreaListModel.Output> areaList;
    private List<PincodeListModel.Output> pincodeList;
    private List<SubDivisionModel.Output> divisionList;
    private String selectedApptDate;
    private String TeamName;
    private String campId;
    private String CampConfirmation = "0";
    private String inPhotoPath = "";
    private String outImagePath = "";
    private String campPhotoPath = "";
    private String patientImagePath = "";
    private String testFlag = "0";

    private ApiInterface apiInterface;

    private String ackImagePath = "";

    private List<SubOrganizationModel.Output> organizationList;
    private List<TalukaListForFilterModel.Output> talukaList;
    private List<LandingLabModel.Output> landingLabList;
    private String call_statusid = "1", teamId = "0", camptype, userInvoiceId, isPaymentNotReceived, isPaymentReceived, searchFilterType = "0", landingLabId = "0", mobileNumber, EmpCode, divisionId = "0", pincodeArray, referenceId, size, areaArray, taluka, STATELGDCODE = "2", DISTLGDCODE = "0", agentId,
            oganizationId = "0", DESGID, district, TALLGDCODE = "0", LabCode, statusType = "0";
    private UserSessionManager session;
    private SearchView searchView;

    private String isMarkInOut = "0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
//
//        // 2. Get the controller for system bars
//        WindowInsetsControllerCompat windowInsetsController =
//                WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
//
//        if (windowInsetsController != null) {
//            // Hide the status bar and the navigation bar
//            windowInsetsController.hide(WindowInsetsCompat.Type.systemBars());
//
//            // Optional: Specify how hidden bars behave when the user interacts
//            windowInsetsController.setSystemBarsBehavior(
//                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
//            );
//        }

        binding = ActivityAttendanceDetailsForCcBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        getSessionData();
        setDefault();
        eventListener();
        setUpToolbar();
    }


    void init() {
        mContext = AttendanceDetailsForCCActivity.this;
        sessionManager = new UserSessionManager(mContext);
        pd = new ProgressDialog(mContext);

        apiInterface = ApiClient.getD2DClient().create(ApiInterface.class);


        localBroadcastManager = LocalBroadcastManager.getInstance(mContext);
        IntentFilter intentFilter = new IntentFilter("refresh_exp_benf_list");
        localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);


//        calMonth = Calendar.getInstance();


        binding.rvPatientList.setLayoutManager(new LinearLayoutManager(mContext));


        binding.btnSave.setBackgroundColor(ContextCompat.getColor(this, R.color.primaryDarkColor));


        Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);


        binding.tvDate.setText(Utilities.dfDate4.format(new Date()));
        selectedApptDate = Utilities.dfDate4.format(new Date());

    }


    private void getSessionData() {
        try {
            JSONArray user_info = new JSONArray(sessionManager.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                DESGID = json.getString("DESGID");
                DISTLGDCODE = json.getString("DISTLGDCODE");
                district = json.getString("district");
                TALLGDCODE = json.getString("TALLGDCODE");
                taluka = json.getString("taluka");
                EmpCode = json.getString("EmpCode");
                mobileNumber = json.getString("BMobile");
                LabCode = json.getString("LabCode");
                oganizationId = json.getString("SubOrgId");


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setDefault() {
        if (getIntent() != null) {
            CampDate = getIntent().getStringExtra("FromDate");
            toDate = getIntent().getStringExtra("ToDate");
//            oganizationId = getIntent().getStringExtra("organizationId");
            divisionId = getIntent().getStringExtra("divisionId");
            DISTLGDCODE = getIntent().getStringExtra("distLgd");
            TALLGDCODE = getIntent().getStringExtra("talukaId");
            landingLabId = getIntent().getStringExtra("labcode");


            binding.btnSaveCampClosingConfirmation.setEnabled(false);
            binding.btnSaveCampClosingConfirmation.setBackgroundColor(ContextCompat.getColor(this, R.color.primaryDarkColor));




//            Calendar cal = Calendar.getInstance();
//            cal.add(Calendar.MONTH, -1);
//
//            mYear = cal.get(Calendar.YEAR);
//            mMonth = cal.get(Calendar.MONTH);
//            mDay = cal.get(Calendar.DAY_OF_MONTH);
//
//
//            String[] monthYear = (android.text.format.DateFormat.format("MM yyyy", cal)).toString().split(" ");
//
//            monthId = monthYear[0];
//            yearid = monthYear[1];

            getTestCompleteFlag();


            if (sessionManager.isHllUser()) {


                if (DESGID.equals("139") || DESGID.equals("136")) {
                    camptype = "6";
                } else {
                    camptype = "3";
                }

                statusType = "1";

                binding.rbD2d.setChecked(true);

            } else {
                camptype = "1";
                statusType = "2";

                binding.rbRegular.setChecked(true);

            }


            if (binding.rbRegular.isChecked()) {

                camptype = "1";
                statusType = "2";

                binding.rbRegular.setTextColor(ContextCompat.getColor(this, R.color.light_text));
                binding.rbD2d.setTextColor(ContextCompat.getColor(this, R.color.white));
                binding.llMainTeam.setVisibility(View.GONE);


                binding.llMainBtn.setVisibility(View.GONE);


            } else if (binding.rbD2d.isChecked() || camptype.equals("3") || camptype.equals("6")) {

                if (DESGID.equals("139") || DESGID.equals("136")) {
                    camptype = "6";
                } else {
                    camptype = "3";
                }
                statusType = "1";

                binding.rbD2d.setTextColor(ContextCompat.getColor(this, R.color.light_text));
                binding.rbRegular.setTextColor(ContextCompat.getColor(this, R.color.white));
                binding.llMainTeam.setVisibility(View.VISIBLE);

                binding.llMainBtn.setVisibility(View.VISIBLE);
            }


            if (DESGID.equals("29") || DESGID.equals("162") || DESGID.equals("92") || DESGID.equals("139") || DESGID.equals("136")) {

                binding.llMainBtnUpload.setVisibility(View.GONE);
                binding.llMainBtn.setVisibility(View.GONE);

            } else {

                if (sessionManager.isHllUser()) {
                    binding.rbRegular.setVisibility(View.GONE);
                } else {
                    binding.rbD2d.setVisibility(View.GONE);
                }
            }


        }

    }

    private void warningDialogue() {
        Dialog dialog;
        dialog = new Dialog(AttendanceDetailsForCCActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.attendance_mark_warning_dialoge);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        ImageView btnCloseDialog = dialog.findViewById(R.id.btnCloseDialog);
        Button btnClose = dialog.findViewById(R.id.btnClose);
        btnCloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void eventListener() {

//        binding.tvYear.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getYearlist();
//
//            }
//        });
//        binding.tvMonth.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (binding.tvYear.getText().toString().matches("")) {
//                    Utilities.showAlertDialog(mContext, "Alert", "Please select year", false);
//                    return;
//                }
//
//                getMonthlist();
//
//            }
//        });


        binding.btnApproveIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (binding.tvDate.getText().toString().isEmpty()) {
                    Utilities.showAlertDialog(mContext, "Alert", "Please select date", false);
                    return;
                }

                if (binding.tvCampId.getText().toString().isEmpty()) {
                    Utilities.showAlertDialog(mContext, "Alert", "Please select campId", false);
                    return;
                }



                if (binding.rbD2d.isChecked() || camptype.equals("3") | camptype.equals("6")) {
                    if (binding.tvTeam.getText().toString().isEmpty()) {
                        Utilities.showAlertDialog(mContext, "Alert", "Please select team number", false);
                        return;
                    }
                }



                if (inPhotoPath.equals("")|| inPhotoPath.equals("NA")) {

                    Utilities.showAlertDialog(mContext, "Alert", "टीमकडून फोटो अजून अपलोड झालेला नाही.", false);
                    return;

                }


                insertPhotoFlag("1", "1");
            }
        });

        binding.btnRejectIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (binding.tvDate.getText().toString().isEmpty()) {
                    Utilities.showAlertDialog(mContext, "Alert", "Please select date", false);
                    return;
                }

                if (binding.tvCampId.getText().toString().isEmpty()) {
                    Utilities.showAlertDialog(mContext, "Alert", "Please select campId", false);
                    return;
                }

                if (binding.rbD2d.isChecked() || camptype.equals("3") | camptype.equals("6")) {
                    if (binding.tvTeam.getText().toString().isEmpty()) {
                        Utilities.showAlertDialog(mContext, "Alert", "Please select team number", false);
                        return;
                    }
                }


                if (inPhotoPath.equals("") || inPhotoPath.equals("NA")) {

                    Utilities.showAlertDialog(mContext, "Alert", "टीमकडून फोटो अजून अपलोड झालेला नाही.", false);
                    return;

                }


                insertPhotoFlag("1", "2");
            }
        });


        binding.btnApproveCamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (binding.tvDate.getText().toString().isEmpty()) {
                    Utilities.showAlertDialog(mContext, "Alert", "Please select date", false);
                    return;
                }

                if (binding.tvCampId.getText().toString().isEmpty()) {
                    Utilities.showAlertDialog(mContext, "Alert", "Please select campId", false);
                    return;
                }

                if (binding.rbD2d.isChecked() || camptype.equals("3") | camptype.equals("6")) {
                    if (binding.tvTeam.getText().toString().isEmpty()) {
                        Utilities.showAlertDialog(mContext, "Alert", "Please select team number", false);
                        return;
                    }
                }


                if (campPhotoPath.equals("") || campPhotoPath.equals("NA")) {

                    Utilities.showAlertDialog(mContext, "Alert", "टीमकडून फोटो अजून अपलोड झालेला नाही.", false);
                    return;

                }

                insertPhotoFlag("3", "1");
            }
        });

        binding.btnRejectCamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (binding.tvDate.getText().toString().isEmpty()) {
                    Utilities.showAlertDialog(mContext, "Alert", "Please select date", false);
                    return;
                }

                if (binding.tvCampId.getText().toString().isEmpty()) {
                    Utilities.showAlertDialog(mContext, "Alert", "Please select campId", false);
                    return;
                }

                if (binding.rbD2d.isChecked() || camptype.equals("3") | camptype.equals("6")) {
                    if (binding.tvTeam.getText().toString().isEmpty()) {
                        Utilities.showAlertDialog(mContext, "Alert", "Please select team number", false);
                        return;
                    }
                }


                if (campPhotoPath.equals("") || campPhotoPath.equals("NA")) {

                    Utilities.showAlertDialog(mContext, "Alert", "टीमकडून फोटो अजून अपलोड झालेला नाही.", false);
                    return;

                }


                insertPhotoFlag("3", "2");
            }
        });

        binding.btnApproveOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (binding.tvDate.getText().toString().isEmpty()) {
                    Utilities.showAlertDialog(mContext, "Alert", "Please select date", false);
                    return;
                }

                if (binding.tvCampId.getText().toString().isEmpty()) {
                    Utilities.showAlertDialog(mContext, "Alert", "Please select campId", false);
                    return;
                }


                if (binding.rbD2d.isChecked() || camptype.equals("3") | camptype.equals("6")) {
                    if (binding.tvTeam.getText().toString().isEmpty()) {
                        Utilities.showAlertDialog(mContext, "Alert", "Please select team number", false);
                        return;
                    }
                }

                if (outImagePath.equals("")|| outImagePath.equals("NA")) {

                    Utilities.showAlertDialog(mContext, "Alert", "टीमकडून फोटो अजून अपलोड झालेला नाही.", false);
                    return;

                }

                insertPhotoFlag("2", "1");
            }
        });


        binding.btnRejectOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (binding.tvDate.getText().toString().isEmpty()) {
                    Utilities.showAlertDialog(mContext, "Alert", "Please select date", false);
                    return;
                }

                if (binding.tvCampId.getText().toString().isEmpty()) {
                    Utilities.showAlertDialog(mContext, "Alert", "Please select campId", false);
                    return;
                }


                if (binding.rbD2d.isChecked() || camptype.equals("3") | camptype.equals("6")) {
                    if (binding.tvTeam.getText().toString().isEmpty()) {
                        Utilities.showAlertDialog(mContext, "Alert", "Please select team number", false);
                        return;
                    }
                }

                if (outImagePath.equals("") || outImagePath.equals("NA")) {

                    Utilities.showAlertDialog(mContext, "Alert", "टीमकडून फोटो अजून अपलोड झालेला नाही.", false);
                    return;

                }

                insertPhotoFlag("2", "2");
            }
        });


        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                startActivity(new Intent(mContext, CampClosingConfirmationActivity.class)
//                        .putExtra("Date", selectedApptDate)
//                        .putExtra("DISTLGDCODE", "0")
//                        .putExtra("campId", campId)
//
//                );


                submit();

            }
        });


        binding.btnSaveCampClosingConfirmation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, CampClosingConfirmationActivity.class)
                        .putExtra("Date", selectedApptDate)
                        .putExtra("DISTLGDCODE", "0")
                        .putExtra("campId", campId)

                );

            }
        });

        binding.tvTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (binding.tvDate.getText().toString().isEmpty()) {
                    Utilities.showAlertDialog(mContext, "Alert", "Please select date", false);
                    return;
                }

                if (binding.tvCampId.getText().toString().isEmpty()) {
                    Utilities.showAlertDialog(mContext, "Alert", "Please select campId", false);
                    return;
                }


                if (DESGID.equals("92") || DESGID.equals("162") || DESGID.equals("29") || DESGID.equals("139") || DESGID.equals("136")) {
                    if (Utilities.isNetworkAvailable(mContext)) {
                        new GetTeamDetailsListForAssign().execute(campId, selectedApptDate, camptype);
                    } else {
                        Utilities.showToastMessage("Please Check Your Connection", mContext, false);
                    }

                } else {
                    new GetTeamId().execute(campId, EmpCode);
                }


            }
        });

        binding.tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(1);


            }
        });

        binding.tvCampId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (binding.tvDate.getText().toString().isEmpty()) {
                    Utilities.showAlertDialog(mContext, "Alert", "Please select date", false);
                    return;
                }


                getCampId();
            }
        });


        binding.rbRegular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                binding.rbRegular.setTextColor(ContextCompat.getColor(mContext, R.color.light_text));
                binding.rbD2d.setTextColor(ContextCompat.getColor(AttendanceDetailsForCCActivity.this, R.color.white));

                binding.llMainTeam.setVisibility(View.GONE);

                binding.tvCampId.setText("");
                binding.tvDate.setText("");
                binding.tvTeam.setText("");


                binding.llMainBtn.setVisibility(View.GONE);


                statusType = "2";
                camptype = "1";


                clearData();


            }
        });

        binding.rbD2d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                binding.rbD2d.setTextColor(ContextCompat.getColor(mContext, R.color.light_text));
                binding.rbRegular.setTextColor(ContextCompat.getColor(AttendanceDetailsForCCActivity.this, R.color.white));

                binding.llMainTeam.setVisibility(View.VISIBLE);
                statusType = "1";

                if (DESGID.equals("139") || DESGID.equals("136")) {
                    camptype = "6";
                } else {
                    camptype = "3";
                }

                binding.tvCampId.setText("");
                binding.tvDate.setText("");
                binding.tvTeam.setText("");


                binding.llMainBtn.setVisibility(View.VISIBLE);


                clearData();


//
//                if (binding.tvTeam.getText().toString().isEmpty()){
//                    Utilities.showAlertDialog(mContext,"Alert","Please select team number",false);
//                    return;
//                }

//                getAttendanceDetails();

                setUpToolbar();


            }
        });

    }

    private void clearData() {

        binding.btnSaveCampClosingConfirmation.setEnabled(false);

        binding.llMainBtnUpload.setVisibility(View.VISIBLE);


        if (camptype.equals("3") || camptype.equals("6")) {
            binding.llMainBtn.setVisibility(View.VISIBLE);

        }


        if (attandanceDeatilsList != null) {
            attandanceDeatilsList.clear();
        }

        AttendanceDetailsAdapter attendanceDetailsAdapter = new AttendanceDetailsAdapter(AttendanceDetailsForCCActivity.this, new ArrayList<>());
        binding.rvPatientList.setAdapter(attendanceDetailsAdapter);


        binding.imvCheckIn.setImageResource(R.drawable.icon_camera_primary);
        binding.imvCheckOut.setImageResource(R.drawable.icon_camera_primary);
        binding.imvCampImage.setImageResource(R.drawable.icon_camera_primary);


        binding.tvCampPhotoStatus.setVisibility(View.GONE);
        binding.tvOutPhotoStatus.setVisibility(View.GONE);
        binding.tvInPhotoStatus.setVisibility(View.GONE);

        binding.llMainIn.setVisibility(View.VISIBLE);
        binding.llMainCamp.setVisibility(View.VISIBLE);
        binding.llMainOut.setVisibility(View.VISIBLE);


        binding.tvCampPhotoStatus.setText("");
        binding.tvOutPhotoStatus.setText("");
        binding.tvInPhotoStatus.setText("");




        inPhotoPath = "";
        outImagePath = "";
        campPhotoPath = "";

        Picasso.with(this).cancelRequest(binding.imvCampImage);
        binding.imvCampImage.setImageResource(R.drawable.icon_camera_primary);
        binding.imvCampImage.setOnClickListener(null);

        Picasso.with(this).cancelRequest(binding.imvCheckIn);
        binding.imvCheckIn.setImageResource(R.drawable.icon_camera_primary);
        binding.imvCheckIn.setOnClickListener(null);


        Picasso.with(this).cancelRequest(binding.imvCheckOut);
        binding.imvCheckOut.setImageResource(R.drawable.icon_camera_primary);
        binding.imvCheckOut.setOnClickListener(null);



        binding.tvUploadedInTime.setText("");
        binding.tvUploadedOutTime.setText("");
        binding.tvUploadedCampTime.setText("");


        if (!(DESGID.equals("29") || DESGID.equals("162") || DESGID.equals("92") || DESGID.equals("139") || DESGID.equals("136"))) {

            inPhotoPath = "";
            outImagePath = "";
            patientImagePath = "";
            ackImagePath = "";
            campPhotoPath = "";

        } else {
            binding.llMainBtnUpload.setVisibility(View.GONE);
            binding.llMainBtn.setVisibility(View.GONE);


        }



    }


    private void submit() {


        if (binding.tvDate.getText().toString().isEmpty()) {
            Utilities.showAlertDialog(mContext, "Alert", "Please select date", false);
            return;
        }

        if (binding.tvCampId.getText().toString().isEmpty()) {
            Utilities.showAlertDialog(mContext, "Alert", "Please select campId", false);
            return;
        }


        if (binding.rbD2d.isChecked() || camptype.equals("3") | camptype.equals("6")) {
            if (binding.tvTeam.getText().toString().isEmpty()) {
                Utilities.showAlertDialog(mContext, "Alert", "Please select team number", false);
                return;
            }
        }


        if (isMarkInOut.equals("0")) {
            if (patientImagePath.equals("")) {
                Utilities.showToastMessage("Please capture check in photo", mContext, false);
                return;
            }
        } else {
        }


        if (isMarkInOut.equals("0")) {
            if (ackImagePath.equals("")) {
                Utilities.showToastMessage("कॅम्प closing confirmation देण्यापूर्वी टीमचा फोटो अपलोड करा.", mContext, false);
                return;
            }
        } else {
        }


        if (isMarkInOut.equals("1")) {
            if (patientImagePath.equals("")) {
                Utilities.showToastMessage("Please capture check in photo", mContext, false);
                return;
            }
        } else {
            patientImagePath = "";

        }


        if (isMarkInOut.equals("2")) {
            if (ackImagePath.equals("")) {
                Utilities.showToastMessage("कॅम्प closing confirmation देण्यापूर्वी टीमचा फोटो अपलोड करा.", mContext, false);
                return;
            }
        } else {
            ackImagePath = "";
        }


//        new InsertAttendanceData().execute(campId,patientImagePath,ackImagePath,isMarkInOut);

        insertData();


    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String confirmation = intent.getStringExtra("confirmation");

            if ("1".equals(confirmation)) {
                binding.llMainBtn.setVisibility(View.GONE);
            }

            Log.d("BroadcastReceiver", "onReceive: " + confirmation);
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(broadcastReceiver);
    }

    private void setUpToolbar() {
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        ImageButton imageButton = findViewById(R.id.btn_save_accordian);


        if (getSupportActionBar() != null) {

            getSupportActionBar().setTitle("Team Attendance In Camp");

        }
        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(v -> finish());


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog dialog;
                dialog = new Dialog(AttendanceDetailsForCCActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.attendance_mark_info_dialoge);
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                ImageView btnCloseDialog = dialog.findViewById(R.id.btnCloseDialog);
                btnCloseDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            }
        });

    }


    private void showDatePicker(int dateTypeId) {

        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(AttendanceDetailsForCCActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar selectedCalendar = Calendar.getInstance();
                        selectedCalendar.set(year, month, dayOfMonth);

                        // Apply validation based on dateTypeId
                        if (dateTypeId == 1) {
                            Calendar minDateCalendar = Calendar.getInstance();
                            minDateCalendar.add(Calendar.DAY_OF_MONTH, -7);
                            if (selectedCalendar.before(minDateCalendar)) {
                                binding.tvDate.setError("Please select a date after 7 days ago.");
                                return;
                            }
                        }

                        String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        String dt = "";
                        try {
                            dt = Utilities.dfDate4.format(Utilities.dfDate2.parse(selectedDate));
                            selectedApptDate = dt;

                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }

                        binding.tvDate.setText(dt);

                        binding.tvCampId.setText("");
                        binding.tvTeam.setText("");
                        campId = "0";


                        clearData();


                    }
                }, currentYear, currentMonth, currentDay);

        if (dateTypeId == 1) {
            // Set minimum date to 7 days ago and max date to today for dateTypeId 1
            Calendar minDateCalendar = Calendar.getInstance();
            minDateCalendar.add(Calendar.DAY_OF_MONTH, -7);
            datePickerDialog.getDatePicker().setMinDate(minDateCalendar.getTimeInMillis());
            datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        } else if (dateTypeId == 2) {
            datePickerDialog.show();

        } else if (dateTypeId == 3) {
            datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());

        }

        datePickerDialog.show();
    }

    private void getAttendanceDetails() {
        final ProgressDialog progressDialog = new ProgressDialog(AttendanceDetailsForCCActivity.this);
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.getD2DClient().create(ApiInterface.class);
        Call<AttendanceDetailsModel> call = apiService.getAttendanceDetails(selectedApptDate, teamId, campId, statusType);
        call.enqueue(new Callback<AttendanceDetailsModel>() {
            @Override
            public void onResponse(Call<AttendanceDetailsModel> call, Response<AttendanceDetailsModel> response) {

                try {

                    String status = response.body().getStatus();
                    String message = response.body().getMessage();
                    if (response.isSuccessful()) {
                        progressDialog.dismiss();
                        if (status.equalsIgnoreCase("Success")) {

                            binding.tvUploadedInTime.setVisibility(View.GONE);
                            binding.tvUploadedOutTime.setVisibility(View.GONE);
                            binding.tvUploadedOutTime.setText("");
                            binding.tvUploadedInTime.setText("");

                            binding.llMainBtnUpload.setVisibility(View.VISIBLE);


                            if (camptype.equals("3") || camptype.equals("6")) {
                                binding.llMainBtn.setVisibility(View.VISIBLE);

                            }


                            if ((DESGID.equals("29") || DESGID.equals("162") || DESGID.equals("92") || DESGID.equals("139") || DESGID.equals("136"))) {

                                binding.llMainBtnUpload.setVisibility(View.GONE);
                                binding.llMainBtn.setVisibility(View.GONE);


                            }


                            attandanceDeatilsList = response.body().getOutput();
                            AttendanceDetailsAdapter attendanceDetailsAdapter = new AttendanceDetailsAdapter(AttendanceDetailsForCCActivity.this, attandanceDeatilsList);
                            binding.rvPatientList.setAdapter(attendanceDetailsAdapter);

//
//                            String inTime = "";
//                            String outTime = "";
//
//
//                            for (AttendanceDetailsModel.Output output :
//                                    attandanceDeatilsList) {
//
//                                inTime = output.getInTime();
//                                outTime = output.getOuttime();
//                            }
//
//
//
//
//                            if (inTime.equalsIgnoreCase("Pending")) {
//                                Utilities.showAlertDialog(mContext, "Alert", "काही टीम सदस्यांनी उपस्थिती नोंदवलेली नाही", false);
//                                binding.tvCheckIn.setEnabled(false);
//                                binding.tvCheckOut.setEnabled(false);
//
//                                return;
//                            }else{
//                                isMarkInOut = "1";
//
//                                Log.d(TAG, "attendance Flag: " + isMarkInOut);
//
//                            }
//
//                            if (outTime.equalsIgnoreCase("Pending")) {
//                                Utilities.showAlertDialog(mContext, "Alert", "काही टीम सदस्यांनी Out Time नोंदवलेली नाही", false);
//                                binding.tvCheckOut.setEnabled(false);
//                            }else {
//                                isMarkInOut = "2";
//
//                                Log.d(TAG, "attendance Flag: " + isMarkInOut);
//
//                            }

                            int pendingInCount = 0;
                            int pendingOutCount = 0;

                            for (AttendanceDetailsModel.Output output : attandanceDeatilsList) {

                                if ("Pending".equalsIgnoreCase(output.getInTime())) {
                                    pendingInCount++;
                                }

                                if ("Pending".equalsIgnoreCase(output.getOuttime())) {
                                    pendingOutCount++;
                                }
                            }


                            if (pendingInCount >= 1) {
//                                Utilities.showAlertDialog(
//                                        mContext,
//                                        "Alert",
//                                        "काही टीम सदस्यांनी उपस्थिती नोंदवलेली नाही",
//                                        false
//                                );

                                binding.llMainBtnUpload.setVisibility(View.GONE);
                                binding.llMainBtn.setVisibility(View.GONE);

                                return;
                            } else {
                                isMarkInOut = "1";


                                Log.d(TAG, "attendance Flag: " + isMarkInOut);
                            }


                            if (pendingOutCount >= 1) {

//                                Utilities.showAlertDialog(
//                                        mContext,
//                                        "Alert",
//                                        "काही टीम सदस्यांनी Out Time नोंदवलेली नाही",
//                                        false
//                                );


                            } else {
                                isMarkInOut = "2";
                                Log.d(TAG, "attendance Flag: " + isMarkInOut);
                            }


                            getImages();


                        } else {
                            AttendanceDetailsAdapter attendanceDetailsAdapter = new AttendanceDetailsAdapter(AttendanceDetailsForCCActivity.this, new ArrayList<>());
                            binding.rvPatientList.setAdapter(attendanceDetailsAdapter);
                            Utilities.showAlertDialog(mContext, "Alert", "Data Not Found", false);


                        }
                    } else {

                        Utilities.showAlertDialog(mContext, "Alert", message, false);

                    }
                } catch (Exception e) {

                    Utilities.showAlertDialog(mContext, "Alert", "Exception", false);
                }

            }

            @Override
            public void onFailure(Call<AttendanceDetailsModel> call, Throwable t) {
                t.getLocalizedMessage();
                progressDialog.dismiss();


            }
        });

    }


    private void getImages() {
        final ProgressDialog progressDialog = new ProgressDialog(AttendanceDetailsForCCActivity.this);
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.getD2DClient().create(ApiInterface.class);
        Call<AttendanceImageModel> call = apiService.getCampImages(campId);
        call.enqueue(new Callback<AttendanceImageModel>() {
            @Override
            public void onResponse(Call<AttendanceImageModel> call, Response<AttendanceImageModel> response) {
                progressDialog.dismiss();

                try {

                    String status = response.body().getStatus();
                    String message = response.body().getMessage();
                    if (response.isSuccessful()) {
                        if (status.equalsIgnoreCase("Success")) {

                            List<AttendanceImageModel.Output> imageList = response.body().getOutput();

                            if (imageList != null && !imageList.isEmpty()) {

                                AttendanceImageModel.Output output = imageList.get(0);

                                inPhotoPath = output.getInImage();
                                outImagePath = output.getOutImage();
                                campPhotoPath = output.getDuringCampImages();



                                binding.tvOutPhotoStatus.setVisibility(View.GONE);


                                if (inPhotoPath != null && !inPhotoPath.equals("NA")) {
                                    String url = BuildConfig.domain + "/CampTeamAttendance/" + inPhotoPath;
                                    Picasso.with(mContext).load(url).into(binding.imvCheckIn);


                                    binding.imvCheckIn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            startActivity(new Intent().setAction(Intent.ACTION_VIEW).setData(Uri.parse(url)));
                                        }
                                    });
                                }


                                if (outImagePath != null && !outImagePath.equals("NA")) {
                                    String urlOut = BuildConfig.domain + "/CampTeamAttendance/" + outImagePath;
                                    Picasso.with(mContext).load(urlOut).into(binding.imvCheckOut);


                                    binding.imvCheckOut.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            startActivity(new Intent().setAction(Intent.ACTION_VIEW).setData(Uri.parse(urlOut)));
                                        }
                                    });
                                }


                                if (campPhotoPath != null && !campPhotoPath.equals("NA")) {
                                    String urlCamp = BuildConfig.domain + "/CampTeamAttendance/" + campPhotoPath;
                                    Picasso.with(mContext).load(urlCamp).into(binding.imvCampImage);


                                    binding.imvCampImage.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            startActivity(new Intent().setAction(Intent.ACTION_VIEW).setData(Uri.parse(urlCamp)));
                                        }
                                    });
                                }

                                if (inPhotoPath.equals("NA") && isMarkInOut.equals("1")) {

//                                    Utilities.showAlertDialog(
//                                            mContext,
//                                            "Alert",
//                                            "कॅम्प सुरू करण्यासाठी टीमचा फोटो अपलोड करा.",
//                                            false
//                                    );


                                    if (!Utilities.isDateBeforeCurrentDate(selectedApptDate)) {


                                        if (!(DESGID.equals("29") || DESGID.equals("162") || DESGID.equals("92") || DESGID.equals("139") || DESGID.equals("136"))) {
                                            warningDialogue();

                                        }

                                    }


                                }


                                if (!inPhotoPath.equals("NA") && isMarkInOut.equals("1")) {

                                    if (output.getIsInImageApproved().equals("Approved")){

//                                        Utilities.showAlertDialog(
//                                                mContext,
//                                                "Alert",
//                                                "काही कॅम्प टीम सदस्यांनी out attendance मार्क केलेली नाही. \n कॅम्प close करण्यापूर्वी out attendance मार्क करा व टीमचा फोटो अपलोड करा.",
//                                                false
//                                        );
                                    }

                                    binding.llMainBtnUpload.setVisibility(View.GONE);
                                    binding.llMainBtn.setVisibility(View.GONE);

                                }

                                if (outImagePath.equals("NA") && isMarkInOut.equals("2")) {

//                                    Utilities.showAlertDialog(
//                                            mContext,
//                                            "Alert",
//                                            "कॅम्प closing confirmation देण्यापूर्वी टीमचा फोटो अपलोड करा.",
//                                            false
//                                    );

                                }

                                assert inPhotoPath != null;
                                if (!inPhotoPath.equals("NA")) {
                                    binding.tvUploadedInTime.setText("Uploaded On :" + output.getInImageUploadedOn());
                                    binding.tvUploadedInTime.setVisibility(View.VISIBLE);
                                }

                                assert outImagePath != null;
                                if (!outImagePath.equals("NA")) {
                                    binding.tvUploadedOutTime.setText("Uploaded On :" + output.getOutImageUploadedOn());
                                    binding.tvUploadedOutTime.setVisibility(View.VISIBLE);
                                }

                                assert campPhotoPath != null;
                                if (!campPhotoPath.equals("NA")) {
                                    binding.tvUploadedCampTime.setText("Uploaded On :" + output.getCampImagesUploadedOn());
                                    binding.tvUploadedCampTime.setVisibility(View.VISIBLE);
                                }


                                switch (output.getIsInImageApproved()) {
                                    case "Pending" -> binding.llMainIn.setVisibility(View.VISIBLE);
                                    case "Approved" -> {
                                        binding.llMainIn.setVisibility(View.GONE);
                                        binding.tvInPhotoStatus.setVisibility(View.VISIBLE);
                                        binding.tvInPhotoStatus.setText("Photo Approved");
                                        binding.tvInPhotoStatus.setTextColor(ContextCompat.getColor(mContext, R.color.green));
                                    }
                                    case "Rejected" -> {
                                        binding.llMainIn.setVisibility(View.GONE);
                                        binding.tvInPhotoStatus.setText("Photo Rejected");
                                        binding.tvInPhotoStatus.setVisibility(View.VISIBLE);
                                        binding.tvInPhotoStatus.setTextColor(ContextCompat.getColor(mContext, R.color.red));
                                    }

                                }


                                switch (output.getIsDuringImageApproved()) {
                                    case "Pending" ->
                                            binding.llMainCamp.setVisibility(View.VISIBLE);
                                    case "Approved" -> {
                                        binding.llMainCamp.setVisibility(View.GONE);
                                        binding.tvCampPhotoStatus.setVisibility(View.VISIBLE);
                                        binding.tvCampPhotoStatus.setText("Photo Approved");
                                        binding.tvCampPhotoStatus.setTextColor(ContextCompat.getColor(mContext, R.color.green));
                                    }
                                    case "Rejected" -> {
                                        binding.llMainCamp.setVisibility(View.GONE);
                                        binding.tvCampPhotoStatus.setText("Photo Rejected");
                                        binding.tvCampPhotoStatus.setVisibility(View.VISIBLE);
                                        binding.tvCampPhotoStatus.setTextColor(ContextCompat.getColor(mContext, R.color.red));
                                    }

                                }


                                switch (output.getIsOutImageApproved()) {
                                    case "Pending" -> binding.llMainOut.setVisibility(View.VISIBLE);
                                    case "Approved" -> {
                                        binding.llMainOut.setVisibility(View.GONE);
                                        binding.tvOutPhotoStatus.setVisibility(View.VISIBLE);
                                        binding.tvOutPhotoStatus.setText("Photo Approved");
                                        binding.tvOutPhotoStatus.setTextColor(ContextCompat.getColor(mContext, R.color.green));
                                    }
                                    case "Rejected" -> {
                                        binding.llMainOut.setVisibility(View.GONE);
                                        binding.tvOutPhotoStatus.setText("Photo Rejected");
                                        binding.tvOutPhotoStatus.setVisibility(View.VISIBLE);
                                        binding.tvOutPhotoStatus.setTextColor(ContextCompat.getColor(mContext, R.color.red));
                                    }

                                }


//                                if (inPhotoPath.equals("NA")) {
//                                    binding.llMainIn.setVisibility(View.GONE);
//                                } else {
////                                    binding.llMainIn.setVisibility(View.VISIBLE);
//
//                                }
//                                if (outImagePath.equals("NA")) {
//                                    binding.llMainOut.setVisibility(View.GONE);
//                                } else {
////                                    binding.llMainOut.setVisibility(View.VISIBLE);
//
//                                }
//
//
//                                if (campPhotoPath.equals("NA")) {
//                                    binding.llMainCamp.setVisibility(View.GONE);
//                                } else {
////                                    binding.llMainCamp.setVisibility(View.VISIBLE);
//
//                                }


                                if (!"NA".equals(inPhotoPath) && !"NA".equals(outImagePath)) {
                                    binding.llMainBtnUpload.setVisibility(View.GONE);


                                    if (!(DESGID.equals("29") || DESGID.equals("162") || DESGID.equals("92") || DESGID.equals("139") || DESGID.equals("136"))) {
                                        if (binding.rbD2d.isChecked() || camptype.equals("3") || camptype.equals("6")) {




                                            if (output.getIsOutImageApproved().equals("Approved") && output.getIsDuringImageApproved().equals("Approved")){
                                                binding.llMainBtn.setVisibility(View.VISIBLE);
                                                binding.btnSaveCampClosingConfirmation.setEnabled(true);

                                            }



                                            if (CampConfirmation.equals("1")) {
                                                binding.llMainBtn.setVisibility(View.GONE);

                                            }


                                        }
                                    }


                                } else {
//                                    binding.btnSave.setVisibility(View.VISIBLE);
                                }


                                if ((DESGID.equals("29") || DESGID.equals("162") || DESGID.equals("92") || DESGID.equals("139") || DESGID.equals("136"))) {

                                    binding.llMainBtnUpload.setVisibility(View.GONE);
                                    binding.llMainBtn.setVisibility(View.GONE);

                                }

                            }


                        } else {

//                            Utilities.showAlertDialog(mContext, "Alert", "Data Not Found", false);


                        }
                    } else {

                        Utilities.showAlertDialog(mContext, "Alert", message, false);

                    }
                } catch (Exception e) {

                    progressDialog.dismiss();

                    Utilities.showAlertDialog(mContext, "Alert", "Exception", false);
                }

            }

            @Override
            public void onFailure(Call<AttendanceImageModel> call, Throwable t) {
                t.getLocalizedMessage();
                progressDialog.dismiss();


            }
        });

    }

    private void getCampId() {
        final ProgressDialog progressDialog = new ProgressDialog(AttendanceDetailsForCCActivity.this);
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.getD2DClient().create(ApiInterface.class);
        Call<CampListModel> call = apiService.getCampId(EmpCode, selectedApptDate, camptype);
        call.enqueue(new Callback<CampListModel>() {
            @Override
            public void onResponse(Call<CampListModel> call, Response<CampListModel> response) {
                progressDialog.dismiss();

                if (response.isSuccessful()) {
                    String status = response.body().getStatus();
                    String message = response.body().getMessage();
                    if (status.equalsIgnoreCase("Success")) {
                        List<CampListModel.OutputBean> outputItems = response.body().getOutput();

                        if (outputItems.size() > 0) {
                            showCampId(outputItems);
                        }
                    } else {
                        Utilities.showAlertDialog(mContext, "Alert", message, false);

                    }

                } else {
                    Utilities.showAlertDialog(mContext, "Error", "Server Error: " + response.code(), false);

                }
            }

            @Override
            public void onFailure(Call<CampListModel> call, Throwable t) {
                t.getLocalizedMessage();
                progressDialog.dismiss();

            }
        });

    }


    private void showTrenchListDialog(final List<YearModel.Output> trenchList) {

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(AttendanceDetailsForCCActivity.this);
        builderSingle.setTitle("Select Year");
        builderSingle.setCancelable(false);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(AttendanceDetailsForCCActivity.this, R.layout.list_row);
        for (int i = 0; i < trenchList.size(); i++) {
            arrayAdapter.add(trenchList.get(i).getYearName());
        }
        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

//                binding.tvYear.setText(trenchList.get(which).getYearName());
                yearid = String.valueOf(trenchList.get(which).getYearID());


            }
        });

        AlertDialog alertDialog = builderSingle.create();
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialog.show();
    }


    private class GetTeamId extends AsyncTask<String, Void, String> {

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

            List<ParamsPojo> param = new ArrayList<>();
            param.add(new ParamsPojo("campid", campId));
            param.add(new ParamsPojo("UserID", String.valueOf(EmpCode)));
            res = WebServiceCall.APICall(ApplicationConstants.GetTeamNumberByCampIdAndUSerId, ApplicationConstants.webservice_d2d, param);

            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            try {
                if (!result.equals("")) {

                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("output");

                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        String teamIds = jsonObject1.getString("TeamNumber");
                        String teamName = jsonObject1.getString("TeamName");
                        teamId = teamIds;
                        TeamName = teamName;


                        binding.tvTeam.setText(TeamName);
//                        binding.tvTeam.setEnabled(false);


                        getAttendanceDetails();


                    } else {
                        Utilities.showAlertDialog(mContext, "Error", jsonObject.getString("message"), false);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                //  Utilities.showAlertDialog(mContext, "Please Try Again", "Server Not Responding", false);
            }
        }
    }

    private void showCampId(final List<CampListModel.OutputBean> trenchList) {

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(AttendanceDetailsForCCActivity.this);
        builderSingle.setTitle("Select CampId");
        builderSingle.setCancelable(false);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(AttendanceDetailsForCCActivity.this, R.layout.list_row);
        for (int i = 0; i < trenchList.size(); i++) {
            arrayAdapter.add(trenchList.get(i).getCampId());
        }
        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                binding.tvCampId.setText(trenchList.get(which).getCampId());
                campId = String.valueOf(trenchList.get(which).getCampId());
                CampConfirmation = String.valueOf(trenchList.get(which).getCampConfirmation());


                binding.tvTeam.setText("");
                clearData();


                if (camptype.equalsIgnoreCase("1")) {

                    getAttendanceDetails();
                } else if (camptype.equalsIgnoreCase("3")) {

                    if (!(DESGID.equals("29") || DESGID.equals("162") || DESGID.equals("92") || DESGID.equals("139") || DESGID.equals("136"))) {
                        if (Utilities.isNetworkAvailable(mContext)) {
                            new GetTeamId().execute(campId, EmpCode);
                        } else {
                            Utilities.showToastMessage("Please Check Your Connection", mContext, false);
                        }
                    }

                }


//                if (DESGID.equals("29")||DESGID.equals("162")||DESGID.equals("92")){
//
//                }

            }
        });

        AlertDialog alertDialog = builderSingle.create();
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialog.show();
    }

    @Override
    public void onDataClick(InvoiceGenModel.Output item) {


    }


    ActivityResultLauncher<Intent> cameraLauncherForPatientPhoto =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            String fileURI = data.getStringExtra("fileURI");
                            String fName = data.getStringExtra("fName");
                            Log.d(TAG, ":fileURI " + fileURI);
                            Log.d(TAG, ":fName " + fName);
                            try {

                                String filename = (int) (Math.random() * 99999 + 1) + "_IN";
                                String compressedFilePath = Utilities.compressImageNew(mContext, fileURI, filename);
                                saveFileAI(compressedFilePath);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });


    ActivityResultLauncher<Intent> cameraLauncherForPatientIDCard =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            String fileURI = data.getStringExtra("fileURI");
                            String fName = data.getStringExtra("fName");
                            Log.d(TAG, ":fileURI " + fileURI);
                            Log.d(TAG, ":fName " + fName);
                            try {

                                String filename = (int) (Math.random() * 99999 + 1) + "_OT";
                                String compressedFilePath = Utilities.compressImageNew(mContext, fileURI, filename);
                                saveFileAIIDCard(compressedFilePath);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });


    @SuppressLint("MissingPermission")
    public void saveFileAI(String filePath) {

        String destinationFilename = filePath;
        patientPicBm = BitmapFactory.decodeFile(destinationFilename);
        String fName = System.currentTimeMillis() + "_IN.jpg";
        String outputFilePath = getExternalCacheDir() + fName;
        // Step 2: Compress the Bitmap and store it in a new file
        try {
            // Open a file output stream to save the compressed image
            FileOutputStream fos = new FileOutputStream(outputFilePath);

            // Compress the bitmap as JPEG with 80% quality
            patientPicBm.compress(Bitmap.CompressFormat.JPEG, 80, fos);

            // Close the output stream
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        binding.imvCheckIn.setImageBitmap(patientPicBm);
        patientImagePath = outputFilePath;

        binding.btnSave.setText("Upload Check In Image");


    }


    @SuppressLint("MissingPermission")
    public void saveFileAIIDCard(String filePath) {
        String destinationFilename = filePath;
        ackPicBm = BitmapFactory.decodeFile(destinationFilename);
        String fName = System.currentTimeMillis() + "_OT.png";
        String outputFilePath = getExternalCacheDir() + fName;
        // Step 2: Compress the Bitmap and store it in a new file
        try {
            // Open a file output stream to save the compressed image
            FileOutputStream fos = new FileOutputStream(outputFilePath);

            // Compress the bitmap as JPEG with 80% quality
            ackPicBm.compress(Bitmap.CompressFormat.JPEG, 80, fos);

            // Close the output stream
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        binding.imvCheckOut.setImageBitmap(ackPicBm);
        ackImagePath = outputFilePath;

        binding.btnSave.setText("Upload Check Out Image");
    }


    public class GetTeamDetailsListForAssign extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("campid", params[0]));
            param.add(new ParamsPojo("CampDate", params[1]));
            param.add(new ParamsPojo("CampType", params[2]));


            // res = WebServiceCall.APICall(ApplicationConstants.GetAssignedTeamDetails, ApplicationConstants.webservice_d2d, param);
            res = WebServiceCall.APICall(ApplicationConstants.GetAssignedTeamDetailsFlexi, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    assignteamlist = new ArrayList<>();
                    ArrayList<TeamsDetailsModel.OutputBean> assignteamlist = new ArrayList<>();
                    TeamsDetailsModel teamsDetailsModel = new Gson().fromJson(result, TeamsDetailsModel.class);
                    type = teamsDetailsModel.getStatus();
                    message = teamsDetailsModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        assignteamlist = teamsDetailsModel.getOutput();
                        if (assignteamlist.size() > 0) {
                            //  districtList.add(0, new DistrictList_Model("0", "All"));

                            showAssignedTeamListDialog(assignteamlist);

                            // ll_assigned.setVisibility(View.VISIBLE);


                        } else {
                            Utilities.showToastMessage(R.string.msgt_emptylist, mContext, false);
                        }
                    } else {
                        Utilities.showAlertDialog(mContext, "Fail", "Team not assigned for selected campId, assign team first", false);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(mContext, "Please select CampId", "You have to select campId", false);
            }
        }
    }


    private void showAssignedTeamListDialog(final ArrayList<TeamsDetailsModel.OutputBean> assignteamlist) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(mContext);
        builderSingle.setTitle("Select Team");
        builderSingle.setCancelable(false);
        View dialogueView = getLayoutInflater().inflate(R.layout.recyclerview_dialogue, null);
        builderSingle.setView(dialogueView);

        RecyclerView rvList = dialogueView.findViewById(R.id.rvList);
        EditText edt_search = dialogueView.findViewById(R.id.edt_search);
        rvList.setHasFixedSize(true);
        rvList.setLayoutManager(new LinearLayoutManager(mContext));

        rvList.setAdapter(new TeamsDetailsAssignedAdapter(assignteamlist));

        rvList.addOnItemTouchListener(
                new RecyclerItemClickListener(mContext,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, final int position) {
                                TeamsDetailsModel.OutputBean team = assignteamlist.get(position);
                                teamId = assignteamlist.get(position).getTeamNumber();

                                binding.tvTeam.setText(assignteamlist.get(position).getTeamName());
                                teamDialog.dismiss();


                                getAttendanceDetails();
                            }
                        }));

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count) {

                if (query.toString().isEmpty()) {
                    rvList.setAdapter(new TeamsDetailsAssignedAdapter(assignteamlist));
                    return;
                }

                if (teamList.size() == 0) {
                    rvList.setVisibility(View.GONE);
                    return;
                }

                if (!query.toString().equals("")) {
                    ArrayList<TeamsDetailsModel.OutputBean> searchedTestList = new ArrayList<>();
                    for (TeamsDetailsModel.OutputBean clientDetails : assignteamlist) {

                        String countryToBeSearched = clientDetails.getMember1().toLowerCase();
                        String member2 = clientDetails.getMember2().toLowerCase();

                        if (countryToBeSearched.contains(query.toString().toLowerCase()) ||
                                member2.contains(query.toString().toLowerCase())) {
                            searchedTestList.add(clientDetails);
                        }
                    }
                    rvList.setAdapter(new TeamsDetailsAssignedAdapter(searchedTestList));
                } else {
                    rvList.setAdapter(new TeamsDetailsAssignedAdapter(assignteamlist));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        teamDialog = builderSingle.create();
        teamDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                int numOfTeams = 0;
                for (TeamsDetailsModel.OutputBean team : assignteamlist
                ) {
                    if (team.isChecked()) {
                        numOfTeams += 1;
                    }
                }
                Log.d("TAG", "onClick: " + numOfTeams);

            }
        });


        teamDialog.show();

    }


    private void getTestCompleteFlag() {
        final ProgressDialog progressDialog = new ProgressDialog(AttendanceDetailsForCCActivity.this);
        Log.d(TAG, "getMachineStatusList: " + EmpCode);
        progressDialog.setMessage("Please wait . . . " + EmpCode);
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.getD2DClient().create(ApiInterface.class);
        Call<AvailabilityTestModel> call = apiService.getTestCompleteFlag(EmpCode);
        call.enqueue(new Callback<AvailabilityTestModel>() {
            @Override
            public void onResponse(Call<AvailabilityTestModel> call, Response<AvailabilityTestModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    String status = response.body().getStatus();
                    String message = response.body().getMessage();
                    if (status.equalsIgnoreCase("Success")) {
                        List<AvailabilityTestModel.Output> machineStatusList = response.body().getOutput();


                        if (machineStatusList != null && !machineStatusList.isEmpty()) {
                            AvailabilityTestModel.Output output = machineStatusList.get(0);


                            testFlag = output.getTestFlag();

                            Log.d(TAG, "test Flag: " + testFlag);

                        }

                    } else {
                        testFlag = "1";
//                        Utilities.showAlertDialog(context, "Alert", message, false);

                        Log.d(TAG, "test Flag: " + testFlag);


                    }
                } else {
//                    Utilities.showAlertDialog(context, "Alert", "Details Not Found", false);

                }
            }

            @Override
            public void onFailure(Call<AvailabilityTestModel> call, Throwable t) {
                t.getLocalizedMessage();
                progressDialog.dismiss();

            }
        });

    }


    private void insertData() {

        pd.setMessage("Uploading File..");
        pd.setCancelable(false);
        pd.show();

        MultipartBody.Part inImagePart = null;
        MultipartBody.Part outImagePart = null;


        // 1 = IN, 2 = OUT (adjust if your logic differs)
        if ("1".equals(isMarkInOut)) {

            // IN attendance
            File uploadFileIn = new File(patientImagePath);
            if (uploadFileIn.exists()) {
                inImagePart = MultipartBody.Part.createFormData(
                        "InImagePath",
                        uploadFileIn.getName(),
                        RequestBody.create(uploadFileIn, MediaType.parse("multipart/form-data"))
                );
            }

        } else if ("2".equals(isMarkInOut)) {

            // OUT attendance
            File uploadFileOut = new File(ackImagePath);
            if (uploadFileOut.exists()) {
                outImagePart = MultipartBody.Part.createFormData(
                        "OutImagePath",
                        uploadFileOut.getName(),
                        RequestBody.create(uploadFileOut, MediaType.parse("multipart/form-data"))
                );
            }
        }


        apiInterface.insertAttendance(
                MultipartBody.Part.createFormData("CampID", campId),
                inImagePart,     // 👈 null when OUT
                outImagePart,    // 👈 null when IN
                MultipartBody.Part.createFormData("UserId", EmpCode),
                MultipartBody.Part.createFormData("StatusID", isMarkInOut)
        ).enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                pd.dismiss();

                try {
                    String res = response.body() != null ? response.body().string() : "";
                    Log.d(TAG, "onResponse: " + res);

                    JSONObject json = new JSONObject(res);
                    String status = json.getString("status");
                    String message = json.getString("message");

                    if ("Success".equalsIgnoreCase(status)) {

                        Utilities.showAlertDialog(
                                AttendanceDetailsForCCActivity.this,
                                status,
                                isMarkInOut.equals("1")
                                        ? "टीमचा फोटो यशस्वीरित्या अपलोड झाला आहे, तुम्ही आता कॅम्प सुरू करू शकता."
                                        : "टीमचा फोटो यशस्वीरित्या अपलोड झाला आहे, तुम्ही आता कॅम्प क्लोजिंग कन्फर्मेशन देऊ शकता.",
                                true,
                                "Okay",
                                (dialog, which) -> finish()
                        );


                    } else {
                        Utilities.showAlertDialog(
                                AttendanceDetailsForCCActivity.this,
                                status,
                                message,
                                false
                        );
                    }

                } catch (Exception e) {
                    Log.e(TAG, "onResponse: " + e.getMessage());
                    Utilities.showAlertDialog(
                            AttendanceDetailsForCCActivity.this,
                            "Fail",
                            e.getMessage(),
                            false
                    );
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                pd.dismiss();

                Log.e(TAG, "onFailure: " + t.getMessage());
                Utilities.showAlertDialog(
                        AttendanceDetailsForCCActivity.this,
                        "Fail",
                        t.getMessage(),
                        false
                );
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(broadcastReceiver,
                        new IntentFilter("refresh_exp_benf_list"));
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        LocalBroadcastManager.getInstance(this)
//                .unregisterReceiver(broadcastReceiver);
//    }


    private void insertPhotoFlag(String status, String approvalStatus) {

        final ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiInterface apiInterface = ApiClient.getD2DClient().create(ApiInterface.class);
        apiInterface.insertApproveReject(status, campId, approvalStatus, EmpCode).enqueue(new Callback<ResponseBody>() {

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


                            Utilities.showAlertDialog(
                                    mContext,
                                    status,
                                    approvalStatus.equals("1")
                                            ? "फोटो approve करण्यात आलेला आहे."
                                            : "फोटो reject करण्यात आलेला आहे.",
                                    true,
                                    "Okay",
                                    (dialog, which) -> {
                                        dialog.dismiss();
                                        getImages();
                                    }
                            );


                        } else {
                            Utilities.showAlertDialog(mContext, "Alert", message, false);
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
}




