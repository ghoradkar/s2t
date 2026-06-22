package com.myhindlab.abkat.activities.regularcampcreation;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.TeamCampMappingActivity;
import com.myhindlab.abkat.activities.regularcampcreation.adapter.DeviceListAdapter;
import com.myhindlab.abkat.activities.regularcampcreation.model.CampCreationModel;
import com.myhindlab.abkat.activities.regularcampcreation.model.DeviceDetailsForApprovalModel;
import com.myhindlab.abkat.activities.regularcampcreation.model.DeviceListModel;
import com.myhindlab.abkat.activities.regularcampcreation.model.SubDeviceListModel;
import com.myhindlab.abkat.activities.regularcampcreation.model.UserResponseModel;
import com.myhindlab.abkat.models.CampListModel;
import com.myhindlab.abkat.models.CampTypeModel;
import com.myhindlab.abkat.models.UserDetailsModel;
import com.myhindlab.abkat.rest.ApiClient;
import com.myhindlab.abkat.rest.ApiInterface;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ConstantData;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DeviceAllocationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeviceAllocationFragment extends Fragment implements DeviceListAdapter.DeviceEvent {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String CampDate, EmpCode, LabCode,isMappingFlag,createdBy, TALLGDCODE, taluka;
    private String CampId,expectedBeneficiary;

    private UserSessionManager session;

    private TextView tvDeviceError, tvCampId, tvDate, tvCampType, tvMsg;
    private RecyclerView rvDeviceList;

    private ProgressDialog pd;

    private Button btnNext,btnSkip;

    private CampCreationModel.CampDetails campDetails;

    private ProgressBar progressBar;
    static FragmentChange fragmentChange;
    private UserSessionManager userSessionManager;
    private Context mContext;
    private ArrayList<CampListModel.OutputBean> campList;
    private int distLgdCode, campTypeId;
    private final String TAG = DeviceAllocationFragment.class.getSimpleName();
    private DeviceListModel deviceListModel;
    private SubDeviceListModel subDeviceListModel;

    private int mYear, mMonth, mDay, mYear1, mMonth1, mDay1;

    private ApiInterface apiService;
    private DeviceListAdapter deviceListAdapter;
    private List<SubDeviceListModel.Output> allSelSubDeviceListModel;


    public DeviceAllocationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DeviceAllocationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DeviceAllocationFragment newInstance(String param1, String param2, FragmentChange fragChange) {
        DeviceAllocationFragment fragment = new DeviceAllocationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
//        args.putString("Date", date);
//        args.putString("DISTLGDCODE", DISTLGDCODE);
//        args.putString("campId", campId);
//        args.putSerializable("campDetails", getIntent().getSerializableExtra("campDetails"));
//        args.putSerializable("campTypeId", ConstantData.getInstance().getCampType());
        fragment.setArguments(args);
        fragmentChange = fragChange;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_device_allocation, container, false);


        initView(rootView);
        clickEvents();
        getSessionDetails();
        setDefault();


        return rootView;
    }

    private void initView(View view) {
        mContext = requireContext();
        userSessionManager = new UserSessionManager(mContext);
        pd = new ProgressDialog(mContext);
        apiService = ApiClient.getD2DClient().create(ApiInterface.class);
        allSelSubDeviceListModel = new ArrayList<SubDeviceListModel.Output>();

        rvDeviceList = view.findViewById(R.id.rvDeviceList);
        tvDeviceError = view.findViewById(R.id.tvDeviceError);
        progressBar = view.findViewById(R.id.progress_circular);
        tvCampId = view.findViewById(R.id.tvCampId);
        tvCampType = view.findViewById(R.id.tvCampType);
        tvDate = view.findViewById(R.id.tvDate);
        tvMsg = view.findViewById(R.id.tvMsg);
        btnNext = view.findViewById(R.id.btnNext);
        btnSkip = view.findViewById(R.id.btnSkip);

        rvDeviceList.setHasFixedSize(true);
        rvDeviceList.setLayoutManager(new LinearLayoutManager(mContext));


        Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);


        tvDate.setText(Utilities.dfDate4.format(new Date()));
        CampDate = Utilities.dfDate4.format(new Date());

        if (Utilities.isNetworkAvailable(mContext)) {
//            getDevices();
        } else {
//            rvDeviceList.setVisibility(View.GONE);
//            tvDeviceError.setVisibility(View.VISIBLE);
//            progressBar.setVisibility(View.GONE);
//            tvDeviceError.setText("No need to map devices");
        }

    }

    private void getSessionDetails() {
//        UserDetailsModel.Output userDetailsJson = userSessionManager.getUserDetailsJson();
//        distLgdCode = CampCreationActivityV4.campCreationModel.getCampDetails().getDistLgdCode();

        try {
            JSONArray user_info = new JSONArray(userSessionManager.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                distLgdCode = Integer.parseInt(json.getString("DISTLGDCODE"));
                TALLGDCODE = json.getString("TALLGDCODE");
                taluka = json.getString("taluka");
                EmpCode = json.getString("EmpCode");
               // LabCode = json.getString("LabCode");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setDefault() {
//        if (CampCreationActivityV4.campCreationModel != null && CampCreationActivityV4.campCreationModel.getSiteList() != null) {
        int expBeneficiary = 0;
//            for (SiteListModel.Output site :
//                    CampCreationActivityV4.campCreationModel.getSiteList()) {
//                expBeneficiary = expBeneficiary + site.getNoWorkersRegister();
//            }
//            tvExpectedBeneficiary.setText("Expected beneficiary (" + expBeneficiary + ")");
//            edt_expected_beneficiary.setText(String.valueOf(expBeneficiary));
        campDetails = new CampCreationModel().new CampDetails();
        campDetails.setExpectedBeneficiary(expBeneficiary);
//            campDetails.setSelectedPartnerId(selectedPartnerId);

//            if (session.getUserDetailsJson().getPatnerID() != null) {
//                edt_camp_organized_by.setText("Partner");
//                campOrgId = 5;
//                edt_camp_organized_by.setEnabled(false);
//            }

//        }


//        try {
//            Date selectedDate = new SimpleDateFormat("yyyy/MM/dd").parse(edt_camp_date.getText().toString().trim());
//
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(selectedDate);
//            calendar.add(Calendar.DATE, 7);
//
//            int postCampYear = calendar.get(Calendar.YEAR);
//            int postCampMonth = calendar.get(Calendar.MONTH);
//            int postCampDay = calendar.get(Calendar.DAY_OF_MONTH);
//            edt_post_camp_date.setText(Utilities.ConvertDateFormat(Utilities.dfDate4, postCampDay, postCampMonth + 1, postCampYear));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getSessionDetails();
//            if (userSessionManager.getUserDetailsJson().getPatnerID() == null) {
//                tvMsg.setText("*Quantity has been Calculated based on the beneficiary count(" + CampCreationActivityV4.campCreationModel.getCampDetails().getExpectedBeneficiary() + ")");
//
//            } else {
//                tvMsg.setVisibility(View.GONE);
//
//            }
        }
    }


    void clickEvents() {
        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd1 = new DatePickerDialog(mContext,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                tvDate.setText(Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year));

                                CampDate = Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year);



                                tvCampId.setText("");
                                rvDeviceList.setVisibility(View.GONE);

                            }

                        }, mYear, mMonth, mDay);
                try {
                    dpd1.getDatePicker().setCalendarViewShown(false);

                    // dpd1.getDatePicker().setMinDate(System.currentTimeMillis());

                } catch (Exception e) {
                    e.printStackTrace();
                }
                dpd1.show();
            }
        });


        tvCampType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<CampTypeModel> campTypeModelArrayList = new ArrayList<>();
                campTypeModelArrayList.add(new CampTypeModel("REGULAR", 1));
