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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.pojos.SiteSurveyRequestList_OutPut_Pojo;
import com.myhindlab.abkat.pojos.SiteSurveyRequestList_Pojo;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.Communicator;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CampRequestList_Actvity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private static ProgressDialog pd;
    private UserSessionManager sessionManager;

    private EditText edt_distrcit, edt_fromdate, edt_todate;
    private static String distlgdcode = "0";
    private int mYear, mMonth, mDay, mYear1, mMonth1, mDay1;
    private RecyclerView rv_camprquestlist;
    private static ArrayList<SiteSurveyRequestList_OutPut_Pojo> sitesList;
    private Button btn_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camps_request_list);

        init();
        setUpToolBar();
        setDefaults();
        setEventHandler();
        setCommunicatorListner();
    }

    private void setCommunicatorListner() {
        Communicator.getInstance().setListener(new Communicator.OnCustomStateListener() {
            @Override
            public void stateChanged() {
                new GetSiteSurveyRequestList().execute();
            }
        });
    }

    private void init() {
        context = CampRequestList_Actvity.this;
        sessionManager = new UserSessionManager(context);
        pd = new ProgressDialog(context);
        edt_distrcit = findViewById(R.id.edt_distrcit);
        edt_fromdate = findViewById(R.id.edt_fromdate);
        edt_todate = findViewById(R.id.edt_todate);
        btn_submit = findViewById(R.id.btn_submit);

        rv_camprquestlist = findViewById(R.id.rv_camprquestlist);
        rv_camprquestlist.setLayoutManager(new LinearLayoutManager(context));

    }

    private void setDefaults() {
        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH) - 1;
        mDay = c.get(Calendar.DAY_OF_MONTH);

        mYear1 = c.get(Calendar.YEAR);
        mMonth1 = c.get(Calendar.MONTH);
        mDay1 = c.get(Calendar.DAY_OF_MONTH);

        edt_fromdate.setText(Utilities.ConvertDateFormat(Utilities.dfDate4, mDay, mMonth + 1, mYear));
        edt_todate.setText(Utilities.ConvertDateFormat(Utilities.dfDate4, mDay1, mMonth1 + 1, mYear1));

        try {
            JSONArray user_info = new JSONArray(sessionManager.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                String distrcit = json.getString("district");
                distlgdcode = json.getString("DISTLGDCODE");
                edt_distrcit.setText(distrcit);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        new GetSiteSurveyRequestList().execute();
    }

    private void setEventHandler() {
        edt_fromdate.setOnClickListener(this);
        edt_todate.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                ArrayList<SiteSurveyRequestList_OutPut_Pojo> selectedSites = new ArrayList<>();

                for (SiteSurveyRequestList_OutPut_Pojo siteDetails : sitesList) {
                    if (siteDetails.isChecked()) {
                        selectedSites.add(siteDetails);
                    }
                }

                if (selectedSites.size() != 0) {
                    Intent i = new Intent(context, CampApprovePreRequest_Activity.class);
                    i.putExtra("sitesList", selectedSites);
                    startActivity(i);
                } else {
                    Utilities.showToastMessage("Please Select At Least One Camp", context, false);
                }
                break;

            case R.id.edt_fromdate:
                DatePickerDialog dpd1 = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                edt_fromdate.setText(Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year));

                                mYear = year;
                                mMonth = monthOfYear;
                                mDay = dayOfMonth;

                                Calendar c = Calendar.getInstance();
                                mYear1 = c.get(Calendar.YEAR);
                                mMonth1 = c.get(Calendar.MONTH);
                                mDay1 = c.get(Calendar.DAY_OF_MONTH);

                                edt_todate.setText(Utilities.ConvertDateFormat(Utilities.dfDate4, mDay1, mMonth1 + 1, mYear1));

                                new GetSiteSurveyRequestList().execute();
                            }
                        }, mYear, mMonth, mDay);
                try {
                    dpd1.getDatePicker().setCalendarViewShown(false);
//                    dpd1.getDatePicker().setMaxDate(System.currentTimeMillis() + (1000 * 60 * 60));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dpd1.show();
                break;

            case R.id.edt_todate:
                if (edt_fromdate.getText().toString().equals("")) {
                    Utilities.showToastMessage("Please Select From Date", context, false);
                    return;
                }

                DatePickerDialog dpd = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        edt_todate.setText(Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year));

                        mYear1 = year;
                        mMonth1 = monthOfYear;
                        mDay1 = dayOfMonth;
                        new GetSiteSurveyRequestList().execute();
                    }
                }, mYear1, mMonth1, mDay1);
                Calendar c = Calendar.getInstance();
                c.set(mYear, mMonth, mDay);
                try {
                    dpd.getDatePicker().setCalendarViewShown(false);
                    dpd.getDatePicker().setMinDate(c.getTimeInMillis());
//                        dpd.getDatePicker().setMaxDate(System.currentTimeMillis() + (1000 * 60 * 60));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dpd.show();

                break;
        }
    }

    private class GetSiteSurveyRequestList extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("Districtcode", distlgdcode));
            param.add(new ParamsPojo("fromdate", edt_fromdate.getText().toString().trim()));
            param.add(new ParamsPojo("ToDate", edt_todate.getText().toString().trim()));
            param.add(new ParamsPojo("Level", "2"));
            res = WebServiceCall.APICall(ApplicationConstants.GetSiteSurveyRequest, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    sitesList = new ArrayList<>();
                    SiteSurveyRequestList_Pojo pojoDetails = new Gson().fromJson(result, SiteSurveyRequestList_Pojo.class);
                    type = pojoDetails.getStatus();
                    if (type.equalsIgnoreCase("success")) {
                        sitesList = pojoDetails.getOutput();
                        if (sitesList.size() > 0) {
                            rv_camprquestlist.setAdapter(new CampRequestListAdapter());
                        }
                    } else {
                        Utilities.showAlertDialog(context, type, "Site Survey details not available.", false);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
            }
        }

        //**************************** Adapter CAMP Request List Adapter **********//

        private class CampRequestListAdapter extends RecyclerView.Adapter<CampRequestListAdapter.MyViewHolder> {

            @Override
            public CampRequestListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.list_row_c_list, parent, false);
                CampRequestListAdapter.MyViewHolder myViewHolder = new CampRequestListAdapter.MyViewHolder(view);
                return myViewHolder;
            }

            @Override
            public void onBindViewHolder(final MyViewHolder holder, int position) {
                SiteSurveyRequestList_OutPut_Pojo desigDetail = sitesList.get(position);

                holder.tv_survet_date.setText(desigDetail.getSITESURVEYDATE());
                holder.tv_status.setText(desigDetail.getFinalStatus());
                holder.tv_sitelocation.setText(desigDetail.getSITELOCATION());
                holder.tv_sitename.setText(desigDetail.getSiteName());
                holder.tv_sitetype.setText(desigDetail.getSiteTypeName());
                holder.tv_regdworker.setText(desigDetail.getREGISTERWORKERS());
                holder.tv_unregdworker.setText(desigDetail.getUNREGISTEREDWORKERS());
                holder.tv_pin_code.setText(desigDetail.getPinCode());
                holder.tv_surveyor_name.setText(desigDetail.getSURVEYORNAME());

                holder.cb_check.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (holder.cb_check.isChecked()) {
                            sitesList.get(position).setChecked(true);
                        } else {
                            sitesList.get(position).setChecked(false);
                        }
                    }
                });
            }

            @Override
            public int getItemCount() {
                return sitesList.size();
            }

            public class MyViewHolder extends RecyclerView.ViewHolder {

                private TextView tv_status, tv_surveyor_name, tv_sitelocation, tv_sitename, tv_sitetype, tv_regdworker, tv_unregdworker, tv_pin_code,
                        tv_survet_date;
                private CheckBox cb_check;

                private MyViewHolder(View view) {
                    super(view);
                    tv_sitelocation = view.findViewById(R.id.tv_sitelocation);
                    tv_status = view.findViewById(R.id.tv_status);
                    tv_sitename = view.findViewById(R.id.tv_sitename);
                    tv_sitetype = view.findViewById(R.id.tv_sitetype);
                    tv_regdworker = view.findViewById(R.id.tv_regdworker);
                    tv_unregdworker = view.findViewById(R.id.tv_unregdworker);
                    tv_pin_code = view.findViewById(R.id.tv_pin_code);
                    tv_survet_date = view.findViewById(R.id.tv_survet_date);
                    tv_surveyor_name = view.findViewById(R.id.tv_surveyor_name);
                    cb_check = view.findViewById(R.id.cb_check);
                }
            }

            @Override
            public int getItemViewType(int position) {
                return position;
            }

        }

    }

    private void setUpToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Camp Request List");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Communicator.getInstance().setListener(null);
    }
}
