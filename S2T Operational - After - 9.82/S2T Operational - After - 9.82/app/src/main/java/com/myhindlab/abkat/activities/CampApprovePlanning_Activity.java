package com.myhindlab.abkat.activities;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.rengwuxian.materialedittext.MaterialEditText;

public class CampApprovePlanning_Activity extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private UserSessionManager sessionManager;
    private MaterialEditText edt_doctor1, edt_doctor2, edt_doctor3, edt_doctor4, edt_nurse1,
            edt_nurse2, edt_nurse3, edt_nurse4, edt_phlebo1, edt_phlebo2, edt_phlebo3, edt_phlebo4,
            edt_vision_technician1, edt_vision_technician2, edt_audio_technician1, edt_audio_technician2,
            edt_LFT_technician1, edt_LFT_technician2, edt_In_house_Doctor, edt_camp_coordinator;
    private Button btn_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camp_approve_planning);
        init();
        setUpToolBar();
        setDefaults();
        setEventHandler();
    }


    private void init() {
        context = CampApprovePlanning_Activity.this;
        sessionManager = new UserSessionManager(context);
        edt_doctor1 = findViewById(R.id.edt_doctor1);
        edt_doctor2 = findViewById(R.id.edt_doctor2);
        edt_doctor3 = findViewById(R.id.edt_doctor3);
        edt_doctor4 = findViewById(R.id.edt_doctor4);
        edt_nurse1 = findViewById(R.id.edt_nurse1);
        edt_nurse2 = findViewById(R.id.edt_nurse2);
        edt_nurse3 = findViewById(R.id.edt_nurse3);
        edt_nurse4 = findViewById(R.id.edt_nurse4);
        edt_phlebo1 = findViewById(R.id.edt_phlebo1);
        edt_phlebo2 = findViewById(R.id.edt_phlebo2);
        edt_phlebo3 = findViewById(R.id.edt_phlebo3);
        edt_phlebo4 = findViewById(R.id.edt_phlebo4);
        edt_vision_technician1 = findViewById(R.id.edt_vision_technician1);
        edt_vision_technician2 = findViewById(R.id.edt_vision_technician2);
        edt_audio_technician1 = findViewById(R.id.edt_audio_technician1);
        edt_audio_technician2 = findViewById(R.id.edt_audio_technician2);
        edt_LFT_technician1 = findViewById(R.id.edt_LFT_technician1);
        edt_LFT_technician2 = findViewById(R.id.edt_LFT_technician2);
        edt_In_house_Doctor = findViewById(R.id.edt_In_house_Doctor);
        edt_camp_coordinator = findViewById(R.id.edt_camp_coordinator);
        btn_save = findViewById(R.id.btn_save);
    }

    private void setUpToolBar() {

    }

    private void setDefaults() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Camp Planning");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setEventHandler() {
        edt_doctor1.setOnClickListener(this);
        edt_doctor2.setOnClickListener(this);
        edt_doctor3.setOnClickListener(this);
        edt_doctor4.setOnClickListener(this);
        edt_nurse1.setOnClickListener(this);
        edt_nurse2.setOnClickListener(this);
        edt_nurse3.setOnClickListener(this);
        edt_nurse4.setOnClickListener(this);
        edt_phlebo1.setOnClickListener(this);
        edt_phlebo2.setOnClickListener(this);
        edt_phlebo3.setOnClickListener(this);
        edt_phlebo4.setOnClickListener(this);
        edt_vision_technician1.setOnClickListener(this);
        edt_vision_technician2.setOnClickListener(this);
        edt_audio_technician1.setOnClickListener(this);
        edt_audio_technician2.setOnClickListener(this);
        edt_LFT_technician1.setOnClickListener(this);
        edt_LFT_technician2.setOnClickListener(this);
        edt_In_house_Doctor.setOnClickListener(this);
        edt_camp_coordinator.setOnClickListener(this);
        btn_save.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
