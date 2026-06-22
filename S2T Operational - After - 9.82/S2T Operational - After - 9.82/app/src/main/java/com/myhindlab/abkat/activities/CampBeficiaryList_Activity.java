package com.myhindlab.abkat.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.adapters.CampBeneficiaryAdapter;
import com.myhindlab.abkat.models.CampBeneficiaryListModel;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ConstantData;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;

import java.util.ArrayList;
import java.util.List;

public class CampBeficiaryList_Activity extends AppCompatActivity {

    private Context context;
    private UserSessionManager session;
    private LinearLayout ll_nothingtoshow, ll_lables;
    private RecyclerView rv_beneficiary;
    private TextView tv_message;
    private ArrayList<CampBeneficiaryListModel.OutputBean> campBeneficiaryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camp_beficiarylist);

        init();
        setDefault();
        setEventHandlers();
        setUpToolBar();
    }

    private void init() {
        context = CampBeficiaryList_Activity.this;
        session = new UserSessionManager(context);
        ll_nothingtoshow = findViewById(R.id.ll_nothingtoshow);
        tv_message = findViewById(R.id.tv_message);
        ll_lables = findViewById(R.id.ll_labels);
        rv_beneficiary = findViewById(R.id.rv_beneficiary);
        rv_beneficiary.setLayoutManager(new LinearLayoutManager(context));
        campBeneficiaryList = new ArrayList<>();
    }

    private void setDefault() {


        if (Utilities.isNetworkAvailable(context)) {
            new GetRegiWorkerDetailsOncampId().execute(ConstantData.getInstance().getCalendarCampId());
        } else {
            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
        }
    }

    private void setEventHandlers() {
    }

    private class GetRegiWorkerDetailsOncampId extends AsyncTask<String, Void, String> {

        private ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(context);
            pd.setMessage("Please wait . . . ");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "";
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("CampId", params[0]));

            res = WebServiceCall.APICall(ApplicationConstants.GetRegiWorkerDetailsOncampId, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            try {
                if (!result.equals("")) {

                    CampBeneficiaryListModel pojoDetails = new Gson().fromJson(result, CampBeneficiaryListModel.class);
                    String status = pojoDetails.getStatus();
                    String message = pojoDetails.getMessage();

                    if (status.equalsIgnoreCase("success")) {
                        campBeneficiaryList = pojoDetails.getOutput();
                        if (campBeneficiaryList.size() > 0) {
                            rv_beneficiary.setVisibility(View.VISIBLE);
                            ll_lables.setVisibility(View.VISIBLE);
                            ll_nothingtoshow.setVisibility(View.GONE);
                            rv_beneficiary.setAdapter(new CampBeneficiaryAdapter(context, campBeneficiaryList));
                        } else {
                            rv_beneficiary.setVisibility(View.GONE);
                            ll_lables.setVisibility(View.GONE);
                            ll_nothingtoshow.setVisibility(View.VISIBLE);
                            tv_message.setText("Beneficiary not mapped with this camp");
                        }
                    } else {
                        rv_beneficiary.setVisibility(View.GONE);
                        ll_lables.setVisibility(View.GONE);
                        ll_nothingtoshow.setVisibility(View.VISIBLE);
                        tv_message.setText("Beneficiary not mapped with this camp");
                    }
                } else {

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setUpToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Beneficiary List");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
