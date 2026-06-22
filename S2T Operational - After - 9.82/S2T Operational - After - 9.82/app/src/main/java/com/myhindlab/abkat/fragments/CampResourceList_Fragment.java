package com.myhindlab.abkat.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.adapters.CampResourcesAdapter;
import com.myhindlab.abkat.models.CampResourceListModel;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ConstantData;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;

import java.util.ArrayList;
import java.util.List;

public class CampResourceList_Fragment extends Fragment {

    private static Context context;
    private static UserSessionManager session;
    private static LinearLayout ll_nothingtoshow, ll_labels;
    private static SwipeRefreshLayout swipeRefreshLayout;
    private static RecyclerView rv_resources;
    private static TextView tv_message;
    private static ArrayList<CampResourceListModel.OutputBean> campResourcesList;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_camp_resources, container, false);
        context = getActivity();
        init(rootView);
        setDefault();
        setEventHandlers();
        return rootView;
    }

    private void init(View rootView) {

        session = new UserSessionManager(context);
        ll_nothingtoshow = rootView.findViewById(R.id.ll_nothingtoshow);
        ll_labels = rootView.findViewById(R.id.ll_labels);
        tv_message = rootView.findViewById(R.id.tv_message);
        swipeRefreshLayout = rootView.findViewById(R.id.swipeRefreshLayout);
        rv_resources = rootView.findViewById(R.id.rv_resources);
        rv_resources.setLayoutManager(new LinearLayoutManager(context));
        campResourcesList = new ArrayList<>();
    }

    public static void setDefault() {
            if (Utilities.isNetworkAvailable(context)) {
                new GetCampResourceListOncampId().execute(ConstantData.getInstance().getCalendarCampId());
            } else {
                Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
            }
    }

    private void setEventHandlers() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Utilities.isNetworkAvailable(context)) {
                    new GetCampResourceListOncampId().execute(ConstantData.getInstance().getCalendarCampId());
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                    Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                }
            }
        });
    }

    private static class GetCampResourceListOncampId extends AsyncTask<String, Void, String> {

        private ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeRefreshLayout.setRefreshing(false);
            pd = new ProgressDialog(context);
            pd.setMessage("Please wait . . . ");
            pd.setCancelable(false);
//            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "";
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("CampId", params[0]));

            res = WebServiceCall.APICall(ApplicationConstants.GetCampResourceListOncampId, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            pd.dismiss();
            try {
                if (!result.equals("")) {

                    CampResourceListModel pojoDetails = new Gson().fromJson(result, CampResourceListModel.class);
                    String status = pojoDetails.getStatus();
                    String message = pojoDetails.getMessage();

                    if (status.equalsIgnoreCase("success")) {
                        campResourcesList = pojoDetails.getOutput();
                        if (campResourcesList.size() > 0) {
                            ll_labels.setVisibility(View.VISIBLE);
                            rv_resources.setVisibility(View.VISIBLE);
                            ll_nothingtoshow.setVisibility(View.GONE);
                            rv_resources.setAdapter(new CampResourcesAdapter(context, campResourcesList));
                        } else {
                            ll_labels.setVisibility(View.GONE);
                            rv_resources.setVisibility(View.GONE);
                            ll_nothingtoshow.setVisibility(View.VISIBLE);
                            tv_message.setText("Resources not mapped with this camp");
                        }
                    } else {
                        ll_labels.setVisibility(View.GONE);
                        rv_resources.setVisibility(View.GONE);
                        ll_nothingtoshow.setVisibility(View.VISIBLE);
                        tv_message.setText("Resources not mapped with this camp");
                    }
                } else {
                    ll_labels.setVisibility(View.GONE);
                    rv_resources.setVisibility(View.GONE);
                    ll_nothingtoshow.setVisibility(View.VISIBLE);
                    tv_message.setText("Resources not mapped with this camp");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
