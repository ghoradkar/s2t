package com.myhindlab.abkat.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.ConstructionSitesList_Model;
import com.myhindlab.abkat.models.ContractorOtherPersonDetails_Model;
import com.myhindlab.abkat.models.InsertBuilderDetails_Model;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ConstantData;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("SetTextI18n")
public class SiteSurvey_BuildingConstContractorDetails_Activity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private ProgressDialog pd;

    private NestedScrollView scrollView;
    private LinearLayout ll_contractordetails, ll_otherpersondetails;
    //    private EditText edt_reranumber;
//    private EditText edt_contractorname, edt_contractornumber,
//            edt_contractoremailid, edt_manageremailid;
    private EditText edt_managername, edt_managernumber;
    private CardView cd_contractordetails, cd_otherpersondetails, cd_viewcontractordetails,
            cd_viewotherpersondetails;
    private RecyclerView recycler_view_contractorList, recycler_view_otherpersonList;
    private Button btn_contractordetails, btn_otherpersondetails, btn_saveandnext;
//    private Button btn_next;

    private ArrayList<LinearLayout> contractorDetailsLayouts = new ArrayList<>();
    private ConstructionSitesList_Model siteDetails;

    private ArrayList<LinearLayout> otherpersonDetailsLayouts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sitesurvey_builderconstcontractordetails);

        init();
        setUpToolbar();
        setDefaults();
        setEventHandler();
    }

    private void init() {
//        edt_reranumber = findViewById(R.id.edt_reranumber);
//        edt_contractorname = findViewById(R.id.edt_contractorname);
//        edt_contractornumber = findViewById(R.id.edt_contractornumber);
//        edt_contractoremailid = findViewById(R.id.edt_contractoremailid);
        edt_managername = findViewById(R.id.edt_managername);
        edt_managernumber = findViewById(R.id.edt_managernumber);
//        edt_manageremailid = findViewById(R.id.edt_manageremailid);

        scrollView = findViewById(R.id.scrollView);

        ll_contractordetails = findViewById(R.id.ll_contractordetails);
        btn_contractordetails = findViewById(R.id.btn_contractordetails);

        cd_contractordetails = findViewById(R.id.cd_contractordetails);
        cd_viewcontractordetails = findViewById(R.id.cd_viewcontractordetails);
        recycler_view_contractorList = findViewById(R.id.recycler_view_contractorList);

        ll_otherpersondetails = findViewById(R.id.ll_otherpersondetails);
        btn_otherpersondetails = findViewById(R.id.btn_otherpersondetails);

        cd_otherpersondetails = findViewById(R.id.cd_otherpersondetails);
        cd_viewotherpersondetails = findViewById(R.id.cd_viewotherpersondetails);
        recycler_view_otherpersonList = findViewById(R.id.recycler_view_otherpersonList);

        btn_saveandnext = findViewById(R.id.btn_saveandnext);
//        btn_next = findViewById(R.id.btn_next);
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ConstantData constantData = ConstantData.getInstance();
        getSupportActionBar().setTitle(constantData.getSetSitetypeName() + " Site Survey");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setDefaults() {
        context = SiteSurvey_BuildingConstContractorDetails_Activity.this;
        pd = new ProgressDialog(context);

        siteDetails = new ConstructionSitesList_Model();
        siteDetails = (ConstructionSitesList_Model) getIntent().getSerializableExtra("siteDetails");

//        String reraNumber = siteDetails.getReraId();
//        edt_reranumber.setText(reraNumber);
//
//        if (Utilities.isNetworkAvailable(context)) {
//            new GetListOfBuilderDetailsAPICall().execute(siteDetails.getSiteDetailId());
//        } else {
//            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
//        }
    }

    private void setEventHandler() {
        btn_saveandnext.setOnClickListener(this);
//        btn_next.setOnClickListener(this);

        btn_contractordetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View rowView = inflater.inflate(R.layout.add_layout_contractordetails, null);
                LinearLayout ll = (LinearLayout) rowView;
                contractorDetailsLayouts.add(ll);
                ll_contractordetails.addView(rowView, ll_contractordetails.getChildCount() - 1);
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });
            }
        });

        btn_otherpersondetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (otherpersonDetailsLayouts.size() < 1) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View rowView = inflater.inflate(R.layout.add_layout_otherpersondetails, null);
                LinearLayout ll = (LinearLayout) rowView;
                otherpersonDetailsLayouts.add(ll);
                ll_otherpersondetails.addView(rowView, ll_otherpersondetails.getChildCount() - 1);
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });
//                } else {
//                    Utilities.showToastMessage("Only one other person details can be added", context, false);
//                }
            }
        });
    }

    public void removeContractorDetailsView(View view) {
        ll_contractordetails.removeView((View) view.getParent());
        contractorDetailsLayouts.remove(view.getParent());
//        relationsIdList.remove(view.getParent());
    }

    public void removeOtherPersonDetailsView(View view) {
        ll_otherpersondetails.removeView((View) view.getParent());
        otherpersonDetailsLayouts.remove(view.getParent());
//        relationsIdList.remove(view.getParent());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.btn_next:
//                startActivity(new Intent(context, SiteSurvey_RERASiteConstWorkerDetails_Activity.class)
//                        .putExtra("siteDetails", siteDetails));
//                finish();
//                break;
//
            case R.id.btn_saveandnext:
                if (!Utilities.isNetworkAvailable(context)) {
                    Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                    return;
                }
//
//                if (edt_contractorname.getText().toString().trim().isEmpty()) {
//                    edt_contractorname.setError("Required Contractor Name");
//                    edt_contractorname.requestFocus();
//                    return;
//                }
//
//                if (edt_contractornumber.getText().toString().trim().isEmpty()) {
//                    edt_contractornumber.setError("Required Mobile Number");
//                    edt_contractornumber.requestFocus();
//                    return;
//                } else if (!Utilities.isMobileNo(edt_contractornumber.getText().toString().trim())) {
//                    edt_contractornumber.setError("Invalid Mobile Number");
//                    edt_contractornumber.requestFocus();
//                    return;
//                }
//
//                if (!edt_contractoremailid.getText().toString().trim().isEmpty()) {
//                    if (!Utilities.isEmailValid(edt_contractoremailid.getText().toString().trim())) {
//                        edt_contractoremailid.setError("Invalid Email ID");
//                        edt_contractoremailid.requestFocus();
//                        return;
//                    }
//                }
//
                if (edt_managername.getText().toString().trim().isEmpty()) {
                    edt_managername.setError("Required Site Manager Name");
                    edt_managername.requestFocus();
                    return;
                }

                if (edt_managernumber.getText().toString().trim().isEmpty()) {
                    edt_managernumber.setError("Required Mobile Number");
                    edt_managernumber.requestFocus();
                    return;
                } else if (!Utilities.isMobileNo(edt_managernumber.getText().toString().trim())) {
                    edt_managernumber.setError("Invalid Mobile Number");
                    edt_managernumber.requestFocus();
                    return;
                }
//
//                if (!edt_manageremailid.getText().toString().trim().isEmpty()) {
//                    if (!Utilities.isEmailValid(edt_manageremailid.getText().toString().trim())) {
//                        edt_manageremailid.setError("Invalid Email ID");
//                        edt_manageremailid.requestFocus();
//                        return;
//                    }
//                }
//
                if (contractorDetailsLayouts.size() <= 0) {
                    Utilities.showToastMessage("Please enter atleast one contractor details.", context, false);
                    return;
                }

                ArrayList<ContractorOtherPersonDetails_Model> contractorDetailList = new ArrayList<>();
                for (int i = 0; i < contractorDetailsLayouts.size(); i++) {
                    EditText cname = contractorDetailsLayouts.get(i).findViewById(R.id.edt_contractorname);
                    EditText cnumber = contractorDetailsLayouts.get(i).findViewById(R.id.edt_contractornumber);
//                    EditText cemailid = contractorDetailsLayouts.get(i).findViewById(R.id.edt_emailid);

                    if (cname.getText().toString().trim().isEmpty()) {
                        cname.setError("Required contractor Name");
                        cname.requestFocus();
                        return;
                    }

                    if (cnumber.getText().toString().trim().isEmpty()) {
                        cnumber.setError("Required Mobile Number");
                        cnumber.requestFocus();
                        return;
                    } else if (!Utilities.isMobileNo(cnumber.getText().toString().trim())) {
                        cnumber.setError("Invalid Mobile Number");
                        cnumber.requestFocus();
                        return;
                    }
//
//                    if (!cemailid.getText().toString().trim().isEmpty()) {
//                        if (!Utilities.isEmailValid(cemailid.getText().toString().trim())) {
//                            cemailid.setError("Invalid Email ID");
//                            cemailid.requestFocus();
//                            return;
//                        }
//                    }
//
                    ContractorOtherPersonDetails_Model data = new ContractorOtherPersonDetails_Model();
                    data.setContractorName(cname.getText().toString().trim());
                    data.setContractorContactNo(cnumber.getText().toString().trim());
                    data.setContractorEmailId("");
                    data.setType("1");
//                    data.setContractorEmailId(cemailid.getText().toString().trim());

                    contractorDetailList.add(data);
                }

//                ArrayList<OtherPersonDetails_Model> otherPersonDetails = new ArrayList<>();
                if (otherpersonDetailsLayouts.size() > 0) {
                    for (int i = 0; i < otherpersonDetailsLayouts.size(); i++) {
                        EditText opname = otherpersonDetailsLayouts.get(i).findViewById(R.id.edt_otherpersonname);
                        EditText opnumber = otherpersonDetailsLayouts.get(i).findViewById(R.id.edt_otherpersonnumber);

                        if (opname.getText().toString().trim().isEmpty()) {
                            opname.setError("Required Person Name");
                            opname.requestFocus();
                            return;
                        }

                        if (opnumber.getText().toString().trim().isEmpty()) {
                            opnumber.setError("Required Mobile Number");
                            opnumber.requestFocus();
                            return;
                        } else if (!Utilities.isMobileNo(opnumber.getText().toString().trim())) {
                            opnumber.setError("Invalid Mobile Number");
                            opnumber.requestFocus();
                            return;
                        }

                        ContractorOtherPersonDetails_Model data = new ContractorOtherPersonDetails_Model();
                        data.setContractorName(opname.getText().toString().trim());
                        data.setContractorContactNo(opnumber.getText().toString().trim());
                        data.setContractorEmailId("");
                        data.setType("2");
//                    data.setContractorEmailId(cemailid.getText().toString().trim());

                        contractorDetailList.add(data);
                    }
                }

                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.serializeNulls();
                Gson gson = gsonBuilder.create();
                String contractorOtherPersonDeatils = gson.toJson(contractorDetailList);

                insertAPICall(contractorOtherPersonDeatils);
                break;

            default:
                break;
        }
    }

    private void insertAPICall(String contractorOtherPersonDetails) {
//    private void insertAPICall(ArrayList<ContractorOtherPersonDetails_Model> contractorOtherPersonDetailList) {
        InsertBuilderDetails_Model data = new InsertBuilderDetails_Model();

//        String bName = edt_buildername.getText().toString().trim();
//        String bNumber = edt_buildernumber.getText().toString().trim();
//        String bEmailID = edt_builderemailid.getText().toString().trim();
        String mName = edt_managername.getText().toString().trim();
        String mNumber = edt_managernumber.getText().toString().trim();
//        String mEmailID = edt_manageremailid.getText().toString().trim();

        data.setSiteDetailId(siteDetails.getSiteDetailId());
        data.setReraId(siteDetails.getReraId());
//        data.setBuilderName(bName);
//        data.setBuilderContactNo(bNumber);
//        data.setEmailId(bEmailID);
        data.setBuilderName("");
        data.setBuilderContactNo("");
        data.setEmailId("");
        data.setSiteMngrName(mName);
        data.setSiteMngrContactNo(mNumber);
        data.setSiteMngrEmailId("");
//        data.setSiteMngrEmailId(mEmailID);
        data.setCreatedBy(siteDetails.getUserID());
//        data.setContractorDetails(contractorDetailList);
//        if (otherPersonDetails.size() > 0) {
////            data.setOtherPersonDetails(otherPersonDetails);
//            String otherPersonName = "";
//            for (int i = 0; i < otherPersonDetails.size(); i++) {
//                otherPersonName = otherPersonDetails.get(i).getOtherPersonName().trim();
//            }
//
//            if (otherPersonName == null || otherPersonName.equalsIgnoreCase("")) {
//                otherPersonName = "";
//            }
//
//            data.setOtherContName(otherPersonName);
//        } else {
//            data.setOtherContName("");
//        }
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();
        Gson gson = gsonBuilder.create();
        String inputString = gson.toJson(data);

        new insertCW_BuilderDetailsForApp().execute(inputString, contractorOtherPersonDetails);
    }

    private class insertCW_BuilderDetailsForApp extends AsyncTask<String, Integer, String> {

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
            param.add(new ParamsPojo("BuilderDetailsJSON", params[0]));
            param.add(new ParamsPojo("JSONOtherContractor_Details", params[1]));

            res = WebServiceCall.APICall(ApplicationConstants.insertCW_BuilderDetailsForApp, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();

            if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
                try {
                    JSONObject obj1 = new JSONObject(result);
                    String status = obj1.getString("status");
                    String message = obj1.getString("message");

                    if (status.equalsIgnoreCase("Success")) {
                        Utilities.showToastMessage("Data save successfully", context, true);
                        siteDetails.setBuilderID(message);
                        startActivity(new Intent(context, SiteSurvey_BuildingConstWorkerDetails_Activity.class)
                                .putExtra("siteDetails", siteDetails));
                        finish();
                    } else
                        Utilities.showAlertDialog(context, status, message, false);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
                }
            } else {
                Utilities.showToastMessage(R.string.msgt_emptyresponse, context, false);
            }
        }
    }

