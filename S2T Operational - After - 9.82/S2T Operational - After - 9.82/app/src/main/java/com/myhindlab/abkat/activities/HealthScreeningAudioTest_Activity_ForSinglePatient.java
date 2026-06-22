package com.myhindlab.abkat.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.adapters.HearingDataAdapter_New;
import com.myhindlab.abkat.models.HearingData_New;
import com.myhindlab.abkat.models.MasterModel;
import com.myhindlab.abkat.models.ModelClass;
import com.myhindlab.abkat.models.Responce;
import com.myhindlab.abkat.rest.ApiClient;
import com.myhindlab.abkat.rest.ApiInterface;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.MultipartUtility;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HealthScreeningAudioTest_Activity_ForSinglePatient extends AppCompatActivity {

    private Context context;
    private ProgressDialog pd;
    private String TAG = getClass().getName();
    private UserSessionManager session;
    private MaterialEditText edt_beneficiaryname, edt_gender, edt_age, edt_height, edt_weight;
    //    private PresentPatientList_Model patientDetails;
    private String userID, campId, hearingObservation = "", RightRemark = "", healthScreentype;
    private LinearLayout ll_chart;
    private TextView tv_patnamepatregno;

    private ArrayList<HearingData_New> hearingData;
    private ArrayList<ModelClass> dbVolume;
    private ArrayList<ModelClass> hzFreq;

    private LineChart line_chart;
    private RadioButton rb_left, rb_right;
    private MaterialEditText edt_frequency, edt_volume, edt_observation, edt_leftobservation;
    private Button btn_addvolume, btn_subvolume, btn_save;
    private RecyclerView recycler_view_record;

    private int dbPos = 0, hzPos = 0;
    private String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_healthscreening_audiotest_new);

        init();
        setUpToolbar();
        setDefaults();
        getSessionData();
        setEventHandler();
    }

    private void init() {
        context = HealthScreeningAudioTest_Activity_ForSinglePatient.this;
        pd = new ProgressDialog(context);
        session = new UserSessionManager(context);

        ll_chart = findViewById(R.id.ll_chart);
        edt_beneficiaryname = findViewById(R.id.edt_beneficiaryname);
        edt_gender = findViewById(R.id.edt_gender);
        edt_age = findViewById(R.id.edt_age);
        edt_height = findViewById(R.id.edt_height);
        edt_weight = findViewById(R.id.edt_weight);
        tv_patnamepatregno = findViewById(R.id.tv_patnamepatregno);

        rb_left = findViewById(R.id.rb_left);
        rb_right = findViewById(R.id.rb_right);

        edt_frequency = findViewById(R.id.edt_frequency);
        edt_volume = findViewById(R.id.edt_volume);

        btn_addvolume = findViewById(R.id.btn_addvolume);
        btn_subvolume = findViewById(R.id.btn_subvolume);

        recycler_view_record = findViewById(R.id.recycler_view_record);

        line_chart = findViewById(R.id.line_chart);
        edt_observation = findViewById(R.id.edt_observation);
        edt_leftobservation = findViewById(R.id.edt_leftobservation);
        btn_save = findViewById(R.id.btn_save);
    }

    private void setDefaults() {
//        patientDetails = (PresentPatientList_Model) getIntent().getSerializableExtra("patientDetails");
//        campId = getIntent().getStringExtra("campId");
        campId = "260";

//        healthScreentype = getIntent().getStringExtra("healthScreentype");
        healthScreentype = "5";
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }

//        edt_beneficiaryname.setText(patientDetails.getEnglishName());
//        edt_age.setText(patientDetails.getAge());
//        edt_height.setText(patientDetails.getHeight_CMs());
//        edt_weight.setText(patientDetails.getWeight_KGs());

//        if (patientDetails.getGender().equalsIgnoreCase("M")) {
//            edt_gender.setText("Male");
//        } else if (patientDetails.getGender().equalsIgnoreCase("F")) {
//            edt_gender.setText("Female");
//        } else if (patientDetails.getGender().equalsIgnoreCase("O")) {
//            edt_gender.setText("Other");
//        }

