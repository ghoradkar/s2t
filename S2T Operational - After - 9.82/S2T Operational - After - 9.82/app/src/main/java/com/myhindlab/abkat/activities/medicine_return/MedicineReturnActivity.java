package com.myhindlab.abkat.activities.medicine_return;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.BarcodeScanner_Activity;
import com.myhindlab.abkat.adapters.PackageReturnNew;
import com.myhindlab.abkat.adapters.ReportDeliveryCampIdAdapter;
import com.myhindlab.abkat.adapters.TeamsDetailsAssignedReportDeliveryAdapter;
import com.myhindlab.abkat.models.CampListModel;
import com.myhindlab.abkat.models.CampTypeModel;
import com.myhindlab.abkat.models.DistrictList_Model;
import com.myhindlab.abkat.models.GetTeamsModel;
import com.myhindlab.abkat.models.LandingLabModel;
import com.myhindlab.abkat.models.PacketReturnModel;
import com.myhindlab.abkat.models.TalukaModel;
import com.myhindlab.abkat.models.TeamsDetailsModel;
import com.myhindlab.abkat.models.doortodoor.TeamNumberModel;
import com.myhindlab.abkat.pojos.DistrictList_Pojo;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.RecyclerItemClickListener;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MedicineReturnActivity extends AppCompatActivity {
    private Context context;
    private ProgressDialog pd;
    private UserSessionManager session;
    private Button btn_accept_courier;
    private TextView tv_block, tv_resourceNme, tv_taluka, tv_from_date, tv_to_date, tvDate, tv_teamNumber, tvCamp, tvCampType, tv_district;
    private ArrayList<GetTeamsModel.OutputBean> teamList;
    private int mYear, mMonth, mDay, mYear1, mMonth1, mDay1;
    private SearchView searchview_campname;
//    private androidx.appcompat.widget.SearchView ;

    private RadioGroup rg_selection;

    private RadioButton rb_regId, rb_campId;
    private AlertDialog teamDialog;
    private List<CampListModel.OutputBean> assignteamlist;
    private RecyclerView rv_patient_list;
    PackageReturnNew postCampBeneficiaryAdapter;

    private ArrayList<DistrictList_Model> districtList;

    private ArrayList<PacketReturnModel.Output> filteredList = new ArrayList<>();

    private ArrayList<TeamNumberModel.Output> teamlist;
    private LinearLayout ll_buttons, llTeamNumber,
            mainllCampId, mainLLDate, LL_taluka, LL_delivery_executive,
            campDate, mainllCampType, mainLL_search, llDistrict;


    private CardView cardviewSearch, mainLL_searchView;
    String fromDate;

    private CheckBox cb_accept;

    private RadioButton rb_board, rb_manual;
    private MaterialEditText edt_barcode1;

    private ImageView imv_barcode1;
    private ImageView imv_search;


    private String campId = "0", callType, landinglabId = "0", talukaId = "0", resourceId = "0", isAdmin, campTypeId, EmpCode, STATELGDCODE = "2", DESGID, teamId = "0",
            district, TALLGDCODE, DISTLGDCODE, taluka, LabCode, CampDate, CampDateToDate, AssignedId;

    private LocalBroadcastManager localBroadcastManager;
    private List<PacketReturnModel.Output> campBeneficiaryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_medicine);

        initView();
        setUpToolbar();
        getSessionData();
        setDefaults();
        setEventHandler();

    }

    private void initView() {
        context = MedicineReturnActivity.this;
        pd = new ProgressDialog(context);
        session = new UserSessionManager(context);
        searchview_campname = findViewById(R.id.searchview_campname);
        tv_block = findViewById(R.id.tv_block);
        ll_buttons = findViewById(R.id.ll_buttons);
        tv_teamNumber = findViewById(R.id.tv_teamNumber);
        tv_district = findViewById(R.id.tv_district);
        imv_barcode1 = findViewById(R.id.imv_barcode1);
        edt_barcode1 = findViewById(R.id.edt_barcode1);
        btn_accept_courier = findViewById(R.id.btn_accept_courier);
        imv_search = findViewById(R.id.imv_search);
        rb_manual = findViewById(R.id.rb_manual);
        rb_board = findViewById(R.id.rb_board);
        mainLL_search = findViewById(R.id.mainLL_search);

        searchview_campname.setImeOptions(EditorInfo.IME_ACTION_DONE);

        tv_resourceNme = findViewById(R.id.tv_resourceNme);
        rv_patient_list = findViewById(R.id.rv_patient_list);
        rv_patient_list.setLayoutManager(new LinearLayoutManager(context));
        tv_from_date = findViewById(R.id.tv_from_date);
        tv_to_date = findViewById(R.id.tv_to_date);
        tv_taluka = findViewById(R.id.tv_taluka);
        llTeamNumber = findViewById(R.id.llTeamNumber);
        llDistrict = findViewById(R.id.llDistrict);
        rg_selection = findViewById(R.id.rg_selection);
        rb_regId = findViewById(R.id.rb_regId);
        rb_campId = findViewById(R.id.rb_campId);
        tvDate = findViewById(R.id.tvDate);
        tvCamp = findViewById(R.id.tvCamp);
        mainLLDate = findViewById(R.id.mainLLDate);
        LL_taluka = findViewById(R.id.LL_taluka);
        LL_delivery_executive = findViewById(R.id.LL_delivery_executive);
        cardviewSearch = findViewById(R.id.cardviewSearch);
        rb_campId = findViewById(R.id.rb_campId);
        rb_regId = findViewById(R.id.rb_regId);
        campDate = findViewById(R.id.campDate);
        mainllCampId = findViewById(R.id.mainllCampId);
        tvCampType = findViewById(R.id.tvCampType);
        mainllCampType = findViewById(R.id.mainllCampType);
        cb_accept = findViewById(R.id.cb_accept);
        mainLL_searchView = findViewById(R.id.mainLL_searchView);


        Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);
//
//        Calendar todayCal = Calendar.getInstance();
//        todayCal.add(Calendar.DAY_OF_MONTH, -22);
//
        Calendar toCal = Calendar.getInstance();
        toCal.add(Calendar.DAY_OF_MONTH, -3);


        campDate.setVisibility(View.GONE);
        mainllCampId.setVisibility(View.GONE);
        mainllCampType.setVisibility(View.GONE);
        mainLLDate.setVisibility(View.VISIBLE);

        tv_to_date.setText(Utilities.dfDate4.format(new Date()));
        tv_from_date.setText(Utilities.dfDate4.format(toCal.getTime()));

        fromDate = Utilities.dfDate4.format(toCal.getTime());
        CampDateToDate = Utilities.dfDate4.format(new Date());

        tvDate.setText(Utilities.dfDate4.format(new Date()));
        CampDate = Utilities.dfDate4.format(new Date());


        SimpleDateFormat mdformat = new SimpleDateFormat("dd-MM-yyy");
