package com.myhindlab.abkat.activities.couriermodule;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.adapters.BarcodeListNewAdapter;
import com.myhindlab.abkat.adapters.TeamsDetailsAssignedAdapterNew;
import com.myhindlab.abkat.models.CampListModel;
import com.myhindlab.abkat.models.CampTypeModel;
import com.myhindlab.abkat.models.TeamsDetailsModel;
import com.myhindlab.abkat.models.TeamsDetailsModelNew;
import com.myhindlab.abkat.models.couriermodule.BarcodeListCountNewModel;
import com.myhindlab.abkat.models.couriermodule.BarcodeListNewModel;
import com.myhindlab.abkat.models.couriermodule.CamplistOnLandingLabModel;
import com.myhindlab.abkat.models.couriermodule.LandingLabNewModel;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.RecyclerItemClickListener;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class AcceptSamplesInLabActivity extends AppCompatActivity {

    private Context context;
    private ProgressDialog pd;
    private UserSessionManager session;
    private TextView textView, tvDate, tvSampleCount, tvTeamHeader, tvTeamNumber, tvSampleBarcode,tv_teamnumber, tvDoctorHeader, tvPhleboHeader, tvDataEntryHeader, tvTeamId, tvCampType, tvDistrict, tvLandingLab, tvCampList, tvExternalPhlebo, tvAssigned, tvTeams;
    private Button btnAssign;
    private EditText edtRemark;
    private LinearLayout llteam;
    private int  assignTypeId, selectectLabId;
    private int mYear, mMonth, mDay, mYear1, mMonth1, mDay1;
    private String DESGID, EmpCode, LabCode, CampDATE, DISTLGDCODE, district, TALLGDCODE, taluka, STATELGDCODE = "2",campTypeId, selectedLabID = "", resorceId, selectedLabName = "", exernalPhleboId, TeamID, CampId, CampDate, UserId, AssignedId = "", IsTeam;
    private ArrayList<CampListModel.OutputBean> campList;
    private ArrayList<CampListModel.OutputBean> selectedCampList;
    private TeamsDetailsModel teamsDetailsModel;
    private ArrayList<TeamsDetailsModel> assignteamlist;
    private AlertDialog teamDialog;
    private ArrayList<BarcodeListNewModel.Output> barcodeList;
    private ArrayList<BarcodeListCountNewModel.Output> barcodecountList;
    private ArrayList<BarcodeListNewModel.Output> selectedBarcodeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_samples_in_lab);


        initView();
        setEventHandlers();
        getSessionData();
        setUpToolbar();


    }

    private void initView() {
        context = AcceptSamplesInLabActivity.this;
        session = new UserSessionManager(context);
        pd = new ProgressDialog(context);
        tvDate = findViewById(R.id.tvDate);
        tvCampType = findViewById(R.id.tvCampType);
        tvDistrict = findViewById(R.id.tvDistrict);
        tvLandingLab = findViewById(R.id.tvLandingLab);
        tvCampList = findViewById(R.id.tvCampList);
        btnAssign = findViewById(R.id.btnAssign);
        tvExternalPhlebo = findViewById(R.id.tvExternalPhlebo);
        tvTeamId = findViewById(R.id.tvTeamId);
        tvAssigned = findViewById(R.id.tvAssigned);
        tvTeams = findViewById(R.id.tvTeams);
        tvTeamHeader = findViewById(R.id.tvTeamHeader);
        tvDoctorHeader = findViewById(R.id.tvDoctorHeader);
        tvPhleboHeader = findViewById(R.id.tvPhleboHeader);
        tvDataEntryHeader = findViewById(R.id.tvDataEntryHeader);
        tvTeamNumber = findViewById(R.id.tvTeamNumber);
        tvSampleBarcode = findViewById(R.id.tvSampleBarcode);
        tvSampleCount = findViewById(R.id.tvSampleCount);
        edtRemark = findViewById(R.id.edtRemark);
        tv_teamnumber = findViewById(R.id.tv_teamnumber);
        llteam = findViewById(R.id.llteam);



        Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);


        tvDate.setText(Utilities.dfDate4.format(new Date()));
        CampDate = Utilities.dfDate4.format(new Date());


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
                UserId = json.getString("UserId");
                LabCode = json.getString("LabCode");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }


    private void setEventHandlers() {


        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd1 = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                tvDate.setText(Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year));

                                CampDate = Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year);

