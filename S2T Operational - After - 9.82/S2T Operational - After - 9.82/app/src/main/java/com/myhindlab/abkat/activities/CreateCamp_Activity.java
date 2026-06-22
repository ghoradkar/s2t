package com.myhindlab.abkat.activities;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.pojos.Lab_OutPut_Pojo;
import com.myhindlab.abkat.pojos.Lab_Pojo;
import com.myhindlab.abkat.pojos.SelectAddress_OutPut_pojo;
import com.myhindlab.abkat.pojos.SelectAddress_pojo;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.myhindlab.abkat.utilities.Utilities.compressImage;

public class CreateCamp_Activity extends AppCompatActivity {

    private Context context;
    private UserSessionManager session;
    private static ProgressDialog pd;
    private MaterialEditText edt_district, edt_camp_address, edt_camp_name, edt_camp_date,
            edt_post_camp_date, edt_lab, edt_hospital, edt_aarogyamitra_name, edt_aarogyamitra_mobile;
    private ImageView imv_camp_approval_letter;
    private Button btn_save;
    private Uri imageURI;
    private Bitmap imagePicBm = null;
    private final int CAMERA_REQUEST = 100;

    private File patientPicsFolder;
    private String userId, distLgdCode, labCode, hospitalId, imagePath = "";
    private int mYear, mMonth, mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_camp);

        init();
        getSessionDetails();
        setDefaults();
        setEventHandler();
        setUpToolBar();
    }

    private void init() {
        context = CreateCamp_Activity.this;
        session = new UserSessionManager(context);
        pd = new ProgressDialog(context);

        edt_district = findViewById(R.id.edt_district);
        edt_camp_address = findViewById(R.id.edt_camp_address);
        edt_camp_name = findViewById(R.id.edt_camp_name);
        edt_camp_date = findViewById(R.id.edt_camp_date);
        edt_post_camp_date = findViewById(R.id.edt_post_camp_date);
        edt_lab = findViewById(R.id.edt_lab);
        edt_hospital = findViewById(R.id.edt_hospital);
        edt_aarogyamitra_name = findViewById(R.id.edt_aarogyamitra_name);
        edt_aarogyamitra_mobile = findViewById(R.id.edt_aarogyamitra_mobile);
        imv_camp_approval_letter = findViewById(R.id.imv_camp_approval_letter);
        btn_save = findViewById(R.id.btn_save);
    }

    private void getSessionDetails() {
        try {
            JSONArray user_info = new JSONArray(session.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                userId = json.getString("EmpCode");
                distLgdCode = json.getString("DISTLGDCODE");
                edt_district.setText(json.getString("district"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDefaults() {
        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        patientPicsFolder = new File(Environment.getExternalStorageDirectory() + "/Health Checkup/" + "/Camp Approval Letter/");
        if (!patientPicsFolder.exists())
            patientPicsFolder.mkdirs();
    }

    private void setEventHandler() {
        edt_camp_date.setOnClickListener(v -> {
            DatePickerDialog dpd1 = new DatePickerDialog(context,
                    (DatePickerDialog.OnDateSetListener) (view, year, monthOfYear, dayOfMonth) -> {
                        edt_camp_date.setText(Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year));

                        mYear = year;
                        mMonth = monthOfYear;
                        mDay = dayOfMonth;

                        try {
                            Date selectedDate = new SimpleDateFormat("yyyy/MM/dd").parse(edt_camp_date.getText().toString().trim());

                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(selectedDate);
                            calendar.add(Calendar.DATE, 7);

                            int postCampYear = calendar.get(Calendar.YEAR);
                            int postCampMonth = calendar.get(Calendar.MONTH);
                            int postCampDay = calendar.get(Calendar.DAY_OF_MONTH);
                            edt_post_camp_date.setText(Utilities.ConvertDateFormat(Utilities.dfDate4, postCampDay, postCampMonth + 1, postCampYear));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }, mYear, mMonth, mDay);
            dpd1.getDatePicker().setCalendarViewShown(false);
            dpd1.show();
        });

        edt_post_camp_date.setOnClickListener(v -> {

        });

        edt_lab.setOnClickListener(v -> {
            if (Utilities.isNetworkAvailable(context))
                new GetLab().execute();
            else {
                Utilities.showToastMessage("Please Check your internet connection", context, false);
            }
        });

        edt_hospital.setOnClickListener(v -> {
            if (edt_district.getText().toString().trim().isEmpty()) {
                edt_district.setError("Select district");
                return;
            }

            if (Utilities.isNetworkAvailable(context))
                new SelectHospital().execute();
            else
                Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
        });

        imv_camp_approval_letter.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{CAMERA, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 2);
                return;
            }
//            int randomEndtNo = (int) (Math.random() * 999999 + 1);
//            File patientImageFile = new File(patientPicsFolder, randomEndtNo + ".png");
//            imageURI = Uri.fromFile(patientImageFile);
//            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageURI);
//            startActivityForResult(intent, CAMERA_REQUEST);

            int randomEndtNo = (int) (Math.random() * 999999 + 1);

            ContentResolver resolver = context.getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, randomEndtNo);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
            imageURI = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageURI);
            startActivityForResult(intent, CAMERA_REQUEST);
        });

        btn_save.setOnClickListener(v -> {
            submitData();
        });
    }

    private void submitData() {
        if (edt_district.getText().toString().trim().isEmpty()) {
            edt_district.setError("Select district");
            return;
        }

        if (edt_camp_address.getText().toString().trim().isEmpty()) {
            edt_camp_address.setError("Enter camp address");
            return;
        }

        if (edt_camp_name.getText().toString().trim().isEmpty()) {
            edt_camp_name.setError("Enter camp name");
            return;
        }

        if (edt_camp_date.getText().toString().trim().isEmpty()) {
            edt_camp_date.setError("Select camp date");
            return;
        }

        if (edt_lab.getText().toString().trim().isEmpty()) {
            edt_lab.setError("Select lab");
            return;
        }

        if (edt_hospital.getText().toString().trim().isEmpty()) {
            edt_hospital.setError("Select hospital");
            return;
        }

        if (edt_aarogyamitra_name.getText().toString().trim().isEmpty()) {
            edt_aarogyamitra_name.setError("Enter aarogyamitra name");
            return;
        }

        if (edt_aarogyamitra_mobile.getText().toString().trim().isEmpty()) {
            edt_aarogyamitra_mobile.setError("Enter aarogyamitra mobile");
            return;
        }

        if (imagePath.equals("")) {
            Utilities.showToastMessage("Please click camp approval letter", context, false);
            return;
        }

        if (Utilities.isNetworkAvailable(context)) {
            new CreateCamp().execute(
                    edt_camp_name.getText().toString().trim(),
                    edt_camp_date.getText().toString().trim(),
                    edt_post_camp_date.getText().toString().trim(),
                    labCode,
                    hospitalId,
                    edt_aarogyamitra_name.getText().toString().trim(),
                    distLgdCode,
                    userId,
                    edt_camp_address.getText().toString().trim(),
                    edt_aarogyamitra_mobile.getText().toString().trim(),
                    imagePath
            );
        } else {
            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
        }

    }

    private class GetLab extends AsyncTask<String, Void, String> {

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
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            res = WebServiceCall.APICall(ApplicationConstants.GetLab, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {

                    Lab_Pojo pojoDetails = new Gson().fromJson(result, Lab_Pojo.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        ArrayList<Lab_OutPut_Pojo> labList = pojoDetails.getOutput();
                        if (labList.size() > 0) {
                            showLabListDialog(labList);
                        } else {
                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                        }
                    } else {
                        Utilities.showAlertDialog(context, "Fail", message, false);
                    }
                } else {
                    Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                }
            } catch (Exception e) {
                e.printStackTrace();

                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
            }
        }
    }

    private void showLabListDialog(final ArrayList<Lab_OutPut_Pojo> lab_List) {
        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);

        builderSingle.setTitle("Select Lab");

        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        for (Lab_OutPut_Pojo subTrenchModel : lab_List) {
            arrayAdapter.add(subTrenchModel.getLabName());
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
                labCode = lab_List.get(which).getLabCode();
                edt_lab.setText(lab_List.get(which).getLabName());
            }
        });
        builderSingle.show();
    }

    private class SelectHospital extends AsyncTask<String, Void, String> {

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
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("DISTLGDCODE", distLgdCode));
            res = WebServiceCall.APICall(ApplicationConstants.GetNearistHospitalList1, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                pd.dismiss();

                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
                    SelectAddress_pojo pojoDetails = new Gson().fromJson(result, SelectAddress_pojo.class);
                    String status = pojoDetails.getStatus();
                    String message = pojoDetails.getMessage();
                    if (status.equalsIgnoreCase("success")) {
                        ArrayList<SelectAddress_OutPut_pojo> hospitalList = pojoDetails.getOutput();
                        if (hospitalList.size() > 0) {
                            Collections.sort(hospitalList, (o1, o2) -> o1.getHospitalName().compareTo(o2.getHospitalName()));
                            showHospitalListDialog(hospitalList);
                        }
                    } else {
                        Utilities.showAlertDialog(context, status, message, false);
                    }
                } else
                    Utilities.showAlertDialog(context, "Please try again", "Server not responding.", false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void showHospitalListDialog(ArrayList<SelectAddress_OutPut_pojo> hospitalList) {
        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);

        builderSingle.setTitle("Select Hospital");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        for (SelectAddress_OutPut_pojo subTrenchModel : hospitalList) {
            arrayAdapter.add(subTrenchModel.getHospitalName());
        }
        builderSingle.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
            hospitalId = hospitalList.get(which).getHospitalId();
            edt_hospital.setText(hospitalList.get(which).getHospitalName());
        });
        builderSingle.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST) {
//                CropImage.activity(imageURI).setGuidelines(CropImageView.Guidelines.ON).start(CreateCamp_Activity.this);
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

    public void savefile(Uri sourceuri) {
        Log.i("sourceuri1", "" + sourceuri);
        String destinationFilename = "";
        String filename = (int) (Math.random() * 999999 + 1) + ".png";
//        destinationFilename = Environment.getExternalStorageDirectory() + "/Health Checkup/" + "Camp Approval Letter/" + filename;
        destinationFilename = getExternalCacheDir()+"/" + filename;

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

        imagePicBm = BitmapFactory.decodeFile(destinationFilename);
        destinationFilename = compressImage(destinationFilename);
        imagePicBm = Bitmap.createScaledBitmap(imagePicBm, 150, 150, false);
        imv_camp_approval_letter.setImageBitmap(imagePicBm);
        imagePath = destinationFilename;
    }

    private class CreateCamp extends AsyncTask<String, Integer, String> {

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

                MultipartUtility multipart = new MultipartUtility(ApplicationConstants.CW_CampCreation, "UTF-8");
                multipart.addFormField("CampName", params[0]);
                multipart.addFormField("CampDate", params[1]);
                multipart.addFormField("PostCampDate", params[2]);
                multipart.addFormField("LABCODE", params[3]);
                multipart.addFormField("AffilatedHospitalId", params[4]);
                multipart.addFormField("ArogyaMitra", params[5]);
                multipart.addFormField("Distlgdcode", params[6]);
                multipart.addFormField("UserId", params[7]);
                multipart.addFormField("CampLocation", params[8]);
                multipart.addFormField("ArogyaMitraMOB", params[9]);
                multipart.addFilePart("FileName", new File(params[10]));
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
                        builder.setMessage("Camp created successfully");
                        builder.setTitle("Success");
                        builder.setIcon(R.drawable.icon_success);
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", (dialog, id) -> finish());
                        AlertDialog alertD = builder.create();
                        alertD.show();
                    } else {
                        Utilities.showAlertDialog(context, "Fail", message, false);
                    }
                } else
                    Utilities.showAlertDialog(context, "Please try again", "Server not responding.", false);
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Please try again", "Server not responding.", false);

            }
        }

    }

    private void setUpToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Camp Creation");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(v -> finish());
    }
}

//0 = "19"
//1 = "Test Camp Name"
//2 = "2020/06/11"
//3 = "2020/06/18"
//4 = "29"
//5 = "378"
//6 = "Manish Kale"
//7 = "490"
//8 = "1219"
//9 = "29/2,Shantikunj,Somwar Peth,Pune 411011"
//10 = "8646494949"
//11 = "/storage/emulated/0/Health Checkup/Camp Approval Letter/760854.png"