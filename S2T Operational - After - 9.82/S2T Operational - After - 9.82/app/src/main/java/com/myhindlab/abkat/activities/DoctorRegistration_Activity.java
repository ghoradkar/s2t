package com.myhindlab.abkat.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.google.gson.Gson;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.DistrictList_Model;
import com.myhindlab.abkat.models.DoctorTypeList_Model;
import com.myhindlab.abkat.pojos.DistrictList_Pojo;
import com.myhindlab.abkat.pojos.DoctorTypeList_Pojo;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DoctorRegistration_Activity extends AppCompatActivity {

    private Context context;
    private MaterialEditText edt_district, edt_doctortype, edt_fname, edt_mname, edt_lname, edt_mothername, edt_moblieno, edt_email, edt_hospital, edt_registrationno;
    private Button btn_register;
    private ProgressDialog pd;
    private String DISTLGDCODE, DocTypeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_registration);

        init();
        setUpToolbar();
        setDefaults();
        getSessionData();
        setEventHandler();
    }

    private void init() {
        context = DoctorRegistration_Activity.this;
        pd = new ProgressDialog(context);
        edt_district = findViewById(R.id.edt_district);
        edt_doctortype = findViewById(R.id.edt_doctortype);
        edt_fname = findViewById(R.id.edt_fname);
        edt_mname = findViewById(R.id.edt_mname);
        edt_lname = findViewById(R.id.edt_lname);
        edt_mothername = findViewById(R.id.edt_mothername);
        edt_moblieno = findViewById(R.id.edt_moblieno);
        edt_email = findViewById(R.id.edt_email);
        edt_hospital = findViewById(R.id.edt_hospital);
        edt_registrationno = findViewById(R.id.edt_registrationno);
        btn_register = findViewById(R.id.btn_register);
    }

    private void setDefaults() {

    }

    private void getSessionData() {

    }

    private void setEventHandler() {
        edt_district.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utilities.isNetworkAvailable(context)) {
                    new GetDistrictList().execute();
                } else {
                    Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                }
            }
        });

        edt_doctortype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utilities.isNetworkAvailable(context)) {
                    new GetDoctorType().execute();
                } else {
                    Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                }
            }
        });


        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitData();
            }
        });
    }

    private void submitData() {

        if (edt_district.getText().toString().trim().isEmpty()) {
            edt_district.setError("Please select district");
            return;
        }

        if (edt_fname.getText().toString().trim().isEmpty()) {
            edt_fname.setError("Please enter first name");
            return;
        }

        if (edt_mname.getText().toString().trim().isEmpty()) {
            edt_mname.setError("Please enter middle name");
            return;
        }

        if (edt_lname.getText().toString().trim().isEmpty()) {
            edt_lname.setError("Please enter last name");
            return;
        }

        if (edt_mothername.getText().toString().trim().isEmpty()) {
            edt_mothername.setError("Please enter mother name");
            return;
        }

        if (!Utilities.isMobileNo(edt_moblieno.getText().toString().trim())) {
            edt_moblieno.setError("Please enter valid mobile no.");
            return;
        }

        if (!Utilities.isEmailValid(edt_email.getText().toString().trim())) {
            edt_email.setError("Please enter valid email");
            return;
        }

        if (edt_hospital.getText().toString().trim().isEmpty()) {
            edt_hospital.setError("Please enter clinic / hospital name");
            return;
        }

        if (edt_registrationno.getText().toString().trim().isEmpty()) {
            edt_district.setError("Please enter registration no.");
            return;
        }

        if (Utilities.isNetworkAvailable(context)) {
            new DoctorRegistration().execute(
                    edt_moblieno.getText().toString().trim(),
                    edt_email.getText().toString().trim(),
                    "64",
                    edt_fname.getText().toString().trim(),
                    edt_mname.getText().toString().trim(),
                    edt_lname.getText().toString().trim(),
                    edt_mothername.getText().toString().trim(),
                    "2",
                    "1004",
                    DISTLGDCODE,
                    edt_hospital.getText().toString().trim(),
                    edt_registrationno.getText().toString().trim(),
                    DocTypeId
            );
        } else {
            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
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

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                edt_district.setText(districtList.get(which).getDISTNAME());
                DISTLGDCODE = districtList.get(which).getDISTLGDCODE();
            }
        });
        builderSingle.show();
    }

    public class GetDoctorType extends AsyncTask<String, Void, String> {

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

            res = WebServiceCall.APICall(ApplicationConstants.GetDoctorType, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    ArrayList<DoctorTypeList_Model> doctorTypeList = new ArrayList<>();
                    DoctorTypeList_Pojo pojoDetails = new Gson().fromJson(result, DoctorTypeList_Pojo.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        doctorTypeList = pojoDetails.getOutput();
                        if (doctorTypeList.size() > 0) {
                            showDoctorTypeListDialog(doctorTypeList);
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

    private void showDoctorTypeListDialog(final ArrayList<DoctorTypeList_Model> doctorTypeList) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        builderSingle.setTitle("Select Doctor Type");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        for (int i = 0; i < doctorTypeList.size(); i++) {
            arrayAdapter.add(String.valueOf(doctorTypeList.get(i).getDocType()));
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
                edt_doctortype.setText(doctorTypeList.get(which).getDocType());
                DocTypeId = doctorTypeList.get(which).getDocTypeId();
            }
        });
        builderSingle.show();
    }

    public class DoctorRegistration extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("MobileNo", params[0]));
            param.add(new ParamsPojo("EmailId", params[1]));
            param.add(new ParamsPojo("DesgId", params[2]));
            param.add(new ParamsPojo("FIRSTNAME", params[3]));
            param.add(new ParamsPojo("MIDNAME", params[4]));
            param.add(new ParamsPojo("LASTNAME", params[5]));
            param.add(new ParamsPojo("MOTHERNAME", params[6]));
            param.add(new ParamsPojo("STATELGDCODE", params[7]));
            param.add(new ParamsPojo("SubOrgId", params[8]));
            param.add(new ParamsPojo("DISTLGDCODE", params[9]));
            param.add(new ParamsPojo("HospitalName", params[10]));
            param.add(new ParamsPojo("RegistrationNo", params[11]));
            param.add(new ParamsPojo("DocType", params[12]));
            res = WebServiceCall.APICall(ApplicationConstants.DoctorRegistration, ApplicationConstants.webservice, param);
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setIcon(R.drawable.icon_success);
                        builder.setTitle("Success");
                        builder.setCancelable(false);
                        builder.setMessage(message);
                        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
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


    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Doctor Registration");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