//                                if (Utilities.isDateBeforeCurrentDate(CampDate)) {
//                                    btnAssign.setEnabled(true);
//                                } else {
//                                    btnAssign.setEnabled(false);
//                                }

//                                selectedLabID = "";
//                                campTypeId = 0;
                                tvCampType.setText("");
//                                tvDistrict.setText("");
                                tvLandingLab.setText("");
                                tvCampList.setText("");
//                                tvAssign.setText("");


                                //                                if (Utilities.isNetworkAvailable(context)) {
                                //                                 } else {
                                //                                    Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                                //                                }
                            }

                        }, mYear, mMonth, mDay);
                try {
                    // dpd1.getDatePicker().setCalendarViewShown(false);

                    Calendar c = Calendar.getInstance();
                    c.add(Calendar.DAY_OF_MONTH, -2);
                    dpd1.getDatePicker().setMinDate(c.getTimeInMillis());
                    dpd1.getDatePicker().setMaxDate(System.currentTimeMillis());

                   // dpd1.getDatePicker().setMinDate(System.currentTimeMillis());

                } catch (Exception e) {
                    e.printStackTrace();
                }
                dpd1.show();
            }
        });


//        tvAssigned.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (Utilities.isNetworkAvailable(context)) {
//                    new GetTeamDetailsListForAssign().execute(CampId, CampDate);
//                } else {
//                    Utilities.showToastMessage("Please Check Your Connection", context, false);
//                }
//            }
//        });


        tvCampList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (CampDate != null) {
                    if (Utilities.isNetworkAvailable(context)) {
                        new GetCampList().execute(CampDate, String.valueOf(campTypeId), selectedLabID);
                    } else {
                        Utilities.showToastMessage("Please Check Your Connection", context, false);
                    }
                } else {
                    tvDate.setError("Select Camp Date");

                }


            }
        });


        tvLandingLab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utilities.isNetworkAvailable(context)) {
                    new GetLandingLab().execute(EmpCode);
                } else {
                    Utilities.showToastMessage("Please Check Your Connection", context, false);
                    return;
                }
            }
        });

        btnAssign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitData();
            }
        });


        tvTeamNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utilities.isNetworkAvailable(context)) {
                    new GetTeamDetailsListForAssign().execute(CampId, CampDate);
                } else {
                    Utilities.showToastMessage("Please Check Your Connection", context, false);
                }
            }
        });

        tvSampleBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utilities.isNetworkAvailable(context)) {
                    if (barcodeList==null || barcodeList.size() == 0){
                        if (campTypeId.equalsIgnoreCase("1")||campTypeId.equalsIgnoreCase("2")||campTypeId.equalsIgnoreCase("4")){
                            new GetBarcodeList().execute(CampDate, String.valueOf(campTypeId), selectedLabID, CampId, "0", "1");
                        }else if (campTypeId.equalsIgnoreCase("3")){
                            new GetBarcodeList().execute(CampDate, String.valueOf(campTypeId), selectedLabID, CampId, AssignedId, "1");
                        }

                    }else{
                        BarcodeListDialog(barcodeList);

                    }
                } else {
                    Utilities.showToastMessage("Please Check Your Connection", context, false);
                    return;
                }
            }
        });


        tvCampType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<CampTypeModel> campTypeModelArrayList = new ArrayList<>();
                campTypeModelArrayList.add(new CampTypeModel("REGULAR", 1));
                campTypeModelArrayList.add(new CampTypeModel("CSC CAMP", 2));
                campTypeModelArrayList.add(new CampTypeModel("DOOR TO DOOR", 3));
                campTypeModelArrayList.add(new CampTypeModel("CSC D2D", 4));


                showCampType(campTypeModelArrayList);


                tvCampList.setText("");
                if (campList != null) {
                    campList.clear();
                }


            }
        });


    }

    private void submitData() {

        if (tvDate.getText().toString().trim().isEmpty()) {
            Utilities.showToastMessage("Please Select Date", context, false);
            return;
        }
        if (tvCampType.getText().toString().trim().isEmpty()) {
            Utilities.showToastMessage("Please Select CampType", context, false);
            return;
        }
        if (tvLandingLab.getText().toString().trim().isEmpty()) {
            Utilities.showToastMessage("Please Select Landing Lab", context, false);
            return;
        }
        if (tvCampList.getText().toString().trim().isEmpty()) {
            Utilities.showToastMessage("Please Select Camp", context, false);
            return;
        }

        if (llteam.getVisibility() == View.VISIBLE){
            if (tvTeamNumber.getText().toString().trim().isEmpty()){
                Utilities.showToastMessage("Please Select Team", context, false);
                return;
            }


        }
        if (tvSampleBarcode.getText().toString().trim().isEmpty()) {
            Utilities.showToastMessage("Please Select Sample Barcode", context, false);
            return;
        }


        JsonArray barcodeJsonArray = new JsonArray();


        if (barcodeList != null) {
            //  Utilities.showToastMessage("Please select Doctor", context, false);

            int doctorSelected = 0;
            for (BarcodeListNewModel.Output t :
                    barcodeList) {
                if (t.isChecked()) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("Teamid", AssignedId);
                    jsonObject.addProperty("CampID", CampId);
                    jsonObject.addProperty("Labcode", selectedLabID);
                    jsonObject.addProperty("Barcode", t.getBarcode());
                    barcodeJsonArray.add(jsonObject);

                }
            }
        }

        JsonArray barcodeJsonArraynew = new JsonArray();


        if (barcodeList != null) {
            //  Utilities.showToastMessage("Please select Doctor", context, false);

            int doctorSelected = 0;
            for (BarcodeListNewModel.Output t :
                    barcodeList) {
                if (t.isChecked()) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("Teamid", "0");
                    jsonObject.addProperty("CampID", CampId);
                    jsonObject.addProperty("Labcode", selectedLabID);
                    jsonObject.addProperty("Barcode", t.getBarcode());
                    barcodeJsonArraynew.add(jsonObject);

                }
            }
        }


        Log.d("Submit", "submitData: " + barcodeJsonArray.toString());

        if (Utilities.isNetworkAvailable(context)) {

            if (campTypeId.equalsIgnoreCase("1")||(campTypeId.equalsIgnoreCase("2")||(campTypeId.equalsIgnoreCase("4")))){
                new InsertAcceptSample().execute(barcodeJsonArraynew.toString(), EmpCode, edtRemark.getText().toString().trim());

            }else if (campTypeId.equalsIgnoreCase("3")){
                new InsertAcceptSample().execute(barcodeJsonArray.toString(), EmpCode, edtRemark.getText().toString().trim());
            }

        } else {
            Utilities.showMessage(R.string.msgt_nointernetconnection, context);
        }


    }


    private void showCampType(final ArrayList<CampTypeModel> campTypeModelsList) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
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

                if (campTypeId.equalsIgnoreCase("1")||(campTypeId.equalsIgnoreCase("2")||(campTypeId.equalsIgnoreCase("4")))){
                    llteam.setVisibility(View.GONE);
                }else {
                    llteam.setVisibility(View.VISIBLE);
                }
                if (barcodeList!= null){
                    barcodeList.clear();
                }
                tvSampleBarcode.setText("");







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
            param.add(new ParamsPojo("CampType", params[1]));
            param.add(new ParamsPojo("LABCODE", params[2]));


            res = WebServiceCall.APICall(ApplicationConstants.GetCampListByLandingLab, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    campList = new ArrayList<>();
                    selectedCampList = new ArrayList<>();
                    List<CamplistOnLandingLabModel.Output> campList = new ArrayList<>();

                    //  Log.d("result ",result.toString());


                    CamplistOnLandingLabModel camplistOnLandingLabModel = new Gson().fromJson(result, CamplistOnLandingLabModel.class);
                    type = camplistOnLandingLabModel.getStatus();
                    message = camplistOnLandingLabModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {

                        campList = camplistOnLandingLabModel.getOutput();

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

    private void showCampListDialog(final List<CamplistOnLandingLabModel.Output> campList) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
        builderSingle.setTitle("Select Camp");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        for (int i = 0; i < campList.size(); i++) {
            arrayAdapter.add(String.valueOf(campList.get(i).getCampId()));

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
                tvCampList.setText(String.valueOf(campList.get(which).getCampId()));
                CampId = String.valueOf(campList.get(which).getCampId());
                CampDate = campList.get(which).getCampDate();

                if (campTypeId.equalsIgnoreCase("1")||campTypeId.equalsIgnoreCase("2")||campTypeId.equalsIgnoreCase("4")){
                    new GetBarcodeCount().execute(CampDate, String.valueOf(campTypeId), selectedLabID, CampId, "0", "2");
                }else if (campTypeId.equalsIgnoreCase("3")){
                }

                if (assignteamlist!= null){
                    assignteamlist.clear();
                    tvTeamNumber.setText("");
                }



                //  refreshCalendar();
            }
        });
        builderSingle.show();

    }

    public class GetLandingLab extends AsyncTask<String, Void, String> {

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

            res = WebServiceCall.APICall(ApplicationConstants.GetUserMappedLabList, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    //  campList = new ArrayList<>();
                    List<LandingLabNewModel.Output> labList = new ArrayList<>();

                    //  Log.d("result ",result.toString());

                    LandingLabNewModel landingLabNewModel = new Gson().fromJson(result, LandingLabNewModel.class);
                    type = landingLabNewModel.getStatus();
                    message = landingLabNewModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {

                        labList = landingLabNewModel.getOutput();

                        if (labList.size() > 0) {
                            // districtList.add(0, new DistrictList_Model("0", "All"));

                            showLandingDialog(labList);
                        } else {
                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                        }
                    } else {
                        Utilities.showAlertDialog(context, "Fail", message, false);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void showLandingDialog(final List<LandingLabNewModel.Output> labList) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
        builderSingle.setTitle("Select Landing Lab");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        for (int i = 0; i < labList.size(); i++) {
            arrayAdapter.add(String.valueOf(labList.get(i).getLabName()));

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
                tvLandingLab.setText(labList.get(which).getLabName());
                selectedLabID = String.valueOf(labList.get(which).getLabCode());
                //  CampDate = labList.get(which).getCampDate();


                //  refreshCalendar();
            }
        });
        builderSingle.show();

    }


    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Accept Samples In Lab");

        ImageButton btn_save_accordian = findViewById(R.id.btn_save_accordian);


        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });

