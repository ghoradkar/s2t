package com.myhindlab.abkat.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.developer.filepicker.model.DialogConfigs;
import com.developer.filepicker.model.DialogProperties;
import com.developer.filepicker.view.FilePickerDialog;
import com.google.gson.Gson;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.BreastDeviceModel;
import com.myhindlab.abkat.models.BreastScreeningExaminarNoteModel;
import com.myhindlab.abkat.models.PresentPatientList_Model;
import com.myhindlab.abkat.models.QuadrantListModel;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.MultipartUtility;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HealthScreeningBreast_Activity_v3 extends AppCompatActivity {

    private Context context;
    private UserSessionManager session;
    private ProgressDialog pd;
    private TextView tv_files;
    private MaterialEditText edt_beneficiaryname, edt_gender, edt_age, edt_height, edt_weight, edt_machine, edt_family_history,
            edt_comment, edt_final_remark, edt_examiner_note, edt_left_quadrant, edt_right_quadrant;
    private LinearLayout ll_quadrant;
    private RadioButton rb_observation_yes, rb_observation_no, rb_lumb_yes, rb_lumb_no;
    private RadioGroup rg_observation, rg_lumb;
    private Button btn_choose_file, btn_register, btn_machine;
    private PresentPatientList_Model patientDetails;
    private String userID, name, campId, healthScreentype, pdfPath = "", pngPath = "", csvPath = "", breastScreeningDeviceId, examinerNoteId;

    List<QuadrantListModel.OutputBean> quadrantList = new ArrayList<>();
    private FilePickerDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_healthscreening_breast_v3);

        init();
        setUpToolbar();
        setDefaults();
        getSessionData();
        setEventHandler();
    }

    private void init() {
        context = HealthScreeningBreast_Activity_v3.this;
        session = new UserSessionManager(context);
        pd = new ProgressDialog(context);

        edt_beneficiaryname = findViewById(R.id.edt_beneficiaryname);
        edt_gender = findViewById(R.id.edt_gender);
        edt_age = findViewById(R.id.edt_age);
        edt_height = findViewById(R.id.edt_height);
        edt_machine = findViewById(R.id.edt_machine);
        edt_weight = findViewById(R.id.edt_weight);
        edt_family_history = findViewById(R.id.edt_family_history);
        edt_comment = findViewById(R.id.edt_comment);
        edt_final_remark = findViewById(R.id.edt_final_remark);
        edt_examiner_note = findViewById(R.id.edt_examiner_note);
        edt_left_quadrant = findViewById(R.id.edt_left_quadrant);
        edt_right_quadrant = findViewById(R.id.edt_right_quadrant);
        tv_files = findViewById(R.id.tv_files);
        btn_choose_file = findViewById(R.id.btn_choose_file);
        rb_observation_yes = findViewById(R.id.rb_observation_yes);
        rb_observation_no = findViewById(R.id.rb_observation_no);
        rb_lumb_yes = findViewById(R.id.rb_lumb_yes);
        rb_lumb_no = findViewById(R.id.rb_lumb_no);
        rg_observation = findViewById(R.id.rg_observation);
        rg_lumb = findViewById(R.id.rg_lumb);
        ll_quadrant = findViewById(R.id.ll_quadrant);
        btn_machine = findViewById(R.id.btn_machine);

        btn_register = findViewById(R.id.btn_register);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        getDeviceFromSession();
    }

    private void setDefaults() {

        patientDetails = (PresentPatientList_Model) getIntent().getSerializableExtra("patientDetails");
        campId = getIntent().getStringExtra("campId");
        healthScreentype = getIntent().getStringExtra("healthScreentype");

        breastScreeningDeviceId = getIntent().getStringExtra("breastScreeningDeviceId");
        edt_machine.setText(getIntent().getStringExtra("breastScreeningDeviceName"));

        edt_beneficiaryname.setText(patientDetails.getEnglishName());
        edt_age.setText(patientDetails.getAge());
        edt_height.setText(String.valueOf(patientDetails.getHeightCMs()));
        edt_weight.setText(String.valueOf(patientDetails.getWeightKGs()));

        if (patientDetails.getGender().equalsIgnoreCase("M")) {
            edt_gender.setText("Male");
        } else if (patientDetails.getGender().equalsIgnoreCase("F")) {
            edt_gender.setText("Female");
        } else if (patientDetails.getGender().equalsIgnoreCase("O")) {
            edt_gender.setText("Other");
        }


        final DialogProperties properties = new DialogProperties();

        dialog = new FilePickerDialog(context, properties);
        dialog.setTitle("Select a File");
        dialog.setPositiveBtnName("Select");
        dialog.setNegativeBtnName("Cancel");
        String fextension = "csv,png,pdf";
        //Add extensions to be sorted from the EditText input to the array of String.
        int commas = countCommas(fextension);

        //Array representing extensions.
        String[] exts = new String[commas + 1];
        StringBuffer buff = new StringBuffer();
        int i = 0;
        for (int j = 0; j < fextension.length(); j++) {
            if (fextension.charAt(j) == ',') {
                exts[i] = buff.toString();
                buff = new StringBuffer();
                i++;
            } else {
                buff.append(fextension.charAt(j));
            }
        }
        exts[i] = buff.toString();

        //Set String Array of extensions.
        properties.extensions = exts;
        String foffset = "/sdcard/IBE";
        if (foffset.length() > 0 || !foffset.equals("")) {
            //Setting Parent Directory.
            properties.root = new File(foffset);
        } else {
            //Setting Parent Directory to Default SDCARD.
            properties.root = new File(DialogConfigs.DEFAULT_DIR);
        }
        properties.selection_mode = DialogConfigs.MULTI_MODE;
        properties.selection_type = DialogConfigs.FILE_SELECT;

//            properties.offset = new File(DialogConfigs.DEFAULT_DIR);

        properties.show_hidden_files = true;

        //Setting Alternative Directory, in case root is not accessible.This will be
        //used.

        properties.error_dir = new File("/mnt");
        //Set new properties of dialog.
        dialog.setProperties(properties);

    }

    private int countCommas(String fextension) {
        int count = 0;
        for (char ch : fextension.toCharArray()) {
            if (ch == ',') count++;
        }
        return count;
    }

    private void getDeviceFromSession() {
    }

    private void getSessionData() {
        try {
            JSONArray user_info = new JSONArray(session.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                userID = json.getString("EmpCode");
                name = json.getString("name");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setEventHandler() {
        rg_observation.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_observation_yes:
                    edt_comment.setVisibility(View.VISIBLE);
                    ll_quadrant.setVisibility(View.VISIBLE);
                    edt_comment.setText("");
                    edt_left_quadrant.setText("");
                    edt_right_quadrant.setText("");
                    break;

                case R.id.rb_observation_no:
                    edt_comment.setVisibility(View.GONE);
                    ll_quadrant.setVisibility(View.GONE);
                    edt_comment.setText("");
                    edt_left_quadrant.setText("");
                    edt_right_quadrant.setText("");
                    break;
            }
        });

        btn_register.setOnClickListener(view -> submitData());

        btn_choose_file.setOnClickListener(v -> dialog.show());

        dialog.setDialogSelectionListener(files -> {
            tv_files.setText("");
            csvPath = "";
            pngPath = "";
            pdfPath = "";
            tv_files.setText("");


            StringBuilder stringBuilder = new StringBuilder();
            for (String path : files) {
                File file = new File(path);
                if (file.getAbsolutePath().contains("csv")) {
                    csvPath = file.getAbsolutePath();
                    stringBuilder.append("CSV File - " + file.getAbsolutePath() + "\n\n");
                } else if (file.getAbsolutePath().contains("png")) {
                    pngPath = file.getAbsolutePath();
                    stringBuilder.append("PNG File - " + file.getAbsolutePath() + "\n\n");
                } else if (file.getAbsolutePath().contains("pdf")) {
                    pdfPath = file.getAbsolutePath();
                    stringBuilder.append("PDF File - " + file.getAbsolutePath() + "\n\n");
                }
            }

            tv_files.setText(stringBuilder.toString());
        });

        btn_machine.setOnClickListener(v -> {
            if (Utilities.isNetworkAvailable(context)) {
                new GetDeviceList().execute();
            } else {
                Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
            }
        });

        rb_lumb_yes.setOnClickListener(v -> edt_final_remark.setText("Refer to a doctor"));

        rb_lumb_no.setOnClickListener(v -> {
            edt_final_remark.setText("NA");
            tv_files.setText("");
        });

        edt_examiner_note.setOnClickListener(v -> {
            if (Utilities.isNetworkAvailable(context)) {
                new GetExaminarNoteList().execute();
            } else {
                Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
            }
        });

        edt_left_quadrant.setOnClickListener(v -> {
            if (Utilities.isNetworkAvailable(context)) {
                new GetBreastQuadrantValues().execute("1");
            } else {
                Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
            }
        });

        edt_right_quadrant.setOnClickListener(v -> {
            if (Utilities.isNetworkAvailable(context)) {
                new GetBreastQuadrantValues().execute("2");
            } else {
                Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
            }
        });
    }

    private void submitData() {
        String PSObservation = "0";
        String LumpObservation = "0";

        if (edt_machine.getText().toString().trim().isEmpty()) {
            edt_machine.setError("Please select machine from above button");
            return;
        }

        if (edt_family_history.getText().toString().trim().isEmpty()) {
            edt_family_history.setError("Please enter family history");
            return;
        }

        if (rb_observation_yes.isChecked()) {
            PSObservation = "1";
            if (edt_comment.getText().toString().trim().isEmpty()) {
                edt_comment.setError("Please enter comment");
                return;
            }

            if (edt_left_quadrant.getText().toString().trim().isEmpty() &&
                    edt_right_quadrant.getText().toString().trim().isEmpty()) {
                Utilities.showMessageString("Please select left or right breast quadrant", context);
                return;
            }

//            if (edt_left_quadrant.getText().toString().trim().isEmpty()) {
//                edt_left_quadrant.setError("Please select left quadrant");
//                return;
//            }
//
//            if (edt_right_quadrant.getText().toString().trim().isEmpty()) {
//                edt_left_quadrant.setError("Please select right quadrant");
//                return;
//            }

        } else if (rb_observation_no.isChecked()) {
            PSObservation = "0";
        } else {
            Utilities.showToastMessage("Please Select Symptoms", context, false);
            return;
        }

//        if (rb_lumb_yes.isChecked()) {
//            LumpObservation = "1";
//        } else if (rb_lumb_no.isChecked()) {
//            LumpObservation = "0";
//        } else {
//            Utilities.showToastMessage("Please Select Survivor", context, false);
//            return;
//        }

        if (edt_examiner_note.getText().toString().trim().isEmpty()) {
            edt_examiner_note.setError("Please select examiner's note");
            return;
        }

        if (csvPath.isEmpty()) {
            Utilities.showToastMessage("Please Select CSV File", context, false);
            return;
        }

        if (pngPath.isEmpty()) {
            Utilities.showToastMessage("Please Select PNG File", context, false);
            return;
        }

        if (pdfPath.isEmpty()) {
            Utilities.showToastMessage("Please Select PDF File", context, false);
            return;
        }

        if (Utilities.isNetworkAvailable(context)) {
            new UploadPatientDetails().execute(
                    String.valueOf(patientDetails.getRegdId()),
                    campId,
                    edt_family_history.getText().toString().trim(),
                    PSObservation,
                    edt_comment.getText().toString().trim(),
                    LumpObservation,
//                    edt_final_remark.getText().toString().trim(),
                    "",
                    userID,
                    breastScreeningDeviceId,
                    examinerNoteId,
                    csvPath,
                    pngPath,
                    pdfPath,
                    edt_left_quadrant.getText().toString().trim(),
                    edt_right_quadrant.getText().toString().trim()
            );
        } else {
            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
        }
    }

    private class UploadPatientDetails extends AsyncTask<String, Integer, String> {

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

                MultipartUtility multipart = new MultipartUtility(ApplicationConstants.InsertBreastScreening_CSVNew1, "UTF-8");
                multipart.addFormField("RegId", params[0]);
                multipart.addFormField("CampId", params[1]);
                multipart.addFormField("FamilyHisComment", params[2]);
                multipart.addFormField("PSObservation", params[3]);
                multipart.addFormField("PSComment", params[4]);
                multipart.addFormField("LumpObservation", params[5]);
                multipart.addFormField("FinalRemark", params[6]);
                multipart.addFormField("CreatedBy", params[7]);
                multipart.addFormField("DeviceId", params[8]);
                multipart.addFormField("ExaminerNoteID", params[9]);
                multipart.addFormField("Left_Breast_Findings", params[13]);
                multipart.addFormField("Right_Breast_Findings", params[14]);
                multipart.addFilePart("CS", new File(params[10]));
                multipart.addFilePart("PN", new File(params[11]));
                multipart.addFilePart("PD", new File(params[12]));
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
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("AttendanceMarkedPatients_Activity"));

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setIcon(R.drawable.icon_success);
                        builder.setTitle("Success");
                        builder.setCancelable(false);
                        builder.setMessage("Breast screening details submitted successfully");
                        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        AlertDialog alertD = builder.create();
                        alertD.show();
                    } else {
                        Utilities.showAlertDialog(context, status, message, false);
                    }
                } else
                    Utilities.showAlertDialog(context,
                            "Please try again", "Server not responding.", false);
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context,
                        "Please try again", "Server not responding.", false);
            }
        }
    }

    public class GetExaminarNoteList extends AsyncTask<String, Void, String> {

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

            res = WebServiceCall.APICall(ApplicationConstants.GetExaminarNoteList, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    List<BreastScreeningExaminarNoteModel.OutputBean> examinersNotesList = new ArrayList<>();
                    BreastScreeningExaminarNoteModel pojoDetails = new Gson().fromJson(result, BreastScreeningExaminarNoteModel.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        examinersNotesList = pojoDetails.getOutput();
                        if (examinersNotesList.size() > 0) {
                            showExaminerNoteListDialog(examinersNotesList);
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

    private void showExaminerNoteListDialog(final List<BreastScreeningExaminarNoteModel.OutputBean> examinersNotesList) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        builderSingle.setTitle("Select Examiner's Note");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        for (int i = 0; i < examinersNotesList.size(); i++) {
            arrayAdapter.add(String.valueOf(examinersNotesList.get(i).getExaminarNote()));
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
                examinerNoteId = examinersNotesList.get(which).getExaId();
                edt_examiner_note.setText(examinersNotesList.get(which).getExaminarNote());
            }
        });
        builderSingle.show();
    }

    public class GetBreastQuadrantValues extends AsyncTask<String, Void, String> {

        String flag = "";

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
            flag = params[0];
            List<ParamsPojo> param = new ArrayList<>();
            res = WebServiceCall.APICall(ApplicationConstants.getBreastQuadrantValues, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    QuadrantListModel pojoDetails = new Gson().fromJson(result, QuadrantListModel.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        quadrantList = pojoDetails.getOutput();
                        if (quadrantList.size() > 0) {
                            if (flag.equals("1"))
                                showLeftQuadrantListDialog();
                            else if (flag.equals("2"))
                                showRightQuadrantListDialog();
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

    private void showLeftQuadrantListDialog() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_check_list, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        builder.setCancelable(false);
        builder.setTitle("Select Left Quadrant");

        RecyclerView rv_checklist = view.findViewById(R.id.rv_checklist);
        rv_checklist.setLayoutManager(new LinearLayoutManager(context));

        rv_checklist.setAdapter(new LeftQuadrantListAdapter());

        builder.setPositiveButton("Select", (dialog, which) -> {
            edt_left_quadrant.setText("");

            StringBuilder selectedSubCategories = new StringBuilder();

            for (QuadrantListModel.OutputBean sample : quadrantList) {
                if (sample.isLeftChecked()) {
                    selectedSubCategories.append(sample.getLeftQuadrant()).append(", ");
                }
            }

            if (selectedSubCategories.toString().length() != 0) {
                String selectedLabsStr = selectedSubCategories.substring(0, selectedSubCategories.toString().length() - 2);
                edt_left_quadrant.setText(selectedLabsStr);
            }
        });

        builder.create().show();
    }

    private class LeftQuadrantListAdapter extends RecyclerView.Adapter<LeftQuadrantListAdapter.MyViewHolder> {

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

            holder.cb_select.setText(quadrantList.get(position).getLeftQuadrant());

            if (quadrantList.get(position).isLeftChecked()) {
                holder.cb_select.setChecked(true);
            }

            holder.cb_select.setOnCheckedChangeListener((buttonView, isChecked) -> {
                quadrantList.get(position).setLeftChecked(isChecked);
            });
        }

        @Override
        public int getItemCount() {
            return quadrantList.size();
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

    private void showRightQuadrantListDialog() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_check_list, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        builder.setCancelable(false);
        builder.setTitle("Select Right Quadrant");

        RecyclerView rv_checklist = view.findViewById(R.id.rv_checklist);
        rv_checklist.setLayoutManager(new LinearLayoutManager(context));

        rv_checklist.setAdapter(new RightQuadrantListAdapter());

        builder.setPositiveButton("Select", (dialog, which) -> {
            edt_right_quadrant.setText("");

            StringBuilder selectedSubCategories = new StringBuilder();

            for (QuadrantListModel.OutputBean sample : quadrantList) {
                if (sample.isRightChecked()) {
                    selectedSubCategories.append(sample.getRightQuadrant()).append(", ");
                }
            }

            if (selectedSubCategories.toString().length() != 0) {
                String selectedLabsStr = selectedSubCategories.substring(0, selectedSubCategories.toString().length() - 2);
                edt_right_quadrant.setText(selectedLabsStr);
            }
        });

        builder.create().show();
    }

    private class RightQuadrantListAdapter extends RecyclerView.Adapter<RightQuadrantListAdapter.MyViewHolder> {

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

            holder.cb_select.setText(quadrantList.get(position).getRightQuadrant());

            if (quadrantList.get(position).isRightChecked()) {
                holder.cb_select.setChecked(true);
            }

            holder.cb_select.setOnCheckedChangeListener((buttonView, isChecked) -> {
                quadrantList.get(position).setRightChecked(isChecked);
            });
        }

        @Override
        public int getItemCount() {
            return quadrantList.size();
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

    public class GetDeviceList extends AsyncTask<String, Void, String> {

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
                session.setDeviceId(deviceList.get(which).getDeviceId());
                session.setDeviceName(deviceList.get(which).getDeviceName());

                getDeviceFromSession();

            }
        });
        builderSingle.show();
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Breast Screening");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
