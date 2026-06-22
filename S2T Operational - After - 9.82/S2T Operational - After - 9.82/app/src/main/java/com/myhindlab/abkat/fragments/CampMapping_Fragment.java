package com.myhindlab.abkat.fragments;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;

import com.google.gson.Gson;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.adapters.CampMapping_Adapter;
import com.myhindlab.abkat.models.CampListForMapping_Model;
import com.myhindlab.abkat.pojos.CampListForMapping_Pojo;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CampMapping_Fragment extends Fragment {

    private Context context;
    private MaterialEditText edt_date;
    private RecyclerView rv_camplist;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int mYear, mMonth, mDay;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_campmapping, container, false);
        context = getActivity();
        init(rootView);
        setDefault();
        setEventHandlers();
        return rootView;
    }

    private void init(View rootView) {
        swipeRefreshLayout = rootView.findViewById(R.id.swipeRefreshLayout);
        rv_camplist = rootView.findViewById(R.id.rv_camplist);
        edt_date = rootView.findViewById(R.id.edt_date);
        rv_camplist.setLayoutManager(new LinearLayoutManager(context));

    }

    private void setDefault() {

        Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);

        edt_date.setText(Utilities.ConvertDateFormat(Utilities.dfDate4, mDay, mMonth + 1, mYear));

        if (Utilities.isNetworkAvailable(context)) {
//            new GetActiveCampOnDate_CW().execute("0", edt_date.getText().toString().trim());
        } else {
            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
        }
    }

    private void setEventHandlers() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (Utilities.isNetworkAvailable(context)) {
//                    new GetActiveCampOnDate_CW().execute("0", edt_date.getText().toString().trim());
                } else {
                    Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                }

            }
        });

        edt_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd1 = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                edt_date.setText(Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year));

                                mYear = year;
                                mMonth = monthOfYear;
                                mDay = dayOfMonth;

                                if (Utilities.isNetworkAvailable(context)) {
//                                    new GetActiveCampOnDate_CW().execute("0", edt_date.getText().toString().trim());
                                } else {
                                    Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                                }
                            }
                        }, mYear, mMonth, mDay);
                try {
//                    dpd1.getDatePicker().setMinDate(System.currentTimeMillis());
                    dpd1.getDatePicker().setCalendarViewShown(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dpd1.show();
            }
        });
    }

    public class GetActiveCampOnDate_CW extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "";
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("DISTLGDCODE", params[0]));
            param.add(new ParamsPojo("Date", params[1]));
            res = WebServiceCall.APICall(ApplicationConstants.GetActiveCampOnDate_CW, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            swipeRefreshLayout.setRefreshing(false);
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    ArrayList<CampListForMapping_Model> campList = new ArrayList<>();
                    CampListForMapping_Pojo pojoDetails = new Gson().fromJson(result, CampListForMapping_Pojo.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        campList = pojoDetails.getOutput();
                        if (campList.size() > 0) {
                            rv_camplist.setAdapter(new CampMapping_Adapter(context, campList));
                        }
                    } else {
                        rv_camplist.setAdapter(new CampMapping_Adapter(context, campList));

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
            }
        }
    }
}