//        tv_from_date.setText(Utilities.getPreviousDate());
//        tv_to_date.setText(mdformat.format(calendar.getTime()));

        campBeneficiaryList = new ArrayList<>();

        rb_board.isChecked();
    }

    private void getSessionData() {

        try {
            JSONArray user_info = new JSONArray(session.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                DESGID = json.getString("DESGID");
                DISTLGDCODE = json.getString("DISTLGDCODE");
                district = json.getString("district");
                TALLGDCODE = json.getString("TALLGDCODE");
                taluka = json.getString("taluka");
                EmpCode = json.getString("EmpCode");
                LabCode = json.getString("LabCode");
            }


            if (DESGID.equalsIgnoreCase("64") || (DESGID.equalsIgnoreCase("35") || DESGID.equalsIgnoreCase("86"))) {
                tv_district.setText(district);

            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setUpToolbar() {
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        TextView titleTextView = new TextView(this);
//
//        titleTextView.setText("Medicine Delivery Confirmation");
//        titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 2);  // Set size in SP
//        toolbar.addView(titleTextView);
//
////        getSupportActionBar().setTitle("Medicine Delivery Confirmation");
//
//
//
//        //        getSupportActionBar().setTitle("Medicine Delivery Confirmation");
//
//
//
//
////        toolbar.setNavigationIcon(R.drawable.icon_back);
//
//        toolbar.setNavigationOnClickListener(v -> finish());


        Toolbar toolbar = findViewById(R.id.toolbar);
        ImageView btn_registration = findViewById(R.id.iv_info);

        setSupportActionBar(toolbar);


//        btn_registration.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Dialog dialog;
//                dialog = new Dialog(MedicineReturnActivity.this);
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialog.setContentView(R.layout.colour_info_dialoge_for_packet_reallocation);
//                dialog.setCanceledOnTouchOutside(true);
//                dialog.show();
//                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//
//                ImageView btnCloseDialog = dialog.findViewById(R.id.btnCloseDialog);
//                btnCloseDialog.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });
//            }
//        });

// Disable the default toolbar title
        getSupportActionBar().setDisplayShowTitleEnabled(false);

// Create a custom TextView for the title
        TextView titleTextView = new TextView(this);

// Set the text, size, color, and other properties
        titleTextView.setText("Packet Return To Pharmacy");
        titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);  // Set size in SP
        titleTextView.setTextColor(Color.WHITE);                    // Set text color
//        titleTextView.setTypeface(Typeface.DEFAULT_BOLD);            // Optional, bold text

// Set layout params to ensure it displays properly
//        Toolbar.LayoutParams layoutParams = new Toolbar.LayoutParams(
//                Toolbar.LayoutParams.WRAP_CONTENT,
//                Toolbar.LayoutParams.WRAP_CONTENT,
////                Gravity.CENTER_HORIZONTAL);  // Align the title in the center
//        titleTextView.setLayoutParams(layoutParams);

// Add the custom TextView to the Toolbar
        toolbar.addView(titleTextView);


        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
//
        toolbar.setNavigationOnClickListener(v -> finish());

    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            apiCall();
            String regId = intent.getStringExtra("regId");
            String campId = intent.getStringExtra("campId");
            Log.d("BroadcastReceiver", "onReceive: " + regId);


            if (DESGID.equalsIgnoreCase("92") || (DESGID.equalsIgnoreCase("29") || (DESGID.equalsIgnoreCase("104") || (DESGID.equalsIgnoreCase("162") || DESGID.equalsIgnoreCase("78") || DESGID.equalsIgnoreCase("77") || DESGID.equalsIgnoreCase("128") || DESGID.equalsIgnoreCase("131") || DESGID.equalsIgnoreCase("160"))))) {

            } else if (DESGID.equalsIgnoreCase("64") || (DESGID.equalsIgnoreCase("35") || DESGID.equalsIgnoreCase("86"))) {

            }

        }
    };


    private void setDefaults() {
        campId = getIntent().getStringExtra("campId");
        callType = getIntent().getStringExtra("callType");
        isAdmin = getIntent().getStringExtra("isAdmin");


        if (rb_board.isChecked()) {
            mainLLDate.setVisibility(View.VISIBLE);
//            llDistrict.setVisibility(View.GONE);
            LL_taluka.setVisibility(View.VISIBLE);
            LL_delivery_executive.setVisibility(View.VISIBLE);
            rb_board.setTextColor(Color.parseColor("#ff59a9ff"));

        } else {
            mainLLDate.setVisibility(View.VISIBLE);
            llDistrict.setVisibility(View.VISIBLE);
            LL_delivery_executive.setVisibility(View.VISIBLE);
            mainLL_search.setVisibility(View.GONE);
            mainLL_searchView.setVisibility(View.VISIBLE);
            rb_board.setTextColor(Color.parseColor("#000000"));
        }

        new GetTalukaNew().execute(EmpCode, DISTLGDCODE);


//
//        if ((DESGID.equalsIgnoreCase("64") || (DESGID.equalsIgnoreCase("35") || DESGID.equalsIgnoreCase("86")))) {
//            tv_district.setEnabled(false);
//            tv_district.setBackgroundColor(this.getResources().getColor(R.color.greylight));
//
//        }


        localBroadcastManager = LocalBroadcastManager.getInstance(context);
        IntentFilter intentFilter = new IntentFilter("MedicineDeliveryActivity");
        localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);


    }

    private void setEventHandler() {

//        searchview_campname.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                searchview_campname.clearFocus();
//                ArrayList<PacketReturnModel.Output> facilitySearchedList = new ArrayList<>();
//                for (PacketReturnModel.Output facility : campBeneficiaryList) {
//                    if (facility.getPatientName() != null &&
//                            facility.getPatientName().toLowerCase().startsWith(query.toLowerCase())) {
//                        facilitySearchedList.add(facility);
//                    }
//                }
//                if (facilitySearchedList.size() == 0) {
//                    Utilities.showToastMessage("No Such Customer Name Found", context, false);
//                    facilitySearchedList.clear();
//
//                    postCampBeneficiaryAdapter = new PackageReturnNew(context, facilitySearchedList, "0", "0");
//                    rv_patient_list.setAdapter(postCampBeneficiaryAdapter);
//
//
////                    setRecyclerView(facilitySearchedList);
//                } else {
//
//                    postCampBeneficiaryAdapter = new PackageReturnNew(context, facilitySearchedList, "0", "0");
//                    rv_patient_list.setAdapter(postCampBeneficiaryAdapter);
////                    setRecyclerView(facilitySearchedList);
//                }
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                ArrayList<PacketReturnModel.Output> facilitySearchedList = new ArrayList<>();
//                if (newText.equals("")) {
//                    //searchView.clearFocus();
//                    facilitySearchedList.clear();
//
//                    postCampBeneficiaryAdapter = new PackageReturnNew(context, campBeneficiaryList, "0", "0");
//                    rv_patient_list.setAdapter(postCampBeneficiaryAdapter);
//
////                    setRecyclerView(sampleCollectionModelList);
//                    Log.e("TAG", "onQueryTextChange: ");
//                } else {
//                    for (PacketReturnModel.Output facility : campBeneficiaryList) {
//                        if (facility.getPatientName() != null &&
//                                facility.getPatientName().toLowerCase().startsWith(newText.toLowerCase())) {
//
//
//                            facilitySearchedList.add(facility);
//                        }
//                    }
//
//                    if (facilitySearchedList.size() == 0) {
//                        Utilities.showToastMessage("No Such Customer Name Found", context, false);
//                        facilitySearchedList.clear();
//
//
//                        postCampBeneficiaryAdapter = new PackageReturnNew(context, facilitySearchedList, "0", "0");
//                        rv_patient_list.setAdapter(postCampBeneficiaryAdapter);
////                        setRecyclerView(facilitySearchedList);
//                    } else {
//
//                        postCampBeneficiaryAdapter = new PackageReturnNew(context, facilitySearchedList, "0", "0");
//                        rv_patient_list.setAdapter(postCampBeneficiaryAdapter);
////                        setRecyclerView(facilitySearchedList);
//                    }
//                }
//                return true;
//            }
//        });



        cb_accept.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (filteredList.isEmpty()) {

                    filteredList.addAll(campBeneficiaryList); // If no search is active, use full list
                }

                for (PacketReturnModel.Output output : filteredList) {
                    output.setChecked(checked);
                }

                postCampBeneficiaryAdapter.notifyDataSetChanged();
            }
        });

