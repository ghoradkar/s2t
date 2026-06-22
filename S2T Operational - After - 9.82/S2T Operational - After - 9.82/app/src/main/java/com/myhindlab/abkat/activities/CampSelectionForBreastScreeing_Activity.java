package com.myhindlab.abkat.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.adapters.CampListAdapter;
import com.myhindlab.abkat.models.BreastScreeningUploadedDeviceModel;
import com.myhindlab.abkat.models.DistrictList_Model;
import com.myhindlab.abkat.models.GetApprovedCampListDetailsForAppList;
import com.myhindlab.abkat.pojos.DistrictList_Pojo;
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

public class CampSelectionForBreastScreeing_Activity extends AppCompatActivity {
    private Context context;
    private ProgressDialog pd;
    private UserSessionManager session;

    private MaterialEditText edt_campdate, edt_selectdistrict;
    private LinearLayout ll_campdetailslist;
    private SearchView searchview_campname;
    private RecyclerView recyclerview_campList;

    private ArrayList<GetApprovedCampListDetailsForAppList> campList;
    private ArrayList<GetApprovedCampListDetailsForAppList> searchCampList;
    private GetApprovedCampListDetailsForAppList selectedCamp;
    private String userID = "", DISTLGDCODE = "0";
    private int mYear, mMonth, mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campselection_forbreastscreeing);

        init();
        setUpToolbar();
        setDefaults();
        setEventHandler();
    }

    private void init() {
        edt_campdate = findViewById(R.id.edt_campdate);
        edt_selectdistrict = findViewById(R.id.edt_selectdistrict);
        ll_campdetailslist = findViewById(R.id.ll_campdetailslist);
        searchview_campname = findViewById(R.id.searchview_campname);
        recyclerview_campList = findViewById(R.id.recyclerview_campList);
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Select Camp");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setDefaults() {
        context = CampSelectionForBreastScreeing_Activity.this;
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
        edt_campdate.setOnClickListener(v -> {
            DatePickerDialog dpd1 = new DatePickerDialog(context,
                    (view, year, monthOfYear, dayOfMonth) -> {
                        edt_campdate.setText(Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year));

                        if (Utilities.isNetworkAvailable(context)) {
                            new GetApprovedCampListDetailsForAppAPICall().execute(edt_campdate.getText().toString().trim(), userID, DISTLGDCODE);
                        } else {
                            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                        }
                    }, mYear, mMonth, mDay);
            try {
                dpd1.getDatePicker().setCalendarViewShown(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
            dpd1.show();
        });

        edt_selectdistrict.setOnClickListener(v -> {
            if (Utilities.isNetworkAvailable(context)) {
                new GetDistrictList().execute();
            } else {
                Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
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
                new RecyclerItemClickListener(context, (view, position) -> {
                    GetApprovedCampListDetailsForAppList campDetails = campList.get(position);
                    selectedCamp = campDetails;

                    if (selectedCamp.getFLAG().equals("1")) {
                        Utilities.showAlertDialog(context, "Alert", "IBE screening is closed for this camp", true);
                    } else {

                        if (Utilities.isNetworkAvailable(context)) {
                            new CheckCampClosedforBreastScreening().execute(userID, edt_campdate.getText().toString().trim());
                        } else {
                            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                        }
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

            res = WebServiceCall.APICall(ApplicationConstants.GetApprovedCampListDetailsForApp, ApplicationConstants.webservice, param);
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

            res = WebServiceCall.APICall(ApplicationConstants.GetDistrictList, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    ArrayList<DistrictList_Model> districtList = new ArrayList<>();
                    DistrictList_Pojo pojoDetails = new Gson().fromJson(result, DistrictList_Pojo.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        districtList = pojoDetails.getOutput();
                        if (districtList.size() > 0) {
                            districtList.add(0, new DistrictList_Model("0", "All"));

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

        builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
            edt_selectdistrict.setText(districtList.get(which).getDISTNAME());
            DISTLGDCODE = districtList.get(which).getDISTLGDCODE();

            if (Utilities.isNetworkAvailable(context)) {
                new GetApprovedCampListDetailsForAppAPICall().execute(edt_campdate.getText().toString().trim(), userID, DISTLGDCODE);
            } else {
                Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
            }
        });
        builderSingle.show();
    }

    private class CheckCampClosedforBreastScreening extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("CampDATE", params[1]));
            res = WebServiceCall.APICall(ApplicationConstants.CheckCampClosedforBreastScreening, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "";
            try {
                if (!result.equals("")) {

                    GetApprovedCampListDetailsForApp_Pojo pojoDetails = new Gson().fromJson(result, GetApprovedCampListDetailsForApp_Pojo.class);
                    type = pojoDetails.getStatus();
                    if (type.equalsIgnoreCase("success")) {
                        List<GetApprovedCampListDetailsForAppList> campList = pojoDetails.getOutput();
                        if (campList.size() > 0) {
                            showCampClosingPendingCamps(campList);
                        } else {
                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                        }
                    } else {
                        if (Utilities.isNetworkAvailable(context)) {
                            new GetDeviceImageDetails().execute(userID, selectedCamp.getCampId(), "1");
                        } else {
                            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
            }
        }
    }

    android.app.AlertDialog alertD;

    private void showCampClosingPendingCamps(List<GetApprovedCampListDetailsForAppList> campList) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View promptView = layoutInflater.inflate(R.layout.prompt_camplist, null);
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Camp List");
        alertDialogBuilder.setView(promptView);

        RecyclerView rv_camplist = promptView.findViewById(R.id.rv_camplist);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        rv_camplist.setLayoutManager(layoutManager);
        rv_camplist.setAdapter(new PendingCampScreeningClosingListAdapter(context, campList));

        alertDialogBuilder.setNegativeButton("Cancel", (dialog, view) -> dialog.cancel());
        alertDialogBuilder.setCancelable(false);
        alertD = alertDialogBuilder.create();

        alertD.show();
    }

    private class GetDeviceImageDetails extends AsyncTask<String, Void, String> {

        String campId = "", callType = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Please wait . . . ");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            campId = params[1];
            callType = params[2];
            String res = "";
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("UserId", params[0]));
            param.add(new ParamsPojo("CampId", params[1]));
            res = WebServiceCall.APICall(ApplicationConstants.GetDeviceImageDetails, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {

                    BreastScreeningUploadedDeviceModel pojoDetails = new Gson().fromJson(result, BreastScreeningUploadedDeviceModel.class);
                    type = pojoDetails.getStatus();
                    if (type.equalsIgnoreCase("success")) {
                        List<BreastScreeningUploadedDeviceModel.OutputBean> deviceList = pojoDetails.getOutput();
                        if (deviceList.size() > 0) {
                            if (callType.equals("1")) {
                                startActivity(new Intent(context, AttendanceMarkedPatients_Activity.class)
                                        .putExtra("healthScreentype", "13")
                                        .putExtra("campId", campId)
                                        .putExtra("breastScreeningDeviceId", String.valueOf(deviceList.get(0).getDeviceId()))
                                        .putExtra("breastScreeningDeviceName", deviceList.get(0).getDeviceName())
                                        .putExtra("selectedCamp", selectedCamp));
                            } else {
                                startActivity(new Intent(context, CloseBreastScreeningCamp.class)
                                        .putExtra("selectedCamp", selectedCamp)
                                        .putExtra("breastScreeningDeviceId", String.valueOf(deviceList.get(0).getDeviceId()))
                                        .putExtra("breastScreeningDeviceName", deviceList.get(0).getDeviceName()));
                            }
                        } else {
                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                        }
                    } else {
                        startActivity(new Intent(context, BreastScreeningDevicePhoto_Activity.class)
                                .putExtra("campId", campId));


                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
            }
        }
    }

    private class PendingCampScreeningClosingListAdapter
            extends RecyclerView.Adapter<PendingCampScreeningClosingListAdapter.MyViewHolder> {

        private Context context;
        private List<GetApprovedCampListDetailsForAppList> campList;

        public PendingCampScreeningClosingListAdapter(Context context, List<GetApprovedCampListDetailsForAppList> campList) {
            this.context = context;
            this.campList = campList;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.list_row_pending_camp_screening_closing, viewGroup, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
            int position = holder.getAdapterPosition();
            GetApprovedCampListDetailsForAppList campDetails = campList.get(position);

            holder.tv_date.setText(campDetails.getCampDate());
            holder.tv_name.setText(campDetails.getCampLocation());

            holder.btn_close.setOnClickListener(v -> {
                selectedCamp = campDetails;
                if (Utilities.isNetworkAvailable(context)) {
                    new GetDeviceImageDetails().execute(userID, campDetails.getCampId(), "2");
                } else {
                    Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                }
                alertD.dismiss();
            });
        }

        @Override
        public int getItemCount() {
            return campList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            private TextView tv_date, tv_name;
            private Button btn_close;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                tv_date = itemView.findViewById(R.id.tv_date);
                tv_name = itemView.findViewById(R.id.tv_name);
                btn_close = itemView.findViewById(R.id.btn_close);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        searchview_campname.clearFocus();

        if (Utilities.isNetworkAvailable(context)) {
            new GetApprovedCampListDetailsForAppAPICall().execute(edt_campdate.getText().toString().trim(), userID, DISTLGDCODE);
        } else {
            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
        }
    }
}
