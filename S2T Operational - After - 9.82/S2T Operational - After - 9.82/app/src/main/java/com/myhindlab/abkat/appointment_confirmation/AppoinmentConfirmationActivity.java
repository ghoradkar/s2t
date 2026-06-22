package com.myhindlab.abkat.appointment_confirmation;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.myhindlab.abkat.activities.doortodoor.D2DSelectCampActivity;
import com.myhindlab.abkat.adapters.Dependent_Adapter;
import com.myhindlab.abkat.adapters.Screened_Dependent_Adapter;
import com.myhindlab.abkat.appointment_confirmation.callstatus.CallingRemarkModel;
import com.myhindlab.abkat.appointment_confirmation.callstatus.CallstatusResponse;
import com.myhindlab.abkat.appointment_confirmation.callstatus.OutputItem;
import com.myhindlab.abkat.appointment_confirmation.insertapi.ResponseModel;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.databinding.ActivityAppoinmentConfirmationBinding;
import com.myhindlab.abkat.models.CampTypeModel;
import com.myhindlab.abkat.models.DependentModel;
import com.myhindlab.abkat.models.DistrictList_Model;
import com.myhindlab.abkat.models.ScreenedDependentModel;
import com.myhindlab.abkat.models.TalukaModel;
import com.myhindlab.abkat.models.doortodoor.BeneficiaryDetailsCallingModuleModel;
import com.myhindlab.abkat.models.doortodoor.D2dWorkingTeamModel;
import com.myhindlab.abkat.pojos.DistrictList_Pojo;
import com.myhindlab.abkat.pojos.doortodoor.RelationPojo;
import com.myhindlab.abkat.rest.ApiClient;
import com.myhindlab.abkat.rest.ApiInterface;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppoinmentConfirmationActivity extends AppCompatActivity implements com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener, Dependent_Adapter.DependentEvents {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    String outputDate = "";
    String no_ofdepartment = "0", b_name, screenedDependents = "0", versionNumber, date, fname, mname, lname, CallingLog, time, b_mobile, genderId, b_address, remarkId, phleboremarkId, remark, AssignCallID, IsAddressChanged, pendingName, check_status = "0", LabCode, relationId = "0", gender, marriedStatusId = "0",
            EmpCode, taluka, STATELGDCODE = "2", DISTLGDCODE = "0", screeningType, DESGID, district, TALLGDCODE = "0";
    int call_statusid, differenceCount, noOfPendingScreeningDependant;


    private ArrayList<DistrictList_Model> districtList;

    private List<D2dWorkingTeamModel.Output> notWorkingList;

    private List<BeneficiaryDetailsCallingModuleModel.Output> adminlist;

    private ArrayList<DependentModel> dependentList;
    private List<ScreenedDependentModel.Output> screenedDependentModelList;

    boolean is24HView = false;
    int selectedHour = 10;
    int selectedMinute = 20;

    int t1Hour, t1Minute, t2Hour, t2Monute, lastSelectedHour, lastSelectedMinute;

    private ArrayList<DistrictList_Model> districtList_models;
    private List<TalukaModel.Output> talukatList_models;

    private ProgressDialog pd;
    private int flag = 1;

    private Context context;
    ArrayList<String> staticList = new ArrayList<>();
    ActivityAppoinmentConfirmationBinding binding;
    private UserSessionManager session;
    private com.wdullaer.materialdatetimepicker.time.TimePickerDialog timePickerDialog;

    private JSONArray user_info;
    String time24;
    com.myhindlab.abkat.appointment_confirmation.pojo.OutputItem beneficiary;
    String selectedApptDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_appoinment_confirmation);
        context = AppoinmentConfirmationActivity.this;
        setUpToolBarr();
        session = new UserSessionManager(AppoinmentConfirmationActivity.this);
        // session = new UserSessionManager(context);
        pd = new ProgressDialog(context);
        Intent intent = getIntent();
        binding.rvDependent.setLayoutManager(new LinearLayoutManager(this));
        binding.rvDependent.setHasFixedSize(true);

        binding.rvDependentScreened.setLayoutManager(new LinearLayoutManager(this));
        binding.rvDependentScreened.setHasFixedSize(true);


        beneficiary = (com.myhindlab.abkat.appointment_confirmation.pojo.OutputItem) intent.getSerializableExtra("beneficiary");
        flag = intent.getIntExtra("flag", 1);
        b_name = beneficiary.getBeneficiaryName() + " " + "[" + " " + beneficiary.getAge() + " " + "]";
        b_address = beneficiary.getRegAddress();
//        gender = beneficiary.getGender();
        b_mobile = beneficiary.getMobile();
        AssignCallID = String.valueOf(beneficiary.getAssignCallID());
        binding.tvName.setText(b_name);


//        if (beneficiary.getDependantScreeningPending() != null){
//            if (beneficiary.getDependantScreeningPending().equalsIgnoreCase("0")){
//               binding.edtCallstatus.setEnabled(false);
//               binding.edtCallstatus.setText("Denied For Screening");
//               AssignCallID = "3";
//            }
//
//        };

//        if (beneficiary.getGender().equalsIgnoreCase("")){
//            binding.edtWorkerGender.setEnabled(true);
//        }else {
//            binding.edtWorkerGender.setEnabled(false);


//        }
        ;


        binding.etAltMobilenumber.setText(beneficiary.getAltMobileNo());


        // binding.edtCallstatus.setText(beneficiary.getAssignStatus());
        // IsAddressChanged = beneficiary.getIsAddressChanged();
//        binding.etDate.setText(beneficiary.getAppoinmentDate());
//        binding.etTime.setText(beneficiary.getAppoinmentTime());


//        binding.etRemark.setText(beneficiary.getRemark());

        call_statusid = beneficiary.getAssignStatusID();
        binding.edtCallstatus.setText(beneficiary.getCallingStatus());

//        if (call_statusid == 2) {
//            binding.edtCallstatus.setText("Booking Confirmed");
//            binding.llApptDateTime.setVisibility(View.VISIBLE);
//
//        } else if (call_statusid == 3) {
//            binding.edtCallstatus.setText("Denied For Screening ");
//            binding.llApptDateTime.setVisibility(View.GONE);
//
//        } else if (call_statusid == 4) {
//            binding.edtCallstatus.setText("Incorrect Number");
//            binding.llApptDateTime.setVisibility(View.GONE);
//
//        } else if (call_statusid == 5) {
//            binding.edtCallstatus.setText("Call Not Picked");
//            binding.llApptDateTime.setVisibility(View.GONE);
//
//
//        } else if (call_statusid == 6) {
//            binding.edtCallstatus.setText("Out Of Coverage");
//            binding.llApptDateTime.setVisibility(View.GONE);
//
//
//        } else if (call_statusid == 7) {
//            binding.edtCallstatus.setText("Switched Off");
//            binding.llApptDateTime.setVisibility(View.GONE);
//
//
//        } else if (call_statusid == 8) {
//            binding.edtCallstatus.setText("Invalid Number");
//            binding.llApptDateTime.setVisibility(View.GONE);
//
//
//        } else if (call_statusid == 9) {
//            binding.edtCallstatus.setText("Number Busy");
//            binding.llApptDateTime.setVisibility(View.GONE);
//
//
//        } else if (call_statusid == 11) {
//            binding.edtCallstatus.setText("Family Registration Covered");
//            binding.llApptDateTime.setVisibility(View.GONE);
//
//        }else if (call_statusid == 12){
//            binding.edtCallstatus.setText("Call Back Later");
//            binding.llApptDateTime.setVisibility(View.GONE);
//        }

        if (call_statusid == 5 || call_statusid == 4 || call_statusid == 6 || call_statusid == 7 || call_statusid == 8 || call_statusid == 9 || call_statusid == 12 || call_statusid == 13) {
            binding.Checkbox.setEnabled(false);
            binding.tvPersonalDependant.setEnabled(false);
            binding.etDate.setEnabled(false);
            binding.etTime.setEnabled(false);
            binding.edtWorkerMarrageStatus.setEnabled(false);
            binding.edtNoofdepartment.setEnabled(false);
            binding.edtSceeendepartment.setEnabled(false);
            // binding.edtWorkerGender.setEnabled(false);

        } else {
            binding.Checkbox.setEnabled(true);
            binding.tvPersonalDependant.setEnabled(true);
            binding.etDate.setEnabled(true);
            binding.etTime.setEnabled(true);
            binding.edtWorkerMarrageStatus.setEnabled(true);
            binding.edtNoofdepartment.setEnabled(true);
            binding.edtSceeendepartment.setEnabled(true);
            // binding.edtWorkerGender.setEnabled(true);
        }
//        else if (call_statusid == 10){
//            binding.edtCallstatus.setText("Appointment Missed");
//        }


        if (no_ofdepartment != null) {
            no_ofdepartment = beneficiary.getNoOfDependants();
            binding.edtNoofdepartment.setText(no_ofdepartment);
        }


        if (String.valueOf(noOfPendingScreeningDependant) != null) {
            pendingName = beneficiary.getDependantScreeningPending();
            noOfPendingScreeningDependant = Integer.parseInt(beneficiary.getDependantScreeningPending());
            binding.edtSceeendepartment.setText(pendingName);
        }


//        if (beneficiary.getAssignStatusID() == 2 || beneficiary.getAssignStatusID() == 3 || beneficiary.getAssignStatusID() == 4 || beneficiary.getAssignStatusID() == 5 || beneficiary.getAssignStatusID() == 12
//                || beneficiary.getAssignStatusID() == 7 || beneficiary.getAssignStatusID() == 9 || beneficiary.getAssignStatusID() == 8) {
//            //  marriedStatusId = beneficiary.getWorkersMaritalStatus();
//            noOfPendingScreeningDependant = Integer.parseInt(beneficiary.getDependantScreeningPending());
//            if (marriedStatusId != null) {
//
//                if (marriedStatusId.equalsIgnoreCase("1")) {
//                    binding.edtWorkerMarrageStatus.setText("Married");
////                    binding.headerLL.setVisibility(View.VISIBLE);
////                    binding.viewTop.setVisibility(View.VISIBLE);
////                    binding.viewBottom.setVisibility(View.VISIBLE);
//                    binding.mainLLNoOfDependent.setVisibility(View.VISIBLE);
//                    binding.mailPersonalLL.setVisibility(View.VISIBLE);
//
//                } else if (marriedStatusId.equalsIgnoreCase("2")) {
//                    binding.edtWorkerMarrageStatus.setText("UnMarried");
//                } else if (marriedStatusId.equalsIgnoreCase("0")) {
//                    binding.edtWorkerMarrageStatus.setText("");
//                }
//
//            }
//
//
//        }


        binding.etRegmobilenumber.setText(b_mobile);


        binding.etDistrict.setEnabled(false);
        binding.etTaluka.setEnabled(false);
        binding.etHouseNo.setEnabled(false);
        binding.etRoad.setEnabled(false);
        binding.etArea.setEnabled(false);
        binding.etLandmark.setEnabled(false);
        binding.etPincode.setEnabled(false);
        binding.etAltMobilenumber.setEnabled(false);
        binding.etBuildingName.setEnabled(false);

//        binding.inputLayoutDistrict.setBackgroundColor(Color.parseColor("#c0c0c0"));
//        binding.etTaluka.setBackgroundColor(Color.parseColor("#CCCCCC"));
//        binding.etHouseNo.setBackgroundColor(Color.parseColor("#CCCCCC"));
//        binding.etRoad.setBackgroundColor(Color.parseColor("#CCCCCC"));
//        binding.etArea.setBackgroundColor(Color.parseColor("#CCCCCC"));
//        binding.etArea.setBackgroundColor(Color.parseColor("#CCCCCC"));
//        binding.etPincode.setBackgroundColor(Color.parseColor("#CCCCCC"));
//        binding.etAltMobilenumber.setBackgroundColor(Color.parseColor("#CCCCCC"));


        binding.etDistrict.setTextColor(Color.parseColor("#c0c0c0"));
        binding.etTaluka.setTextColor(Color.parseColor("#c0c0c0"));
        binding.etHouseNo.setTextColor(Color.parseColor("#c0c0c0"));
        binding.etRoad.setTextColor(Color.parseColor("#c0c0c0"));
        binding.etArea.setTextColor(Color.parseColor("#c0c0c0"));
        binding.etLandmark.setTextColor(Color.parseColor("#c0c0c0"));
        binding.etPincode.setTextColor(Color.parseColor("#c0c0c0"));
        binding.etRegmobilenumber.setTextColor(Color.parseColor("#c0c0c0"));
        binding.etAltMobilenumber.setTextColor(Color.parseColor("#c0c0c0"));
        binding.etBuildingName.setTextColor(Color.parseColor("#c0c0c0"));


        binding.etDistrict.setText(beneficiary.getDistrict());
        //  DISTLGDCODE = beneficiary.getdISTLGDCODE();
        binding.etTaluka.setText(beneficiary.getTaluka());
        //  TALLGDCODE = beneficiary.gettALLGDCODE();

        binding.edtWorkerGender.setText(gender);
        // binding.edtWorkerGender.setEnabled(false);
        binding.editTextRegister.setText(b_address + "," + beneficiary.getPincode());
        binding.etPincode.setText("" + beneficiary.getPincode());
        binding.etHouseNo.setText(beneficiary.getHouseNo());
        binding.etRoad.setText(beneficiary.getRoad());
        binding.etArea.setText(beneficiary.getArea());
        binding.etLandmark.setText(beneficiary.getLandMark());
        staticList.add("0");
        staticList.add("1");
        staticList.add("2");
        staticList.add("3");
        staticList.add("4");
        staticList.add("5");


        try {
            PackageInfo pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String versionName = pinfo.versionName.trim();
            binding.tvVersionNumber.setText("Version No." + versionName);
            versionNumber = versionName;
        } catch (Exception e) {
            e.printStackTrace();

        }
        //   getDependentList();
        dependentList = new ArrayList<>();


        setDefault();
        eventListener();
        getSessionData();
    }


    private void setDefault() {

//        if (getIntent() != null) {
//
//        }


        new GetBeneficiaryData().execute(AssignCallID);

        getScreeningCount("0");


        if (flag == 2) {
            binding.tvAddress.setVisibility(View.GONE);
            binding.mainllAdress.setVisibility(View.GONE);
            binding.btnRegistration.setVisibility(View.VISIBLE);
            binding.etRemarkOther.setVisibility(View.GONE);
            binding.edtRemarkPhlebo.setVisibility(View.VISIBLE);
            binding.edtWorkerGender.setEnabled(false);
            binding.edtWorkerGender.setVisibility(View.GONE);
            binding.edtWorkerMarrageStatus.setEnabled(false);
            binding.edtNoofdepartment.setEnabled(false);
            binding.edtSceeendepartment.setEnabled(false);
            binding.edtWorkerMarrageStatus.setVisibility(View.GONE);
            binding.tvPersonalDependant.setText("Screening Details");
            binding.btnDependent.setVisibility(View.GONE);

            setUpToolBarr();


            binding.edtRemarkPhlebo.setText(beneficiary.getPhleboRemark());
            phleboremarkId = beneficiary.getPhleboRemarkID();


            if (call_statusid == 2) {
                binding.edtCallstatus.setText("Booking Confirmed");
                binding.llApptDateTime.setVisibility(View.VISIBLE);

            } else if (call_statusid == 10) {
                binding.edtCallstatus.setText("Appointment Missed");
                binding.llApptDateTime.setVisibility(View.GONE);

            } else if (call_statusid == 14) {
                binding.edtCallstatus.setText("Not Available For Screening");
                binding.llApptDateTime.setVisibility(View.GONE);

            }


        }


    }


    private void eventListener() {
        binding.edtCallstatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (flag == 1) {
                    getapicall();

                } else if (flag == 2) {
                    getapicallForPhlebo();
                }

            }
        });
        binding.edtRemarkPhlebo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getRemark();

            }
        });

        binding.etDistrict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDistrictList();
            }
        });

        binding.etRemarkOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getRemark();
            }
        });


        binding.etTaluka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTalukaList();
            }
        });

        binding.etRelation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getRelationList();
            }
        });


        binding.btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(AppoinmentConfirmationActivity.this);
                android.app.AlertDialog alertDialog = null;


                View v = LayoutInflater.from(AppoinmentConfirmationActivity.this).inflate(R.layout.beneficiary_number_dialogue, null, false);
                builder.setView(v);
                builder.setTitle("");

                builder.setPositiveButton("Next", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // finish();

                        startActivity(new Intent(context, D2DSelectCampActivity.class)
                                .putExtra("beneficiaryNumber", beneficiary.getBeneficiaryNo())
                                .putExtra("Type", "7")

                        );


                    }
                });


                EditText beneficiaryEdt = (EditText) v.findViewById(R.id.beneficiaryEdt);
                Button btnSubmit = (Button) v.findViewById(R.id.btnSubmit);


                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Copy text to clipboard
                        String textToCopy = beneficiaryEdt.getText().toString();
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("Beneficiary Number", textToCopy);
                        clipboard.setPrimaryClip(clip);

                        // Show confirmation
                        Toast.makeText(AppoinmentConfirmationActivity.this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
                    }
                });

                alertDialog = builder.create();

                android.app.AlertDialog finalAlertDialog = alertDialog;

                beneficiaryEdt.setText(beneficiary.getBeneficiaryNo());


                alertDialog.show();


            }

        });

        binding.edtWorkerMarrageStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.edtCallstatus.getText().toString().isEmpty()) {
                    Utilities.showAlertDialog(AppoinmentConfirmationActivity.this, "Select Call Status", "Please select call status", false);
                    return;
                }

                ArrayList<CampTypeModel> campTypeModelArrayList = new ArrayList<>();
                campTypeModelArrayList.add(new CampTypeModel("Married", 1));
                campTypeModelArrayList.add(new CampTypeModel("UnMarried", 2));
                campTypeModelArrayList.add(new CampTypeModel("Divorcee", 3));
                campTypeModelArrayList.add(new CampTypeModel("Widow", 4));
                showCampType(campTypeModelArrayList);
            }
        });

        binding.edtWorkerGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<CampTypeModel> genderTypeArrayList = new ArrayList<>();
                genderTypeArrayList.add(new CampTypeModel("Male", 1));
                genderTypeArrayList.add(new CampTypeModel("Female", 2));
                genderTypeArrayList.add(new CampTypeModel("Other", 3));
                showGenderType(genderTypeArrayList);
            }
        });

