package com.myhindlab.abkat.activities;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.adapters.DashboardPostCampPendingAdapter;
import com.myhindlab.abkat.models.InvoiceDashboardModel;

import java.util.List;

public class DashboardPostCampPending_Activity extends AppCompatActivity {

    private Context context;
    private RecyclerView rv_districtlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_post_camp_pending);

        init();
        setDefaults();
        setUpToolbar();
    }

    private void init() {
        context = DashboardPostCampPending_Activity.this;
        rv_districtlist = findViewById(R.id.rv_districtlist);
        rv_districtlist.setLayoutManager(new LinearLayoutManager(context));
    }

    private void setDefaults() {
        List<InvoiceDashboardModel.OutputBean> invoiceList = (List<InvoiceDashboardModel.OutputBean>) getIntent().getSerializableExtra("invoiceList");

        rv_districtlist.setAdapter(new DashboardPostCampPendingAdapter(context,
                invoiceList,
                getIntent().getStringExtra("districtId"),
                getIntent().getStringExtra("selectedMonthId"),
                getIntent().getStringExtra("selectedYearId")));
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Post Camp Pending");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}