//    private class GetListOfBuilderDetailsAPICall extends AsyncTask<String, Integer, String> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pd.setMessage("Please wait . . . ");
//            pd.setCancelable(false);
//            pd.show();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            String res = "";
//
//            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
//            param.add(new ParamsPojo("Id", params[0]));
//
//            res = WebServiceCall.APICall(ApplicationConstants.GetListOfBuilderDetails, param);
//            return res;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            pd.dismiss();
//
//            if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
//                try {
//                    Gson gson = new Gson();
//                    GetListOfBuilderDetails_Responce responce = gson.fromJson(result, GetListOfBuilderDetails_Responce.class);
//                    String status = responce.getStatus();
//                    String message = responce.getMessage();
//
//                    if (status.equalsIgnoreCase("Success")) {
//                        ArrayList<GetListOfBuilderDetails> contractorDetailsList = new ArrayList<>();
//                        contractorDetailsList = responce.getOutput();
//
//                        if (contractorDetailsList.size() > 0) {
//                            GetListOfBuilderDetails data = new GetListOfBuilderDetails();
//                            data = contractorDetailsList.get(0);
//                            siteDetails.setBuilderID(data.getBuilderDetailId());
//                            siteDetails.setSiteDetailId(data.getSiteDetailId());
//
//                            ArrayList<ContractorOtherPersonDetails_Model> cDetailsList = new ArrayList<>();
//                            for (GetListOfBuilderDetails cdata : contractorDetailsList) {
//                                ContractorOtherPersonDetails_Model pojo = new ContractorOtherPersonDetails_Model();
//                                pojo.setContractorName(cdata.getContractorName());
//                                pojo.setContractorContactNo(cdata.getContractorContactNo());
//                                pojo.setContractorEmailId(cdata.getContractorEmailId());
//                                cDetailsList.add(pojo);
//                            }
//
//                            edt_buildername.setText(data.getBuilderName());
//                            edt_buildernumber.setText(data.getBuilderContactNo());
//                            edt_builderemailid.setText(data.getEmailId());
//                            edt_managername.setText(data.getSiteMngrName());
//                            edt_managernumber.setText(data.getSiteMngrContactNo());
//                            edt_manageremailid.setText(data.getSiteMngrEmailId());
//
//                            edt_buildername.setClickable(false);
//                            edt_buildernumber.setClickable(false);
//                            edt_builderemailid.setClickable(false);
//                            edt_managername.setClickable(false);
//                            edt_managernumber.setClickable(false);
//                            edt_manageremailid.setClickable(false);
//
//                            edt_buildername.setFocusable(false);
//                            edt_buildernumber.setFocusable(false);
//                            edt_builderemailid.setFocusable(false);
//                            edt_managername.setFocusable(false);
//                            edt_managernumber.setFocusable(false);
//                            edt_manageremailid.setFocusable(false);
//
//                            cd_contractordetails.setVisibility(View.GONE);
//                            cd_viewcontractordetails.setVisibility(View.VISIBLE);
//                            btn_saveandnext.setVisibility(View.GONE);
//                            btn_next.setVisibility(View.VISIBLE);
//
//                            showSiteListDialog(cDetailsList);
//
//                        } else {
//                            cd_contractordetails.setVisibility(View.VISIBLE);
//                            cd_viewcontractordetails.setVisibility(View.GONE);
//                            btn_saveandnext.setVisibility(View.VISIBLE);
//                            btn_next.setVisibility(View.GONE);
//                        }
//                    } else {
//                        cd_contractordetails.setVisibility(View.VISIBLE);
//                        cd_viewcontractordetails.setVisibility(View.GONE);
//                        btn_saveandnext.setVisibility(View.VISIBLE);
//                        btn_next.setVisibility(View.GONE);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    cd_contractordetails.setVisibility(View.VISIBLE);
//                    cd_viewcontractordetails.setVisibility(View.GONE);
//                    btn_saveandnext.setVisibility(View.VISIBLE);
//                    btn_next.setVisibility(View.GONE);
//                }
//            } else {
//                cd_contractordetails.setVisibility(View.VISIBLE);
//                cd_viewcontractordetails.setVisibility(View.GONE);
//                btn_saveandnext.setVisibility(View.VISIBLE);
//                btn_next.setVisibility(View.GONE);
//            }
//        }
//    }
//
//    private void showSiteListDialog(ArrayList<ContractorOtherPersonDetails_Model> cDetailsList) {
//        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
//        recycler_view_contractorList.setLayoutManager(layoutManager);
//        recycler_view_contractorList.setAdapter(new ConstructionDetailsAdapter(cDetailsList));
//    }
}
