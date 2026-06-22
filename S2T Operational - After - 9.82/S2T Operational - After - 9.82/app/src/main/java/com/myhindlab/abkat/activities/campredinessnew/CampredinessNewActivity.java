package com.myhindlab.abkat.activities.campredinessnew;

import static androidx.fragment.app.FragmentManager.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.gson.Gson;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.SiteSurvey_Menu_Activity;
import com.myhindlab.abkat.activities.TeamCampMappingActivity;
import com.myhindlab.abkat.activities.TeamCampMappingdetailsActivity;
import com.myhindlab.abkat.activities.campredinessnew.CampredinessAdapter;
import com.myhindlab.abkat.activities.doortodoor.D2DSelectCampActivity;
import com.myhindlab.abkat.adapters.CampListAdpterForReadinessAdapter;
import com.myhindlab.abkat.adapters.LabListAdapter;
import com.myhindlab.abkat.databinding.ActivityCampredinessNewBinding;
import com.myhindlab.abkat.models.CampListModel;
import com.myhindlab.abkat.models.CampTypeModel;
import com.myhindlab.abkat.models.DistrictList_Model;
import com.myhindlab.abkat.models.NewCampTypeModel;


import com.myhindlab.abkat.pojos.DistrictList_Pojo;
import com.myhindlab.abkat.pojos.Lab_OutPut_Pojo;
import com.myhindlab.abkat.rest.ApiClient;
import com.myhindlab.abkat.rest.ApiInterface;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.RecyclerItemClickListener;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CampredinessNewActivity extends AppCompatActivity implements CampredinessAdapter.RBOnclicklisner {

    ActivityCampredinessNewBinding binding;
    private int mYear, mMonth, mDay, mYear1, mMonth1, mDay1;
    String CampDate, campTypeId;
    private Context context;
    private ProgressDialog pd;
    private UserSessionManager session;
    private String DESGID, EmpCode, LabCode, CampDATE, DISTLGDCODE, district, TALLGDCODE, taluka,
            STATELGDCODE = "2", selectedLabID = "", resorceId, selectedLabName = "", exernalPhleboId, TeamID = "0", TeamName,
            CampId = "0", UserId, AssignedId = "", IsTeam;
    private ArrayList<CampListModel.OutputBean> campList;
    List<Type_CampReadinessForm> typeCampReadinessFormslist;

    CampredinessAdapter campredinessAdapter;
    private boolean flagstutus = false;
    private String flagsaved = "0";
    int lastVisibleItemPosition;
    ArrayList<OutputItem> itemArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_camprediness_new);
        context = CampredinessNewActivity.this;
        session = new UserSessionManager(context);
        pd = new ProgressDialog(context);
        Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);
        binding.tvDate.setText(Utilities.dfDate4.format(new Date()));
        CampDate = Utilities.dfDate4.format(new Date());


        String note = "Note : Scroll down to view remaining item";

// Create a SpannableString with your text
        SpannableString spannableString = new SpannableString(note);

// Find the index of the word you want to bold
        int startIndex = note.indexOf("Note");

// Apply the bold style to the specified range of text
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), startIndex, startIndex + "Note".length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);

