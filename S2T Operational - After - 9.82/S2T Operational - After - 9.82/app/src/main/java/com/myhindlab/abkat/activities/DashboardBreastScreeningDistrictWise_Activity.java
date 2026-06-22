package com.myhindlab.abkat.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.adapters.BreastScreeningDistrictWiseAdapter;
import com.myhindlab.abkat.models.BreastScreeningDistrictWiseModel;

import java.util.List;

public class DashboardBreastScreeningDistrictWise_Activity extends AppCompatActivity {

    private Context context;
    private ProgressDialog pd;

    private RecyclerView rv_district;
    private TextView tv_count_name;
    private String fromdate, toDate, statusType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_breast_screening_district_wise);

        init();
        setDefaults();
        setUpToolBar();
    }

    private void init() {
        context = DashboardBreastScreeningDistrictWise_Activity.this;

        pd = new ProgressDialog(context);
        pd.setMessage("Please wait . . . ");
        pd.setCancelable(false);

        rv_district = findViewById(R.id.rv_district);
        tv_count_name = findViewById(R.id.tv_count_name);
        rv_district.setLayoutManager(new LinearLayoutManager(context));
    }

    private void setDefaults() {
        List<BreastScreeningDistrictWiseModel.OutputBean> districtList =
                (List<BreastScreeningDistrictWiseModel.OutputBean>) getIntent().getSerializableExtra("districtList");
        fromdate = getIntent().getStringExtra("fromdate");
        toDate = getIntent().getStringExtra("toDate");
        statusType = getIntent().getStringExtra("statusType");

        switch (statusType) {
            case "1":
                tv_count_name.setText("Positive Count");
                break;
            case "2":
                tv_count_name.setText("Negative Count");
                break;
            case "3":
                tv_count_name.setText("No Status Count");
                break;
        }
        rv_district.setAdapter(new BreastScreeningDistrictWiseAdapter(fromdate, toDate, context, districtList, statusType));
    }

    private void setUpToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Breast Screening Dashboard");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
