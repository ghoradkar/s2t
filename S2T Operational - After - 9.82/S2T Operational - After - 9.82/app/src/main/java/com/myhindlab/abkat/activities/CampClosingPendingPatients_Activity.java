package com.myhindlab.abkat.activities;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.adapters.CampClosingPendingPatientsAdapter;
import com.myhindlab.abkat.models.PatientStatusModel;
import com.myhindlab.abkat.utilities.UserSessionManager;

import java.util.ArrayList;

public class CampClosingPendingPatients_Activity extends AppCompatActivity {

    private Context context;
    private UserSessionManager session;

    private RecyclerView rv_beneficiary;
    private ArrayList<PatientStatusModel> patientList;
    private int mYear, mMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camp_closing_pending_patients);

        init();
        getSessionDetails();
        setDefaults();
        setEventHandler();
        setUpToolBar();
    }

    private void init() {
        context = CampClosingPendingPatients_Activity.this;
        session = new UserSessionManager(context);

        rv_beneficiary = findViewById(R.id.rv_beneficiary);
        rv_beneficiary.setLayoutManager(new LinearLayoutManager(context));
    }

    private void getSessionDetails() {
    }

    private void setDefaults() {
        mYear = getIntent().getIntExtra("mYear", 0);
        mMonth = getIntent().getIntExtra("mMonth", 0);

        patientList = (ArrayList<PatientStatusModel>) getIntent().getSerializableExtra("patientList");
        rv_beneficiary.setAdapter(new CampClosingPendingPatientsAdapter(context, patientList, mMonth, mYear));
    }

    private void setEventHandler() {
    }

    private void setUpToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Pending Beneficiary");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(v -> finish());
    }
}