// Set the modified text to your TextView or wherever you want to display it
        binding.tvNote.setText(spannableString);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.tvNote.setTextColor(getColor(R.color.red));
        }

        getSessionData();
        setUpToolbar();
        binding.tvDate.setEnabled(false);

        binding.tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd1 = new DatePickerDialog(CampredinessNewActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                binding.tvDate.setText(Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year));
                                CampDate = Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year);
                            }

                        }, mYear, mMonth, mDay);
                try {
                    dpd1.getDatePicker().setCalendarViewShown(false);

                    // dpd1.getDatePicker().setMinDate(System.currentTimeMillis());

                } catch (Exception e) {
                    e.printStackTrace();
                }
                dpd1.show();
            }
        });


        binding.tvDistrict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetDistrictList().execute("2",EmpCode);

            }
        });


        binding.tvCampType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (DESGID.equalsIgnoreCase("129") || DESGID.equalsIgnoreCase("146")) {

                    new GetFlexiCampType().execute();

                } else if (DESGID.equalsIgnoreCase("138")||DESGID.equalsIgnoreCase("137")
                        ||DESGID.equalsIgnoreCase("169")||DESGID.equalsIgnoreCase("177")
                        ||DESGID.equalsIgnoreCase("31")||DESGID.equalsIgnoreCase("176")

                ) {
                    new GetCampTypeMMU().execute();


                } else if (DESGID.equalsIgnoreCase("35") || DESGID.equalsIgnoreCase("86")) {

                    ArrayList<CampTypeModel> campTypeModelArrayList = new ArrayList<>();
                    campTypeModelArrayList.add(new CampTypeModel("DOOR TO DOOR", 3));
//                    campTypeModelArrayList.add(new CampTypeModel("FLEXI CAMP", 5));
                    showCampType(campTypeModelArrayList);

                } else {

                    ArrayList<CampTypeModel> campTypeModelArrayList = new ArrayList<>();
                    campTypeModelArrayList.add(new CampTypeModel("REGULAR", 1));
                    // campTypeModelArrayList.add(new CampTypeModel("CSC REGULAR CAMP", 2));
                    //campTypeModelArrayList.add(new CampTypeModel("DOOR TO DOOR", 3));
//                    campTypeModelArrayList.add(new CampTypeModel("CSC D2D", 4));
                    //                campTypeModelArrayList.add(new CampTypeModel("CSC", 2));
                    showCampType(campTypeModelArrayList);
                }
            }
        });

        binding.tvCampList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (binding.tvDistrict.getText().toString().trim().isEmpty()){
                    Utilities.showAlertDialog(context,"Alert","Please select district",false);
                    return;
                }

 

                if (CampDate != null) {
                    if (Utilities.isNetworkAvailable(context)) {
                        new GetCampList().execute(CampDate, EmpCode, DISTLGDCODE, String.valueOf(campTypeId), "0");
                    } else {
                        Utilities.showToastMessage("Please Check Your Connection", context, false);
                    }
                } else {
                    binding.tvDate.setError("Select Camp Date");
                }
            }
        });

        //   getCampdetails();

        binding.rvCampview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                int totalItemCount = layoutManager.getItemCount();

                // Check if the last visible item is the last item in the list
                if (lastVisibleItemPosition == totalItemCount - 1) {
                    // RecyclerView scrolled to the end
                    // Do something here
                }
            }
        });


        binding.btnSaveloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
           /*     if (!flagstutus) {
                    if (flagsaved.equals("1")) {
                        binding.btnSaveloc.setText("Submitted");
                        binding.btnSaveloc.setEnabled(false);
                    } else {
                        binding.btnSaveloc.setText("Submit");
                        binding.btnSaveloc.setEnabled(true);

                    }

                    binding.btnBack.setVisibility(View.VISIBLE);
                    ArrayList<OutputItem> itemArraysecondtList = new ArrayList<>();
                    for (int i = 0; i < itemArrayList.size(); i++) {
                        itemArraysecondtList.add(new OutputItem(itemArrayList.get(i).getItemStatus(), itemArrayList.get(i).getIsActive(), itemArrayList.get(i).getItemType(), itemArrayList.get(i).getItemName(), itemArrayList.get(i).getItemId()));
                    }


                   // binding.rvCampview.smoothScrollToPosition(itemArraysecondtList.size() - 1);
//                    campredinessAdapter.nextPage();
                    flagstutus = true;
                    //  setadapter(flagstutus,itemArraysecondtList);
                } else {*/
                List<OutputItem> outputItems = campredinessAdapter.getOutputItems();
                ArrayList<Type_CampReadinessForm> type_campReadinessFormArrayList = new ArrayList<>();
                for (int i = 0; i < outputItems.size(); i++) {


                    if (outputItems.get(i).getItemStatus() == 0 || String.valueOf(outputItems.get(i).getItemStatus()) == null) {
                        Utilities.showAlertDialog(context,"Alert","Please select all the items.",false);
                        return;

//                        outputItems.get(i).setItemStatus(1);
                    }

                    type_campReadinessFormArrayList.add(new Type_CampReadinessForm(String.valueOf(outputItems.get(i).getItemId()), String.valueOf(outputItems.get(i).getItemStatus())));

                }
                Log.d("Testing", "onClick: " + type_campReadinessFormArrayList);

                insertdata(type_campReadinessFormArrayList);
                // }
            }
        });


        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flagstutus = false;
                //   campredinessAdapter.prevPage();
                binding.btnBack.setVisibility(View.GONE);
                binding.btnSaveloc.setText("Next");
                binding.btnSaveloc.setEnabled(true);
                //    binding.rvCampview.smoothScrollToPosition(0);
            }
        });

    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Camp Readiness Form");

        ImageButton btn_save_accordian = findViewById(R.id.btn_save_accordian);


        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });

        btn_save_accordian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, TeamCampMappingdetailsActivity.class));
                finish();
            }
        });
    }

    private void insertdata(ArrayList<Type_CampReadinessForm> type_campReadinessFormArrayList) {

        pd.setMessage("Please wait . . . ");
        pd.setCancelable(false);
        pd.show();
        String campredinesslist = new Gson().toJson(type_campReadinessFormArrayList);
        ApiInterface apiInterface = ApiClient.getD2DClient().create(ApiInterface.class);
        apiInterface.SentInsertCampredinessdetails(CampId, campTypeId, EmpCode, TeamID, campredinesslist).enqueue(new Callback<InsertCampResponse>() {
            @Override
            public void onResponse(Call<InsertCampResponse> call, Response<InsertCampResponse> response) {
                pd.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase("success")) {
                        Utilities.showToastMessage(response.body().getMessage(), context, true);
                        getCampdetails(flagstutus);
//                        Intent intent = new Intent(context, SiteSurvey_Menu_Activity.class);
//                        startActivity(intent);
                    } else {
                        Utilities.showToastMessage(response.body().getMessage(), context, false);
                    }
                } else {
                    // Utilities.showToastMessage(response.body().getMessage(), context, false);
                }

            }

            @Override
            public void onFailure(Call<InsertCampResponse> call, Throwable t) {
                t.getMessage();
                pd.dismiss();
            }
        });
    }

    @Override
    public void onclickphone(OutputItem outputItems) {

        for (int i = 0; i < itemArrayList.size(); i++) {
            if (itemArrayList.get(i).getItemId() == outputItems.getItemId()) {
                itemArrayList.get(i).setItemStatus(outputItems.getItemStatus());
                break;
            }
        }
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

            res = WebServiceCall.APICall(ApplicationConstants.GetCampList_CampReadiness, ApplicationConstants.webservice_d2d, param);
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
                    List<CampListModel.OutputBean> campList = new ArrayList<>();
                    //  Log.d("result ",result.toString());

                    CampListModel campListModel = new Gson().fromJson(result, CampListModel.class);
                    type = campListModel.getStatus();
                    message = campListModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        campList = campListModel.getOutput();
                        if (campList.size() > 0) {
                            // districtList.add(0, new DistrictList_Model("0", "All"));
                            showLabListDialog(campList);
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

    private void showCampListDialog(final List<CampListModel.OutputBean> campList) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
        builderSingle.setTitle("Select Camp");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);


        for (int i = 0; i < campList.size(); i++) {
            arrayAdapter.add(String.valueOf(campList.get(i).getCampId() + "(" + campList.get(i).getCampCreatedBy() + ")"));

            if (campList.get(i).getMapStatus().equalsIgnoreCase("1")){

            }


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
                binding.tvCampList.setText(campList.get(which).getCampId());
                CampId = campList.get(which).getCampId();
                CampDate = campList.get(which).getCampDate();
               /* new TeamCampMappingActivity.GetPhleboDetailsList().execute(CampId, CampDate, "35");
                new TeamCampMappingActivity.GetFlexiPhleboDetailsList().execute(CampId, CampDate, "146");
                new TeamCampMappingActivity.GetDeopDetailsList().execute(CampId, CampDate, "64");
                new TeamCampMappingActivity.GetDoctorDetailsList().execute(CampId, CampDate, "34");
                new TeamCampMappingActivity.GetTeamDetailsList().execute(CampId, CampDate, "0");
                new TeamCampMappingActivity.GetFlexiDoctorDetailsList().execute(CampId, CampDate, "147");*/
                //  refreshCalendar();

                if (DESGID.equalsIgnoreCase("35") || DESGID.equalsIgnoreCase("129") || DESGID.equalsIgnoreCase("86")
                        || DESGID.equalsIgnoreCase("146")||DESGID.equalsIgnoreCase("138")
                        ||DESGID.equalsIgnoreCase("137")||DESGID.equalsIgnoreCase("169")
                        ||DESGID.equalsIgnoreCase("177")||DESGID.equalsIgnoreCase("31")||DESGID.equalsIgnoreCase("176")) {
                    if (Utilities.isNetworkAvailable(context)) {
                        new GetTeamId().execute(CampId, EmpCode);
                    } else {
                        Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                    }
                } else {
                    getCampdetails(flagstutus);
                }

            }
        });
        builderSingle.show();

    }


    private void showLabListDialog(final List<CampListModel.OutputBean> campList) {

        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
        builderSingle.setTitle("Select campId");
        builderSingle.setCancelable(false);
        View dialogueView = getLayoutInflater().inflate(R.layout.recyclerview_dialogue, null);
        builderSingle.setView(dialogueView);


        RecyclerView rvList = dialogueView.findViewById(R.id.rvList);
        EditText edt_search = dialogueView.findViewById(R.id.edt_search);

        edt_search.setVisibility(View.GONE);

        rvList.setHasFixedSize(true);
        rvList.setLayoutManager(new LinearLayoutManager(context));

        CampListAdpterForReadinessAdapter campListAdpterForReadinessAdapter = new CampListAdpterForReadinessAdapter(context,campList);
        rvList.setAdapter(campListAdpterForReadinessAdapter);

        List<CampListModel.OutputBean> filteredList = new ArrayList<>();

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

                if (campList != null) {
                    if (edt_search.getText().toString().equals("")) {
                        filteredList.addAll(campList);
                        rvList.setAdapter(new CampListAdpterForReadinessAdapter(context,filteredList));

                    } else {
                        if (campList.size() > 0) {
                            for (CampListModel.OutputBean pojo : campList) {
                                String siteDetails = pojo.getLabName();
                                if (siteDetails != null && siteDetails.toLowerCase().contains(edt_search.getText().toString().toLowerCase())) {
                                    filteredList.add(pojo);
                                }
                            }

                            if (filteredList.size() == 0) {
                                filteredList.addAll(campList);
                                rvList.setAdapter(new CampListAdpterForReadinessAdapter(context,filteredList));
                            } else {
                                rvList.setAdapter(new CampListAdpterForReadinessAdapter(context,filteredList));

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

                    binding.tvCampList.setText(filteredList.get(position).getCampId());
                    CampId = filteredList.get(position).getCampId();
                    CampDate = filteredList.get(position).getCampDate();

//                    selectedLabID = filteredList.get(position).getLabCode();
//                    selectedLabName = filteredList.get(position).getLabName();
//                    tvLab.setText(selectedLabName);
                } else {
                    binding.tvCampList.setText(campList.get(position).getCampId());
                    CampId = campList.get(position).getCampId();
                    CampDate = campList.get(position).getCampDate();

//                    selectedLabID = campList.get(position).getLabCode();
//                    selectedLabName = campList.get(position).getLabName();
//                    tvLab.setText(selectedLabName);
                }


                if (DESGID.equalsIgnoreCase("35") || DESGID.equalsIgnoreCase("129") || DESGID.equalsIgnoreCase("86")
                        || DESGID.equalsIgnoreCase("146")||DESGID.equalsIgnoreCase("138")||DESGID.equalsIgnoreCase("137")
                        ||DESGID.equalsIgnoreCase("169")||DESGID.equalsIgnoreCase("177")
                        ||DESGID.equalsIgnoreCase("31")||DESGID.equalsIgnoreCase("176")
                ) {
                    if (Utilities.isNetworkAvailable(context)) {
                        new GetTeamId().execute(CampId, EmpCode);
                    } else {
                        Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                    }
                } else {
                    getCampdetails(flagstutus);
                }
                alertDialog.dismiss();

            }
        }));

        alertDialog.show();

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
                binding.tvCampType.setText(campTypeModelsList.get(which).getCampTypeName());
                campTypeId = String.valueOf(campTypeModelsList.get(which).getCampTypeId());

                binding.tvCampList.setText("");
                CampId = "";
                //  refreshCalendar();
            }
        });

        builderSingle.show();
    }

    public class GetFlexiCampType extends AsyncTask<String, Void, String> {

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
            res = WebServiceCall.APICall(ApplicationConstants.CampTypeFlexi, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {

                    NewCampTypeModel newCampTypeModel = new Gson().fromJson(result, NewCampTypeModel.class);
                    type = newCampTypeModel.getStatus();
                    message = newCampTypeModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        List<NewCampTypeModel.Output> camptypelist = newCampTypeModel.getOutput();
                        if (camptypelist.size() > 0) {
                            showCampTypeDialog(camptypelist);
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

        private void showCampTypeDialog(final List<NewCampTypeModel.Output> camptypelist) {
            androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
            builderSingle.setTitle("Select Camp Type");
            builderSingle.setCancelable(false);

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

            for (int i = 0; i < camptypelist.size(); i++) {
                arrayAdapter.add(String.valueOf(camptypelist.get(i).getCampTypeDescription()));
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
                    binding.tvCampType.setText(camptypelist.get(which).getCampTypeDescription());
                    campTypeId = String.valueOf(camptypelist.get(which).getCamptype());
                }
            });

            builderSingle.show();
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
//                    districtList = new ArrayList<>();
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

//                                designId = output.getDISTLGDCODE();
//                                tv.setText(output.getDISTNAME());


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
                binding.tvDistrict.setText(districtList.get(which).getDISTNAME());
                DISTLGDCODE = districtList.get(which).getDISTLGDCODE();


                campredinessAdapter = new CampredinessAdapter(new ArrayList<>(), CampredinessNewActivity.this::onclickphone);
                binding.rvCampview.setAdapter(campredinessAdapter);

                binding.tvCampList.setText("");
                binding.tvNotice.setVisibility(View.GONE);

                binding.linTitle.setVisibility(View.GONE);

                binding.linBtnstatus.setVisibility(View.GONE);

                binding.linTeamId.setVisibility(View.GONE);



//                if (Utilities.isNetworkAvailable(context)) {
////                    new GetCampDetails().execute(labCode, distLgdCode, userId);
//
//
//
//                } else {
//
//                }
            }
        });
        builderSingle.show();
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
            param.add(new ParamsPojo("campid", CampId));
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
                        String teamId = jsonObject1.getString("TeamNumber");
                        String teamName = jsonObject1.getString("TeamName");
                        TeamID = teamId;
                        TeamName = teamName;
                        binding.linTeamId.setVisibility(View.VISIBLE);

                        binding.tvteamList.setText(teamName);
                        getCampdetails(flagstutus);

                    } else {
                        Utilities.showAlertDialog(context, "Error", " Your Selected Camp Not Mapped To You", false);
                        campredinessAdapter = new CampredinessAdapter(new ArrayList<>(), CampredinessNewActivity.this::onclickphone);
                        binding.rvCampview.setAdapter(campredinessAdapter);
                        binding.linBtnstatus.setVisibility(View.GONE);
                        binding.linTitle.setVisibility(View.GONE);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                //  Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
            }
        }
    }

    private void getCampdetails(boolean flagstutus) {
        flagsaved = "0";
        pd.setMessage("Please wait . . . ");
        pd.setCancelable(false);
        pd.show();
        ApiInterface apiInterface = ApiClient.getD2DClient().create(ApiInterface.class);
        apiInterface.GetCampReadinessFormItemsnew(CampId, TeamID).enqueue(new Callback<CampRedinessDataResponse>() {
            @Override
            public void onResponse(Call<CampRedinessDataResponse> call, Response<CampRedinessDataResponse> response) {
                pd.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase("success")) {
                        binding.linBtnstatus.setVisibility(View.VISIBLE);
                        binding.rvCampview.setVisibility(View.VISIBLE);
                        binding.linTitle.setVisibility(View.VISIBLE);
                        String itemstutus = String.valueOf(response.body().getOutput().get(0).getItemStatus());
                        if (!itemstutus.equals("0")) {
                            flagsaved = "1";
                            binding.tvNotice.setVisibility(View.VISIBLE);
                            binding.btnSaveloc.setVisibility(View.GONE);
                            //    Utilities.showToastMessage("Camp Readiness Form Already saved", context, true);
                        } else {
                            binding.tvNotice.setVisibility(View.GONE);
                            binding.btnSaveloc.setVisibility(View.VISIBLE);
                        }
                        itemArrayList = (ArrayList<OutputItem>) response.body().getOutput();
                        setadapter(itemArrayList);
                        ArrayList<OutputItem> itemArrayfiastList = new ArrayList<>();
                        ArrayList<OutputItem> itemArraysecondtList = new ArrayList<>();
                       /* if (flagstutus) {
                            for (int i = 15; i < itemArrayList.size(); i++) {
                                itemArraysecondtList.add(new OutputItem(itemArrayList.get(i).getItemStatus(), itemArrayList.get(i).getIsActive(), itemArrayList.get(i).getItemType(), itemArrayList.get(i).getItemName(), itemArrayList.get(i).getItemId()));
                            }
                            setadapter(itemArraysecondtList);
                            binding.btnSaveloc.setText("Submit");
                        } else {
                            for (int i = 0; i < 15 && i < itemArrayList.size(); i++) {
                                itemArrayfiastList.add(new OutputItem(itemArrayList.get(i).getItemStatus(), itemArrayList.get(i).getIsActive(), itemArrayList.get(i).getItemType(), itemArrayList.get(i).getItemName(), itemArrayList.get(i).getItemId()));
                            }
                            setadapter(itemArrayfiastList);
                            binding.btnBack.setVisibility(View.GONE);
                            binding.btnSaveloc.setText("Next");
                        }*/

                    }
                }
            }

            @Override
            public void onFailure(Call<CampRedinessDataResponse> call, Throwable t) {
                t.getMessage();
                pd.dismiss();
            }
        });
    }

    private void setadapter(ArrayList<OutputItem> itemArrayfilterList) {
        binding.rvCampview.setLayoutManager(new LinearLayoutManager(CampredinessNewActivity.this));
        campredinessAdapter = new CampredinessAdapter(itemArrayList, CampredinessNewActivity.this::onclickphone);
        binding.rvCampview.setAdapter(campredinessAdapter);
        campredinessAdapter.notifyDataSetChanged();

    }



    public class GetCampTypeMMU extends AsyncTask<String, Void, String> {

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
            res = WebServiceCall.APICall(ApplicationConstants.CampTypeMMU, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {

                    NewCampTypeModel newCampTypeModel = new Gson().fromJson(result, NewCampTypeModel.class);
                    type = newCampTypeModel.getStatus();
                    message = newCampTypeModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        List<NewCampTypeModel.Output> camptypelist = newCampTypeModel.getOutput();
                        if (camptypelist.size() > 0) {
                            showCampTypeDialog(camptypelist);
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

        private void showCampTypeDialog(final List<NewCampTypeModel.Output> camptypelist) {
            androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
            builderSingle.setTitle("Select Camp Type");
            builderSingle.setCancelable(false);

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

            for (int i = 0; i < camptypelist.size(); i++) {
                arrayAdapter.add(String.valueOf(camptypelist.get(i).getCampTypeDescription()));
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
                    binding.tvCampType.setText(camptypelist.get(which).getCampTypeDescription());
                    campTypeId = String.valueOf(camptypelist.get(which).getCamptype());
                    //  refreshCalendar();
                }
            });
            builderSingle.show();

        }

    }

}