package com.myhindlab.abkat.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.gson.Gson;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.adapters.TodaysAvailabilityReportLabWiseAdapter;
import com.myhindlab.abkat.pojos.AttendanceReportDistrictWise_OutPut_Pojo;
import com.myhindlab.abkat.pojos.AttendanceReportDistrictWise_Pojo;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.RecyclerItemClickListener;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DashboardAvailabilityReportDistrict_Activty extends AppCompatActivity {

    private Context context;
    private UserSessionManager session;
    private ProgressDialog pd;

    private ScrollView GraphicalView;
    private LinearLayout StaticView;

    private PieChart Pie_Chart;
    private BarChart Stacked_Chart;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView pie_txt, stacked_txt, Locationname,
            row1, row2, row3,
            markedCntTv, notMarkedCntTv, totalCntTv;
    private RecyclerView avalibilityReportList;
    private ArrayList<AttendanceReportDistrictWise_OutPut_Pojo> listDetails;
    private ArrayList<AttendanceReportDistrictWise_OutPut_Pojo> attendanceList;

    private int mYear, mMonth, mDay;
    private TextView txt_filterdate, txt_filterdate1;
    private String fromDate;
    private int visibleStatus = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_availability_report_district__activty);

        init();
        setDefaults();
        setToolBar();
        setEventHandlers();
    }
    private void init() {
        GraphicalView = findViewById(R.id.sv_graphicalview);

        pie_txt = findViewById(R.id.tv_pietext);
        Pie_Chart = findViewById(R.id.pie_chart);

        stacked_txt = findViewById(R.id.tv_stackedtext);
        Stacked_Chart = findViewById(R.id.stacked_chart);

        StaticView = findViewById(R.id.ll_staticview);

        Locationname = findViewById(R.id.txt_locationname);
        row1 = findViewById(R.id.txt_row1);
        row2 = findViewById(R.id.txt_row2);
        row3 = findViewById(R.id.txt_row3);

        markedCntTv = findViewById(R.id.tv_row1);
        notMarkedCntTv = findViewById(R.id.tv_row2);
        totalCntTv = findViewById(R.id.tv_row3);

        mSwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        avalibilityReportList = findViewById(R.id.recyclerview_list);
        txt_filterdate = findViewById(R.id.txt_filterdate);
        txt_filterdate1 = findViewById(R.id.txt_filterdate1);
    }

    private void setDefaults() {
        context = DashboardAvailabilityReportDistrict_Activty.this;
        session = new UserSessionManager(context);
        pd = new ProgressDialog(context);
        Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);


        txt_filterdate.setText("Date - " + Utilities.ConvertDateFormat(Utilities.dfDate4, mDay, mMonth + 1, mYear));
        txt_filterdate1.setText("Date - " + Utilities.ConvertDateFormat(Utilities.dfDate4, mDay, mMonth + 1, mYear));
        fromDate = Utilities.ConvertDateFormat(Utilities.dfDate4, mDay, mMonth + 1, mYear);

        pie_txt.setText("Availability Pie Chart in State -");
        stacked_txt.setText("Availability Stacked Chart Lab Wise -");

        Locationname.setText("District");
        row1.setText("Marked");
        row2.setText("Not Marked");
        row3.setText("Total");

        setChart();
    }

    private void setToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Availability Dashboard");


        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageView logout = findViewById(R.id.img_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (visibleStatus == 0) {
                    visibleStatus = 1;
                    GraphicalView.setVisibility(View.GONE);
                    StaticView.setVisibility(View.VISIBLE);
                    // right_btn.setBackgroundResource(R.drawable.icon_graphicalview);

                    setStaticData();

                } else if (visibleStatus == 1) {
                    visibleStatus = 0;

                    GraphicalView.setVisibility(View.VISIBLE);
                    StaticView.setVisibility(View.GONE);
                    //  right_btn.setBackgroundResource(R.drawable.icon_staticview);

                    setChart();
                }
            }
        });
