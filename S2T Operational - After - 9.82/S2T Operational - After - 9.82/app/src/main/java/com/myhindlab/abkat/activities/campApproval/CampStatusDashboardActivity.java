package com.myhindlab.abkat.activities.campApproval;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.campApproval.models.CampStatusCountModel;
import com.myhindlab.abkat.models.DistrictList_Model;
import com.myhindlab.abkat.pojos.DistrictList_Pojo;
import com.myhindlab.abkat.rest.ApiClient;
import com.myhindlab.abkat.rest.ApiInterface;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;


import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CampStatusDashboardActivity extends AppCompatActivity {
    private Context context;
    private AppCompatSpinner appCompatSpinner;
    private TextInputEditText edtFromDate, edtToDate, edtSelDistrict;
    private String fromDate, toDate, districtId = "0";
    private ApiInterface apiInterface;
    private TextView tvPendingCount, tvHoldCount, tvApprovedCount, tvTotalCount, tvRejectCount;
    private ArrayList<DistrictList_Model> districtList_modelList;
    private MaterialCardView cvPending, cvHold, cvApprove, cvRejected, cvTotal;
    private UserSessionManager userSessionManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int mYear, mMonth, mDay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camp_status_dashboard);
        setUpToolbar();
        init();
        clickEvent();
        if (Utilities.isNetworkAvailable(context))
            getCount();
        else
            Utilities.showToastMessage("Please connect to the internet", context, false);
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Camp Status");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    void init() {
        context = CampStatusDashboardActivity.this;
        userSessionManager = new UserSessionManager(context);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        edtSelDistrict = findViewById(R.id.edtSelDistrict);
        edtFromDate = findViewById(R.id.edt_from_date);
        edtToDate = findViewById(R.id.edt_to_date);
        cvPending = findViewById(R.id.cvPending);
        cvApprove = findViewById(R.id.cvApprove);
        cvHold = findViewById(R.id.cvHold);
        tvApprovedCount = findViewById(R.id.tvApproveCount);
        tvPendingCount = findViewById(R.id.tvPendingCount);
        tvHoldCount = findViewById(R.id.tvHoldCount);
        tvRejectCount = findViewById(R.id.tvRejectCount);
        tvTotalCount = findViewById(R.id.tvTotalCount);
        cvRejected = findViewById(R.id.cvRejected);
        cvTotal = findViewById(R.id.cvTotal);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        fromDate = mYear + "-" + new DecimalFormat("00").format((mMonth + 1)) + "-" + new DecimalFormat("00").format((mDay));
        toDate = mYear + "-" + new DecimalFormat("00").format((mMonth + 1)) + "-" + new DecimalFormat("00").format((mDay));

        edtFromDate.setText(fromDate);
        edtToDate.setText(toDate);


    }

    void clickEvent() {
        edtFromDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DatePickerDialog dpd = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                fromDate = year + "-"
                                        + new DecimalFormat("00").format((monthOfYear + 1))
                                        + "-" + new DecimalFormat("00").format(dayOfMonth);
                                edtFromDate.setText(fromDate);

//                                FromDate = year + "-" + monthOfYear + 1 + "-" + dayOfMonth;
                                toDate = "";
                                edtToDate.setText(toDate);
                            }
                        }, mYear, mMonth, mDay);
                dpd.getDatePicker().setCalendarViewShown(false);
                dpd.getDatePicker().setMaxDate(System.currentTimeMillis());

                dpd.show();
            }
        });


        edtToDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
                long milliseconds = System.currentTimeMillis();
                if (!fromDate.isEmpty()) {
                    try {
                        Date d = f.parse(fromDate);
                        milliseconds = d.getTime();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                DatePickerDialog dpd = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                toDate = year + "-"
                                        + new DecimalFormat("00").format((monthOfYear + 1))
                                        + "-" + new DecimalFormat("00").format(dayOfMonth);
                                edtToDate.setText(toDate);
                                getCount();


                            }
                        }, mYear, mMonth, mDay);
                dpd.getDatePicker().setMinDate(milliseconds);
                dpd.getDatePicker().setCalendarViewShown(false);
                dpd.show();
            }
        });

