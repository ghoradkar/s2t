package com.myhindlab.abkat.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.adapters.HomeLabAdapter;
import com.myhindlab.abkat.models.CampListModel;
import com.myhindlab.abkat.models.CampTypeModel;
import com.myhindlab.abkat.models.HomeLabHubLabModel;
import com.myhindlab.abkat.models.HomeLabHublabDetailsModel;
import com.myhindlab.abkat.models.RemarkModel;
import com.myhindlab.abkat.models.TeamsDetailsModel;
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

public class UpdateProcessLabActivity extends AppCompatActivity {

    private Context context;
    private UserSessionManager session;
    private static ProgressDialog pd;
    private TextView textView, tvDate, tvHomelab, tvHubLab, tvSampleCount, tvTeamHeader, tvTeamNumber, tvSampleBarcode, tv_teamnumber, tvDoctorHeader, tvPhleboHeader, tvDataEntryHeader, tvTeamId, tvCampType, tvDistrict, tvLandingLab, tvCampList, tvExternalPhlebo, tvAssigned, tvTeams;
    private Button btnAssign;
    private EditText edtRemark, edtOther,edtTokenForm;
    private LinearLayout llteam, llRemark;
    private int mYear, mMonth, mDay, mYear1, mMonth1, mDay1;
    private ArrayList<CampListModel.OutputBean> campList;
    private ArrayList<CampListModel.OutputBean> selectedCampList;
    private TeamsDetailsModel teamsDetailsModel;
    private ArrayList<TeamsDetailsModel> assignteamlist;
    private HomeLabHubLabModel homeLabHubLabModel;


    private AlertDialog teamDialog;
    private ArrayList<BarcodeListNewModel.Output> barcodeList;
    private String DESGID, EmpCode, remarkId, LabCode, selectedHubLabID, CampDATE, DISTLGDCODE, district, TALLGDCODE, taluka, STATELGDCODE = "2", selectedHomeLabID, campTypeId, selectedLabID = "", resorceId, selectedLabName = "", exernalPhleboId, TeamID, CampId, CampDate, UserId, AssignedId = "", IsTeam;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_process_lab);

        init();
        getSessionData();
        setEventHandlers();
        setUpToolbar();


    }

    private void init() {
        context = UpdateProcessLabActivity.this;
        session = new UserSessionManager(context);
        pd = new ProgressDialog(context);
        tvCampType = findViewById(R.id.tvCampType);
        tvDistrict = findViewById(R.id.tvDistrict);
        tvLandingLab = findViewById(R.id.tvLandingLab);
        tvCampList = findViewById(R.id.tvCampList);
        tvDate = findViewById(R.id.tvDate);
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
        tvHubLab = findViewById(R.id.tvHubLab);
        tvHomelab = findViewById(R.id.tvHomelab);
        edtRemark = findViewById(R.id.edtRemark);
        edtOther = findViewById(R.id.edtOther);
        llRemark = findViewById(R.id.llRemark);
        edtTokenForm = findViewById(R.id.edtTokenForm);


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
//
                                if (campList != null){
                                    campList.clear();
                                }
                                tvHomelab.setText("");
                                tvHubLab.setText("");


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

        tvHomelab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utilities.isNetworkAvailable(context)) {
                    new GetHomeLab(1).execute();
                } else {
                    Utilities.showToastMessage("Please Check Your Connection", context, false);
                    return;
                }
            }
        });
        tvHubLab.setOnClickListener(v -> {


            new GetHomeLab(2).execute();


        });