//                campTypeModelArrayList.add(new CampTypeModel("CSC REGULAR CAMP", 2));
//                campTypeModelArrayList.add(new CampTypeModel("FLEXI CAMP", 5));
                //                campTypeModelArrayList.add(new CampTypeModel("CSC", 2));

                showCampType(campTypeModelArrayList);

            }
        });



        tvCampId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CampDate != null) {
                    if (Utilities.isNetworkAvailable(mContext)) {
                        new GetCampList().execute(CampDate, EmpCode, String.valueOf(distLgdCode), String.valueOf(campTypeId), "0");
                    } else {
                        Utilities.showToastMessage("Please Check Your Connection", mContext, false);
                    }
                } else {
                    tvDate.setError("Select Camp Date");
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (Utilities.isNetworkAvailable(mContext)) {
//                    CampCreationActivityV4.campCreationModel.setSelectedDevice(allSelSubDeviceListModel);
//
//                    campDetails.setCampId(String.valueOf(CampId));
//                    campDetails.setExpectedBeneficiary(Integer.valueOf(expectedBeneficiary));
//                    campDetails.setDistLgdCode(distLgdCode);
//                    campDetails.setCampDate(tvDate.getText().toString());
//                    campDetails.setLabCode(LabCode);
//
//
//                    CampCreationActivityV4.campCreationModel.setCampDetails(campDetails);
//
//
//                    fragmentChange.onFragmentChange(1);
//
//                    return;
//                }

                if (tvCampType.getText().toString().equals("")){
                    Utilities.showAlertDialog(mContext,"Alert","please select camp type",false);
                    return;
                }
                if (tvCampId.getText().toString().equals("")){
                    Utilities.showAlertDialog(mContext,"Alert","please select campId",false);
                    return;
                }

                int deviceRq = 0;
                for (DeviceListModel.Output o :
                        deviceListModel.getOutput()) {
                    deviceRq = deviceRq + o.getRequiredDevice();

                }
                for (int i = 0; i < deviceListModel.getOutput().size(); i++) {

                    DeviceListAdapter.DeviceListViewHolder myViewHolder = (DeviceListAdapter.DeviceListViewHolder) rvDeviceList.findViewHolderForAdapterPosition(i);

                    if (myViewHolder != null) {
                        try {
//                        assert myViewHolder != null;
                            if (myViewHolder.edtSubDevices.getText().toString().isEmpty()) {
                                myViewHolder.edtSubDevices.setError("Select " + deviceListModel.getOutput().get(i).getRequiredDevice() + " device");
                                return;
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
//                if (CampCreationActivityV4.campCreationModel.getSelectedDevice() == null) {
                if (allSelSubDeviceListModel.size() >= deviceRq) {
                    Utilities.showAlertDialog(mContext, "Devices", "You have selected " + allSelSubDeviceListModel.size() + " Devices for this camp", true, "Next", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            CampCreationActivityV4.campCreationModel.setSelectedDevice(allSelSubDeviceListModel);

                            campDetails.setCampId(String.valueOf(CampId));
                            campDetails.setExpectedBeneficiary(Integer.valueOf(expectedBeneficiary));
                            campDetails.setDistLgdCode(distLgdCode);
                            campDetails.setCampDate(tvDate.getText().toString());
                            campDetails.setLabCode(LabCode);
                            campDetails.setEmpCode(EmpCode);
                            campDetails.setSkipFlag("0");



                            CampCreationActivityV4.campCreationModel.setCampDetails(campDetails);

                            fragmentChange.onFragmentChange(1);

                        }
                    });
                } else {
                    Utilities.showAlertDialog(mContext, "Alert", "Please select required devices (" + deviceRq + ") for camp", false);
                }
//                } else {
//                    fragmentChange.onFragmentChange(3);
//
//                }
            }
        });

        
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (tvCampType.getText().toString().equals("")){
                    Utilities.showAlertDialog(mContext,"Alert","please select camp type",false);
                    return;
                }
                if (tvCampId.getText().toString().equals("")){
                    Utilities.showAlertDialog(mContext,"Alert","please select campId",false);
                    return;
                }



                if (Utilities.isNetworkAvailable(mContext)) {
                    CampCreationActivityV4.campCreationModel.setSelectedDevice(allSelSubDeviceListModel);

                    campDetails.setCampId(String.valueOf(CampId));
                    campDetails.setExpectedBeneficiary(Integer.valueOf(expectedBeneficiary));
                    campDetails.setDistLgdCode(distLgdCode);
                    campDetails.setCampDate(tvDate.getText().toString());
                    campDetails.setLabCode(LabCode);
                    campDetails.setEmpCode(EmpCode);
                    campDetails.setSkipFlag("1");


                    CampCreationActivityV4.campCreationModel.setCampDetails(campDetails);


                    fragmentChange.onFragmentChange(1);

                    return;
                }

                int deviceRq = 0;
                for (DeviceListModel.Output o :
                        deviceListModel.getOutput()) {
                    deviceRq = deviceRq + o.getRequiredDevice();

                }
                for (int i = 0; i < deviceListModel.getOutput().size(); i++) {

                    DeviceListAdapter.DeviceListViewHolder myViewHolder = (DeviceListAdapter.DeviceListViewHolder) rvDeviceList.findViewHolderForAdapterPosition(i);

                    if (myViewHolder != null) {
                        try {
//                        assert myViewHolder != null;
                            if (myViewHolder.edtSubDevices.getText().toString().isEmpty()) {
                                myViewHolder.edtSubDevices.setError("Select " + deviceListModel.getOutput().get(i).getRequiredDevice() + " device");
                                return;
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
//                if (CampCreationActivityV4.campCreationModel.getSelectedDevice() == null) {
                if (allSelSubDeviceListModel.size() >= deviceRq) {
                    Utilities.showAlertDialog(mContext, "Devices", "You have selected " + allSelSubDeviceListModel.size() + " Devices for this camp", true, "Next", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            CampCreationActivityV4.campCreationModel.setSelectedDevice(allSelSubDeviceListModel);

                            campDetails.setCampId(String.valueOf(CampId));
                            campDetails.setExpectedBeneficiary(Integer.valueOf(expectedBeneficiary));
                            campDetails.setDistLgdCode(distLgdCode);
                            campDetails.setCampDate(tvDate.getText().toString());
                            campDetails.setLabCode(LabCode);
                            campDetails.setEmpCode(EmpCode);
                            campDetails.setSkipFlag("0");



                            CampCreationActivityV4.campCreationModel.setCampDetails(campDetails);

                            fragmentChange.onFragmentChange(1);
                        }
                    });
                } else {
                    Utilities.showAlertDialog(mContext, "Alert", "Please select required devices (" + deviceRq + ") for camp", false);
                }
//                } else {
//                    fragmentChange.onFragmentChange(3);
//
//                }
            }
        });
    }


    void getDevices() {
        progressBar.setVisibility(View.VISIBLE);
        rvDeviceList.setVisibility(View.GONE);
        apiService.getDeviceListForCamp(Integer.parseInt(expectedBeneficiary)).enqueue(new Callback<DeviceListModel>() {
            @Override
            public void onResponse(Call<DeviceListModel> call, Response<DeviceListModel> response) {
                progressBar.setVisibility(View.GONE);
                rvDeviceList.setVisibility(View.VISIBLE);
                Log.i(TAG, "onResponse: " + response);
                if (response.isSuccessful()) {
                    deviceListModel = response.body();
                    if (deviceListModel.getStatus().equalsIgnoreCase("success")) {
                        if (deviceListModel.getOutput().size() > 0) {
                            allSelSubDeviceListModel.clear();
                            Collections.sort(deviceListModel.getOutput(), (o1, o2) -> o1.getDeviceName().compareTo(o2.getDeviceName()));
                            deviceListAdapter = new DeviceListAdapter(deviceListModel.getOutput(), DeviceAllocationFragment.this);
                            rvDeviceList.setAdapter(deviceListAdapter);
                        } else {
                            rvDeviceList.setVisibility(View.GONE);
                            tvDeviceError.setVisibility(View.VISIBLE);
                        }
                    } else {
                        rvDeviceList.setVisibility(View.GONE);
                        tvDeviceError.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<DeviceListModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                tvDeviceError.setVisibility(View.VISIBLE);

                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    public void onDeviceSelected(DeviceListModel.Output output) {
        getSubDevices(output);
    }

    @Override
    public void onApproveDeviceSelected(DeviceDetailsForApprovalModel.Output output) {

    }

    void getSubDevices(DeviceListModel.Output device) {
        if (device.getSubDevices() == null) {

            apiService.getSubDeviceListForCamp(device.getDevicesId(), CampDate, distLgdCode, Integer.parseInt(LabCode)).enqueue(new Callback<SubDeviceListModel>() {
                @Override
                public void onResponse(Call<SubDeviceListModel> call, Response<SubDeviceListModel> response) {

                    Log.i(TAG, "onResponse: " + new Gson().toJson(response.body()));
                    if (response.isSuccessful()) {
                        subDeviceListModel = response.body();
                        if (subDeviceListModel.getStatus().equalsIgnoreCase("success")) {
                            if (subDeviceListModel.getOutput().size() > 0) {
                                Collections.sort(subDeviceListModel.getOutput(), (o1, o2) -> o1.getDeviceCompName().compareTo(o2.getDeviceCompName()));
                                showSubDeviceDialog(device);
                            }
                        } else {
                            Utilities.showToastMessage("Devices not available", mContext, false);
                        }
                    }
                }

                @Override
                public void onFailure(Call<SubDeviceListModel> call, Throwable t) {

                    Log.e(TAG, "onFailure: " + t.getMessage());
                }
            });
        } else {
            subDeviceListModel.setOutput(device.getSubDevices());
            showSubDeviceDialog(device);

        }
    }


    public class GetCampList extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("CampDATE", params[0]));
            param.add(new ParamsPojo("UserId", params[1]));
            param.add(new ParamsPojo("DISTLGDCODE", params[2]));
            param.add(new ParamsPojo("CampType", params[3]));
            param.add(new ParamsPojo("LABCODE", params[4]));

            res = WebServiceCall.APICall(ApplicationConstants.GetCampList_V3, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    campList = new ArrayList<>();
                    List<CampListModel.OutputBean> campList = new ArrayList<>();

                    //  Log.d("result ",result.toString());

                    CampListModel campListModel = new Gson().fromJson(result, CampListModel.class);
                    type = campListModel.getStatus();
                    message = campListModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {

                        campList = campListModel.getOutput();

                        if (campList.size() > 0) {
                            // districtList.add(0, new DistrictList_Model("0", "All"));

                            showCampListDialog(campList);
                        } else {
                            Utilities.showToastMessage(R.string.msgt_emptylist, mContext, false);
                        }
                    } else {
                        Utilities.showAlertDialog(mContext, "Fail", message, false);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(mContext, "Please Try Again", "Camp I'd not available", false);
            }
        }
    }


    private void showSubDeviceDialog(DeviceListModel.Output device) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.dialog_check_list, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(view);
        builder.setTitle("Select Devices");
        builder.setCancelable(false);

        RecyclerView rv_checklist = view.findViewById(R.id.rv_checklist);
        rv_checklist.setLayoutManager(new LinearLayoutManager(mContext));
        rv_checklist.setAdapter(new SubDeviceListAdapter());


        AlertDialog alertDialog = builder.create();
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Select", (dialog, which) -> {


            int checkedTestCount = 0;
            StringBuilder selectedSubCategories = new StringBuilder();
            ArrayList<SubDeviceListModel.Output> selSubDeviceListModel = new ArrayList<SubDeviceListModel.Output>();
            selSubDeviceListModel.addAll(subDeviceListModel.getOutput());

            for (SubDeviceListModel.Output output : subDeviceListModel.getOutput()) {

                if (output.isChecked()) {
                    checkedTestCount = checkedTestCount + 1;
                    if (!allSelSubDeviceListModel.contains(output)) {
                        allSelSubDeviceListModel.add(output);
                    }
                } else {
                    if (allSelSubDeviceListModel.contains(output)) {
                        allSelSubDeviceListModel.remove(output);
                    }
                }
            }


            if (checkedTestCount < device.getRequiredDevice()) {
                Utilities.showToastMessage("Please select " + device.getRequiredDevice() + " for this camp", mContext, false);
            }
            device.setSubDevices(selSubDeviceListModel);
            rvDeviceList.setAdapter(deviceListAdapter);
//            deviceListAdapter.notifyDataSetChanged();


        });

        alertDialog.show();

    }


    private void showCampType(final ArrayList<CampTypeModel> campTypeModelsList) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(mContext);
        builderSingle.setTitle("Select Camp Type");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(mContext, R.layout.list_row);

        for (int i = 0; i < campTypeModelsList.size(); i++) {
            arrayAdapter.add(String.valueOf(campTypeModelsList.get(i).getCampTypeName()));
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
                tvCampType.setText(campTypeModelsList.get(which).getCampTypeName());
                campTypeId = campTypeModelsList.get(which).getCampTypeId();


                //  refreshCalendar();


            }
        });
        builderSingle.show();
    }


    private void showCampListDialog(final List<CampListModel.OutputBean> campList) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(mContext);
        builderSingle.setTitle("Select Camp");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(mContext, R.layout.list_row);

        for (int i = 0; i < campList.size(); i++) {
            arrayAdapter.add(String.valueOf(campList.get(i).getCampId() + "(" + campList.get(i).getLabName() + ")" +"("+campList.get(i).getCampCreatedBy()+")"));
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
                tvCampId.setText(campList.get(which).getCampId());
                CampId = campList.get(which).getCampId();
                CampDate = campList.get(which).getCampDate();
                expectedBeneficiary = campList.get(which).getExpectedbeneficiarycount();
                LabCode = campList.get(which).getLABCODE();
                isMappingFlag = campList.get(which).getResourceMappingFlag();
                createdBy = campList.get(which).getCreatedBy();


//                if (!(EmpCode == createdBy)){
//                    Utilities.showAlertDialog(mContext,"Alert","This camp not created by you,please select another camp",false);
//                }

                if (!EmpCode.equalsIgnoreCase(createdBy)){
                    Utilities.showAlertDialog(mContext,"Alert","This camp not created by you,please select created camp",false);
                    tvCampId.setText("");
                    CampId = "";
                    return;
                }

                if (isMappingFlag.equalsIgnoreCase("1")){
                    Utilities.showAlertDialog(mContext,"Alert","Device and resource mapping already done for selected camp",false);
                    tvCampId.setText("");
                    CampId = "";
                }else {
                    getDevices();
                }



            }
        });

        builderSingle.show();

    }


    private class SubDeviceListAdapter extends RecyclerView.Adapter<SubDeviceListAdapter.MyViewHolder> {

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.sub_device_list_item, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int pos) {
            final int position = holder.getAdapterPosition();

            holder.cb_select.setText(subDeviceListModel.getOutput().get(position).getDeviceCompName());
            holder.tvSerialNumber.setText("Serial Number:- " + subDeviceListModel.getOutput().get(position).getDeviceSerial());

            if (subDeviceListModel.getOutput().get(position).isChecked()) {
                holder.cb_select.setChecked(true);
            }

            holder.cb_select.setOnCheckedChangeListener((buttonView, isChecked) -> {
                subDeviceListModel.getOutput().get(position).setChecked(isChecked);
            });
        }

        @Override
        public int getItemCount() {
            return subDeviceListModel.getOutput().size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            private CheckBox cb_select;
            private TextView tvSerialNumber;

            public MyViewHolder(@NonNull View view) {
                super(view);
                cb_select = view.findViewById(R.id.cb_select);
                tvSerialNumber = view.findViewById(R.id.tvSerialNumber);
            }
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }
    }


}