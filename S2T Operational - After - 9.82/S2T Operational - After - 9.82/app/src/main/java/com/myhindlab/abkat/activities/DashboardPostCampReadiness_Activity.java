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

import com.google.gson.Gson;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.adapters.PostCampReadinessAdapter;
import com.myhindlab.abkat.models.PostCampReadinessModel;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class DashboardPostCampReadiness_Activity extends AppCompatActivity {

    private Context context;
    private RecyclerView rv_camp_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_postcamp_readiness);

        init();
        setDefaults();
        setUpToolbar();
    }

    private void init() {
        context = DashboardPostCampReadiness_Activity.this;
        rv_camp_list = findViewById(R.id.rv_camp_list);
        rv_camp_list.setLayoutManager(new LinearLayoutManager(context));
    }

    private void setDefaults() {
//        if (Utilities.isNetworkAvailable(context)) {
//            new InvoiceAndPostCampDashboard(
//                    context,
//                    rv_camp_list)
//                    .execute(
//                            getIntent().getStringExtra("districtId"),
//                            getIntent().getStringExtra("selectedMonthId"),
//                            getIntent().getStringExtra("selectedYearId"),
//                            "3"
//                    );
//        } else {
//            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
//        }
        List<PostCampReadinessModel.OutputBean> campList;
        campList = (List<PostCampReadinessModel.OutputBean>) getIntent().getSerializableExtra("invoiceList");
        rv_camp_list.setAdapter(new PostCampReadinessAdapter(context, campList));
    }

    private static class InvoiceAndPostCampDashboard extends AsyncTask<String, Void, String> {
        private ProgressDialog pd;

        WeakReference<Context> context;
        WeakReference<RecyclerView> rv_camp_list;

        InvoiceAndPostCampDashboard(Context context, RecyclerView rv_camp_list) {
            this.context = new WeakReference<>(context);
            this.rv_camp_list = new WeakReference<>(rv_camp_list);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(context.get());
            pd.setMessage("Please wait . . . ");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String res;
            List<ParamsPojo> param = new ArrayList<>();
            param.add(new ParamsPojo("DISTLGDCODE", params[0]));
            param.add(new ParamsPojo("MonthID", params[1]));
            param.add(new ParamsPojo("Year", params[2]));
            param.add(new ParamsPojo("TYPE", params[3]));
            res = WebServiceCall.APICall(ApplicationConstants.InvoiceAndPostCampDashboard, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type, message;
            try {
                if (!result.equals("")) {
                    PostCampReadinessModel pojoDetails = new Gson().fromJson(result, PostCampReadinessModel.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        List<PostCampReadinessModel.OutputBean> campList = pojoDetails.getOutput();
                        if (campList.size() > 0) {
                            rv_camp_list.get().setAdapter(new PostCampReadinessAdapter(context.get(), campList));
                        } else {
                            Utilities.showToastMessage(R.string.msgt_emptylist, context.get(), false);
                        }
                    } else {
                        Utilities.showAlertDialog(context.get(), "Fail", message, false);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context.get(), "Please Try Again", "Server Not Responding", false);
            }
        }
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Post Camp Readiness");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