//        if (session.getIsStaticalView()
//                .get(ApplicationConstants.KEY_STATICALVIEW).equalsIgnoreCase("0"))
//            right_btn.setBackgroundResource(R.drawable.icon_staticview);
//
//        else if (session.getIsStaticalView()
//                .get(ApplicationConstants.KEY_STATICALVIEW).equalsIgnoreCase("1"))
//            right_btn.setBackgroundResource(R.drawable.icon_graphicalview);

    }

    private void setEventHandlers() {
    txt_filterdate.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DatePickerDialog dpd1 = new DatePickerDialog(context,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            txt_filterdate.setText("Date - " + Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year));
                            txt_filterdate1.setText("Date - " + Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year));
                            fromDate = Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year);
                            mYear = year;
                            mMonth = monthOfYear;
                            mDay = dayOfMonth;
                            new TodayAttendanceReport_LABWISEGraphical().execute(fromDate, "0", "0", "0");
                        }
                    }, mYear, mMonth, mDay);
            try {
                dpd1.getDatePicker().setCalendarViewShown(false);
                dpd1.getDatePicker().setMaxDate(System.currentTimeMillis());
            } catch (Exception e) {
                e.printStackTrace();
            }
            dpd1.show();
        }
    });

        avalibilityReportList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                AttendanceReportDistrictWise_OutPut_Pojo selected = new AttendanceReportDistrictWise_OutPut_Pojo();