// Search View Logic
        searchview_campname.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterList(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }

            private void filterList(String query) {
                filteredList.clear();
                if (query.isEmpty()) {
                    filteredList.addAll(campBeneficiaryList);
                } else {
                    for (PacketReturnModel.Output facility : campBeneficiaryList) {
                        if (facility.getPatientName() != null &&
                                facility.getPatientName().toLowerCase().startsWith(query.toLowerCase())) {
                            filteredList.add(facility);
                        }
                    }
                }

                if (filteredList.isEmpty()) {
                    Utilities.showToastMessage("No Such Patient Name Found", context, false);
                }

                // Update adapter with filtered list
                postCampBeneficiaryAdapter = new PackageReturnNew(context, filteredList, "0", "0");
                rv_patient_list.setAdapter(postCampBeneficiaryAdapter);

                // Maintain select-all state
                cb_accept.setChecked(isAllSelected(filteredList));
            }

            private boolean isAllSelected(List<PacketReturnModel.Output> list) {
                for (PacketReturnModel.Output output : list) {
                    if (!output.isChecked()) return false;
                }
                return !list.isEmpty();
            }
        });

//        if (rb_board.isChecked()){
//            mainLLDate.setVisibility(View.GONE);
//            llDistrict.setVisibility(View.GONE);
//        }else {
//            mainLLDate.setVisibility(View.VISIBLE);
//            llDistrict.setVisibility(View.VISIBLE);
//
//        }
//
//        if (rb_manual.isChecked()){
//            mainLLDate.setVisibility(View.VISIBLE);
//            llDistrict.setVisibility(View.VISIBLE);
//        }else {
//            mainLLDate.setVisibility(View.GONE);
//            llDistrict.setVisibility(View.GONE);
//        }

        rb_board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainLLDate.setVisibility(View.VISIBLE);
//                llDistrict.setVisibility(View.GONE);
                LL_taluka.setVisibility(View.VISIBLE);
                LL_delivery_executive.setVisibility(View.VISIBLE);
                rb_board.setTextColor(Color.parseColor("#ff59a9ff"));
                rb_manual.setTextColor(Color.parseColor("#000000"));

                edt_barcode1.setText("");


                mainLL_search.setVisibility(View.GONE);
                mainLL_searchView.setVisibility(View.VISIBLE);

                btn_accept_courier.setText("Accept In Lab");

                cb_accept.setChecked(false);

                if (Utilities.isNetworkAvailable(context)) {


                    new GetBarcodePostCampDetails().execute(fromDate,CampDateToDate,"0",talukaId,resourceId,EmpCode

                    );
                }

            }
        });

        rb_manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainLLDate.setVisibility(View.VISIBLE);
                llDistrict.setVisibility(View.VISIBLE);
                LL_delivery_executive.setVisibility(View.VISIBLE);
                rb_manual.setTextColor(Color.parseColor("#ff59a9ff"));
                rb_board.setTextColor(Color.parseColor("#000000"));

                edt_barcode1.setText("");
                cb_accept.setChecked(false);


                mainLL_search.setVisibility(View.GONE);
                mainLL_searchView.setVisibility(View.VISIBLE);
                btn_accept_courier.setVisibility(View.VISIBLE);


                btn_accept_courier.setText("Return To Pharmacy");


                new GetAssignPacketData().execute(
                        fromDate,
                        CampDateToDate,
                        "0",
                        talukaId,
                        resourceId,
                        EmpCode
                );
            }
        });

        tv_from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd1 = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                tv_from_date.setText(Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year));
                                fromDate = Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year);

                                tv_to_date.setText("");

                                CampDate = Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year);
                            }

                        }, mYear, mMonth, mDay);
                Calendar c = Calendar.getInstance();
                c.set(mYear, mMonth, mDay);
                //  c.add(Calendar.DAY_OF_MONTH, 15);


                try {
                    dpd1.getDatePicker().setCalendarViewShown(false);

//                    dpd1.getDatePicker().setCalendarViewShown(false);
//                    dpd1.getDatePicker().setMinDate(c.getTimeInMillis());
//                    dpd1.getDatePicker().setMaxDate(System.currentTimeMillis() + (1000 * 60 * 60));

                    // dpd1.getDatePicker().setMinDate(System.currentTimeMillis());


                } catch (Exception e) {
                    e.printStackTrace();
                }
                dpd1.show();
            }
        });


        imv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (rb_board.isChecked()) {
                    if (Utilities.isNetworkAvailable(context)) {
                        new GetBarcodePostCampDetails().execute(fromDate,CampDateToDate,"0",talukaId,resourceId,EmpCode

                        );
                    }
                }

                if (rb_manual.isChecked()) {


                    if (edt_barcode1.getText().toString().equalsIgnoreCase("0")) {
                        Utilities.showAlertDialog(context, "Alert", "Delivery Challan No : Zero , can not be accepted.\n" +
                                "Please use checkbox for packet pick up.", false);
                        edt_barcode1.setText("");
                        return;
                    }


                    new GetAssignPacketData().execute(
                            fromDate,
                            CampDateToDate,
                            "0",
                            talukaId,
                            resourceId,
                            EmpCode
                    );
                }
            }
        });

        imv_barcode1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
//                        ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
//                        || ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions((Activity) context, new String[]{CAMERA, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 1);
//                    return;
//                }

                if (SDK_INT < Build.VERSION_CODES.S) {
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                            ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                            || ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions((Activity) context, new String[]{CAMERA, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 2);
                        return;
                    }

                } else {
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions((Activity) context, new String[]{CAMERA}, 2);
                        return;
                    }
                }

                Intent i = new Intent(context, BarcodeScanner_Activity.class);
                startActivityForResult(i, 10001);
            }
        });


//        cb_accept.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
//
//
//                if (cb_accept.isChecked()) {
//                    if (campBeneficiaryList != null) {
//                        for (PacketReturnModel.Output output :
//                                campBeneficiaryList) {
//                            output.setChecked(true);
//
//                        }
//                        postCampBeneficiaryAdapter = new PackageReturnNew(context, campBeneficiaryList, "0", "0");
//                        rv_patient_list.setAdapter(postCampBeneficiaryAdapter);
//                    }
//
//                } else {
//
//                    if (campBeneficiaryList != null) {
//
//                        for (PacketReturnModel.Output output :
//                                campBeneficiaryList) {
//                            output.setChecked(false);
//
//                        }
//
//                        postCampBeneficiaryAdapter = new PackageReturnNew(context, campBeneficiaryList, "0", "0");
//                        rv_patient_list.setAdapter(postCampBeneficiaryAdapter);
//
//
//                    }
//
//                }
//
//
//            }
//        });


        btn_accept_courier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