//        binding.etTime.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if (charSequence.toString().isEmpty()) {
//                    binding.inputLayoutTime.setError("Please enter Appointment TIme");
//
//                } else {
//                    binding.inputLayoutTime.setErrorEnabled(false);
//                }
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });


//        binding.etDistrict.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (Utilities.isNetworkAvailable(context)) {
//                    new GetDistrictList().execute(STATELGDCODE, DISTLGDCODE);
//                } else {
//                    Utilities.showToastMessage("Please Check Your Connection", context, false);
//                }
//            }
//        });


        binding.tvAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                binding.mainllAdress.setVisibility(binding.mainllAdress.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);

                if (binding.mainllAdress.getVisibility() == View.VISIBLE) {
                    binding.tvAddress.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_baseline_keyboard_arrow_up_24), null);
                } else {
                    binding.tvAddress.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_baseline_keyboard_arrow_down_24), null);

                }
            }
        });

        binding.tvPersonalDependant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                binding.mailPersonalLL.setVisibility(binding.mailPersonalLL.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);

                if (binding.mailPersonalLL.getVisibility() == View.VISIBLE) {
                    binding.tvPersonalDependant.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_baseline_keyboard_arrow_up_24), null);
                } else {
                    binding.tvPersonalDependant.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_baseline_keyboard_arrow_down_24), null);

                }
            }
        });

        binding.tvPersonalDependantScreened.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                binding.mainLlScreenedBeneficiary.setVisibility(binding.mainLlScreenedBeneficiary.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);

                if (binding.mainLlScreenedBeneficiary.getVisibility() == View.VISIBLE) {
                    binding.tvPersonalDependantScreened.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_baseline_keyboard_arrow_up_24), null);
                } else {
                    binding.tvPersonalDependantScreened.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_baseline_keyboard_arrow_down_24), null);

                }
            }
        });

//        binding.tvAddDependent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                binding.mainDependentLL.setVisibility(binding.mainDependentLL.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
//
//                if (binding.mainDependentLL.getVisibility() == View.VISIBLE) {
//                    binding.tvAddDependent.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_baseline_keyboard_arrow_up_24), null);
//                } else {
//                    binding.tvAddDependent.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_baseline_keyboard_arrow_down_24), null);
//
//                }
//            }
//        });


//        binding.etDate.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if (charSequence.toString().isEmpty()) {
//                    binding.inputLayoutApptDate.setError("Please enter Appointment Date");
//                } else {
//                    binding.inputLayoutApptDate.setErrorEnabled(false);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
        binding.etCurrent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.toString().isEmpty()) {
                    binding.inputLayoutCrrAddress.setError("Please enter current address");
                } else {
                    binding.inputLayoutCrrAddress.setErrorEnabled(false);

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
//        binding.etAltMobilenumber.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if (charSequence.toString().isEmpty()) {
//                    binding.inputLayoutAltMobile.setError("Please enter Alternate Mobile Number");
//                } else {
//                    binding.inputLayoutAltMobile.setErrorEnabled(false);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });

//        binding.edtNoofdepartment.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if (charSequence.toString().isEmpty()) {
//                    binding.inputLayoutNoOfDependant.setError("Please enter No. of Dependent");
//                } else {
//                    binding.inputLayoutNoOfDependant.setErrorEnabled(false);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });

//        binding.edtSceeendepartment.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if (charSequence.toString().isEmpty()) {
//                    binding.inputLayoutScreenedDependant.setError("Please enter Screened Dependant");
//                } else {
//                    binding.inputLayoutScreenedDependant.setErrorEnabled(false);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });

        binding.Checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.edtCallstatus.getText().toString().isEmpty()) {
                    Utilities.showAlertDialog(AppoinmentConfirmationActivity.this, "Select Call Status", "Please select call status", false);
                    return;
                }

                if (binding.Checkbox.isChecked()) {
                    check_status = "1";

//                    binding.etDistrict.setEnabled(true);
//                    binding.etTaluka.setEnabled(true);
                    binding.etHouseNo.setEnabled(true);
                    binding.etRoad.setEnabled(true);
                    binding.etArea.setEnabled(true);
                    binding.etLandmark.setEnabled(true);
//                    binding.etPincode.setEnabled(true);
                    binding.etAltMobilenumber.setEnabled(true);
                    binding.etBuildingName.setEnabled(true);


//                    binding.etDistrict.setTextColor(Color.parseColor("#000000"));
//                    binding.etTaluka.setTextColor(Color.parseColor("#000000"));
                    binding.etHouseNo.setTextColor(Color.parseColor("#000000"));
                    binding.etBuildingName.setTextColor(Color.parseColor("#000000"));
                    binding.etRoad.setTextColor(Color.parseColor("#000000"));
                    binding.etArea.setTextColor(Color.parseColor("#000000"));
                    binding.etLandmark.setTextColor(Color.parseColor("#000000"));
//                    binding.etPincode.setTextColor(Color.parseColor("#000000"));
//                    binding.etRegmobilenumber.setTextColor(Color.parseColor("#000000"));
                    binding.etAltMobilenumber.setTextColor(Color.parseColor("#000000"));
//                    binding.etCurrent.setText(binding.editTextRegister.getText().toString());
//                    binding.etCurrent.setEnabled(false);
                } else if (!binding.Checkbox.isChecked()) {
                    check_status = "0";

                    binding.etDistrict.setEnabled(false);
                    binding.etTaluka.setEnabled(false);
                    binding.etHouseNo.setEnabled(false);
                    binding.etRoad.setEnabled(false);
                    binding.etArea.setEnabled(false);
                    binding.etLandmark.setEnabled(false);
                    binding.etPincode.setEnabled(false);
                    binding.etAltMobilenumber.setEnabled(false);
                    binding.etBuildingName.setEnabled(false);


                    binding.etDistrict.setTextColor(Color.parseColor("#c0c0c0"));
                    binding.etTaluka.setTextColor(Color.parseColor("#c0c0c0"));
                    binding.etHouseNo.setTextColor(Color.parseColor("#c0c0c0"));
                    binding.etRoad.setTextColor(Color.parseColor("#c0c0c0"));
                    binding.etArea.setTextColor(Color.parseColor("#c0c0c0"));
                    binding.etLandmark.setTextColor(Color.parseColor("#c0c0c0"));
                    binding.etPincode.setTextColor(Color.parseColor("#c0c0c0"));
                    binding.etRegmobilenumber.setTextColor(Color.parseColor("#c0c0c0"));
                    binding.etAltMobilenumber.setTextColor(Color.parseColor("#c0c0c0"));
                    binding.etBuildingName.setTextColor(Color.parseColor("#c0c0c0"));

//                    binding.etCurrent.setText("");
//                    binding.etCurrent.setEnabled(true);

                }
            }
        });

        binding.edtNoofdepartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shownoofdeparmenttDialog(staticList, "1");
            }
        });

        binding.edtSceeendepartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.edtNoofdepartment.getText().toString().isEmpty()) {
                    Toast.makeText(AppoinmentConfirmationActivity.this, "Please Select No Of Dependent First", Toast.LENGTH_SHORT).show();
                    return;
                }
                showNoOfScreeningPendingDependantDialog(staticList, "2");

            }
        });


        binding.etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (binding.edtCallstatus.getText().toString().isEmpty()) {
                    Utilities.showAlertDialog(AppoinmentConfirmationActivity.this, "Select Call Status", "Please select call status", false);
                    return;
                }

                showDatePicker();

            }
        });


//        binding.etRegmobilenumber.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(Intent.ACTION_DIAL);
//                String phoneNumber = b_mobile;
//                intent.setData(Uri.parse("tel:" + phoneNumber));
//                startActivity(intent);
//            }
//        });
//        binding.etAltMobilenumber.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(Intent.ACTION_DIAL);
//                String phoneNumber = b_mobile;
//                intent.setData(Uri.parse("tel:" + phoneNumber));
//                startActivity(intent);
//            }
//        });
//        binding.etTime.setOnClickListener
//
//
//      (new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
//
//
//                  //  Date currentTime = Calendar.getInstance().getTime();
//
//
//                    @Override
//                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                        if(hourOfDay>=0 && hourOfDay<12){
//                            time = hourOfDay + " : " + minute + " AM";
//                        } else {
//                            if(hourOfDay == 12){
//                                time = hourOfDay + " : " + minute + "PM";
//                            } else{
//                                hourOfDay = hourOfDay -12;
//                                time = hourOfDay + " : " + minute + "PM";
//                            }
//
//                        }
//                       // SimpleDateFormat formatDate = new SimpleDateFormat("hh:mm:ss a");
////                        String formattedDate = dateFormat.format(new Date()).toString();
////                        System.out.println(formattedDate);
//
//                        binding.etTime.setText(time);
//                        lastSelectedHour = hourOfDay;
//                        lastSelectedMinute = minute;
//
//
//                    }
//                };
//
//// Create TimePickerDialog:
//                TimePickerDialog timePickerDialog = new TimePickerDialog(AppoinmentConfirmationActivity.this,
//                        android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
//                        timeSetListener, lastSelectedHour, lastSelectedMinute, is24HView);
//
//// Show
//                timePickerDialog.show();
//
////                showtime();
//            }
//        });

        binding.etTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.etDate.getText().toString().isEmpty()) {
                    Utilities.showAlertDialog(AppoinmentConfirmationActivity.this, "Select Appointment date", "Please select appointment date first", false);
                    return;
                }
                Calendar now = Calendar.getInstance();

                timePickerDialog = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(
                        (com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener) AppoinmentConfirmationActivity.this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        false
                );

                if (selectedApptDate != null && !selectedApptDate.isEmpty()) {
                    try {
                        Log.d("TAG", "onClick: " + Utilities.dfDate6.parse(selectedApptDate).compareTo(new Date(System.currentTimeMillis())));
                        if (Utilities.dfDate6.parse(selectedApptDate).compareTo(new Date(System.currentTimeMillis())) == -1) {
                            now.add(Calendar.HOUR_OF_DAY, 1);

                            if (now.get(Calendar.MINUTE) > 15) {

                                timePickerDialog.setMinTime(now.get(Calendar.HOUR_OF_DAY), 30, 0);
                            } else {
                                timePickerDialog.setMinTime(now.get(Calendar.HOUR_OF_DAY), 00, 0);

                            }

                        }
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                }

                timePickerDialog.setThemeDark(false);
                timePickerDialog.setTitle("Select Appointment Time");
                timePickerDialog.setTimeInterval(1, 30, 60);
                timePickerDialog.setAccentColor(Color.parseColor("#9C27B0"));


                timePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        Log.d("TimePicker", "Dialog was cancelled");
                    }
                });

                timePickerDialog.show(getSupportFragmentManager(), "Timepickerdialog");
            }
        });

        binding.etLandmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //    requestLocationPermission();
            }
        });


        binding.btnDependent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (binding.edtCallstatus.getText().toString().isEmpty()) {
                    Utilities.showAlertDialog(AppoinmentConfirmationActivity.this, "Select Call Status", "Please select call status", false);
                    return;
                }

//////////////////////New Change///////////////

//                if (dependentList != null && !dependentList.isEmpty()) {
//                    for (DependentModel model : dependentList) {
//                        String lastDependantScreeningDate = model.getLastDependantScreeningDate();
//
//                        if (lastDependantScreeningDate != null && !lastDependantScreeningDate.isEmpty()) {
//                            try {
//                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yy", Locale.ENGLISH);
//                                Date screeningDate = sdf.parse(lastDependantScreeningDate);
//                                Date currentDate = new Date();
//
//                                long diffInMillies = Math.abs(currentDate.getTime() - screeningDate.getTime());
//                                long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
//
//                                if (diffInDays > 365) {
//                                    Utilities.showAlertDialog(AppoinmentConfirmationActivity.this, "Alert",
//                                            "मागील ३६५ दिवसांत १ dependent ची स्क्रिनिंग झालेली आहे, त्यामुळे त्याची गणना स्क्रिनिंग pending मध्ये करू नये.\n कृपया स्क्रिनिंगसाठी pending असलेल्या dependent ची योग्य संख्या नमूद करा.\n तसेच एकूण dependent ची संख्या बरोबर नोंदलेली आहे की नाही, याचीही खात्री करा.", false);
//                                    return;
//                                }
//
//                            } catch (ParseException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                }


                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(AppoinmentConfirmationActivity.this);
                android.app.AlertDialog alertDialog = null;


                View v = LayoutInflater.from(AppoinmentConfirmationActivity.this).inflate(R.layout.dependent_list_dilogue, null, false);
                builder.setView(v);
                builder.setTitle("Add Dependent");

                builder.setPositiveButton("close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // finish();

                    }
                });

//                    builder.setIcon(R.drawable.icon_campcreation);
                // TextView titletextView = (TextView) v.findViewById(R.id.tv_alert_dialog_title);

                EditText tvRelation = (EditText) v.findViewById(R.id.tvRelation);
                EditText etAge = (EditText) v.findViewById(R.id.etAge);
                EditText tvfname = (EditText) v.findViewById(R.id.tvfname);
                EditText etMiddleName = (EditText) v.findViewById(R.id.etMiddleName);
                EditText etLastName = (EditText) v.findViewById(R.id.etLastName);
                //  ImageView imvCall = (ImageView) v.findViewById(R.id.imvCall);
                Button btnSubmit = (Button) v.findViewById(R.id.btnSubmit);
                alertDialog = builder.create();


                android.app.AlertDialog finalAlertDialog = alertDialog;

                if (gender.equalsIgnoreCase("Male") || binding.edtWorkerGender.getText().toString().matches("Male")) {
//                    etMiddleName.setText(beneficiary.getFirstName());
//                    etLastName.setText(beneficiary.getLastName());

                    etMiddleName.setText(fname);
                    etLastName.setText(lname);


                } else if (gender.equalsIgnoreCase("Female") || binding.edtWorkerGender.getText().toString().matches("Female")) {
                    // etMiddleName.setText(beneficiary.getFirstName());
//                    etLastName.setText(beneficiary.getLastName());

                    etLastName.setText(lname);


                } else if (gender.equalsIgnoreCase("Other") || binding.edtWorkerGender.getText().toString().matches("Other")) {
//                    etMiddleName.setText(beneficiary.getFirstName());
//                    etLastName.setText(beneficiary.getLastName());

                    etMiddleName.setText(fname);
                    etLastName.setText(lname);

                } else if (gender.equalsIgnoreCase("") || binding.edtWorkerGender.getText().toString().matches("")) {
//                    etMiddleName.setText(beneficiary.getFirstName());
//                    etLastName.setText(beneficiary.getLastName());

                    etMiddleName.setText(fname);
                    etLastName.setText(lname);


                }


                etAge.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                    }

                    @Override
                    public void afterTextChanged(Editable editable) {


                        if (relationId.equalsIgnoreCase("1") || relationId.equalsIgnoreCase("2")
                                || relationId.equalsIgnoreCase("21") || relationId.equalsIgnoreCase("22")) {


                            if (etAge.getText().toString().length() == 2) {
                                try {
                                    int beneficiaryAge = Integer.parseInt(beneficiary.getAge());
                                    int enteredAge = Integer.parseInt(etAge.getText().toString());

                                    if (!(enteredAge >= 18 && enteredAge >= beneficiaryAge + 1)) {
                                        Utilities.showAlertDialog(context, "Alert",
                                                "आई,वडील,सासू,सासरे यांचे वय नोंदणीकृत बांधकाम कामगारापेक्षा जास्त असावे.", false);

                                        etAge.setText("");
                                    }
                                } catch (NumberFormatException e) {
//                                Utilities.showAlertDialog(context, "Error", "Invalid age format.", false);
                                }

                            }

                        } else if (relationId.equalsIgnoreCase("10") || relationId.equalsIgnoreCase("9")) {

                            if (etAge.getText().toString().length() == 2) {
                                try {
                                    int beneficiaryAge = Integer.parseInt(beneficiary.getAge());
                                    int enteredAge = Integer.parseInt(etAge.getText().toString());

                                    if (!(enteredAge >= 18 && enteredAge <= 75)) {
                                        Utilities.showAlertDialog(context, "Alert",
                                                "पत्नी/पती  चे वय १८ पेक्षा जास्त आणि ७५ पेक्षा कमी असावे.", false);

                                        etAge.setText("");
                                    }
                                } catch (NumberFormatException e) {
//                                Utilities.showAlertDialog(context, "Error", "Invalid age format.", false);
                                }

                            }

                        } else if (relationId.equalsIgnoreCase("7") || relationId.equalsIgnoreCase("8")) {

                            if (etAge.getText().toString().length() == 2) {
                                try {
                                    int beneficiaryAge = Integer.parseInt(beneficiary.getAge());
                                    int enteredAge = Integer.parseInt(etAge.getText().toString());

                                    if (!(enteredAge >= 10 && enteredAge <= beneficiaryAge - 15)) {
                                        Utilities.showAlertDialog(context, "Alert",
                                                "१.नोंदणीकृत बांधकाम कामगाराच्या मुलगा/मुलीचे वय १० वर्षापेक्षा जास्त असावे आणि\n" +
                                                        "२. मुलगा/मुलगी आणि नोंदणीकृत बांधकाम कामगार यांच्या वयातील फरक किमान १५ वर्ष असावा.", false);

                                        etAge.setText("");
                                    }
                                } catch (NumberFormatException e) {
//                                Utilities.showAlertDialog(context, "Error", "Invalid age format.", false);
                                }

                            }

                        }
                    }
                });

                tvRelation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new GetRelation(tvRelation, tvfname, etMiddleName, etLastName, etAge).execute(marriedStatusId, binding.edtWorkerGender.getText().toString());
                    }
                });