//                selected = listDetails.get(position);
//                if (Utilities.isInternetAvailable(context)) {
//                    if (!selected.getOnBoardUsersCount().equals("0")) {
//                        Intent i = new Intent(context, DashboardAvailabilityReportUserList_Activity.class);
//                        i.putExtra("LabCode", selected.getLabcode());
//                        i.putExtra("facilityName", selected.getLabName());
//                        i.putExtra("Date", fromDate);
//                        i.putExtra("IsLBM", "N");
//                        startActivity(i);
//                    } else
//                        Utilities.showAlertDialog(context, "Alert", "No Record Found", false);
//                } else
//                    Utilities.showMessage(R.string.msgt_nointernetconnection, context);
            }
        });


        avalibilityReportList.addOnItemTouchListener(new RecyclerItemClickListener(context,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        AttendanceReportDistrictWise_OutPut_Pojo selected = new AttendanceReportDistrictWise_OutPut_Pojo();
                        selected = listDetails.get(position);
                        if (Utilities.isNetworkAvailable(context)) {
                            if (!selected.getOnBoardUsersCount().equals("0")) {
                                Intent i = new Intent(context, DashboardAvailabilityReportUserList_Activity.class);
                                i.putExtra("DISTLGDCODE", selected.getDISTLGDCODE());
                                i.putExtra("DISTNAME", selected.getDISTNAME());
                                i.putExtra("Total Attendance", selected.getTotalAttendance());
                                i.putExtra("Absent", selected.getNotMarked());
                                i.putExtra("Date", fromDate);
                                startActivity(i);
                            } else
                                Utilities.showAlertDialog(context, "Alert", "No Record Found", false);
                        } else
                            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                    }
                }));
    }

    public void setChart() {

        // --- Setting Pie Chart ---
        Pie_Chart.setUsePercentValues(true);
        Pie_Chart.getDescription().setEnabled(false);
        Pie_Chart.setExtraOffsets(5, 10, 5, 5);

        Pie_Chart.setDragDecelerationFrictionCoef(0.95f);

        Pie_Chart.setDrawHoleEnabled(true);
        Pie_Chart.setHoleColor(Color.WHITE);

        Pie_Chart.setTransparentCircleColor(Color.WHITE);
        Pie_Chart.setTransparentCircleAlpha(110);

        Pie_Chart.setHoleRadius(58f);
        Pie_Chart.setTransparentCircleRadius(61f);

        Pie_Chart.setDrawCenterText(true);

        Pie_Chart.setRotationAngle(0);
        // enable rotation of the chart by touch
        Pie_Chart.setRotationEnabled(true);
        Pie_Chart.setHighlightPerTapEnabled(true);

        Legend l = Pie_Chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setWordWrapEnabled(true);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // --- Entry label styling
        Pie_Chart.setEntryLabelColor(Color.BLACK);
        Pie_Chart.setEntryLabelTextSize(12f);

        // --- Setting Stacked Chart ---
        Stacked_Chart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        Stacked_Chart.setMaxVisibleValueCount(100);

        // scaling can now only be done on x- and y-axis separately
        Stacked_Chart.setPinchZoom(false);
        Stacked_Chart.setScaleMinima(2.6f, 0.75f);

        Stacked_Chart.setDrawGridBackground(false);
        Stacked_Chart.setDrawBarShadow(false);

        Stacked_Chart.setDrawValueAboveBar(false);
        Stacked_Chart.setHighlightFullBarEnabled(false);

        // change the position of the y-labels
        YAxis leftAxis = Stacked_Chart.getAxisLeft();
//        leftAxis.setValueFormatter(new MyAxisValueFormatter());
//        leftAxis.setLabelCount(8, false);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        Stacked_Chart.getAxisRight().setEnabled(false);

//        XAxis xLabels = Stacked_Chart.getXAxis();
//        xLabels.setPosition(XAxis.XAxisPosition.TOP);
        XAxis xAxis = Stacked_Chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setLabelRotationAngle(-90);

        Legend l2 = Stacked_Chart.getLegend();
        l2.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l2.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l2.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l2.setWordWrapEnabled(true);
        l2.setDrawInside(true);
        l2.setFormSize(8f);
        l2.setFormToTextSpace(4f);
        l2.setXEntrySpace(15f);

        // Getting data form web services.
        if (Utilities.isNetworkAvailable(context)) {
            new TodayAttendanceReport_LABWISEGraphical().execute(fromDate, "0", "0", "0");
        } else
            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
    }

    public class TodayAttendanceReport_LABWISEGraphical extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pd.setMessage("Please wait ...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String res = "[]";

            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("Date", params[0]));
            param.add(new ParamsPojo("DesgId", params[1]));
            param.add(new ParamsPojo("DISTLGDCODE", params[2]));
            param.add(new ParamsPojo("DIVID", params[3]));

            res = WebServiceCall.APICall(ApplicationConstants.GetAttendanceReportDistrictWise, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            try {
                pd.dismiss();

                int markedAttendanceSum = 0;
                int notMarkedAttendanceSum = 0;
                int totalsum = 0;

                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {

                    Gson gson = new Gson();
                    AttendanceReportDistrictWise_Pojo output = gson.fromJson(result, AttendanceReportDistrictWise_Pojo.class);

                    String status = output.getStatus();
                    String message = output.getMessage();

                    if (status.equalsIgnoreCase("Success")) {

                        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
                        ArrayList<String> labName = new ArrayList<String>();

                        listDetails = new ArrayList<>();
                        listDetails = output.getOutput();

                        if (listDetails.size() > 0) {
                            for (int i = 0; i < listDetails.size(); i++) {
                                AttendanceReportDistrictWise_OutPut_Pojo attendanceInLab = new AttendanceReportDistrictWise_OutPut_Pojo();
                                attendanceInLab = listDetails.get(i);

                                markedAttendanceSum = markedAttendanceSum
                                        + Integer.parseInt(attendanceInLab.getTotalAttendance());
                                notMarkedAttendanceSum = notMarkedAttendanceSum
                                        + Integer.parseInt(attendanceInLab.getNotMarked());
                                totalsum = totalsum
                                        + Integer.parseInt(attendanceInLab.getOnBoardUsersCount());

                                yVals1.add(new BarEntry(i,
                                        new float[]{
                                                Float.valueOf(attendanceInLab.getNotMarked()),
                                                Float.valueOf(attendanceInLab.getTotalAttendance())}));
                                labName.add(attendanceInLab.getDISTNAME());
                            }

                            // --- Setting Stacked Entity ---
                            final String[] mValues = new String[labName.size()];

                            for (int i = 0; i < labName.size(); i++)
                                mValues[i] = labName.get(i);

                            IAxisValueFormatter formatter = new IAxisValueFormatter() {
                                @Override
                                public String getFormattedValue(float value, AxisBase axis) {
                                    return mValues[(int) value % mValues.length];
                                }
                            };

                            Stacked_Chart.getXAxis().setValueFormatter(formatter);

                            BarDataSet set1;

                            if (Stacked_Chart.getData() != null &&
                                    Stacked_Chart.getData().getDataSetCount() > 0) {
                                set1 = (BarDataSet) Stacked_Chart.getData().getDataSetByIndex(0);
                                set1.setValues(yVals1);
                                Stacked_Chart.getData().notifyDataChanged();
                                Stacked_Chart.notifyDataSetChanged();

                            } else {
                                set1 = new BarDataSet(yVals1, "");
                                set1.setColors(ColorTemplate.rgb("#C62828"),
                                        ColorTemplate.rgb("#7EC0EE"));
                                set1.setStackLabels(new String[]{
                                        "Not Marked Availability",
                                        "Marked Availability"});

                                ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
                                dataSets.add(set1);

                                BarData data = new BarData(dataSets);
                                data.setValueTextColor(Color.WHITE);
                                data.setValueFormatter(new MyValueFormatter());
                                Stacked_Chart.setData(data);
                            }

                            Stacked_Chart.setFitBars(true);
                            Stacked_Chart.invalidate();
                            Stacked_Chart.animateXY(1400, 1400);

                            // --- Setting Pie Entity ---
                            Pie_Chart.setCenterText(generateCenterSpannableText(totalsum));

                            ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

                            entries.add(new PieEntry((float) (notMarkedAttendanceSum),
                                    "Not Marked " + notMarkedAttendanceSum));
                            entries.add(new PieEntry((float) (markedAttendanceSum),
                                    "Marked " + markedAttendanceSum));

                            PieDataSet dataSet = new PieDataSet(entries, "");
                            dataSet.setSliceSpace(3f);
                            dataSet.setSelectionShift(5f);

                            // add a lot of colors
                            ArrayList<Integer> colors = new ArrayList<Integer>();
//                            for (int c : ColorTemplate.COLORFUL_COLORS)
                            for (int c : new int[]{ColorTemplate.rgb("#C62828")})
                                colors.add(c);
                            for (int c : new int[]{ColorTemplate.rgb("#7EC0EE")})
                                colors.add(c);

                            colors.add(ColorTemplate.getHoloBlue());

                            dataSet.setColors(colors);
                            dataSet.setValueLinePart1OffsetPercentage(80.f);
                            dataSet.setValueLinePart1Length(0.2f);
                            dataSet.setValueLinePart2Length(0.4f);
                            dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

                            PieData data = new PieData(dataSet);
                            data.setValueFormatter(new PercentFormatter());
                            data.setValueTextSize(11f);
                            data.setValueTextColor(Color.WHITE);
                            Pie_Chart.setData(data);

                            // undo all highlights
                            Pie_Chart.highlightValues(null);
                            Pie_Chart.invalidate();
                            Pie_Chart.animateXY(1400, 1400);

                        } else
                            Utilities.showAlertDialog(context, "Alert", "No record found.", false);

                    } else
                        Utilities.showAlertDialog(context, output.getStatus(), output.getMessage(), false);
                } else
                    Utilities.showAlertDialog(context,
                            "Please try again", "Server not responding.", false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public class MyValueFormatter implements IValueFormatter {

        private DecimalFormat mFormat;

        public MyValueFormatter() {
            mFormat = new DecimalFormat("###,###,##0");
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {

            if (value > 0) {
                return mFormat.format(value);
            } else {
                return "";
            }
        }
    }

    private SpannableString generateCenterSpannableText(int totalCount) {
        SpannableString s = new SpannableString("Total\nAvailability in State\n" + totalCount);

        s.setSpan(new RelativeSizeSpan(1.2f), 0, 5, 0);
        s.setSpan(new StyleSpan(Typeface.BOLD), 0, 5, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), 0, 5, 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), 0, 5, 0);

        s.setSpan(new RelativeSizeSpan(1.2f), 6, 27, 0);
        s.setSpan(new StyleSpan(Typeface.BOLD), 6, 27, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), 6, 27, 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), 6, 27, 0);

        s.setSpan(new RelativeSizeSpan(1.2f), 28, 28 + String.valueOf(totalCount).length(), 0);
        s.setSpan(new StyleSpan(Typeface.BOLD), 28, 28 + String.valueOf(totalCount).length(), 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), 28, 28 + String.valueOf(totalCount).length(), 0);
        return s;
    }


    private void setStaticData() {
        Locationname.setText("Distrcit");

        if (Utilities.isNetworkAvailable(context)) {
            new TodayAttendanceReport_LABWISEStatical().execute(fromDate, "0", "0", "0");
            mSwipeRefreshLayout.setRefreshing(false);
        } else
            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItems();
            }
        });
    }

    private void refreshItems() {
        onItemsLoadComplete();
    }

    private void onItemsLoadComplete() {
        if (Utilities.isNetworkAvailable(context)) {
            new TodayAttendanceReport_LABWISEGraphical().execute(fromDate, "0", "0", "0");
            mSwipeRefreshLayout.setRefreshing(false);
        } else
            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
    }

    public class TodayAttendanceReport_LABWISEStatical extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pd.setMessage("Please wait ...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String res = "[]";

            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("Date", params[0]));
            param.add(new ParamsPojo("DesgId", params[1]));
            param.add(new ParamsPojo("DISTLGDCODE", params[2]));
            param.add(new ParamsPojo("DIVID", params[3]));

            res = WebServiceCall.APICall(ApplicationConstants.GetAttendanceReportDistrictWise, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            try {
                pd.dismiss();

                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {

                    Gson gson = new Gson();
                    AttendanceReportDistrictWise_Pojo output = gson.fromJson(result,
                            AttendanceReportDistrictWise_Pojo.class);

                    if (output.getStatus().equalsIgnoreCase("Success")) {

                        listDetails = new ArrayList<>();
                        listDetails = output.getOutput();

                        if (listDetails.size() > 0) {
                            getTotalCnt(listDetails);
                            setRecyclerView(listDetails);

                        } else
                            Utilities.showAlertDialog(context, "Alert", "No record found.", false);

                    } else
                        Utilities.showAlertDialog(context, output.getStatus(), output.getMessage(), false);
                } else
                    Utilities.showAlertDialog(context,
                            "Please try again", "Server not responding.", false);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        private void getTotalCnt(ArrayList<AttendanceReportDistrictWise_OutPut_Pojo> attendanceReport) {
            if (attendanceReport == null)
                return;

            int markedCnt = 0;
            int notMarkedCnt = 0;
            int total = 0;

            for (AttendanceReportDistrictWise_OutPut_Pojo listDetails : attendanceReport) {

                if (listDetails.getTotalAttendance() != null
                        || !listDetails.getTotalAttendance().equals(""))
                    markedCnt = markedCnt + Integer.parseInt(listDetails.getTotalAttendance());

                if (listDetails.getNotMarked() != null
                        || !listDetails.getNotMarked().equals(""))
                    notMarkedCnt = notMarkedCnt + Integer.parseInt(listDetails.getNotMarked());

                if (listDetails.getOnBoardUsersCount() != null
                        || !listDetails.getOnBoardUsersCount().equals(""))
                    total = total + Integer.parseInt(listDetails.getOnBoardUsersCount());
            }

            markedCntTv.setText("" + markedCnt);
            notMarkedCntTv.setText("" + notMarkedCnt);
            totalCntTv.setText("" + total);

        }
    }

    private void setRecyclerView(ArrayList<AttendanceReportDistrictWise_OutPut_Pojo> attList) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        avalibilityReportList.setLayoutManager(layoutManager);

        avalibilityReportList.setAdapter(new TodaysAvailabilityReportLabWiseAdapter(context, attList));
    }


}