//        tv_patnamepatregno.setText(patientDetails.getEnglishName() + " (" + patientDetails.getRegdNo() + ")");
        tv_patnamepatregno.setText("Ashwin Bhimrao Hirole" + " (" + "90800028716" + ")");
//        if (Utilities.isNetworkAvailable(context)) {
//            new GetCW_AudiometicMasterDetails().execute();
//            GetListOfLungandAudioImageDetailsAPICall(patientDetails.getRegdId());
//        } else {
//            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
//        }

        setChartProperties();

        hzFreq = new ArrayList<>();
        hzFreq.add(new ModelClass("125 HZ", 125));
        hzFreq.add(new ModelClass("250 HZ", 250));
        hzFreq.add(new ModelClass("500 HZ", 500));
        hzFreq.add(new ModelClass("1000 HZ", 1000));
        hzFreq.add(new ModelClass("2000 HZ", 2000));
        hzFreq.add(new ModelClass("4000 HZ", 4000));
        hzFreq.add(new ModelClass("8000 HZ", 8000));

        hearingData = new ArrayList<>();
        for (int i = 0; i < hzFreq.size(); i++) {
            hearingData.add(new HearingData_New(hzFreq.get(i).getValue(), 40, 40));
        }

        dbVolume = new ArrayList<>();
        dbVolume.add(new ModelClass("-5 dB", -5));
        dbVolume.add(new ModelClass("0 dB", 0));
        dbVolume.add(new ModelClass("5 dB", 5));
        dbVolume.add(new ModelClass("10 dB", 10));
        dbVolume.add(new ModelClass("20 dB", 20));
        dbVolume.add(new ModelClass("30 dB", 30));
        dbVolume.add(new ModelClass("40 dB", 40));
        dbVolume.add(new ModelClass("50 dB", 50));
        dbVolume.add(new ModelClass("60 dB", 60));
        dbVolume.add(new ModelClass("70 dB", 70));
        dbVolume.add(new ModelClass("80 dB", 80));

        hzPos = 0;
        dbPos = 6;

        edt_frequency.setText(hzFreq.get(hzPos).getText());
        edt_volume.setText(dbVolume.get(dbPos).getText());

//        GradientDrawable gradientDrawable = new GradientDrawable(
//                GradientDrawable.Orientation.TOP_BOTTOM,
//                new int[]{ContextCompat.getColor(this, R.color.color1),
//                        ContextCompat.getColor(this, R.color.color2),
//                        ContextCompat.getColor(this, R.color.color3),
//                        ContextCompat.getColor(this, R.color.color4)});

//        line_chart.setBackground(gradientDrawable);
    }

    private void getSessionData() {
//        try {
//            JSONArray user_info = new JSONArray(session.getUserDetails().get(
//                    ApplicationConstants.KEY_LOGIN_INFO));
//            for (int j = 0; j < user_info.length(); j++) {
//                JSONObject json = user_info.getJSONObject(j);
//                userID = json.getString("EmpCode");
//                String name = json.getString("name");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        userID = "1219";
    }

    private void setEventHandler() {
        edt_frequency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showListDialog(hzFreq);
            }
        });

        btn_addvolume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = dbPos;
                i++;

                if (i > (dbVolume.size() - 1)) {
                    Utilities.showToastMessage("dB can not be grater then 80 dB", context, false);
                    return;
                }

                dbPos = i;
                edt_volume.setText(dbVolume.get(dbPos).getText());

                playSound();
            }
        });

        btn_subvolume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = dbPos;
                i--;

                if (i < 0) {
                    Utilities.showToastMessage("dB can not be less then -5 dB", context, false);
                    return;
                }

                dbPos = i;
                edt_volume.setText(dbVolume.get(dbPos).getText());

                playSound();
            }
        });

        edt_observation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<MasterModel> diseaseInjury = new ArrayList<>();

                diseaseInjury.add(new MasterModel("1", "Normal Hearing"));
                diseaseInjury.add(new MasterModel("2", "Mild Hearing Loss"));
                diseaseInjury.add(new MasterModel("3", "Moderate Hearing Loss"));
                diseaseInjury.add(new MasterModel("4", "Severe Hearing Loss"));
                diseaseInjury.add(new MasterModel("5", "Deafness"));
                showDiseaseInjuryListDialog(diseaseInjury, "3");
            }
        });

        edt_leftobservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<MasterModel> diseaseInjury = new ArrayList<>();

                diseaseInjury.add(new MasterModel("1", "Normal Hearing"));
                diseaseInjury.add(new MasterModel("2", "Mild Hearing Loss"));
                diseaseInjury.add(new MasterModel("3", "Moderate Hearing Loss"));
                diseaseInjury.add(new MasterModel("4", "Severe Hearing Loss"));
                diseaseInjury.add(new MasterModel("5", "Deafness"));
                showDiseaseInjuryListDialog(diseaseInjury, "4");
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Utilities.isNetworkAvailable(context)) {
                    Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                    return;
                }

                if (RightRemark.equalsIgnoreCase("")) {
                    edt_observation.setError("Please select Hearing Observation.");
                    edt_observation.setFocusable(true);
                    return;
                }

                if (hearingObservation.equalsIgnoreCase("")) {
                    edt_leftobservation.setError("Please select Hearing Observation.");
                    edt_leftobservation.setFocusable(true);
                    return;
                }

