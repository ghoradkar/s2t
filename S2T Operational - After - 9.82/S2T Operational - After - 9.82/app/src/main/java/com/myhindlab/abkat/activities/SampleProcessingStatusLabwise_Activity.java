package com.myhindlab.abkat.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.adapters.SampleProcessingLabWiseAdapter;
import com.myhindlab.abkat.models.SampleProcessingLabModel;
import com.myhindlab.abkat.utilities.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class SampleProcessingStatusLabwise_Activity extends AppCompatActivity {

    private Context context;
    private RecyclerView rv_lablist;
    private TextView tv_title;

    List<SampleProcessingLabModel.OutputBean> labList;
    private int type, selectedMonthId, selectedYearId;
    private String districtCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sampleprocessing_statuslabwise);

        init();
        setDefaults();
        setEventHandlers();
        setUpToolbar();
    }

    private void init() {
        context = SampleProcessingStatusLabwise_Activity.this;
        rv_lablist = findViewById(R.id.rv_lablist);
        tv_title = findViewById(R.id.tv_title);

        labList = new ArrayList<>();

    }

    private void setDefaults() {
        rv_lablist.setLayoutManager(new LinearLayoutManager(context));

        type = getIntent().getIntExtra("type", 0);
        selectedMonthId = getIntent().getIntExtra("selectedMonthId", 0);
        selectedYearId = getIntent().getIntExtra("selectedYearId", 0);
        districtCode = getIntent().getStringExtra("districtCode");
        labList = getIntent().getParcelableArrayListExtra("labList");

        switch (type) {
            case 1:
                tv_title.setText("Completed");
                break;
            case 2:
                tv_title.setText("Pending Patients");
                break;
            case 3:
                tv_title.setText("Rejected");
                break;
        }

        rv_lablist.setAdapter(new SampleProcessingLabWiseAdapter(context, labList, type));
    }

    private void setEventHandlers() {
        rv_lablist.addOnItemTouchListener(new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                SampleProcessingLabModel.OutputBean labDetails = labList.get(position);
                if (type == 1 || type == 3) {
                    startActivity(new Intent(context, SampleProcessingCampWise_Activity.class)
                            .putExtra("labCode", labDetails.getLabCode())
                            .putExtra("type", type)
                            .putExtra("selectedMonthId", selectedMonthId)
                            .putExtra("selectedYearId", selectedYearId)
                            .putExtra("districtCode", districtCode));
                } else if (type == 2) {
                    startActivity(new Intent(context, SampleProcessingTestWise_Activity.class)
                            .putExtra("labCode", labDetails.getLabCode())
                            .putExtra("type", type)
                            .putExtra("selectedMonthId", selectedMonthId)
                            .putExtra("selectedYearId", selectedYearId)
                            .putExtra("districtCode", districtCode));

                }
            }
        }));
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Sample Processing");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
