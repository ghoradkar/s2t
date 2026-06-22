package com.myhindlab.abkat.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.BreastDeviceModel;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.MultipartUtility;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;
import com.rengwuxian.materialedittext.MaterialEditText;
//import com.theartofdev.edmodo.cropper.CropImage;
//import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.myhindlab.abkat.utilities.Utilities.CAMERA_REQUEST;
import static com.myhindlab.abkat.utilities.Utilities.compressImage;

public class BreastScreeningDevicePhoto_Activity extends AppCompatActivity {

    private Context context;
    private ProgressDialog pd;
    private UserSessionManager session;

    private MaterialEditText edt_device;
    private ImageView imv_device_photo;
    private Button btn_save;
    private Uri photoURI;
    private Bitmap photoBm = null;

    private String userId = "", campId = "", photoImagePath = "", deviceId = "";
    private File photoFolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breast_screening_device_photo);

        init();
        getSessionData();
        setDefaults();
        setEventHandler();
        setUpToolbar();
    }

    private void init() {
        context = BreastScreeningDevicePhoto_Activity.this;
        pd = new ProgressDialog(context);
        session = new UserSessionManager(context);

        edt_device = findViewById(R.id.edt_device);
        imv_device_photo = findViewById(R.id.imv_device_photo);
        btn_save = findViewById(R.id.btn_save);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        photoFolder = new File(Environment.getExternalStorageDirectory() + "/Health Checkup/" + "Device Photo/");
        if (!photoFolder.exists())
            photoFolder.mkdirs();
    }

    private void getSessionData() {
        try {
            JSONArray user_info = new JSONArray(session.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                userId = json.getString("EmpCode");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDefaults() {
        campId = getIntent().getStringExtra("campId");
    }

    private void setEventHandler() {
        edt_device.setOnClickListener(v -> {
            if (Utilities.isNetworkAvailable(context)) {
                new GetDeviceList().execute();
            } else {
                Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
            }
        });

        imv_device_photo.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{CAMERA, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 2);
                return;
            }
//            File patientImageFile = new File(photoFolder, userId + "_" + campId + "_" + ".png");
//            photoURI = Uri.fromFile(patientImageFile);
//            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//            startActivityForResult(intent, CAMERA_REQUEST);

            photoFolder=getExternalCacheDir();
            if (!photoFolder.exists())
                photoFolder.mkdirs();

            int randomEndtNo = (int) (Math.random() * 99999 + 1);
//        File patientImageFile = new File(letterPicsFolder, randomEndtNo + ".png");
//        letterURI = Uri.fromFile(patientImageFile);
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, letterURI);
//        startActivityForResult(intent, LETTER_CAMERA_REQUEST);

            ContentResolver resolver = context.getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, randomEndtNo + ".png");
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
            photoURI = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(intent, CAMERA_REQUEST);
        });


        btn_save.setOnClickListener(v -> {
            if (edt_device.getText().toString().trim().isEmpty()) {
                edt_device.setError("Please select device");
                return;
            }

            if (photoBm == null) {
                Utilities.showToastMessage("Please pick device photo", context, false);
                return;
            }

            if (Utilities.isNetworkAvailable(context))
                new UploadDevicePhoto().execute(
                        deviceId,
                        campId,
                        userId,
                        userId,
                        photoImagePath
                );
            else
                Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST) {
//                CropImage.activity(photoURI).setGuidelines(CropImageView.Guidelines.ON).start(BreastScreeningDevicePhoto_Activity.this);
            }
        }

//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//            if (resultCode == RESULT_OK) {
//                Uri resultUri = result.getUri();
//                savefile(resultUri);
//            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                Exception error = result.getError();
//            }
//        }
    }

    private class GetDeviceList extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Please wait . . . ");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "";
            List<ParamsPojo> param = new ArrayList<>();

            res = WebServiceCall.APICall(ApplicationConstants.GetDeviceList, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    List<BreastDeviceModel.OutputBean> deviceList = new ArrayList<>();
                    BreastDeviceModel pojoDetails = new Gson().fromJson(result, BreastDeviceModel.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        deviceList = pojoDetails.getOutput();
                        if (deviceList.size() > 0) {
                            showDeviceListDialog(deviceList);
                        } else {
                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                        }
                    } else {
                        Utilities.showAlertDialog(context, "Fail", message, false);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
            }
        }
    }

    public void savefile(Uri sourceuri) {
        Log.i("sourceuri1", "" + sourceuri);
        String destinationFilename = "";
        String filename = userId + "_" + campId + "_" + ".png";
//        destinationFilename = Environment.getExternalStorageDirectory() + "/Health Checkup/" + "Device Photo/" + filename;
        destinationFilename = getExternalCacheDir()+"/" + filename + ".png";

        String sourceFilename = sourceuri.getPath();
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        try {
            bis = new BufferedInputStream(new FileInputStream(sourceFilename));
            bos = new BufferedOutputStream(new FileOutputStream(destinationFilename, false));
            byte[] buf = new byte[1024];
            bis.read(buf);
            do {
                bos.write(buf);
            } while (bis.read(buf) != -1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) bis.close();
                if (bos != null) bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        photoBm = BitmapFactory.decodeFile(destinationFilename);
        destinationFilename = compressImage(destinationFilename);
        photoBm = Bitmap.createScaledBitmap(photoBm, 150, 150, false);
        imv_device_photo.setImageBitmap(photoBm);
        photoImagePath = destinationFilename;
    }

    private class UploadDevicePhoto extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Please Wait . . .");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "";
            try {

                MultipartUtility multipart = new MultipartUtility(ApplicationConstants.InsertBreastSceeningDeviceDetails, "UTF-8");
                multipart.addFormField("DeviceId", params[0]);
                multipart.addFormField("CampId", params[1]);
                multipart.addFormField("UserId", params[2]);
                multipart.addFormField("CreatedBy", params[3]);
                multipart.addFilePart("ImagePath", new File(params[4]));
                List<String> response = multipart.finish();
                for (String line : response) {
                    res = res + line;
                }
                return res;
            } catch (IOException ex) {
                return ex.toString();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            try {
                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
                    JSONObject obj1 = new JSONObject(result);
                    String status = obj1.getString("status");
                    String message = obj1.getString("message");

                    if (status.equalsIgnoreCase("Success")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage("Device Uploaded Successfully!");
                        builder.setTitle("Success");
                        builder.setIcon(R.drawable.icon_success);
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", (dialog, id) -> {
                            finish();
                        });
                        AlertDialog alertD = builder.create();
                        alertD.show();
                    } else {
                        Utilities.showAlertDialog(context, "Fail", message, false);
                    }
                } else
                    Utilities.showAlertDialog(context,
                            "Please try again", "Server not responding.", false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void showDeviceListDialog(final List<BreastDeviceModel.OutputBean> deviceList) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        builderSingle.setTitle("Select Machine");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        for (int i = 0; i < deviceList.size(); i++) {
            arrayAdapter.add(String.valueOf(deviceList.get(i).getDeviceName()));
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
                deviceId = deviceList.get(which).getDeviceId();
                edt_device.setText(deviceList.get(which).getDeviceName());
            }
        });
        builderSingle.show();
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Upload Device Photo");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