//        tvHubLab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (Utilities.isNetworkAvailable(context)) {
//                    new GetHubLab().execute();
//
//                } else {
//                    Utilities.showToastMessage("Please Check Your Connection", context, false);
//                    return;
//                }
//            }
//        });

        edtRemark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utilities.isNetworkAvailable(context)) {
                    new GetRemark().execute();
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

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Change Process Lab");

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
//        if (edtOther.getVisibility() == View.VISIBLE){
//            if (edtOther.getText().toString().isEmpty()) {
//                Utilities.showToastMessage("Please Enter other Description", context, false);
//                return;
//            }
//        }

        if (llRemark.getVisibility() == View.VISIBLE) {
            if (edtOther.getText().toString().trim().isEmpty()) {
                Utilities.showToastMessage("Please Enter other Description", context, false);
                return;
            }

        }
        if (edtTokenForm.getText().toString().trim().isEmpty()){
            Utilities.showToastMessage("Please Enter Approval Taken From", context, false);
            return;
        }


        //  Log.d("Submit", "submitData: " + barcodeJsonArray.toString());

        if (Utilities.isNetworkAvailable(context)) {
            new UpdateLab().execute(CampId, selectedHomeLabID, selectedHubLabID, remarkId, edtOther.getText().toString().trim(),edtTokenForm.getText().toString(), EmpCode);

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

                tvLandingLab.setText("");
                tvCampList.setText("");
                if (campList != null){
                    campList.clear();
                }
                tvHomelab.setText("");
                tvHubLab.setText("");



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

                new GetHomeLabHubLabDetails().execute(CampId, CampDate);
                //  new GetHubLab().execute();


                //  refreshCalendar();
            }
        });
        builderSingle.show();

    }

    public class GetHomeLab extends AsyncTask<String, Void, String> {

        int type = 1;

        public GetHomeLab(int i) {
            this.type = i;
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

            res = WebServiceCall.APICall(ApplicationConstants.GetHomeAndHubLab, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String status = "", message = "";
            try {
                if (!result.equals("")) {
                    //  campList = new ArrayList<>();
//                    ArrayList<HomeLabHubLabModel.Output> homelabList = new ArrayList<>();

                    //  Log.d("result ",result.toString());

                    homeLabHubLabModel = new Gson().fromJson(result, HomeLabHubLabModel.class);
                    status = homeLabHubLabModel.getStatus();
                    message = homeLabHubLabModel.getMessage();
                    if (status.equalsIgnoreCase("success")) {

//                        homelabList = homeLabHubLabModel.getOutput();

                        if (homeLabHubLabModel.getOutput().size() > 0) {
                            // districtList.add(0, new DistrictList_Model("0", "All"));
                            if (type == 1) {

                                showHomelabDialog(homeLabHubLabModel.getOutput());
                            } else {
                                ArrayList<HomeLabHubLabModel.Output> labListTwo = new ArrayList<>();
                                for (int i = 0; i < homeLabHubLabModel.getOutput().size(); i++) {
                                    if (homeLabHubLabModel.getOutput().get(i).getIsHubLab() == 1) {
                                        labListTwo.add(homeLabHubLabModel.getOutput().get(i));

                                    }
                                }

                                showHublabDialog(labListTwo);
                            }

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





    private void showHomelabDialog(final ArrayList<HomeLabHubLabModel.Output> homelabList) {

        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
        builderSingle.setTitle("Select Home Lab");
        builderSingle.setCancelable(false);
        View dialogueView = getLayoutInflater().inflate(R.layout.recyclerview_dialogue, null);
        builderSingle.setView(dialogueView);


        RecyclerView rvList = dialogueView.findViewById(R.id.rvList);
        EditText edt_search = dialogueView.findViewById(R.id.edt_search);

        rvList.setHasFixedSize(true);
        rvList.setLayoutManager(new LinearLayoutManager(context));

        HomeLabAdapter homeLabAdapter = new HomeLabAdapter(homelabList);
        rvList.setAdapter(homeLabAdapter);

        ArrayList<HomeLabHubLabModel.Output> filteredList = new ArrayList<>();

        //        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        //        for (Lab_OutPut_Pojo subTrenchModel : lab_List) {
        //            arrayAdapter.add(subTrenchModel.getLabName());
        //        }

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count) {

                if (filteredList != null)
                    filteredList.clear();

                if (homelabList != null) {
                    if (edt_search.getText().toString().equals("")) {
                        filteredList.addAll(homelabList);
                        rvList.setAdapter(new HomeLabAdapter(filteredList));

                    } else {
                        if (homelabList.size() > 0) {
                            for (HomeLabHubLabModel.Output pojo : homelabList) {
                                String siteDetails = pojo.getLabName();
                                if (siteDetails != null && siteDetails.toLowerCase().contains(edt_search.getText().toString().toLowerCase())) {
                                    filteredList.add(pojo);
                                }
                            }

                            if (filteredList.size() == 0) {
                                filteredList.addAll(homelabList);
                                rvList.setAdapter(new HomeLabAdapter(filteredList));
                            } else {
                                rvList.setAdapter(new HomeLabAdapter(filteredList));

                            }
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        AlertDialog alertDialog = builderSingle.create();

        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


//                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        selectedLabID = lab_List.get(which).getLabCode();
//                        selectedLabName = lab_List.get(which).getLabName();
//                        tvLab.setText(selectedLabName);
//                    }
//                });

        rvList.addOnItemTouchListener(new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (filteredList.size() > 0) {
                    selectedHomeLabID = String.valueOf(filteredList.get(position).getLabCode());
                    selectedLabName = filteredList.get(position).getLabName();
                    tvHomelab.setText(selectedLabName);
                } else {
                    selectedHomeLabID = String.valueOf(homelabList.get(position).getLabCode());
                    selectedLabName = homelabList.get(position).getLabName();
                    tvHomelab.setText(selectedLabName);
                }
                alertDialog.dismiss();

            }
        }));

        alertDialog.show();

    }

    public class GetRemark extends AsyncTask<String, Void, String> {

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

            res = WebServiceCall.APICall(ApplicationConstants.GetUpdateLabRemark, ApplicationConstants.webservice_d2d, param);
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
                    List<RemarkModel.Output> remarklist = new ArrayList<>();

                    //  Log.d("result ",result.toString());

                    RemarkModel remarkModel = new Gson().fromJson(result, RemarkModel.class);
                    type = remarkModel.getStatus();
                    message = remarkModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {

                        remarklist = remarkModel.getOutput();

                        if (remarklist.size() > 0) {
                            // districtList.add(0, new DistrictList_Model("0", "All"));

                            showRemarkdialogue(remarklist);
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

    private void showRemarkdialogue(final List<RemarkModel.Output> remarklist) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
        builderSingle.setTitle("Select Landing Lab");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        for (int i = 0; i < remarklist.size(); i++) {
            arrayAdapter.add(String.valueOf(remarklist.get(i).getRemark()));

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
                edtRemark.setText(remarklist.get(which).getRemark());
                remarkId = String.valueOf(remarklist.get(which).getRemarkID());
                //  CampDate = labList.get(which).getCampDate();

                if (remarkId.equalsIgnoreCase("2")) {
                    llRemark.setVisibility(View.VISIBLE);
                } else {
                    llRemark.setVisibility(View.GONE);
                }


                //  refreshCalendar();
            }
        });
        builderSingle.show();

    }

    private class UpdateLab extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("CampID", params[0]));
            param.add(new ParamsPojo("ReqHomeLab", params[1]));
            param.add(new ParamsPojo("ReqHubLab", params[2]));
            param.add(new ParamsPojo("RemarkID", params[3]));
            param.add(new ParamsPojo("Remark", params[4]));
            param.add(new ParamsPojo("ApprovalFrom", params[5]));
            param.add(new ParamsPojo("CreatedBy", params[6]));
            res = WebServiceCall.APICall(ApplicationConstants.UpdateProcessLab, ApplicationConstants.webservice_d2d, param);
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
                        builder.setMessage("Update Process Lab successfully");
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

    public class GetHubLab extends AsyncTask<String, Void, String> {

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

            res = WebServiceCall.APICall(ApplicationConstants.GetHomeAndHubLab, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    // hublabList = new ArrayList<>();
                    ArrayList<HomeLabHubLabModel.Output> hublabList = new ArrayList<>();


                    //  Log.d("result ",result.toString());

                    HomeLabHubLabModel homeLabHubLabModel = new Gson().fromJson(result, HomeLabHubLabModel.class);
                    type = homeLabHubLabModel.getStatus();
                    message = homeLabHubLabModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {

                        hublabList = homeLabHubLabModel.getOutput();

                        if (hublabList.size() > 0) {
                            // districtList.add(0, new DistrictList_Model("0", "All"));

                            showHublabDialog(hublabList);
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

    private void showHublabDialog(final ArrayList<HomeLabHubLabModel.Output> hublabList) {

        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
        builderSingle.setTitle("Select Hub Lab");
        builderSingle.setCancelable(false);
        View dialogueView = getLayoutInflater().inflate(R.layout.recyclerview_dialogue, null);
        builderSingle.setView(dialogueView);


        RecyclerView rvList = dialogueView.findViewById(R.id.rvList);
        EditText edt_search = dialogueView.findViewById(R.id.edt_search);

        rvList.setHasFixedSize(true);
        rvList.setLayoutManager(new LinearLayoutManager(context));

        HomeLabAdapter homeLabAdapter = new HomeLabAdapter(hublabList);
        rvList.setAdapter(homeLabAdapter);

        ArrayList<HomeLabHubLabModel.Output> filteredList = new ArrayList<>();

        //        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        //        for (Lab_OutPut_Pojo subTrenchModel : lab_List) {
        //            arrayAdapter.add(subTrenchModel.getLabName());
        //        }

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count) {

                if (filteredList != null)
                    filteredList.clear();

                if (hublabList != null) {
                    if (edt_search.getText().toString().equals("")) {
                        filteredList.addAll(hublabList);
                        rvList.setAdapter(new HomeLabAdapter(filteredList));

                    } else {
                        if (hublabList.size() > 0) {
                            for (HomeLabHubLabModel.Output pojo : hublabList) {
                                String siteDetails = pojo.getLabName();
                                if (siteDetails != null && siteDetails.toLowerCase().contains(edt_search.getText().toString().toLowerCase())) {
                                    filteredList.add(pojo);
                                }
                            }

                            if (filteredList.size() == 0) {
                                filteredList.addAll(hublabList);
                                rvList.setAdapter(new HomeLabAdapter(filteredList));
                            } else {
                                rvList.setAdapter(new HomeLabAdapter(filteredList));

                            }
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        AlertDialog alertDialog = builderSingle.create();

        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


//                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        selectedLabID = lab_List.get(which).getLabCode();
//                        selectedLabName = lab_List.get(which).getLabName();
//                        tvLab.setText(selectedLabName);
//                    }
//                });

        rvList.addOnItemTouchListener(new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (filteredList.size() > 0) {
                    selectedHubLabID = String.valueOf(filteredList.get(position).getLabCode());
                    selectedLabName = filteredList.get(position).getLabName();
                    tvHubLab.setText(selectedLabName);
                } else {
                    selectedHubLabID = String.valueOf(hublabList.get(position).getLabCode());
                    selectedLabName = hublabList.get(position).getLabName();
                    tvHubLab.setText(selectedLabName);
                }
                alertDialog.dismiss();

            }
        }));

        alertDialog.show();

    }

    public class GetHomeLabHubLabDetails extends AsyncTask<String, Void, String> {

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

            res = WebServiceCall.APICall(ApplicationConstants.GetCampDetailsForLabUpate, ApplicationConstants.webservice_d2d, param);
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
                    // selectedCampList = new ArrayList<>();
                    List<HomeLabHublabDetailsModel.Output> campList = new ArrayList<>();

                    HomeLabHublabDetailsModel homeLabHublabDetailsModel = new Gson().fromJson(result, HomeLabHublabDetailsModel.class);
                    type = homeLabHublabDetailsModel.getStatus();
                    message = homeLabHublabDetailsModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {

                        //  campList = HomeLabHublabDetailsModel.getOutput();
                        if (homeLabHublabDetailsModel.getOutput().size() > 0) {
                            HomeLabHublabDetailsModel.Output output = homeLabHublabDetailsModel.getOutput().get(0);

                            tvHomelab.setText(output.getHomeLab());
                            tvHubLab.setText(output.getHubLab());
                            selectedHomeLabID = String.valueOf(output.getHomeLabI());
                            selectedHubLabID = String.valueOf(output.getHubLabID());

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


}