//                if (rb_manual.isChecked()) {
//                    if (tv_resourceNme.getText().toString().isEmpty()) {
//                        Utilities.showToastMessage("Please select report delivery executive", context, false);
//                        return;
//                    }
//                }


                Dialog dialog;
                dialog = new Dialog(MedicineReturnActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.alert_popup_dialoge);
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                ImageView btnCloseDialog = dialog.findViewById(R.id.btnCloseDialog);
                Button btnYes = dialog.findViewById(R.id.btnYes);
                Button btnNo = dialog.findViewById(R.id.btnNo);
                btnCloseDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        SubmitData();

                    }
                });
                btnNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                    }
                });


            }
        });


        tvCamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvCampType.getText().toString().isEmpty()) {
                    Utilities.showAlertDialog(context, "Select camp type", "Please select camp type", false);
                    return;
                }

                new GetCampList().execute(CampDate, EmpCode, DISTLGDCODE, campTypeId, "0");

            }
        });

        tv_taluka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if (tvCampType.getText().toString().isEmpty()) {
//                    Utilities.showAlertDialog(context, "Select camp type", "Please select camp type", false);
//                    return;
//                }


                new GetTaluka().execute(EmpCode, DISTLGDCODE);
            }
        });


        tvCampType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<CampTypeModel> campTypeModelArrayList = new ArrayList<>();
                campTypeModelArrayList.add(new CampTypeModel("REGULAR", 1));
//                campTypeModelArrayList.add(new CampTypeModel("CSC REGULAR CAMP", 2));
                campTypeModelArrayList.add(new CampTypeModel("DOOR TO DOOR", 3));
//                campTypeModelArrayList.add(new CampTypeModel("CSC D2D", 4));
                campTypeModelArrayList.add(new CampTypeModel("FLEXI CAMP", 5));

                showCampType(campTypeModelArrayList);

            }
        });

        tv_to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_from_date.getText().toString().equals(""))
                    Utilities.showMessageString("Please Select From Date", context);
                else {

                    DatePickerDialog dpd1 = new DatePickerDialog(context,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    tv_to_date.setText(Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year));

                                    CampDateToDate = Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year);


//                                    tv_teamNumber.setText("");
//                                    tv_district.setText("");
//                                    tv_resourceNme.setText("");

                                    if (rb_board.isChecked()) {

                                        if (Utilities.isNetworkAvailable(context)) {
                                            new GetBarcodePostCampDetails().execute(fromDate,CampDateToDate,"0",talukaId,resourceId,EmpCode

                                            );
                                        }


                                    } else if (rb_manual.isChecked()) {
                                        new GetAssignPacketData().execute(
                                                fromDate,
                                                CampDateToDate,
                                                "0",
                                                talukaId,
                                                resourceId,
                                                EmpCode
                                        );

                                    }


//                                    new GetAssignPacketData().execute(
//                                            fromDate,
//                                            CampDateToDate,
//                                            landinglabId,
//                                            talukaId
//                                    );

                                    if (DESGID.equalsIgnoreCase("92") || (DESGID.equalsIgnoreCase("29") || (DESGID.equalsIgnoreCase("104") || (DESGID.equalsIgnoreCase("162") || DESGID.equalsIgnoreCase("78") || DESGID.equalsIgnoreCase("77") || DESGID.equalsIgnoreCase("128") || DESGID.equalsIgnoreCase("131") || DESGID.equalsIgnoreCase("160"))))) {

                                        if (Utilities.isNetworkAvailable(context)) {
                                        }

                                    } else if (DESGID.equalsIgnoreCase("64") || (DESGID.equalsIgnoreCase("35") || DESGID.equalsIgnoreCase("86"))) {
                                        llTeamNumber.setVisibility(View.VISIBLE);


                                    }


                                }

                            }, mYear, mMonth, mDay);

                    Calendar c = Calendar.getInstance();
//                    try {
//                        c.setTime(Utilities.dfDate4.parse(CampDate));
//                    } catch (ParseException e) {
//                        throw new RuntimeException(e);
//                    }
//                    c.add(Calendar.DAY_OF_MONTH, 14);

                    try {
                        dpd1.getDatePicker().setCalendarViewShown(false);
                        dpd1.getDatePicker().setMaxDate(c.getTimeInMillis());

                        // dpd1.getDatePicker().setMinDate(System.currentTimeMillis());


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    dpd1.show();
                }
            }
        });