//                imvCall.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        alertDialog.dismiss();
//                    }
//                });

                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (binding.edtNoofdepartment.getText().toString().isEmpty()) {
                            Toast.makeText(AppoinmentConfirmationActivity.this, "Please Select no of dependent", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (tvRelation.getText().toString().isEmpty()) {
                            Toast.makeText(AppoinmentConfirmationActivity.this, "Please Select Relation", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (etAge.getText().toString().isEmpty()) {
                            Toast.makeText(AppoinmentConfirmationActivity.this, "Please Enter Age", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (!(Double.parseDouble(String.valueOf(etAge.getText().toString())) >= 10)) {
                            Toast.makeText(AppoinmentConfirmationActivity.this, "Beneficiary age should be greater than or equal to 10 years.", Toast.LENGTH_SHORT).show();
                            return;

                        }

                        if (tvfname.getText().toString().isEmpty()) {
                            Toast.makeText(AppoinmentConfirmationActivity.this, "Enter First Name", Toast.LENGTH_SHORT).show();
                            return;

                        }

                        if (!tvfname.getText().toString().matches("[a-zA-Z ]+")) {
                            Toast.makeText(AppoinmentConfirmationActivity.this, "Please enter english letters only in first name", Toast.LENGTH_SHORT).show();
                            return;

                        }

                        if (!etMiddleName.getText().toString().isEmpty()) {

                            if (!etMiddleName.getText().toString().matches("[a-zA-Z ]+")) {
                                Toast.makeText(AppoinmentConfirmationActivity.this, "Please enter english letters only in middle name", Toast.LENGTH_SHORT).show();
                                return;
                            }

//                            Toast.makeText(AppoinmentConfirmationActivity.this, "Please Enter Middle Name", Toast.LENGTH_SHORT).show();

                        }

                        if (etLastName.getText().toString().isEmpty()) {
                            Toast.makeText(AppoinmentConfirmationActivity.this, "Please Enter Last Name", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (!etLastName.getText().toString().matches("[a-zA-Z ]+")) {
                            Toast.makeText(AppoinmentConfirmationActivity.this, "Please enter letters only in last name", Toast.LENGTH_SHORT).show();
                            return;
                        }


//                        for (DependentModel dependent : dependentList) {
//                            if (dependent.getRelId().equals(relationId)) {
//                                Toast.makeText(AppoinmentConfirmationActivity.this, "This relation is already added.", Toast.LENGTH_SHORT).show();
//                                return;
//                            }
//                        }

                        dependentList.add(new DependentModel(AssignCallID, relationId, etAge.getText().toString(), tvfname.getText().toString(), etMiddleName.getText().toString(), etLastName.getText().toString(), ""));

                        Dependent_Adapter dependent_adapter = new Dependent_Adapter(AppoinmentConfirmationActivity.this, dependentList, AppoinmentConfirmationActivity.this::onDelete);
                        binding.rvDependent.setAdapter(dependent_adapter);

//////////New Change/////////////
//                        if (dependentList.size() == 5 || Integer.parseInt(binding.edtNoofdepartment.getText().toString()) == dependentList.size()) {
//                            btnSubmit.setVisibility(View.GONE);
//                            binding.btnDependent.setVisibility(View.GONE);
//                            if (finalAlertDialog != null) {
//                                finalAlertDialog.dismiss();
//
//                                Utilities.hideSoftKeyboard(AppoinmentConfirmationActivity.this);
//
//                            }
//                        }


                        if (dependentList.size() == 5 || Integer.parseInt(binding.edtSceeendepartment.getText().toString()) == dependentList.size()) {
                            btnSubmit.setVisibility(View.GONE);
                            binding.btnDependent.setVisibility(View.GONE);
                            if (finalAlertDialog != null) {
                                finalAlertDialog.dismiss();

                                Utilities.hideSoftKeyboard(AppoinmentConfirmationActivity.this);

                            }
                        }


                        tvRelation.setText("");
                        etAge.setText("");
                        tvfname.setText("");
//                        etMiddleName.setText("");
//                        etLastName.setText("");

                        binding.headerLL.setVisibility(View.VISIBLE);
                        binding.viewTop.setVisibility(View.VISIBLE);
                        binding.viewBottom.setVisibility(View.VISIBLE);


                    }
                });


//                 android.app.AlertDialog finalAlertDialog = alertDialog;

                alertDialog.show();


            }


        });

//        binding.btnDependent.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
////                if (binding.edtCallstatus.getText().toString().isEmpty()) {
////                    Toast.makeText(AppoinmentConfirmationActivity.this, "Select Call Status", Toast.LENGTH_SHORT).show();
////                    binding.inputLayoutCallStatus.setError("Select Call Status");
////                } else if (binding.editTextRegister.getText().toString().isEmpty()) {
////                    Toast.makeText(AppoinmentConfirmationActivity.this, "Enter Your Register Address", Toast.LENGTH_SHORT).show();
////                    binding.inputLayoutRegAddress.setError("Enter Your Register Address");
////                } else if (binding.etRegmobilenumber.getText().toString().isEmpty() || !Utilities.isMobileNo(binding.etAltMobilenumber.getText().toString())) {
////                    Toast.makeText(AppoinmentConfirmationActivity.this, "Enter Your Mobile Number", Toast.LENGTH_SHORT).show();
////                } else if (!binding.etRegmobilenumber.getText().toString().isEmpty()) {
////                    if (Utilities.isMobileNo(binding.etAltMobilenumber.getText().toString())) {
////                        Toast.makeText(AppoinmentConfirmationActivity.this, "Enter Valid Mobile Number", Toast.LENGTH_SHORT).show();
////                    }
////                } else if (binding.etDate.getText().toString().isEmpty()) {
////                    Toast.makeText(AppoinmentConfirmationActivity.this, "Enter Your Date", Toast.LENGTH_SHORT).show();
////                    binding.inputLayoutApptDate.setError("Please enter Appointment Date");
////                } else if (binding.edtNoofdepartment.getText().toString().isEmpty()) {
////                    binding.inputLayoutNoOfDependant.setError("Please enter No Of Dependant");
////                } else if (binding.edtSceeendepartment.getText().toString().isEmpty()) {
////                    binding.inputLayoutScreenedDependant.setError("Please enter No Of Screening Pending Dependant");
////                } else {
//
//
//                if (marriedStatusId.equalsIgnoreCase("1")) {
//
//
//                    if (binding.edtNoofdepartment.getText().toString().trim().isEmpty()) {
//                        Toast.makeText(AppoinmentConfirmationActivity.this, "Select Number Of Dependent", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                    if (binding.edtSceeendepartment.getText().toString().trim().isEmpty()) {
//                        Toast.makeText(AppoinmentConfirmationActivity.this, "Select Number Of Screening Pending", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                    if (binding.etRelation.getText().toString().trim().isEmpty()) {
//                        Toast.makeText(AppoinmentConfirmationActivity.this, "Select Relation", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                    if (binding.etAge.getText().toString().trim().isEmpty()) {
//                        Toast.makeText(AppoinmentConfirmationActivity.this, "Select Age", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                    if (binding.etFname.getText().toString().trim().isEmpty()) {
//                        Toast.makeText(AppoinmentConfirmationActivity.this, "Select First Name", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                    if (binding.etMiddleName.getText().toString().trim().isEmpty()) {
//                        Toast.makeText(AppoinmentConfirmationActivity.this, "Select Middle Name", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//
//                    if (binding.etLastName.getText().toString().trim().isEmpty()) {
//                        Toast.makeText(AppoinmentConfirmationActivity.this, "Select Last Name", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                }
//
//
//                dependentList.add(new DependentModel(AssignCallID, relationId, binding.etAge.getText().toString(), binding.etFname.getText().toString(), binding.etMiddleName.getText().toString(), binding.etLastName.getText().toString()));
//
//                Dependent_Adapter dependent_adapter = new Dependent_Adapter(AppoinmentConfirmationActivity.this, dependentList, AppoinmentConfirmationActivity.this::onDelete);
//                binding.rvDependent.setAdapter(dependent_adapter);
//
//                if (dependentList.size() == 3) {
//                    binding.btnDependent.setVisibility(View.GONE);
//                }
//                binding.etRelation.setText("");
//                binding.etAge.setText("");
//                binding.etFname.setText("");
//////              binding.etMiddleName.setText("");
////              binding.etLastName.setText("");
//
//
//            }
//        });


        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (flag == 1) {


                    if (CallingLog != null) {
                        if (CallingLog.equalsIgnoreCase("0")) {
                            Utilities.showAlertDialog(AppoinmentConfirmationActivity.this, "Alert", " It seems you have not called to beneficiary. Please call to beneficiary first and fill in the information ", false);
                            return;
                        }
                    }


//                if (binding.edtCallstatus.getText().toString().isEmpty()) {
//                    Toast.makeText(AppoinmentConfirmationActivity.this, "Select Call Status", Toast.LENGTH_SHORT).show();
//                    binding.inputLayoutCallStatus.setError("Select Call Status");
//                } else if (binding.editTextRegister.getText().toString().isEmpty()) {
//                    Toast.makeText(AppoinmentConfirmationActivity.this, "Enter Your Register Address", Toast.LENGTH_SHORT).show();
//                    binding.inputLayoutRegAddress.setError("Enter Your Register Address");
//                } else if (binding.etRegmobilenumber.getText().toString().isEmpty() || !Utilities.isMobileNo(binding.etAltMobilenumber.getText().toString())) {
//                    Toast.makeText(AppoinmentConfirmationActivity.this, "Enter Your Mobile Number", Toast.LENGTH_SHORT).show();
//                } else if (!binding.etRegmobilenumber.getText().toString().isEmpty()) {
//                    if (Utilities.isMobileNo(binding.etAltMobilenumber.getText().toString())) {
//                        Toast.makeText(AppoinmentConfirmationActivity.this, "Enter Valid Mobile Number", Toast.LENGTH_SHORT).show();
//                    }
//                } else if (binding.etDate.getText().toString().isEmpty()) {
//                    Toast.makeText(AppoinmentConfirmationActivity.this, "Enter Your Date", Toast.LENGTH_SHORT).show();
//                    binding.inputLayoutApptDate.setError("Please enter Appointment Date");
//                } else if (binding.edtNoofdepartment.getText().toString().isEmpty()) {
//                    binding.inputLayoutNoOfDependant.setError("Please enter No Of Dependant");
//                } else if (binding.edtSceeendepartment.getText().toString().isEmpty()) {
//                    binding.inputLayoutScreenedDependant.setError("Please enter No Of Screening Pending Dependant");
//                } else {


                    if (binding.edtCallstatus.getText().toString().trim().isEmpty()) {
                        Toast.makeText(AppoinmentConfirmationActivity.this, "Select Call Status", Toast.LENGTH_SHORT).show();
                        return;
                    }


                    if (!binding.etPincode.getText().toString().isEmpty()) {
                        if (!(binding.etPincode.getText().toString().length() == 6)) {
                            Toast.makeText(AppoinmentConfirmationActivity.this, "Please Enter Valid Pincode", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    if (!binding.etAltMobilenumber.getText().toString().isEmpty()) {
                        if (!(binding.etAltMobilenumber.getText().toString().length() == 10)) {
                            Toast.makeText(AppoinmentConfirmationActivity.this, "Please Enter Valid Alternate Mobile Number", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

//                if (binding.edtWorkerGender.getText().toString().trim().isEmpty()) {
//                    Toast.makeText(AppoinmentConfirmationActivity.this, "Please Select Worker's Gender", Toast.LENGTH_SHORT).show();
//                    return;
//                }


                    if (marriedStatusId != null) {


                        ///////////////////New Change//////////


//                        if (call_statusid == 2 && marriedStatusId.equalsIgnoreCase("1") || marriedStatusId.equalsIgnoreCase("2") || marriedStatusId.equalsIgnoreCase("3") || marriedStatusId.equalsIgnoreCase("4")) {
//                            if (!(Integer.parseInt(binding.edtNoofdepartment.getText().toString()) == dependentList.size())) {
//                                Toast.makeText(AppoinmentConfirmationActivity.this, "Please Add or Remove Dependent Info.", Toast.LENGTH_SHORT).show();
//                                //  binding.btnDependent.setVisibility(View.VISIBLE);
//                                return;
//                            }
//
//                        }

                        if(binding.edtSceeendepartment.getText().toString().isEmpty()){
                            Toast.makeText(AppoinmentConfirmationActivity.this, "Please Enter Dependent Screening Pending count ", Toast.LENGTH_SHORT).show();
                            return;

                        }

                        if (call_statusid == 2 && marriedStatusId.equalsIgnoreCase("1") || marriedStatusId.equalsIgnoreCase("2") || marriedStatusId.equalsIgnoreCase("3") || marriedStatusId.equalsIgnoreCase("4")) {


                            if (!(Integer.parseInt(binding.edtSceeendepartment.getText().toString()) == dependentList.size())) {
                                Toast.makeText(AppoinmentConfirmationActivity.this, "Please Add or Remove Dependent Info.", Toast.LENGTH_SHORT).show();
                                //  binding.btnDependent.setVisibility(View.VISIBLE);
                                return;
                            }

                        }

                        if (binding.etRemarkOther.getText().toString().trim().isEmpty()) {
                            Toast.makeText(AppoinmentConfirmationActivity.this, "Please Select Remark ", Toast.LENGTH_SHORT).show();
                            return;
                        }


                        if (binding.etRemark.getText().toString().trim().isEmpty()) {
                            Toast.makeText(AppoinmentConfirmationActivity.this, "Please Enter Remark ", Toast.LENGTH_SHORT).show();
                            return;
                        }


                    }


                    if (call_statusid == 2) {

                        if (binding.etDistrict.getText().toString().trim().isEmpty()) {
                            Toast.makeText(AppoinmentConfirmationActivity.this, "Select District", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (binding.etTaluka.getText().toString().trim().isEmpty()) {
                            Toast.makeText(AppoinmentConfirmationActivity.this, "Select Taluka", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (binding.etPincode.getText().toString().trim().isEmpty()) {
                            Toast.makeText(AppoinmentConfirmationActivity.this, "Please Enter Pincode", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (!(binding.etPincode.getText().toString().length() == 6)) {
                            Toast.makeText(AppoinmentConfirmationActivity.this, "Please Enter Valid Pincode", Toast.LENGTH_SHORT).show();
                            return;

                        }

                        if (!binding.etAltMobilenumber.getText().toString().isEmpty()) {
                            if (!(binding.etAltMobilenumber.getText().toString().length() == 10)) {
                                Toast.makeText(AppoinmentConfirmationActivity.this, "Please Enter Valid Alternate Mobile Number", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        if (binding.edtWorkerGender.getText().toString().trim().isEmpty()) {
                            Toast.makeText(AppoinmentConfirmationActivity.this, "Please Select Worker's Gender", Toast.LENGTH_SHORT).show();
                            return;
                        }


                        if (binding.edtWorkerGender.getText().toString().trim().isEmpty()) {
                            Toast.makeText(AppoinmentConfirmationActivity.this, "Select Worker Gender", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (binding.edtWorkerMarrageStatus.getText().toString().trim().isEmpty()) {
                            Toast.makeText(AppoinmentConfirmationActivity.this, "Please select marital status of worker", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (marriedStatusId.equalsIgnoreCase("1") || marriedStatusId.equalsIgnoreCase("2") || marriedStatusId.equalsIgnoreCase("3") || marriedStatusId.equalsIgnoreCase("4")) {
                            if (binding.edtNoofdepartment.getText().toString().trim().isEmpty()) {
                                Toast.makeText(AppoinmentConfirmationActivity.this, "Select Number Of Dependent", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        if (marriedStatusId.equalsIgnoreCase("1") || marriedStatusId.equalsIgnoreCase("2") || marriedStatusId.equalsIgnoreCase("3") || marriedStatusId.equalsIgnoreCase("4")) {
                            if (binding.edtSceeendepartment.getText().toString().trim().isEmpty()) {
                                Toast.makeText(AppoinmentConfirmationActivity.this, "Select Number Of Screening Pending Dependent", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        if (binding.etDate.getText().toString().trim().isEmpty()) {
                            Toast.makeText(AppoinmentConfirmationActivity.this, "Select Date", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (binding.etTime.getText().toString().trim().isEmpty()) {
                            Toast.makeText(AppoinmentConfirmationActivity.this, "Select Time", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (marriedStatusId != null) {


                            //////////////New Change////////////
//                            if (call_statusid == 2 && marriedStatusId.equalsIgnoreCase("1") || marriedStatusId.equalsIgnoreCase("2") || marriedStatusId.equalsIgnoreCase("3") || marriedStatusId.equalsIgnoreCase("4")) {
//                                if (!(Integer.parseInt(binding.edtNoofdepartment.getText().toString()) == dependentList.size())) {
//                                    Toast.makeText(AppoinmentConfirmationActivity.this, "Please Add or Remove Dependent Info", Toast.LENGTH_SHORT).show();
//                                    binding.btnDependent.setVisibility(View.VISIBLE);
//                                    return;
//                                }
//                            }


                            if (call_statusid == 2 && marriedStatusId.equalsIgnoreCase("1") || marriedStatusId.equalsIgnoreCase("2") || marriedStatusId.equalsIgnoreCase("3") || marriedStatusId.equalsIgnoreCase("4")) {
                                if (!(Integer.parseInt(binding.edtSceeendepartment.getText().toString()) == dependentList.size())) {
                                    Toast.makeText(AppoinmentConfirmationActivity.this, "Please Add or Remove Dependent Info", Toast.LENGTH_SHORT).show();
                                    binding.btnDependent.setVisibility(View.VISIBLE);
                                    return;
                                }
                            }
                        }
                    }

                    if (call_statusid == 11) {


                        /////////////New Change//////////////
//                        if (!(Integer.parseInt(binding.edtNoofdepartment.getText().toString()) == dependentList.size())) {
//                            Toast.makeText(AppoinmentConfirmationActivity.this, "Please Add or Remove Dependent Info", Toast.LENGTH_SHORT).show();
//                            binding.btnDependent.setVisibility(View.VISIBLE);
//                            return;
//                        }


                        if (!(Integer.parseInt(binding.edtSceeendepartment.getText().toString()) == dependentList.size())) {
                            Toast.makeText(AppoinmentConfirmationActivity.this, "Please Add or Remove Dependent Info", Toast.LENGTH_SHORT).show();
                            binding.btnDependent.setVisibility(View.VISIBLE);
                            return;
                        }

                    }

                    if (call_statusid == 3) {
                        if (binding.etRemark.getText().toString().trim().isEmpty()) {
                            Toast.makeText(AppoinmentConfirmationActivity.this, "Please Enter Remark ", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }


//                if (marriedStatusId.equalsIgnoreCase("1")) {
//                    if (binding.etRelation.getText().toString().trim().isEmpty()) {
//                        Toast.makeText(AppoinmentConfirmationActivity.this, "Select Relation", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//
//                }


                    String landmark = binding.etLandmark.getText().toString();
                    String inputDate = binding.etDate.getText().toString();
                    String outputFormat = "dd-MMMM-yyyy";


                    JsonArray dependentUserJsonArray = new JsonArray();

                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("AssignCallID", AssignCallID);
                    jsonObject.addProperty("RelId", relationId);
                    jsonObject.addProperty("Age", binding.etAge.getText().toString());
                    jsonObject.addProperty("FirstName", binding.etFname.getText().toString());
                    jsonObject.addProperty("MiddleName", binding.etMiddleName.getText().toString());
                    jsonObject.addProperty("LastName", binding.etLastName.getText().toString());
                    jsonObject.addProperty("LastDependantScreeningDate", "");
                    dependentUserJsonArray.add(jsonObject);

                    Gson g = new Gson();
                    String j = g.toJson(dependentList);

                    if (dependentList.isEmpty()) {
                        j = "[{\"AssignCallID\":\"0\",\"RelId\":\"0\",\"Age\":\"0\",\"FirstName\":\" \",\"MiddleName\":\" \",\"LastName\":\" \",\"LastDependantScreeningDate\":\" \"}]";
                    }


//                if (beneficiary.getAppointmentCount()  > 20 ){
//
//
//                    android.app.AlertDialog.Builder b = new android.app.AlertDialog.Builder(context)
//                            .setTitle("20 appointments are are already confirmed \n" +
//                                    "on dd-mon-yy.\n" +
//                                    " Are you still want to book this appointment?")
//                            .setIcon(R.drawable.icon_success)
//                            .setPositiveButton("Yes",
//                                    new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog, int whichButton) {
//                                            // do something...
//                                            dialog.dismiss();
//
//                                        }
//                                    }
//                            )
//                            .setNegativeButton("No",
//                                    new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog, int whichButton) {
//                                            binding.etDate.setText("");
//
//
//                                        }
//                                    }
//                            );
//                    b.show();
//
//
//                }


                    insertdata(
                            String.valueOf(call_statusid),
                            binding.editTextRegister.getText().toString().replace(",", " "),
                            binding.etCurrent.getText().toString(),
                            binding.etRegmobilenumber.getText().toString(),
                            binding.etPincode.getText().toString(),
                            no_ofdepartment,
                            String.valueOf(noOfPendingScreeningDependant),
                            binding.etDate.getText().toString(),
                            binding.etTime.getText().toString(),
                            binding.etRemark.getText().toString(),
                            landmark,
                            binding.edtWorkerGender.getText().toString(),
                            marriedStatusId,
                            DISTLGDCODE,
                            TALLGDCODE,
                            binding.etHouseNo.getText().toString(),
                            binding.etRoad.getText().toString(),
                            binding.etArea.getText().toString(),
                            j,
                            binding.etAltMobilenumber.getText().toString());


                } else if (flag == 2) {


                    if (binding.edtCallstatus.getText().toString().isEmpty()) {
                        Utilities.showAlertDialog(context, "Alert", "Please select call status", false);
                        return;
                    }

                    if (binding.etDate.getText().toString().isEmpty()) {
                        Utilities.showAlertDialog(context, "Alert", "Please select appointment date", false);
                        return;
                    }

                    if (binding.etTime.getText().toString().isEmpty()) {
                        Utilities.showAlertDialog(context, "Alert", "Please select appointment time", false);
                        return;
                    }

                    if (binding.edtRemarkPhlebo.getText().toString().isEmpty() || binding.edtRemarkPhlebo.getText().toString().equalsIgnoreCase("NA")) {
                        Utilities.showAlertDialog(context, "Alert", "Please select remark", false);
                        return;
                    }

                    insertdataNew(
                            AssignCallID,
                            String.valueOf(call_statusid),
                            binding.etDate.getText().toString(),
                            binding.etTime.getText().toString(),
                            binding.edtRemarkPhlebo.getText().toString(),
                            phleboremarkId
                    );

                }
            }

        });

    }

    @Override
    public void onTimeSet(com.wdullaer.materialdatetimepicker.time.TimePickerDialog view, int hourOfDay, int minute, int second) {
        try {
            Date selData = Utilities.timeFormat24.parse(String.valueOf(hourOfDay) + ":" + String.valueOf(minute));
            String time = Utilities.timeFormat.format(selData);
            binding.etTime.setText(time);

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }

    private void getSessionData() {

        try {
            JSONArray user_info = new JSONArray(session.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                DESGID = json.getString("DESGID");
                //    DISTLGDCODE = json.getString("DISTLGDCODE");
                district = json.getString("district");
                //  TALLGDCODE = json.getString("TALLGDCODE");
                taluka = json.getString("taluka");
                EmpCode = json.getString("EmpCode");
                LabCode = json.getString("LabCode");
                //  UserId = json.getString("UserId");

//                binding.etDistrict.setText(district);
//                binding.etTaluka.setText(taluka);


            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }


    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Permission already granted
            getCurrentLocation();
        }
    }


    private void getCurrentLocation() {
        FusedLocationProviderClient fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();

                            //  fetchLandmark(latitude, longitude);
                        } else {
                            Toast.makeText(AppoinmentConfirmationActivity.this, "Please Try Again!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AppoinmentConfirmationActivity.this, "Please Try Again!!", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void showtime() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(AppoinmentConfirmationActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar c = Calendar.getInstance();
                        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        c.set(Calendar.MINUTE, minute);
                        String time12 = Utilities.timeFormat.format(c.getTime());
                        time24 = Utilities.timeFormat24.format(c.getTime());
//                        String selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                        binding.etTime.setText(time12);

                    }
                }, hour, minute, false);

        timePickerDialog.show();
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(AppoinmentConfirmationActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar selectedCalendar = Calendar.getInstance();
                        selectedCalendar.set(year, month, dayOfMonth);
                        if (selectedCalendar.before(calendar)) {
                            binding.etDate.setError("Please select a date after the current date.");
                        } else {
                            String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                            String dt = "";
                            try {
                                dt = Utilities.dfDate6.format(Utilities.dfDate2.parse(selectedDate));
                                selectedApptDate = dt;

                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }

                            binding.etDate.setText(dt);
                            binding.etTime.setText("");
                            new GetAppointmentCount().execute(String.valueOf(session.getUserDetailsJson().getEmpCode()), dt);
                        }
                    }
                }, currentYear, currentMonth, currentDay);

        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());

        datePickerDialog.show();
    }

    private void insertdata(String call_status, String register_address, String current_address, String mobile_no, String pincode, String noOfDependants, String noOfPendingDependant, String date, String time, String remark, String landmark, String WorkersGender, String WorkersMaritalStatus, String DISTLGDCODE, String TALLGDCODE, String HouseNo, String Road, String Area, String DependantDetails, String alternateMobile) {
        String department_number = binding.edtNoofdepartment.getText().toString();
        Log.e("check_number>>>", "insertdata: " + department_number);
        final ProgressDialog progressDialog = new ProgressDialog(AppoinmentConfirmationActivity.this);
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.web_forcalllist().create(ApiInterface.class);
        Call<ResponseModel> call = apiService.InsertBeneficiaryCallingAppointmentDetailsNew(AssignCallID, call_status, register_address, current_address, check_status, mobile_no, alternateMobile, pincode, landmark, WorkersGender, WorkersMaritalStatus, noOfDependants, noOfPendingDependant, date, time, remark, Integer.valueOf(DISTLGDCODE), Integer.valueOf(TALLGDCODE), HouseNo, Road, Area, DependantDetails, String.valueOf(session.getUserDetailsJson().getEmpCode()), beneficiary.getIsWorkerScreened(), remarkId);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
//                    Log.d("Res", new Gson().toJson(response));
                    String status = response.body().getStatus();
                    String message = response.body().getMessage();
                    if (status.equalsIgnoreCase("Success")) {
                        LocalBroadcastManager.getInstance(AppoinmentConfirmationActivity.this).sendBroadcast(new Intent("refresh_exp_benf_list"));

//                        binding.etRelation.setText("");
//                        binding.etAge.setText("");
//                        binding.etFname.setText("");
//                        binding.etDate.setText("");
//                        binding.etTime.setText("");
//                        binding.etRemark.setText("");

                        Utilities.showAlertDialog(AppoinmentConfirmationActivity.this, status, "Details saved successfully", true, "Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        });

                    } else if (status.equalsIgnoreCase("Fail")) {
                        if (message.contains("20 appointments are already booked")) {
                            Utilities.showAlertDialog(AppoinmentConfirmationActivity.this, status, "20 appointments are already booked on " + binding.etDate.getText().toString() + " for this team .Please book an appointment for another date.", false);
                        } else {
                            Utilities.showAlertDialog(AppoinmentConfirmationActivity.this, status, message, false);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void insertdataNew(String AssignId, String call_status, String date, String time, String remark, String phleboremarkId) {
        String department_number = binding.edtNoofdepartment.getText().toString();
        Log.e("check_number>>>", "insertdata: " + department_number);
        final ProgressDialog progressDialog = new ProgressDialog(AppoinmentConfirmationActivity.this);
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.web_forcalllist().create(ApiInterface.class);
        Call<ResponseModel> call = apiService.InsertBeneficiaryCallingAppointmentDetailsNewForUpdate(AssignId, call_status, date, time, remark, phleboremarkId, String.valueOf(session.getUserDetailsJson().getEmpCode()));
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
//                    Log.d("Res", new Gson().toJson(response));
                    String status = response.body().getStatus();
                    String message = response.body().getMessage();
                    if (status.equalsIgnoreCase("Success")) {
//                        LocalBroadcastManager.getInstance(AppoinmentConfirmationActivity.this).sendBroadcast(new Intent("refresh_exp_benf_list"));

//                        binding.etRelation.setText("");
//                        binding.etAge.setText("");
//                        binding.etFname.setText("");
//                        binding.etDate.setText("");
//                        binding.etTime.setText("");
//                        binding.etRemark.setText("");

                        Utilities.showAlertDialog(AppoinmentConfirmationActivity.this, status, "Details saved successfully", true, "Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        });

                    } else if (status.equalsIgnoreCase("Fail")) {
                        if (message.contains("20 appointments are already booked")) {
                            Utilities.showAlertDialog(AppoinmentConfirmationActivity.this, status, "20 appointments are already booked on " + binding.etDate.getText().toString() + " for this team .Please book an appointment for another date.", false);
                        } else {
                            Utilities.showAlertDialog(AppoinmentConfirmationActivity.this, status, message, false);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void setUpToolBarr() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        if (flag == 1) {
            getSupportActionBar().setTitle("Appointment Confirmation");

        } else if (flag == 2) {
            getSupportActionBar().setTitle("Beneficiary Details");

        }

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getapicall() {
        final ProgressDialog progressDialog = new ProgressDialog(AppoinmentConfirmationActivity.this);
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.web_forcalllist().create(ApiInterface.class);
        Call<CallstatusResponse> call = apiService.CALLSTATUS_RESPONSE_CALL_New(AssignCallID);
        call.enqueue(new Callback<CallstatusResponse>() {
            @Override
            public void onResponse(Call<CallstatusResponse> call, Response<CallstatusResponse> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    String status = response.body().getStatus();
                    String message = response.body().getMessage();
                    if (status.equalsIgnoreCase("Success")) {
                        List<OutputItem> outputItems = response.body().getOutput();
                        if (outputItems.size() > 0) {
                            for (OutputItem o :
                                    outputItems) {
                                if (o.getAssignStatusID() == 1) {
                                    outputItems.remove(o);
                                    break;
                                }
                            }
                            showTrenchListDialog(outputItems);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<CallstatusResponse> call, Throwable t) {
                progressDialog.dismiss();
                t.getLocalizedMessage();

            }
        });
    }

    private void getapicallForPhlebo() {
        final ProgressDialog progressDialog = new ProgressDialog(AppoinmentConfirmationActivity.this);
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.web_forcalllist().create(ApiInterface.class);
        Call<CallstatusResponse> call = apiService.CALLSTATUS_RESPONSE_CALL_For_Phlebo(AssignCallID);
        call.enqueue(new Callback<CallstatusResponse>() {
            @Override
            public void onResponse(Call<CallstatusResponse> call, Response<CallstatusResponse> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    String status = response.body().getStatus();
                    String message = response.body().getMessage();
                    if (status.equalsIgnoreCase("Success")) {
                        List<OutputItem> outputItems = response.body().getOutput();
                        if (outputItems.size() > 0) {
                            for (OutputItem o :
                                    outputItems) {
                                if (o.getAssignStatusID() == 1) {
                                    outputItems.remove(o);
                                    break;
                                }
                            }
                            showTrenchListDialogForPhlebo(outputItems);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<CallstatusResponse> call, Throwable t) {
                progressDialog.dismiss();
                t.getLocalizedMessage();

            }
        });
    }

    private void getDistrictList() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        apiInterface.getAllDistrictList(2).enqueue(new Callback<DistrictList_Pojo>() {
            @Override
            public void onResponse(Call<DistrictList_Pojo> call, Response<DistrictList_Pojo> response) {

                if (response.isSuccessful()) {

                    if (response.body().getStatus().equalsIgnoreCase("success")) {
                        districtList_models = response.body().getOutput();
                        List<DistrictList_Model> districtListModels = response.body().getOutput();
                        if (districtListModels.size() > 0) {
                            for (DistrictList_Model o :
                                    districtListModels) {
//                                if (o.getAssignStatusID() == 1) {
//                                    outputItems.remove(o);
//                                    break;
//                                }
                            }
                            showdistrictListDialog(districtListModels);
                        }
                        //   new NestedCampList_Activity.GetMonthlySurveySiteRequestForOS().execute(String.valueOf(selectedMonthId), String.valueOf(selectedYearId), DISTLGDCODE, String.valueOf(campTypeId));

                    }
                }
            }

            @Override
            public void onFailure(Call<DistrictList_Pojo> call, Throwable t) {

            }
        });
    }

    private void getTalukaList() {
        ApiInterface apiInterface = ApiClient.getD2DClient().create(ApiInterface.class);
        apiInterface.getTaluka(Integer.parseInt(STATELGDCODE), Integer.parseInt(DISTLGDCODE)).enqueue(new Callback<TalukaModel>() {
            @Override
            public void onResponse(Call<TalukaModel> call, Response<TalukaModel> response) {

                if (response.isSuccessful()) {

                    if (response.body().getStatus().equalsIgnoreCase("success")) {
                        talukatList_models = response.body().getOutput();
                        List<TalukaModel.Output> talukaModels = response.body().getOutput();
                        if (talukaModels.size() > 0) {
                            for (TalukaModel.Output o :
                                    talukaModels) {
//                                if (o.getAssignStatusID() == 1) {
//                                    outputItems.remove(o);
//                                    break;
//                                }


                            }
                            showTalukaListDialog(talukaModels);
                        }
                        //   new NestedCampList_Activity.GetMonthlySurveySiteRequestForOS().execute(String.valueOf(selectedMonthId), String.valueOf(selectedYearId), DISTLGDCODE, String.valueOf(campTypeId));

                    }
                }
            }

            @Override
            public void onFailure(Call<TalukaModel> call, Throwable t) {

            }
        });
    }





    private void getDependentList() {
        ApiInterface apiInterface = ApiClient.web_forcalllist().create(ApiInterface.class);
        apiInterface.DEPENDENTLIS_RESPONSE_CALL(AssignCallID).enqueue(new Callback<DependentModel>() {
            @Override
            public void onResponse(Call<DependentModel> call, Response<DependentModel> response) {

                if (response.isSuccessful()) {

                    if (response.body().getStatus().equalsIgnoreCase("success")) {
                        List<DependentModel> dependentListModels = response.body().getOutput();
                        dependentList = new ArrayList<>();
                        if (dependentListModels.size() > 0) {


                            for (DependentModel o :
                                    dependentListModels) {

                                dependentList.add(new DependentModel(String.valueOf(o.getAssignCallID()), String.valueOf(o.getRelId()), String.valueOf(o.getAge()), o.getFirstName(), o.getMiddleName(), o.getLastName(), o.getLastDependantScreeningDate()));


                                binding.headerLL.setVisibility(View.VISIBLE);
                                binding.viewTop.setVisibility(View.VISIBLE);
                                binding.viewBottom.setVisibility(View.VISIBLE);


                                
                                if (flag == 1) {

                                    if ((dependentList.size() == 3)) {
                                        binding.btnDependent.setVisibility(View.GONE);
                                    } else if (dependentList.size() < 3) {
                                        binding.btnDependent.setVisibility(View.VISIBLE);
                                    }

                                    if (dependentList.size() > 0) {

                                        /////////New Change/////////////
//                                        if ((Integer.parseInt(binding.edtNoofdepartment.getText().toString()) > dependentList.size())) {
//                                            binding.btnDependent.setVisibility(View.VISIBLE);
//                                            //  Utilities.showAlertDialog(AppoinmentConfirmationActivity.this, "Alert", "Add Dependent", false);
//
//                                        } else {
//                                            if ((Integer.parseInt(binding.edtNoofdepartment.getText().toString()) < dependentList.size())) {
//                                                // binding.btnDependent.setVisibility(View.VISIBLE);
//                                                //   Utilities.showAlertDialog(AppoinmentConfirmationActivity.this, "Alert", "Select Dependent You Want To Remove", false);
//                                                binding.btnDependent.setVisibility(View.GONE);
//                                            } else {
//                                                if ((Integer.parseInt(binding.edtNoofdepartment.getText().toString()) == dependentList.size())) {
//                                                    // binding.btnDependent.setVisibility(View.VISIBLE);
//                                                    //  Utilities.showAlertDialog(AppoinmentConfirmationActivity.this, "Alert", "Select Dependent You Want To Remove", false);
//                                                    binding.btnDependent.setVisibility(View.GONE);
//                                                }
//
//                                            }
//
//                                        }


                                        if ((Integer.parseInt(binding.edtSceeendepartment.getText().toString()) > dependentList.size())) {
                                            binding.btnDependent.setVisibility(View.VISIBLE);
                                            //  Utilities.showAlertDialog(AppoinmentConfirmationActivity.this, "Alert", "Add Dependent", false);

                                        } else {
                                            if ((Integer.parseInt(binding.edtSceeendepartment.getText().toString()) < dependentList.size())) {
                                                // binding.btnDependent.setVisibility(View.VISIBLE);
                                                //   Utilities.showAlertDialog(AppoinmentConfirmationActivity.this, "Alert", "Select Dependent You Want To Remove", false);
                                                binding.btnDependent.setVisibility(View.GONE);
                                            } else {
                                                if ((Integer.parseInt(binding.edtSceeendepartment.getText().toString()) == dependentList.size())) {
                                                    // binding.btnDependent.setVisibility(View.VISIBLE);
                                                    //  Utilities.showAlertDialog(AppoinmentConfirmationActivity.this, "Alert", "Select Dependent You Want To Remove", false);
                                                    binding.btnDependent.setVisibility(View.GONE);
                                                }

                                            }

                                        }



                                    }
                                } else if (flag == 2) {

                                    binding.btnDependent.setVisibility(View.GONE);


                                    if ((dependentList.size() == 3)) {
                                        binding.btnDependent.setVisibility(View.GONE);
                                    } else if (dependentList.size() < 3) {
                                        binding.btnDependent.setVisibility(View.GONE);
                                    }

                                    if (dependentList.size() > 0) {
                                        if ((Integer.parseInt(binding.edtNoofdepartment.getText().toString()) > dependentList.size())) {
                                            binding.btnDependent.setVisibility(View.GONE);
                                            //  Utilities.showAlertDialog(AppoinmentConfirmationActivity.this, "Alert", "Add Dependent", false);

                                        } else {
                                            if ((Integer.parseInt(binding.edtNoofdepartment.getText().toString()) < dependentList.size())) {
                                                // binding.btnDependent.setVisibility(View.VISIBLE);
                                                //   Utilities.showAlertDialog(AppoinmentConfirmationActivity.this, "Alert", "Select Dependent You Want To Remove", false);
                                                binding.btnDependent.setVisibility(View.GONE);
                                            } else {
                                                if ((Integer.parseInt(binding.edtNoofdepartment.getText().toString()) == dependentList.size())) {
                                                    // binding.btnDependent.setVisibility(View.VISIBLE);
                                                    //  Utilities.showAlertDialog(AppoinmentConfirmationActivity.this, "Alert", "Select Dependent You Want To Remove", false);
                                                    binding.btnDependent.setVisibility(View.GONE);
                                                }

                                            }

                                        }

                                    }
                                }

                            }

                            Dependent_Adapter dependent_adapter = new Dependent_Adapter(AppoinmentConfirmationActivity.this, dependentList, AppoinmentConfirmationActivity.this::onDelete);
                            binding.rvDependent.setAdapter(dependent_adapter);

                            //  showTalukaListDialog(dependentListModels);
                        }

                        if (flag == 2) {
                            binding.btnDependent.setVisibility(View.GONE);

                        }
                        //   new NestedCampList_Activity.GetMonthlySurveySiteRequestForOS().execute(String.valueOf(selectedMonthId), String.valueOf(selectedYearId), DISTLGDCODE, String.valueOf(campTypeId));

                    }
                }

                if (flag == 2) {
                    binding.btnDependent.setVisibility(View.GONE);

                }

                if (noOfPendingScreeningDependant == 0){
                    binding.btnDependent.setVisibility(View.GONE);

                }

            }

            @Override
            public void onFailure(Call<DependentModel> call, Throwable t) {

            }
        });
    }

    private void getRelationList() {

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        apiInterface.getRelation().enqueue(new Callback<RelationPojo>() {
            @Override
            public void onResponse(Call<RelationPojo> call, Response<RelationPojo> response) {

                if (response.isSuccessful()) {

                    if (response.body().getStatus().equalsIgnoreCase("success")) {
                        // talukatList_models = response.body().getOutput();
                        ArrayList<RelationPojo.Output> relationPojos = response.body().getOutput();
                        if (relationPojos.size() > 0) {
                            for (RelationPojo.Output o :
                                    relationPojos) {

                                if (beneficiary.getGender().equalsIgnoreCase("Male")) {
                                    if (o.getRelId().equalsIgnoreCase("9") || (o.getRelId().equalsIgnoreCase("20"))) {
                                        relationPojos.remove(o);
                                        break;
                                    }

                                }
                                if (beneficiary.getGender().equalsIgnoreCase("Female")) {
                                    if (o.getRelId().equalsIgnoreCase("10")) {
                                        relationPojos.remove(o);
                                        break;
                                    }

                                }

                            }


                            showRelationListDialog(relationPojos);
                        }
                        //   new NestedCampList_Activity.GetMonthlySurveySiteRequestForOS().execute(String.valueOf(selectedMonthId), String.valueOf(selectedYearId), DISTLGDCODE, String.valueOf(campTypeId));

                    }
                }
            }

            @Override
            public void onFailure(Call<RelationPojo> call, Throwable t) {

            }
        });
    }

    private void shownoofdeparmenttDialog(final ArrayList<String> trenchList, String s) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(AppoinmentConfirmationActivity.this);
        builderSingle.setTitle("Select No. Of Dependent");
        builderSingle.setCancelable(false);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(AppoinmentConfirmationActivity.this, R.layout.list_row);
        if (marriedStatusId.equalsIgnoreCase("1")) {
            arrayAdapter.add("0");
            arrayAdapter.add("1");
            arrayAdapter.add("2");
            arrayAdapter.add("3");
            arrayAdapter.add("4");
            arrayAdapter.add("5");
        } else if (marriedStatusId.equalsIgnoreCase("2")) {
            arrayAdapter.add("0");
            arrayAdapter.add("1");
            arrayAdapter.add("2");
        } else if (marriedStatusId.equalsIgnoreCase("3") || marriedStatusId.equalsIgnoreCase("4")) {
            arrayAdapter.add("0");
            arrayAdapter.add("1");
            arrayAdapter.add("2");
            arrayAdapter.add("3");
            arrayAdapter.add("4");
        }

        builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                binding.edtNoofdepartment.setText(trenchList.get(which));
                no_ofdepartment = trenchList.get(which);
                Log.e("No_ofdepartment>>>", "onClick: " + no_ofdepartment);


                /////////////New Change///////////


                if (!"0".equalsIgnoreCase(screenedDependents)) {
                    int screenedCount = 0;
                    int departmentCount = 0;

                    try {
                        screenedCount = Integer.parseInt(screenedDependents);
                        departmentCount = Integer.parseInt(no_ofdepartment);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }


                    differenceCount = Math.abs(screenedCount - departmentCount);


                    if (departmentCount < screenedCount) {
                        Utilities.showAlertDialog(context, "Alert", "You have add number of dependents greater than" + " " + screenedCount + "Screened dependent", false);
                        no_ofdepartment = "0";
                        binding.edtNoofdepartment.setText("");


                        return;
                    }

                }


//                ************ Need to change logic if screened beneficiary not available ************


                if (beneficiary.getIsWorkerScreened().equalsIgnoreCase("Yes") && call_statusid == 11) {
                    binding.edtSceeendepartment.setText("0");
//                    binding.edtNoofdepartment.setText("0");
                    binding.edtSceeendepartment.setClickable(false);
//                    binding.edtNoofdepartment.setClickable(false);


                    ///////////New Change//////
//                    if ((Integer.parseInt(binding.edtNoofdepartment.getText().toString()) > dependentList.size())) {
//                        binding.btnDependent.setVisibility(View.VISIBLE);
//                        Utilities.showAlertDialog(AppoinmentConfirmationActivity.this, "Alert", "Add Dependent", false);
//
//                    } else {
//                        if ((Integer.parseInt(binding.edtNoofdepartment.getText().toString()) < dependentList.size())) {
//                            binding.btnDependent.setVisibility(View.VISIBLE);
//                            Utilities.showAlertDialog(AppoinmentConfirmationActivity.this, "Alert", "Select Dependent You Want To Remove", false);
//                            binding.btnDependent.setVisibility(View.GONE);
//                        } else {
//                            if ((Integer.parseInt(binding.edtNoofdepartment.getText().toString()) == dependentList.size())) {
//                                binding.btnDependent.setVisibility(View.VISIBLE);
//                                //  Utilities.showAlertDialog(AppoinmentConfirmationActivity.this, "Alert", "Select Dependent You Want To Remove", false);
//                                binding.btnDependent.setVisibility(View.GONE);
//                            }
//                        }
//                    }


                    if ((Integer.parseInt(binding.edtSceeendepartment.getText().toString()) > dependentList.size())) {
                        binding.btnDependent.setVisibility(View.VISIBLE);
                        Utilities.showAlertDialog(AppoinmentConfirmationActivity.this, "Alert", "Add Dependent", false);

                    } else {
                        if ((Integer.parseInt(binding.edtSceeendepartment.getText().toString()) < dependentList.size())) {
                            binding.btnDependent.setVisibility(View.VISIBLE);
                            Utilities.showAlertDialog(AppoinmentConfirmationActivity.this, "Alert", "Select Dependent You Want To Remove", false);
                            binding.btnDependent.setVisibility(View.GONE);
                        } else {
                            if ((Integer.parseInt(binding.edtSceeendepartment.getText().toString()) == dependentList.size())) {
                                binding.btnDependent.setVisibility(View.VISIBLE);
                                //  Utilities.showAlertDialog(AppoinmentConfirmationActivity.this, "Alert", "Select Dependent You Want To Remove", false);
                                binding.btnDependent.setVisibility(View.GONE);
                            }
                        }
                    }


                    //  noOfPendingScreeningDependant = 0;

                    //  Utilities.showAlertDialog(AppoinmentConfirmationActivity.this, "Alert", "You cannot select this status as worker screening is pending", false);

                } else {
                    binding.edtSceeendepartment.setText("");
                    binding.edtSceeendepartment.setClickable(true);

                }

                if (binding.edtNoofdepartment.getText().toString().matches("0")) {
                    binding.btnDependent.setEnabled(false);
                } else {
                    binding.btnDependent.setEnabled(true);
                }
            }
        });
        builderSingle.show();
    }

    private void showNoOfScreeningPendingDependantDialog(final ArrayList<String> trenchList, String s) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(AppoinmentConfirmationActivity.this);
        builderSingle.setTitle("Select Dependent Screening Pending");
        builderSingle.setCancelable(false);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(AppoinmentConfirmationActivity.this, R.layout.list_row);
        arrayAdapter.add("0");
        for (int i = 1; i <= Integer.parseInt(no_ofdepartment); i++) {
            arrayAdapter.add(String.valueOf(i));
        }
        builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                binding.edtSceeendepartment.setText(trenchList.get(which));
                noOfPendingScreeningDependant = Integer.parseInt(trenchList.get(which));

//                screenedDependents ="2";

                /////New Change///////////

                if (!"0".equalsIgnoreCase(screenedDependents)) {
                    int screenedCount = 0;
                    try {
                        screenedCount = Integer.parseInt(screenedDependents);
                    } catch (NumberFormatException e) {
                        e.printStackTrace(); // handle it appropriately
                    }

                    if (!(differenceCount == noOfPendingScreeningDependant)) {
                        Utilities.showAlertDialog(context, "Alert", "मागील ३६५ दिवसांत" + " " + screenedCount + " " + "dependent ची स्क्रिनिंग झालेली आहे, त्यामुळे त्याची गणना स्क्रिनिंग pending मध्ये करू नये.\n कृपया स्क्रिनिंगसाठी pending असलेल्या dependent ची योग्य संख्या नमूद करा.\n तसेच एकूण dependent ची संख्या बरोबर नोंदलेली आहे की नाही, याचीही खात्री करा", false);
                        noOfPendingScreeningDependant = 0;
                        binding.edtSceeendepartment.setText("");

                        binding.btnDependent.setVisibility(View.GONE);

                        return;
                    }else {
                        binding.btnDependent.setVisibility(View.VISIBLE);

                    }
                } else {


                    int screeCount = Integer.parseInt(screenedDependents);
                    int totalNode = Integer.parseInt(no_ofdepartment);

                    int sub = Math.abs(screeCount - totalNode);

                    if (!(noOfPendingScreeningDependant == sub)) {
                        Utilities.showAlertDialog(context, "Alert", "एकूण dependent संख्या आणि स्क्रीनिंग pending dependent संख्या यामध्ये तफावत दिसत आहे. कृपया खात्री करून स्क्रीनिंग pending असलेल्या dependent ची अचूक संख्या भरा.", false);
                        noOfPendingScreeningDependant = 0;
                        binding.edtSceeendepartment.setText("");

                        binding.btnDependent.setVisibility(View.GONE);

                        return;
                    }else {
                        binding.btnDependent.setVisibility(View.VISIBLE);

                    }


                }




//                ***************New Change For Dependent screening pending count************


                //***************New Change For Dependent screening pending count************


//                if (dependentList.size() > 0){
//                    if ( (Integer.parseInt(binding.edtNoofdepartment.getText().toString()) >  dependentList.size())) {
//                        binding.btnDependent.setVisibility(View.VISIBLE);
//
//                    }else {
//                        binding.btnDependent.setVisibility(View.GONE);
//
//                    }
//
//                }
                if (dependentList.size() == 0) {
                    binding.btnDependent.setVisibility(View.VISIBLE);
                }


                //////New Change////////////
//                if (dependentList.size() > 0) {
//                    if ((Integer.parseInt(binding.edtNoofdepartment.getText().toString()) > dependentList.size())) {
//                        binding.btnDependent.setVisibility(View.VISIBLE);
//                        Utilities.showAlertDialog(AppoinmentConfirmationActivity.this, "Alert", "Add Dependent", false);
//
//                    } else {
//                        if ((Integer.parseInt(binding.edtNoofdepartment.getText().toString()) < dependentList.size())) {
//                            binding.btnDependent.setVisibility(View.VISIBLE);
//                            Utilities.showAlertDialog(AppoinmentConfirmationActivity.this, "Alert", "Select Dependent You Want To Remove", false);
//                            binding.btnDependent.setVisibility(View.GONE);
//                        } else {
//                            if ((Integer.parseInt(binding.edtNoofdepartment.getText().toString()) == dependentList.size())) {
//                                binding.btnDependent.setVisibility(View.VISIBLE);
//                                //  Utilities.showAlertDialog(AppoinmentConfirmationActivity.this, "Alert", "Select Dependent You Want To Remove", false);
//                                binding.btnDependent.setVisibility(View.GONE);
//                            }
//                        }
//                    }
//                }


                if (dependentList.size() > 0) {
                    if ((Integer.parseInt(binding.edtSceeendepartment.getText().toString()) > dependentList.size())) {
                        binding.btnDependent.setVisibility(View.VISIBLE);
                        Utilities.showAlertDialog(AppoinmentConfirmationActivity.this, "Alert", "Add Dependent", false);

                    } else {
                        if ((Integer.parseInt(binding.edtSceeendepartment.getText().toString()) < dependentList.size())) {
                            binding.btnDependent.setVisibility(View.VISIBLE);
                            Utilities.showAlertDialog(AppoinmentConfirmationActivity.this, "Alert", "Select Dependent You Want To Remove", false);
                            binding.btnDependent.setVisibility(View.GONE);
                        } else {
                            if ((Integer.parseInt(binding.edtSceeendepartment.getText().toString()) == dependentList.size())) {
                                binding.btnDependent.setVisibility(View.VISIBLE);
                                //  Utilities.showAlertDialog(AppoinmentConfirmationActivity.this, "Alert", "Select Dependent You Want To Remove", false);
                                binding.btnDependent.setVisibility(View.GONE);
                            }
                        }
                    }
                }


//                binding.btnDependent.setVisibility(View.VISIBLE);


                if (noOfPendingScreeningDependant == 0){
                    binding.btnDependent.setVisibility(View.GONE);

                }
            }
        });
        builderSingle.show();
    }

    private void showTrenchListDialog(final List<OutputItem> trenchList) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(AppoinmentConfirmationActivity.this);
        builderSingle.setTitle("Select Call Status");
        builderSingle.setCancelable(false);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(AppoinmentConfirmationActivity.this, R.layout.list_row);
        for (int i = 0; i < trenchList.size(); i++) {
            arrayAdapter.add(trenchList.get(i).getCallingStatus());
        }
        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                binding.edtCallstatus.setText(trenchList.get(which).getCallingStatus());
                call_statusid = trenchList.get(which).getAssignStatusID();
                binding.inputLayoutCallStatus.setErrorEnabled(false);

                binding.etRemarkOther.setText("");

                if (binding.edtCallstatus.getText().toString().equalsIgnoreCase("Booking Confirmed")) {
                    binding.llApptDateTime.setVisibility(View.VISIBLE);
                    binding.mainllAdress.setVisibility(View.VISIBLE);
                } else {
                    binding.llApptDateTime.setVisibility(View.GONE);
                    binding.mainllAdress.setVisibility(View.GONE);

                }

                if (beneficiary.getIsWorkerScreened().equalsIgnoreCase("NO") && call_statusid == 11) {
                    binding.llApptDateTime.setVisibility(View.GONE);
                    binding.edtCallstatus.setText("");
                    Utilities.showAlertDialog(AppoinmentConfirmationActivity.this, "Alert", "You cannot select this status as worker screening is pending", false);
                }

                if (beneficiary.getIsWorkerScreened().equalsIgnoreCase("Yes") && call_statusid == 11) {
//                    binding.edtSceeendepartment.setText("0");
//                    binding.edtSceeendepartment.setClickable(false);

                    binding.edtSceeendepartment.setText("0");
//                    noOfPendingScreeningDependant = 0;
                    no_ofdepartment = "0";
//                    binding.edtNoofdepartment.setText("0");
                    binding.edtSceeendepartment.setClickable(false);
//                    binding.edtNoofdepartment.setClickable(false);

                    binding.etRemark.setText("");
                    relationId = "0";

                    //  noOfPendingScreeningDependant = 0;

                    //  Utilities.showAlertDialog(AppoinmentConfirmationActivity.this, "Alert", "You cannot select this status as worker screening is pending", false);

                } else {
                    binding.edtSceeendepartment.setClickable(true);
                    binding.edtNoofdepartment.setClickable(true);
                }

                if (call_statusid == 11) {
                    binding.llApptDateTime.setVisibility(View.GONE);
                }


                if (call_statusid == 5 || call_statusid == 4 || call_statusid == 6 || call_statusid == 7 || call_statusid == 8 || call_statusid == 9 || call_statusid == 12 || call_statusid == 13) {
                    binding.Checkbox.setEnabled(false);
                    binding.tvPersonalDependant.setEnabled(false);
                    binding.etDate.setEnabled(false);
                    binding.etTime.setEnabled(false);
                    binding.edtWorkerMarrageStatus.setEnabled(false);
                    binding.edtNoofdepartment.setEnabled(false);
                    binding.edtSceeendepartment.setEnabled(false);
                    //   binding.edtWorkerGender.setEnabled(false);

                } else {
                    binding.Checkbox.setEnabled(true);
                    binding.tvPersonalDependant.setEnabled(true);
                    binding.etDate.setEnabled(true);
                    binding.etTime.setEnabled(true);
                    binding.edtWorkerMarrageStatus.setEnabled(true);
                    binding.edtNoofdepartment.setEnabled(true);
                    binding.edtSceeendepartment.setEnabled(true);
                    //   binding.edtWorkerGender.setEnabled(true);
                }
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

    private void showTrenchListDialogForPhlebo(final List<OutputItem> trenchList) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(AppoinmentConfirmationActivity.this);
        builderSingle.setTitle("Select Call Status");
        builderSingle.setCancelable(false);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(AppoinmentConfirmationActivity.this, R.layout.list_row);
        for (int i = 0; i < trenchList.size(); i++) {
            arrayAdapter.add(trenchList.get(i).getCallingStatus());
        }
        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                binding.edtCallstatus.setText(trenchList.get(which).getCallingStatus());
                call_statusid = trenchList.get(which).getAssignStatusID();
                binding.inputLayoutCallStatus.setErrorEnabled(false);

                binding.etRemarkOther.setText("");
                binding.edtRemarkPhlebo.setText("");


                if (flag == 1) {
                    if (binding.edtCallstatus.getText().toString().equalsIgnoreCase("Booking Confirmed")) {
                        binding.llApptDateTime.setVisibility(View.VISIBLE);
                        binding.mainllAdress.setVisibility(View.VISIBLE);
                    } else {
                        binding.llApptDateTime.setVisibility(View.GONE);
                        binding.mainllAdress.setVisibility(View.GONE);

                    }


                    if (beneficiary.getIsWorkerScreened().equalsIgnoreCase("Yes") && call_statusid == 11) {
//                    binding.edtSceeendepartment.setText("0");
//                    binding.edtSceeendepartment.setClickable(false);

                        binding.edtSceeendepartment.setText("0");
//                    noOfPendingScreeningDependant = 0;
                        no_ofdepartment = "0";
//                    binding.edtNoofdepartment.setText("0");
                        binding.edtSceeendepartment.setClickable(false);
//                    binding.edtNoofdepartment.setClickable(false);

                        binding.etRemark.setText("");
                        relationId = "0";

                        //  noOfPendingScreeningDependant = 0;

                        //  Utilities.showAlertDialog(AppoinmentConfirmationActivity.this, "Alert", "You cannot select this status as worker screening is pending", false);

                    } else {
                        binding.edtSceeendepartment.setClickable(true);
                        binding.edtNoofdepartment.setClickable(true);
                    }


                    if (call_statusid == 5 || call_statusid == 4 || call_statusid == 6 || call_statusid == 7 || call_statusid == 8 || call_statusid == 9 || call_statusid == 12 || call_statusid == 13) {
                        binding.Checkbox.setEnabled(false);
                        binding.tvPersonalDependant.setEnabled(false);
                        binding.etDate.setEnabled(false);
                        binding.etTime.setEnabled(false);
                        binding.edtWorkerMarrageStatus.setEnabled(false);
                        binding.edtNoofdepartment.setEnabled(false);
                        binding.edtSceeendepartment.setEnabled(false);
                        //   binding.edtWorkerGender.setEnabled(false);

                    } else {
                        binding.Checkbox.setEnabled(true);
                        binding.tvPersonalDependant.setEnabled(true);
                        binding.etDate.setEnabled(true);
                        binding.etTime.setEnabled(true);
                        binding.edtWorkerMarrageStatus.setEnabled(true);
                        binding.edtNoofdepartment.setEnabled(true);
                        binding.edtSceeendepartment.setEnabled(true);
                        //   binding.edtWorkerGender.setEnabled(true);
                    }
                }


                if (flag == 2) {

                    if (call_statusid == 5 || call_statusid == 3 || call_statusid == 4 || call_statusid == 6 || call_statusid == 7 || call_statusid == 8 || call_statusid == 9 || call_statusid == 12 || call_statusid == 13 || call_statusid == 14) {
                        binding.Checkbox.setEnabled(false);
                        binding.tvPersonalDependant.setEnabled(false);
                        binding.etDate.setEnabled(false);
                        binding.etTime.setEnabled(false);
                        binding.edtWorkerMarrageStatus.setEnabled(false);
                        binding.edtNoofdepartment.setEnabled(false);
                        binding.edtSceeendepartment.setEnabled(false);
                        //   binding.edtWorkerGender.setEnabled(false);

                    } else {
                        binding.etDate.setEnabled(true);
                        binding.etTime.setEnabled(true);
                    }
                }

                if (beneficiary.getIsWorkerScreened().equalsIgnoreCase("NO") && call_statusid == 11) {
                    binding.llApptDateTime.setVisibility(View.GONE);
                    binding.edtCallstatus.setText("");
                    Utilities.showAlertDialog(AppoinmentConfirmationActivity.this, "Alert", "You cannot select this status as worker screening is pending", false);

                }


                if (call_statusid == 11) {
                    binding.llApptDateTime.setVisibility(View.GONE);
                }


                if (call_statusid == 2) {
                    binding.etDate.setEnabled(true);
                    binding.etTime.setEnabled(true);
                    binding.llApptDateTime.setVisibility(View.VISIBLE);

                }

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

    private void showRemarkDialog(final List<CallingRemarkModel.Output> trenchList) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(AppoinmentConfirmationActivity.this);
        builderSingle.setTitle("Select Remark");
        builderSingle.setCancelable(false);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(AppoinmentConfirmationActivity.this, R.layout.list_row);
        for (int i = 0; i < trenchList.size(); i++) {
            arrayAdapter.add(trenchList.get(i).getCallingRemark());
        }
        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                binding.etRemarkOther.setText(trenchList.get(which).getCallingRemark());
                remarkId = String.valueOf(trenchList.get(which).getCReamrkID());


                binding.edtRemarkPhlebo.setText(trenchList.get(which).getCallingRemark());
                phleboremarkId = String.valueOf(trenchList.get(which).getCReamrkID());

//                if (remarkId.equalsIgnoreCase("1") || remarkId.equalsIgnoreCase("3") || remarkId.equalsIgnoreCase("4")) {
//                    binding.inputLayoutRemark.setVisibility(View.GONE);
//                    binding.etRemark.setText(trenchList.get(which).getCallingRemark());
//                } else {
//                    binding.inputLayoutRemark.setVisibility(View.VISIBLE);
//                    binding.etRemark.setText("");
//                }


                if (remarkId.equalsIgnoreCase("2")) {
                    binding.inputLayoutRemark.setVisibility(View.VISIBLE);
                    binding.etRemark.setText("");
                } else {
                    binding.inputLayoutRemark.setVisibility(View.GONE);
                    binding.etRemark.setText(trenchList.get(which).getCallingRemark());
                }


                //  binding.inputLayoutCallStatus.setErrorEnabled(false);
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

    private void showdistrictListDialog(final List<DistrictList_Model> trenchList) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(AppoinmentConfirmationActivity.this);
        builderSingle.setTitle("Select District");
        builderSingle.setCancelable(false);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(AppoinmentConfirmationActivity.this, R.layout.list_row);
        for (int i = 0; i < trenchList.size(); i++) {
            arrayAdapter.add(trenchList.get(i).getDISTNAME());
        }
        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                binding.etDistrict.setText(trenchList.get(which).getDISTNAME());
                DISTLGDCODE = trenchList.get(which).getDISTLGDCODE();
                //  binding.inputLayoutCallStatus.setErrorEnabled(false);
//                if (binding.edtCallstatus.getText().toString().equalsIgnoreCase("Booking Confirmed")) {
//                    binding.llApptDateTime.setVisibility(View.VISIBLE);
//                }else
//                {
//                    binding.llApptDateTime.setVisibility(View.GONE);
//                }

                binding.etTaluka.setText("");
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

    private void showTalukaListDialog(final List<TalukaModel.Output> trenchList) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(AppoinmentConfirmationActivity.this);
        builderSingle.setTitle("Select Taluka");
        builderSingle.setCancelable(false);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(AppoinmentConfirmationActivity.this, R.layout.list_row);
        for (int i = 0; i < trenchList.size(); i++) {
            arrayAdapter.add(trenchList.get(i).gettALNAME());
        }
        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                binding.etTaluka.setText(trenchList.get(which).gettALNAME());
                TALLGDCODE = String.valueOf(trenchList.get(which).gettLLGDCODE());
                //  binding.inputLayoutCallStatus.setErrorEnabled(false);
//                if (binding.edtCallstatus.getText().toString().equalsIgnoreCase("Booking Confirmed")) {
//                    binding.llApptDateTime.setVisibility(View.VISIBLE);
//                }else
//                {
//                    binding.llApptDateTime.setVisibility(View.GONE);
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


    private void getRemark() {
        final ProgressDialog progressDialog = new ProgressDialog(AppoinmentConfirmationActivity.this);
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.web_forcalllist().create(ApiInterface.class);
        Call<CallingRemarkModel> call = apiService.RemarkLNew(String.valueOf(call_statusid));
        call.enqueue(new Callback<CallingRemarkModel>() {
            @Override
            public void onResponse(Call<CallingRemarkModel> call, Response<CallingRemarkModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    String status = response.body().getStatus();
                    String message = response.body().getMessage();
                    if (status.equalsIgnoreCase("Success")) {
                        List<CallingRemarkModel.Output> outputItems = response.body().getOutput();

                        if (outputItems.size() > 0) {
                            showRemarkDialog(outputItems);
                        }
                    } else {
                        Utilities.showAlertDialog(AppoinmentConfirmationActivity.this, "Alert", message, false);
                    }
                }
            }

            @Override
            public void onFailure(Call<CallingRemarkModel> call, Throwable t) {
                progressDialog.dismiss();
                t.getLocalizedMessage();

            }
        });
    }


    private void showCampType(final ArrayList<CampTypeModel> campTypeModelsList) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(AppoinmentConfirmationActivity.this);
        builderSingle.setTitle("Select Worker's Marital Status");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(AppoinmentConfirmationActivity.this, R.layout.list_row);

        for (int i = 0; i < campTypeModelsList.size(); i++) {
            arrayAdapter.add(String.valueOf(campTypeModelsList.get(i).getCampTypeName()));
        }

        builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                binding.edtWorkerMarrageStatus.setText(campTypeModelsList.get(which).getCampTypeName());
                marriedStatusId = String.valueOf(campTypeModelsList.get(which).getCampTypeId());
                if (marriedStatusId.equalsIgnoreCase("1")) {
                    binding.mainLLNoOfDependent.setVisibility(View.VISIBLE);


                } else {
                    binding.mainLLNoOfDependent.setVisibility(View.VISIBLE);
                    binding.btnDependent.setVisibility(View.VISIBLE);

                }

                binding.edtNoofdepartment.setText("");
                binding.edtSceeendepartment.setText("");

//                if (marriedStatusId.equalsIgnoreCase("2")) {
//                    binding.edtNoofdepartment.setText("");
//                    binding.edtSceeendepartment.setText("");
//                    no_ofdepartment = "0";
//
//
//                    if (dependentList != null) {
//                        dependentList.clear();
//                    }
//
//
//                    binding.headerLL.setVisibility(View.GONE);
//                    binding.viewTop.setVisibility(View.GONE);
//                    binding.viewBottom.setVisibility(View.GONE);
//
//                }

                if (marriedStatusId.equalsIgnoreCase("1") || marriedStatusId.equalsIgnoreCase("2") | marriedStatusId.equalsIgnoreCase("3") || marriedStatusId.equalsIgnoreCase("4")) {

                    //////////////New Change/////////////
//                    if (dependentList != null) {
//                        if (!(binding.edtNoofdepartment.getText().toString().isEmpty())) {
//                            if ((Integer.parseInt(binding.edtNoofdepartment.getText().toString()) > dependentList.size())) {
//                                binding.btnDependent.setVisibility(View.VISIBLE);
//                                //  Utilities.showAlertDialog(AppoinmentConfirmationActivity.this, "Alert", "Add Dependent", false);
//
//                            } else {
//
//                                if ((Integer.parseInt(binding.edtNoofdepartment.getText().toString()) < dependentList.size())) {
//                                    binding.btnDependent.setVisibility(View.VISIBLE);
//                                    //   Utilities.showAlertDialog(AppoinmentConfirmationActivity.this, "Alert", "Select Dependent You Want To Remove", false);
//                                    binding.btnDependent.setVisibility(View.GONE);
//                                }
//
//                            }
//                        }
//
//                    }


                    if (dependentList != null) {
                        if (!(binding.edtSceeendepartment.getText().toString().isEmpty())) {
                            if ((Integer.parseInt(binding.edtSceeendepartment.getText().toString()) > dependentList.size())) {
                                binding.btnDependent.setVisibility(View.VISIBLE);
                                //  Utilities.showAlertDialog(AppoinmentConfirmationActivity.this, "Alert", "Add Dependent", false);

                            } else {

                                if ((Integer.parseInt(binding.edtSceeendepartment.getText().toString()) < dependentList.size())) {
                                    binding.btnDependent.setVisibility(View.VISIBLE);
                                    //   Utilities.showAlertDialog(AppoinmentConfirmationActivity.this, "Alert", "Select Dependent You Want To Remove", false);
                                    binding.btnDependent.setVisibility(View.GONE);
                                }

                            }
                        }

                    }
                }

//                binding.edtNoofdepartment.setText("");
//                binding.edtSceeendepartment.setText("");

//
//                if (marriedStatusId.equalsIgnoreCase("1")) {
//                    binding.mainDependentLL.setVisibility(View.VISIBLE);
//                    binding.mainDependentLL.setVisibility(View.VISIBLE);
//                } else {
//                    binding.mainDependentLL.setVisibility(View.GONE);
//                    binding.mainDependentLL.setVisibility(View.GONE);
//
//
//                }

                //  refreshCalendar();


            }
        });
        builderSingle.show();
    }

    private void showGenderType(final ArrayList<CampTypeModel> campTypeModelsList) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(AppoinmentConfirmationActivity.this);
        builderSingle.setTitle("Select Worker's Gender");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(AppoinmentConfirmationActivity.this, R.layout.list_row);

        for (int i = 0; i < campTypeModelsList.size(); i++) {
            arrayAdapter.add(String.valueOf(campTypeModelsList.get(i).getCampTypeName()));
        }

        builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                binding.edtWorkerGender.setText(campTypeModelsList.get(which).getCampTypeName());
                genderId = String.valueOf(campTypeModelsList.get(which).getCampTypeId());


//                binding.edtNoofdepartment.setText("");
//                binding.edtSceeendepartment.setText("");

//
//                if (marriedStatusId.equalsIgnoreCase("1")) {
//                    binding.mainDependentLL.setVisibility(View.VISIBLE);
//                    binding.mainDependentLL.setVisibility(View.VISIBLE);
//                } else {
//                    binding.mainDependentLL.setVisibility(View.GONE);
//                    binding.mainDependentLL.setVisibility(View.GONE);
//
//
//                }

                //  refreshCalendar();


            }
        });
        builderSingle.show();
    }

    private void showRelationListDialog(final List<RelationPojo.Output> trenchList) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(AppoinmentConfirmationActivity.this);
        builderSingle.setTitle("Select Relation");
        builderSingle.setCancelable(false);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(AppoinmentConfirmationActivity.this, R.layout.list_row);
        for (int i = 0; i < trenchList.size(); i++) {
            arrayAdapter.add(trenchList.get(i).getRelName());
        }
        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                binding.etRelation.setText(trenchList.get(which).getRelName());
                relationId = String.valueOf(trenchList.get(which).getRelId());
                //  binding.inputLayoutCallStatus.setErrorEnabled(false);
//                if (binding.edtCallstatus.getText().toString().equalsIgnoreCase("Booking Confirmed")) {
//                    binding.llApptDateTime.setVisibility(View.VISIBLE);
//                }else
//                {
//                    binding.llApptDateTime.setVisibility(View.GONE);
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


//    @Override
//    public void onDelete(DependentModel dependentModel) {
//
//
//        if (flag ==1){
//
//            dependentList.remove(dependentModel);
//            Dependent_Adapter dependent_adapter = new Dependent_Adapter(AppoinmentConfirmationActivity.this, dependentList, AppoinmentConfirmationActivity.this::onDelete);
//            binding.rvDependent.setAdapter(dependent_adapter);
//
//            if (dependentList != null) {
//                if (dependentList.size() == 0) {
//                    binding.btnDependent.setVisibility(View.VISIBLE);
//                    binding.headerLL.setVisibility(View.GONE);
//                    binding.viewTop.setVisibility(View.GONE);
//                    binding.viewBottom.setVisibility(View.GONE);
//
//                    Utilities.hideSoftKeyboard(AppoinmentConfirmationActivity.this);
//
//
//                    //    Utilities.showAlertDialog(AppoinmentConfirmationActivity.this, "Alert", "Please Add Dependent", false);
//
//                }
//            }
//
//
//            if ((Integer.parseInt(binding.edtNoofdepartment.getText().toString()) > dependentList.size())) {
//                binding.btnDependent.setVisibility(View.VISIBLE);
//                Utilities.showAlertDialog(AppoinmentConfirmationActivity.this, "Alert", "Add Dependent", false);
//
//            } else {
//                if ((Integer.parseInt(binding.edtNoofdepartment.getText().toString()) < dependentList.size())) {
//                    binding.btnDependent.setVisibility(View.VISIBLE);
//                    Utilities.showAlertDialog(AppoinmentConfirmationActivity.this, "Alert", "Select Dependent You Want To Remove", false);
//                    binding.btnDependent.setVisibility(View.GONE);
//                } else {
//                    if ((Integer.parseInt(binding.edtNoofdepartment.getText().toString()) == dependentList.size())) {
//                        binding.btnDependent.setVisibility(View.VISIBLE);
//                        //  Utilities.showAlertDialog(AppoinmentConfirmationActivity.this, "Alert", "Select Dependent You Want To Remove", false);
//                        binding.btnDependent.setVisibility(View.GONE);
//                    }
//                }
//
//            }
//        }
//    }

    @Override
    public void onDelete(DependentModel dependentModel) {

///////////////New change/////////////
        String lastScreeningDateStr = dependentModel.getLastDependantScreeningDate();
        if (lastScreeningDateStr != null && !lastScreeningDateStr.isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yy", Locale.getDefault());
                Date screeningDate = sdf.parse(lastScreeningDateStr);
                Date today = new Date();

                long diffInMillis = today.getTime() - screeningDate.getTime();
                long diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMillis);
                if (diffInDays < 365) {
                    Utilities.showAlertDialog(AppoinmentConfirmationActivity.this,
                            "Alert",
                            "You cannot delete dependent screened in last 365 days.",
                            false);
                    return;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (flag == 1) {

            dependentList.remove(dependentModel);
            Dependent_Adapter dependent_adapter = new Dependent_Adapter(AppoinmentConfirmationActivity.this, dependentList, AppoinmentConfirmationActivity.this::onDelete);
            binding.rvDependent.setAdapter(dependent_adapter);

            if (dependentList != null) {
                if (dependentList.size() == 0) {
                    binding.btnDependent.setVisibility(View.VISIBLE);
                    binding.headerLL.setVisibility(View.GONE);
                    binding.viewTop.setVisibility(View.GONE);
                    binding.viewBottom.setVisibility(View.GONE);

                    Utilities.hideSoftKeyboard(AppoinmentConfirmationActivity.this);
                }
            }


            ////////////New Change///////////
//            int count = Integer.parseInt(binding.edtNoofdepartment.getText().toString());
//
//            if (count > dependentList.size()) {
//                binding.btnDependent.setVisibility(View.VISIBLE);
//                Utilities.showAlertDialog(AppoinmentConfirmationActivity.this, "Alert", "Add Dependent", false);
//            } else if (count < dependentList.size()) {
//                binding.btnDependent.setVisibility(View.GONE);
//                Utilities.showAlertDialog(AppoinmentConfirmationActivity.this, "Alert", "Select Dependent You Want To Remove", false);
//            } else if (count == dependentList.size()) {
//                binding.btnDependent.setVisibility(View.GONE);
//            }


            int count = Integer.parseInt(binding.edtSceeendepartment.getText().toString());

            if (count > dependentList.size()) {
                binding.btnDependent.setVisibility(View.VISIBLE);
                Utilities.showAlertDialog(AppoinmentConfirmationActivity.this, "Alert", "Add Dependent", false);
            } else if (count < dependentList.size()) {
                binding.btnDependent.setVisibility(View.GONE);
                Utilities.showAlertDialog(AppoinmentConfirmationActivity.this, "Alert", "Select Dependent You Want To Remove", false);
            } else if (count == dependentList.size()) {
                binding.btnDependent.setVisibility(View.GONE);
            }

        }
    }


    public class GetDistrictList extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("STATELGDCODE", params[0]));
            param.add(new ParamsPojo("DISTLGDCODE", params[1]));


            res = WebServiceCall.APICall(ApplicationConstants.GetAllDistrictList, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    districtList = new ArrayList<>();
                    ArrayList<DistrictList_Model> districtList = new ArrayList<>();
                    DistrictList_Pojo pojoDetails = new Gson().fromJson(result, DistrictList_Pojo.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        pd.dismiss();
                        districtList = pojoDetails.getOutput();
                        if (districtList.size() > 0) {
                            //  districtList.add(0, new DistrictList_Model("0", "All"));

                            showDistrictListDialog(districtList);

                        } else {
                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                        }
                    } else {
                        Utilities.showAlertDialog(context, "Fail", message, false);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
            }
        }
    }

    public class GetAppointmentCount extends AsyncTask<String, Void, String> {


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
            param.add(new ParamsPojo("UserID", params[0]));
            param.add(new ParamsPojo("AppoinmentDate", params[1]));

            //  param.add(new ParamsPojo("CatagoryID", params[6]));
            //  res = WebServiceCall.APICall(ApplicationConstants.GetActiveInactiveD2DWorkingTeamsWithCatagoryID, ApplicationConstants.webservice_d2d, param);
            res = WebServiceCall.APICall(ApplicationConstants.GetAppointmentDateCount, ApplicationConstants.webservice_forcallist, param);


            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            String type = "", message = "";
            try {
                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {

                    D2dWorkingTeamModel d2dWorkingTeamModel = new Gson().fromJson(result, D2dWorkingTeamModel.class);
                    type = d2dWorkingTeamModel.getStatus();
                    message = d2dWorkingTeamModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        pd.dismiss();

                        notWorkingList = d2dWorkingTeamModel.getOutput();
                        if (notWorkingList != null && notWorkingList.size() > 0) {


                            if (d2dWorkingTeamModel.getOutput().size() > 0) {
                                D2dWorkingTeamModel.Output output = d2dWorkingTeamModel.getOutput().get(0);


                                if (output.getAppointmentDateCount() >= 20) {
                                    android.app.AlertDialog.Builder b = new android.app.AlertDialog.Builder(context)
                                            .setTitle("Alert")
                                            .setIcon(R.drawable.icon_success)
                                            .setMessage("20 appointments are already confirmed on" + " " + binding.etDate.getText().toString() + " Are you still want to book this appointment?")
                                            .setPositiveButton("Yes",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int whichButton) {
                                                            // do something...
                                                            dialog.dismiss();
                                                        }
                                                    }
                                            )
                                            .setNegativeButton("No",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int whichButton) {
                                                            binding.etDate.setText("");
                                                        }
                                                    }
                                            );
                                    b.show();


                                }
                            }

                        } else {

                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                        }
                    } else {


                        Utilities.showAlertDialog(context, "Fail", message, false);
                    }
                } else {
                    Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                }
            } catch (Exception e) {
                e.printStackTrace();

                Utilities.showAlertDialog(context, "Please Try Again", "Server not responding", false);
            }
        }
    }

    private void showDistrictListDialog(final ArrayList<DistrictList_Model> districtList) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
        builderSingle.setTitle("Select District");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        for (int i = 0; i < districtList.size(); i++) {
            arrayAdapter.add(String.valueOf(districtList.get(i).getDISTNAME()));
        }

        builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                binding.etDistrict.setText(districtList.get(which).getDISTNAME());
                DISTLGDCODE = districtList.get(which).getDISTLGDCODE();
                //  refreshCalendar();
            }
        });
        builderSingle.show();

    }


    private void setUpToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Appointment Confirmation");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public class GetRelation extends AsyncTask<String, Void, String> {

        EditText tvRelation;
        EditText tvfname;
        EditText etMiddleName;
        EditText etLastName;
        EditText etAge;

        public GetRelation(EditText tvDivision, EditText tvfname, EditText etMiddleName, EditText etLastName, EditText etAge) {
            this.tvRelation = tvDivision;
            this.tvfname = tvfname;
            this.etMiddleName = etMiddleName;
            this.etLastName = etLastName;
            this.etAge = etAge;

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

            param.add(new ParamsPojo("MaritalStatusID", params[0]));
            param.add(new ParamsPojo("Gender", params[1]));

            res = WebServiceCall.APICall(ApplicationConstants.GetRelation_with_Marital_Status, ApplicationConstants.webservice_forcallist, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String type = "", message = "";
            pd.dismiss();
            try {
                if (!result.equals("")) {
                    //    districtList = new ArrayList<>();
                    List<RelationPojo.Output> relationList = new ArrayList<>();
                    RelationPojo relationPojo = new Gson().fromJson(result, RelationPojo.class);
                    type = relationPojo.getStatus();
                    message = relationPojo.getMessage();
                    if (type.equalsIgnoreCase("success")) {


                        relationList = relationPojo.getOutput();
                        if (relationList.size() > 0) {
                            //  districtList.add(0, new DistrictList_Model("0", "All"));


                            for (RelationPojo.Output o :
                                    relationList) {


//                                if (beneficiary.getGender().equalsIgnoreCase("Male")||binding.edtWorkerGender.getText().toString().matches("Male")) {
//                                    if (o.getRelId().equalsIgnoreCase("9") || (o.getRelId().equalsIgnoreCase("20"))) {
//                                        relationList.remove(o);
//                                        break;
//                                    }
//
//                                }
//                                if (beneficiary.getGender().equalsIgnoreCase("Female")||binding.edtWorkerGender.getText().toString().matches("Female")) {
//                                    if (o.getRelId().equalsIgnoreCase("10")) {
//                                        relationList.remove(o);
//                                        break;
//                                    }
//
//                                }
                            }

                            showDivisionListDialog(relationList, tvRelation, tvfname, etMiddleName, etLastName, etAge);

                        } else {
                            Utilities.showToastMessage(R.string.msgt_emptylist, AppoinmentConfirmationActivity.this, false);
                        }
                    } else {
                        Utilities.showAlertDialog(AppoinmentConfirmationActivity.this, "Fail", message, false);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(AppoinmentConfirmationActivity.this, "Server Not Responding", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showDivisionListDialog(final List<RelationPojo.Output> divisionList, TextView tvRelation, EditText tvfname, EditText etMiddleName, EditText etLastName, EditText etAge) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(AppoinmentConfirmationActivity.this);
        builderSingle.setTitle("Select Relation");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(AppoinmentConfirmationActivity.this, R.layout.list_row);

        for (int i = 0; i < divisionList.size(); i++) {
            arrayAdapter.add(String.valueOf(divisionList.get(i).getRelName()));
        }

        builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tvRelation.setText(divisionList.get(which).getRelName());
                relationId = String.valueOf(divisionList.get(which).getRelId());
                //  refreshCalendar();


                etAge.setText("");


///////////////////////////////New Change/////////////////////////////
//                if (relationId.equalsIgnoreCase("1")||relationId.equalsIgnoreCase("2")
//                        ||relationId.equalsIgnoreCase("21")||relationId.equalsIgnoreCase("22")||relationId.equalsIgnoreCase("9")
//                        ||relationId.equalsIgnoreCase("10")){
//                    for (DependentModel dependent : dependentList) {
//                        if (dependent.getRelId().equals(relationId)) {
//                            Toast.makeText(AppoinmentConfirmationActivity.this, "This relation is already added.", Toast.LENGTH_SHORT).show();
//
//                            tvRelation.setText("");
//                            return;
//                        }
//                    }
//
//                } else if (relationId.equalsIgnoreCase("7") || relationId.equalsIgnoreCase("8")) {
//                    int count = 0;
//                    for (DependentModel dependent : dependentList) {
//                        if (dependent.getRelId().equals(relationId)) {
//                            count++;
//                        }
//                    }
//                    if (count >= 2) {
//                        Toast.makeText(AppoinmentConfirmationActivity.this, "You can add this relation only twice.", Toast.LENGTH_SHORT).show();
//                        tvRelation.setText("");
//                        return;
//                    }
//                }

//
//                if (relationId.equalsIgnoreCase("1") || relationId.equalsIgnoreCase("2")
//                        || relationId.equalsIgnoreCase("21") || relationId.equalsIgnoreCase("22")
//                        || relationId.equalsIgnoreCase("9") || relationId.equalsIgnoreCase("10")) {
//
//                    for (DependentModel dependent : dependentList) {
//                        if (dependent.getRelId().equals(relationId)) {
//                            Toast.makeText(AppoinmentConfirmationActivity.this, "This relation is already added.", Toast.LENGTH_SHORT).show();
//                            tvRelation.setText("");
//                            return;
//                        }
//                    }
//
//                } else if (relationId.equalsIgnoreCase("7") || relationId.equalsIgnoreCase("8")) {
//                    int count7 = 0;
//                    int count8 = 0;
//
//                    for (DependentModel dependent : dependentList) {
//                        if (dependent.getRelId().equals("7")) {
//                            count7++;
//                        } else if (dependent.getRelId().equals("8")) {
//                            count8++;
//                        }
//                    }
//
//                    if (count7 >= 2 || count8 >= 2 || (count7 >= 1 && count8 >= 1)) {
//                        Toast.makeText(AppoinmentConfirmationActivity.this, "Maximum two children registration is allowed", Toast.LENGTH_SHORT).show();
//                        tvRelation.setText("");
//                        return;
//                    }
//                }


                if (relationId.equalsIgnoreCase("1") || relationId.equalsIgnoreCase("2")
                        || relationId.equalsIgnoreCase("21") || relationId.equalsIgnoreCase("22")
                        || relationId.equalsIgnoreCase("9") || relationId.equalsIgnoreCase("10")) {

                    for (DependentModel dependent : dependentList) {
                        if (dependent.getRelId().equals(relationId)) {
                            Toast.makeText(AppoinmentConfirmationActivity.this, "This relation is already added.", Toast.LENGTH_SHORT).show();
                            tvRelation.setText("");
                            return;
                        }
                    }

                    // Also check in screenedDependentModelList
                    if (screenedDependentModelList != null && !screenedDependentModelList.isEmpty()) {
                        for (ScreenedDependentModel.Output screened : screenedDependentModelList) {
                            if (screened.getRelId().equals(relationId)) {
                                Toast.makeText(AppoinmentConfirmationActivity.this, "This relation is already added.", Toast.LENGTH_SHORT).show();
                                tvRelation.setText("");
                                return;
                            }
                        }
                    }

                } else if (relationId.equalsIgnoreCase("7") || relationId.equalsIgnoreCase("8")) {
                    int count7 = 0;
                    int count8 = 0;

                    for (DependentModel dependent : dependentList) {
                        if (dependent.getRelId().equals("7")) {
                            count7++;
                        } else if (dependent.getRelId().equals("8")) {
                            count8++;
                        }
                    }

                    if (screenedDependentModelList != null && !screenedDependentModelList.isEmpty()) {
                        for (ScreenedDependentModel.Output screened : screenedDependentModelList) {
                            if (screened.getRelId().equals("7")) {
                                count7++;
                            } else if (screened.getRelId().equals("8")) {
                                count8++;
                            }
                        }
                    }

                    if (count7 >= 2 || count8 >= 2 || (count7 >= 1 && count8 >= 1)) {
                        Toast.makeText(AppoinmentConfirmationActivity.this, "Maximum two children registration is allowed", Toast.LENGTH_SHORT).show();
                        tvRelation.setText("");
                        return;
                    }
                }


                if (relationId.equalsIgnoreCase("1")) {

                    if (gender.equalsIgnoreCase("Female") && binding.edtWorkerGender.getText().toString().matches("Female")) {
                        tvfname.setText(mname);
                        etMiddleName.setText("");
                        etLastName.setText(lname);
                    } else if (gender.equalsIgnoreCase("Female") && binding.edtWorkerGender.getText().toString().matches("Male")) {
                        tvfname.setText("");
                        etMiddleName.setText("");
                        etLastName.setText(lname);
                    } else if (gender.equalsIgnoreCase("Male") && binding.edtWorkerGender.getText().toString().matches("Male")) {
                        tvfname.setText(mname);
                        etMiddleName.setText("");
                        etLastName.setText(lname);
                    } else if (gender.equalsIgnoreCase("Male") && binding.edtWorkerGender.getText().toString().matches("Female")) {
                        tvfname.setText("");
                        etMiddleName.setText("");
                        etLastName.setText(lname);
                    }

                } else if (relationId.equalsIgnoreCase("2")) {

                    if (gender.equalsIgnoreCase("Female") && binding.edtWorkerGender.getText().toString().matches("Female")) {
                        tvfname.setText("");
                        etMiddleName.setText(mname);
                        etLastName.setText(lname);
                    } else if (gender.equalsIgnoreCase("Female") && binding.edtWorkerGender.getText().toString().matches("Male")) {
                        tvfname.setText("");
                        etMiddleName.setText("");
                        etLastName.setText(lname);
                    } else if (gender.equalsIgnoreCase("Male") && binding.edtWorkerGender.getText().toString().matches("Male")) {
                        tvfname.setText("");
                        etMiddleName.setText(mname);
                        etLastName.setText(lname);
                    } else if (gender.equalsIgnoreCase("Male") && binding.edtWorkerGender.getText().toString().matches("Female")) {
                        tvfname.setText("");
                        etMiddleName.setText("");
                        etLastName.setText(lname);
                    }


//                    etMiddleName.setText(mname);
//                    tvfname.setText("");
//                    etLastName.setText(lname);

                } else if (relationId.equalsIgnoreCase("21")) {
                    tvfname.setText("");
                    etMiddleName.setText("");
                    etLastName.setText("");

                } else if (relationId.equalsIgnoreCase("22")) {
                    tvfname.setText("");
                    etMiddleName.setText("");
                    etLastName.setText("");
                } else if (relationId.equalsIgnoreCase("7") || relationId.equalsIgnoreCase("8")) {

                    if (gender.equalsIgnoreCase("Male") && binding.edtWorkerGender.getText().toString().matches("Male")) {
                        tvfname.setText("");
                        etMiddleName.setText(fname);
                        etLastName.setText(lname);
                    } else if (gender.equalsIgnoreCase("Female") || binding.edtWorkerGender.getText().toString().matches("Female")) {
                        tvfname.setText("");
                        etMiddleName.setText(mname);
                        etLastName.setText(lname);
                    }

                } else if (relationId.equalsIgnoreCase("9")) {

                    tvfname.setText(mname);
                    etMiddleName.setText("");
                    etLastName.setText(lname);

                } else if (relationId.equalsIgnoreCase("10")) {

                    if (gender.equalsIgnoreCase("Female") && binding.edtWorkerGender.getText().toString().matches("Male")) {
                        tvfname.setText("");
                        etMiddleName.setText("");
                        etLastName.setText(lname);
                    } else if (gender.equalsIgnoreCase("Male") || binding.edtWorkerGender.getText().toString().matches("Male")) {

                        tvfname.setText("");
                        etMiddleName.setText(fname);
                        etLastName.setText(lname);
                    }

                }
            }
        });
        builderSingle.show();
    }


    public class GetBeneficiaryData extends AsyncTask<String, Void, String> {


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
            param.add(new ParamsPojo("AssignCallID", params[0]));
            res = WebServiceCall.APICall(ApplicationConstants.GetBeneficiaryAddressDetails, ApplicationConstants.webservice_forcallist, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            pd.dismiss();
            String type = "", message = "";
            try {
                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {

                    BeneficiaryDetailsCallingModuleModel adminActiveInactiveModel = new Gson().fromJson(result, BeneficiaryDetailsCallingModuleModel.class);
                    type = adminActiveInactiveModel.getStatus();
                    message = adminActiveInactiveModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        adminlist = adminActiveInactiveModel.getOutput();
                        if (adminlist != null && adminlist.size() > 0) {


                            if (adminActiveInactiveModel.getOutput().size() > 0) {
                                BeneficiaryDetailsCallingModuleModel.Output output = adminActiveInactiveModel.getOutput().get(0);

//                                b_name = output.getBeneficiaryName();
                                //   gender = output.getGender();
                                // b_mobile = output.getMobile();
                                // AssignCallID = String.valueOf(beneficiary.getAssignCallID());
                                //  binding.tvName.setText(b_name);


                                b_address = output.getRegAddress();
                                binding.etDistrict.setText(output.getDistrict());
                                DISTLGDCODE = String.valueOf(output.getDistlgdcode());
                                binding.etTaluka.setText(output.getTaluka());
                                TALLGDCODE = String.valueOf(output.getTallgdcode());
                                binding.editTextRegister.setText(b_address + "," + output.getPincode());

                                if (output.getPincode() == 0) {
                                    binding.etPincode.setText("");
                                } else {
                                    binding.etPincode.setText("" + output.getPincode());
                                }
                                binding.etHouseNo.setText(output.getHouseNo());
                                binding.etRoad.setText(output.getRoad());
                                binding.etArea.setText(output.getArea());
                                binding.etLandmark.setText(output.getLandMark());
                                date = String.valueOf(output.getAppoinmentDate());
                                binding.etDate.setText(date);
                                time = String.valueOf(output.getAppoinmentTime());
                                binding.etTime.setText(time);
                                fname = output.getFirstName();
                                mname = output.getMiddleName();
                                lname = output.getLastName();
                                CallingLog = output.getCallingLog();
                                remarkId = output.getRemarkID();

                                gender = output.getGender();

                                binding.edtWorkerGender.setText(output.getGender());

                                binding.etAltMobilenumber.setText(output.getAltMobileNo());

                                remark = output.getRemark();


//                                if (remark != null) {
//
//                                    if (remark.equalsIgnoreCase("Reschedule Post Address validation")
//                                            || remark.equalsIgnoreCase("Check With IT")
//                                            || remark.equalsIgnoreCase("Beneficiary Already Registered")
//                                            || remark.equalsIgnoreCase("Beneficiary Already Screened")
//                                            || remark.equalsIgnoreCase("Beneficiary Migrated (Permanent)")
//                                            || remark.equalsIgnoreCase("Beneficiary Migrated (Temporary)")
//                                            || remark.equalsIgnoreCase("Communication With Vendor")
//                                            || remark.equalsIgnoreCase("Interested In Screening")
//                                            || remark.equalsIgnoreCase("Multiple Cards In Family")
//                                            || remark.equalsIgnoreCase("Not Interested In Screening")) {
//                                        binding.etRemarkOther.setText("" + remark);
//                                        binding.etRemark.setText("" + remark);
//                                    } else {
//                                        binding.etRemarkOther.setText("Other");
//                                        binding.etRemark.setText("" + remark);
//                                        binding.inputLayoutRemark.setVisibility(View.VISIBLE);
//                                    }
//
//                                }

                                if (remarkId != null) {
                                    if (remarkId.equalsIgnoreCase("2")) {
                                        binding.etRemarkOther.setText("Other");
                                        binding.etRemark.setText("" + remark);
                                        binding.inputLayoutRemark.setVisibility(View.VISIBLE);
                                    } else {

                                        binding.etRemarkOther.setText("" + remark);
                                        binding.etRemark.setText("" + remark);
                                    }
                                }


//                                if (remark != null) {
//
//                                    if (remark.equalsIgnoreCase("Other")) {
//                                        binding.etRemarkOther.setText("Other");
//                                        binding.etRemark.setText("" + remark);
//                                        binding.inputLayoutRemark.setVisibility(View.VISIBLE);
//                                    }
//
//                                } else {
//                                    binding.etRemarkOther.setText("" + remark);
//                                    binding.etRemark.setText("" + remark);
//
//                                }


                                if (output.getGender() != null) {

                                    if (output.getGender().equalsIgnoreCase("Male")) {
                                        binding.etMiddleName.setText(output.getFirstName());
                                        binding.etLastName.setText(output.getLastName());
                                    } else {
                                        binding.etLastName.setText(output.getLastName());

                                    }

                                }


                                marriedStatusId = output.getWorkersMaritalStatus();


                                if (marriedStatusId == null) {
                                    marriedStatusId = "0";
                                }


                                if (marriedStatusId != null) {
                                    if (marriedStatusId.equalsIgnoreCase("1")) {
                                        binding.edtWorkerMarrageStatus.setText("Married");
//                binding.headerLL.setVisibility(View.VISIBLE);
//                binding.viewTop.setVisibility(View.VISIBLE);
//                binding.viewBottom.setVisibility(View.VISIBLE);
                                        binding.mainLLNoOfDependent.setVisibility(View.VISIBLE);
                                        binding.mailPersonalLL.setVisibility(View.VISIBLE);

                                        if (flag == 1) {
                                            binding.btnDependent.setVisibility(View.VISIBLE);

                                        }

                                    } else if (marriedStatusId.equalsIgnoreCase("2")) {
                                        binding.edtWorkerMarrageStatus.setText("UnMarried");
                                    } else if (marriedStatusId.equalsIgnoreCase("0")) {
                                        binding.edtWorkerMarrageStatus.setText("");
                                    } else if (marriedStatusId.equalsIgnoreCase("3")) {
                                        binding.edtWorkerMarrageStatus.setText("Divorcee");

                                    } else if (marriedStatusId.equalsIgnoreCase("4")) {
                                        binding.edtWorkerMarrageStatus.setText("Widow");
                                    }

                                }

                                if (beneficiary.getAssignStatusID() == 2 || beneficiary.getAssignStatusID() == 3 || beneficiary.getAssignStatusID() == 4 || beneficiary.getAssignStatusID() == 5 || beneficiary.getAssignStatusID() == 12
                                        || beneficiary.getAssignStatusID() == 7 || beneficiary.getAssignStatusID() == 9 || beneficiary.getAssignStatusID() == 8) {
                                    //  marriedStatusId = beneficiary.getWorkersMaritalStatus();
                                    noOfPendingScreeningDependant = Integer.parseInt(beneficiary.getDependantScreeningPending());
                                    if (marriedStatusId != null) {

                                        if (marriedStatusId.equalsIgnoreCase("1")) {
                                            binding.edtWorkerMarrageStatus.setText("Married");
//                    binding.headerLL.setVisibility(View.VISIBLE);
//                    binding.viewTop.setVisibility(View.VISIBLE);
//                    binding.viewBottom.setVisibility(View.VISIBLE);
                                            binding.mainLLNoOfDependent.setVisibility(View.VISIBLE);
                                            binding.mailPersonalLL.setVisibility(View.VISIBLE);

                                        } else if (marriedStatusId.equalsIgnoreCase("2")) {
                                            binding.edtWorkerMarrageStatus.setText("UnMarried");
                                        } else if (marriedStatusId.equalsIgnoreCase("0")) {
                                            binding.edtWorkerMarrageStatus.setText("");
                                        }

                                    }


                                }


                                getDependentList();


                            }

                        } else {
//                            binding.etPincode.setText("");
                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                        }
                    } else {
//                        binding.etPincode.setEnabled(true);
                        binding.etPincode.setText("");
                        Utilities.showAlertDialog(AppoinmentConfirmationActivity.this, "Fail", message, false);
                    }
                } else {
                    Utilities.showToastMessage(R.string.msgt_emptylist, AppoinmentConfirmationActivity.this, false);
                }
            } catch (Exception e) {
                e.printStackTrace();

                Utilities.showAlertDialog(AppoinmentConfirmationActivity.this, "Please Try Again", "Server not responding", false);
            }
        }
    }


    private void getScreeningCount(String screeningType) {
        final ProgressDialog progressDialog = new ProgressDialog(AppoinmentConfirmationActivity.this);
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.getD2DClient().create(ApiInterface.class);
        Call<ScreenedDependentModel> call = apiService.getScreenedDependentCount(AssignCallID, screeningType);
        call.enqueue(new Callback<ScreenedDependentModel>() {
            @Override
            public void onResponse(Call<ScreenedDependentModel> call, Response<ScreenedDependentModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    String status = response.body().getStatus();
                    String message = response.body().getMessage();
                    if (status.equalsIgnoreCase("Success")) {
                        List<ScreenedDependentModel.Output> screenedDetailsList = response.body().getOutput();

//////////////////New Change/////////////
                        if (screenedDetailsList != null && !screenedDetailsList.isEmpty()) {
                            ScreenedDependentModel.Output output = screenedDetailsList.get(0);

                            screenedDependents = output.getNoOFScreenedDepedent();


                            int screenedCount = 0;
                            int departmentCount = 0;

                            try {
                                screenedCount = Integer.parseInt(screenedDependents);
                                departmentCount = Integer.parseInt(no_ofdepartment);
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }


                            differenceCount = Math.abs(screenedCount - departmentCount);


                            getScreenedBeneficiaryList("1");

                        }


                    } else {

//                        Utilities.showAlertDialog(context, "Alert", "Details Not Found", false);
                        screenedDependents = "0";


                    }
                } else {

//                    Utilities.showAlertDialog(context, "Alert", "Details Not Found", false);

                }
            }

            @Override
            public void onFailure(Call<ScreenedDependentModel> call, Throwable t) {
                t.getLocalizedMessage();
                progressDialog.dismiss();

            }
        });

    }

    private void getScreenedBeneficiaryList(String screeningType) {
        ApiInterface apiInterface = ApiClient.getD2DClient().create(ApiInterface.class);
        apiInterface.getScreenedDependentCount(AssignCallID, screeningType).enqueue(new Callback<ScreenedDependentModel>() {
            @Override
            public void onResponse(Call<ScreenedDependentModel> call, Response<ScreenedDependentModel> response) {

                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase("success")) {
                        screenedDependentModelList = response.body().getOutput();  // ✅ Use global variable
//                        screenedDependentList = new ArrayList<>();

                        if (screenedDependentModelList != null && screenedDependentModelList.size() > 0) {
                            binding.headerLLScreened.setVisibility(View.VISIBLE);
                            binding.viewTopScreened.setVisibility(View.VISIBLE);
                            binding.viewBottomScreened.setVisibility(View.VISIBLE);

                            Screened_Dependent_Adapter screened_dependent_adapter =
                                    new Screened_Dependent_Adapter(AppoinmentConfirmationActivity.this, screenedDependentModelList);
                            binding.rvDependentScreened.setAdapter(screened_dependent_adapter);
                        }

                        if (flag == 2) {
                            binding.btnDependent.setVisibility(View.GONE);
                        }
                    }
                }


            }

            @Override
            public void onFailure(Call<ScreenedDependentModel> call, Throwable t) {

            }
        });
    }

}