//        if (userSessionManager.getUserDetailsJson().getDesgid() != 84) {
            edtSelDistrict.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Utilities.isNetworkAvailable(context))
                        getDistrict();
                    else
                        Utilities.showToastMessage("Please connect to the internet", context, false);

                }
            });
//        } else {
//            districtId = String.valueOf(userSessionManager.getUserDetailsJson().getDistlgdcode());
//            edtSelDistrict.setText(userSessionManager.getUserDetailsJson().getDistrict());
//        }

        cvPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, CampStatusListActivity.class)
                        .putExtra("distCode", districtId)
                        .putExtra("fromDate", fromDate)
                        .putExtra("toDate", toDate)
                        .putExtra("status", 0));
            }
        });
        cvApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, CampStatusListActivity.class)
                        .putExtra("distCode", districtId)
                        .putExtra("fromDate", fromDate)
                        .putExtra("toDate", toDate)
                        .putExtra("status", 1));
            }
        });
        cvRejected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, CampStatusListActivity.class)
                        .putExtra("distCode", districtId)
                        .putExtra("fromDate", fromDate)
                        .putExtra("toDate", toDate)
                        .putExtra("status", 2));
            }
        });

        cvHold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, CampStatusListActivity.class)
                        .putExtra("distCode", districtId)
                        .putExtra("fromDate", fromDate)
                        .putExtra("toDate", toDate)
                        .putExtra("status", 3));
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                getCount();
            }
        });
    }


    void getDistrict() {
        apiInterface.getDistrictListWithGloMapping(userSessionManager.getUserDetailsJson().getEmpCode()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    try {
                        DistrictList_Pojo districtList_pojo = new Gson().fromJson(response.body().string(), DistrictList_Pojo.class);

                        if (districtList_pojo.getStatus().equalsIgnoreCase("success")) {
                            if (districtList_pojo.getOutput().size() > 0) {
                                districtList_modelList = new ArrayList<>();
                                districtList_modelList.add(new DistrictList_Model("0", "All"));
                                districtList_modelList.addAll(districtList_pojo.getOutput());
                                showDistrictListDialog(districtList_modelList);

                            }
                        } else {
                            Utilities.showToastMessage(districtList_pojo.getMessage(), context, false);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Utilities.showToastMessage(t.getMessage(), context, false);

            }
        });

    }

    void getCount() {
        apiInterface.getCampCountApproveHolePendingCount(districtId, fromDate, toDate).enqueue(new Callback<CampStatusCountModel>() {
            @Override
            public void onResponse(Call<CampStatusCountModel> call, Response<CampStatusCountModel> response) {

                if (response.isSuccessful()) {

                    if (response.body().getStatus().equalsIgnoreCase("success")) {
                        if (response.body().getOutput().size() > 0) {
                            int approve = response.body().getOutput().get(0).getApprovedCampCount();
                            int hold = response.body().getOutput().get(0).getHoldCampCount();
                            int pending = response.body().getOutput().get(0).getPendingApproval();
                            int total = approve + pending + hold;
                            tvPendingCount.setText(String.valueOf(response.body().getOutput().get(0).getPendingApproval()));
                            tvHoldCount.setText(String.valueOf(response.body().getOutput().get(0).getHoldCampCount()));
                            tvApprovedCount.setText(String.valueOf(response.body().getOutput().get(0).getApprovedCampCount()));
                            tvTotalCount.setText(String.valueOf(total));
                            tvRejectCount.setText(String.valueOf(response.body().getOutput().get(0).getRejectedCampCount()));
                        }
                    } else {
                        Utilities.showToastMessage(response.body().getMessage(), context, false);
                    }

                }
            }

            @Override
            public void onFailure(Call<CampStatusCountModel> call, Throwable t) {
                Utilities.showToastMessage(t.getMessage(), context, false);

            }
        });

    }


    private void showDistrictListDialog(final ArrayList<DistrictList_Model> districtList) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context, R.style.CustomDialogTheme);
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
                edtSelDistrict.setText(districtList.get(which).getDISTNAME());
                districtId = districtList.get(which).getDISTLGDCODE();
                getCount();
            }
        });
        builderSingle.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCount();
    }
}