package com.myhindlab.abkat.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.SearchView;

import com.google.gson.Gson;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.adapters.CampListAdapter;
import com.myhindlab.abkat.models.GetApprovedCampListDetailsForAppList;
import com.myhindlab.abkat.pojos.GetApprovedCampListDetailsForApp_Pojo;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.RecyclerItemClickListener;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CampAwarenessCampList_Activity extends AppCompatActivity {

    private Context context;
    private ProgressDialog pd;
    private UserSessionManager session;

    private MaterialEditText edt_campdate;
    private LinearLayout ll_campdetailslist;
    private SearchView searchview_campname;
    private RecyclerView recyclerview_campList;

    private ArrayList<GetApprovedCampListDetailsForAppList> campList;
    private ArrayList<GetApprovedCampListDetailsForAppList> searchCampList;
    private String userID = "";
    private int mYear, mMonth, mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campawarenesslist);

        init();
        setUpToolbar();
        setDefaults();
        setEventHandler();
    }

    private void init() {
        edt_campdate = findViewById(R.id.edt_campdate);
        ll_campdetailslist = findViewById(R.id.ll_campdetailslist);
        searchview_campname = findViewById(R.id.searchview_campname);
        recyclerview_campList = findViewById(R.id.recyclerview_campList);
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Awareness Camp List");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setDefaults() {
        context = CampAwarenessCampList_Activity.this;
        session = new UserSessionManager(context);
        pd = new ProgressDialog(context);

        Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);

        edt_campdate.setText(Utilities.ConvertDateFormat(Utilities.dfDate4, mDay, mMonth + 1, mYear));

        ll_campdetailslist.setVisibility(View.GONE);

        try {
            JSONArray user_info = new JSONArray(session.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                userID = json.getString("EmpCode");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setEventHandler() {
        edt_campdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd1 = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                edt_campdate.setText(Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year));

                                if (Utilities.isNetworkAvailable(context)) {
                                    new GetApprovedCampListDetailsForAppAPICall().execute(edt_campdate.getText().toString().trim(), userID);
                                } else {
                                    Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                                }
                            }
                        }, mYear, mMonth, mDay);
                dpd1.getDatePicker().setCalendarViewShown(false);
                dpd1.show();
            }
        });

        searchview_campname.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchCampList = new ArrayList<>();

                if (campList != null) {
                    if (campList.size() > 0) {
                        for (GetApprovedCampListDetailsForAppList pojo : campList) {
                            String siteDetails = pojo.getCampId();
                            if (siteDetails != null && siteDetails.toLowerCase().contains(query.toLowerCase())) {
                                searchCampList.add(pojo);
                            }
                        }

                        if (searchCampList.size() == 0) {
                            Utilities.showAlertDialog(context, "Alert", "No record found.", false);
                            searchCampList.addAll(campList);
                            showSiteListDialog(searchCampList);
                        } else {
                            showSiteListDialog(searchCampList);
                        }
                    }
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchCampList = new ArrayList<>();

                if (campList != null) {
                    if (newText.equals("")) {
                        searchCampList.addAll(campList);
                        showSiteListDialog(searchCampList);
                    } else {
                        if (campList.size() > 0) {
                            for (GetApprovedCampListDetailsForAppList pojo : campList) {
                                String siteDetails = pojo.getCampId();
                                if (siteDetails != null && siteDetails.toLowerCase().contains(newText.toLowerCase())) {
                                    searchCampList.add(pojo);
                                }
                            }

                            if (searchCampList.size() == 0) {
                                searchCampList.addAll(campList);
                                showSiteListDialog(searchCampList);
                            } else {
                                showSiteListDialog(searchCampList);
                            }
                        }
                    }
                }
                return true;
            }
        });

        recyclerview_campList.addOnItemTouchListener(
                new RecyclerItemClickListener(context,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, final int position) {
                                GetApprovedCampListDetailsForAppList campDetails = searchCampList.get(position);

                                startActivity(new Intent(context, CampAwarenessImageCheckList_Activity.class)
                                        .putExtra("campDetails", campDetails));
                            }
                        }));
    }

    private class GetApprovedCampListDetailsForAppAPICall extends AsyncTask<String, Void, String> {

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

            res = WebServiceCall.APICall(ApplicationConstants.GetApprovedCampListforAwareNess, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            try {
                if (!result.equals("")) {

                    GetApprovedCampListDetailsForApp_Pojo pojoDetails = new Gson().fromJson(result, GetApprovedCampListDetailsForApp_Pojo.class);
                    String status = pojoDetails.getStatus();
                    String message = pojoDetails.getMessage();

                    if (status.equalsIgnoreCase("success")) {

                        searchview_campname.clearFocus();

                        campList = new ArrayList<>();
                        campList = pojoDetails.getOutput();

                        searchCampList = new ArrayList<>();
                        searchCampList.addAll(campList);

                        if (searchCampList.size() > 0) {
                            showSiteListDialog(searchCampList);
                        } else {
                            ll_campdetailslist.setVisibility(View.GONE);
                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                        }
                    } else {
                        ll_campdetailslist.setVisibility(View.GONE);
                        Utilities.showAlertDialog(context, status, message, false);
                    }
                } else {
                    ll_campdetailslist.setVisibility(View.GONE);
                    Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                }
            } catch (Exception e) {
                e.printStackTrace();
                ll_campdetailslist.setVisibility(View.GONE);
                Utilities.showAlertDialog(context, "Alert", e.getMessage(), false);
            }
        }
    }

    private void showSiteListDialog(ArrayList<GetApprovedCampListDetailsForAppList> camplist) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerview_campList.setLayoutManager(layoutManager);
        ll_campdetailslist.setVisibility(View.VISIBLE);
        recyclerview_campList.setAdapter(new CampListAdapter(camplist));
    }

    @Override
    protected void onResume() {
        super.onResume();
        searchview_campname.clearFocus();

        if (Utilities.isNetworkAvailable(context)) {
            new GetApprovedCampListDetailsForAppAPICall().execute(edt_campdate.getText().toString().trim(), userID);
        } else {
            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
        }
    }
}
