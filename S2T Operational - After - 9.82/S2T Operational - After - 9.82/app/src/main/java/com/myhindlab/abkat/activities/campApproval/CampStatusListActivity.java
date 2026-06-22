package com.myhindlab.abkat.activities.campApproval;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.campApproval.adapter.CampStatusListAdapter;
import com.myhindlab.abkat.activities.campApproval.models.CampStatusListModel;
import com.myhindlab.abkat.rest.ApiClient;
import com.myhindlab.abkat.rest.ApiInterface;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CampStatusListActivity extends AppCompatActivity {

    private ApiInterface apiInterface;
    private RecyclerView rvCampStatus;
    private Context mContext;
    private String fromDate, toDate, districtId = "0";
    private int status = 0;
    private TextView tvError;
    private ProgressBar progressBar;

    private UserSessionManager userSessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camp_status_list);
        setUpToolbar();
        init();
        setDefault();
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Camp List");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    void init() {
        mContext = CampStatusListActivity.this;
        userSessionManager = new UserSessionManager(mContext);
        apiInterface = ApiClient.getD2DClient().create(ApiInterface.class);
        rvCampStatus = findViewById(R.id.rvCampStatusList);
        tvError = findViewById(R.id.tvError);
        progressBar = findViewById(R.id.progressBar);
        rvCampStatus.setHasFixedSize(true);
        rvCampStatus.setLayoutManager(new LinearLayoutManager(mContext));


    }

    void setDefault() {
        if (getIntent() != null) {
            fromDate = getIntent().getStringExtra("fromDate");
            toDate = getIntent().getStringExtra("toDate");
            districtId = getIntent().getStringExtra("distCode");
            status = getIntent().getIntExtra("status", 0);
        }

//        if (userSessionManager.getUserDetailsJson().getPatnerID() == null) {
            getCampList();
//        } else {
//            getCampListChannelPartner();
//        }

    }

    void getCampList() {
        progressBar.setVisibility(View.VISIBLE);
        rvCampStatus.setVisibility(View.GONE);
        tvError.setVisibility(View.GONE);

        apiInterface.getCampListForApprove(districtId, fromDate, toDate, status).enqueue(new Callback<CampStatusListModel>() {
            @Override
            public void onResponse(Call<CampStatusListModel> call, Response<CampStatusListModel> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase("success")) {
                        if (response.body().getOutput().size() > 0) {
                            rvCampStatus.setVisibility(View.VISIBLE);
                            rvCampStatus.setAdapter(new CampStatusListAdapter(response.body().getOutput(), status));
                        }
                    } else {
                        rvCampStatus.setVisibility(View.GONE);
                        tvError.setVisibility(View.VISIBLE);
                    }
                } else {
                    rvCampStatus.setVisibility(View.GONE);
                    tvError.setVisibility(View.VISIBLE);
//                    Utilities.showToastMessage(response.body().getMessage(), mContext, false);
                }
            }

            @Override
            public void onFailure(Call<CampStatusListModel> call, Throwable t) {
                tvError.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                rvCampStatus.setVisibility(View.GONE);

                Utilities.showToastMessage(t.getMessage(), mContext, false);

            }
        });


    }

    void getCampListChannelPartner() {
        progressBar.setVisibility(View.VISIBLE);
        rvCampStatus.setVisibility(View.GONE);
        tvError.setVisibility(View.GONE);

        apiInterface.getCampListForApproveForPartner(districtId, fromDate, toDate, status).enqueue(new Callback<CampStatusListModel>() {
            @Override
            public void onResponse(Call<CampStatusListModel> call, Response<CampStatusListModel> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase("success")) {
                        if (response.body().getOutput().size() > 0) {
                            rvCampStatus.setVisibility(View.VISIBLE);
                            rvCampStatus.setAdapter(new CampStatusListAdapter(response.body().getOutput(), status));
                        }
                    } else {
                        rvCampStatus.setVisibility(View.GONE);
                        tvError.setVisibility(View.VISIBLE);
                    }
                } else {
                    rvCampStatus.setVisibility(View.GONE);
                    tvError.setVisibility(View.VISIBLE);
                    Utilities.showToastMessage(response.body().getMessage(), mContext, false);
                }
            }

            @Override
            public void onFailure(Call<CampStatusListModel> call, Throwable t) {
                tvError.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                rvCampStatus.setVisibility(View.GONE);

                Utilities.showToastMessage(t.getMessage(), mContext, false);

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
//            int fStatus = data.getIntExtra("finishStatus", 0);
//            if (fStatus == 1) {
//                finish();
//            }

            if (resultCode == 1) {
                finish();
            }
        }
    }
}