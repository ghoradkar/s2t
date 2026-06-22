package com.myhindlab.abkat.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.MasterModel;
import com.myhindlab.abkat.pojos.GetNearestHospital_OutPut_Pojo;
import com.myhindlab.abkat.pojos.GetNearestHospital_Pojo;
import com.myhindlab.abkat.pojos.Lab_OutPut_Pojo;
import com.myhindlab.abkat.pojos.Lab_Pojo;
import com.myhindlab.abkat.pojos.Remark_Pojo;
import com.myhindlab.abkat.pojos.SiteSurveyRequestList_OutPut_Pojo;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.Communicator;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CampApprovePreRequest_Activity extends AppCompatActivity implements View.OnClickListener {
    private static Context context;
    private static ProgressDialog pd;
    private static MaterialEditText edt_other, edt_lab, edt_date, edt_postdate, edt_name,
            edt_mobieNo, edt_hospital, edt_campName, edt_camp_type, edt_remark;
    private AutoCompleteTextView edt_camp_location;
    private Button btn_save;

    private static String selectedLabID = "", selectedLabName = "", dist_code = "", userID = "", selectedHospitalName = "", selectedHospitalID = "", desg_id = "", selectedCampType = "";
    private UserSessionManager session;
    private int mYear, mMonth, mDay;
    private static ArrayList<Remark_Pojo> objArr1;
    private String jarr1 = "", locationID_List = "";

    private static ArrayList<SiteSurveyRequestList_OutPut_Pojo> sitesList;
    String CampId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camp_approve_pre_request);
        init();
        setUpToolBar();
        setDefaults();
        setEventHandler();

    }

    private void init() {
        context = CampApprovePreRequest_Activity.this;
        session = new UserSessionManager(context);
        pd = new ProgressDialog(context);
        edt_camp_location = findViewById(R.id.edt_camp_location);
        edt_other = findViewById(R.id.edt_other);
        edt_lab = findViewById(R.id.edt_lab);
        edt_date = findViewById(R.id.edt_date);
        edt_postdate = findViewById(R.id.edt_postdate);
        edt_name = findViewById(R.id.edt_name);
        edt_mobieNo = findViewById(R.id.edt_mobieNo);
        edt_hospital = findViewById(R.id.edt_hospital);
        edt_campName = findViewById(R.id.edt_campName);
        edt_camp_type = findViewById(R.id.edt_camp_type);
        edt_remark = findViewById(R.id.edt_remark);
        btn_save = findViewById(R.id.btn_save);

        sitesList = new ArrayList<>();
        objArr1 = new ArrayList<>();
    }

    private void setUpToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Pre Camp Request");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setDefaults() {
        sitesList = (ArrayList<SiteSurveyRequestList_OutPut_Pojo>) getIntent().getSerializableExtra("sitesList");

        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        try {
            JSONArray user_info = new JSONArray(session.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                userID = json.getString("EmpCode");
                dist_code = json.getString("DISTLGDCODE");
                desg_id = json.getString("DESGID");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            ArrayAdapter<SiteSurveyRequestList_OutPut_Pojo> adapter = new ArrayAdapter<>(context, R.layout.list_row, sitesList);
            edt_camp_location.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setEventHandler() {
        edt_camp_location.setOnClickListener(this);
        edt_lab.setOnClickListener(this);
        edt_date.setOnClickListener(this);
        edt_hospital.setOnClickListener(this);
        edt_camp_type.setOnClickListener(this);
        btn_save.setOnClickListener(this);

        edt_camp_location.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                SiteSurveyRequestList_OutPut_Pojo siteObj = (SiteSurveyRequestList_OutPut_Pojo) arg0.getAdapter().getItem(arg2);
                edt_campName.setText(siteObj.getSiteName());
            }
        });

    }

    private void CreateJSONRemark() {
        Remark_Pojo obj = new Remark_Pojo();
        obj.setRemark(edt_remark.getText().toString().trim());
        obj.setDORemark("");
        obj.setNoRemark(edt_remark.getText().toString().trim());
        obj.setCheckListId("1");
        objArr1.add(obj);
        Gson gson = new Gson();
        jarr1 = gson.toJson(objArr1);
        new CreateCamp().execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save: {
                if (Utilities.isNetworkAvailable(context)) {
                    if (Utilities.isEmpty(edt_camp_location)) {
                        Utilities.showToastMessage("Please Provide Camp Location", context, false);
                        edt_camp_location.requestFocus();
                        return;
                    }

                    if (Utilities.isEmpty(edt_other)) {
                        Utilities.showToastMessage("Please Provide Other", context, false);
                        edt_other.requestFocus();
                        return;
                    }

                    if (Utilities.isEmpty(edt_lab)) {
                        Utilities.showToastMessage("Please Select Lab", context, false);
                        edt_lab.requestFocus();
                        return;
                    }

                    if (Utilities.isEmpty(edt_date)) {
                        Utilities.showToastMessage("Please Select Date", context, false);
                        edt_date.requestFocus();
                        return;
                    }

                    if (Utilities.isEmpty(edt_postdate)) {
                        Utilities.showToastMessage("Please Select Post Camp Date", context, false);
                        edt_postdate.requestFocus();
                        return;
                    }

                    if (Utilities.isEmpty(edt_name)) {
                        Utilities.showToastMessage("Please Provide Name of AarogyaMitra", context, false);
                        edt_name.requestFocus();
                        return;
                    }

                    if (Utilities.isEmpty(edt_mobieNo)) {
                        Utilities.showToastMessage("Please Enter Mobile No of AarogyaMitra", context, false);
                        edt_mobieNo.requestFocus();
                        return;
                    }

                    if (!Utilities.isMobileNo(edt_mobieNo.getText().toString().trim())) {
                        Utilities.showToastMessage("Please Enter Valid Mobile No of AarogyaMitra", context, false);
                        edt_mobieNo.requestFocus();
                        return;
                    }

                    if (Utilities.isEmpty(edt_hospital)) {
                        Utilities.showToastMessage("Please Enter Afilliated Hospital Name", context, false);
                        edt_hospital.requestFocus();
                        return;
                    }

                    if (Utilities.isEmpty(edt_campName)) {
                        Utilities.showToastMessage("Please Enter Camp Name", context, false);
                        edt_campName.requestFocus();
                        return;
                    }

                    if (Utilities.isEmpty(edt_camp_type)) {
                        Utilities.showToastMessage("Please Select Camp Type", context, false);
                        return;
                    }

                    if (Utilities.isEmpty(edt_remark)) {
                        Utilities.showToastMessage("Please Enter Remark", context, false);
                        edt_campName.requestFocus();
                        return;
                    }

                    JsonArray jsonArray = new JsonArray();

                    for (SiteSurveyRequestList_OutPut_Pojo siteDetails : sitesList) {
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("SiteDetailId", siteDetails.getSiteDetailId());
                        jsonArray.add(jsonObject);
                    }

                    locationID_List = jsonArray.toString();

                    CreateJSONRemark();

                } else
                    Utilities.showToastMessage(String.valueOf(R.string.msgt_nointernetconnection), context, false);
                break;
            }

            case R.id.edt_date: {
                DatePickerDialog dpd1 = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        edt_date.setText(Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year));

                        mYear = year;
                        mMonth = monthOfYear;
                        mDay = dayOfMonth;

                        Calendar c = Calendar.getInstance();
                        c.set(mYear, mMonth, mDay);
                        c.add(Calendar.DATE, 3);
                        int mYear = c.get(Calendar.YEAR);
                        int mMonth = c.get(Calendar.MONTH);
                        int mDay = c.get(Calendar.DAY_OF_MONTH);

                        edt_postdate.setText(Utilities.ConvertDateFormat(Utilities.dfDate4, mDay, mMonth + 1, mYear));

                    }
                }, mYear, mMonth, mDay);
                try {
                    dpd1.getDatePicker().setCalendarViewShown(false);
                    dpd1.getDatePicker().setMinDate(System.currentTimeMillis());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dpd1.show();
                break;
            }

            case R.id.edt_lab: {
                if (Utilities.isNetworkAvailable(context))
                    new GetLab().execute();
                else {
                    Utilities.showToastMessage("Please Check your internet connection", context, false);
                    return;
                }
                break;
            }

            case R.id.edt_hospital: {
                if (Utilities.isNetworkAvailable(context))
                    new GetNearestHospitalList().execute();
                else {
                    Utilities.showToastMessage("Please Check your internet connection", context, false);
                    return;
                }
                break;
            }

            case R.id.edt_camp_type: {
                ArrayList<MasterModel> campTypeList = new ArrayList<>();
                campTypeList.add(new MasterModel("1", "Regular Camp"));
                campTypeList.add(new MasterModel("2", "Mini Camp"));
                showCampTypeList(campTypeList);
                break;
            }
        }

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public class GetLab extends AsyncTask<String, Void, String> {

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
            res = WebServiceCall.APICall(ApplicationConstants.GetLab, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {

                    Lab_Pojo pojoDetails = new Gson().fromJson(result, Lab_Pojo.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        ArrayList<Lab_OutPut_Pojo> labList = pojoDetails.getOutput();
                        if (labList.size() > 0) {
                            showLabListDialog(labList);
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
    }

    private void showLabListDialog(final ArrayList<Lab_OutPut_Pojo> lab_List) {
        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);

        builderSingle.setTitle("Select Lab");

        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        for (Lab_OutPut_Pojo subTrenchModel : lab_List) {
            arrayAdapter.add(subTrenchModel.getLabName());
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
                selectedLabID = lab_List.get(which).getLabCode();
                selectedLabName = lab_List.get(which).getLabName();
                edt_lab.setText(selectedLabName);
            }
        });
        builderSingle.show();
    }

    public class GetNearestHospitalList extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("DISTLGDCODE", dist_code));
            res = WebServiceCall.APICall(ApplicationConstants.GetNearistHospitalList, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {

                    GetNearestHospital_Pojo pojoDetails = new Gson().fromJson(result, GetNearestHospital_Pojo.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        ArrayList<GetNearestHospital_OutPut_Pojo> hospitalList = pojoDetails.getOutput();

                        if (hospitalList.size() > 0) {
                            showHospitalListDialog(hospitalList);
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
    }

    private void showHospitalListDialog(final ArrayList<GetNearestHospital_OutPut_Pojo> hospital_List) {
        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);

        builderSingle.setTitle("Select Hospital");

        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        for (GetNearestHospital_OutPut_Pojo subTrenchModel : hospital_List) {
            arrayAdapter.add(subTrenchModel.getHospitalName());
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
                selectedHospitalID = hospital_List.get(which).getHospitalId();
                selectedHospitalName = hospital_List.get(which).getHospitalName();
                edt_hospital.setText(selectedHospitalName);
            }
        });
        builderSingle.show();
    }

    private void showCampTypeList(final ArrayList<MasterModel> campTypeList) {
        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        builderSingle.setTitle("Select Camp Type");

        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        for (MasterModel campType : campTypeList) {
            arrayAdapter.add(campType.getName());
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
                selectedCampType = campTypeList.get(which).getId();
                edt_camp_type.setText(campTypeList.get(which).getName());
            }
        });
        builderSingle.show();
    }

    private class CreateCamp extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("Distlgdcode", dist_code));
            param.add(new ParamsPojo("UserId", userID));
            param.add(new ParamsPojo("DesgID", desg_id));
            param.add(new ParamsPojo("Create_CAMP", "1"));
            param.add(new ParamsPojo("CampLocation", edt_camp_location.getText().toString().trim()));
            param.add(new ParamsPojo("CampName", edt_campName.getText().toString().trim()));
            param.add(new ParamsPojo("CampDate", edt_date.getText().toString().trim()));
            param.add(new ParamsPojo("PostCampDate", edt_postdate.getText().toString().trim()));
            param.add(new ParamsPojo("LabCode", selectedLabID));
            param.add(new ParamsPojo("Hospital", selectedHospitalID));
            param.add(new ParamsPojo("ArogyaMitra", edt_name.getText().toString().trim()));
            param.add(new ParamsPojo("ArogyaMitraMOB", edt_mobieNo.getText().toString().trim()));
            param.add(new ParamsPojo("CampType", selectedCampType));
            param.add(new ParamsPojo("jsonstringRemrk", jarr1));
            param.add(new ParamsPojo("jsonstringSite", locationID_List));

            res = WebServiceCall.APICall(ApplicationConstants.CreateCamp, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                pd.dismiss();

                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
                    JSONObject obj = new JSONObject(result);
                    String status = obj.getString("status");
                    String message = obj.getString("message");
                    if (status.equalsIgnoreCase("Success")) {
                        CampId = obj.getString("CampId");
                        new ApproveCamp().execute();
                    } else {
                        Utilities.showAlertDialog(context, status, message, false);
                    }
                } else {
                    Utilities.showAlertDialog(context, "Please try again", "Server not responding.", false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class ApproveCamp extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("CampId", CampId));
            param.add(new ParamsPojo("Createdby", userID));

            res = WebServiceCall.APICall(ApplicationConstants.ApproveCamp, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                pd.dismiss();

                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
                    JSONObject obj = new JSONObject(result);
                    String status = obj.getString("status");
                    String message = obj.getString("message");
                    if (status.equalsIgnoreCase("Success")) {
                        Communicator.getInstance().changeState();

                        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                                .setIcon(R.drawable.icon_success)
                                .setTitle("Success")
                                .setMessage("Camp created successfully")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                });
                        AlertDialog dialog = builder.create();
                        dialog.show();

                    } else {
                        Utilities.showAlertDialog(context, status, message, false);
                        showReuploadDialog();
                    }
                } else {
                    showReuploadDialog();
                }
            } catch (Exception e) {
                e.printStackTrace();
                showReuploadDialog();
            }
        }
    }

    private void showReuploadDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Something went wrong. Please click OK to try again");
        builder.setTitle("Success");
        builder.setIcon(R.drawable.icon_alertred);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                new ApproveCamp().execute();
            }
        });
        AlertDialog alertD = builder.create();
        alertD.show();
    }


}
