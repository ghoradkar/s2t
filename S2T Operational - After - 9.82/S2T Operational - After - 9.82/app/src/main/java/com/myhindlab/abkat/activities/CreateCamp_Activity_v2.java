package com.myhindlab.abkat.activities;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
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
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.ScreeningTestModel;
import com.myhindlab.abkat.models.ServiceGroupModel;
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

public class CreateCamp_Activity_v2 extends AppCompatActivity {

    private Context context;
    private UserSessionManager session;
    private static ProgressDialog pd;
    private MaterialEditText edt_district, edt_camp_address, edt_camp_name, edt_camp_date,
            edt_post_camp_date, edt_lab, edt_hospital, edt_aarogyamitra_name, edt_aarogyamitra_mobile, edt_screening_tests,
            edt_lab_1, edt_tests_for_lab_1, edt_lab_2, edt_tests_for_lab_2;
    private ImageView imv_camp_approval_letter;
    private Button btn_save;
    private Uri imageURI;
    private final int CAMERA_REQUEST = 100;
    private List<ScreeningTestModel.OutputBean> screeningTestList;
    private List<ServiceGroupModel.OutputBean> serviceLabOneList, serviceLabTwoList;
    private ArrayList<Lab_OutPut_Pojo> labList;

    private File patientPicsFolder;
    private String userId, distLgdCode, labCode, labCodeOne, labCodeTwo, hospitalId, imagePath = "";
    private int mYear, mMonth, mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_camp_v2);

        init();
        getSessionDetails();
        setDefaults();
        setEventHandler();
        setUpToolBar();
    }

    private void init() {
        context = CreateCamp_Activity_v2.this;
        session = new UserSessionManager(context);
        pd = new ProgressDialog(context);

        edt_district = findViewById(R.id.edt_district);
        edt_camp_address = findViewById(R.id.edt_camp_address);
        edt_camp_name = findViewById(R.id.edt_camp_name);
        edt_camp_date = findViewById(R.id.edt_camp_date);
        edt_post_camp_date = findViewById(R.id.edt_post_camp_date);
        edt_lab = findViewById(R.id.edt_lab);
        edt_hospital = findViewById(R.id.edt_hospital);
        edt_screening_tests = findViewById(R.id.edt_screening_tests);
        edt_lab_1 = findViewById(R.id.edt_lab_1);
        edt_tests_for_lab_1 = findViewById(R.id.edt_tests_for_lab_1);
        edt_lab_2 = findViewById(R.id.edt_lab_2);
        edt_tests_for_lab_2 = findViewById(R.id.edt_tests_for_lab_2);
        edt_aarogyamitra_name = findViewById(R.id.edt_aarogyamitra_name);
        edt_aarogyamitra_mobile = findViewById(R.id.edt_aarogyamitra_mobile);
        imv_camp_approval_letter = findViewById(R.id.imv_camp_approval_letter);
        btn_save = findViewById(R.id.btn_save);

        screeningTestList = new ArrayList<>();
        serviceLabOneList = new ArrayList<>();
        serviceLabTwoList = new ArrayList<>();

        labList = new ArrayList<>();
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
            try {
                dpd1.getDatePicker().setCalendarViewShown(false);
                dpd1.getDatePicker().setMinDate(System.currentTimeMillis());
            } catch (Exception e) {
                e.printStackTrace();
            }
            dpd1.show();
        });

        edt_post_camp_date.setOnClickListener(v -> {

        });