//                Gson gson = new Gson();
//                String data = gson.toJson(hearingData);
//                String obsver = hearingObservation;
                for (int i = 0; i < hearingData.size(); i++) {
                    hearingData.get(i).setPatientRegId("494584");
                    hearingData.get(i).setObservation(hearingObservation);
                    hearingData.get(i).setRightRemark(RightRemark);
                    hearingData.get(i).setCampId(campId);
                }

                Gson gson = new Gson();
                String data = gson.toJson(hearingData);

                InsertMachineHearingTestAPICall(userID, data);
                getBitmapFromView(ll_chart);
            }
        });

    }

    private void showDiseaseInjuryListDialog(final ArrayList<MasterModel> pipeTypeList, final String type) {
        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        if (type.equals("3")) {
            builderSingle.setTitle("Select Hearing Observation");
        } else if (type.equals("4")) {
            builderSingle.setTitle("Select Hearing Observation");
        }
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        for (MasterModel subTrenchModel : pipeTypeList) {
            arrayAdapter.add(subTrenchModel.getName());
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
                if (type.equals("3")) {
                    RightRemark = pipeTypeList.get(which).getId();
                    edt_observation.setText(pipeTypeList.get(which).getName());
                } else if (type.equals("4")) {
                    hearingObservation = pipeTypeList.get(which).getId();
                    edt_leftobservation.setText(pipeTypeList.get(which).getName());
                }
            }
        });
        builderSingle.show();
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Audio Screening Test");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void showListDialog(final ArrayList<ModelClass> fztitle) {
        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);

        builderSingle.setTitle("Select Frequency");

        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        for (ModelClass title : fztitle) {
            arrayAdapter.add(title.getText());
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
                hzPos = which;
//                selectedFrequency = fztitle.get(which).getValue();
                edt_frequency.setText(fztitle.get(which).getText());

                dbPos = 6;
                edt_volume.setText(dbVolume.get(dbPos).getText());
                playSound();
            }
        });
        builderSingle.show();
    }

    public void playSound() {
//        AssetManager am;
        try {
            String fzFolderName = String.valueOf(hzFreq.get(hzPos).getValue());
            String dBFileName = String.valueOf(dbVolume.get(dbPos).getValue());

//            getAssets().open("filename.txt")
//            AssetFileDescriptor afd = getAssets().openFd("AudioFile.mp3");
            AssetFileDescriptor afd = context.getAssets()
                    .openFd("puretone_" + fzFolderName + "_" + dBFileName + ".ogg");

//            Toast.makeText(context, "puretone_" + folderName + "_" + fileName + ".ogg", Toast.LENGTH_LONG).show();

            MediaPlayer player = new MediaPlayer();
            player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),
                    afd.getLength());
            player.prepare();
            player.start();

            int right = 0;
            int left = 0;

            if (rb_right.isChecked()) {
                right = 1;
            }

            if (rb_left.isChecked()) {
                left = 1;
            }

            player.setVolume(left, right);

            final ProgressDialog pd = new ProgressDialog(context);
            pd.setMessage("Please wait . . . ");
            pd.setCancelable(false);
            pd.show();
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    // TODO Auto-generated method stub
                    mp.release();
                    saveData();
                    pd.dismiss();
                }
            });
            player.setLooping(false);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void saveData() {
        if (rb_right.isChecked()) {
            hearingData.get(hzPos).setRightVolume(dbVolume.get(dbPos).getValue());
        }

        if (rb_left.isChecked()) {
            hearingData.get(hzPos).setLefttVolume(dbVolume.get(dbPos).getValue());
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recycler_view_record.setLayoutManager(layoutManager);
        recycler_view_record.setAdapter(new HearingDataAdapter_New(hearingData));

        setData(hearingData);
    }

    private void setChartProperties() {
        line_chart.getDescription().setEnabled(false);

        line_chart.setMaxVisibleValueCount(100);

        line_chart.setPinchZoom(false);
        line_chart.setScaleMinima(2.6f, 0.75f);

        line_chart.setDrawGridBackground(false);


        // change the position of the y-labels
        YAxis leftAxis = line_chart.getAxisLeft();
//        leftAxis.setValueFormatter(new MyAxisValueFormatter());
        leftAxis.setLabelCount(8, false);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        leftAxis.setLabelCount(11);
        leftAxis.setAxisMaxValue(80);
        leftAxis.setAxisMinValue(-5);
        line_chart.getAxisRight().setEnabled(false);
        line_chart.setScaleEnabled(false);
        line_chart.setDrawGridBackground(false);
        line_chart.setDoubleTapToZoomEnabled(false);
        line_chart.getLegend().setEnabled(false);
        line_chart.setHighlightPerTapEnabled(false);
        line_chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTH_SIDED);
//        XAxis xLabels = Stacked_Chart.getXAxis();
//        xLabels.setPosition(XAxis.XAxisPosition.TOP);
        XAxis xAxis = line_chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        xAxis.setDrawGridLines(true);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelRotationAngle(-45);

        Legend l2 = line_chart.getLegend();
        l2.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l2.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l2.setOrientation(Legend.LegendOrientation.HORIZONTAL);
//        l2.setOrientation(Legend.LegendOrientation.VERTICAL);
        l2.setWordWrapEnabled(true);
        l2.setDrawInside(true);
        l2.setFormSize(8f);
        l2.setFormToTextSpace(4f);
        l2.setXEntrySpace(15f);
    }

    private void setData(ArrayList<HearingData_New> lineChartList) {
//        getDaysBetweenDates(fromDateStr, toDateStr);
        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<Entry> yVals = new ArrayList<Entry>();
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
//        ArrayList<Entry> yVals2 = new ArrayList<Entry>();
//        ArrayList<Entry> yVals3 = new ArrayList<Entry>();

        for (int i = 0; i < lineChartList.size(); i++) {
            HearingData_New pojo = lineChartList.get(i);
            String date = String.valueOf(pojo.getFrequency());
            xVals.add(date);


            String leftVolume = String.valueOf(pojo.getLefttVolume());
            if (leftVolume.equals("")) {
                leftVolume = "0";
            }

            String rightVolume = String.valueOf(pojo.getRightVolume());
            if (rightVolume.equals("")) {
                rightVolume = "0";
            }

//            String chamber = String.valueOf(Integer.parseInt(pojo.getJointChember()) + Integer.parseInt(pojo.getLoopChember()));
//            if (chamber.equals("")) {
//                chamber = "0";
//            }
//
//            String splicing = pojo.getSplicing();
//            if (splicing.equals("")) {
//                splicing = "0";
//            }

            float leftVolumeFlt = Float.parseFloat(leftVolume);
            float rightVolumeFlt = Float.parseFloat(rightVolume);
            yVals.add(new Entry(i, leftVolumeFlt, getResources().getDrawable(R.drawable.icon_line_cross)));
            yVals1.add(new Entry(i, rightVolumeFlt, getResources().getDrawable(R.drawable.icon_line_ring)));
        }


        final String[] mValues = new String[xVals.size()];
        for (int i = 0; i < xVals.size(); i++) {
            mValues[i] = xVals.get(i);
        }

        line_chart.getAxisLeft().setDrawGridLines(true);
        line_chart.getXAxis().setDrawGridLines(true);
        line_chart.setDrawGridBackground(true);
        line_chart.getDescription().setText("");

        Legend l = line_chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setWordWrapEnabled(true);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setEnabled(true);
        l.setYOffset(0f);

        YAxis rightAxis = line_chart.getAxisRight();
        rightAxis.setDrawGridLines(true);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis leftAxis = line_chart.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        XAxis xAxis = line_chart.getXAxis();
        xAxis.setGranularityEnabled(true);
        xAxis.setLabelCount(mValues.length, false);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mValues[(int) value % mValues.length];

            }
        });

        LineData d = new LineData();

        LineDataSet set = new LineDataSet(yVals, "");
        set.setColors(ColorTemplate.rgb("#0000FF"));
        set.setLabel("Left Ear");
        set.setLineWidth(2.5f);
        set.setCircleColor(Color.rgb(0, 0, 0));
        set.setCircleRadius(3f);
        set.setCircleColorHole(ColorTemplate.rgb("#000000"));
        set.setFillColor(Color.rgb(0, 0, 0));
        set.setMode(LineDataSet.Mode.LINEAR);
        set.setDrawHighlightIndicators(false);
        set.setDrawValues(true);
        set.setDrawIcons(true);
        set.setValueTextSize(10f);
        set.setValueTextColor(Color.rgb(0, 0, 0));
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        d.addDataSet(set);

        LineDataSet set1 = new LineDataSet(yVals1, "");
        set1.setColors(ColorTemplate.rgb("#FF0000"));
        set1.setLabel("Right Ear");
        set1.setLineWidth(2.5f);
        set1.setCircleColor(Color.rgb(0, 0, 0));
        set1.setCircleRadius(3f);
        set1.setCircleColorHole(ColorTemplate.rgb("#000000"));
        set1.setFillColor(Color.rgb(0, 0, 0));
        set1.setMode(LineDataSet.Mode.LINEAR);
        set1.setDrawHighlightIndicators(false);
        set.setDrawIcons(true);
        set1.setDrawValues(true);
        set1.setValueTextSize(10f);
        set1.setValueTextColor(Color.rgb(0, 0, 0));
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        d.addDataSet(set1);

        line_chart.setData(d);
        line_chart.setVisibleXRange(Float.parseFloat("5"), Float.parseFloat("8"));
        line_chart.invalidate();

