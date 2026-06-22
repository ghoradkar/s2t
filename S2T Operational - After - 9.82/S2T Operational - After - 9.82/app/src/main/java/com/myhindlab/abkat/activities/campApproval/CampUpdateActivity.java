package com.myhindlab.abkat.activities.campApproval;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.campApproval.adapter.ResourceListForApprovalAdapter;
import com.myhindlab.abkat.activities.campApproval.adapter.SiteListAdapter;
import com.myhindlab.abkat.activities.campApproval.models.AllocatedResourceListModel;
import com.myhindlab.abkat.activities.campApproval.models.CampDetailsModel;
import com.myhindlab.abkat.activities.campApproval.models.CampOrganizedListModel;
import com.myhindlab.abkat.activities.campApproval.models.ResourceDetailsForApprovalModel;
import com.myhindlab.abkat.activities.regularcampcreation.adapter.ConsumableListAdapter;
import com.myhindlab.abkat.activities.regularcampcreation.adapter.DeviceListAdapter;
import com.myhindlab.abkat.activities.regularcampcreation.model.AllocatedSubDeviceListModel;
import com.myhindlab.abkat.activities.regularcampcreation.model.ConsumableDetailsForApprovalModel;
import com.myhindlab.abkat.activities.regularcampcreation.model.DeviceDetailsForApprovalModel;
import com.myhindlab.abkat.activities.regularcampcreation.model.DeviceListModel;
import com.myhindlab.abkat.activities.regularcampcreation.model.GetLabOnDistrictListModel;
import com.myhindlab.abkat.activities.regularcampcreation.model.ProductStockListModel;
import com.myhindlab.abkat.activities.regularcampcreation.model.SiteListModel;
import com.myhindlab.abkat.activities.regularcampcreation.model.UserResponseModel;
import com.myhindlab.abkat.models.UserDetailsModel;
import com.myhindlab.abkat.rest.ApiClient;
import com.myhindlab.abkat.rest.ApiInterface;
import com.myhindlab.abkat.rest.MediProcAPI;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.rengwuxian.materialedittext.MaterialEditText;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CampUpdateActivity extends AppCompatActivity implements SiteListAdapter.SiteListEvent, DeviceListAdapter.DeviceEvent, ResourceListForApprovalAdapter.ResourceListForApprovalEvent {

    //Site List
    private UserSessionManager userSessionManager;
    private Context mContext;
    private int distLgdCode,selectedResorce;
    private RecyclerView rvSitesList;
    private final String TAG = CampUpdateActivity.class.getSimpleName();
    private SearchView siteSearchView;
    private SiteListModel siteListModel;
    private ArrayList<SiteListModel.Output> searchList;
    private Button btnNext;
    private List<SiteListModel.Output> selSites;
    private MaterialCardView cvSiteList;
    private boolean siteSectionVisibility = true;
    private ConstraintLayout clSiteList;
    private TextView tvSiteList, tvSiteError;

    //Camp Details
    private MaterialEditText edt_district, edt_camp_address, edt_camp_name, edt_camp_date,
            edt_post_camp_date, edt_lab, edt_hospital, edt_aarogyamitra_name, edt_aarogyamitra_mobile, edt_screening_tests,
            edt_lab_1, edt_tests_for_lab_1, edt_lab_2, edt_tests_for_lab_2, edt_lab_3, edt_tests_for_lab_3, edt_camp_organized_by, edt_expected_beneficiary, edt_sel_address;
    private ImageView imv_camp_approval_letter;
    private TextView tvExpectedBeneficiary;
    private Button btn_save;
    private Uri imageURI;
    private final int CAMERA_REQUEST = 100;
    private File patientPicsFolder;
    private String userId, labCode, labCodeOne, labCodeTwo, hospitalId, imagePath = "", labCodeThree;
    private int mYear, mMonth, mDay, campOrgId;
    private ArrayList<CampOrganizedListModel.Output> campOrganizedListModel;
    private CampDetailsModel.Output campDetailsModel;
    private MaterialCardView cvCampDetails;
    private LinearLayout llCampDetails;
    private TextView tvCampDetails;
    private boolean campDetailsSectionVisibility = false;


    //Device List
    private TextView tvDeviceError;
    private RecyclerView rvDeviceList;
    private ProgressBar progressBar;
    private DeviceDetailsForApprovalModel deviceListModel;
    private AllocatedSubDeviceListModel subDeviceListModel;
    private ApiInterface apiService;
    private DeviceListAdapter deviceListAdapter;
    private List<AllocatedSubDeviceListModel.Output> allSelSubDeviceListModel;
    private boolean deviceSectionVisibility = false;
    private MaterialCardView cvDeviceAllocation;
    private ConstraintLayout clDeviceAllocation, clDeviceHeader, clDeviceStatusButton;
    private TextView tvDeviceDetails;
    private String deviceRemark;
    private int isDeviceApprove = 2;


    //Consumables
    private TextView tvError, tvConsumableMsg, tvConsumable;
    private RecyclerView rvConsumableList;
    private ProgressBar consumablesprogressBar;
    private ConsumableDetailsForApprovalModel consumableListModel;
    private ConsumableListAdapter consumableListAdapter;
    private ConstraintLayout clConsumableStatusButton, clConsumable;
    private MaterialCardView cvConsumable, cvConsumableHeader;
    private boolean consumableSectionVisibility = false;
    private String consumableRemark;
    private int isConsumableApprove = 2;

    private ConsumableDetailsForApprovalModel consumableDetailsForApprovalModel;
    private MediProcAPI mediProcAPI;
    private ProductStockListModel productStockListModel;


    //Resource Allocation
    private TextView tvResourceError, tvResource;
    private RecyclerView rvDesignation;
    private Button btnSubmit;
    private ProgressBar resourceProgressBar;
    private ResourceDetailsForApprovalModel designationListModel;
    private AllocatedResourceListModel resourceListModel;
    private AllocatedResourceListModel allocatedResourceListModel;
    private List<AllocatedResourceListModel.Output> filteredResourceModel;
    private ResourceListForApprovalAdapter resourceListAdapter;
    private List<AllocatedResourceListModel.Output> allSelResourceModel;
    private ConstraintLayout clResourceStatusButton, clResource;
    private MaterialCardView cvResource;
    private CardView cvResourceHeader;
    private boolean resourceSectionVisibility = false;
    private String resourceRemark;
    private int isResourceApprove = 2;
    private ResourceDetailsForApprovalModel resourceDetailsForApprovalModel;


    private int campId, campStatus;
    private String campDate;
    private AlertDialog progressDialog;
    private Button btnFinalApproval;

    private ConstraintLayout constraintLayout6;
    private String campType;
    private boolean isPartnerCamp, IsCSCVLECamp;
    UserDetailsModel.Output userResponseModel;
//    UserResponseModel userResponseModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camp_update);

        setUpToolbar();
        init();
        getSessionDetails();
        clickEvent();
    }

    void init() {
        mContext = CampUpdateActivity.this;
        userSessionManager =new UserSessionManager(mContext);
        apiService = ApiClient.getD2DClient().create(ApiInterface.class);
        mediProcAPI = ApiClient.getMediProcClient().create(MediProcAPI.class);
        allSelSubDeviceListModel = new ArrayList<AllocatedSubDeviceListModel.Output>();
        allSelResourceModel = new ArrayList<>();

        selSites = new ArrayList<>();
        progressDialog = Utilities.ProgressDialog(mContext, "Please Wait..").create();

        //Site List
        siteSearchView = findViewById(R.id.siteSearchView);
        rvSitesList = findViewById(R.id.rvSiteList);
        rvSitesList.setHasFixedSize(true);
        rvSitesList.setLayoutManager(new LinearLayoutManager(mContext));
        btnNext = findViewById(R.id.btnNext);
        cvSiteList = findViewById(R.id.cvSiteList);
        clSiteList = findViewById(R.id.clSiteList);
        tvSiteList = findViewById(R.id.tvSiteList);
        tvSiteError = findViewById(R.id.tvSiteError);
        constraintLayout6 = findViewById(R.id.constraintLayout6);

        //Camp Details
        llCampDetails = findViewById(R.id.llCampDetails);
        cvCampDetails = findViewById(R.id.cvCampDetails);
        tvCampDetails = findViewById(R.id.tvCampDetails);
        edt_district = findViewById(R.id.edt_district);
        edt_camp_address = findViewById(R.id.edt_camp_address);
        edt_camp_name = findViewById(R.id.edt_camp_name);
        edt_camp_date = findViewById(R.id.edt_camp_date);
        edt_post_camp_date = findViewById(R.id.edt_post_camp_date);
        edt_hospital = findViewById(R.id.edt_hospital);
        imv_camp_approval_letter = findViewById(R.id.imv_camp_approval_letter);
        btn_save = findViewById(R.id.btn_save);
        edt_expected_beneficiary = findViewById(R.id.edt_expected_beneficiary);
        edt_camp_organized_by = findViewById(R.id.edt_camp_organized_by);
        tvExpectedBeneficiary = findViewById(R.id.tvExpectedBeneficiary);
        edt_sel_address = findViewById(R.id.edt_sel_address);

        //Device Details
        rvDeviceList = findViewById(R.id.rvDeviceList);
        tvDeviceError = findViewById(R.id.tvDeviceError);
        progressBar = findViewById(R.id.progress_circular);
        rvDeviceList.setHasFixedSize(true);
        rvDeviceList.setLayoutManager(new LinearLayoutManager(mContext));
        cvDeviceAllocation = findViewById(R.id.cvDeviceAllocation);
        clDeviceAllocation = findViewById(R.id.clDeviceAllocation);
        tvDeviceDetails = findViewById(R.id.tvDeviceDetails);
        clDeviceHeader = findViewById(R.id.clDeviceHeader);
        clDeviceStatusButton = findViewById(R.id.clDeviceStatusButton);


        //Consumable
        tvConsumable = findViewById(R.id.tvConsumable);
        cvConsumable = findViewById(R.id.cvConsumables);
        clConsumable = findViewById(R.id.clConsumable);
        cvConsumableHeader = findViewById(R.id.cvConsumableHeader);
        rvConsumableList = findViewById(R.id.rvConsumable);
        clConsumableStatusButton = findViewById(R.id.clConsumableStatusButton);
        tvError = findViewById(R.id.tvError);
        consumablesprogressBar = findViewById(R.id.progressBar);
        rvConsumableList.setHasFixedSize(true);
        rvConsumableList.setLayoutManager(new LinearLayoutManager(mContext));


        //Resource
        rvDesignation = findViewById(R.id.rvResourceList);
        tvResourceError = findViewById(R.id.tvResourceError);
        resourceProgressBar = findViewById(R.id.resourceProgressBar);
        tvResource = findViewById(R.id.tvResource);
        cvResource = findViewById(R.id.cvResourceAllocation);
        clResource = findViewById(R.id.clResource);
        cvResourceHeader = findViewById(R.id.cvResourceHeader);
        clResourceStatusButton = findViewById(R.id.clResourceStatusButton);


        rvDesignation.setHasFixedSize(true);
        rvDesignation.setLayoutManager(new LinearLayoutManager(mContext));

        btnFinalApproval = findViewById(R.id.btnFinalApprove);


    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Camp Update");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getSessionDetails() {
        userResponseModel = userSessionManager.getUserDetailsJson();
        if (getIntent() != null) {
            distLgdCode = Integer.parseInt(getIntent().getStringExtra("campDistLgdCode"));
        }
//        if (userResponseModel.getDesgid() == 84 || userResponseModel.getDesgid() == 100 || userResponseModel.getDesgid() == 102 || userResponseModel.getDesgid() == 83) {
//            btnFinalApproval.setVisibility(View.VISIBLE);
//        } else {
        btnFinalApproval.setVisibility(View.GONE);
//        }
        if (getIntent() != null) {
            campId = Integer.parseInt(getIntent().getStringExtra("campId"));
            labCode = getIntent().getStringExtra("labCode");
            campType = getIntent().getStringExtra("campType");
            campDate = getIntent().getStringExtra("campDate");
            campStatus = getIntent().getIntExtra("campStatus", 0);
            isPartnerCamp = getIntent().getBooleanExtra("isPartnerCamp", false);
            IsCSCVLECamp = getIntent().getBooleanExtra("IsCSCVLECamp", false);


//            if (campStatus == 1) {
//                isDeviceApprove = campStatus;
//                isConsumableApprove = campStatus;
//                isResourceApprove = campStatus;
//
//                btnConsumableApprove.setChecked(true);
//                btnConsumableReject.setEnabled(false);
//                btnConsumableHold.setEnabled(false);
//
//                btnResourceApprove.setChecked(true);
//                btnResourceReject.setEnabled(false);
//                btnResourceHold.setEnabled(false);
//
//                btnDeviceApprove.setChecked(true);
//                btnDeviceReject.setEnabled(false);
//                btnDeviceHold.setEnabled(false);
//
//                edtRemarkDevice.setVisibility(View.GONE);
//                edtRemarkConsumable.setVisibility(View.GONE);
//                edtRemarkResource.setVisibility(View.GONE);
//
//                btnFinalApproval.setVisibility(View.GONE);
//            }
//            if (campType.equalsIgnoreCase("3")) {
//                constraintLayout6.setVisibility(View.GONE);
//            }
//            getSites();


            toggleResourceSection();

            setUpSearch();
        }
    }

    void toggleSiteSection() {
        if (!siteSectionVisibility) {
            getCampDetails();
        }

        siteSectionVisibility = !siteSectionVisibility;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                tvSiteList.setCompoundDrawablesWithIntrinsicBounds(null, null, siteSectionVisibility ? mContext.getResources().getDrawable(R.drawable.ic_baseline_keyboard_arrow_up_24) : mContext.getResources().getDrawable(R.drawable.icon_arrowdown), null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        clSiteList.setVisibility(siteSectionVisibility ? View.VISIBLE : View.GONE);
    }

    void toggleCampDetailsSection() {
        if (!campDetailsSectionVisibility) {
            getCampDetails();
        }

        campDetailsSectionVisibility = !campDetailsSectionVisibility;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                tvCampDetails.setCompoundDrawablesWithIntrinsicBounds(null, null, campDetailsSectionVisibility ? mContext.getResources().getDrawable(R.drawable.ic_baseline_keyboard_arrow_up_24) : mContext.getResources().getDrawable(R.drawable.icon_arrowdown), null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        llCampDetails.setVisibility(campDetailsSectionVisibility ? View.VISIBLE : View.GONE);

    }

    void toggleDeviceSection() {
        if (!deviceSectionVisibility) {
            getDevices();
        }

        deviceSectionVisibility = !deviceSectionVisibility;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                tvDeviceDetails.setCompoundDrawablesWithIntrinsicBounds(null, null, deviceSectionVisibility ? mContext.getResources().getDrawable(R.drawable.ic_baseline_keyboard_arrow_up_24) : mContext.getResources().getDrawable(R.drawable.icon_arrowdown), null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        clDeviceAllocation.setVisibility(deviceSectionVisibility ? View.VISIBLE : View.GONE);

    }
    void toggleConsumableSection() {
        if (!consumableSectionVisibility) {
            getConsumables();
        }

        consumableSectionVisibility = !consumableSectionVisibility;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                tvConsumable.setCompoundDrawablesWithIntrinsicBounds(null, null, consumableSectionVisibility ? mContext.getResources().getDrawable(R.drawable.ic_baseline_keyboard_arrow_up_24) : mContext.getResources().getDrawable(R.drawable.icon_arrowdown), null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        clConsumable.setVisibility(consumableSectionVisibility ? View.VISIBLE : View.GONE);
    }

    void toggleResourceSection() {
        if (!resourceSectionVisibility) {
            getResourcesForApproval();
        }

        resourceSectionVisibility = !resourceSectionVisibility;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                tvResource.setCompoundDrawablesWithIntrinsicBounds(null, null, resourceSectionVisibility ? mContext.getResources().getDrawable(R.drawable.ic_baseline_keyboard_arrow_up_24) : mContext.getResources().getDrawable(R.drawable.icon_arrowdown), null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        clResource.setVisibility(resourceSectionVisibility ? View.VISIBLE : View.GONE);
    }

    void clickEvent() {

        cvSiteList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSiteSection();
            }
        });

        cvCampDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleCampDetailsSection();
            }
        });

        cvDeviceAllocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleDeviceSection();
            }
        });

        cvConsumable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleConsumableSection();
            }
        });

        cvResource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleResourceSection();
            }
        });

    }

    void validateInput() {

    }

    void submitData() {

        Gson gson = new GsonBuilder().serializeNulls().addSerializationExclusionStrategy(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {

                Log.i(TAG, "shouldSkipField: " + f.getName());
                if (f.getName().equalsIgnoreCase("deviceName") || f.getName().equalsIgnoreCase("deviceSerialNumbers")
                        || f.getName().equalsIgnoreCase("devicecount") || f.getName().equalsIgnoreCase("") ||
                        f.getName().equalsIgnoreCase("expectedQuantity") || f.getName().equalsIgnoreCase("productName")
                        || f.getName().equalsIgnoreCase("requiredQuantity") || f.getName().equalsIgnoreCase("stockAsPerPhelbo")
                        || f.getName().equalsIgnoreCase("desgName") || f.getName().equalsIgnoreCase("AVAILABELSTOCK") ||
                        f.getName().equalsIgnoreCase("resourceName") || f.getName().equalsIgnoreCase("testId") || f.getName().equalsIgnoreCase("testName") || f.getName().equalsIgnoreCase("RequiredDevice") || f.getName().equalsIgnoreCase("resources") || f.getName().equalsIgnoreCase("subDevices")

                ) {
                    return true;

                } else
                    return false;
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        }).create();


        Log.i(TAG, "Device: " + gson.toJson(deviceListModel.getOutput()));
        Log.i(TAG, "Consumable: " + gson.toJson(consumableDetailsForApprovalModel.getOutput()));
        Log.i(TAG, "Resource: " + gson.toJson(resourceDetailsForApprovalModel.getOutput()));
        String deviceJson = gson.toJson(deviceListModel.getOutput());
        String consumableJson = gson.toJson(consumableDetailsForApprovalModel.getOutput());
        String resourceJson = gson.toJson(resourceDetailsForApprovalModel.getOutput());
        progressDialog.show();
        apiService.insertInternalCampApproval(deviceJson, resourceJson).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();

                try {
//                    Log.d(TAG, "onResponse: " + response.body().string());

                    if (response.isSuccessful()) {
                        String res = response.body().string();
                        JSONObject jsonObject = new JSONObject(res);
                        String status = jsonObject.getString("status");
//                        {"status":"Success","message":"1"}
                        if (status.equalsIgnoreCase("Success")) {
                            Utilities.showToastMessage("Status submitted successfully", mContext, true);
                            setResult(1);
                            finish();
//                            Gson gson = new GsonBuilder().addSerializationExclusionStrategy(new ExclusionStrategy() {
//                                        @Override
//                                        public boolean shouldSkipField(FieldAttributes f) {
//                                            if (f.getName().equalsIgnoreCase("desgid") || f.getName().equalsIgnoreCase("AVAILABELSTOCK") || f.getName().equalsIgnoreCase("Expectedbeneficiarycount")) {
//                                                return true;
//                                            }
//                                            return false;
//                                        }
//
//                                        @Override
//                                        public boolean shouldSkipClass(Class<?> clazz) {
//                                            return false;
//                                        }
//                                    })
////                                    .excludeFieldsWithoutExposeAnnotation()
//                                    .serializeNulls()
//                                    .create();

                            String consumableMappingJson = gson.toJson(consumableDetailsForApprovalModel.getOutput());
                            String deviceMappingJson = gson.toJson(deviceListModel.getOutput());
                            String resourceMappingJson = gson.toJson(resourceDetailsForApprovalModel.getOutput());

//                            apiService.updateCampCreationResourceDeviceConsumptionDetails(deviceMappingJson, consumableMappingJson, resourceMappingJson, userId, String.valueOf(campId)).enqueue(new Callback<ResponseBody>() {
//                                @Override
//                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                                    try {
//                                        Log.d(TAG, "onResponse: " + response.body().string());
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
//                                    if (response.isSuccessful()) {
//                                        setResult(1);
//                                        finish();
//                                    }
//                                }
//
//                                @Override
//                                public void onFailure(Call<ResponseBody> call, Throwable t) {
//                                    Log.e(TAG, "updateCampCreation fail: " + t.getMessage());
//                                }
//                            });


                        } else {
                            String msg = null;
                            try {
                                msg = jsonObject.getString("ExceptionValue");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Utilities.showToastMessage(msg, mContext, false);

                        }


                    }

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Log.e(TAG, "onFailure: " + t.getMessage());

            }
        });
    }

    void submitChannelPartnerData() {

        Gson gson = new GsonBuilder().serializeNulls().addSerializationExclusionStrategy(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {

                Log.i(TAG, "shouldSkipField: " + f.getName());
                if (f.getName().equalsIgnoreCase("deviceName") || f.getName().equalsIgnoreCase("deviceSerialNumbers")
                        || f.getName().equalsIgnoreCase("devicecount") || f.getName().equalsIgnoreCase("") ||
                        f.getName().equalsIgnoreCase("expectedQuantity") || f.getName().equalsIgnoreCase("productName")
                        || f.getName().equalsIgnoreCase("requiredQuantity") || f.getName().equalsIgnoreCase("stockAsPerPhelbo")
                        || f.getName().equalsIgnoreCase("desgName") || f.getName().equalsIgnoreCase("AVAILABELSTOCK") ||
                        f.getName().equalsIgnoreCase("resourceName") || f.getName().equalsIgnoreCase("testName") || f.getName().equalsIgnoreCase("RequiredDevice") || f.getName().equalsIgnoreCase("resources") || f.getName().equalsIgnoreCase("subDevices")

                ) {
                    return true;

                } else
                    return false;
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        }).create();

//        Log.i(TAG, "Device: " + gson.toJson(deviceListModel.getOutput()));
//        Log.i(TAG, "Consumable: " + gson.toJson(consumableDetailsForApprovalModel.getOutput()));
        Log.i(TAG, "Resource: " + gson.toJson(resourceDetailsForApprovalModel.getOutput()));
//        String deviceJson = gson.toJson(deviceListModel.getOutput());
//        String consumableJson = gson.toJson(consumableDetailsForApprovalModel.getOutput());
        String resourceJson = gson.toJson(resourceDetailsForApprovalModel.getOutput());
        progressDialog.show();
        apiService.insertInternalCampApprovalForPartner("[]", "[]", resourceJson).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();

                try {
//                    Log.d(TAG, "onResponse: " + response.body().string());

                    if (response.isSuccessful()) {
                        String res = response.body().string();
                        JSONObject jsonObject = new JSONObject(res);
                        String status = jsonObject.getString("status");
//                        {"status":"Success","message":"1"}
                        if (status.equalsIgnoreCase("Success")) {
                            Utilities.showToastMessage("Status submitted successfully", mContext, true);
                            setResult(1);
                            finish();
//                            Gson gson = new GsonBuilder().addSerializationExclusionStrategy(new ExclusionStrategy() {
//                                        @Override
//                                        public boolean shouldSkipField(FieldAttributes f) {
//                                            if (f.getName().equalsIgnoreCase("desgid") || f.getName().equalsIgnoreCase("AVAILABELSTOCK") || f.getName().equalsIgnoreCase("Expectedbeneficiarycount")) {
//                                                return true;
//                                            }
//                                            return false;
//                                        }
//
//                                        @Override
//                                        public boolean shouldSkipClass(Class<?> clazz) {
//                                            return false;
//                                        }
//                                    })
////                                    .excludeFieldsWithoutExposeAnnotation()
//                                    .serializeNulls()
//                                    .create();

//                            String consumableMappingJson = gson.toJson(consumableDetailsForApprovalModel.getOutput());
//                            String deviceMappingJson = gson.toJson(deviceListModel.getOutput());
//                            String resourceMappingJson = gson.toJson(resourceDetailsForApprovalModel.getOutput());

//                            apiService.updateCampCreationResourceDeviceConsumptionDetails(deviceMappingJson, consumableMappingJson, resourceMappingJson, userId, String.valueOf(campId)).enqueue(new Callback<ResponseBody>() {
//                                @Override
//                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                                    try {
//                                        Log.d(TAG, "onResponse: " + response.body().string());
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
//                                    if (response.isSuccessful()) {
//                                        setResult(1);
//                                        finish();
//                                    }
//                                }
//
//                                @Override
//                                public void onFailure(Call<ResponseBody> call, Throwable t) {
//                                    Log.e(TAG, "updateCampCreation fail: " + t.getMessage());
//                                }
//                            });


                        } else {
                            String msg = null;
                            try {
                                msg = jsonObject.getString("ExceptionValue");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Utilities.showToastMessage(msg, mContext, false);

                        }


                    }

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    Utilities.showToastMessage("Unable to update camp status", mContext, false);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Log.e(TAG, "onFailure: " + t.getMessage());
                Utilities.showToastMessage("Unable to update camp status", mContext, false);


            }
        });
    }


    void getSites() {
        apiService.getCampSiteDetails_ForAppForIntApproval(campId).enqueue(new Callback<SiteListModel>() {
            @Override
            public void onResponse(Call<SiteListModel> call, Response<SiteListModel> response) {

                Log.i(TAG, "onResponse: " + response);
                if (response.isSuccessful()) {
                    siteListModel = response.body();
                    if (siteListModel.getStatus().equalsIgnoreCase("success")) {
                        tvSiteError.setVisibility(View.GONE);
                        if (!siteListModel.getOutput().isEmpty()) {
                            Collections.sort(siteListModel.getOutput(), (o1, o2) -> o1.getSiteName().toLowerCase().compareTo(o2.getSiteName().toLowerCase()));
                            rvSitesList.setAdapter(new SiteListAdapter(siteListModel.getOutput(), CampUpdateActivity.this::onSiteSelected, 1));

                        } else {
                            Utilities.showToastMessage("No sites available", mContext, false);
                            tvSiteError.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Utilities.showToastMessage("No sites available", mContext, false);
                        tvSiteError.setVisibility(View.VISIBLE);

                    }
                }
            }

            @Override
            public void onFailure(Call<SiteListModel> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
                tvSiteError.setVisibility(View.VISIBLE);

            }
        });
    }

    void setUpSearch() {
        siteSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchList = new ArrayList<>();
                if (!query.isEmpty()) {
                    for (SiteListModel.Output site :
                            siteListModel.getOutput()) {
                        if (site.getSiteName().toLowerCase().contains(query.toLowerCase())) {
                            searchList.add(site);
                        }
                    }
                    Log.i(TAG, "onQueryTextSubmit: " + searchList.size());

                    if (searchList.size() > 0) {
                        rvSitesList.setAdapter(new SiteListAdapter(searchList, CampUpdateActivity.this::onSiteSelected, 1));

                    } else {
                        rvSitesList.setAdapter(new SiteListAdapter(siteListModel.getOutput(), CampUpdateActivity.this::onSiteSelected, 1));

                    }
                } else {
                    if (siteListModel != null)
                        rvSitesList.setAdapter(new SiteListAdapter(siteListModel.getOutput(), CampUpdateActivity.this::onSiteSelected, 1));

                }
                return true;

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList = new ArrayList<>();
                if (!newText.isEmpty()) {
                    for (SiteListModel.Output site :
                            siteListModel.getOutput()) {
                        if (site.getSiteName().toLowerCase().contains(newText.toLowerCase())) {
                            searchList.add(site);
                        }

                    }
                    Log.i(TAG, "onQueryTextChange: " + searchList.size());
                    if (searchList.size() > 0) {
                        rvSitesList.setAdapter(new SiteListAdapter(searchList, CampUpdateActivity.this::onSiteSelected, 1));

                    } else {
                        rvSitesList.setAdapter(new SiteListAdapter(siteListModel.getOutput(), CampUpdateActivity.this::onSiteSelected, 1));

                    }
                } else {
                    if (siteListModel != null)
                        rvSitesList.setAdapter(new SiteListAdapter(siteListModel.getOutput(), CampUpdateActivity.this::onSiteSelected, 1));

                }

                return true;
            }
        });

    }

    //Camp Details
    void getCampDetails() {
        progressDialog.show();
        apiService.getCampDetails_ForAppForIntApproval(campId).enqueue(new Callback<CampDetailsModel>() {
            @Override
            public void onResponse(Call<CampDetailsModel> call, Response<CampDetailsModel> response) {
                progressDialog.dismiss();

                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase("success")) {

                        campDetailsModel = response.body().getOutput().get(0);
                        edt_district.setText(campDetailsModel.getDistrict());
                        edt_camp_name.setText(campDetailsModel.getCampName());
                        edt_camp_address.setText(campDetailsModel.getCampLocation());
                        Calendar c = Calendar.getInstance();
                        try {
                            c.setTime(Utilities.dfDate.parse(campDetailsModel.getCampDate()));
                            c.add(Calendar.DAY_OF_MONTH, 7);
                            edt_camp_date.setText(Utilities.dfDate2.format(Utilities.dfDate.parse(campDetailsModel.getCampDate())));
                            edt_post_camp_date.setText(Utilities.dfDate2.format(c.getTimeInMillis()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        tvExpectedBeneficiary.setText(String.valueOf("Expected Beneficiary (" + campDetailsModel.getExpectedbeneficiarycount() + ")"));

                    } else {
                        toggleCampDetailsSection();
                        Utilities.showToastMessage("Camp Details not found", mContext, false);
                    }
                }
            }

            @Override
            public void onFailure(Call<CampDetailsModel> call, Throwable t) {
                toggleCampDetailsSection();

                progressDialog.dismiss();
                Utilities.showToastMessage("Server not responding", mContext, false);

            }
        });
    }

    //Device Details
    void getDevices() {
        progressBar.setVisibility(View.VISIBLE);
        rvDeviceList.setVisibility(View.GONE);
        clDeviceHeader.setVisibility(View.GONE);
        clDeviceStatusButton.setVisibility(View.GONE);

        apiService.getCampDeviceDetails_ForAppForIntApproval(campId).enqueue(new Callback<DeviceDetailsForApprovalModel>() {
            @Override
            public void onResponse(Call<DeviceDetailsForApprovalModel> call, Response<DeviceDetailsForApprovalModel> response) {
                progressBar.setVisibility(View.GONE);
                Log.i(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
                    deviceListModel = response.body();
                    if (deviceListModel.getStatus().equalsIgnoreCase("success")) {

                        if (deviceListModel.getOutput().size() > 0) {
                            Collections.sort(deviceListModel.getOutput(), (o1, o2) -> o1.getDeviceName().compareTo(o2.getDeviceName()));
                            deviceListAdapter = new DeviceListAdapter(deviceListModel.getOutput(), CampUpdateActivity.this, 1);
                            rvDeviceList.setAdapter(deviceListAdapter);
                            rvDeviceList.setVisibility(View.VISIBLE);
                            clDeviceHeader.setVisibility(View.VISIBLE);
                            clDeviceStatusButton.setVisibility(View.VISIBLE);


                        } else {
                            tvDeviceError.setVisibility(View.VISIBLE);
                        }
                    } else {
                        tvDeviceError.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<DeviceDetailsForApprovalModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                tvDeviceError.setVisibility(View.VISIBLE);

                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }


    //Consumable
    void getConsumables() {
        consumablesprogressBar.setVisibility(View.VISIBLE);
        rvConsumableList.setVisibility(View.GONE);
        cvConsumableHeader.setVisibility(View.GONE);
        clConsumableStatusButton.setVisibility(View.GONE);
        apiService.getCampConsumptionDetails_ForAppForIntApproval(campId).enqueue(new Callback<ConsumableDetailsForApprovalModel>() {
            @Override
            public void onResponse(Call<ConsumableDetailsForApprovalModel> call, Response<ConsumableDetailsForApprovalModel> response) {
                consumablesprogressBar.setVisibility(View.GONE);
                consumableDetailsForApprovalModel = response.body();
                if (response.isSuccessful()) {
                    consumableListModel = response.body();
                    if (response.body().getStatus().equalsIgnoreCase("success")) {
                        if (response.body().getOutput().size() > 0) {
//                            rvConsumableList.setAdapter(new ConsumableListAdapter(response.body().getOutput(), 1));
                            rvConsumableList.setVisibility(View.VISIBLE);
                            cvConsumableHeader.setVisibility(View.VISIBLE);
                            clConsumableStatusButton.setVisibility(View.VISIBLE);
                            tvError.setVisibility(View.GONE);

                            getLabOnDistrict(distLgdCode);

                        }

                    } else {
                        tvError.setText("Consumables not found");
                        tvError.setVisibility(View.VISIBLE);


                    }
                }
            }

            @Override
            public void onFailure(Call<ConsumableDetailsForApprovalModel> call, Throwable t) {
                consumablesprogressBar.setVisibility(View.GONE);
                tvError.setText("Server not responding");
                tvError.setVisibility(View.VISIBLE);
            }
        });
    }



    void getLabOnDistrict(int distLgdCode) {

        apiService.getLabOnDistrict(distLgdCode).enqueue(new Callback<GetLabOnDistrictListModel>() {
            @Override
            public void onResponse(Call<GetLabOnDistrictListModel> call, Response<GetLabOnDistrictListModel> response) {
                Log.d(TAG, "onResponse: " + response.body().getStatus());

                try {
                    if (response.isSuccessful()) {
                        if (response.body().getOutput() != null && response.body().getOutput().size() > 0) {
                            getConsumablesDetailsFromMediProcs("5001");
                        } else {
                            Utilities.showAlertDialog(mContext, "Lab not found", "Lab not mapped for this district", false, "Okay", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ((CampUpdateActivity) mContext).finish();
                                    dialogInterface.dismiss();
                                }
                            });
                        }
                    } else {
                        Utilities.showToastMessage("Lab Not Found", mContext, false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<GetLabOnDistrictListModel> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
                Utilities.showToastMessage("Lab Not Found " + t.getMessage(), mContext, false);

            }

        });

    }


    void getConsumablesDetailsFromMediProcs(String labcode) {
        progressBar.setVisibility(View.VISIBLE);
        rvConsumableList.setVisibility(View.GONE);
        mediProcAPI.getLabProductStockListByLab(labcode).enqueue(new Callback<ProductStockListModel>() {
            @Override
            public void onResponse(Call<ProductStockListModel> call, Response<ProductStockListModel> response) {
                progressBar.setVisibility(View.GONE);
                rvConsumableList.setVisibility(View.VISIBLE);
                Log.i(TAG, "onResponse: " + response);
                if (response.isSuccessful()) {
                    productStockListModel = response.body();
                    if (productStockListModel.getStatus().equalsIgnoreCase("success")) {
                        if (consumableListModel.getOutput().size() > 0) {
                            for (ConsumableDetailsForApprovalModel.Output c :
                                    consumableListModel.getOutput()) {
                                for (ProductStockListModel.Output p :
                                        productStockListModel.getOutput()) {
                                    if (Objects.equals(c.getProductid(), p.getProductId())) {
                                        c.setAvailabelstock(p.getAvailabelstock());
                                    }
                                }
                            }
                            Collections.sort(consumableListModel.getOutput(), (o1, o2) -> o1.getProductName().compareTo(o2.getProductName()));
                            consumableListAdapter = new ConsumableListAdapter(consumableListModel.getOutput(), 1);
                            rvConsumableList.setAdapter(consumableListAdapter);
                        } else {
                            rvConsumableList.setVisibility(View.GONE);
                            tvError.setVisibility(View.VISIBLE);
                        }
                    } else {
                        rvConsumableList.setVisibility(View.GONE);
                        tvError.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ProductStockListModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                tvError.setVisibility(View.VISIBLE);

                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }


    void getResourcesForApproval() {
        resourceProgressBar.setVisibility(View.VISIBLE);
        rvDesignation.setVisibility(View.GONE);
        cvResourceHeader.setVisibility(View.GONE);
        clResourceStatusButton.setVisibility(View.GONE);
        apiService.getCampResourceDetails_ForAppForIntApproval(campId).enqueue(new Callback<ResourceDetailsForApprovalModel>() {
            @Override
            public void onResponse(Call<ResourceDetailsForApprovalModel> call, Response<ResourceDetailsForApprovalModel> response) {
                resourceProgressBar.setVisibility(View.GONE);
                resourceDetailsForApprovalModel = response.body();
                Log.i(TAG, "getCampResourceDetails onResponse: " + new Gson().toJson(resourceDetailsForApprovalModel));
                if (response.isSuccessful()) {
                    designationListModel = response.body();
                    if (designationListModel.getStatus().equalsIgnoreCase("success")) {
                        if (designationListModel.getOutput().size() > 0) {
                            rvDesignation.setVisibility(View.VISIBLE);
                            cvResourceHeader.setVisibility(View.VISIBLE);
                            clResourceStatusButton.setVisibility(View.VISIBLE);
                            tvResourceError.setVisibility(View.GONE);

                            boolean isPEExists = false;
                            boolean isBArExists = false;
                            boolean isrAudioExists = false;
                            boolean isLFTExists = false;
                            boolean isREGExists = false;
                            boolean isVIExists = false;
                            boolean isBASICExists = false;


//                            if (isPartnerCamp && campStatus != 1 && !campType.equalsIgnoreCase("3")) {
                            for (ResourceDetailsForApprovalModel.Output o :
                                    designationListModel.getOutput()) {

                                if (o.getTestId() == 3) {
                                    isPEExists = true;
                                }

                                if (o.getTestId() == 7) {
                                    isBArExists = true;
                                }

                                if (o.getTestId() == 5) {
                                    isrAudioExists = true;
                                }

                                if (o.getTestId() == 4) {
                                    isLFTExists = true;
                                }

                                if (o.getTestId() == 1) {
                                    isREGExists = true;
                                }

                                if (o.getTestId() == 6) {
                                    isVIExists = true;
                                }

                                if (o.getTestId() == 2) {
                                    isBASICExists = true;
                                }

                            }

                            if (!isPEExists) {
                                ResourceDetailsForApprovalModel resourceDetailsForApprovalModel1 = new ResourceDetailsForApprovalModel();
                                designationListModel.getOutput().add(designationListModel.getOutput().size() - 1, resourceDetailsForApprovalModel1.new Output(designationListModel.getOutput().get(0).getCampId(), 3, designationListModel.getOutput().get(0).getDesgName(), 0, "", "", 0, 0, "Physical Examination", new ArrayList<>()));
                            }
                            if (!isBArExists) {
                                ResourceDetailsForApprovalModel resourceDetailsForApprovalModel1 = new ResourceDetailsForApprovalModel();
                                designationListModel.getOutput().add(designationListModel.getOutput().size() - 1, resourceDetailsForApprovalModel1.new Output(designationListModel.getOutput().get(0).getCampId(), 7, designationListModel.getOutput().get(0).getDesgName(), 0, "", "", 0, 0, "Barcode", new ArrayList<>()));
                            }
                            if (!isrAudioExists) {
                                ResourceDetailsForApprovalModel resourceDetailsForApprovalModel1 = new ResourceDetailsForApprovalModel();
                                designationListModel.getOutput().add(designationListModel.getOutput().size() - 1, resourceDetailsForApprovalModel1.new Output(designationListModel.getOutput().get(0).getCampId(), 5, designationListModel.getOutput().get(0).getDesgName(), 0, "", "", 0, 0, "Audio Screening Test", new ArrayList<>()));
                            }
                            if (!isLFTExists) {
                                ResourceDetailsForApprovalModel resourceDetailsForApprovalModel1 = new ResourceDetailsForApprovalModel();
                                designationListModel.getOutput().add(designationListModel.getOutput().size() - 1, resourceDetailsForApprovalModel1.new Output(designationListModel.getOutput().get(0).getCampId(), 4, designationListModel.getOutput().get(0).getDesgName(), 0, "", "", 0, 0, "Lung Functioin Test", new ArrayList<>()));
                            }
                            if (!isREGExists) {
                                ResourceDetailsForApprovalModel resourceDetailsForApprovalModel1 = new ResourceDetailsForApprovalModel();
                                designationListModel.getOutput().add(designationListModel.getOutput().size() - 1, resourceDetailsForApprovalModel1.new Output(designationListModel.getOutput().get(0).getCampId(), 1, designationListModel.getOutput().get(0).getDesgName(), 0, "", "", 0, 0, "Registration", new ArrayList<>()));
                            }
                            if (!isVIExists) {
                                ResourceDetailsForApprovalModel resourceDetailsForApprovalModel1 = new ResourceDetailsForApprovalModel();
                                designationListModel.getOutput().add(designationListModel.getOutput().size() - 1, resourceDetailsForApprovalModel1.new Output(designationListModel.getOutput().get(0).getCampId(), 6, designationListModel.getOutput().get(0).getDesgName(), 0, "", "", 0, 0, "Vision Screening", new ArrayList<>()));
                            }
                            if (!isBASICExists) {
                                ResourceDetailsForApprovalModel resourceDetailsForApprovalModel1 = new ResourceDetailsForApprovalModel();
                                designationListModel.getOutput().add(designationListModel.getOutput().size() - 1, resourceDetailsForApprovalModel1.new Output(designationListModel.getOutput().get(0).getCampId(), 2, designationListModel.getOutput().get(0).getDesgName(), 0, "", "", 0, 0, "Basic Details", new ArrayList<>()));
                            }

//                            }
//                            Collections.sort(designationListModel.getOutput(), (o1, o2) -> o1.getResourceName().compareTo(o2.getResourceName()));
                            resourceListAdapter = new ResourceListForApprovalAdapter(designationListModel.getOutput(), CampUpdateActivity.this);
                            rvDesignation.setAdapter(resourceListAdapter);

                        } else {
                            tvResourceError.setVisibility(View.VISIBLE);
                        }
                    } else {
                        tvResourceError.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResourceDetailsForApprovalModel> call, Throwable t) {
                resourceProgressBar.setVisibility(View.GONE);
                tvResourceError.setVisibility(View.VISIBLE);

                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    /**
     * @param type   0=Device,1=Consumable
     * @param status Approve=1,Reject=2,Hold=3
     */
    void remarkDialog(int type, int status) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View dialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_with_edittext, null, false);
        builder.setTitle("Remark");
        builder.setCancelable(false);
        builder.setView(dialogView);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        Button btnSubmit = dialogView.findViewById(R.id.btnSubmit);
        TextInputEditText edtRemark = dialogView.findViewById(R.id.edtReason);
        TextInputLayout remarkLayout = dialogView.findViewById(R.id.textInputLayout);

        AlertDialog alertDialog = builder.create();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!edtRemark.getText().toString().isEmpty()) {
                    remarkLayout.setErrorEnabled(false);
                    if (type == 0) {
                        deviceRemark = edtRemark.getText().toString();
                        isDeviceApprove = status;
                    } else if (type == 1) {
                        isConsumableApprove = status;
                        consumableRemark = edtRemark.getText().toString();
                    } else {
                        isResourceApprove = status;
                        resourceRemark = edtRemark.getText().toString();
                    }
                } else {
                    remarkLayout.setErrorEnabled(true);
                    remarkLayout.setError("Please enter remark");
                }

                alertDialog.dismiss();

            }
        });

        alertDialog.show();

    }


    @Override
    public void onSiteSelected(SiteListModel.Output output) {

    }

    @Override
    public void onDeviceSelected(DeviceListModel.Output output) {
    }

    @Override
    public void onApproveDeviceSelected(DeviceDetailsForApprovalModel.Output output) {
        getSubDevices(output);
    }

    void getSubDevices(DeviceDetailsForApprovalModel.Output device) {
        if (device.getSubDevices() == null) {

            apiService.getApproveSubDeviceListForCampAllo(device.getDevicesId(), campDate, String.valueOf(campId)).enqueue(new Callback<AllocatedSubDeviceListModel>() {
                @Override
                public void onResponse(Call<AllocatedSubDeviceListModel> call, Response<AllocatedSubDeviceListModel> response) {

                    Log.i(TAG, "onResponse: " + response);
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
                public void onFailure(Call<AllocatedSubDeviceListModel> call, Throwable t) {

                    Log.e(TAG, "onFailure: " + t.getMessage());
                }

            });
        } else {
            subDeviceListModel.setOutput(device.getSubDevices());
            showSubDeviceDialog(device);

        }
    }


    private void showSubDeviceDialog(DeviceDetailsForApprovalModel.Output device) {
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
            ArrayList<AllocatedSubDeviceListModel.Output> selSubDeviceListModel = new ArrayList<>();
            selSubDeviceListModel.addAll(subDeviceListModel.getOutput());

            for (AllocatedSubDeviceListModel.Output output : subDeviceListModel.getOutput()) {

                if (output.isChecked() || output.getDeviceStatus().equalsIgnoreCase("Selected")) {
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


            if (checkedTestCount < device.getDevicecount()) {
                Utilities.showToastMessage("Please select " + device.getDevicecount() + " for this camp", mContext, false);
            }
            device.setSubDevices(selSubDeviceListModel);
            rvDeviceList.setAdapter(deviceListAdapter);
//            deviceListAdapter.notifyDataSetChanged();


        });

        alertDialog.show();

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

            if (subDeviceListModel.getOutput().get(position).isChecked() || subDeviceListModel.getOutput().get(position).getDeviceStatus().equalsIgnoreCase("Selected")) {
                holder.cb_select.setChecked(true);
            }

            holder.cb_select.setOnCheckedChangeListener((buttonView, isChecked) -> {
                subDeviceListModel.getOutput().get(position).setChecked(isChecked);
                subDeviceListModel.getOutput().get(position).setDeviceStatus(isChecked ? "Selected" : "Not Selected");
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


    /**
     * For Resources
     */
    void getResources(ResourceDetailsForApprovalModel.Output designation) {

        if (designation.getResources() == null || designation.getResources().size() == 0) {
            progressDialog = Utilities.ProgressDialog(mContext, "Getting resources..").setCancelable(false).create();
            progressDialog.show();
            apiService.getApproveResourcelstForUpdate(designation.getTestId(), campDate, String.valueOf(campId),labCode, distLgdCode).enqueue(new Callback<AllocatedResourceListModel>() {
                @Override
                public void onResponse(Call<AllocatedResourceListModel> call, Response<AllocatedResourceListModel> response) {
                    progressDialog.dismiss();
                    Log.i(TAG, "onResponse: " + response);
                    if (response.isSuccessful()) {
                        allocatedResourceListModel = response.body();
                        if (allocatedResourceListModel.getStatus().equalsIgnoreCase("success")) {
                            if (allocatedResourceListModel.getOutput().size() > 0) {
                                Collections.sort(allocatedResourceListModel.getOutput(), (o1, o2) -> o1.getResourceName().compareTo(o2.getResourceName()));
                                showResourcesDialog(designation);
                            }
                        } else {
                            Utilities.showToastMessage("Resources not available", mContext, false);
                        }
                    }
                }

                @Override
                public void onFailure(Call<AllocatedResourceListModel> call, Throwable t) {
                    progressDialog.dismiss();

                    Log.e(TAG, "onFailure: " + t.getMessage());
                }
            });
        } else {
            allocatedResourceListModel.setOutput(designation.getResources());
            showResourcesDialog(designation);

        }
    }


    private void showResourcesDialog(ResourceDetailsForApprovalModel.Output test) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.dialog_check_list_search, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(view);
        builder.setTitle("Select Resources");
//        builder.setCancelable(false);

        selectedIndex = -1;
        RecyclerView rv_checklist = view.findViewById(R.id.rv_checklist);
        SearchView edtSearchName = view.findViewById(R.id.edtSearchName);
        rv_checklist.setLayoutManager(new LinearLayoutManager(mContext));
        List<AllocatedResourceListModel.Output> resourceList = allocatedResourceListModel.getOutput();
        filteredResourceModel = new ArrayList<>();
        filteredResourceModel.addAll(allocatedResourceListModel.getOutput());
        rv_checklist.setAdapter(new ResourceListAdapter(filteredResourceModel));

        edtSearchName.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.isEmpty()) {
                    filteredResourceModel.clear();
                    selectedIndex = -1;
                    if (allocatedResourceListModel == null || allocatedResourceListModel.getOutput().size() == 0) {
                        return false;
                    }
                    for (AllocatedResourceListModel.Output output : allocatedResourceListModel.getOutput()
                    ) {
                        if (output.getResourceName().toLowerCase(Locale.ENGLISH).startsWith(query.toLowerCase(Locale.ENGLISH))) {
                            filteredResourceModel.add(output);
                        }
                    }

                    if (filteredResourceModel.size() > 0) {
                        rv_checklist.setAdapter(new ResourceListAdapter(filteredResourceModel));
                    }
                } else {
                    rv_checklist.setAdapter(new ResourceListAdapter(resourceList));

                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!newText.isEmpty()) {
                    selectedIndex = -1;
                    filteredResourceModel.clear();
                    if (allocatedResourceListModel == null || allocatedResourceListModel.getOutput().size() == 0) {
                        return false;
                    }

                    for (AllocatedResourceListModel.Output output : allocatedResourceListModel.getOutput()
                    ) {
                        if (output.getResourceName().toLowerCase(Locale.ENGLISH).startsWith(newText.toLowerCase(Locale.ENGLISH))) {
                            filteredResourceModel.add(output);
                        }
                    }

                    if (filteredResourceModel.size() > 0) {
                        rv_checklist.setAdapter(new ResourceListAdapter(filteredResourceModel));
                    }
                } else {
                    rv_checklist.setAdapter(new ResourceListAdapter(resourceList));

                }

                return true;
            }
        });


        builder.setNegativeButton("Remove", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                {
                    allSelResourceModel.clear();

                    String selectedResource = String.valueOf(0);
                    int checkedTestCount = 0;
                    StringBuilder selectedSubCategories = new StringBuilder();
                    ArrayList<AllocatedResourceListModel.Output> selResourceListModel = new ArrayList<>();
                    selResourceListModel.addAll(filteredResourceModel);

                    for (AllocatedResourceListModel.Output output : filteredResourceModel) {

                        if (output.isChecked() || output.getResStatus().equalsIgnoreCase("Selected")) {
                            checkedTestCount = checkedTestCount + 1;

//                            selectedResource += output.getResStatus().equalsIgnoreCase("Selected");
//
//                            if (selectedResource.equalsIgnoreCase("1")){
//
//                                Utilities.showAlertDialog(mContext,"Alert","Atleast one resource is needed for test you need to add resource then remove resouce",false);
////                                  //  Utilities.showToastMessage("Please select at least one resource", mContext, false);
//                                    return;
//                            }



                            if (!allSelResourceModel.contains(output)) {
                                allSelResourceModel.add(output);


//                                if (output.getResStatus().equalsIgnoreCase("Selected")){
//                                    selectedResorce +=  1;
//
//
//                                if (selectedResorce == 1) {
//
//                                    Utilities.showAlertDialog(mContext,"Alert","Atleast one resource is needed for test you need to add resource then remove resouce",false);
//                                  //  Utilities.showToastMessage("Please select at least one resource", mContext, false);
//                                    return;
//                                }
//                                }



                            }
                        } else {
                            if (allSelResourceModel.contains(output)) {
                                allSelResourceModel.remove(output);
                            }
                        }
                    }



                    for (AllocatedResourceListModel.Output output : filteredResourceModel) {

                        if (output.getResStatus().equalsIgnoreCase("Selected") ){
                            selectedResorce +=  1;


                            if (selectedResorce == 1) {

                                Utilities.showAlertDialog(mContext,"Alert","Atleast one resource is needed for test you need to add resource then remove resouce",false);
                                //  Utilities.showToastMessage("Please select at least one resource", mContext, false);
                                return;
                            }
                        }
                    }

                    if (checkedTestCount == 0) {
//                       Utilities.showAlertDialog(mContext,"Alert","Atleast one resource is needed for test you need to add resource then remove resouce",false);
                        Utilities.showToastMessage("Please select at least one resource", mContext, false);
                      //  return;
                    }

                    test.setResources(selResourceListModel);
                    rvDesignation.setAdapter(resourceListAdapter);

//            deviceListAdapter.notifyDataSetChanged();

                    progressDialog = Utilities.ProgressDialog(mContext, "Getting resources..").setCancelable(false).create();
                    progressDialog.show();
                    apiService.removeCampMappingResources(allSelResourceModel.get(0).getUserid(), campId, allSelResourceModel.get(0).getTestId(), userResponseModel.getEmpCode()).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            progressDialog.dismiss();
                            try {
                                if (response.isSuccessful()) {
                                    String res = response.body().string();
                                    Log.d(TAG, "onResponse: " + res);
                                    JSONObject jsonObject = new JSONObject(res);
                                    if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                                        Utilities.showAlertDialog(mContext, "Success", "Resource remove successfully", true);
                                        getResourcesForApproval();
                                    }else {
                                        Utilities.showAlertDialog(mContext, "Fail", "Resource can not be removed as test has been performed by resource.", false);
                                        getResourcesForApproval();
                                    }
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            progressDialog.dismiss();
                            Utilities.showAlertDialog(mContext, "Error", "Unable to remove resource", false);
                            Log.e(TAG, "onFailure: ", t);
                        }
                    });

                }

                dialogInterface.dismiss();
            }
        });

        builder.setPositiveButton("Add", (dialog, which) -> {
            allSelResourceModel.clear();

            int checkedTestCount = 0;
            StringBuilder selectedSubCategories = new StringBuilder();
            ArrayList<AllocatedResourceListModel.Output> selResourceListModel = new ArrayList<>();
            selResourceListModel.addAll(filteredResourceModel);

            for (AllocatedResourceListModel.Output output : filteredResourceModel) {

                if (output.isChecked() || output.getResStatus().equalsIgnoreCase("Selected")) {
                    checkedTestCount = checkedTestCount + 1;
                    if (!allSelResourceModel.contains(output)) {
                        allSelResourceModel.add(output);
                    }
                } else {
                    if (allSelResourceModel.contains(output)) {
                        allSelResourceModel.remove(output);
                    }
                }
            }

            if (checkedTestCount == 0) {
                Utilities.showToastMessage("Please select at least one resource", mContext, false);
            }

            test.setResources(selResourceListModel);
            rvDesignation.setAdapter(resourceListAdapter);

//            deviceListAdapter.notifyDataSetChanged();
            progressDialog = Utilities.ProgressDialog(mContext, "Getting resources..").setCancelable(false).create();
            progressDialog.show();
            apiService.updateCampMappingResources(allSelResourceModel.get(0).getUserid(), campId, allSelResourceModel.get(0).getTestId(), userResponseModel.getEmpCode()).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    progressDialog.dismiss();
                    try {
                        if (response.isSuccessful()) {
                            String res = response.body().string();
                            Log.d(TAG, "onResponse: " + res);
                            JSONObject jsonObject = new JSONObject(res);
                            if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                                Utilities.showAlertDialog(mContext, "Success", "या कॅम्पसाठी Resource Mapping यशस्वीरीत्या पूर्ण झाले आहे. कृपया लक्षात घ्या की एकदा patient registration झाल्यानंतर या कॅम्पमध्ये कोणताही नवीन phlebotomist/doctor जोडता किंवा हटवता येणार नाही.", true);
                                getResourcesForApproval();
                            }else {

                                Utilities.showAlertDialog(mContext, "Fail", "Resource already exists.", false);
                                getResourcesForApproval();

                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    progressDialog.dismiss();
                    Utilities.showAlertDialog(mContext, "Error", "Unable to update resource", false);
                    Log.e(TAG, "onFailure: ", t);

                }
            });

        });

        builder.create().show();
    }


    @Override
    public void onDesignationSelected(ResourceDetailsForApprovalModel.Output output) {
        getResources(output);

    }

    int selectedIndex = -1;

    private class ResourceListAdapter extends RecyclerView.Adapter<ResourceListAdapter.MyViewHolder> {

        List<AllocatedResourceListModel.Output> list;

        public ResourceListAdapter(List<AllocatedResourceListModel.Output> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.list_row_resource_update, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int pos) {
            final int position = holder.getAbsoluteAdapterPosition();

            holder.cb_select.setText(list.get(position).getResourceName());

            if (list.get(position).isChecked() || list.get(position).getResStatus().equalsIgnoreCase("Selected")) {
                selectedIndex = position;
//                holder.cb_select.setChecked(true);
            } else {
//                holder.cb_select.setChecked(false);

            }
            holder.cb_select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedIndex = position;
                    for (AllocatedResourceListModel.Output o : list
                    ) {
                        o.setChecked(false);
                        o.setResStatus("Not Selected");
                    }
                    list.get(position).setChecked(true);
                    list.get(position).setResStatus("Selected");
                    notifyDataSetChanged();
                }

            });

            if (selectedIndex == position) {
                holder.llMain.setBackground(getResources().getDrawable(R.drawable.border_layout_button_blue));
                holder.cb_select.setTextColor(getResources().getColor(R.color.white));
                holder.cb_select.setChecked(true);
            } else {
                holder.llMain.setBackground(null);
                holder.cb_select.setChecked(false);
                holder.cb_select.setTextColor(getResources().getColor(R.color.black));
            }


//            holder.cb_select.setOnCheckedChangeListener((buttonView, isChecked) -> {
//                if(isChecked){
//                    selectedIndex=position;
//                    notifyItemChanged(position);
//                }else{
//
//                }
//                allocatedResourceListModel.getOutput().get(position).setChecked(isChecked);
//                allocatedResourceListModel.getOutput().get(position).setResStatus(isChecked ? "Selected" : "Not Selected");
//            });
        }

        @Override
        public int getItemCount() {
            return list == null ? 0 : list.size();
        }


        public class MyViewHolder extends RecyclerView.ViewHolder {

            private CheckBox cb_select;
            LinearLayout llMain;

            public MyViewHolder(@NonNull View view) {
                super(view);
                cb_select = view.findViewById(R.id.cb_select);
                llMain = view.findViewById(R.id.llMain);
            }
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }
    }
}