//        edt_lab.setOnClickListener(v -> {
//            if (labList.size() == 0) {
//                if (Utilities.isNetworkAvailable(context))
//                    new GetLab().execute();
//                else {
//                    Utilities.showToastMessage("Please Check your internet connection", context, false);
//                }
//            } else {
//                showLabListDialog();
//            }
//        });

        edt_lab_1.setOnClickListener(v -> {
            if (labList.size() == 0) {
                if (Utilities.isNetworkAvailable(context))
                    new GetLab().execute();
                else {
                    Utilities.showToastMessage("Please Check your internet connection", context, false);
                }
            } else {
                showLabListDialogOne();
            }
        });

        edt_lab_2.setOnClickListener(v -> {
            if (edt_lab_1.getText().toString().trim().equals("")) {
                edt_lab_1.setError("Select Lab 1");
                return;
            }

            if (edt_tests_for_lab_1.getText().toString().trim().equals("")) {
                edt_tests_for_lab_1.setError("Select Tests for Mapping - Lab 1");
                return;
            }

            int isSelected = 0;

            for (int i = 0; i < serviceLabOneList.size(); i++)
                if (serviceLabOneList.get(i).isCheckedInListOne())
                    isSelected = isSelected + 1;

            if (serviceLabOneList.size() == isSelected) {
                Utilities.showToastMessage("All test groups are mapped to lab 1", context, true);
                return;
            }

            ArrayList<Lab_OutPut_Pojo> labListTwo = new ArrayList<>();
            for (int i = 0; i < labList.size(); i++) {
                if (!labList.get(i).getLabCode().equals(labCodeOne))
                    labListTwo.add(labList.get(i));
            }

            showLabListDialogTwo(labListTwo);
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

        edt_tests_for_lab_1.setOnClickListener(v -> {
            if (serviceLabOneList.size() == 0) {
                if (Utilities.isNetworkAvailable(context))
                    new GetServiceGroupList().execute();
                else {
                    Utilities.showToastMessage("Please Check your internet connection", context, false);
                }
            } else {
                showServiceGroupDialogOne();
            }
        });

        edt_tests_for_lab_2.setOnClickListener(v -> {

            if (edt_tests_for_lab_1.getText().toString().trim().equals("")) {
                edt_tests_for_lab_1.setError("Select Tests for Mapping - Lab 1");
                return;
            }

            serviceLabTwoList = new ArrayList<>();

            List<ServiceGroupModel.OutputBean> serviceLabOneListDummy = new ArrayList<>(serviceLabOneList);

            for (int i = 0; i < serviceLabOneListDummy.size(); i++)
                if (!serviceLabOneListDummy.get(i).isCheckedInListOne())
                    serviceLabTwoList.add(serviceLabOneListDummy.get(i));

            if (serviceLabTwoList.size() != 0)
                showServiceGroupDialogTwo();
            else
                Utilities.showToastMessage("All test groups are mapped to lab 1", context, true);
        });

        edt_screening_tests.setOnClickListener(v -> {
            if (screeningTestList.size() == 0) {
                if (Utilities.isNetworkAvailable(context))
                    new GetScreeningTest().execute();
                else {
                    Utilities.showToastMessage("Please Check your internet connection", context, false);
                }
            } else {
                showScreeningTestDialog();
            }
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

//        if (edt_lab.getText().toString().trim().isEmpty()) {
//            edt_lab.setError("Select lab");
//            return;
//        }

        if (edt_hospital.getText().toString().trim().isEmpty()) {
            edt_hospital.setError("Select hospital");
            return;
        }

        if (edt_screening_tests.getText().toString().trim().isEmpty()) {
            edt_screening_tests.setError("Select screening tests");
            return;
        }

        if (edt_lab_1.getText().toString().trim().equals("")) {
            edt_lab_1.setError("Select Lab 1");
            return;
        }

        if (edt_tests_for_lab_1.getText().toString().trim().equals("")) {
            edt_tests_for_lab_1.setError("Select Tests for Mapping - Lab 1");
            return;
        }

        if (!edt_tests_for_lab_2.getText().toString().trim().equals(""))
            if (edt_lab_2.getText().toString().trim().equals("")) {
                edt_lab_2.setError("Select Lab 2");
                return;
            }
//
//        if (edt_tests_for_lab_2.getText().toString().trim().equals("")) {
//            edt_tests_for_lab_2.setError("Select Tests for Mapping - Lab 2");
//            return;
//        }
        int servicesSelectedCount = 0;

        for (ServiceGroupModel.OutputBean serviceDetails : serviceLabOneList) {
            if (serviceDetails.isCheckedInListOne()) {
                servicesSelectedCount = servicesSelectedCount + 1;
            }
        }

        for (ServiceGroupModel.OutputBean serviceDetails : serviceLabTwoList) {
            if (serviceDetails.isCheckedInListTwo()) {
                servicesSelectedCount = servicesSelectedCount + 1;
            }
        }

        if (servicesSelectedCount != serviceLabOneList.size()) {
            Utilities.showAlertDialog(context, "Alert", serviceLabOneList.size() - servicesSelectedCount + " test group mapping is pending. Please map all test groups to lab", false);
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


        JsonArray screeningTestJsonArray = new JsonArray();
        JsonArray serviceTestLabJsonArray = new JsonArray();

        for (ScreeningTestModel.OutputBean screeningDetails : screeningTestList) {
            JsonObject screeningTestJsonObj = new JsonObject();
            screeningTestJsonObj.addProperty("CampID", "0");
            screeningTestJsonObj.addProperty("TestID", screeningDetails.getTestId());
            if (screeningDetails.isChecked()) {
                screeningTestJsonObj.addProperty("IsTestProcess", "1");
            } else {
                screeningTestJsonObj.addProperty("IsTestProcess", "0");
            }
            screeningTestJsonObj.addProperty("IsActive", "1");
            screeningTestJsonObj.addProperty("CreatedBy", userId);
            screeningTestJsonArray.add(screeningTestJsonObj);
        }

        for (ServiceGroupModel.OutputBean serviceDetails : serviceLabOneList) {
            if (serviceDetails.isCheckedInListOne()) {
                JsonObject serviceTestJsonObj = new JsonObject();
                serviceTestJsonObj.addProperty("Labcode", labCodeOne);
                serviceTestJsonObj.addProperty("Groupid", serviceDetails.getGroupID());
                serviceTestJsonObj.addProperty("IsActive", "1");
                serviceTestLabJsonArray.add(serviceTestJsonObj);
            }
        }

        for (ServiceGroupModel.OutputBean serviceDetails : serviceLabTwoList) {
            if (serviceDetails.isCheckedInListTwo()) {
                JsonObject serviceTestJsonObj = new JsonObject();
                serviceTestJsonObj.addProperty("Labcode", labCodeTwo);
                serviceTestJsonObj.addProperty("Groupid", serviceDetails.getGroupID());
                serviceTestJsonObj.addProperty("IsActive", "1");
                serviceTestLabJsonArray.add(serviceTestJsonObj);
            }
        }

        if (Utilities.isNetworkAvailable(context)) {
            new CreateCamp().execute(
                    edt_camp_name.getText().toString().trim(),
                    edt_camp_date.getText().toString().trim(),
                    edt_post_camp_date.getText().toString().trim(),
                    labCodeOne,
                    hospitalId,
                    edt_aarogyamitra_name.getText().toString().trim(),
                    distLgdCode,
                    userId,
                    edt_camp_address.getText().toString().trim(),
                    edt_aarogyamitra_mobile.getText().toString().trim(),
                    screeningTestJsonArray.toString(),
                    serviceTestLabJsonArray.toString(),
                    imagePath
            );
        } else {
            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
        }

    }

    //////////////////////////////////// Lab, Lab 1 and Lab 2 ////////////////////////////////////

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
                        labList = pojoDetails.getOutput();
                        if (labList.size() > 0) {
                            showLabListDialogOne();
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

    private void showLabListDialog() {
        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);

        builderSingle.setTitle("Select Lab");

        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        for (Lab_OutPut_Pojo subTrenchModel : labList) {
            arrayAdapter.add(subTrenchModel.getLabName());
        }
        builderSingle.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
            labCode = labList.get(which).getLabCode();
            edt_lab.setText(labList.get(which).getLabName());
        });
        builderSingle.show();
    }

    private void showLabListDialogOne() {
        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);

        builderSingle.setTitle("Select Lab 1");

        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        for (Lab_OutPut_Pojo subTrenchModel : labList) {
            arrayAdapter.add(subTrenchModel.getLabName());
        }
        builderSingle.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
            labCodeOne = labList.get(which).getLabCode();
            edt_lab_1.setText(labList.get(which).getLabName());
            edt_lab_2.setText("");
        });
        builderSingle.show();
    }

    private void showLabListDialogTwo(ArrayList<Lab_OutPut_Pojo> labList) {
        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);

        builderSingle.setTitle("Select Lab 2");

        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        for (Lab_OutPut_Pojo subTrenchModel : labList) {
            arrayAdapter.add(subTrenchModel.getLabName());
        }
        builderSingle.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
            labCodeTwo = labList.get(which).getLabCode();
            edt_lab_2.setText(labList.get(which).getLabName());
        });
        builderSingle.show();
    }

    //////////////////////////////////// Hospital ////////////////////////////////////////////////

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

    //////////////////////////////////// Screening Test //////////////////////////////////////////

    private class GetScreeningTest extends AsyncTask<String, Void, String> {

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
            res = WebServiceCall.APICall(ApplicationConstants.getTestList, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                pd.dismiss();

                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
                    ScreeningTestModel pojoDetails = new Gson().fromJson(result, ScreeningTestModel.class);
                    String status = pojoDetails.getStatus();
                    String message = pojoDetails.getMessage();
                    if (status.equalsIgnoreCase("success")) {
                        screeningTestList = pojoDetails.getOutput();
                        if (screeningTestList.size() > 0) {
                            showScreeningTestDialog();
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

    private void showScreeningTestDialog() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_check_list, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        builder.setTitle("Select Screening Test");
        builder.setCancelable(false);

        RecyclerView rv_checklist = view.findViewById(R.id.rv_checklist);
        rv_checklist.setLayoutManager(new LinearLayoutManager(context));

        for (int i = 0; i < screeningTestList.size(); i++)
            if (screeningTestList.get(i).getIsCompulsary().equals("1"))
                screeningTestList.get(i).setChecked(true);

        rv_checklist.setAdapter(new ScreeningTestsAdapter());

        builder.setPositiveButton("Select", (dialog, which) -> {
            edt_screening_tests.setText("");

            StringBuilder selectedSubCategories = new StringBuilder();

            for (ScreeningTestModel.OutputBean sample : screeningTestList) {
                if (sample.isChecked()) {
                    selectedSubCategories.append(sample.getTestName()).append(", ");
                }
            }

            if (selectedSubCategories.toString().length() != 0) {
                String selectedLabsStr = selectedSubCategories.substring(0, selectedSubCategories.toString().length() - 2);
                edt_screening_tests.setText(selectedLabsStr);
            }

        });

        builder.create().show();
    }

    private class ScreeningTestsAdapter extends RecyclerView.Adapter<ScreeningTestsAdapter.MyViewHolder> {

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.list_row_checklist, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int pos) {
            final int position = holder.getAdapterPosition();

            holder.cb_select.setText(screeningTestList.get(position).getTestName());

            if (screeningTestList.get(position).isChecked()) {
                holder.cb_select.setChecked(true);
            }

            if (screeningTestList.get(position).getIsCompulsary().equals("0"))
                holder.cb_select.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    screeningTestList.get(position).setChecked(isChecked);
                });
            else
                holder.cb_select.setClickable(false);
        }

        @Override
        public int getItemCount() {
            return screeningTestList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            private CheckBox cb_select;

            public MyViewHolder(@NonNull View view) {
                super(view);
                cb_select = view.findViewById(R.id.cb_select);
            }
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }
    }

    //////////////////////////////////// Service Group Mapping //////////////////////////////////////////

    private class GetServiceGroupList extends AsyncTask<String, Void, String> {

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
            res = WebServiceCall.APICall(ApplicationConstants.getServiceGroupList, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                pd.dismiss();
                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
                    ServiceGroupModel pojoDetails = new Gson().fromJson(result, ServiceGroupModel.class);
                    String status = pojoDetails.getStatus();
                    String message = pojoDetails.getMessage();
                    if (status.equalsIgnoreCase("success")) {
                        serviceLabOneList = pojoDetails.getOutput();
                        if (serviceLabOneList.size() > 0) {
                            showServiceGroupDialogOne();
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

    private void showServiceGroupDialogOne() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_check_list, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        builder.setTitle("Select Tests for Mapping - Lab 1");
        builder.setCancelable(false);

        RecyclerView rv_checklist = view.findViewById(R.id.rv_checklist);
        rv_checklist.setLayoutManager(new LinearLayoutManager(context));
        rv_checklist.setAdapter(new ServiceGroupAdapterOne());

        builder.setPositiveButton("Select", (dialog, which) -> {
            edt_tests_for_lab_1.setText("");
            edt_tests_for_lab_2.setText("");

            int checkedTestCount = 0;
            StringBuilder selectedSubCategories = new StringBuilder();

            for (ServiceGroupModel.OutputBean sample : serviceLabOneList) {
                if (sample.isCheckedInListOne()) {
                    selectedSubCategories.append(sample.getGroupName()).append(", ");
                    checkedTestCount = checkedTestCount + 1;
                }
            }

            if (serviceLabOneList.size() == checkedTestCount)
                edt_lab_2.setText("");

            if (selectedSubCategories.toString().length() != 0) {
                String selectedLabsStr = selectedSubCategories.substring(0, selectedSubCategories.toString().length() - 2);
                edt_tests_for_lab_1.setText(selectedLabsStr);
            }

            for (int i = 0; i < serviceLabTwoList.size(); i++) {
                serviceLabTwoList.get(i).setCheckedInListTwo(false);
            }

        });

        builder.create().show();
    }

    private class ServiceGroupAdapterOne extends RecyclerView.Adapter<ServiceGroupAdapterOne.MyViewHolder> {

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.list_row_checklist, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int pos) {
            final int position = holder.getAdapterPosition();

            holder.cb_select.setText(serviceLabOneList.get(position).getGroupName());

            if (serviceLabOneList.get(position).isCheckedInListOne()) {
                holder.cb_select.setChecked(true);
            }

            holder.cb_select.setOnCheckedChangeListener((buttonView, isChecked) -> {
                serviceLabOneList.get(position).setCheckedInListOne(isChecked);
            });
        }

        @Override
        public int getItemCount() {
            return serviceLabOneList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            private CheckBox cb_select;

            public MyViewHolder(@NonNull View view) {
                super(view);
                cb_select = view.findViewById(R.id.cb_select);
            }
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }
    }

    private void showServiceGroupDialogTwo() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_check_list, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        builder.setTitle("Select Tests for Mapping - Lab 2");
        builder.setCancelable(false);

        RecyclerView rv_checklist = view.findViewById(R.id.rv_checklist);
        rv_checklist.setLayoutManager(new LinearLayoutManager(context));
        rv_checklist.setAdapter(new ServiceGroupAdapterTwo());

        builder.setPositiveButton("Select", (dialog, which) -> {
            edt_tests_for_lab_2.setText("");

            StringBuilder selectedSubCategories = new StringBuilder();

            for (ServiceGroupModel.OutputBean sample : serviceLabTwoList) {
                if (sample.isCheckedInListTwo()) {
                    selectedSubCategories.append(sample.getGroupName()).append(", ");
                }
            }

            if (selectedSubCategories.toString().length() != 0) {
                String selectedLabsStr = selectedSubCategories.substring(0, selectedSubCategories.toString().length() - 2);
                edt_tests_for_lab_2.setText(selectedLabsStr);
            }
        });

        builder.create().show();
    }

    private class ServiceGroupAdapterTwo extends RecyclerView.Adapter<ServiceGroupAdapterTwo.MyViewHolder> {

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.list_row_checklist, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int pos) {
            final int position = holder.getAdapterPosition();

            holder.cb_select.setText(serviceLabTwoList.get(position).getGroupName());

            if (serviceLabTwoList.get(position).isCheckedInListTwo()) {
                holder.cb_select.setChecked(true);
            }

            holder.cb_select.setOnCheckedChangeListener((buttonView, isChecked) -> {
                serviceLabTwoList.get(position).setCheckedInListTwo(isChecked);
            });
        }

        @Override
        public int getItemCount() {
            return serviceLabTwoList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            private CheckBox cb_select;

            public MyViewHolder(@NonNull View view) {
                super(view);
                cb_select = view.findViewById(R.id.cb_select);
            }
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST) {
//                CropImage.activity(imageURI).setGuidelines(CropImageView.Guidelines.ON).start(CreateCamp_Activity_v2.this);
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

        Bitmap imagePicBm = BitmapFactory.decodeFile(destinationFilename);
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

                MultipartUtility multipart = new MultipartUtility(ApplicationConstants.CW_CampCreationNew, "UTF-8");
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
                multipart.addFormField("CampTestMapping", params[10]);
                multipart.addFormField("CampLabMapping", params[11]);
                multipart.addFilePart("FileName", new File(params[12]));
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