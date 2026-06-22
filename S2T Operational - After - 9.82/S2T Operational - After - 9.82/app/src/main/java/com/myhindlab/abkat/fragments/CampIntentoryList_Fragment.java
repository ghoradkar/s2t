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
import com.myhindlab.abkat.adapters.CampInventoryAdapter;
import com.myhindlab.abkat.models.CampInventoryListModel;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ConstantData;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;

import java.util.ArrayList;
import java.util.List;

public class CampIntentoryList_Fragment extends Fragment {

    private static Context context;
    private static UserSessionManager session;
    private static LinearLayout ll_nothingtoshow, ll_lables;
    private static RecyclerView rv_inventory;
    private static SwipeRefreshLayout swipeRefreshLayout;
    private static TextView tv_message;
    private static ArrayList<CampInventoryListModel.OutputBean> campInventoryList;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_camp_inventory, container, false);
        context = getActivity();
        init(rootView);
        setDefault();
        setEventHandlers();
        return rootView;
    }

    private void init(View rootView) {
        session = new UserSessionManager(context);
        ll_nothingtoshow = rootView.findViewById(R.id.ll_nothingtoshow);
        tv_message = rootView.findViewById(R.id.tv_message);
        ll_lables = rootView.findViewById(R.id.ll_labels);
        swipeRefreshLayout = rootView.findViewById(R.id.swipeRefreshLayout);
        rv_inventory = rootView.findViewById(R.id.rv_inventory);
        rv_inventory.setLayoutManager(new LinearLayoutManager(context));
        campInventoryList = new ArrayList<>();
    }

    public static void setDefault() {
        if (Utilities.isNetworkAvailable(context)) {
            new GetInventoryDetailsOncampId().execute(ConstantData.getInstance().getCalendarCampId());
        } else {
            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
        }
    }

    private void setEventHandlers() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Utilities.isNetworkAvailable(context)) {
                    new GetInventoryDetailsOncampId().execute(ConstantData.getInstance().getCalendarCampId());
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                    Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                }
            }
        });
    }

    private static class GetInventoryDetailsOncampId extends AsyncTask<String, Void, String> {

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

            res = WebServiceCall.APICall(ApplicationConstants.GetInventoryDetailsOncampId, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            pd.dismiss();
            try {
                if (!result.equals("")) {

                    CampInventoryListModel pojoDetails = new Gson().fromJson(result, CampInventoryListModel.class);
                    String status = pojoDetails.getStatus();
                    String message = pojoDetails.getMessage();

                    if (status.equalsIgnoreCase("success")) {
                        campInventoryList = pojoDetails.getOutput();
                        if (campInventoryList.size() > 0) {
                            rv_inventory.setVisibility(View.VISIBLE);
                            ll_lables.setVisibility(View.VISIBLE);
                            ll_nothingtoshow.setVisibility(View.GONE);
                            rv_inventory.setAdapter(new CampInventoryAdapter(context, campInventoryList));
                        } else {
                            rv_inventory.setVisibility(View.GONE);
                            ll_lables.setVisibility(View.GONE);
                            ll_nothingtoshow.setVisibility(View.VISIBLE);
                            tv_message.setText("Inventory not mapped with this camp");
                        }
                    } else {
                        rv_inventory.setVisibility(View.GONE);
                        ll_lables.setVisibility(View.GONE);
                        ll_nothingtoshow.setVisibility(View.VISIBLE);
                        tv_message.setText("Inventory not mapped with this camp");
                    }
                } else {
                    rv_inventory.setVisibility(View.GONE);
                    ll_lables.setVisibility(View.GONE);
                    ll_nothingtoshow.setVisibility(View.VISIBLE);
                    tv_message.setText("Inventory not mapped with this camp");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