//        for (int i = 0; i < lineChartList.size(); i++) {
//            if (lineChartList.get(0).getRightVolume() <= 20) {
//                rNormal = rNormal + 1;
//            } else if (lineChartList.get(0).getRightVolume() > 20 && lineChartList.get(0).getRightVolume() <= 40) {
//                rMild = rMild + 1;
//            } else if (lineChartList.get(0).getRightVolume() > 40 && lineChartList.get(0).getRightVolume() <= 60) {
//                rModerate = rModerate + 1;
//            } else if (lineChartList.get(0).getRightVolume() > 60 && lineChartList.get(0).getRightVolume() < 80) {
//                rSevere = rSevere + 1;
//            } else if (lineChartList.get(0).getRightVolume() == 80) {
//                rDeafness = rDeafness + 1;
//            }
//
//            if (lineChartList.get(0).getLefttVolume() <= 20) {
//                lNormal = lNormal + 1;
//            } else if (lineChartList.get(0).getLefttVolume() > 20 && lineChartList.get(0).getLefttVolume() <= 40) {
//                lMild = lMild + 1;
//            } else if (lineChartList.get(0).getLefttVolume() > 40 && lineChartList.get(0).getLefttVolume() <= 70) {
//                lModerate = lModerate + 1;
//            } else if (lineChartList.get(0).getLefttVolume() > 70 && lineChartList.get(0).getLefttVolume() < 80) {
//                lSevere = lSevere + 1;
//            } else if (lineChartList.get(0).getLefttVolume() == 80) {
//                lDeafness = lDeafness + 1;
//            }
//
//        }
//
//        int right[] = {rNormal, rMild, rModerate, rSevere, rDeafness};
//        int left[] = {lNormal, lMild, lModerate, lSevere, lDeafness};
//
//        int max1 = right[0];
//        int index1 = 0;
//
//        for (int i = 0; i < right.length; i++) {
//            if (max1 < right[i]) {
//                max1 = right[i];
//                index1 = i;
//            }
//        }
//
//        System.out.println("AUDIOMETRIC Right value in an array is  :  " + index1);
//
//        int max2 = left[0];
//        int index2 = 0;
//
//        for (int i = 0; i < left.length; i++) {
//            if (max2 < left[i]) {
//                max2 = left[i];
//                index2 = i;
//            }
//        }
//
//        System.out.println("AUDIOMETRIC Left value in an array is  :  " + index2);

    }

    private void InsertMachineHearingTestAPICall(String uId, String data) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait . . .");
        progressDialog.show();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<Responce> call = apiService.InsertMachineHearingTest(uId, data);
        call.enqueue(new Callback<Responce>() {
            @Override
            public void onResponse(Call<Responce> call,
                                   Response<Responce> response) {
                progressDialog.dismiss();

                int surverCode = response.code();
                String surverMessage = response.message();

                if (surverCode == ApplicationConstants.OKSUCCESS) {
                    try {
                        String status = response.body().getStatus();
                        String message = response.body().getMessage();

                        if (status.equalsIgnoreCase("Success")) {
                            new InsertAudioImages().execute(
                                    "494584",
                                    userID,
                                    imagePath
                            );

                        } else {
                            Utilities.showAlertDialog(context, status, message, false);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Utilities.showAlertDialog(context,
                                "Alert", "Exception " + e.toString(), false);
                    }
                } else {
                    Utilities.showAlertDialog(context,
                            "Alert", "Server returns : " + surverCode + " " + surverMessage, false);
                }
            }

            @Override
            public void onFailure(Call<Responce> call, Throwable t) {
                progressDialog.dismiss();
                Log.e(TAG, t.toString());
                Utilities.showAlertDialog(context,
                        "Please try again", t.toString(), false);
            }
        });
    }

    public class InsertAudioImages extends AsyncTask<String, Void, String> {

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
            try {
                MultipartUtility multipart = new MultipartUtility(ApplicationConstants.InsertAudioImages, "UTF-8");

                multipart.addFormField("RegId", params[0]);
                multipart.addFormField("CreatedBy", params[1]);
                multipart.addFilePart("File", new File(params[2]));

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
            try {
                pd.dismiss();

                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
                    JSONObject obj = new JSONObject(result);
                    String status = obj.getString("status");
                    String message = obj.getString("message");
                    if (status.equalsIgnoreCase("Success")) {
                        Utilities.showAlertDialog(context, "Success", "Audiometric test saved successfully",
                                true, "OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                });
                    } else {
                        showReuploadDialog();
                    }
                } else {
                    showReuploadDialog();
                }
            } catch (Exception e) {
                e.printStackTrace();
                showReuploadDialog();
            }
        }

    }

    private void getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        } else {
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        }
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        createPdf(returnedBitmap);
    }

    private void createPdf(Bitmap bitmap) {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        PdfDocument document = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            document = new PdfDocument();
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), 1).create();
            PdfDocument.Page page = document.startPage(pageInfo);

            Canvas canvas = page.getCanvas();
            Paint paint = new Paint();
            paint.setColor(Color.parseColor("#ffffff"));
            canvas.drawPaint(paint);

            bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);

            paint.setColor(Color.BLUE);
            canvas.drawBitmap(bitmap, 0, 0, null);
            document.finishPage(page);

            File patientPicsFolder = new File(Environment.getExternalStorageDirectory() + "/Health Checkup/AudiometerImage");
            if (!patientPicsFolder.exists()) {
                patientPicsFolder.mkdirs();
            }

            File filePath = new File(patientPicsFolder, userID + "_" + "494584" + ".pdf");
            try {
                document.writeTo(new FileOutputStream(filePath));
                imagePath = filePath.getPath();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
            }

            // close the document
            document.close();
        }


    }

    private void showReuploadDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Something went wrong. Please click OK to upload again");
        builder.setTitle("Success");
        builder.setIcon(R.drawable.icon_alertred);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                new InsertAudioImages().execute(
                        "494584",
                        userID,
                        imagePath
                );
            }
        });
        AlertDialog alertD = builder.create();
        alertD.show();
    }
}