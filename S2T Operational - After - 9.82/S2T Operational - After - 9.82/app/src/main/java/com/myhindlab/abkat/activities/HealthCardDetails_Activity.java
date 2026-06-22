package com.myhindlab.abkat.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.myhindlab.abkat.R;
import com.rengwuxian.materialedittext.MaterialEditText;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.myhindlab.abkat.utilities.Utilities.provideCameraAndStorageAccess;

public class HealthCardDetails_Activity extends AppCompatActivity {

    private Context context;
    private MaterialEditText edt_workerregno;
    private ImageView imv_barcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_healthcard_details);

        init();
        setUpToolbar();
        setDefaults();
        getSessionData();
        setEventHandler();
    }

    private void init() {
        context = HealthCardDetails_Activity.this;

        edt_workerregno = findViewById(R.id.edt_workerregno);
        imv_barcode = findViewById(R.id.imv_barcode);

    }

    private void setDefaults() {

    }

    private void getSessionData() {

    }

    private void setEventHandler() {
        imv_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{CAMERA, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 1);
                    return;
                }

                Intent i = new Intent(context, BarcodeScanner_Activity.class);
                startActivityForResult(i, 10001);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 10001 && resultCode == RESULT_OK) {
                String requiredValue = data.getStringExtra("key");
                Toast.makeText(context, requiredValue, Toast.LENGTH_SHORT).show();
                edt_workerregno.setText(requiredValue);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getIntent().getStringExtra("LABEL"));

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    provideCameraAndStorageAccess(context);
                }
            }

        }
    }

}