//        btn_save_accordian.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(context, TeamCampMappingdetailsActivity.class));
//                finish();
//            }
//        });
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


            res = WebServiceCall.APICall(ApplicationConstants.GetTeamDetailsForSampleAcceptance, ApplicationConstants.webservice_d2d, param);
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
                    ArrayList<TeamsDetailsModelNew.OutputBean> assignteamlist = new ArrayList<>();
                    TeamsDetailsModelNew teamsDetailsModelNew = new Gson().fromJson(result, TeamsDetailsModelNew.class);
                    type = teamsDetailsModelNew.getStatus();
                    message = teamsDetailsModelNew.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        assignteamlist = teamsDetailsModelNew.getOutput();
                        if (assignteamlist.size() > 0) {
                            //  districtList.add(0, new DistrictList_Model("0", "All"));

                            showAssignedTeamListDialog(assignteamlist);

                            // ll_assigned.setVisibility(View.VISIBLE);


                        } else {
                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                        }
                    } else {
                        Utilities.showAlertDialog(context, "Fail", "Team not assigned for selected campId, assign team first", false);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Please select CampId", "You have to select campId", false);
            }
        }
    }

    private void showAssignedTeamListDialog(final ArrayList<TeamsDetailsModelNew.OutputBean> assignteamlist) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
        builderSingle.setTitle("Select Team");
        builderSingle.setCancelable(false);
        View dialogueView = getLayoutInflater().inflate(R.layout.recyclerview_dialogue, null);
        builderSingle.setView(dialogueView);

        RecyclerView rvList = dialogueView.findViewById(R.id.rvList);
        EditText edt_search = dialogueView.findViewById(R.id.edt_search);
        rvList.setHasFixedSize(true);
        rvList.setLayoutManager(new LinearLayoutManager(context));

        rvList.setAdapter(new TeamsDetailsAssignedAdapterNew(assignteamlist));

        rvList.addOnItemTouchListener(
                new RecyclerItemClickListener(context,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, final int position) {
                                TeamsDetailsModelNew.OutputBean team = assignteamlist.get(position);
                                AssignedId = assignteamlist.get(position).getTeamNumber();

                                tvTeamNumber.setText(assignteamlist.get(position).getTeamName());
                                teamDialog.dismiss();

                                if (barcodeList!=null){
                                    barcodeList.clear();
                                }
                                tvSampleBarcode.setText("");


                                new GetBarcodeCount().execute(CampDate, String.valueOf(campTypeId), selectedLabID, CampId, AssignedId, "2");

                            }
                        }));

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count) {

                if (query.toString().isEmpty()) {
                    rvList.setAdapter(new TeamsDetailsAssignedAdapterNew(assignteamlist));
                    return;
                }

                if (assignteamlist.size() == 0) {
                    rvList.setVisibility(View.GONE);
                    return;
                }

                if (!query.toString().equals("")) {
                    ArrayList<TeamsDetailsModelNew.OutputBean> searchedTestList = new ArrayList<>();
                    for (TeamsDetailsModelNew.OutputBean clientDetails : assignteamlist) {

                        String countryToBeSearched = clientDetails.getMember1().toLowerCase();
                        String member2 = clientDetails.getMember2().toLowerCase();

                        if (countryToBeSearched.contains(query.toString().toLowerCase()) ||
                                member2.contains(query.toString().toLowerCase())) {
                            searchedTestList.add(clientDetails);
                        }
                    }
                    rvList.setAdapter(new TeamsDetailsAssignedAdapterNew(searchedTestList));
                } else {
                    rvList.setAdapter(new TeamsDetailsAssignedAdapterNew(assignteamlist));
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
                for (TeamsDetailsModelNew.OutputBean team : assignteamlist
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


    private class GetBarcodeList extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("campdate", params[0]));
            param.add(new ParamsPojo("camptype", params[1]));
            param.add(new ParamsPojo("LandingLab", params[2]));
            param.add(new ParamsPojo("campID", params[3]));
            param.add(new ParamsPojo("Teamid", params[4]));
            param.add(new ParamsPojo("type", params[5]));
            res = WebServiceCall.APICall(ApplicationConstants.GetSampleBarcodeList, ApplicationConstants.webservice_d2d, param);

            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String status = "", message = "";
            Log.d("GetBarcodelist Resp", result);

            try {
                pd.dismiss();
                if (!result.equals("")) {
                    BarcodeListNewModel barcodeListNewModel = new Gson().fromJson(result, BarcodeListNewModel.class);
                    if (barcodeListNewModel.getStatus().equalsIgnoreCase("success")) {
//                        Collections.sort(clientListModel.getOutput(), (o1, o2) -> o1.getClientName().compareTo(o2.getClientName()));
                        if (barcodeListNewModel.getOutput().size() > 0) {
                            barcodeList = new ArrayList<>();
                            selectedBarcodeList = new ArrayList<>();
                            /**
                             * 1. Channel Partner Registration
                             * 2. OnBoard User For Channel Partner
                             * 3. Camp Creation D2D or Regular (org by Channel Partner), No device, No Consumables
                             * */

                            for (BarcodeListNewModel.Output o : barcodeListNewModel.getOutput()
                            ) {
                                o.setChecked(true);
                                barcodeList.add(o);
                            }
                            BarcodeListDialog(barcodeList);

                        } else {
                            Utilities.showAlertDialog(context, "No Barcodes Available", "", false);

                        }
                    } else
                        Utilities.showAlertDialog(context, "Fail", "Barcode Not Found", false);

                } else {
                    Utilities.showAlertDialog(context, "Fail", "Empty response", false);

                }
            } catch (Exception e) {
                Utilities.showAlertDialog(context, "Fail",
                        "Data Not Found Or Select Team", false);
                e.printStackTrace();
            }
        }
    }


    android.app.AlertDialog labDialog;

    private void BarcodeListDialog(ArrayList<BarcodeListNewModel.Output> mainlist) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_search_test, null);

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setView(view);
        builder.setTitle("Select Barcodes");
        builder.setCancelable(false);

        final RecyclerView rv_testlist = view.findViewById(R.id.rv_testlist);
        EditText edt_search = view.findViewById(R.id.edt_search);
        rv_testlist.setLayoutManager(new LinearLayoutManager(context));
        rv_testlist.setAdapter(new BarcodeListNewAdapter(mainlist));

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count) {

                if (query.toString().isEmpty()) {
                    rv_testlist.setAdapter(new BarcodeListNewAdapter(mainlist));
                    return;
                }

                if (mainlist.size() == 0) {
                    rv_testlist.setVisibility(View.GONE);
                    return;
                }

                if (!query.toString().equals("")) {
                    ArrayList<BarcodeListNewModel.Output> searchedTestList = new ArrayList<BarcodeListNewModel.Output>();
                    for (BarcodeListNewModel.Output clientDetails : mainlist) {

                        String countryToBeSearched = clientDetails.getBarcode().toLowerCase();

                        if (countryToBeSearched.contains(query.toString().toLowerCase())) {
                            searchedTestList.add(clientDetails);
                        }
                    }
                    rv_testlist.setAdapter(new BarcodeListNewAdapter(searchedTestList));
                } else {
                    rv_testlist.setAdapter(new BarcodeListNewAdapter(mainlist));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        builder.setNeutralButton("cancel", (dialog, which) -> {

        });
        builder.setPositiveButton("Okay", (dialog, which) -> {
            Log.d("Selected Barcode", new Gson().toJson(barcodeList));
            int barcodeCount = 0;

            for (BarcodeListNewModel.Output output :
                    barcodeList) {
                if (output.isChecked()){
                    barcodeCount+= 1;

                }

            }

            tvSampleBarcode.setText("Total sample Barcodes " +barcodeCount);
            tvSampleCount.setText(""+barcodeCount);
            //  edt_sample_count.setText(String.valueOf(selectedBarcodeList.size()));



        });

        builder.setNeutralButton("Select All", (dialogInterface, i) -> {
          //  selectedBarcodeList.clear();
            for (BarcodeListNewModel.Output output :
                    barcodeList) {
                output.setChecked(true);

            }
            selectedCampList.addAll(campList);
            tvSampleBarcode.setText("Total Barcodes " + barcodeList.size());
            tvSampleCount.setText(""+barcodeList.size());

            // edt_sample_count.setText(String.valueOf(selectedBarcodeList.size()));


        });

        labDialog = builder.create();
        labDialog.show();
    }


    private class GetBarcodeCount extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("campdate", params[0]));
            param.add(new ParamsPojo("camptype", params[1]));
            param.add(new ParamsPojo("LandingLab", params[2]));
            param.add(new ParamsPojo("campID", params[3]));
            param.add(new ParamsPojo("Teamid", params[4]));
            param.add(new ParamsPojo("type", params[5]));
            res = WebServiceCall.APICall(ApplicationConstants.GetSampleBarcodeList, ApplicationConstants.webservice_d2d, param);

            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String status = "", message = "";
            Log.d("GetBarcodelist Resp", result);

            try {
                pd.dismiss();
                if (!result.equals("")) {
                    BarcodeListCountNewModel barcodeListCountNewModel = new Gson().fromJson(result, BarcodeListCountNewModel.class);
                    if (barcodeListCountNewModel.getStatus().equalsIgnoreCase("success")) {
//                        Collections.sort(clientListModel.getOutput(), (o1, o2) -> o1.getClientName().compareTo(o2.getClientName()));
                        if (barcodeListCountNewModel.getOutput().size() > 0) {
                            BarcodeListCountNewModel.Output output = barcodeListCountNewModel.getOutput().get(0);


                            tvSampleCount.setText(output.getTotalBarcode());

//                            barcodeList = new ArrayList<>();
                            // selectedBarcodeList = new ArrayList<>();
                            //  barcodecountList = barcodeListCountNewModel.getOutput();


//                             ArrayList<BarcodeListCountNewModel.Output> count =barcodeListCountNewModel.getOutput();

//                            BarcodeListDialog(barcodeList);


                        } else {
                            Utilities.showAlertDialog(context, "No Barcodes Available", "", false);

                        }
                    } else
                        Utilities.showAlertDialog(context, "Fail", "Barcode Not Found", false);

                } else {
                    Utilities.showAlertDialog(context, "Fail", "Empty response", false);

                }
            } catch (Exception e) {
                Utilities.showAlertDialog(context, "Fail",
                        "Data Not Found", false);
                e.printStackTrace();
            }
        }
    }


    private class InsertAcceptSample extends AsyncTask<String, Void, String> {

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
            Log.d("InsertSampleAccept", "doInBackground: " + Arrays.toString(params));
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("SampleAccept", params[0]));
            param.add(new ParamsPojo("CreatedBy", params[1]));
            param.add(new ParamsPojo("Remark", params[2]));
            res = WebServiceCall.APICall(ApplicationConstants.InsertSampleAcceptance, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("InsertSampleAccept", "onPostExecute: " + result);
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
                        builder.setMessage("Sample Accepted successfully");
                        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();

                                //  ll_assigned.setVisibility(View.VISIBLE);


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