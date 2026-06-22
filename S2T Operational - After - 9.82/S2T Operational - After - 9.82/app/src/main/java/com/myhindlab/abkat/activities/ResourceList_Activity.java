package com.myhindlab.abkat.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.SearchView;

import com.google.gson.Gson;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.adapters.TeamListAdapter;
import com.myhindlab.abkat.models.TeamList;
import com.myhindlab.abkat.pojos.TeamListPojo;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ResourceList_Activity extends AppCompatActivity {

    private Context context;
    private ProgressDialog pd;
    private String TAG = getClass().getName();
    private UserSessionManager session;
    private RecyclerView rv_horiteam;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int[] colorArray;
    private int mYear, mMonth, mDay;
    String EmpCode, divId, distlgdcode, desgid;
    private ArrayList<TeamList> desigList;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource_list);

        init();
        getSessionData();
        setDefaults();
        setToolBar();
        setEventHandler();
    }

    private void init() {
        context = ResourceList_Activity.this;
        session = new UserSessionManager(context);
        pd = new ProgressDialog(context);
        rv_horiteam = findViewById(R.id.rv_horiteam);
        searchView = findViewById(R.id.searchView);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        rv_horiteam.setLayoutManager(new GridLayoutManager(context, 2));

    }

    private void setToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Resource List");
        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getSessionData() {

        try {
            JSONArray user_info = new JSONArray(session.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                EmpCode = json.getString("EmpCode");
//                divId = json.getString("EmpCode");
                divId = "0";
                desgid = json.getString("DESGID");
                distlgdcode = json.getString("DISTLGDCODE");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setEventHandler() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Utilities.isNetworkAvailable(context)) {
                    new GetActiveUserList().execute("0", "0", "0");
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                }
            }
        });
    }

    private void setDefaults() {

        Calendar calendar = Calendar.getInstance();

        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        String date = mYear + "-" + mMonth + "-" + mDay;
        colorArray = new int[]{
                getResources().getColor(R.color.dashb1),
                getResources().getColor(R.color.dashb2),
                getResources().getColor(R.color.dashb3),
                getResources().getColor(R.color.dashb4),
                getResources().getColor(R.color.dashb5),
                getResources().getColor(R.color.dashb11),
                getResources().getColor(R.color.dashb7),
                getResources().getColor(R.color.dashb13)};


        if (Utilities.isNetworkAvailable(context)) {
            new GetActiveUserList().execute("0", "0", "0");
        } else {
            // Utilities.showMessage(R.string.msgt_nointernetconnection, context);
            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
        }

    }

    public class GetActiveUserList extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("DIVID", params[0]));
            param.add(new ParamsPojo("DISTLGDCODE", params[1]));
            param.add(new ParamsPojo("DESGID", params[2]));

            res = WebServiceCall.APICall(ApplicationConstants.GetActiveUserList, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    desigList = new ArrayList<>();
                    TeamListPojo pojoDetails = new Gson().fromJson(result, TeamListPojo.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        desigList = pojoDetails.getOutput();
                        if (desigList.size() > 0) {
                            rv_horiteam.setAdapter(new TeamListAdapter(context, colorArray, desigList));
                        }
                    } else {
                        Utilities.showAlertDialog(context, type, message, false);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
            }
            searchView.clearFocus();

        }
    }

}