//        tv_district.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (tv_from_date.getText().toString().isEmpty()){
//                    Utilities.showToastMessage("Please select from date", context, false);
//                    return;
//
//                }
//                if (tv_to_date.getText().toString().isEmpty()){
//                    Utilities.showToastMessage("Please select to date", context, false);
//                    return;
//                }
//
//                if (Utilities.isNetworkAvailable(context)) {
//                    new GetDistrictList().execute(STATELGDCODE,EmpCode);
//                } else {
//                    Utilities.showToastMessage("Please Check Your Connection", context, false);
//                }
//            }
//        });


        tv_resourceNme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tv_taluka.getText().toString().isEmpty()) {
                    Utilities.showToastMessage("Please select taluka", context, false);
                    return;

                }


                if (Utilities.isNetworkAvailable(context)) {

                    new GetResourceData().execute(talukaId);
                } else {
                    Utilities.showToastMessage("Please Check Your Connection", context, false);
                }
            }
        });


    }

    private class GetPostCampDetails extends AsyncTask<String, Void, String> {

        private ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(context);
            pd.setMessage("Please wait . . . ");
            pd.setCancelable(false);
            if (!((Activity) context).isFinishing()) {
                pd.show();

            }
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "";
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("FromDate", params[0]));
            param.add(new ParamsPojo("ToDate", params[1]));
            param.add(new ParamsPojo("Labcode", params[2]));
            param.add(new ParamsPojo("TALLGDCODE", params[3]));

            Log.d("GetPostCampDetails", "doInBackground: " + new Gson().toJson(param));
            res = WebServiceCall.APICall(ApplicationConstants.GetDataForPacketAssignment, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            Log.d("TAG", "onPostExecute: " + result);
            try {
                if (!result.equals("")) {

                    PacketReturnModel pojoDetails = new Gson().fromJson(result, PacketReturnModel.class);
                    String status = pojoDetails.getStatus();
                    String message = pojoDetails.getMessage();

                    if (status.equalsIgnoreCase("success")) {
                        campBeneficiaryList = pojoDetails.getOutput();
                        if (campBeneficiaryList.size() > 0) {

                            searchview_campname.setQuery("", false);
                            searchview_campname.clearFocus();

                            cb_accept.setChecked(false);
                            filteredList.clear();
                            postCampBeneficiaryAdapter = new PackageReturnNew(context, campBeneficiaryList, "0", "0");
                            rv_patient_list.setAdapter(postCampBeneficiaryAdapter);
                        }
                    } else {
                        Utilities.showMessageString("Data Not Found", context);

                        postCampBeneficiaryAdapter = new PackageReturnNew(context, new ArrayList<>(), "0", "0");
                        rv_patient_list.setAdapter(postCampBeneficiaryAdapter);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
            param.add(new ParamsPojo("UserID", EmpCode));
            //  res = WebServiceCall.APICall(ApplicationConstants.GetTeamNumberByCampIdAndUSerId, ApplicationConstants.webservice_d2d, param);
            res = WebServiceCall.APICall(ApplicationConstants.GetTeamNumberByUserId_MedicineDelivery, ApplicationConstants.webservice_d2d, param);

            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    teamlist = new ArrayList<>();
                    ArrayList<TeamsDetailsModel.OutputBean> assignteamlist = new ArrayList<>();
                    TeamsDetailsModel teamsDetailsModel = new Gson().fromJson(result, TeamsDetailsModel.class);
                    type = teamsDetailsModel.getStatus();
                    message = teamsDetailsModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        assignteamlist = teamsDetailsModel.getOutput();
                        if (assignteamlist.size() > 0) {
                            //  districtList.add(0, new DistrictList_Model("0", "All"));

                            showAssignedTeamListDialog(assignteamlist);
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

    private void showTeamListListDialog(final List<TeamNumberModel.Output> teamlist) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        builderSingle.setTitle("Select Team Number");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        for (int i = 0; i < teamlist.size(); i++) {
            arrayAdapter.add(String.valueOf(teamlist.get(i).getTeamName()));
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
                tv_teamNumber.setText(teamlist.get(which).getTeamName());
                teamId = String.valueOf(teamlist.get(which).getTeamNumber());
                //  refreshCalendar();
            }
        });
        builderSingle.show();

    }


    private void showAssignedTeamListDialog(final ArrayList<TeamsDetailsModel.OutputBean> assignteamlist) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        builderSingle.setTitle("Select Team");
        builderSingle.setCancelable(false);
        View dialogueView = getLayoutInflater().inflate(R.layout.recyclerview_dialogue, null);
        builderSingle.setView(dialogueView);

        RecyclerView rvList = dialogueView.findViewById(R.id.rvList);
        EditText edt_search = dialogueView.findViewById(R.id.edt_search);
        rvList.setHasFixedSize(true);
        rvList.setLayoutManager(new LinearLayoutManager(context));

        rvList.setAdapter(new TeamsDetailsAssignedReportDeliveryAdapter(assignteamlist));

        rvList.addOnItemTouchListener(
                new RecyclerItemClickListener(context,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, final int position) {
                                TeamsDetailsModel.OutputBean team = assignteamlist.get(position);
                                teamId = assignteamlist.get(position).getTeamNumber();

                                tv_teamNumber.setText(assignteamlist.get(position).getTeamName());
                                teamDialog.dismiss();

                                if (Utilities.isNetworkAvailable(context)) {
                                }


                            }
                        }));

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count) {

                if (query.toString().isEmpty()) {
                    rvList.setAdapter(new TeamsDetailsAssignedReportDeliveryAdapter(assignteamlist));
                    return;
                }

                if (assignteamlist.size() == 0) {
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
                    rvList.setAdapter(new TeamsDetailsAssignedReportDeliveryAdapter(searchedTestList));
                } else {
                    rvList.setAdapter(new TeamsDetailsAssignedReportDeliveryAdapter(assignteamlist));
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


    private void showCampType(final ArrayList<CampTypeModel> campTypeModelsList) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        builderSingle.setTitle("Select Camp Type");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

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
                tvCampType.setText(campTypeModelsList.get(which).getCampTypeName());
                campTypeId = String.valueOf(campTypeModelsList.get(which).getCampTypeId());


                //  refreshCalendar();


            }
        });
        builderSingle.show();
    }


    public class GetCampList extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("CampDATE", params[0]));
            param.add(new ParamsPojo("UserId", params[1]));
            param.add(new ParamsPojo("DISTLGDCODE", params[2]));
            param.add(new ParamsPojo("CampType", params[3]));
            param.add(new ParamsPojo("LABCODE", params[4]));

            res = WebServiceCall.APICall(ApplicationConstants.GetCampList, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    // campList = new ArrayList<>();
                    List<CampListModel.OutputBean> campList = new ArrayList<>();

                    //  Log.d("result ",result.toString());

                    CampListModel campListModel = new Gson().fromJson(result, CampListModel.class);
                    type = campListModel.getStatus();
                    message = campListModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {

                        campList = campListModel.getOutput();

                        if (campList.size() > 0) {
                            // districtList.add(0, new DistrictList_Model("0", "All"));

                            showCampListDialog(campList);
                        } else {
                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                        }
                    } else {
                        Utilities.showAlertDialog(context, "Fail", message, false);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Please Try Again", "Camp I'd not available", false);
            }
        }
    }


    private void showCampListDialog(final List<CampListModel.OutputBean> assignteamlist) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        builderSingle.setTitle("Select CampId");
        builderSingle.setCancelable(false);
        View dialogueView = getLayoutInflater().inflate(R.layout.recyclerview_dialogue, null);
        builderSingle.setView(dialogueView);

        RecyclerView rvList = dialogueView.findViewById(R.id.rvList);
        EditText edt_search = dialogueView.findViewById(R.id.edt_search);
        rvList.setHasFixedSize(true);
        rvList.setLayoutManager(new LinearLayoutManager(context));

        rvList.setAdapter(new ReportDeliveryCampIdAdapter(assignteamlist));

        rvList.addOnItemTouchListener(
                new RecyclerItemClickListener(context,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, final int position) {
                                CampListModel.OutputBean team = assignteamlist.get(position);
                                AssignedId = assignteamlist.get(position).getCampId();

                                tvCamp.setText(assignteamlist.get(position).getCampId());
                                teamDialog.dismiss();

                            }
                        }));

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count) {

                if (query.toString().isEmpty()) {
                    rvList.setAdapter(new ReportDeliveryCampIdAdapter(assignteamlist));
                    return;
                }

                if (teamList.size() == 0) {
                    rvList.setVisibility(View.GONE);
                    return;
                }

                if (!query.toString().equals("")) {
                    List<CampListModel.OutputBean> searchedTestList = new ArrayList<>();
                    for (CampListModel.OutputBean clientDetails : assignteamlist) {

                        String countryToBeSearched = clientDetails.getMember1().toLowerCase();
                        String member2 = clientDetails.getMember2().toLowerCase();

                        if (countryToBeSearched.contains(query.toString().toLowerCase()) ||
                                member2.contains(query.toString().toLowerCase())) {
                            searchedTestList.add(clientDetails);
                        }
                    }
                    rvList.setAdapter(new ReportDeliveryCampIdAdapter(searchedTestList));
                } else {
                    rvList.setAdapter(new ReportDeliveryCampIdAdapter(assignteamlist));
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
                for (CampListModel.OutputBean team : assignteamlist
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
            param.add(new ParamsPojo("USERID", params[1]));


            //  res = WebServiceCall.APICall(ApplicationConstants.GetAllDistrictList, ApplicationConstants.webservice_d2d, param);
            res = WebServiceCall.APICall(ApplicationConstants.GetDistrictByUserID, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    districtList = new ArrayList<>();
                    ArrayList<DistrictList_Model> districtList = new ArrayList<>();
                    DistrictList_Pojo pojoDetails = new Gson().fromJson(result, DistrictList_Pojo.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        districtList = pojoDetails.getOutput();
                        if (districtList.size() > 0) {
                            //  districtList.add(0, new DistrictList_Model("0", "All"));


                            if (pojoDetails.getOutput().size() > 0) {
                                DistrictList_Model output = pojoDetails.getOutput().get(0);

                                DISTLGDCODE = output.getDISTLGDCODE();
                                tv_district.setText(output.getDISTNAME());


                            }


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

    public class GetDistrictListNew extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("USERID", params[1]));


            //  res = WebServiceCall.APICall(ApplicationConstants.GetAllDistrictList, ApplicationConstants.webservice_d2d, param);
            res = WebServiceCall.APICall(ApplicationConstants.GetDistrictByUserID, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    districtList = new ArrayList<>();
                    ArrayList<DistrictList_Model> districtList = new ArrayList<>();
                    DistrictList_Pojo pojoDetails = new Gson().fromJson(result, DistrictList_Pojo.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        districtList = pojoDetails.getOutput();
                        if (districtList.size() > 0) {
                            //  districtList.add(0, new DistrictList_Model("0", "All"));


                            if (pojoDetails.getOutput().size() > 0) {
                                DistrictList_Model output = pojoDetails.getOutput().get(0);

                                DISTLGDCODE = output.getDISTLGDCODE();
                                tv_district.setText(output.getDISTNAME());


                            }

                            if (Utilities.isNetworkAvailable(context)) {
                            }


//                            showDistrictListDialog(districtList);
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

    private void showDistrictListDialog(final ArrayList<DistrictList_Model> districtList) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
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
                tv_district.setText(districtList.get(which).getDISTNAME());
                DISTLGDCODE = districtList.get(which).getDISTLGDCODE();
                //  refreshCalendar();


                if (DESGID.equalsIgnoreCase("92") || (DESGID.equalsIgnoreCase("29") || (DESGID.equalsIgnoreCase("104") || (DESGID.equalsIgnoreCase("162") || DESGID.equalsIgnoreCase("78") || DESGID.equalsIgnoreCase("77") || DESGID.equalsIgnoreCase("128") || DESGID.equalsIgnoreCase("131"))))) {


                } else if (DESGID.equalsIgnoreCase("64") || (DESGID.equalsIgnoreCase("35") || DESGID.equalsIgnoreCase("86"))) {
                    llTeamNumber.setVisibility(View.VISIBLE);


                }


            }
        });
        builderSingle.show();

    }


    public class GetResourceData extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("TALLGDCODE", params[0]));

//            res = WebServiceCall.APICall(ApplicationConstants.GetReportDeliveryExecutive, ApplicationConstants.webservice_d2d, param);
            res = WebServiceCall.APICall(ApplicationConstants.GetReportDeliveryExecutive_D2DTeam, ApplicationConstants.webservice_d2d, param);

            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {

                    LandingLabModel landingLabModel = new Gson().fromJson(result, LandingLabModel.class);
                    type = landingLabModel.getStatus();
                    message = landingLabModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        List<LandingLabModel.Output> landinglablist = landingLabModel.getOutput();


                        if (landinglablist.size() > 0) {

                            showLadingLabDialogue(landinglablist);


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

        private void showLadingLabDialogue(final List<LandingLabModel.Output> landinglablist) {
            AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
            builderSingle.setTitle("Select Resource");
            builderSingle.setCancelable(false);

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

            for (int i = 0; i < landinglablist.size(); i++) {
                arrayAdapter.add(String.valueOf(landinglablist.get(i).getUserName()));
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
                    tv_resourceNme.setText(landinglablist.get(which).getUserName());
                    resourceId = String.valueOf(landinglablist.get(which).getUserID());
                    talukaId = String.valueOf(landinglablist.get(which).getTallgdcode());
                    teamId = landinglablist.get(which).getTeamid();



                    if (rb_board.isChecked()) {
                        if (Utilities.isNetworkAvailable(context)) {
                            new GetBarcodePostCampDetails().execute(fromDate,CampDateToDate,"0",talukaId,resourceId,EmpCode

                            );
                        }

                    }

                    if (rb_manual.isChecked()) {
                        new GetAssignPacketData().execute(
                                fromDate,
                                CampDateToDate,
                                "0",
                                talukaId,
                                resourceId,
                                EmpCode
                        );

                    }


//                    if (Utilities.isNetworkAvailable(context)) {
//
//                        cb_accept.setChecked(false);
//                        new GetPostCampDetails().execute(
//                                fromDate,
//                                CampDateToDate,
//                                landinglabId,
//                                talukaId
//
//                        );
//                    }


                }
            });
            builderSingle.show();

        }
    }


    public class GetTaluka extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("UserId", params[0]));
            param.add(new ParamsPojo("DISTLGDCODE", params[1]));

            res = WebServiceCall.APICall(ApplicationConstants.GetUserMappedTaluka, ApplicationConstants.webservice_d2d, param);
            return res;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {

                    TalukaModel talukaModel = new Gson().fromJson(result, TalukaModel.class);
                    type = talukaModel.getStatus();
                    message = talukaModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        List<TalukaModel.Output> camptypelist = talukaModel.getOutput();
                        if (camptypelist.size() > 0) {
                            showTalukaDialogue(camptypelist);
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

                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
            }
        }


        private void showTalukaDialogue(final List<TalukaModel.Output> talukalist) {
            AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
            builderSingle.setTitle("Select Taluka");
            builderSingle.setCancelable(false);

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

            for (int i = 0; i < talukalist.size(); i++) {
                arrayAdapter.add(String.valueOf(talukalist.get(i).gettALNAME()));
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
                    tv_taluka.setText(talukalist.get(which).gettALNAME());
                    talukaId = String.valueOf(talukalist.get(which).gettLLGDCODE());

                    resourceId = "0";


                    if (rb_board.isChecked()) {

                        if (Utilities.isNetworkAvailable(context)) {
                            new GetBarcodePostCampDetails().execute(fromDate,CampDateToDate,"0",talukaId,resourceId,EmpCode

                            );
                        }


                    } else if (rb_manual.isChecked()) {
                        new GetAssignPacketData().execute(
                                fromDate,
                                CampDateToDate,
                                "0",
                                talukaId,
                                resourceId,
                                EmpCode
                        );

                    }


                    tv_resourceNme.setText("");

                    //  refreshCalendar();
                }
            });
            builderSingle.show();

        }

    }

    public class GetTalukaNew extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("UserId", params[0]));
            param.add(new ParamsPojo("DISTLGDCODE", params[1]));

            res = WebServiceCall.APICall(ApplicationConstants.GetUserMappedTaluka, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {

                    TalukaModel talukaModel = new Gson().fromJson(result, TalukaModel.class);
                    type = talukaModel.getStatus();
                    message = talukaModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        List<TalukaModel.Output> camptypelist = talukaModel.getOutput();
                        if (camptypelist.size() > 0) {

                            if (talukaModel.getOutput().size() > 0) {
                                TalukaModel.Output output = talukaModel.getOutput().get(0);

                                talukaId = String.valueOf(output.gettLLGDCODE());
                                tv_taluka.setText(output.gettALNAME());


                            }


                            if (Utilities.isNetworkAvailable(context)) {
                                new GetBarcodePostCampDetails().execute(fromDate,CampDateToDate,"0",talukaId,resourceId,EmpCode

                                );
                            }

//                            new GetPostCampDetails().execute(
//                                    fromDate,
//                                    CampDateToDate,
//                                    landinglabId,
//                                    talukaId
//
//                            );


//                            showTalukaDialogue(camptypelist);
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

                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
            }
        }

        private void showTalukaDialogue(final List<TalukaModel.Output> talukalist) {
            AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
            builderSingle.setTitle("Select Taluka");
            builderSingle.setCancelable(false);

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

            for (int i = 0; i < talukalist.size(); i++) {
                arrayAdapter.add(String.valueOf(talukalist.get(i).gettALNAME()));
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
                    tv_taluka.setText(talukalist.get(which).gettALNAME());
                    talukaId = String.valueOf(talukalist.get(which).gettLLGDCODE());
                    //  refreshCalendar();
                }
            });
            builderSingle.show();

        }

    }


    private void SubmitData() {


        if (tv_from_date.getText().toString().isEmpty()) {
            Utilities.showToastMessage("Please select from date", context, false);
            return;

        }
        if (tv_to_date.getText().toString().isEmpty()) {
            Utilities.showToastMessage("Please select to date", context, false);
            return;
        }

//        if (rb_manual.isChecked()) {
//            if (tv_resourceNme.getText().toString().isEmpty()) {
//                Utilities.showToastMessage("Please select report delivery executive", context, false);
//                return;
//            }
//
//        }


        JsonArray packetJsonArray = new JsonArray();
        if (campBeneficiaryList != null) {
            int packetSelect = 0;
            for (PacketReturnModel.Output t :
                    campBeneficiaryList) {

                if (t.isChecked()) {
                    JsonObject jsonObject = new JsonObject();

                    jsonObject.addProperty("PacketNo", t.getPacketNo());
                    jsonObject.addProperty("TreatmentID", t.getTreatmentID());
                    packetJsonArray.add(jsonObject);

                    packetSelect += 1;

                }
            }

            if (packetSelect == 0) {
                Utilities.showToastMessage("Please select at least one patient", context, false);
                return;
            }

        }
//


        if (rb_board.isChecked()) {

            if (Utilities.isNetworkAvailable(context)) {
                new InsertPacketDetails().execute(
                        EmpCode,packetJsonArray.toString()
                        );
            }

        } else if (rb_manual.isChecked()) {


            JsonArray packetAssignJsonArray = new JsonArray();
            if (campBeneficiaryList != null) {
                int packetSelect = 0;

                for (PacketReturnModel.Output t :
                        campBeneficiaryList) {

//                    if (!t.getReturnStatus().equals("Return Initiated To Pharmacy")){

                        if (t.isChecked()) {
                            JsonObject jsonObject = new JsonObject();
                            jsonObject.addProperty("PacketNo", t.getPacketNo());
                            jsonObject.addProperty("TreatmentID", t.getTreatmentID());
                            packetAssignJsonArray.add(jsonObject);

                            packetSelect += 1;


                        }

//                    }

                }

                if (packetSelect == 0) {
                    Utilities.showToastMessage("Please select at least one patient", context, false);
                    return;
                }

            }


            if (Utilities.isNetworkAvailable(context)) {
                new InsertAssignPacketDetails().execute(
                        EmpCode,
                        packetAssignJsonArray.toString()
                        );
            } else {
                Utilities.showMessage(R.string.msgt_nointernetconnection, context);
            }

        } else {
            Utilities.showMessage(R.string.msgt_nointernetconnection, context);
        }
    }


    private class InsertAssignPacketDetails extends AsyncTask<String, Void, String> {


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
            Log.d("InsertPacket", "doInBackground: " + Arrays.toString(params));
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("CreatedBy", params[0]));
            param.add(new ParamsPojo("ReturnDetails", params[1]));
            res = WebServiceCall.APICall(ApplicationConstants.InsertMedicineReturn_ToPharmacy, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("InsertPacket", "onPostExecute: " + result);
            try {
                pd.dismiss();

                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
                    JSONObject obj = new JSONObject(result);
                    String status = obj.getString("status");
                    String message = obj.getString("message");
                    if (status.equalsIgnoreCase("Success")) {

                        cb_accept.setChecked(false);

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setIcon(R.drawable.icon_success);
                        builder.setTitle("Success");
                        builder.setCancelable(false);
                        builder.setMessage("Packet Return successfully");
                        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // finish();

                                cb_accept.setChecked(false);

                                if (Utilities.isNetworkAvailable(context)) {
                                    new GetAssignPacketData().execute(
                                            fromDate,
                                            CampDateToDate,
                                            "0",
                                            talukaId,
                                            resourceId,
                                            EmpCode
                                    );
                                }


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
        }
    }

    private class InsertPacketDetails extends AsyncTask<String, Void, String> {


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
            Log.d("InsertPacket", "doInBackground: " + Arrays.toString(params));
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("CreatedBy", params[0]));
            param.add(new ParamsPojo("AccpetDetails", params[1]));
            res = WebServiceCall.APICall(ApplicationConstants.InsertMedicineReturn_AcceptInLab, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("InsertPacket", "onPostExecute: " + result);
            try {
                pd.dismiss();

                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
                    JSONObject obj = new JSONObject(result);
                    String status = obj.getString("status");
                    String message = obj.getString("message");
                    if (status.equalsIgnoreCase("Success")) {


                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setIcon(R.drawable.icon_success);
                        builder.setTitle("Success");
                        builder.setCancelable(false);
                        builder.setMessage("Packet Accepted successfully");
                        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // finish();

                                cb_accept.setChecked(false);

                                if (Utilities.isNetworkAvailable(context)) {
                                    new GetBarcodePostCampDetails().execute(fromDate,CampDateToDate,"0",talukaId,resourceId,EmpCode

                                    );
                                }


//                                postCampBeneficiaryAdapter = new PackageReturnNew(context, new ArrayList<>(), "0", "0");
//                                rv_patient_list.setAdapter(postCampBeneficiaryAdapter);
//
//                                tv_taluka.setText("");
//                                talukaId = "0";
//                                edt_barcode1.setText("");

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
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == RESULT_OK) {
                if (requestCode == 10001) {
                    String requiredValue = data.getStringExtra("key");
                    Toast.makeText(context, requiredValue, Toast.LENGTH_SHORT).show();
                    edt_barcode1.setText(requiredValue);


                    if (rb_board.isChecked()) {
                        if (Utilities.isNetworkAvailable(context)) {
                            new GetBarcodePostCampDetails().execute(fromDate,CampDateToDate,"0",talukaId,resourceId,EmpCode

                            );
                        }

                    }

                    if (rb_manual.isChecked()) {
                        new GetAssignPacketData().execute(
                                fromDate,
                                CampDateToDate,
                                "0",
                                talukaId,
                                resourceId,
                                EmpCode
                        );

                    }


//                    JsonArray packetJsonArray = new JsonArray();
//
//                    JsonObject jsonObject = new JsonObject();
//                    jsonObject.addProperty("PacketID", edt_barcode1.getText().toString());
//                    packetJsonArray.add(jsonObject);
//
//
//                    if (Utilities.isNetworkAvailable(context)) {
//                        new InsertPacketDetailsForBarcode().execute(
//                                EmpCode,
//                                packetJsonArray.toString(),
//                                landinglabId);
//                    } else {
//                        Utilities.showMessage(R.string.msgt_nointernetconnection, context);
//                    }
//


                } else if (requestCode == 10002) {
                    String requiredValue = data.getStringExtra("key");
                    Toast.makeText(context, requiredValue, Toast.LENGTH_SHORT).show();
//                    edt_barcode2.setText(requiredValue);
                } else if (requestCode == 10003) {
                    String requiredValue = data.getStringExtra("key");
                    Toast.makeText(context, requiredValue, Toast.LENGTH_SHORT).show();
//                    edt_GlucoseBarcode.setText(requiredValue);

                }
            }

//            if (resultCode == RESULT_OK) {
//                if (requestCode == CAMERA_REQUEST) {
//                    CropImage.activity(photoURI).setGuidelines(CropImageView.Guidelines.ON).start(HealthDetailsForm_Activity.this);
//                }
//            }
//
//            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//                CropImage.ActivityResult result = CropImage.getActivityResult(data);
//                if (resultCode == RESULT_OK) {
//                    Uri resultUri = result.getUri();
//                    savefile(resultUri);
//                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                    Exception error = result.getError();
//                }
//            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private class GetBarcodePostCampDetails extends AsyncTask<String, Void, String> {

        private ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(context);
            pd.setMessage("Please wait . . . ");
            pd.setCancelable(false);
            if (!((Activity) context).isFinishing()) {
                pd.show();

            }
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "";
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("Fromdate", params[0]));
            param.add(new ParamsPojo("todate", params[1]));
            param.add(new ParamsPojo("DISTLGDCODE", params[2]));
            param.add(new ParamsPojo("TALLGDCODE", params[3]));
            param.add(new ParamsPojo("UserID", params[4]));
            param.add(new ParamsPojo("LoginUserID", params[5]));


            Log.d("GetPostCampDetails", "doInBackground: " + new Gson().toJson(param));

            res = WebServiceCall.APICall(ApplicationConstants.GetBenfListForMedicalReturn_AcceptInLab, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            Log.d("TAG", "onPostExecute: " + result);

            try {
                if (!result.equals("")) {

                    PacketReturnModel pojoDetails = new Gson().fromJson(result, PacketReturnModel.class);
                    String status = pojoDetails.getStatus();
                    String message = pojoDetails.getMessage();

                    if (status.equalsIgnoreCase("success")) {
                        campBeneficiaryList = pojoDetails.getOutput();
                        if (campBeneficiaryList.size() > 0) {

                            cb_accept.setChecked(false);
                            filteredList.clear();


                            postCampBeneficiaryAdapter = new PackageReturnNew(context, campBeneficiaryList, "0", "0");
                            rv_patient_list.setAdapter(postCampBeneficiaryAdapter);


                            int packetSelect = 0;
                            int packetCollect = 0;
                            for (PacketReturnModel.Output t :
                                    campBeneficiaryList) {

//                                if (t.getOverallStatusID().equals("1")) {
//                                    btn_accept_courier.setVisibility(View.GONE);
//                                    packetSelect += 1;
//                                } else {
//                                    btn_accept_courier.setVisibility(View.VISIBLE);
//
//                                }
//                                if (t.getOverallStatusID().equals("2")) {
//                                    btn_accept_courier.setVisibility(View.GONE);
//                                    packetCollect += 1;
//                                } else {
//                                    btn_accept_courier.setVisibility(View.VISIBLE);
//
//                                }

                            }


                            if (packetSelect > 0) {
                                Utilities.showAlertDialog(context, "Alert", "Failure to accept return, process collection of packet first and try again.", false);
                                btn_accept_courier.setVisibility(View.GONE);
                            }

                            if (packetCollect > 0) {
                                Utilities.showAlertDialog(context, "Alert", "Failure to accept return, process receiving of packet first and try again.", false);
                                btn_accept_courier.setVisibility(View.GONE);
                            }

                        }
                    } else {
                        Utilities.showMessageString("Packet No. /Delivery Challan No. Not Found", context);

                        postCampBeneficiaryAdapter = new PackageReturnNew(context, new ArrayList<>(), "0", "0");
                        rv_patient_list.setAdapter(postCampBeneficiaryAdapter);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private class GetAssignPacketData extends AsyncTask<String, Void, String> {

        private ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(context);
            pd.setMessage("Please wait . . . ");
            pd.setCancelable(false);
            if (!((Activity) context).isFinishing()) {
                pd.show();

            }
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "";
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("Fromdate", params[0]));
            param.add(new ParamsPojo("todate", params[1]));
            param.add(new ParamsPojo("DISTLGDCODE", params[2]));
            param.add(new ParamsPojo("TALLGDCODE", params[3]));
            param.add(new ParamsPojo("UserID", params[4]));
            param.add(new ParamsPojo("LoginUserID", params[5]));

            Log.d("GetPostCampDetails", "doInBackground: " + new Gson().toJson(param));
            res = WebServiceCall.APICall(ApplicationConstants.GetBenfListForMedicineReturn_ToPharmacy , ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            Log.d("TAG", "onPostExecute: " + result);
            try {
                if (!result.equals("")) {

                    PacketReturnModel pojoDetails = new Gson().fromJson(result, PacketReturnModel.class);
                    String status = pojoDetails.getStatus();
                    String message = pojoDetails.getMessage();

                    if (status.equalsIgnoreCase("success")) {
                        campBeneficiaryList = pojoDetails.getOutput();
                        if (campBeneficiaryList.size() > 0) {
                            cb_accept.setChecked(false);

                            filteredList.clear();
                            postCampBeneficiaryAdapter = new PackageReturnNew(context, campBeneficiaryList, "0", "0");
                            rv_patient_list.setAdapter(postCampBeneficiaryAdapter);
                        }
                    } else {
                        Utilities.showMessageString("Data Not Found", context);

                        postCampBeneficiaryAdapter = new PackageReturnNew(context, new ArrayList<>(), "0", "0");
                        rv_patient_list.setAdapter(postCampBeneficiaryAdapter);
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private class InsertPacketDetailsForBarcode extends AsyncTask<String, Void, String> {

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
            Log.d("InsertPacket", "doInBackground: " + Arrays.toString(params));
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("CreatedBy", params[0]));
            param.add(new ParamsPojo("PacketDetails", params[1]));
            param.add(new ParamsPojo("LabCode", params[2]));

            res = WebServiceCall.APICall(ApplicationConstants.InsertPacketCollectionDetails, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("InsertPacket", "onPostExecute: " + result);
            try {
                pd.dismiss();

                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
                    JSONObject obj = new JSONObject(result);
                    String status = obj.getString("status");
                    String message = obj.getString("message");
                    if (status.equalsIgnoreCase("Success")) {

                        cb_accept.setChecked(false);

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setIcon(R.drawable.icon_success);
                        builder.setTitle("Success");
                        builder.setCancelable(false);
                        builder.setMessage("Packet No :" + " " + edt_barcode1.getText().toString().trim() + " " + "Accept In Lab");
                        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // finish();


                                edt_barcode1.setText("");

                                if (Utilities.isNetworkAvailable(context)) {
                                    new GetAssignPacketData().execute(
                                            fromDate,
                                            CampDateToDate,
                                            "0",
                                            talukaId,
                                            resourceId,
                                            EmpCode
                                    );
                                }


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
        }
    }


}