package com.myhindlab.abkat.activities.couriermodule;

import static android.content.ContentValues.TAG;

import static com.myhindlab.abkat.utilities.PermissionUtil.PERMISSION_ALL;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.google.zxing.Result;
import com.myhindlab.abkat.R;


import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by shrik on 18-09-2017.
 */

public class BarcodeScannerZxingActivity extends Activity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;
    Context mContext = BarcodeScannerZxingActivity.this;
    String[] PERMISSIONS = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}; // List of permissions required


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       // setContentView(R.layout.activity_simple_scanner_layout);
        //ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);

        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);
        //contentFrame.addView(mScannerView);
        if(doesAppNeedPermissions()){
            askPermission();
        }
    }

    //d
    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
//        String barcodeFormat = rawResult.getBarcodeFormat().toString();
//        String result = rawResult.getText();
        Log.d(TAG, rawResult.getText()); // Prints scan results
        Log.d(TAG, rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)

        Intent intent = getIntent();
        intent.putExtra("key", rawResult.toString());
        setResult(RESULT_OK, intent);
        finish();
    }
//        if(URLUtil.isHttpUrl(result) || URLUtil.isHttpsUrl(result)){
//            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(result));
//            startActivity(browserIntent);
//        }else{
//            Toast.makeText(mContext, rawResult.toString(), Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(mContext, BarcodeTestActivity.class);
//            intent.putExtra("test", rawResult.toString());
//            startActivity(intent);
//            finish();

//        }
        //String temp;
        // If you would like to resume scanning, call this method below:
        // mScannerView.resumeCameraPreview(this);

        public static boolean doesAppNeedPermissions(){
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
        }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void askPermission() {
//        for (String permission : PERMISSIONS) {
//
//
//            if (ActivityCompat.checkSelfPermission(this, permission)!= PackageManager.PERMISSION_GRANTED) {
//                requestPermissions(PERMISSIONS, PERMISSION_ALL);
//                selectImage(1);
//                return;
//            }
//            else{
//                selectImage(1);
//            }
//
//        }


        // String[] PERMISSIONS = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}; // List of permissions required

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            requestPermissions(PERMISSIONS, PERMISSION_ALL);
            //        selectImage(1);
            return;
        }
        else{
           // selectImage(1);
        }


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {

        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED ) {
                    //Do your work.

                    //selectImage(1);
                } else {
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(mContext,R.style.AppCompatAlertDialogStyle);
                    builder.setTitle("Alert");
                    builder.setMessage("Please provide permission for Camera and Gallery");
                    //no button dialog
                    // Add the buttons
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button
                            dialog.dismiss();

                            startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.fromParts("package", getPackageName(), null)));                                   //
                            finish();
                        }
                    });
                    builder.create();
                    builder.show();
                }                }

        }
    }

    }
